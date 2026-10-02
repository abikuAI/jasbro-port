"""
Restore per-case block scoping in a decompiled Java file.

The decompiler flattened `switch` bodies, dropping the braces that originally
delimited each case. As a result, case-level local variables from *different*
cases landed in one shared block and collide:

    case GROPE:
        Iterator i$x = ...;   // case-level
    case LOOK:
        Iterator i$x = ...;   // <-- "already defined in method perform()"

Wrapping each case body in braces restores the original scoping. Braces do not
affect fall-through (that is control flow, not scope), so semantics are preserved;
they only give each case its own local variable scope, which is what the bytecode
had.
"""
import re
import sys
import os

wf = sys.argv[1]
rel = "jasbro/game/character/activities/sub/business/Attend.java"
target = os.path.join(wf, "decompiled", "merged", rel)

src = open(target, encoding="utf-8", errors="replace").read()
lines = src.split("\n")


def mask_line(line):
    out = list(line)
    i, n, in_str = 0, len(line), False
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

# depth at the START of each 1-based line
depth_at = [0] * (len(lines) + 2)
depth = 0
for idx, ml in enumerate(masked):
    depth_at[idx + 1] = depth
    depth += ml.count('{') - ml.count('}')

# matching brace pairs: open line -> close line
match_close = {}
stack = []
for idx, ml in enumerate(masked):
    ln = idx + 1
    for ch in ml:
        if ch == '{':
            stack.append(ln)
        elif ch == '}' and stack:
            match_close[stack.pop()] = ln

# locate `switch (...) {` bodies
switches = []
for idx, ml in enumerate(masked):
    if not re.search(r'\bswitch\s*\(', ml):
        continue
    ln = idx + 1
    open_ln = None
    for j in range(idx, len(lines)):
        if '{' in masked[j]:
            open_ln = j + 1
            break
    if open_ln and open_ln in match_close:
        switches.append((open_ln, match_close[open_ln]))

print(f"switch bodies found: {len(switches)}")

case_re = re.compile(r'^\s*(case\s+[^:]+:|default\s*:)')
insert_after = {}   # line -> count of '{' to insert after it
insert_before = {}  # line -> count of '}' to insert before it

for body_open, body_close in switches:
    d = depth_at[body_open] + 1
    labels = [ln for ln in range(body_open + 1, body_close)
              if case_re.match(lines[ln - 1]) and depth_at[ln] == d]
    if len(labels) < 1:
        continue
    for i, ln in enumerate(labels):
        insert_after[ln] = insert_after.get(ln, 0) + 1
        boundary = labels[i + 1] if i + 1 < len(labels) else body_close
        insert_before[boundary] = insert_before.get(boundary, 0) + 1
    print(f"  switch L{body_open}-L{body_close}: {len(labels)} case labels braced")

# rebuild
out = []
for idx, line in enumerate(lines):
    ln = idx + 1
    for _ in range(insert_before.get(ln, 0)):
        out.append(" " * max(0, len(line) - len(line.lstrip())) + "}")
    out.append(line)
    for _ in range(insert_after.get(ln, 0)):
        out.append(" " * max(0, len(line) - len(line.lstrip())) + "{")

new = "\n".join(out)
open(target + ".casefix.bak", "w", encoding="utf-8", newline="").write(src)
open(target, "w", encoding="utf-8", newline="").write(new)
print(f"\nlines {len(lines)} -> {len(out)}")
