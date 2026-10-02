import zipfile, os, sys
wf = sys.argv[1]
jar = os.path.join(wf,"original","JaSBro-reference.jar")
dest = os.path.join(wf,"decompiled","procyon-in")
if os.path.exists(dest):
    import shutil; shutil.rmtree(dest)
targets = ["jasbro/game/character/activities/sub/business/Attend",
           "jasbro/util/itemEditor/ItemEditorPanel"]
with zipfile.ZipFile(jar) as z:
    n=0
    for nm in z.namelist():
        if not nm.endswith(".class"): continue
        top = nm[:-6].split("$")[0]
        if top in targets:
            out = os.path.join(dest, nm)
            os.makedirs(os.path.dirname(out), exist_ok=True)
            open(out,"wb").write(z.read(nm)); n+=1
print("extracted", n, "class files")
