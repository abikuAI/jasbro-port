# Audit — Content Scripting Engine + XML Parsers

Scope: `game/world/xml/` (14 files), `game/world/customContent/**`, `RoomLoader.java` — 90 files,
all read. Shipped content cross-checked in `original/` **and** the live game at `C:\Games\Jasbro_Final`.

**Fork-twin note:** almost every `customContent/**` file has a human-written twin in
`repo/src/main/java/jasbro/game/world/customContent/`, so a defect found in both is *author intent*,
not decompiler damage. The genuinely new R0.1.2 code is `world/xml/` and `RoomLoader.java` — those
have no twin.

## How it is wired

`Jasbro.getWorldEvents()` → `EventAndQuestFileLoader.loadAllCustomEvents()` →
`addEvents(new TFile("events"), list, 5)` — **path relative to the process CWD**, recursion depth 5.
Each `*.xml` is slurped to a `String` and handed to a **fresh** `XStream(new StaxDriver())` with
`autodetectAnnotations(true)`. **The object id comes from the filename minus `.xml` — the file's own
`<id>` element is ignored** (`:194`). Quests load identically from `"quests"`. `RoomLoader` is
separate: a DOM parse of `rooms.xml`, invoked exactly once from `RoomInfoUtil.<clinit>` with `null`.

Execution uses **one shared `bsh.Interpreter`** (`Jasbro.java:608-623`). `WorldEvent.getInterpreter()`
and `CustomQuest.getInterpreter()` point at that global and copy their `attributeMap` into its
namespace. Trigger → `TriggerRequirement.isValid` → `CodeRequirement.java:16` `eval`. Teardown:
`QuestManager`/`EventManager` call `event.reset()` in a `finally` after every event, and `reset(true)`
→ `Jasbro.cleanupInterpreter()` unsets **every** variable in the shared interpreter.

---

## CRITICAL — shipped content that breaks in normal play

> ### ⚠️ `WorldEventGainItem` + 2 dangling item ids
> ```java
> Item item = Jasbro.getInstance().getItems().get(this.itemId);   // no existence check
> Jasbro.getInstance().getData().getInventory().addItem(item);
> ```
> `addItem(null)` does `item.getId()` → **NPE**. `WorldEvent.execute` catches only `EvalError`, so it
> escapes to `QuestManager.java:108-111 catch (Exception)`, which **removes the quest from
> `activeQuests`** — the quest and its reward are destroyed.

### Exhaustive verification (this audit)

Across **all** shipped content, only **10 distinct `itemId`s** are referenced. **Exactly 2 are
dangling** — and they are the two reported. No others.

| Dangling id | Referenced by | Correct value |
|---|---|---|
| `XX_JSbro_Dress Uniform Bertending` | `events/freeStuffEvents/freeBunnysuit.xml` | `XX_JSbro_Dress Uniform Bartending` (**exists** — unambiguous typo) |
| `dungeon1Map` | `events/dungeonEvents/dungeon1UnlockEvent.xml` | `BlueprintDungeon` (the only dungeon item that exists) |

### The FreeBunnySuitQuest chain — the full failure

`quests/freeStuffQuests/FreeBunnySuitQuest.xml` defines exactly **one** `CustomQuestStage`
(valid index **0**). Its trigger is `ACTIVITYPERFORMED` + `ActivityRequirement(BARTEND)` +
`AttributeRequirement(BARTENDING > 30)` + `ChanceRequirement(100)` — i.e. **it fires the first time
any character bartends with skill over 30.** That is ordinary play.

`events/freeStuffEvents/freeBunnysuit.xml` then runs its effects in this order:

1. `WorldEventSimpleMessage` — the gift-box message
2. **`WorldEventSetQuestStage` → `<questStage>1</questStage>`** ← out of range (only index 0 exists)
3. `WorldEventSetQuestStatus` → `SOLVEDFORGOOD`
4. **`WorldEventGainItem` → `XX_JSbro_Dress Uniform Bertending`** ← **NPE**
5. `WorldEventGainItem` → `XX_JSbro_Head Uniform Bartending` (exists)

So the quest is marked **permanently solved** and then crashes — the player never receives either
item, and two separate latent defects are stacked such that only the first is ever visible.

**Fixing the typo alone is not enough:** `setCurrentStage(1)` only stores an int, so the failure
surfaces later at `getStages().get(1)` (`Quest.java:36-38`, `CustomQuest.showInQuestLog():131`) as an
`IndexOutOfBoundsException` — also outside `catch (EvalError)`. **Both must be fixed.**

---

## HIGH

**`WorldEventSetQuestStage.java:14-15` + `Quest.java:36-38` — no bounds check either side.**
Every other stage/value pair in shipped content is in range (verified per file; e.g. `nhQuest` has 12
stages, max index 11). `freeBunnysuit.xml` is the sole offender.

