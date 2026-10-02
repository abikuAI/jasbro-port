import json, sys, collections
wf = sys.argv[1]
d = json.load(open(wf + r"\build-out\logs\static-scan.json", encoding="utf-8"))
f = d["findings"]

print("=== unguarded-selection  (the confirmed NPE bug class) ===")
for x in f["unguarded-selection"]:
    print(f"  {x['file']}:{x['line']}")
    print(f"      {x['text'][:150]}")

print()
print("=== enum valueOf sites, grouped by enum (content/code-drift risk) ===")
g = collections.Counter(x["extra"]["enum"] for x in f["enum-valueof"])
for k,v in g.most_common(): print(f"  {v:3d}  {k}.valueOf(...)")

print()
print("=== resource-maybe-unclosed ===")
for x in f["resource-maybe-unclosed"][:15]:
    print(f"  {x['file']}:{x['line']}  {x['text'][:110]}")

print()
print("=== empty-catch by file ===")
g2 = collections.Counter(x["file"] for x in f["empty-catch"])
for k,v in g2.most_common(): print(f"  {v:3d}  {k}")

print()
print("=== static-mutable (thread-safety surface) ===")
for x in f["static-mutable"][:20]:
    print(f"  {x['file']}:{x['line']}  {x['text'][:110]}")
