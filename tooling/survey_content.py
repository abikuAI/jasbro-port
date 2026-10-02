"""Survey the shipped event/quest content XML.

Establishes, from the real files rather than from the Java class list, which elements the content
actually uses. That matters because the codebase carries two parallel requirement systems
(customContent/ and world/xml/) and only one of them is what the shipped content is written against.

Also counts <code> blocks, since those are the BeanShell expressions that are the hard part of the
port.
"""

import collections
import pathlib
import re
import sys
import xml.etree.ElementTree as ET

root = pathlib.Path(sys.argv[1] if len(sys.argv) > 1 else ".")
dirs = [root / "events", root / "quests"]

elements = collections.Counter()
attrs = collections.Counter()
code_blocks = 0
files = 0
parse_failed = []

for d in dirs:
    if not d.is_dir():
        continue
    for f in sorted(d.rglob("*.xml")):
        files += 1
        raw = f.read_text(encoding="utf-8", errors="replace")
        code_blocks += len(re.findall(r"<code>", raw))
        try:
            tree = ET.fromstring(raw)
        except ET.ParseError as e:
            parse_failed.append((str(f.relative_to(root)), str(e)))
            # Fall back to a regex sweep so one malformed file does not hide its structure.
            for m in re.finditer(r"<([A-Za-z][\w.]*)", raw):
                elements[m.group(1)] += 1
            continue
        for el in tree.iter():
            elements[el.tag] += 1
            for a in el.attrib:
                attrs[a] += 1

print(f"files scanned : {files}")
print(f"<code> blocks : {code_blocks}")
if parse_failed:
    print(f"\nUNPARSEABLE ({len(parse_failed)}):")
    for name, err in parse_failed[:10]:
        print(f"  {name}: {err}")

print(f"\ndistinct elements: {len(elements)}")
print("\n=== elements by frequency ===")
for name, n in elements.most_common(60):
    print(f"{n:6}  {name}")

if attrs:
    print(f"\n=== attributes ({len(attrs)}) ===")
    for name, n in attrs.most_common(25):
        print(f"{n:6}  {name}")

# The parallel-system question. NOTE: an earlier version of this script concluded from the absence of
# these element names that the legacy world/xml package was dead code. That was WRONG, and the reason
# is instructive: rooms.xml dispatches on the **type attribute**
#     <requirement type="exact-occupant" count="2" />
# not on element names, so an element-name survey cannot see it at all. RoomLoader does use those
# parsers. Absence of evidence in an element-name scan is not evidence of absence here.
LEGACY = ["minimumOccupant", "exactOccupant", "noActivity", "minimumCharacter", "maximumOccupant"]
hits = {k: elements.get(k, 0) for k in LEGACY}
if any(hits.values()):
    print(f"\nLEGACY world/xml element names present: {hits}")
else:
    print("\nNo legacy world/xml element names found in events/quests (expected: they live in")
    print("rooms.xml and dispatch on the `type` ATTRIBUTE, which an element-name scan cannot see).")

# Survey rooms.xml too, by attribute value, since that is where the other system actually lives.
rooms = root / "rooms.xml"
if rooms.is_file():
    raw = rooms.read_text(encoding="utf-8", errors="replace")
    types = collections.Counter(re.findall(r'<requirement\s+type="([^"]+)"', raw))
    chars = collections.Counter(re.findall(r'<char-requirement\s+type="([^"]+)"', raw))
    print(f"\n=== rooms.xml activity requirement types ({sum(types.values())}) ===")
    for name, n in types.most_common():
        print(f"{n:6}  {name}")
    print(f"\n=== rooms.xml char-requirement types ({sum(chars.values())}) ===")
    for name, n in chars.most_common():
        print(f"{n:6}  {name}")
