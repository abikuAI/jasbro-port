import re, os, sys
wf = sys.argv[1]
def failing(logpath, srcroot):
    txt = open(logpath, encoding="utf-8", errors="replace").read()
    files = set()
    for m in re.finditer(r'^(.*?\.java):\d+: error:', txt, re.M):
        p = m.group(1)
        rel = os.path.relpath(p, srcroot).replace("\\", "/")
        files.add(rel)
    return files

cfr_fail = failing(os.path.join(wf,"build-out","logs","compile-decompiled2.log"), os.path.join(wf,"decompiled","cfr"))
vf_fail  = failing(os.path.join(wf,"build-out","logs","compile-vf.log"),          os.path.join(wf,"decompiled","vineflower"))

def toplevel(rel):
    b = rel[:-5]
    return b.split("$")[0]

cfr_top = {toplevel(f) for f in cfr_fail}
vf_top  = {toplevel(f) for f in vf_fail}

print("CFR failing files (%d):" % len(cfr_fail))
for f in sorted(cfr_fail): print("   ", f)
print()
print("Vineflower failing files (%d):" % len(vf_fail))
for f in sorted(vf_fail): print("   ", f)
print()
print("=== top-level classes each decompiler BREAKS ===")
print("CFR       :", sorted(cfr_top))
print("Vineflower:", sorted(vf_top))
print()
print("=== broken by BOTH (need manual fix) ===", sorted(cfr_top & vf_top))
print("=== broken by CFR only -> use Vineflower ===", sorted(cfr_top - vf_top))
print("=== broken by Vineflower only -> use CFR ===", sorted(vf_top - cfr_top))
