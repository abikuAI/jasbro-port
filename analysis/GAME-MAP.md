# JaSBro — Authoritative Game Map

An evidence-backed map of how the shipped game (`R0.1.2 final`) is structured, built from the
decompiled source plus direct measurement of your install at `C:\Games\Jasbro_Final`.

This document is the reference for the port: state model, content model, save format, and the
systems that must be reproduced.

---

## 1. Shape of the game

A single-process Java Swing desktop application. `jasbro.Jasbro` is both the entry point and the
global application object (`Jasbro.getInstance()`), holding the GUI, the content caches, the shared
BeanShell interpreter, and a cached thread pool.

```
jasbro.Jasbro  (singleton: gui, content caches, interpreter, thread pool)
   └── jasbro.gui.RPGView            (Swing top-level window)
   └── jasbro.game.GameData          (ALL game state; the save root)
```

State is a **mutable object graph** with cyclic references, serialised whole via XStream.

---

## 2. State model — `GameData` is the save root

`jasbro/game/GameData.java` (293 lines). Everything about a playthrough lives here:

| Field | Type | Default | Notes |
|---|---|---|---|
| `day` | `int` | `1` | |
| `time` | `Time` | `MORNING` | shift within the day |
| `money` | **`long`** | `500` | signed; can go negative → fires `BROKE` |
| `houses` | `List<House>` | lazy | owned property |
| `characters` | `List<Charakter>` | lazy | slaves + trainers |
| `protagonist` | `Charakter` | derived | see below |
| `auctionHouse` / `slaveMarket` / `shop` | market objects | lazy | |
| `inventory` | `Inventory` | lazy | |
| `eventManager` | `EventManager` | eager | the event bus |
| `questManager` | `QuestManager` | lazy | |
| `otherLocationMap` | `Map<LocationType, CharacterLocation>` | `EnumMap` | |
| `defaultPreferences` | `DefaultPreferences` | lazy | |
| `unlocks` | `Unlocks` | lazy | fame-based unlocks |
| `statCollector` | `StatCollector` | **`transient`** | rebuilt on load |

**Money is `long`** — the port must not narrow it.

### Notable behaviours to reproduce faithfully

```java
public void earnMoney(long amount, Object source) {
   this.getEventManager().handleEvent(new MoneyChangedEvent(EventType.MONEYEARNED, source, amount));
   this.money += amount;                       // event fires BEFORE the balance changes
```

```java
public void spendMoney(long amount, Object source) {
   this.getEventManager().handleEvent(new MoneyChangedEvent(EventType.MONEYSPENT, source, amount));
   this.money -= amount;
   Jasbro.getInstance().getGui().updateStatus();   // <-- direct GUI call from the domain model
   if (this.money < 0L) {
      this.eventManager.handleEvent(new MyEvent(EventType.BROKE, null));
   }
```

Three things to carry over: the event fires **before** the balance mutates; money silently goes
negative; and `GameData` calls **directly into the GUI** — a layering violation the port should fix
rather than copy.

```java
public Charakter getProtagonist() {
   ...
   if (this.protagonist == null) {
      this.protagonist = this.getSlaves().get(0);   // IndexOutOfBoundsException if no slaves
   }
```

Also note `setProtagonist` has heavy side effects (grants the `LEGACY` specialization, floors
EXPERIENCE at 50, adds a bonus perk). Because XStream writes **fields**, the setter is not invoked on
load — so a save can legitimately contain a protagonist that never had those applied.

`getSlavesForSale()` removes from a list while iterating with an index fixup — correct but fragile.

---

## 3. Save / load

**Where:** the game *working directory*, as plain XML — `save1.xml`, `save2.xml`, … and
`quicksave.xml` (`SlotSaveAction.java:25-27`, `SlotLoadAction.java:25-27`). Slot count is a setting
(`Settings.SAVESLOTS`, default 3).

**How:** `SaveAndLoadPerformer.java`

```java
XStream xstream = new XStream(new StaxDriver());
xstream.autodetectAnnotations(true);
String xml = xstream.toXML(gameData);
```