**`CustomQuestStage.java:52-53` (also `:92`, `:96`) — two unguarded `Map.get()` calls.**
`triggerToWorldEventMap` is **identity-keyed** (`Trigger` has no `equals`/`hashCode`). The class's own
`getEventName():145-147` exists to null-check this; `handleEvent`/`modifyActivities` do not. Latent for
shipped content (all 11 quest files have matching trigger/entry counts), immediate for hand-edited content.

**`RoomLoader.java:64` + `RoomInfoUtil.java:46` — swallowed failure chain.**
All four catch handlers log and fall through with `doc == null` → `doc.getDocumentElement()` NPE. The
caller then dereferences hard-coded ids unguarded in a static initialiser. Surfaces as
`ExceptionInInitializerError` from `RoomInfoUtil.<clinit>` — **the game cannot start.** This is a
**distinct cause** from the known `Trait.valueOf` crash that funnels through the same `<clinit>`.

**`WorldEventRemoveCharacter.java:17-25` — divergent state mutation.**
Reimplements only the first loop of `Jasbro.removeCharacter` (`:440-447`) and drops: equipment return
to inventory (`:449-453`), `new MyEvent(EventType.CHARACTERLOST, character)` + dispatch (`:455-456`),
and protagonist/game-over handling. **Equipment is destroyed and no `CHARACTERLOST` listener fires**
(`Trigger.TriggerType.CHARACTERLOST` exists, `Trigger.java:67`). Identical in the fork.

---

## MEDIUM

**`CharacterTypeRequirement.java:13` — requirement fails OPEN.**
```java
return this.characerType == null ? true : character.getType().equals(this.characerType);
```
An unset/typo'd type makes the requirement **always true**. Sibling `CharacterIdRequirement.java:12`
fails **closed** for the same case. Identical in the fork — intended-but-wrong, not a decompiler artifact.

**`TraitRequirement.java:18`, `SpecializationRequirement.java:18`+`:13` — missing guards siblings have.**
`TraitRequirement` derefs `getCharacters().get(0)` unguarded, unlike `AttributeRequirement:32-34`,
`CharacterIdRequirement:17-19`, `FameRequirement:17-19`. These fire for event types with no
`characters` attribute (`NEXTDAY`/`SHIFTSTART`). NPE is not `EvalError`, so `WorldEvent:68` does not catch it.

**Empty requirement list dereferenced** — `AnyOfOwnedCharactersRequirement.java:12`,
`MinimumCharactersMatchRequirement.java:15`. `canAddRequirement` permits empty;
`AllCharactersRequirement:10` and `MainCharacterRequirement:11` guard the identical access.

**Null `characters`/`activity` NPE family** — `ExactCharacterAmountRequirement:15`,
`MaximumCharacterAmountRequirement:11`, `MinimumCharacterAmountRequirement:11`,
`MinimumCharactersMatchRequirement:14`, `ActivityRequirement:12`, plus `WorldEventAbortActivity:11`
and `WorldEventAddToMessage:17-18`. The classes that *do* guard (`FameRequirement`,
`AttributeRequirement`, `NoChildPresentRequirement`, `ChildCareRequirement`) show the authors knew.

**Unguarded lookup on content-supplied names** — `WorldEventChangeFame.java:15`,
`WorldEventChangeAttribute.java:22`, `WorldEventProtectCharacter.java:14`, `ImageSelection.java:26`,
`WorldEventCallAnotherEvent.java:14-15`. Unknown name → `null` → NPE; a name resolving to a
non-`Charakter` (`"data"`, `"people"`, `"activity"` are valid namespace entries) → unchecked
`ClassCastException`. `SaveVariable`/`LoadVariable:15` additionally require `worldEvent.getQuest() != null`.

**Remaining unguarded `Enum.valueOf` on content strings** — `RoomLoader.java:162` (`RoomSlotType`),
`:181` (`ActivityType`), `xml/SpecializationRequirementParser.java:14`,
`xml/CharacterTypeRequirementParser.java:14`. All shipped values resolve today (all 42 distinct
`<activity id>` against the 59 `ActivityType` constants, and all 4 `<slot type>` values), but these run
inside `RoomInfoUtil.<clinit>`, so one unknown token in a custom `rooms.xml` reproduces the known
startup `ExceptionInInitializerError` through **four new paths**.

**Unrestricted XStream deserialisation** — `EventAndQuestFileLoader.java:71-84`, `:179-192`. No
`allowTypes`/security setup: any class named in a content file is instantiated reflectively. Content is
already arbitrary-code via BeanShell by design, so marginal risk is limited to third-party packs — but
that *is* the game's user-extensible surface.

**Port-risk / save-compat — one global interpreter, two parallel variable stores.**
`CustomQuest.getAttributeMap()` is a **different map** from `Quest.variables` (what
`CustomQuestStage.customize` reads and `WorldEventSaveVariable` writes) — shipped content depends on
the split (**56 `SaveVariable`/`LoadVariable` elements**). Collapsing them in C# changes behaviour both
ways. Status `INACTIVE` clears only `quest.getVariables()` (`:26`), leaving the interpreter copy live.
`CustomQuestStage.handleEvent:53` aliases the event's `attributeMap` to the quest's. And
`triggerToWorldEventMap` relies on **XStream relative object references**
(`<…Trigger reference="../../../triggers/…[2]">`) — C# serialisers will not reproduce that; port to
stable trigger **ids** with an explicit re-map step.

