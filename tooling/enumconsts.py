"""Extract Java enum constant names correctly.

Naive regex extraction over-captures: enum constants can take arguments that
reference OTHER constants (e.g. `VAGINAL(FOREPLAY, ...)`), and those arguments
get picked up as if they were declarators. This walks the enum body tracking
parenthesis depth and only takes the leading identifier of each top-level
declarator.

Usage: python enumconsts.py <source_root> EnumName [more names...]
"""
import os
import re
import sys


def enum_body(text, name):
    m = re.search(r'\benum\s+' + re.escape(name) + r'\b', text)
    if not m:
        return None
    b = text.index("{", m.end())
    depth = 0
    i = b
    while i < len(text):
        if text[i] == "{":
            depth += 1
        elif text[i] == "}":
            depth -= 1
            if depth == 0:
                return text[b + 1:i]
        i += 1
    return None


def split_top_level(body):
    """Split the enum body on commas that are not nested in parens/braces."""
    parts = []
    cur = []
    depth = 0
    for ch in body:
        if ch in "({[":
            depth += 1
        elif ch in ")}]":
            depth -= 1
        if ch == "," and depth == 0:
            parts.append("".join(cur))
            cur = []
        elif ch == ";" and depth == 0:
            parts.append("".join(cur))
            cur = []
            break
        else:
            cur.append(ch)
    if "".join(cur).strip():
        parts.append("".join(cur))
    return parts


def constants(text, name):
    body = enum_body(text, name)
    if body is None:
        return None
    out = []
    for decl in split_top_level(body):
        d = decl.strip()
        if not d:
            continue
        mm = re.match(r'([A-Za-z_$][A-Za-z0-9_$]*)', d)
        if mm:
            out.append(mm.group(1))
    return out


if __name__ == "__main__":
    root = sys.argv[1]
    names = sys.argv[2:]
    # index every java file by simple class name
    index = {}
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith(".java"):
                index[f[:-5]] = os.path.join(r, f)
    for n in names:
        p = index.get(n)
        if not p:
            print(f"{n}: NOT FOUND")
            continue
        cs = constants(open(p, encoding="utf-8", errors="replace").read(), n)
        if cs is None:
            print(f"{n}: enum body not parsed")
            continue
        dupes = sorted({c for c in cs if cs.count(c) > 1})
        print(f"{n}: {len(cs)} constants, {len(set(cs))} distinct"
              + (f"  DUPLICATES: {dupes}" if dupes else ""))
        print("   " + ", ".join(cs))
        print()
