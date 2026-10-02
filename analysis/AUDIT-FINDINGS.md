# JaSBro — Audit Findings Register

**Target:** `decompiled/source/` — 616 files, ~67,000 lines, recovered from the shipped `JaSBro.jar`
(R0.1.2 final) and verified to compile (0 errors, 1,637 classes).

**Status of this document:** complete for the *mechanical* pass (all 616 files scanned, deterministic)
and for user-facing crashes. The *deep logic* pass (agent review of all 616 files) is still running and
will be folded in as a further section — see "Coverage" at the end.

---

## A. Confirmed bugs — reproduced from your own `jasbro.log`

### A1 — Equipment drag crashes with NPE ⚠️ live, reproduced 3× in your log
`gui/dnd/MyEquipmentTransferHandler.java:32-35`
```java
Item item = ((Inventory.ItemData)((JList)c).getSelectedValue()).getItem();
if (item instanceof Equipment) {
   Equipment equipment = (Equipment)item;
   this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Mode.AUTOMATIC));
```
`JList.getSelectedValue()` returns `null` when nothing is selected → immediate NPE on the EDT.
Same class, further unguarded dereferences: line 77 `this.getItem(support).getEquipmentType()`
(`getItem()` returns `null` on any error, lines 109-111); lines 106-108 cast a `get()` result that
may be `null`. **One-line fix; guaranteed win.**

### A2 — Unknown trait crashes new-game creation ⚠️ defect remains, trigger gone
`game/world/xml/TraitRequirementParser.java:12-14`
```java
String trait = requirementElement.getAttribute("trait");
Validate.notBlank(trait);
return new TraitRequirement(Trait.valueOf(trait));
```
`Trait.valueOf()` throws on an unknown name. From `rooms.xml` that propagates
`RoomLoader.loadRooms` → `RoomInfoUtil.<clinit>` → **`ExceptionInInitializerError`**, permanently
poisoning the class. Your log shows exactly this with `Trait.BEAUTICIAN`.

**Verified:** current content is clean — all trait references across 233 content files resolve against
all 372 enum constants. The *trigger* was an older `rooms.xml`. **The defect is unfixed**: any future
typo in content kills the game instead of logging a warning. Note `CharacterFileLoader.java:319`
guards the identical call with try/catch — same operation, two different failure policies.

---

## B. The `unguarded-selection` family — 7 more sites of the A1 bug class

Mechanically found across all 616 files. Every one is a `null` dereference waiting to happen:

| Location | Code |
|---|---|
| `gui/objects/div/InventoryPanel.java:177` | `selectedItem = this.itemList.getSelectedValue().getItem();` |
| `gui/pages/NewGameScreen.java:156` | `...getImageDataByTag(ImageTag.CLOTHED, slaveList.getSelectedValue().getImages());` |
| `gui/pages/NewGameScreen.java:158` | `...getImageDataByTag(ImageTag.CLOTHED, trainerList.getSelectedValue().getImages());` |
| `gui/pages/subView/CheatScreen.java:216` | `...getImageDataByTag(ImageTag.STANDARD, list.getSelectedValue().getImages());` |
| `gui/pages/subView/ShopPanel.java:220` | `...getSelectedItem().getValue() * amount * discount / 100 + ")"` |
| `gui/town/ShopMenu.java:294` | `...getSelectedItem().getValue() * amount + ")"` |

**Interesting detail:** `InventoryPanel.java:195` guards the *same* lookup properly —
`getSelectedValue() != null ? ... : null` — while line 177 in the same file does not. This is
inconsistency within a single class, which is a strong sign these are oversights rather than intent.

---

## C. Mechanical scan — complete coverage of all 616 files

Full data in `build-out/logs/static-scan.json`.

| Pattern | Hits | Files | Reading |
|---|---|---|---|
| `chained-get` (`.get(x).method()`) | 926 | 74 | Broadest unguarded-lookup surface; needs triage, many benign |
| `catch (Exception/Throwable/Error)` | 85 | 42 | Broad catching hides real failures |
| `synchronized` | 67 | 16 | Thread-safety surface is narrow and uneven |
| **`enum.valueOf(...)`** | **52** | **19** | **A2's bug class — content/code drift** |
| `static` mutable field | 33 | 16 | Uncontrolled global state |
| **empty catch** | **32** | **20** | **Silent failure — see C1** |
| `printStackTrace` | 31 | 11 | Production code leaking to stderr |
| integer division | 29 | 12 | Truncation + divide-by-zero risk |
| print-and-continue catch | 28 | 10 | Failure logged then ignored |
| `Thread.sleep` | 19 | 11 | Blocking; check EDT |
| resource not in try-with-resources | 15 | 8 | Leaks — see C3 |
| `new XStream(...)` | 10 | 4 | Per-file instantiation (performance) |
| **unguarded selection** | **7** | **5** | **Section B** |
| `System.out/err` | 5 | 3 | — |
| string concat in loop | 4 | 4 | Quadratic |

