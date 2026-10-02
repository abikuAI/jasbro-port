import zipfile, os, sys
wf = sys.argv[1]
jar = os.path.join(wf, "original", "JaSBro-reference.jar")
cls = os.path.join(wf, "build-out", "classes-main")

with zipfile.ZipFile(jar) as z:
    shipped = {n for n in z.namelist() if n.endswith(".class")}

mine = set()
for root, _, files in os.walk(cls):
    for f in files:
        if f.endswith(".class"):
            full = os.path.join(root, f)
            rel = os.path.relpath(full, cls).replace("\\", "/")
            mine.add(rel)

print("shipped jar classes :", len(shipped))
print("freshly compiled    :", len(mine))
print()
missing = sorted(shipped - mine)
extra   = sorted(mine - shipped)
print("IN JAR but NOT compiled:", len(missing))
for m in missing[:60]: print("   -", m)
print()
print("COMPILED but not in jar:", len(extra))
for e in extra[:60]: print("   +", e)
