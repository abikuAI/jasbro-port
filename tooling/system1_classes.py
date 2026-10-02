"""
Inventory System 1's polymorphic class graph: every `class` attribute in events/ and quests/,
mapped to its decompiled source, with the fields each class actually declares.

WHY THIS EXISTS
---------------
events/ and quests/ are XStream files whose polymorphism is carried by a `class` attribute holding
an FQCN (175 occurrences). To write a parser for them we need to know, for every FQCN that appears:
does the class exist in the decompiled source, and what fields does it declare?

That is exactly the "exact serialisation of System 1's requirement/effect graph" item left open in
CONTENT-MODEL.md. This script answers it mechanically rather than by reading class lists, because
reading class lists is what produced the earlier wrong conclusions about this codebase.

WHAT IT REPORTS
---------------
  1. every distinct `class` value, with occurrence count and where it lives
  2. for each, whether decompiled/source has the file, and if so its declared fields
  3. classes referenced but NOT present in the source (a port would have to invent them)
  4. the `reference` targets, which must preserve object identity when deserialised

Output is written as JSON so downstream tooling can consume it, and a readable summary is printed.
"""

import json
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path

WORK = Path(__file__).resolve().parent.parent
ORIGINAL = WORK / "original"
SOURCE = WORK / "decompiled" / "source"
OUT = WORK / "build-out" / "logs" / "system1-classes.json"


def find_xml_files(root: Path) -> list[Path]:
    r"""Every .xml under root, recursively. Mirrors the loader's `.+\.xml` walk."""
    return sorted(root.rglob("*.xml"))


def collect_class_attributes(files: list[Path]):
    """Returns (Counter of FQCN, map FQCN -> set of relative file paths)."""
    counts = Counter()
    where = defaultdict(set)
    pattern = re.compile(r'\bclass="([^"]+)"')

    for f in files:
        try:
            text = f.read_text(encoding="utf-8", errors="replace")
        except OSError as e:
            print(f"  WARN could not read {f}: {e}", file=sys.stderr)
            continue
        for m in pattern.finditer(text):
            name = m.group(1).strip()
            counts[name] += 1
            where[name].add(str(f.relative_to(WORK)))

    return counts, where


def collect_element_types(files: list[Path]):
    """
    Collects polymorphism carried by ELEMENT NAME, the second mechanism.

    XStream writes a concrete type as the element name when the value sits in a collection, and as
    a `class` attribute when it is a single-valued field. Only counting the attribute form badly
    undercounts the class inventory - this was a real error in the first version of this script.

    Only element names that look like an FQCN are kept (contain a dot and a lowercase package
    segment); plain field elements such as <value> or <target> are ignored.
    """
    counts = Counter()
    where = defaultdict(set)
    pattern = re.compile(r"<([a-zA-Z_][\w.]*\.[a-zA-Z_]\w*)>")

    for f in files:
        try:
            text = f.read_text(encoding="utf-8", errors="replace")
        except OSError:
            continue
        for m in pattern.finditer(text):
            name = m.group(1).strip()
            # Require a plausible lowercase package segment before the final class name.
            if not re.match(r"^[a-z][\w]*(\.[a-z][\w]*)*\.[A-Z]", name):
                continue
            counts[name] += 1
            where[name].add(str(f.relative_to(WORK)))

    return counts, where


def collect_reference_attributes(files: list[Path]) -> Counter:
    """Returns a Counter of `reference` values, which must resolve to shared instances."""
    refs = Counter()
    pattern = re.compile(r'\breference="([^"]+)"')
    for f in files:
        try:
            text = f.read_text(encoding="utf-8", errors="replace")
        except OSError:
            continue
        for m in pattern.finditer(text):
            refs[m.group(1).strip()] += 1
    return refs


def source_file_for(fqcn: str) -> Path | None:
    """Maps an FQCN to its decompiled .java file, or None if absent."""
    rel = fqcn.replace(".", "/") + ".java"
    candidate = SOURCE / rel
    if candidate.is_file():
        return candidate
    # Nested classes decompile to Outer$Inner, and some files land under a different root.
    return None


# Field declarations in decompiled output look like:
#     private final int maximum;
#     protected Map<String, X> things = new HashMap<>();
#     static final Logger log;
FIELD_RE = re.compile(
    r"^\s{2,}(?:@\w+(?:\([^)]*\))?\s+)*"          # annotations
    r"(?:(public|protected|private)\s+)?"           # visibility, optional
    r"(?:(static|final|transient|volatile)\s+)*"    # modifiers
    r"([\w.<>\[\],\s?]+?)\s+"                       # type
    r"(\w+)\s*(?:=|;)",                             # name
    re.MULTILINE,
)

SKIP_TYPES = {"return", "new", "else", "break", "continue", "case", "throw", "import", "package"}


