import re, sys, os
wf = sys.argv[1]
src = os.path.join(wf, "decompiled", "source")
tj = os.path.join(src, "jasbro", "game", "character", "traits", "Trait.java")
txt = open(tj, encoding="utf-8", errors="replace").read()

# enum body sits between the first '{' after 'enum Trait' and its matching '}'
start = txt.index("enum Trait")
body_start = txt.index("{", start)
depth, i = 0, body_start
while i < len(txt):
    if txt[i] == "{": depth += 1
    elif txt[i] == "}":
        depth -= 1
        if depth == 0: break
    i += 1
body = txt[body_start+1:i]

# a constant is NAME followed by ',' or '(' or ';' at statement position
declared = set(re.findall(r'(?:^|[\s;{])([A-Z][A-Z0-9_]*)\s*(?=[(,;])', body))
print("Trait enum constants (corrected):", len(declared))
for probe in ("LOYAL", "BEAUTICIAN"):
    print(f"  {probe}: {'declared' if probe in declared else 'ABSENT'}")

print()
print("=== trait= references in each content folder not declared in the enum ===")
folders = ("items","npcs","events","quests","characters")
miss = {}
total_files = 0
for k in folders:
    root = os.path.join(r"C:\Games\Jasbro_Final", k)
    if not os.path.isdir(root): continue
    for r, _, fs in os.walk(root):
        for f in fs:
            if not f.lower().endswith((".xml",".properties")): continue
            p = os.path.join(r,f); total_files += 1
            try: t = open(p, encoding="utf-8", errors="replace").read()
            except Exception: continue
            for m in set(re.findall(r'trait\s*=\s*"([^"]+)"', t)) | set(re.findall(r'<trait>([A-Za-z0-9_]+)</trait>', t)):
                if m not in declared:
                    miss.setdefault(m, []).append(os.path.relpath(p, r"C:\Games\Jasbro_Final"))
print(f"scanned {total_files} content files")
if not miss: print("  none - all referenced traits exist in the enum")
for k in sorted(miss):
    print(f"  {k} ({len(miss[k])} file(s)): {miss[k][:3]}")
