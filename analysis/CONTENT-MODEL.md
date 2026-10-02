# The content model — two systems, not one

**The single most useful thing to know before porting content: JaSBro has TWO unrelated content
systems, and they share no code, no parser, and no file format conventions.**

A port that implements only one of them will appear to work and then silently fail on the other
half of the game. This was established by surveying the shipped files
(`tooling/survey_content.py`), not by reading the class list.

---

## System 1 — `events/` + `quests/` : XStream, class-attribute polymorphism

**62 files** (51 events, 11 quests), loaded by `EventAndQuestFileLoader`
(`jasbro/game/world/customContent/EventAndQuestFileLoader.java:38-208`).

| Property | Value |
|---|---|
| Parser | `new XStream(new StaxDriver())` with `autodetectAnnotations(true)` |
| Root types | `jasbro.game.world.customContent.WorldEvent`, `CustomQuestTemplate` |
| Polymorphism | **`class` attribute** holding an FQCN — 175 occurrences |
| References | **`reference` attribute** — 41 occurrences |
| Distinct elements | 102 |
| BeanShell | **166 `<code>` blocks** |
| Discovery | recursive to **depth 5**, files matching `.+\.xml` |
| Path | `new TFile("events")` / `new TFile("quests")` — **relative to the working directory** |
| ID | the **filename minus `.xml`** |

This is the same serialisation mechanism as saves, so the port's existing XStream reader
(`Simbro.Core/Save/XStreamNode.cs`) already understands its shape. The `class`/`reference`
attributes are exactly the two forms already verified for saves.

### The elements that actually appear

The 40 most frequent, from the real files:

```
533 background        183 subEffects        97 WorldEventEffectContainer    62 id
424 target            179 ImageSelection    91 Trigger                     51 WorldEvent
356 imageLocation     177 WorldEventSimpleMessage  87 triggers              51 effects
356 imageTags         177 message           86 WorldEventCondition          50 triggerType
186 image             177 importantMessage  79 WorldEventCode               47 source
166 code              127 requirement       73 displayHeight               41 entry
```

Effects present in shipped content: `WorldEventSimpleMessage` (177), `WorldEventEffectContainer`
(97), `WorldEventCondition` (86), **`WorldEventCode` (79)**, `WorldEventSetQuestStage` (32),
`WorldEventSaveVariable` (28), `WorldEventSetQuestStatus` (26), `WorldEventLoadVariable` (19),
`WorldEventChangeAttribute` (15), `WorldEventGainItem` (14).

Requirements present: `ChanceRequirement` (18), `AttributeRequirement` (17), `ActivityRequirement`
(16), `MainCharacterRequirement` (13), `LocationTypeRequirement` (8), `NotRequirement` (8).

**`WorldEventCode` at 79 is the port's biggest single item** — it is the element whose body is
BeanShell. See [`BSH-MIGRATION.md`](BSH-MIGRATION.md).

---

## System 2 — `rooms.xml` : attribute-dispatched DOM, no XStream

**1369 lines**, loaded by `RoomLoader` (`jasbro/game/world/RoomLoader.java`), which registers
parser objects into two maps keyed by **string** (`RoomLoader.java:211-222`).

```xml
<room id="EMPTYROOM" image="images/backgrounds/emptyroom.jpg" cost="10" max-occupancy="6">
  <slots><slot type="SMALLROOM" /></slots>
  <activities>
    <activity id="SEX">
      <requirement type="exact-occupant" count="2" />
    </activity>
    <activity id="TRAIN">
      <requirement type="and">
        <requirement type="exact-occupant" count="2" />
        <requirement type="min-character" count="1">
          <char-requirement type="char-type" char-type="TRAINER" />
        </requirement>
      </requirement>
    </activity>
  </activities>
</room>
```

**Everything dispatches on the `type` attribute, never on the element name.** All 396 activity
requirements are spelled `<requirement>`; all 107 character requirements are spelled
`<char-requirement>`. The element name carries no information.

### Activity requirement types — all 396, counted

| Type | Count | Parser |
|---|---:|---|
| `and` | 89 | `AndActivityRequirementParser` |
| `min-character` | 83 | `MinimumCharacterRequirementParser` |
| `exact-occupant` | 70 | `ExactOccupantRequirementParser` |
| `none` | 49 | `NoActivityRequirementParser` |
| `min-occupant` | 47 | `MinimumOccupantRequirementParser` |
| `max-occupant` | 22 | `MaximumOccupantRequirementParser` |
| `all-character` | 21 | `AllCharacterRequirementParser` |
| `child-care` | 14 | `ChildCareRequirementParser` |
| `specialization` | 1 | `SpecializationRequirementParser` |

### Character requirement types — all 107

| Type | Count | Note |
|---|---:|---|
| `trait` | 53 | the dominant character gate |
| `specialization` | 28 | |
| `char-type` | 24 | |
| `or` | 2 | rare — only 2 of 107 |

`RoomLoader` also registers `ChildCareRequirementParser`, `MinimumCharacterRequirementParser` and
others on the *character* side; the counts above are what the shipped file actually uses, not what
the code supports.

---

## Why this matters for the port

### A trap worth naming

An element-name survey of the content will **not** see System 2 at all. An early version of
`survey_content.py` did exactly this, found no legacy element names, and concluded the
`jasbro/game/world/xml/` package was dead code. **That conclusion was wrong** — `RoomLoader` uses
every one of those parsers. Absence of evidence in an element-name scan is not evidence of absence
when dispatch is by attribute.

The corrected script now scans `rooms.xml` by attribute value and prints both tables above.

### Consequence

`Simbro.Core` needs **two content front-ends**:

1. a **class-attribute polymorphic** reader, which can reuse the save reader's node model; and
2. an **attribute-dispatched** reader with its own parser registry keyed on `type`.

They cannot be unified without inventing a format neither game version uses.

### Where the behaviour lives

System 2's requirement classes are *tiny* (`NoActivityRequirementParser` is 7 lines — it is a
predicate object). The interesting logic is in the **activity** implementations
(`Attend`, `Bartend`, `Strip`, …), which are the port's largest work item at ~10,000 lines
combined. System 2 only decides **which** activities are offered; System 1 decides what happens
during events.

---

## Still to establish

- **The exact serialisation of System 1's requirement/effect graph.** The `class` attribute gives
  the type, but the field set per class has not been enumerated, and `reference` (41 occurrences)
  implies shared sub-objects that must preserve identity.
- **Whether `events/` content is reached at all without the editors.** The loader recurses to depth
  5 and is CWD-relative, so the shipped layout matters.
- **The generated-vs-authored question** for the 3 editor jars in the game folder — if the editors
  rewrite these files, hand-editing content is not durable.
