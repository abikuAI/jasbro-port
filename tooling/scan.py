"""Static bug-pattern scan over the full decompiled JaSBro source tree.

Deterministic, complete-coverage pass: every .java file is examined for a fixed
set of mechanically-detectable defect patterns. Complements (and is more reliable
than) LLM review for the patterns it covers.

Usage: python scan.py <source_root> <out_dir>
"""
import os
import re
import sys
import json
import collections

SRC = sys.argv[1]
OUT = sys.argv[2]

files = []
for r, _, fs in os.walk(SRC):
    for f in fs:
        if f.endswith(".java"):
            files.append(os.path.join(r, f))


def rel(p):
    return os.path.relpath(p, SRC).replace("\\", "/")


def strip_comments(t):
    t = re.sub(r'/\*.*?\*/', lambda m: "\n" * m.group(0).count("\n"), t, flags=re.S)
    t = re.sub(r'//[^\n]*', '', t)
    return t


findings = collections.defaultdict(list)


def add(kind, file, line, text, extra=None):
    findings[kind].append({
        "file": file, "line": line,
        "text": text.strip()[:300],
        "extra": extra or {}
    })


# ---- per-file analysis -------------------------------------------------
for path in files:
    raw = open(path, encoding="utf-8", errors="replace").read()
    src = strip_comments(raw)
    lines = src.split("\n")
    rawlines = raw.split("\n")
    rf = rel(path)

    # 1. empty catch blocks: catch (...) { } possibly with whitespace/newlines
    for m in re.finditer(r'catch\s*\(([^)]*)\)\s*\{\s*\}', src, re.S):
        line = src[:m.start()].count("\n") + 1
        add("empty-catch", rf, line, m.group(0).replace("\n", " "),
            {"exception": m.group(1).strip()})

    # 2. catch blocks whose body is only a print/log (silently continues)
    for m in re.finditer(r'catch\s*\(([^)]*)\)\s*\{([^{}]*)\}', src, re.S):
        body = m.group(2).strip()
        if not body:
            continue
        if re.fullmatch(r'(?:e|ex|exception|throwable)\s*\.\s*printStackTrace\s*\(\s*\)\s*;', body):
            line = src[:m.start()].count("\n") + 1
            add("print-and-continue", rf, line, m.group(0).replace("\n", " "),
                {"exception": m.group(1).strip()})

    # 3. unguarded chained lookup: X.get(...).method() on one line
    for i, ln in enumerate(lines, 1):
        if re.search(r'\.get\([^)]*\)\s*\.\s*[A-Za-z_]', ln):
            add("chained-get", rf, i, ln)
        if re.search(r'getSelectedValue\s*\(\s*\)\s*\.', ln) or \
           re.search(r'getSelectedItem\s*\(\s*\)\s*\.', ln):
            add("unguarded-selection", rf, i, ln)

    # 4. enum valueOf sites (content/code drift risk)
    for i, ln in enumerate(lines, 1):
        for m in re.finditer(r'([A-Z][A-Za-z0-9_]*)\.valueOf\s*\(', ln):
            add("enum-valueof", rf, i, ln, {"enum": m.group(1)})

    # 5. resource acquisition not in try-with-resources
    for i, ln in enumerate(lines, 1):
        if re.search(r'new\s+(?:T?File(?:Input|Output)Stream|FileReader|FileWriter|BufferedReader|BufferedWriter|ZipFile)\s*\(', ln):
            window = "\n".join(lines[max(0, i - 6):i + 1])
            if "try (" not in window and "try(" not in window:
                add("resource-maybe-unclosed", rf, i, ln)

    # 6. integer division by a variable/intermediate (truncation + div-by-zero)
    for i, ln in enumerate(lines, 1):
        if re.search(r'[A-Za-z0-9_\)\]]\s*/\s*[A-Za-z_][A-Za-z0-9_]*', ln) and \
           not re.search(r'(?://|/\*)', ln) and not re.search(r'^\s*import|^\s*package', ln):
            if re.search(r'\b(int|long)\b', ln):
                add("integer-division", rf, i, ln)

    # 7. float/double equality
    for i, ln in enumerate(lines, 1):
        if re.search(r'\b(float|double)\b.*[=!]=', ln):
            add("float-equality", rf, i, ln)

    # 8. stdout/stderr
    for i, ln in enumerate(lines, 1):
        if re.search(r'System\s*\.\s*(out|err)\s*\.', ln):
            add("console-io", rf, i, ln)

    # 9. Thread.sleep
    for i, ln in enumerate(lines, 1):
        if "Thread.sleep" in ln:
            add("thread-sleep", rf, i, ln)

    # 10. string concatenation inside a loop (quadratic)
    for m in re.finditer(r'for\s*\([^)]*\)\s*\{(.{0,600}?)\}', src, re.S):
        body = m.group(1)
        if re.search(r'[A-Za-z_][A-Za-z0-9_]*\s*=\s*[A-Za-z_][A-Za-z0-9_]*\s*\+\s*[^;]*\+', body):
            line = src[:m.start()].count("\n") + 1
            add("string-concat-in-loop", rf, line, body.replace("\n", " ")[:200])

    # 11. static mutable (non-final) fields
    for i, ln in enumerate(lines, 1):
        if re.match(r'\s*(private|public|protected)?\s*static\s+(?!final)\S+\s+\w+\s*[;=]', ln) and \
           "static final" not in ln:
            add("static-mutable", rf, i, ln)

    # 12. XStream instantiation
    for i, ln in enumerate(lines, 1):
        if "new XStream(" in ln:
            add("xstream-new", rf, i, ln)

    # 13. synchronized methods
    for i, ln in enumerate(lines, 1):
        if re.search(r'\bsynchronized\b', ln):
            add("synchronized", rf, i, ln)

    # 14. printStackTrace anywhere (context for #2)
    for i, ln in enumerate(lines, 1):
        if "printStackTrace" in ln:
            add("printstacktrace", rf, i, ln)

    # 15. catching broad exceptions
    for i, ln in enumerate(lines, 1):
        if re.search(r'catch\s*\(\s*(Exception|Throwable|Error)\b', ln):
            add("catch-broad", rf, i, ln)

# ---- summary -----------------------------------------------------------
summary = {}
for k, v in sorted(findings.items(), key=lambda kv: -len(kv[1])):
    fileset = sorted(set(x["file"] for x in v))
    summary[k] = {"count": len(v), "files": len(fileset), "fileList": fileset}

os.makedirs(OUT, exist_ok=True)
json.dump({"summary": summary, "findings": findings},
          open(os.path.join(OUT, "static-scan.json"), "w", encoding="utf-8"), indent=1)

print(f"scanned {len(files)} java files\n")
print(f"{'pattern':28s} {'hits':>6s} {'files':>6s}")
print("-" * 44)
for k, s in summary.items():
    print(f"{k:28s} {s['count']:6d} {s['files']:6d}")
print(f"\n-> {os.path.join(OUT, 'static-scan.json')}")