---

## LOW

- `ImageSelection.java:39` — `imageTags` is never initialised (`:20`); `getImageData` bypasses the
  lazily-initialising `getImageTags()` (`:103-110`), so an `ImageSelection` with neither field NPEs
  instead of returning the documented `null`. Identical in fork.
- `ExactOccupantRequirementParser.java:12` (+ Maximum/Minimum) — `Validate.matchesPattern(count, "[0-9]", …)`
  is whole-string, so it accepts exactly **one** digit, yet the next line does `Integer.parseInt` for
  multi-digit. `count="10"` **aborts room loading**. Shipped content uses 1–5 only. All three messages
  wrongly say `'min-occupant'`. `RoomLoader.parseCharacterRequirement:201-203` does
  `getAttribute("type")` with no null check while both callers pass a possibly-null first child.

---

## Checked and deliberately NOT reported

- `Trigger.isTriggered:14-22` looks inverted vs the fork but is **equivalent by De Morgan**;
  `CUSTOMACTIVITY`'s null `eventType` makes the comparison false rather than throwing.
- `Util.getInt(0,100) < chance` and `getInt(0, effects.size())` are **not** off-by-one — `getInt` is
  `nextInt(end-start)+start`, i.e. `[start, end-1]`, so `chance = 100` is 100% and the effect
  container cannot overrun.
- `WorldEvent.handleEvent:74`'s cast is safe — `HEALTHZERO` is only ever built as an `AttributeChangedEvent`.
- The `"({}} Error in this code: {}"` format string still yields 2 placeholders for 2 args — cosmetic.
- `WorldEventChangeAttribute`'s `addToActivity` has no accessor, matching the fork's
  `//Enable setting this in the editor` comment; XStream sets private fields directly.
- `RecurringDayRequirement.java:20`'s odd `currentDay < offset/everyXDays` is byte-identical to the
  fork — original design intent.

---

## Bottom line for the port

Two shipped-content defects fire in normal play, both in the `FreeBunnySuitQuest` chain, and both are
invisible today only because the NPE happens first. Port `WorldEventGainItem`,
`WorldEventCallAnotherEvent`, `WorldEventSetQuestStage` and the requirement classes **with explicit
existence + bounds validation**, and replace event-id/trigger **object identity** with explicit data.

---

## Appendix — exact content patch (documented only, NOT applied)

> The live game at `C:\Games\Jasbro_Final` was deliberately **left unmodified**. This is the change
> that would fix both defects, recorded so it can be applied later without re-deriving it.

**File 1 — `events/freeStuffEvents/freeBunnysuit.xml`** (two edits, same line)

| Find | Replace | Why |
|---|---|---|
| `<questStage>1</questStage>` | `<questStage>0</questStage>` | `FreeBunnySuitQuest.xml` defines exactly one stage; valid index is 0 |
| `<itemId>XX_JSbro_Dress Uniform Bertending</itemId>` | `<itemId>XX_JSbro_Dress Uniform Bartending</itemId>` | `Bertending` is a typo; the item id comes from the filename |

The second `<itemId>XX_JSbro_Head Uniform Bartending</itemId>` in that file is **correct** — leave it.

**File 2 — `events/dungeonEvents/dungeon1UnlockEvent.xml`** (one edit)

| Find | Replace | Why |
|---|---|---|
| `<itemId>dungeon1Map</itemId>` | `<itemId>BlueprintDungeon</itemId>` | No `*Map*` item exists anywhere; `BlueprintDungeon` is the only dungeon item. **This is an inference, not a certainty** — verify the intended reward before relying on it. |

**Both edits are required for the bunny quest.** Fixing only the item id moves the failure to
`getStages().get(1)`; fixing only the stage moves it to the `null` item.

Back up before applying:

```powershell
$stamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$bk = "C:\Games\Jasbro_Final\_backup-$stamp"
New-Item -ItemType Directory -Path "$bk\events\freeStuffEvents","$bk\events\dungeonEvents" -Force
Copy-Item "C:\Games\Jasbro_Final\events\freeStuffEvents\freeBunnysuit.xml"      "$bk\events\freeStuffEvents\"
Copy-Item "C:\Games\Jasbro_Final\events\dungeonEvents\dungeon1UnlockEvent.xml" "$bk\events\dungeonEvents\"
```

Note: these files are **single-line XML**, so a find/replace must target the literal substrings above,
not whole lines. An existing save will still have the quest recorded as `SOLVEDFORGOOD` — the fix
prevents the bug for new games or unsolved saves; it does not retroactively grant a lost reward.
