# System 1's class graph — `events/` and `quests/`

**The complete polymorphic surface is 53 classes, and every one of them is present in the
decompiled source.** That is the headline: there are no missing types to invent, no library
classes serialised by reference, and no unknown shapes.

Measured by [`tooling/system1_classes.py`](tooling/system1_classes.py) over all **62** XML files
under `original/events/` and `original/quests/`. Raw output: `build-out/logs/system1-classes.json`
(published as **`data/system1-classes.json`** — this document is written against the working folder
and is copied verbatim into the review repo's `analysis/`, where `build-out/logs/` becomes `data/`).

---

## Two polymorphism mechanisms, and why counting one of them fails

This is the single most important thing in this document.

XStream picks the mechanism based on **whether the value sits in a collection**:

| Value position | Serialised as | Example |
|---|---|---|
| a **single-valued field** | a `class` **attribute** on the field element | `<attributeType class="jasbro.game.character.attributes.EssentialAttributes">ENERGY</attributeType>` |
| an **element of a collection** | the **element name** is the type | `<jasbro.game.world.customContent.requirements.ActivityRequirement>…</…>` |

Counting only `class` attributes finds **13** classes. Counting only element names finds **47**.
The truth is the **union: 53**.

**40 of the 53 classes appear ONLY as element names.** A tool that reads `class=` attributes — the
obvious thing to reach for, since `class` is literally the attribute name — misses more than
two-thirds of the type surface. This was a real error made while writing this very analysis, and it
is the third time in this project that a conclusion drawn from *names* turned out to disagree with
the code that consumes them.

### The numbers

| Mechanism | Distinct | Occurrences |
|---|---:|---:|
| `class` attribute | 13 | 175 |
| element name | 47 | 1416 |
| **union** | **53** | **1591** |

- appearing **only** as element names: **40**
- appearing **only** as `class` attributes: **6**
- classes with **no** decompiled source: **0**

The earlier element survey reported "102 distinct elements". That number is consistent: it counts
53 polymorphic type names **plus** roughly 49 plain field names (`<value>`, `<target>`,
`<triggerType>`, `<amount>`, …) which are not types at all.

---

## The 25 most-used classes

| Occurrences | Class | Fields | Notes |
|---:|---|---:|---|
| 356 | `jasbro.gui.pictures.ImageTag` | 18 | an **enum**, used as a collection element |
| 179 | `customContent.ImageSelection` | 11 | |
| 177 | `effects.WorldEventSimpleMessage` | 7 | the most common effect |
| 97 | `effects.WorldEventEffectContainer` | 1 | |
| 87 | `requirements.CodeRequirement` | 2 | **holds BeanShell** |
| 86 | `effects.WorldEventCondition` | 1 | |
| 79 | `effects.WorldEventCode` | 3 | **holds BeanShell** |
| 51 | `customContent.WorldEvent` | 23 | **has transient field(s)** |
| 50 | `customContent.Trigger` | 5 | the only reference target |
| 37 | `requirements.AndRequirement` | 0 | composite |
| 36 | `customContent.CustomQuestStage` | 13 | |
| 32 | `effects.WorldEventSetQuestStage` | 2 | |
| 28 | `effects.WorldEventSaveVariable` | 2 | |
| 26 | `effects.WorldEventSetQuestStatus` | 4 | |
| 21 | `requirements.AttributeRequirement` | 4 | |
| 19 | `character.attributes.EssentialAttributes` | 3 | an **enum** |
| 19 | `requirements.ChanceRequirement` | 1 | |
| 19 | `effects.WorldEventLoadVariable` | 2 | |
| 16 | `requirements.MainCharacterRequirement` | 0 | |
| 16 | `requirements.ActivityRequirement` | 1 | |
| 15 | `effects.WorldEventChangeAttribute` | 7 | |
| 14 | `effects.WorldEventGainItem` | 2 | |
| 11 | `customContent.CustomQuestTemplate` | 6 | **has transient field(s)** |
| 9 | `character.specialization.SpecializationAttribute` | 1 | an **enum** |
| 8 | `world.locations.LocationType` | 4 | an **enum** |

Note that several entries are **enums serialised with a `class` attribute** — because the declared
field type is a supertype the enum implements. `WorldEventChangeAttribute.attributeType`, for
instance, accepts `EssentialAttributes`, `BaseAttributeTypes`, and `SpecializationAttribute`, so
XStream must record which family the constant came from. The port's existing three-family attribute
model already reflects this.

---

## Transient fields — identified exactly

`transient` means **excluded from serialisation**. Four classes in this area declare them:

**`WorldEvent`** (51 occurrences in content)
```java
private transient Interpreter interpreter;          // the BeanShell interpreter
private transient Map<String, Object> attributeMap; // the runtime variable store
private transient TFile file;
private transient Set<WorldEvent> triggeredEvents;
private transient Set<Charakter> protectedCharacters;
private transient boolean inProgress = false;
```

**`CustomQuestTemplate`** (11 occurrences in content)
```java
private transient TFile file;
```

**`CustomQuest`** — *not* a content-file type, but present in **saves**
```java
private transient Interpreter interpreter;
private transient Map<String, Object> attributeMap;
private transient CustomQuestTemplate template;     // <- see below
private transient Set<WorldEvent> triggeredEvents;
private transient boolean inProgress = false;
```

**`ComplexEnemyTemplate`** (in `npcs/` content)
```java
private transient String id;
private transient TFile file;
```

### Three consequences that change the design

**1. Content files carry authored state only.** `WorldEvent` declares 23 fields but writes 17 of
them. The `interpreter`, the runtime `attributeMap`, the backing `TFile`, and all live progress
state are reconstructed at load. So a content parser can be a **plain data mapper** — it never has
to reconstruct a live script engine or thread state.

**2. `WorldEvent.attributeMap` being transient confirms the two-variable-store split.**
[`BSH-MIGRATION.md`](BSH-MIGRATION.md) established that content has **two** parallel variable stores
and that many elements depend on the distinction. Here is the mechanism: `WorldEvent.attributeMap`
is runtime-only and **never serialised**, whereas `Quest.variables` **is** serialised. A port that
merged the two stores into one would round-trip content correctly and still diverge from the game
whenever a save is involved.

**3. `CustomQuest.template` is transient — a saved quest does not contain its own definition.**
A `CustomQuest` in a savefile records *progress*, not the template it was built from. On load the
template must be **re-linked from the content set by id**. The direct implication: **a savefile is
not self-contained with respect to custom quests**, and loading one against a different (or
missing) content set yields a quest that cannot describe itself. This is the same class of coupling
as `Charakter.base` being transient and keyed by `baseId`, and it deserves the same treatment in the
port: a save/load round-trip test must supply the matching content.

### For anyone using the JSON

`build-out/logs/system1-classes.json` lists **every declared field, including transient ones**, with
type, visibility and modifiers. A parser must filter on `transient`, so the field counts in the
table above overstate what is serialised — `WorldEvent`'s "23 fields" is really 17.

---

## References: only `Trigger`, and only three distinct paths

All 41 `reference` attributes resolve to `Trigger` objects:

| Occurrences | Path |
|---:|---|
| 26 | `../../../triggers/jasbro.game.world.customContent.Trigger` |
| 14 | `../../../triggers/jasbro.game.world.customContent.Trigger[2]` |
| 1 | `../../../triggers/jasbro.game.world.customContent.Trigger[3]` |

This is genuinely good news for the port. Both reference forms already verified for saves appear
here — the **unindexed** form (first match) and the **1-based indexed** form — and they resolve
within a `WorldEvent`'s own `triggers` collection, a shallow and well-understood path.

Shared triggers matter semantically: a `Trigger` referenced from two places is **one object**, and
mutating it once must be visible from both. Cloning it during load would silently duplicate
trigger state.

---

## What this establishes, and what it does not

**Established**

- The exact set of 53 types, all present, with declared field counts.
- Which mechanism carries the type in each position.
- The complete reference graph: one class, three paths, two already-verified forms.
- The exact transient fields, for all four classes that declare any — and the fact that
  `WorldEvent.attributeMap` and `CustomQuest.template` being transient has real consequences for
  saves.

**Not established**

- The field **type** of each polymorphic slot, which determines the legal set of concrete classes.
  The scan records what *is* used, not what *may* be. This is the main prerequisite for a parser:
  it decides which `<requirement>` / `<effect>` element names are even legal in a given position.
- The requirement/effect **evaluation** semantics — this maps the shape, not the behaviour.
- Whether the 6 class-attribute-only types are ever written as element names by the editors.
- Whether `ComplexEnemyTemplate`'s transient `id` is re-derived from the filename at load, the way
  event ids are. If it is, `npcs/` follows the same id-from-filename rule as `events/`.

The field-level detail needed for a parser is in
`build-out/logs/system1-classes.json`, which lists every declared field with its type, visibility
and modifiers for all 53 classes. **Filter on `transient` before using it.**
