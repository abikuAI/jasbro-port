import re, sys, os
src = os.path.join(sys.argv[1], "decompiled", "source", "jasbro", "game", "character", "specialization", "SpecializationType.java")
t = open(src, encoding="utf-8", errors="replace").read()
# body of the enum: from the first '{' after 'enum SpecializationType' to its matching '}'
i = t.index("enum SpecializationType")
b = t.index("{", i)
depth, j = 0, b
while j < len(t):
    if t[j] == "{": depth += 1
    elif t[j] == "}":
        depth -= 1
        if depth == 0: break
    j += 1
body = t[b+1:j]
consts = re.findall(r'(?:^|[\s;{])([A-Z][A-Z0-9_]*)\s*(?=[(,;])', body)
print("SpecializationType constants:", len(consts))
print(consts)
