import os, shutil, sys
wf = sys.argv[1]
vf  = os.path.join(wf, "decompiled", "vineflower")
cfr = os.path.join(wf, "decompiled", "cfr")
mg  = os.path.join(wf, "decompiled", "merged")

use_cfr = [
 "jasbro/game/character/activities/ActivityType",
 "jasbro/game/character/activities/sub/business/Bartend",
 "jasbro/game/character/activities/sub/business/Offerings",
 "jasbro/game/events/EventManager",
 "jasbro/game/events/business/BusinessCalculations",
 "jasbro/gui/pictures/MyGifImageObject",
 "jasbro/util/itemEditor/ItemEditorPanel",
]

if os.path.exists(mg): shutil.rmtree(mg)
shutil.copytree(vf, mg)

def allfiles(root):
    out = []
    for r, _, fs in os.walk(root):
        for f in fs:
            if f.endswith(".java"):
                out.append(os.path.relpath(os.path.join(r,f), root).replace("\\","/"))
    return out

report = []
for top in use_cfr:
    # remove every vineflower file belonging to this top-level class (incl. $inner)
    removed = []
    for rel in allfiles(mg):
        if rel[:-5].split("$")[0] == top:
            os.remove(os.path.join(mg, rel)); removed.append(rel)
    # copy CFR's version(s)
    added = []
    for rel in allfiles(cfr):
        if rel[:-5].split("$")[0] == top:
            dst = os.path.join(mg, rel)
            os.makedirs(os.path.dirname(dst), exist_ok=True)
            shutil.copy2(os.path.join(cfr, rel), dst); added.append(rel)
    report.append((top, removed, added))

print("=== merge report ===")
for top, rem, add in report:
    print(f"  {top}")
    print(f"      removed (vineflower): {rem}")
    print(f"      added   (cfr)       : {add}")
n = len(allfiles(mg))
print()
print("merged tree file count:", n)
