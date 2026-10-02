import os, re
root = r"C:\Games\Jasbro_Final"
hits = 0; files_with = 0; samples = []
total = 0
for sub in ("events","quests","npcs","items","characters"):
    d = os.path.join(root, sub)
    if not os.path.isdir(d): continue
    for r,_,fs in os.walk(d):
        for f in fs:
            if not f.lower().endswith(".xml"): continue
            p = os.path.join(r,f); total += 1
            try: t = open(p, encoding="utf-8", errors="replace").read()
            except Exception: continue
            m = re.findall(r'<code>(.*?)</code>', t, re.S)
            m += re.findall(r'code="([^"]{3,})"', t)
            if m:
                files_with += 1; hits += len(m)
                if len(samples) < 8:
                    samples.append((os.path.relpath(p, root), m[0].strip()[:220]))
print(f"XML files scanned      : {total}")
print(f"files containing code  : {files_with}")
print(f"code blocks total      : {hits}")
print()
for p, c in samples:
    print(f"--- {p}")
    print(f"    {c}")
    print()
