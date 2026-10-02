# JaSBro — Version Comparison: GitHub fork vs shipped game

Comparison of the **GitHub fork** (`kingpingpong/JaSBro` @ `11a9560`, master, 2015-04-18) against the
**shipped game** (`R0.1.2 final`, as recovered by decompiling `JaSBro.jar`).

Method: file-set comparison of `repo/src/main/java/jasbro/` (533 files, the tree Gradle actually
compiles) against `decompiled/source/jasbro/` (616 files). Divergence measured on comment- and
whitespace-stripped text; size deltas reported in normalized characters.

---

## Headline

**The repo is an *earlier* snapshot.** The shipped game is not a minor patch — it is a substantially
extended build. Between the fork and release:

- **90 classes were added**
- **7 classes were removed**
- **526 classes are shared**, and only **9 of those are byte-identical** once formatting and
  decompilation artifacts are stripped — i.e. nearly everything moved.

The repo cannot be treated as "the source" for the port. It is useful as (a) an older API reference
and (b) the only source of **original comments** — 1,108 comment lines in the repo vs 36 decompiler
artifacts in the decompiled tree.

---

## 1. File-set differences

| | Count |
|---|---|
| Repo files | 533 |
| Shipped files | 616 |
| **Shared** | **526** |
| **Only in shipped** (added) | **90** |
| **Only in repo** (removed) | **7** |

### 1.1 Added in the shipped build (90 classes)

| Files | Package | What it means |
|---|---|---|
| 25 | `game/character/traits` (+ `perktrees/`) | **Perk trees** — 17 perk-tree classes (Alchemist, Bartender, Dancer, Dominatrix, Fighter, Furry, Kinky, Legacy, Maid, Marketing, Nurse, Sex, Slave, Thief, Trainer, Whore…) |
| 21 | `gui/town` | **A whole new town/menu system** — AuctionHouse, SlaveMarket, SlavePens, SlaverGuild, AdventurersGuild, BuildersGuild, Alchemist, Laboratory, School, Shop, Trainer, RealEstate, Quest, GeneralMarket ×2, InteriorDecoration, TownMenuNew |
| 14 | `game/world/xml` | **XML parser infrastructure** — the file-driven content system (see §3) |
| 4 | `game/character/activities` | New activities |
| 3 | `game/character/battle` | Battle extensions |
| 3 | `game/items/ingredientEffect` | Ingredient effects |
| 3 | `game/realestate` | `Plot`, `RealEstateSystem`, `BuyPlotMapMenu` — **real estate as a system** |
| 4 | `game/housing` | `HouseUtil`, `RoomUnlock`, `SecurityState`, `RoomLocationType` |
| 3 | `game/items` | `IngredientItem`, `LootItem`, `SummoningItem` |
| 1 | `game/world/market` | Market |
| 1 | `game/items/…` + 4 editor panels | Editor support for the new item types |
| — | misc | `QuestScreen`, `MonsterDickType`, `MonsterUtil`, `Struggle`, `Ritual`, `Rob`, `WorkForGuild`, `Crypt`, `SecurityPanel`, `LegacyEventHandler` |

**Interpretation:** post-fork development concentrated on **breadth of content and systems** —
perk trees, a full town economy, real estate, new item categories, and XML-driven content.

### 1.2 Removed in the shipped build — ⚠️ the whole security sandbox

```
MyException.java
game/character/activities/sub/FreeTime.java
game/housing/RoomType.java
security/BshPermissionCollection.java
security/JasbroPermissionCollection.java
security/JasbroPolicy.java
security/ThreadPoolAccessPermission.java
```

**`jasbro/security/` was deleted entirely.** That package was a `SecurityManager`-based sandbox for
the BeanShell scripting the game uses to run content-defined logic. The shipped build still calls
`AccessController.doPrivileged(...)` in several places (`ImageUtil`, `MyImage`, `RPGView`,
`EventAndQuestFileLoader`, `NpcFileLoader`, …), but with no `SecurityManager` and no `Policy`
installed these are **no-ops**.

So the released game executes content-supplied code **unsandboxed**. For a single-player offline
game with local content this is a modding feature rather than a vulnerability — but it is a
deliberate architectural reversal, and it matters for the port (§3).

---

## 2. Where the code changed most

Similarity ratios are noisy (decompiled text never matches original source), so the **size delta** is
the more honest signal.

### 2.1 Biggest growth

| Δ normalised chars | Ratio | File |
|---|---|---|
| +70,432 | ×3.0 | `game/character/activities/sub/business/Attend.java` |
| +31,353 | ×2.1 | `game/character/activities/sub/business/Bartend.java` |
| +23,179 | ×15.1 | `game/world/market/AuctionHouse.java` |
| +19,602 | ×1.7 | `game/character/activities/sub/business/Strip.java` |
| +11,578 | ×3.3 | `game/character/activities/sub/Relax.java` |
| +11,544 | ×1.9 | `game/character/conditions/Buff.java` |
| +10,054 | ×2.0 | `game/character/activities/sub/business/PublicUse.java` |
| +9,936 | ×2.6 | `game/character/activities/sub/Publicize.java` |
| +9,030 | ×2.2 | `game/character/activities/sub/Advertise.java` |
| +8,999 | ×1.6 | `game/character/activities/sub/business/BathAttendant.java` |
| +8,446 | ×2.0 | `gui/RPGView.java` |
| +8,426 | ×7.5 | `game/character/activities/sub/Govern.java` |

