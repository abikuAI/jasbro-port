import zipfile, os, sys
wf = sys.argv[1]
jar = os.path.join(wf,"original","JaSBro-reference.jar")
cls = os.path.join(wf,"decompiled","classes-merged")
with zipfile.ZipFile(jar) as z:
    shipped = {n for n in z.namelist() if n.endswith(".class")}
mine = set()
for r,_,fs in os.walk(cls):
    for f in fs:
        if f.endswith(".class"):
            mine.add(os.path.relpath(os.path.join(r,f), cls).replace("\\","/"))
print("shipped classes :", len(shipped))
print("recompiled      :", len(mine))
miss = sorted(shipped - mine); extra = sorted(mine - shipped)
print()
print("in shipped but NOT recompiled:", len(miss))
for m in miss: print("   -", m)
print("recompiled but NOT in shipped:", len(extra))
for e in extra[:40]: print("   +", e)