### C1 — Silent failure is systemic in the loaders
32 empty catch blocks, concentrated where it hurts most:

```
3  gui/pages/MessageScreen.java          2  gui/town/LaboratoryMenu.java
3  util/ConfigHandler.java               2  gui/town/RealEstateMenu.java
2  game/GameObject.java                  1  game/world/FameUnlockLoader.java
2  game/character/CharacterFileLoader.java   1  game/world/customContent/npc/NpcFileLoader.java
2  game/world/customContent/EventAndQuestFileLoader.java  1  game/events/EventManager.java
2  gui/pages/SelectionScreen.java        1  game/events/MessageData.java
```

**Impact:** corrupt or incompatible content is skipped with *no* diagnostic. This is very likely why
bugs in this game are hard to reproduce — the game swallows the evidence. It is also why A2 was so
hard to pin down.

### C2 — `enum.valueOf` drift risk (A2's family)
52 sites. The enum-typed ones that can throw on bad input:
`HouseType` (7), `Trait` (4), `SpecializationType` (3), `ImageTag` (2), `CharacterType` (2),
`ItemLocation` (2), `RoomSlotType` (2), `Gender`, `BaseAttributeTypes`, `LocationType`, `ActivityType`.

Each is a potential `IllegalArgumentException` on malformed content or an old save.

### C3 — Unclosed resources
```
SaveAndLoadPerformer.java:29,62        new FileWriter(file)          — never closed on exception
ItemFileLoader.java:99                 new FileWriter wrapped
FameUnlockLoader.java:34               new FileInputStream(file)
RoomLoader.java:52                     new FileInputStream(file)
EventAndQuestFileLoader.java:73,118,181,226   readers/writers
NpcFileLoader.java:78,127              readers/writers
ImageUtil.java:103                     TFileInputStream — on the hot image path
ConfigHandler.java:15,28,50            FileInputStream/FileOutputStream on config.ini
```
On Windows an unclosed handle **locks the file** — relevant to a game where users edit content and
saves in place.

### C4 — Mutable global state
`Jasbro.instance`, `Jasbro.maxTrees`, `Util.attributeTypes`, the four `Sextype.possibleSextypes*`
lists, `FurryPerks.h1/h2/h3` static `Buff`s, `AuctionHouse.slaveJob` (a mutable static `String`),
plus several loader singletons. None are final or guarded — a port must model these explicitly.

---

## D. Content is executable — the biggest port risk

40 content files contain **166 embedded BeanShell code blocks** evaluated via `bsh.Interpreter`.
59 have control flow; 21 are real algorithms (contest scoring, generators). Full analysis and
migration options in **`BSH-MIGRATION.md`**. This is the single most port-hostile fact in the codebase.

---

## E. Cross-cutting documents

