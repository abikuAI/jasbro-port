import os, sys, json, math
wf = sys.argv[1]
dec  = os.path.join(wf,"decompiled","source","jasbro")
repo = os.path.join(wf,"repo","src","main","java","jasbro")

files = []
for r,_,fs in os.walk(dec):
    for f in fs:
        if f.endswith(".java"):
            files.append(os.path.relpath(os.path.join(r,f), dec).replace("\\","/"))

from collections import defaultdict
by_dir = defaultdict(list)
for rel in files: by_dir[os.path.dirname(rel)].append(rel)

MAXF = 16
units = []
def emit(name, items): units.append({"u": name, "files": sorted(items)})
for d in sorted(by_dir):
    fs = sorted(by_dir[d])
    if len(fs) <= MAXF:
        emit(d or ".", fs)
    else:
        # contiguous split by shared filename stem groups, to keep related classes together
        chunks = [fs[i*len(fs)//math.ceil(len(fs)/MAXF):(i+1)*len(fs)//math.ceil(len(fs)/MAXF)]
                  for i in range(math.ceil(len(fs)/MAXF))]
        for i,ch in enumerate(chunks):
            emit(f"{d}#{i+1}", ch)

def tier(u):
    n = u["u"]
    if "/util/eventEditor" in n or "/util/itemEditor" in n or "/util/enemyEditor" in n: return "C-editors"
    if n.startswith("gui/") or "/gui/" in n: return "B-gui"
    return "A-domain"

for u in units:
    u["tier"] = tier(u)
# repo twin presence
def twin(f):
    return os.path.exists(os.path.join(repo, f))
for u in units:
    u["twins"] = sum(1 for f in u["files"] if twin(f))

json.dump(units, open(os.path.join(wf,"build-out","logs","units-compact.json"),"w",encoding="utf-8"), separators=(",",":"))
from collections import Counter
c = Counter(u["tier"] for u in units)
fl = Counter()
for u in units: fl[u["tier"]] += len(u["files"])
print("units:", len(units))
for k in sorted(c): print(f"  {k:10s} {c[k]:3d} units  {fl[k]:4d} files")
print()
print("total files covered:", sum(len(u['files']) for u in units))
print("json bytes:", os.path.getsize(os.path.join(wf,"build-out","logs","units-compact.json")))
