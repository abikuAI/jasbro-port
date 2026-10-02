import os, sys, json
wf = sys.argv[1]
dec = os.path.join(wf, "decompiled", "source", "jasbro")
repo = os.path.join(wf, "repo", "src", "main", "java", "jasbro")

files = []
for r,_,fs in os.walk(dec):
    for f in fs:
        if f.endswith(".java"):
            rel = os.path.relpath(os.path.join(r,f), dec).replace("\\","/")
            n = sum(1 for _ in open(os.path.join(r,f), encoding="utf-8", errors="replace"))
            files.append((rel, n))
files.sort(key=lambda x: -x[1])

N = 14
units = [[] for _ in range(N)]
load  = [0]*N
for rel, n in files:
    i = load.index(min(load))     # balance by total lines
    units[i].append(rel); load[i] += n

out = []
for i, u in enumerate(units):
    out.append({
        "u": f"balanced-{i+1}",
        "files": sorted(u),
        "lines": load[i],
        "twins": sum(1 for f in u if os.path.exists(os.path.join(repo, f)))
    })
out.sort(key=lambda x: -x["lines"])
json.dump(out, open(os.path.join(wf,"build-out","logs","units-balanced.json"),"w",encoding="utf-8"), separators=(",",":"))
print(f"{len(files)} files -> {N} balanced units\n")
for u in out:
    print(f"  {len(u['files']):3d} files  {u['lines']:6d} lines  ({u['twins']:3d} twins)  {u['u']}")
    top = sorted(u["files"], key=lambda f: -sum(1 for _ in open(os.path.join(dec,f), encoding='utf-8', errors='replace')))[:3]
    print(f"        biggest: {', '.join(top)}")
print()
print("json bytes:", os.path.getsize(os.path.join(wf,"build-out","logs","units-balanced.json")))
