import json, sys
wf = sys.argv[1]
d = json.load(open(wf + r"\build-out\logs\content-schema.json", encoding="utf-8"))
for folder, classes in d["classesByFolder"].items():
    print(f"=== {folder}  ({d['filesByFolder'].get(folder,0)} files, {len(classes)} distinct classes) ===")
    for k,v in list(classes.items())[:18]:
        print(f"   {v:5d}  {k}")
    print()
print("=== plain (non-class) element names, by folder ===")
for folder, elems in d["elementsByFolder"].items():
    top = ", ".join(f"{k}({v})" for k,v in list(elems.items())[:14])
    print(f"  {folder}: {top}")
    print()
