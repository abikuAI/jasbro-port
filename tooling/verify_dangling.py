import os, re
root = r"C:\Games\Jasbro_Final"
print("Every reference to a map-like itemId across all shipped content:")
pat = re.compile(r'<itemId>(.*?)</itemId>', re.S)
allitems = set()
for r,_,fs in os.walk(os.path.join(root,"items")):
    for f in fs:
        if f.lower().endswith(".xml"): allitems.add(f[:-4])
ids = {}
for r,_,fs in os.walk(root):
    if "\\items" in r: continue
    for f in fs:
        if not f.lower().endswith(".xml"): continue
        p = os.path.join(r,f)
        try: t = open(p, encoding="utf-8", errors="replace").read()
        except: continue
        for m in pat.finditer(t):
            ids.setdefault(m.group(1), []).append(os.path.relpath(p, root))
print(f"  distinct itemIds referenced by content: {len(ids)}")
bad = {k:v for k,v in ids.items() if k not in allitems}
print(f"  DANGLING (referenced but no item file): {len(bad)}")
for k,v in sorted(bad.items()):
    print(f"    MISSING  {k!r}")
    for f in v[:4]: print(f"               <- {f}")
print()
print("  dungeon-ish items available:", sorted(n for n in allitems if "dungeon" in n.lower()))
