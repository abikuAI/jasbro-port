# Save Format — reverse-engineered, evidence-backed

**Method.** Rather than guess, the format was extracted from the real game classes: the decompiled
jar was loaded in a JDK 8 harness (`fixtures/java/SaveFixture.java`) that builds a known `GameData`,
serialises it with **the game's own XStream configuration**, and writes it out. Those files are the
**golden fixtures** the C# port is verified against:

| Fixture | Contents |
|---|---|
| `fixtures/save-golden.xml` | Core scalars + components (no characters) |
| `fixtures/save-golden-character.xml` | Core scalars **+ a full character object** |
| `fixtures/save-golden-reference.xml` | **Shared object reference** — two characters, `protagonist` pointing at the second |

Regenerate them with:

```powershell
$wf = "JaSBro-work"; $jdk = "$wf\tools\jdk8\bin"
$cp = "$wf\decompiled\JaSBro-from-decompiled.jar;" + ((Get-ChildItem "$wf\original\lib\*.jar").FullName -join ';')
& "$jdk\javac.exe" -cp $cp -d "$wf\fixtures\out" "$wf\fixtures\java\SaveFixture.java"
& "$jdk\java.exe"  -cp "$wf\fixtures\out;$cp" SaveFixture `
    "$wf\fixtures\save-golden.xml" `
    "$wf\fixtures\save-golden-character.xml" `
    "$wf\fixtures\save-golden-reference.xml"
```

Writer configuration (`SaveAndLoadPerformer.java`), which the harness reproduces exactly:

```java
XStream xstream = new XStream(new StaxDriver());
xstream.autodetectAnnotations(true);
```

---

## Where saves live

`SlotSaveAction.java:25-27` / `SlotLoadAction.java:25-27` write `save1.xml`, `save2.xml`, … and
`quicksave.xml` **into the game's working directory**. The slot count is `Settings.SAVESLOTS`
(default 3). Filenames are CWD-relative — the same hazard as `rooms.xml` (see the port notes).

---

## The structural rules

### 1. The root element is the fully-qualified class name

```xml
<?xml version="1.0" ?><jasbro.game.GameData>...
```

`GameData` carries `@XmlRootElement(name = "gamedata")`, but that is a **JAXB** annotation and XStream
ignores it. **A reader that looks for `<gamedata>` will never match a real save.** Verified by
fixture.

### 2. An element's name is its type

| Situation | Encoding |
|---|---|
| Field's declared type matches the value exactly | Bare field name — `<money>1234</money>` |
| Value's type differs from the declaration | **Element name is the FQCN** — `<jasbro.game.character.Charakter>` |
| A `List`/`Set` element | Each item is named with its FQCN |
| Collection with a special XStream spelling | Name + `class` attribute — `<listeners class="jasbro.WeakList">` |

### 3. Null fields are omitted entirely; empty collections are present and empty

```
shop, questManager, otherLocationMap   → ABSENT (were null)
houses, characters, items              → PRESENT as empty elements
unlocks                                → PRESENT, empty (the class has no persisted fields)
```

This asymmetry matters: a reader must not confuse "null" with "empty".

### 4. Enums are written by NAME, never ordinal

`<time>NIGHT</time>`, `<ownership>OWNED</ownership>`, `<type>SLAVE</type>`.

> **Save-compat consequence:** renaming any enum constant breaks every existing save. The port must
> keep the exact constant names — which is why `port/Simbro.Core/Content/CharacterEnums.cs` is
> **generated** from the Java rather than hand-written.

### 5. `EnumMap` and `EnumSet` get a dedicated spelling

```xml
<activities class="enum-map" enum-type="jasbro.game.world.Time"></activities>
<specializations class="enum-set" enum-type="jasbro.game.character.specialization.SpecializationType"></specializations>
```

Both the `class` and the `enum-type` attribute are required for a byte-compatible round trip.

### 6. Field order follows declaration order

`GameData`: `day, time, money, houses, inventory, eventManager, characters, defaultPreferences, unlocks`
— matching the field declarations in `GameData.java:36-51`, with nulls and the `transient`
`statCollector` removed.

---

## ⚠️ `Charakter.base` is `transient` — the biggest save-format surprise

`Charakter.java:78`:

```java
private transient CharacterBase base;
```

So **the `CharacterBase` is never written to a save**, and neither are the content `properties.xml`
values it holds. Instead `Charakter` keeps its **own** copies and links home by id:

```java
private String name;          // Charakter's own copy, NOT CharacterBase.name
private CharacterType type;
private Gender gender;
private List<Trait> traits;   // note: List here, but Set<Trait> in CharacterBase
private String baseId;        // the link back to the content definition
```

On load, `getBase()` (`Charakter.java:86-97`) re-resolves the base by scanning
`Jasbro.getCharacterBases()` for a matching `baseId`.

**Consequences for the port:**

- Images, descriptions and initial attributes come from **content**, not from the save. A save is
  therefore only interpretable alongside the matching content set.
- If a content file is missing or its `id` changes, the character silently loses its base.
- `Charakter.traits` is a `List<Trait>` while `CharacterBase.traits` is a `Set<Trait>` — different
  types with different serialisations. Do not unify them without checking both call sites.

### A full character, verbatim

```xml
<jasbro.game.character.Charakter>
  <listeners><copyListLock><value>false</value></copyListLock>
    <queue><lock></lock><queueLength>0</queueLength></queue><list></list></listeners>
  <name>Test Subject</name>
  <attributes></attributes>
  <activities class="enum-map" enum-type="jasbro.game.world.Time"></activities>
  <specializations class="enum-set" enum-type="jasbro.game.character.specialization.SpecializationType"></specializations>
  <conditions></conditions>
  <type>SLAVE</type>
  <gender>FEMALE</gender>
  <fame><fame>0.0</fame></fame>
  <ownership>OWNED</ownership>
  <traits>
    <jasbro.game.character.traits.Trait>LOYAL</jasbro.game.character.traits.Trait>
    <jasbro.game.character.traits.Trait>FIT</jasbro.game.character.traits.Trait>
  </traits>
  <baseId>TestChar</baseId>
  <bonusPerks>0</bonusPerks>
  <numberTrees>0</numberTrees>