and on load, the file is read **line by line and concatenated** before parsing:

```java
do {
   line = bufferedReader.readLine();
   if (line != null) { xml = xml + line + "\n"; }
} while (line != null);
```

That is quadratic string building on a potentially multi-megabyte save — a real (if not fatal)
inefficiency.

**Port implications:** the save is a **class-name-driven XStream graph**, so it encodes Java class
and field names. Two consequences:

1. Old saves *cannot* be read by a differently-shaped C# model without a deliberate mapping layer.
2. XStream field access means **field renames break saves**. This is why `ENGINE_DECISION.md` rule 7
   says legacy XML is a **read-only import format** and new saves use versioned JSON. That is the
   right call.

No save files currently exist in your install, so there is nothing to migrate yet — but the format is
fully determined by `GameData` plus the field graph.

---

## 4. Content model

The content is **hand-written XML**, not XStream output. It is parsed by bespoke parsers, and it
references game classes by **fully-qualified name in element tags** — so the content *is* a serialised
form of the domain model.

Measured across 235 content files: **71 distinct domain classes, 2,259 class references.**

### 4.1 Items — 155 files, 20 classes
The largest content area. The item system is an **effect-composition** model:

| Refs | Class |
|---|---|
| 181 | `ItemSpawnData` |
| 119 | `EquipmentChangeAttribute` |
| 95 | `EquipmentChangeAttributeGain` |
| 79 | `Equipment` |
| 75 | `UsableItemChangeAttribute` |
| 50 | `UsableItem` |
| 49 | `EquipmentChangeCalculatedAttributeFixed` |
| 25 | `EquipmentChangeAttack` |
| 18 | `UnlockItem` |
| 12 | `EquipmentRemoveTrait` |

Common elements: `attributeType`, `amount`, `itemLocation`, `chance`, `minAmount`, `maxAmount`,
`value`, `name`, `type`, `spawnData`, `imageSelection`, `image`, `background`.

**This is the most portable part of the content** — it is genuinely declarative, ~430 effect
references composing into items, with no embedded code.

### 4.2 Events — 51 files, 38 classes
The scripting-heavy area. An event is a tree of effects and requirements:

| Refs | Class |
|---|---|
| 356 | `ImageTag` |
| 179 | `ImageSelection` |
| 177 | `WorldEventSimpleMessage` |
| 97 | `WorldEventEffectContainer` |
| 86 | `WorldEventCondition` |
| **79** | **`WorldEventCode`** ← embedded BeanShell |
| 51 | `WorldEvent` |
| 32 | `WorldEventSetQuestStage` |
| 28 | `WorldEventSaveVariable` |
| 26 | `WorldEventSetQuestStatus` |

Elements: `background`, `target`, `imageLocation`, `imageTags`, `subEffects`, `message`, `images`,
`importantMessage`, `code` (156 occurrences), `requirement`, `displayHeight`, `id`, `triggers`.

### 4.3 Quests — 11 files, 19 classes
Stage machines driven by triggers:

| Refs | Class |
|---|---|
| 82 | `Trigger` |
| 36 | `CustomQuestStage` |
| 13 | `MainCharacterRequirement` |
| 12 | `ChanceRequirement` |
| 11 | `CustomQuestTemplate` |
| 11 | `ActivityRequirement` |
| 11 | `AttributeRequirement` |

Elements: `requirements`, `triggerType`, `showInQuestLog`, `triggers`, `triggerToWorldEventMap`,
`questStages`, `activityType`, `title`, `description`.

Note the **generic containers** — `entry`/`string` pairs, `triggerToWorldEventMap` — used for the
script-variable namespace (§5).

### 4.4 Characters — 11 files
Elements: `tag` (636), `image` (506), `trait` (64), `character`, `name`, `gender`, plus per-attribute
elements (`CHARISMA`, `OBEDIENCE`, `STAMINA`, `INTELLIGENCE`, `STRENGTH`, …) and `initialSpecialization`.

