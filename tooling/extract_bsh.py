import os, re, json, html, collections, sys
wf = sys.argv[1]
root = r"C:\Games\Jasbro_Final"
out = []
for sub in ("events","quests","npcs","items","characters","rooms.xml"):
    pass
targets = []
for sub in ("events","quests","npcs","items","characters"):
    d = os.path.join(root, sub)
    if os.path.isdir(d):
        for r,_,fs in os.walk(d):
            for f in fs:
                if f.lower().endswith(".xml"): targets.append(os.path.join(r,f))
targets.append(os.path.join(root,"rooms.xml"))

def classify(code):
    c = code.strip()
    has_api  = bool(re.search(r'Jasbro\.getInstance\(\)|[A-Z][A-Za-z0-9_]*\.(get|set|add|remove)[A-Z]', c))
    has_ctrl = bool(re.search(r'\b(if|for|while|switch)\b', c))
    has_ret  = c.startswith("return")
    lines = len([l for l in c.splitlines() if l.strip()])
    if lines <= 1 and not has_ctrl: return "expression"
    if has_api and has_ctrl: return "script+api"
    if has_ctrl: return "script"
    if has_api: return "api-call"
    if has_ret: return "predicate"
    return "simple"

for p in targets:
    try: t = open(p, encoding="utf-8", errors="replace").read()
    except Exception: continue
    rel = os.path.relpath(p, root)
    # find <code>...</code> with the nearest enclosing element name before it
    for m in re.finditer(r'<code>(.*?)</code>', t, re.S):
        code = html.unescape(m.group(1)).strip()
        pre = t[:m.start()]
        elem = re.findall(r'<([A-Za-z_][\w\.\-]*)', pre)
        parent = elem[-1] if elem else "?"
        out.append({"file": rel, "parent": parent, "kind": classify(code),
                    "lines": len([l for l in code.splitlines() if l.strip()]),
                    "chars": len(code), "code": code})

byk = collections.Counter(o["kind"] for o in out)
byf = collections.Counter(o["file"] for o in out)
print(f"total code blocks: {len(out)}   across {len(byf)} files")
print()
print("=== by complexity ===")
for k,v in byk.most_common(): print(f"  {v:4d}  {k}")
print()
print("=== top files by block count ===")
for f,v in byf.most_common(12): print(f"  {v:3d}  {f}")
print()
print("=== required API surface (Java symbols referenced from content) ===")
api = collections.Counter()
for o in out:
    for s in re.findall(r'([A-Z][A-Za-z0-9_]*)\.([a-z][A-Za-z0-9_]*)\(', o["code"]):
        api[s[0]+"."+s[1]] += 1
    for s in re.findall(r'(?:^|\.)([a-z][A-Za-z0-9_]*)\.([a-zA-Z][A-Za-z0-9_]*)', o["code"]):
        api[s[0]] += 1
print("  top member access roots:")
for k,v in api.most_common(25): print(f"    {v:4d}  {k}")

json.dump(out, open(os.path.join(wf,"build-out","logs","bsh-blocks.json"),"w",encoding="utf-8"), indent=1)
print()
print("-> build-out/logs/bsh-blocks.json")

