import re, sys, os
wf = sys.argv[1]
src = os.path.join(wf, "decompiled", "source")
trait_java = os.path.join(src, "jasbro", "game", "character", "traits", "Trait.java")
declared = set(re.findall(r'^\s*([A-Z][A-Z0-9_]*)\s*\(', open(trait_java, encoding="utf-8", errors="replace").read(), re.M))

for name, path in [("rooms.xml", r"C:\Games\Jasbro_Final\rooms.xml"),
                   ("fameUnlocks.xml", r"C:\Games\Jasbro_Final\fameUnlocks.xml")]:
    if not os.path.exists(path):
        print(f"--- {name}: not found ---"); continue
    txt = open(path, encoding="utf-8", errors="replace").read()
    used = set(re.findall(r'trait\s*=\s*"([^"]+)"', txt))
    missing = sorted(used - declared)
    print(f"--- {name} ---")
    print(f"  distinct trait= values : {len(used)}")
    print(f"  Trait enum declares    : {len(declared)}")
    print(f"  >>> MISSING from enum  : {len(missing)}")
    for m in missing: print("      -", m)
    print()

# also scan every content folder for trait= references
roots = {k: os.path.join(r"C:\Games\Jasbro_Final", k) for k in ("items","npcs","events","quests","characters")}
allmiss = {}
for k, root in roots.items():
    if not os.path.isdir(root): continue
    for r, _, fs in os.walk(root):
        for f in fs:
            if not f.lower().endswith((".xml",".properties")): continue
            p = os.path.join(r,f)
            try: t = open(p, encoding="utf-8", errors="replace").read()
            except Exception: continue
            for m in set(re.findall(r'trait\s*=\s*"([^"]+)"', t)) | set(re.findall(r'<trait>([A-Z0-9_]+)</trait>', t)):
                if m not in declared:
                    allmiss.setdefault(m, []).append(os.path.relpath(p, r"C:\Games\Jasbro_Final"))
print("=== trait names referenced ANYWHERE in game content but NOT in Trait enum ===")
if not allmiss:
    print("  none")
for k in sorted(allmiss):
    print(f"  {k}  ({len(allmiss[k])} file(s)): {allmiss[k][:3]}")
