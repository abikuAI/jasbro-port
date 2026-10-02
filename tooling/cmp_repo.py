import os, sys
wf = sys.argv[1]
repo = os.path.join(wf, "repo", "src", "main", "java", "jasbro")
dec  = os.path.join(wf, "decompiled", "source", "jasbro")

def collect(root):
    out = {}
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith(".java"):
                rel = os.path.relpath(os.path.join(r,f), root).replace("\\","/")
                out[rel] = os.path.join(r,f)
    return out

R, D = collect(repo), collect(dec)
print("repo (GitHub, 2015-04) java files :", len(R))
print("decompiled (shipped R0.1.2) files :", len(D))
print()
only_repo = sorted(set(R) - set(D))
only_dec  = sorted(set(D) - set(R))
shared    = sorted(set(R) & set(D))
print(f"SHARED          : {len(shared)}")
print(f"ONLY in shipped : {len(only_dec)}")
print(f"ONLY in repo    : {len(only_repo)}")
print()
def group(files):
    from collections import Counter
    c = Counter()
    for f in files:
        parts = f.split("/")
        c["/".join(parts[:3]) if len(parts)>=3 else "/".join(parts[:2])] += 1
    return c
print("=== ONLY in shipped game — by package (features ADDED since the fork) ===")
for k,v in group(only_dec).most_common(): print(f"  {v:3d}  {k}")
print()
print("=== ONLY in repo — by package (features REMOVED/changed since the fork) ===")
for k,v in group(only_repo).most_common(): print(f"  {v:3d}  {k}")
if not only_repo: print("  (none)")
# write full lists
with open(os.path.join(wf,"build-out","logs","repo-vs-shipped.txt"),"w",encoding="utf-8") as fh:
    fh.write("=== ONLY IN SHIPPED ===\n");  [fh.write(x+"\n") for x in only_dec]
    fh.write("\n=== ONLY IN REPO ===\n");   [fh.write(x+"\n") for x in only_repo]
    fh.write("\n=== SHARED ===\n");         [fh.write(x+"\n") for x in shared]
print()
print("full lists -> build-out/logs/repo-vs-shipped.txt")
