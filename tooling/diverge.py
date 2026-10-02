import os, sys, re, difflib
wf = sys.argv[1]
repo = os.path.join(wf,"repo","src","main","java","jasbro")
dec  = os.path.join(wf,"decompiled","source","jasbro")

def norm(p):
    t = open(p, encoding="utf-8", errors="replace").read()
    t = re.sub(r'/\*.*?\*/', ' ', t, flags=re.S)
    t = re.sub(r'//[^\n]*', ' ', t)
    t = re.sub(r'\s+', ' ', t)
    return t.strip()

shared = []
for r,_,fs in os.walk(repo):
    for f in fs:
        if not f.endswith(".java"): continue
        rel = os.path.relpath(os.path.join(r,f), repo).replace("\\","/")
        d = os.path.join(dec, rel)
        if os.path.exists(d): shared.append((rel, os.path.join(r,f), d))

rows=[]
for rel, a, b in shared:
    na, nb = norm(a), norm(b)
    if na == nb:
        rows.append((1.0, rel, len(na), len(nb))); continue
    r = difflib.SequenceMatcher(None, na, nb).quick_ratio()
    if r > 0.85:
        r = difflib.SequenceMatcher(None, na, nb).ratio()
    rows.append((r, rel, len(na), len(nb)))

identical = [row for row in rows if row[0] >= 0.999]
print(f"shared classes compared: {len(rows)}")
print(f"  byte-identical after normalization : {len(identical)}")
buckets = [(0.98,1.01,'essentially same'),(0.9,0.98,'minor drift'),(0.7,0.9,'moderate change'),(0.0,0.7,'MAJOR change')]
for lo,hi,label in buckets:
    n = sum(1 for row in rows if lo <= row[0] < hi)
    print(f"  {label:20s} ({lo:.2f}-{min(hi,1.0):.2f}): {n}")
print()
print("=== 25 MOST DIVERGED shared classes (candidates for behaviour change) ===")
for r, rel, la, lb in sorted(rows)[:25]:
    print(f"  {r:5.2f}  {rel:72s} repo={la:6d} shipped={lb:6d}")
with open(os.path.join(wf,"build-out","logs","divergence.txt"),"w",encoding="utf-8") as fh:
    for r, rel, la, lb in sorted(rows):
        fh.write(f"{r:.4f}\t{rel}\t{la}\t{lb}\n")
print()
print("full ranking -> build-out/logs/divergence.txt")
