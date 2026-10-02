"""Inventory the JaSBro content schema.

XStream serialises Java objects using fully-qualified class names as element
names, so the set of element names in the content XML *is* the domain model.
This extracts that model: which classes appear, how often, and what data they
carry.

Usage: python schema.py <content_root> <out_dir>
"""
import os
import re
import sys
import json
import collections

ROOT = sys.argv[1]
OUT = sys.argv[2]

FOLDERS = ["characters", "items", "npcs", "events", "quests"]
EXTRA = ["rooms.xml", "fameUnlocks.xml"]

targets = []
for f in FOLDERS:
    d = os.path.join(ROOT, f)
    if os.path.isdir(d):
        for r, _, fs in os.walk(d):
            for x in fs:
                if x.lower().endswith((".xml", ".properties")):
                    targets.append((f, os.path.join(r, x)))
for x in EXTRA:
    p = os.path.join(ROOT, x)
    if os.path.exists(p):
        targets.append(("config", p))

class_refs = collections.Counter()
class_by_folder = collections.defaultdict(collections.Counter)
attrs = collections.defaultdict(collections.Counter)
sample = {}
files_by_folder = collections.Counter()

CLASSLIKE = re.compile(r'<([A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)+)(?:\s|>|/)')
ANYELEM = re.compile(r'<([A-Za-z_][\w\.\-]*)')

for folder, path in targets:
    files_by_folder[folder] += 1
    try:
        t = open(path, encoding="utf-8", errors="replace").read()
    except Exception:
        continue
    rel = os.path.relpath(path, ROOT)
    for m in CLASSLIKE.finditer(t):
        cls = m.group(1)
        class_refs[cls] += 1
        class_by_folder[folder][cls] += 1
        if cls not in sample:
            i = m.start()
            sample[cls] = (rel, t[i:i + 260])
    for m in ANYELEM.finditer(t):
        e = m.group(1)
        if "." not in e:
            attrs[folder][e] += 1

report = {
    "filesByFolder": dict(files_by_folder),
    "distinctClasses": len(class_refs),
    "totalClassRefs": sum(class_refs.values()),
    "topClasses": class_refs.most_common(60),
    "classesByFolder": {k: dict(v.most_common(40)) for k, v in class_by_folder.items()},
    "elementsByFolder": {k: dict(v.most_common(40)) for k, v in attrs.items()},
    "samples": {k: v for k, v in list(sample.items())[:40]},
}

os.makedirs(OUT, exist_ok=True)
json.dump(report, open(os.path.join(OUT, "content-schema.json"), "w", encoding="utf-8"), indent=1)

print(f"scanned {sum(files_by_folder.values())} content files")
print(f"files by folder: {dict(files_by_folder)}")
print(f"\ndistinct fully-qualified classes referenced: {len(class_refs)}")
print(f"total class references: {sum(class_refs.values())}\n")
print("=== top 40 classes in content (the domain model) ===")
for k, v in class_refs.most_common(40):
    print(f"  {v:5d}  {k}")
print(f"\n-> {os.path.join(OUT, 'content-schema.json')}")
