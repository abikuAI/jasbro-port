import zipfile, os, sys
wf = sys.argv[1]
jar = os.path.join(wf, "original", "JaSBro-reference.jar")
cls = os.path.join(wf, "build-out", "classes-main")

def top(rel):
    base = rel[:-6]  # strip .class
    return base.split("$")[0] + ".class"

with zipfile.ZipFile(jar) as z:
    shipped = {n for n in z.namelist() if n.endswith(".class")}
mine = set()
for root, _, files in os.walk(cls):
    for f in files:
        if f.endswith(".class"):
            mine.add(os.path.relpath(os.path.join(root,f), cls).replace("\\","/"))

s_top, m_top = {top(x) for x in shipped}, {top(x) for x in mine}
print("TOP-LEVEL classes in shipped jar :", len(s_top))
print("TOP-LEVEL classes compiled       :", len(m_top))
miss = sorted(s_top - m_top)
print()
print(">>> TOP-LEVEL classes in shipped game but ABSENT from GitHub source:", len(miss))
for m in miss: print("   -", m)