**Only 11 of your character folders have a `properties.xml`** — the other 1,627 images are loose files
the game never reads (see `PERF-STARTUP.md`).

### 4.5 NPCs — 5 files, 4 classes
`CalculatedAttribute` (42), `ImageData`, `EnemySpawnData`, `ComplexEnemyTemplate`. Elements include
`entry`/`double` pairs (an attribute map), `hitpoints`, `maxHitpoints`, `dick`.

### 4.6 `rooms.xml` — the property/activity config — 1 file, 42 KB
A different, denser format: `requirement` (396), `activity` (183), `char-requirement` (107), `slot`
(37), `room` (29), `child-activity` (21), `unlock` (17), `fame` (17).

This is what `RoomLoader` parses, and it is where the `Trait.BEAUTICIAN` crash came from — its
requirement parsing calls `Trait.valueOf()` unguarded.

---

## 5. The scripting layer — what makes this hard to port

Not all game logic is in Java. **40 content files contain 166 embedded BeanShell code blocks**,
evaluated at runtime:

| Location | Code |
|---|---|
| `WorldEventCode.java:19` | `worldEvent.getInterpreter().eval(this.code);` |
| `CodeRequirement.java:16` | `(Boolean)triggerParent.getInterpreter().eval(this.code);` |
| `CustomQuest.java:255` | `(String[])this.getInterpreter().eval("return this.variables;")` |

One shared `bsh.Interpreter` lives in `Jasbro.java:608-638` and is `transient` in the model — so it is
rebuilt after load.

The `variables` reflection is important: **any variable a script assigns becomes game state** and is
harvested back into an attribute map. That is an implicit dynamic contract between Java and content,
and it has no direct equivalent in a compiled C# model. See `BSH-MIGRATION.md` for the full analysis.

---

## 6. Systems inventory

From the package census and class list, the game consists of these systems:

| System | Package | Port weight |
|---|---|---|
| Characters & attributes | `game/character` (161 files, 27k lines) | **Heaviest** |
| Activities / jobs | `game/character/activities` | **Heaviest** — `Attend` 2,347 lines |
| Traits & perk trees | `game/character/traits` + `perktrees` | Large, 372 traits |
| Battle | `game/character/battle` | Medium |
| Content/scripting engine | `game/world/customContent` + `xml` | **Highest risk** |
| World, locations, time | `game/world` | Medium |
| Events | `game/events` | Medium |
| Items & equipment | `game/items` (52 files) | Medium — most declarative |
| Property/housing | `game/housing` | Medium |
| Market: auction, shop, slave market | `game/world/market` | Medium |
| Real estate | `game/realestate` | New since fork |
| Quests | `game/quests` | Small |
| Stats | `stats` | Small |
| GUI | `gui` (105 files) | **Discarded** — rewritten in Godot |
| Editor tools | `util/*Editor` (~95 files) | Likely **not ported** |

**~105 GUI files and ~95 editor files will not be ported** — they are rewritten or dropped. That
leaves roughly 400 files of genuine domain logic, of which the activity simulation is the bulk.

---

## 7. What this means for the port

**Ordered by difficulty:**

1. **BeanShell content scripts** (166 blocks) — decide the strategy first; it constrains everything.
2. **XStream save import** — a read-only importer with an explicit class/field map; new saves in
   versioned JSON.
3. **The activity simulation** — `Attend`/`Bartend`/`Strip` are 1–2.3k lines of dense, undocumented
   rules. This is the largest single work item.
4. **372 traits + perk trees** — mechanical but voluminous.
5. **Content parsing** — the item system is nearly declarative and ports cleanly; events/quests do not.

**Things the port should deliberately fix rather than copy:**

- `GameData` calling `Jasbro.getInstance().getGui().updateStatus()` from domain logic.
- Money silently going negative with only an event as the signal.
- `getProtagonist()` throwing `IndexOutOfBoundsException` when there are no slaves.
- Quadratic save-file string building.
- Unguarded `Enum.valueOf` on content-supplied strings (four confirmed crash sites in `rooms.xml`).