</jasbro.game.character.Charakter>
```

Note the traits: **each element is named with the enum's fully-qualified type**, because the field is
a `List<Trait>` rather than a `Set<Trait>`.

---

## ⚠️ `EventManager` leaks collection internals into the save

```xml
<eventManager>
  <listeners class="jasbro.WeakList">
    <copyListLock><value>false</value></copyListLock>
    <queue><lock></lock><queueLength>0</queueLength></queue>
    <list></list>
  </listeners>
</eventManager>
```

`EventManager` stores its listeners in a `WeakList`, and XStream serialises **that collection's
private fields** — its lock, its internal queue, its backing array — straight into the save file.

**The port cannot model listeners as a plain list without changing the on-disk format.** For reading
existing saves it can simply ignore these fields; for writing byte-compatible saves it must emit them.

---

## ⚠️ Object references — shared and cyclic graphs

XStream does not repeat an object it has already written. It emits a **reference** instead:

```xml
<protagonist reference="../characters/jasbro.game.character.Charakter[2]"></protagonist>
```

This is not an edge case — it is guaranteed in every real save, because `GameData.protagonist` is a
`Charakter` that is *also* in `characters`, and `houses` contain rooms that contain characters.

**The path is relative to the referencing element, and indexing is 1-BASED.** Both facts were
established from a fixture (not inferred): the harness put two same-class characters in
`characters` and pointed `protagonist` at the **second**, and XStream produced exactly
`../characters/jasbro.game.character.Charakter[2]`.

Rules:

| Segment | Meaning |
|---|---|
| `name` | first child element with that name |
| `name[n]` | the **n-th** (1-based) child with that name |
| `..` | the parent element |

**A reader that ignores references breaks two ways:** shared objects get duplicated (so identity
comparisons against them fail), and **cyclic graphs recurse forever**. Resolution must therefore
return the *same node instance*, not a copy — `SaveDocument.Resolve` does this and the verifier
asserts it with `ReferenceEquals`.

---

## Object references — two forms

XStream writes references in **two** shapes, and both must be handled:

| Form | Example | Meaning |
|---|---|---|
| **Indexed** | `reference="../characters/jasbro.game.character.Charakter[2]"` | The **2nd** `Charakter` child (1-based) |
| **Unindexed** | `reference="../../../../jasbro.game.character.Charakter"` | The **first** child with that name |

Both are relative paths: `..` climbs to the parent, and the final segment is an element name. An
optional `[n]` selects among same-named siblings, **1-based** — verified by generating a fixture with
two same-class characters where the reference had to disambiguate, and confirming it resolved to the
*second*.

Resolution must return the **same instance**, not a copy. Otherwise a cyclic graph
(house → room → character → house) recurses forever, and shared-object identity silently breaks.

### Weak references round-trip — and preserve identity

`AgeProgressionData` (a **persisted** field) holds `WeakReference<Charakter> mother/father`. Whether
XStream could round-trip a `java.lang.ref.Reference` was an open question — the save fixtures
contained no character with age data, so they neither confirmed nor refuted it.

Tested empirically (`fixtures/java/AgeProgressionFixture.java`), in the two situations that occur in
real play:

**Case 1 — the parent is also a character in the same save (the normal case).** XStream writes a
*reference*, not a copy:

```xml
<mother>
  <referent class="jasbro.game.character.Charakter"
            reference="../../../../jasbro.game.character.Charakter"></referent>
  <queue class="java.lang.ref.ReferenceQueue$Null">...</queue>
