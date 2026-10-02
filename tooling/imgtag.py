import re, sys, os
base = os.path.join(sys.argv[1], "decompiled", "source", "jasbro")
def enum_consts(path, name):
    t = open(path, encoding="utf-8", errors="replace").read()
    i = t.index("enum " + name)
    b = t.index("{", i)
    depth, j = 0, b
    while j < len(t):
        if t[j] == "{": depth += 1
        elif t[j] == "}":
            depth -= 1
            if depth == 0: break
        j += 1
    body = t[b+1:j]
    return re.findall(r'(?:^|[\s;{])([A-Z][A-Z0-9_]*)\s*(?=[(,;])', body)

p = os.path.join(base, "gui", "pictures", "ImageTag.java")
c = enum_consts(p, "ImageTag")
print("ImageTag constants:", len(c))
print(c)
