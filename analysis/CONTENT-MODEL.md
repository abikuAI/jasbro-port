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

**Everything dispatches on the `type` attribute.** The element name is *nearly* always
`<requirement>` on the activity side and `<char-requirement>` on the character side — but see
[the mislabelled element](#a-mislabelled-element-that-works-by-accident) below, because one element
breaks the convention and the loader does not care.

### The two registries, exactly

`RoomLoader`'s static initialiser builds **two maps keyed by the `type` attribute string**. These
are the complete contents; there are no other keys.

| Registry | Keys | Used by |
|---|---|---|
| `ACT_REQUIREMENTS` (8) | `all-character`, `and`, `child-care`, `exact-occupant`, `max-occupant`, `min-character`, `min-occupant`, `none` | `parseActivityRequirement` |
| `CHAR_REQUIREMENTS` (4) | `char-type`, `or`, `specialization`, `trait` | `parseCharacterRequirement` |

Note that **`specialization` exists only on the character side.** There is no activity-side
`specialization` parser, and asking for one is a load error.

### Which registry is consulted is decided by the PARENT, not the tag

This is the part that is easy to get wrong. `parseActivityRequirement` is called for the
`<requirement>` child of an `<activity>` and for every element child of an `and`. Then
`MinimumCharacterRequirement` and `AllCharacterRequirement` call **`parseCharacterRequirement`** on
their first element child — *whatever that child is named*.

So the split between "activity" and "character" requirements is determined by **where the element
sits**, not by its tag. Counting the two kinds by tag name therefore gives a slightly wrong answer,
which is exactly the mistake an early survey made.

### Activity requirement types — all 395, counted

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

### Character requirement types — all 108

| Type | Count | Note |
|---|---:|---|
| `trait` | 53 | the dominant character gate |
| `specialization` | 29 | 28 tagged `<char-requirement>`, 1 tagged `<requirement>` |
| `char-type` | 24 | reads the `char-type` attribute, not `type` |
| `or` | 2 | rare — only 2 of 108 |

395 + 108 = 503 requirement elements, which is the same total as the raw tag count (396 + 107).
The split just moves one element across.

### A mislabelled element that works by accident

`rooms.xml:645` writes:

```xml
<activity id="SELLFOOD">
  <requirement type="min-character" count="1">
    <requirement type="specialization" specialization="MAID" />
  </requirement>
</activity>
```

That inner element is a **character** requirement spelled with the activity tag. It loads fine,
because `MinimumCharacterRequirement` hands its first element child to `parseCharacterRequirement`,
which looks only at `type` and finds `specialization` in `CHAR_REQUIREMENTS`.

Line 645 is in the KITCHEN room. The real loader resolves it to
`min-character(1, specialization(MAID))`, and that exact string is asserted in the port's
verification suite.

**`parseActivity` is the only place in the whole loader that examines a tag name** — it compares
against `child-activity` to decide between `addActivity` and `addChildCareActivity`. Everywhere
else the name is ignored. Do not "clean up" the content, and do not add element-name validation to
a port: **a port that validated element names would reject shipped content.**

---

## Why this matters for the port

### A trap worth naming

An element-name survey of the content will **not** see System 2 at all. An early version of
`survey_content.py` did exactly this, found no legacy element names, and concluded the
`jasbro/game/world/xml/` package was dead code. **That conclusion was wrong** — `RoomLoader` uses
every one of those parsers. Absence of evidence in an element-name scan is not evidence of absence
when dispatch is by attribute.

The corrected script now scans `rooms.xml` by attribute value and prints both tables above.

### The same trap, one level deeper

Correcting the script to read the `type` attribute was **not sufficient**. The corrected script
still grouped requirements by **tag name** — `<requirement>` versus `<char-requirement>` — and
reported one activity-side `specialization`. That number is wrong in a way that matters: the element
in question is a *character* requirement sitting under `min-character`, and it is dispatched as a
character requirement.

The general lesson, now learned twice in the same file: **reading structure from names is not the
same as reading structure from the code that consumes it.** The authoritative answer comes from
following which registry each parse call selects, which means reading the parsers. Both tables above
were re-derived that way, and the port's parity suite asserts the resulting counts.

The practical consequence for anyone auditing this content: an attribute-based count can still be
wrong if the *context* of a nested element is what actually decides its type.

### Consequence

`Simbro.Core` needs **two content front-ends**:

1. a **class-attribute polymorphic** reader, which can reuse the save reader's node model; and
2. an **attribute-dispatched** reader with its own parser registry keyed on `type`.

They cannot be unified without inventing a format neither game version uses.

### Latent defect: occupant counts are validated as a single digit

The three occupant parsers validate `count` like this:

```java
Validate.matchesPattern(count, "[0-9]", "Value '%s' for 'count' in 'min-occupant' ...");
```

`Validate.matchesPattern` anchors the pattern to the **whole** string, so `[0-9]` accepts **exactly
one digit**. `count="10"` would throw a `ValidationException` rather than load.

This is **latent, not active**: every `min-occupant`, `max-occupant` and `exact-occupant` value in
the shipped file is between 1 and 5, so nothing in the shipped content trips it. It would bite the
first content author to write a ten-occupant requirement.

Two smaller oddities in the same three classes, worth knowing before trusting the messages:

- the error text says **`'min-occupant'`** in all three, including maximum and exact — a
  copy-paste artefact, so a failure message can name the wrong attribute;
- `MaximumOccupantRequirement`'s field is `maximum` and `MinimumOccupantRequirement`'s is
  `minimum`, but `ExactOccupantRequirement`'s is `count` — relevant only to reflection-based tools,
  which is how the golden fixture reads them.

**Port decision (Class A fix, see [`PORT-POLICY.md`](PORT-POLICY.md)):** `Simbro.Core` accepts any
non-negative integer instead of reproducing a crash on valid content. Behaviour on all shipped
content is therefore identical — which is what the parity test checks — while new content is not
silently restricted.

---

### Where the behaviour lives

System 2's requirement classes are *tiny* (`NoActivityRequirementParser` is 7 lines — it is a
predicate object). The interesting logic is in the **activity** implementations
(`Attend`, `Bartend`, `Strip`, …), which are the port's largest work item at ~10,000 lines
combined. System 2 only decides **which** activities are offered; System 1 decides what happens
during events.

