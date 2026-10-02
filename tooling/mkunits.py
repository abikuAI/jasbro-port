import os, sys, json, math
wf = sys.argv[1]
dec  = os.path.join(wf,"decompiled","source","jasbro")
repo = os.path.join(wf,"repo","src","main","java","jasbro")

files = []
for r,_,fs in os.walk(dec):
    for f in fs:
        if f.endswith(".java"):
            rel = os.path.relpath(os.path.join(r,f), dec).replace("\\","/")
            files.append(rel)

# group to <= MAXFILES per unit, never splitting a leaf directory
from collections import defaultdict
by_dir = defaultdict(list)
for rel in files:
    by_dir[os.path.dirname(rel)].append(rel)

MAXF = 16
units = []
def emit(name, items):
    units.append({"unit": name, "files": sorted(items)})

for d in sorted(by_dir):
    fs = sorted(by_dir[d])
    parent = os.path.dirname(d) or "jasbro"
    if len(fs) <= MAXF:
        emit(d or "jasbro", fs)
    else:
        # split alphabetically into chunks, keeping files that share a prefix together
        base = os.path.basename(d) or "root"
        n = math.ceil(len(fs)/MAXF)
        for i in range(n):
            chunk = fs[i::n]          # round-robin keeps siblings spread but caps size
            emit(f"{d}#part{i+1}", chunk)

# repo twin map
def has_twin(rel):
    return os.path.exists(os.path.join(repo, rel.replace("/", os.sep)))

tot = 0
for u in units:
    tot += len(u["files"])
    u["repoTwinCount"] = sum(1 for f in u["files"] if has_twin(f))

print("units:", len(units), " total files:", tot)
print()
for u in units:
    print(f"  {len(u['files']):3d} files ({u['repoTwinCount']:3d} w/ repo twin)  {u['unit']}")

with open(os.path.join(wf,"build-out","logs","audit-units.json"),"w",encoding="utf-8") as fh:
    json.dump(units, fh, indent=1)
print()
print("->", os.path.join(wf,"build-out","logs","audit-units.json"))