**The activity/business simulation is where the game grew the most.** `Attend` alone tripled. If you
port anything faithfully, it is this.

### 2.2 Biggest shrinkage (refactors)

| Δ normalised chars | File | Note |
|---|---|---|
| −98,834 | `game/character/traits/Perks.java` | **Monolith split up** — the perk classes moved into `traits/perktrees/`. Repo `Perks.java` was 20× larger. |
| −12,013 | `game/housing/RoomInfoUtil.java` | Housing refactored |
| −9,545 | `game/housing/House.java` | Housing refactored |
| −1,254 | `game/character/activities/ActivityType.java` | Activity system reworked |

**These two facts together are the key structural story:** the shipped build both *split* the perk
monolith into a tree package and *deepened* the activity simulation. Both are central to a port.

---

## 3. ⚠️ The finding that most affects a port: content is executable

**40 content XML files contain 166 embedded BeanShell code blocks.**

Real examples from your install:

```java
// events/travellingMerchantEvent.xml
Jasbro.getInstance().getGui().addMessage(new TravellingMerchantScreen());

// events/controlLowSearchCharacter.xml
data.characters.add(capturedChar);

// events/preventCharacterSpawn.xml
if (characterIds != null) {
    for (int i = 0; i < characterIds.length; i++) {
        characterIds[i] = characterIds[i].toLowerCase();
    }
}

// events/contestQuest/contestQuestBartenderContest.xml
int priceMoney = 0;
int skillPoints = 0;
if (difficulty == 1) { priceMoney = 10000; }
else if (difficulty == 2) { priceMoney = 100000; skillPoints = 1; }
```

These are executed at runtime via `bsh.Interpreter.eval(...)`:

| Location | Code |
|---|---|
| `WorldEventCode.java:19` | `worldEvent.getInterpreter().eval(this.code);` |
| `CodeRequirement.java:16` | `return (Boolean)triggerParent.getInterpreter().eval(this.code);` |
| `CustomQuest.java:255` | `(String[])this.getInterpreter().eval("return this.variables;")` |
| `Jasbro.java:608-638` | `getInterpreter()` / `cleanupInterpreter()` — one shared `bsh.Interpreter` |

**Consequences for the port:**

1. **The content format is not declarative.** You cannot port this by reading XML into C# records.
   A significant amount of game logic *lives in the content files*, as BeanShell.
2. **There is no C# equivalent of BeanShell.** Options are: embed a Java-compatible interpreter
   (heavy, fragile), write a BeanShell→C# transpiler (bounded — the code is simple, but 166 blocks
   must all translate), or **hand-migrate the 166 blocks into a data-driven effect system**
   (most robust, most work).
3. **`getInterpreter()` shares one interpreter and calls `eval("return this.variables;")`** to
   reflect over the script's variable namespace into an attribute map. That's an implicit dynamic
   contract between Java and content — it must be modelled explicitly in the port.
4. **BeanShell objects are `transient`** (`WorldEvent.interpreter`, `CustomQuest.interpreter`), so
   scripts are re-established after load. The port must reproduce that lifecycle.

This should be treated as **a first-class port-design problem**, not a detail. It is the main reason
a "thin rewrite" would fail.

---

## 4. What this means for the port

**Good news**
- Domain logic is mostly plain Java objects with XStream serialization — translatable to C# records.
- The rules are low-frequency (shift/day ticks), not real-time physics.
- Content is XML on disk, so the schema is inspectable and migratable.

**Hard parts, in order of risk**
1. **BeanShell content scripts** (§3) — 166 blocks, needs a real migration strategy.
2. **XStream object-graph serialization** — cyclic references, class-name-tagged XML; needs a
   deliberate migration to a stable schema (§ architecture rule 7 in `ENGINE_DECISION.md`).
3. **The 90 added classes** are poorly understood — 0 repo twins for `perktrees/`, `gui/town/`,
   `game/world/xml/`, `game/realestate/`. These can only be understood by reading the decompiled code.
4. **Sheer scale of the activity sim** — `Attend`/`Bartend`/`Strip` are 1–2.3k lines each of dense
   game rules.

**The repo's role in the port:** for the 526 shared classes it supplies original comments and an
older API to diff against — invaluable when the decompiled blob is ambiguous. It is *not* the
porting base; the decompiled shipped version is.

---

## 5. Suggested sequencing

1. Finish the full audit (in progress) to inventory the 616 files and their defects.
2. **Triage the 166 BeanShell blocks by hand** and decide the migration strategy before writing any
   port code — this decision constrains the whole architecture.
3. Build the content importer + golden fixtures from the Java build (as `ENGINE_DECISION.md` already
   specifies) so parity is measurable.
4. Port the domain core (`.NET`) first, with the activity simulation as the largest work item.
5. Godot presentation layer last.
