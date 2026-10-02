import zipfile, os, sys, re
wf = sys.argv[1]
jar = os.path.join(wf, "original", "JaSBro-reference.jar")
dec = os.path.join(wf, "decompiled", "cfr")

with zipfile.ZipFile(jar) as z:
    names = [n for n in z.namelist() if n.endswith(".class")]
top = {n[:-6].split("$")[0] for n in names}

found = set()
for root, _, files in os.walk(dec):
    for f in files:
        if f.endswith(".java"):
            rel = os.path.relpath(os.path.join(root, f), dec).replace("\\", "/")
            found.add(rel[:-5])

print("top-level classes in jar :", len(top))
print("java files produced      :", len(found))
missing = sorted(top - found)
print("MISSING java files       :", len(missing))
for m in missing[:20]: print("   -", m)

# scan for CFR failure markers
markers = ["has failed to decompile", "Unable to fully structure code",
           "Could not decompile", "Exception while decompiling", "*** "]
hits = {}
for root, _, files in os.walk(dec):
    for f in files:
        if not f.endswith(".java"): continue
        p = os.path.join(root, f)
        try: txt = open(p, encoding="utf-8", errors="replace").read()
        except Exception: continue
        for m in markers:
            if m in txt:
                hits.setdefault(m, []).append(os.path.relpath(p, dec))

print()
print("=== decompiler failure markers ===")
if not hits:
    print("  NONE - clean decompilation across all 606 files")
for m, fl in sorted(hits.items()):
    print(f"  {m!r}: {len(fl)} file(s)")
    for x in fl[:12]: print("      -", x)
