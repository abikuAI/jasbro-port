import json, sys, collections
wf = sys.argv[1]
bl = json.load(open(wf + r"\build-out\logs\bsh-blocks.json", encoding="utf-8"))
hard = [b for b in bl if b["kind"] in ("script","script+api","api-call")]
print(f"blocks needing real translation: {len(hard)} of {len(bl)}")
print()
seen = set()
n = 0
for b in sorted(hard, key=lambda x:-x["chars"]):
    key = b["kind"]
    if n >= 5: break
    print(f"--- [{b['kind']}] {b['file']}  (parent <{b['parent']}>, {b['lines']} lines)")
    print(b["code"][:700])
    print()
    n += 1
print("=== which parent element types carry scripts ===")
for k,v in collections.Counter(b["parent"] for b in bl).most_common(12):
    print(f"  {v:4d}  <{k}>")