### What the port does with System 2 today

`RoomLoader` is ported and verified: `Simbro.Core/Content/Rooms/` holds `RoomDefinition`,
`ActivityRequirements`, the two enums, and a loader that reproduces the registries above.

Verification is by golden fixture, not by hand-written expectation.
`fixtures/java/RoomsFixture.java` runs the **shipped game's own `RoomLoader`** and reflects into
`RoomInfo`'s private requirement maps (`RoomInfo` exposes no getter for them, deliberately), dumping
`fixtures/rooms-golden.txt`. The C# verifier then loads the same `rooms.xml` and compares every
field of every room. All **29 rooms** match on cost, occupancy, image, slot set, activity list, and
the full requirement tree — plus the line-645 mislabelling case as an explicit named assertion.

Still absent from the port: the `IsValid` predicates. They need `Util.TypeAmounts`, which is not yet
modelled, and inventing its shape from guesswork would be worse than waiting.

---

## Still to establish

- **`IsValid` for System 2's requirements.** The parse tree is verified; the predicates are not
  ported, because they consume `Util.TypeAmounts` and that tally is not yet modelled. Boarding the
  predicates before the tally would mean guessing the tally's shape.
- **Whether the `IsValid` predicates are even reachable for every requirement type.** `child-care`
  requirements live in a separate map consulted by `isChildCareActivityValid`, so whether a
  `<child-activity>` gate is ever evaluated depends on a caller that has not been traced.
- **The exact serialisation of System 1's requirement/effect graph.** The `class` attribute gives
  the type, but the field set per class has not been enumerated, and `reference` (41 occurrences)
  implies shared sub-objects that must preserve identity.
- **Whether `events/` content is reached at all without the editors.** The loader recurses to depth
  5 and is CWD-relative, so the shipped layout matters.
- **The generated-vs-authored question** for the 3 editor jars in the game folder — if the editors
  rewrite these files, hand-editing content is not durable.