</mother>
```

After a round trip the weak reference resolved to **the very same instance** as the character in the
list. **Parentage survives by identity.**

**Case 2 — the parent has been garbage-collected.** The reference is written empty and loads cleanly
as `null`. Nothing throws.

> **This is why `AgeProgressionData` also carries plain `nameMother` / `nameFather` strings.** They are
> the durable record; the weak reference is the cheap live link. A port that models only the
> reference loses parentage whenever the parent was collected — which is exactly the case the strings
> exist to cover.

The reference path here climbs **out of** `referent` → `mother` → `ageProgressionData` → the child →
the list, and its final segment is a bare type name with no index. That is a genuinely different
path shape from the indexed form, and it is what a real save with age data contains.

## Verification status

### ✅ Cross-language compatibility is PROVEN in both directions

```powershell
pwsh -File verify-save.ps1
```

The script runs three steps and exits non-zero on any failure:

1. **Java → XML.** Regenerates the golden fixtures using the real decompiled game classes.
2. **C# reads them.** Runs `port/Simbro.Verify` — **104 checks**, asserting 31 save-format
   properties (root class name, scalar round-trips, enum-by-name, null-vs-empty, the transient
   `base`, `baseId`, FQCN-named trait elements, the `enum-map`/`enum-set` spellings, the `WeakList`
   leak, and reference resolution with 1-based indexing and instance identity). It then **writes a
   save of its own**.
3. **Java reads the C# output.** `fixtures/java/SaveLoader.java` loads that file with the actual
   shipped game classes and prints the reconstructed state.

**Result — scalars, components, and a full character:**

```
LOADED-OK
day=99
time=NIGHT
money=497
inventory=jasbro.game.items.Inventory@343570b7
eventManager=jasbro.game.events.EventManager@157853da
defaultPreferences=jasbro.game.DefaultPreferences@71c3b41
unlocks=jasbro.game.world.Unlocks@236e3f4e
CHAR[0].name=Loli
CHAR[0].type=CHILD
CHAR[0].gender=FEMALE
CHAR[0].baseId=Loli
CHAR[0].traits=[LOLI, FRAGILE, LOYAL]
```

**COMPATIBILITY VERIFIED: the shipped Java game loads saves written by the C# port, including a
full character with its traits and its content link.**

This is the direction that would otherwise fail silently — a port that only *reads* legacy saves
looks fine until the first time it saves and the original game can no longer open the result.

> **Test-harness note (a real trap):** the script matches output with `.Contains()`, **not**
> PowerShell's `-like`. `-like` treats `[` and `]` as wildcard character classes, so a pattern like
> `CHAR[0].name=Loli` silently matches nothing and the test reports a false failure — which is
> exactly what happened the first time this ran.

---

## Open questions

- **Byte-identical output is not attempted, and is not the goal.** The port emits *semantically*
  compatible saves — proven by the Java loader reconstructing every value. Matching XStream
  byte-for-byte (attribute ordering, numeric formatting on exotic values) would be brittle and buys
  nothing the loader test does not already establish. Note the header *is* matched
  (`<?xml version="1.0" ?>` — with a space before `?>`), because that is cheap.
- **Characters are written, but only in a minimal shape** — scalars, traits, attributes and the
  `baseId` link. Activities, specialisations, conditions, equipment and inventory are read but not
  yet written.
- **Reference cycles across types** — the fixtures prove a same-type reference (indexed and
  unindexed) and a weak reference. A cycle closing back through a *different* type
  (house → room → character → house) is still not exercised.
- **No real player save exists** to test against (`C:\Games\Jasbro_Final` has none), so the fixtures
  are synthesised from the real classes rather than captured from play. A genuine save from a long
  running game would be the single most valuable test input for this work.