| Document | Contents |
|---|---|
| `VERSION-COMPARISON.md` | Repo vs shipped: 90 classes added, 7 removed (incl. the whole `security/` sandbox), where code grew/shrank most |
| `BSH-MIGRATION.md` | The 166 embedded scripts, required API surface, migration options |
| `PERF-STARTUP.md` | Startup/load performance diagnosis with measurements |
| `ENGINE-CHOICE.md` | Engine recommendation (Godot 4 Mono + C#) with reasoning |
| `GAME-MAP.md` | Authoritative map of the whole codebase for the port |
| `AUDIT-PLAN.md` | The overall audit plan |

---

## G. Deep logic review — per-subsystem findings

Hand-read audits of the shipped decompiled source, cross-checked against the fork and against
multiple decompilers to rule out decompiler artifacts. These supersede the mechanical scan for the
subsystems they cover.

| Report | Scope | Findings |
|---|---|---|
| [`audit/BUSINESS-ACTIVITIES.md`](audit/BUSINESS-ACTIVITIES.md) | `activities/sub/business/*` + `whore/Whore` — the densest game rules | **34** (3 critical, 13 high) |
| [`audit/EVENTS-HOUSING-REALESTATE.md`](audit/EVENTS-HOUSING-REALESTATE.md) | `events/`, `quests/`, `housing/`, `realestate/`, `RoomLoader` | **15** (2 high, 8 medium) |
| [`audit/CONTENT-SCRIPTING.md`](audit/CONTENT-SCRIPTING.md) | `world/xml/`, `world/customContent/**`, `RoomLoader` — the BeanShell/XML engine | **15** (1 critical, 4 high) |
| [`audit/TRAITS-PERKS.md`](audit/TRAITS-PERKS.md) | `traits/` + `perktrees/` — the perk system | **18** (1 critical, 6 high) |
| [`audit/INVENTORY-ECONOMY.md`](audit/INVENTORY-ECONOMY.md) | `items/`, `world/market/`, `stats/`, and every money call site | **40** (15 high, 16 medium) |
| [`audit/CHARACTER-LIFECYCLE.md`](audit/CHARACTER-LIFECYCLE.md) | `Charakter`, conditions, attributes, ageing, pregnancy, time | **25** (2 high, 11 medium) |

**151 findings total**, all in the shipped R0.1.2 build, not the fork.

**How each finding is treated is now decided** — see [`PORT-POLICY.md`](PORT-POLICY.md): crashes and
pure arithmetic get fixed, balance-affecting behaviour is preserved deliberately and documented.

The economy finding is worth stating plainly, because it is the largest single theme in the whole
audit: **money is created and destroyed continuously during ordinary play.** The engine has a
clamped payment API — `payFixed`/`pay` return what was actually collected — and a large fraction of
callers ignore it. Drinks are credited at the nominal price to customers who could not pay (H1/H3),
tips are computed by *reading* a customer's money and never charging them (H4/H5), and the entire
GROUP path charges customers while crediting **nothing** (H6). None of these are edge cases.

Highest-impact results:

- **Two shipped-content defects crash a quest in normal play.** `freeBunnysuit.xml` asks for item
  `XX_JSbro_Dress Uniform Bertending` (a typo — `Bartending` exists) and sets `questStage` to 1 when
  the quest defines only index 0. The quest is marked `SOLVEDFORGOOD` *before* the crash, so the
  reward is permanently lost. Verified exhaustively: of the 10 distinct `itemId`s referenced by all
  shipped content, **exactly 2 are dangling**, and this is one. `dungeon1UnlockEvent.xml` references
  `dungeon1Map`, which does not exist. **Exact patch documented but NOT applied** — see
  [`audit/CONTENT-SCRIPTING.md`](audit/CONTENT-SCRIPTING.md) appendix.
- **`Bartend.java:493`** / `Attend.java:1783` — a loop bounded by `i + Oral/20` indexes past the
  customer list. *Verified by direct re-read.*
- **`Attend.java:65`** — one DANCER character empties `bartenders`, then `getInt(0, 0)`. *Verified.*
- **`SubmitToMonster.java:48`** — a poor customer empties `possibleMonsters` → `nextInt(0)`.
- **`BathAttendant.java:216-242`** — a `switch` with **every `break` missing**; a VAGINAL customer
  falls through five cases.
- **All 8 marketing target/niche perks are no-ops** (`MarketingPerks.java:177`) — `listHouses` is
  allocated and iterated but never populated, so 800 points of purchasable perks do nothing.
- **`realestate/` is entirely dead code** — `RealEstateSystem` is referenced nowhere. **Do not port it.**
- **`BuyPlotMapMenu.java:114`** — charges 100,000 per click with no affordability check, records nothing.

**Port-critical structural facts** (beyond individual bugs):

- Perk effects are **enum-initialised singletons**, so any mutable field on a `TraitEffect` — and the
  `static Buff h1/h2/h3` in `FurryPerks` — is **global state shared across all characters**.
- A large share of perk behaviour lives **not in the trait** but in **trait-name string checks** inside
  activity classes and XML `trait="…"` requirements.
- **16 perktree effect classes are never instantiated** — most duplicate a name check elsewhere.
  Restoring them in the port would double-count.
- `customContent` uses **one shared `bsh.Interpreter`** plus **two parallel variable stores**
  (`CustomQuest.getAttributeMap()` vs `Quest.variables`), and trigger→event mapping relies on
  **XStream relative object references**. Port to stable trigger ids + explicit re-map.
- Content XML is loaded by **unrestricted XStream** with no type allow-list.

**Cross-validated:** three audits independently confirmed there is **no content↔code drift in the
shipped R0.1.2 content** — all `rooms.xml` ids, all 42 activity ids, all character traits and all
specializations resolve to real enum constants. The `TraitRequirementParser` crash (A2) is a latent
defect, not a content error.

---

## F. Coverage — what's done and what isn't

**Done:**
- Complete mechanical scan of **all 616 files** (section C) — deterministic, reproducible.
- Two user-facing crashes reproduced from your log (A1, A2) with root cause.
- The 7-site unguarded-selection family (B).
- Full repo-vs-shipped comparison (E).
- BeanShell content-scripting census (D).
- Startup performance diagnosis (E).
- **Deep logic review of four subsystems (G)** — business activities, events/housing/realestate,
  content scripting/parsers, traits/perks, and inventory/economy. **126 findings**, with the
highest-impact ones independently re-verified by re-reading the cited lines.
- The **port foundation** (`port/`) — 60 parity checks against real content.

**Not done:**
- Deep logic review of the remaining subsystems — **GUI, save/load, the character/training systems,
  inventory/economy, and the map/town screens**.
- Save/load XStream round-trip analysis.
- Content validator tool (planned Phase 0 — never built).
- **The BeanShell migration decision** — gates much of the port's architecture.

**Honest caveat:** the numbers in section C are mechanical pattern hits, not confirmed bugs. `chained-get`
at 926 hits in particular needs triage — most will be benign. Sections A and B are confirmed or
near-confirmed; C is a prioritised worklist.