def extract_fields(path: Path) -> tuple[list[dict], bool]:
    """
    Extracts declarations that look like fields.

    Returns (fields, saw_transient). `transient` matters a great deal here: it is the mechanism by
    which Charakter.base is excluded from saves, and a port must reproduce it exactly.
    """
    text = path.read_text(encoding="utf-8", errors="replace")
    fields = []
    saw_transient = False

    for m in FIELD_RE.finditer(text):
        visibility, modifiers, ftype, name = m.groups()
        ftype = ftype.strip()
        if ftype in SKIP_TYPES or not ftype:
            continue
        # A field name cannot be a Java keyword; this filters most statement false positives.
        if name in {"if", "for", "while", "switch", "catch", "synchronized", "return"}:
            continue
        mods = (modifiers or "").strip()
        if "transient" in mods:
            saw_transient = True
        fields.append({
            "name": name,
            "type": ftype,
            "visibility": visibility or "package",
            "modifiers": mods,
        })

    return fields, saw_transient


def main() -> int:
    events = ORIGINAL / "events"
    quests = ORIGINAL / "quests"

    files = []
    for d in (events, quests):
        if d.is_dir():
            files.extend(find_xml_files(d))

    if not files:
        print(f"ERROR no event/quest XML found under {ORIGINAL}", file=sys.stderr)
        return 1

    print(f"scanned {len(files)} XML file(s) under events/ and quests/")

    counts, where = collect_class_attributes(files)
    elem_counts, elem_where = collect_element_types(files)
    refs = collect_reference_attributes(files)

    print(f"distinct `class` attribute values : {len(counts)}")
    print(f"total `class` attribute occurrences: {sum(counts.values())}")
    print(f"distinct element-name types       : {len(elem_counts)}")
    print(f"total element-name occurrences    : {sum(elem_counts.values())}")
    print(f"distinct `reference` values       : {len(refs)} "
          f"({sum(refs.values())} occurrences)")

    # Merge: a class can appear via either mechanism, so the inventory is the union.
    merged = Counter()
    merged_where = defaultdict(set)
    for src_counts, src_where in ((counts, where), (elem_counts, elem_where)):
        for k, v in src_counts.items():
            merged[k] += v
            merged_where[k] |= src_where[k]

    print(f"UNION of both mechanisms          : {len(merged)} distinct classes, "
          f"{sum(merged.values())} occurrences")

    only_elem = sorted(set(elem_counts) - set(counts))
    only_attr = sorted(set(counts) - set(elem_counts))
    print(f"  appearing ONLY as element names : {len(only_elem)}")
    print(f"  appearing ONLY as class attr    : {len(only_attr)}")
    print()

    if only_elem:
        print("  the element-name-only set (invisible to a class-attribute scan):")
        for name in only_elem[:20]:
            print(f"    {elem_counts[name]:4}x  {name}")
        if len(only_elem) > 20:
            print(f"    ... and {len(only_elem) - 20} more")
        print()

    counts, where = merged, merged_where

    entries = []
    missing = []

    for fqcn, count in counts.most_common():
        src = source_file_for(fqcn)
        if src is None:
            missing.append(fqcn)
            entries.append({
                "fqcn": fqcn,
                "count": count,
                "files": sorted(where[fqcn]),
                "sourceFound": False,
            })
            continue

        fields, saw_transient = extract_fields(src)
        entries.append({
            "fqcn": fqcn,
            "count": count,
            "files": sorted(where[fqcn]),
            "sourceFound": True,
            "sourcePath": str(src.relative_to(WORK)),
            "sawTransient": saw_transient,
            "fields": fields,
        })

    # ---------------------------------------------------------------- report
    print("=" * 78)
    print("CLASSES WITH NO DECOMPILED SOURCE  (a port must invent or drop these)")
    print("=" * 78)
    if missing:
        for fqcn in missing:
            print(f"  {counts[fqcn]:4}x  {fqcn}")
    else:
        print("  none - every referenced class is present in the decompiled source")
    print()

    print("=" * 78)
    print("TOP 25 BY OCCURRENCE")
    print("=" * 78)
    for e in entries[:25]:
        mark = " " if e["sourceFound"] else "?"
        nfields = len(e.get("fields", []))
        t = " transient!" if e.get("sawTransient") else ""
        print(f"  {mark} {e['count']:4}x  {e['fqcn']:<60} {nfields:3} field(s){t}")
    print()

    print("=" * 78)
    print("CLASSES DECLARING transient FIELDS  (these are EXCLUDED from saves)")
    print("=" * 78)
    trans = [e for e in entries if e.get("sawTransient")]
    if trans:
        for e in trans:
            print(f"  {e['count']:4}x  {e['fqcn']}")
    else:
        print("  none")
    print()

    print("=" * 78)
    print("REFERENCE TARGETS  (must resolve to the SAME instance, not a copy)")
    print("=" * 78)
    for target, n in refs.most_common(15):
        print(f"  {n:4}x  {target}")
    if len(refs) > 15:
        print(f"  ... and {len(refs) - 15} more distinct targets")

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps({
        "scannedFiles": len(files),
        "distinctClasses": len(counts),
        "classOccurrences": sum(counts.values()),
        "distinctReferences": len(refs),
        "referenceOccurrences": sum(refs.values()),
        "missingSource": missing,
        "classes": entries,
        "references": dict(refs),
    }, indent=2), encoding="utf-8")
    print()
    print(f"wrote {OUT.relative_to(WORK)}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
