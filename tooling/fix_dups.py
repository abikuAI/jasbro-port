"""
Scope-aware rename of duplicate local variables in a decompiled Java file.

Why: Java forbids shadowing a local variable from an enclosing block within the
same method. Decompilers emit nested re-declarations of the same name, producing
"variable X is already defined in method Y" errors.

Fix: for each javac-flagged declaration, rename it (and exactly its own lexical
scope) to a unique name. Innermost-first ordering guarantees that a scope can only
contain uses belonging to its own declaration, because any deeper redeclaration of
the same name would itself have been flagged and already renamed.
"""
import re
import sys
import os

wf = sys.argv[1]
rel = "jasbro/game/character/activities/sub/business/Attend.java"
target = os.path.join(wf, "decompiled", "merged", rel)
logpath = os.path.join(wf, "build-out", "logs", "compile-merged.log")

src = open(target, encoding="utf-8", errors="replace").read()
lines = src.split("\n")

# ---------------------------------------------------------------- collect errors
scope_re = re.compile(r'Attend\.java:(\d+): error: variable (\S+) is already defined')
errs = set()
for m in scope_re.finditer(open(logpath, encoding="utf-8", errors="replace").read()):
    errs.add((int(m.group(1)), m.group(2)))
errs = sorted(errs)
print(f"flagged declarations: {len(errs)}")


def mask_line(line):
    """Blank out string literals and line/block comments so brace counting is safe."""
    out = list(line)
    i, n = 0, len(line)
    in_str = False
    while i < n:
        c = line[i]
        if in_str:
            if c == '\\':
                out[i] = ' '
                if i + 1 < n:
                    out[i + 1] = ' '
                i += 2
                continue
            if c == '"':
                in_str = False
            out[i] = ' '
            i += 1
            continue
        if c == '"':
            in_str = True
            out[i] = ' '
            i += 1
            continue
        if c == '/' and i + 1 < n and line[i + 1] == '/':
            for j in range(i, n):
                out[j] = ' '
            break
        i += 1
    return ''.join(out)


masked = [mask_line(l) for l in lines]

# ------------------------------------------------------- brace map + block spans
# brace_stack holds 1-based line numbers of currently-open '{'
brace_stack = []
open_line_at = [None] * (len(lines) + 1)   # innermost open block at start of line i
match_close = {}                            # open line -> close line

for idx, ml in enumerate(masked):
    ln = idx + 1
    open_line_at[ln] = brace_stack[-1] if brace_stack else None
    for ch in ml:
        if ch == '{':
            brace_stack.append(ln)
        elif ch == '}':
            if brace_stack:
                o = brace_stack.pop()
                match_close[o] = ln

# --------------------------------------------------------------- apply renames
# innermost (deepest) first so already-renamed inner scopes are left untouched
def depth_of(ln):
    """Nesting depth of the block the declaration sits in."""
    d = 0
    o = open_line_at[ln]
    while o is not None:
        d += 1
        o = open_line_at[o]
    return d

errs.sort(key=lambda t: -depth_of(t[0]))

renamed = 0
for decl_line, name in errs:
    newname = f"{name}${decl_line}"
    o = open_line_at[decl_line]
    if o is None:
        end = len(lines)
    else:
        end = match_close.get(o, len(lines))

    # whole-word name, not preceded by '.' (avoids field/method access)
    pat = re.compile(r'(?<![\w$.])' + re.escape(name) + r'(?![\w$])')
    for i in range(decl_line - 1, min(end, len(lines))):
        if pat.search(masked[i]):
            # keep strings/comments untouched: only substitute on the raw line
            lines[i] = pat.sub(newname, lines[i])
    renamed += 1
    print(f"  L{decl_line:5d} depth={depth_of(decl_line)} scope->L{end:5d}  {name} -> {newname}")

out = "\n".join(lines)
open(target + ".dupfix.bak", "w", encoding="utf-8", newline="").write(src)
open(target, "w", encoding="utf-8", newline="").write(out)
print(f"\nrenamed {renamed} declarations; backup at {os.path.basename(target)}.dupfix.bak")
