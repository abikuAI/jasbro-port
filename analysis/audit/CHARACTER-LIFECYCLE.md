# Audit 6 — Character lifecycle, conditions, ageing & time

*Shipped R0.1.2 decompiled source. Read-only: no file was modified.*

**25 findings — 2 high, 11 medium, 12 low.**

Paths are relative to `decompiled/source/jasbro/`. `repo:` cites the GitHub twin.

> **Twin caveat.** The fork is a *different, older* revision. Only **exact-match** hunks prove author
> intent (`getFinalValue`, `getArmor`, `getResistance`, `getTraits`/`addTrait`, `notifyAll`,
> `Jasbro.load`, the `Pregnancy` constructor). Hunks that **differ** (`CharacterSpawner.spawnChild`,
> `PerkHandler.getBaseRequirement`, `Charakter.handleEvent` NEXTSHIFT/NEXTDAY, `Charakter.getWarnings`,
> `Buff.Pretty`) are decompiled-only evidence. **`perktrees/` has no twin at all.**

---

## The theme: singleton state that should be per-character

The two HIGH findings are the same bug in two forms, and it is the most structurally interesting
defect in the codebase.

Trait effects in this game are **enum-initialised singletons** — `Trait.AQUATIC(true, 0, new FurryPerks.Aquatic())`
constructs exactly one `Aquatic` for the whole process. That is fine for immutable config. It is
catastrophic when the effect stores per-character state.

```java
public static class Aquatic extends TraitEffect {
   int stage = 0;          // ← one field, shared by EVERY character that has AQUATIC
```

So `this.stage` is not "this character's transformation level" — it is "the level of whichever
character most recently ran". Character A's aquatic bonus is computed from character B's progress.

The Heat buffs are the same shape, one level up:

```java
static Buff h1 = new Buff.Heat1();
```

`Condition.character` is a single field on the buff instance. Two characters in heat share one object,
so `handleEvent`'s self-removal (`getCharacter().getConditions().remove(this)`) acts on the **wrong
character's** list — the first keeps `h1` forever while the second loses it immediately.

`FurryPerks.setText` goes further and mutates **JVM-wide enum state**: `Trait.TRANSFORMATION`'s
displayed name becomes `"REPTILIANSTAGE3"` for every character, permanently (`resetText()` is never
called by anything).

**Port consequence:** the port must make all of this per-character instance state. This is a case
where the hybrid policy is unambiguous — it is not "balance behaviour", it is state corruption, and
preserving it would mean deliberately sharing fields across characters.

---

## HIGH

**1 | shared-mutable-singleton | `game/character/traits/perktrees/FurryPerks.java:54-56` (+335-351, 370-372, 391-393) | Heat-stage Buffs are static singletons shared by every character**
```java
static Buff h1 = new Buff.Heat1();
static Buff h2 = new Buff.Heat2();
static Buff h3 = new Buff.Heat3();
...
            character.addCondition(FurryPerks.h1);
         } else if (list.contains(FurryPerks.h1)) {
            character.addCondition(FurryPerks.h2);
            character.removeCondition(FurryPerks.h1);
```
`Condition.character` (`Condition.java:23/42`) is overwritten by whichever character received the
singleton last, so `Buff.handleEvent`'s self-removal and `remainingTime` act on the **wrong
character** — the first keeps `h1` forever while the second loses it immediately; heat progression
(h1→h2→h3) corrupts across characters. **high**

**2 | shared-mutable-singleton | `FurryPerks.java:59-61` (also 188, 250, 433, 474, 576, 686, 818, 1022, 1170) | Per-character morph `stage` stored on the enum-held TraitEffect singleton**
```java
public static class Aquatic extends TraitEffect {
   int stage = 0;
   int stagelimit = 5;
   int stagestep = 10;
```
Used at `:99` (`float multiplier = this.stage * 0.05F;`) and `:218`
(`PREGNANCYCHANCE ? currentValue + 5.0 + 5 * this.stage : currentValue`).
One instance per enum constant (`Trait.java:162` `AQUATIC(true, 0, new FurryPerks.Aquatic())`, `:172`,
`:146`; `private TraitEffect traitEffect` set once per constant). Character A's aquatic/arachnid/
reptilian bonuses and HP-max steps are computed from **character B's** transformation level. **high**

---

## MEDIUM

**3 | shared-mutable-singleton | `FurryPerks.java:71, 200, 262, 445, 486, 588, 698, 830, 1034, 1182` | `Trait.setText` mutates global enum state**
```java
Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("REPTILIANSTAGE" + this.stage);
```
`Trait.text` is a field on the enum constant (`Trait.java:429`, `setText` `:543-545`) and is never
reset — `resetText()` has no callers. The Transformation trait's displayed name for **every**
character becomes the last-updated character's stage label for the rest of the session. **high**

**4 | wrong-variable (copy-paste) | `Charakter.java:804-806` | getArmor discards the ARMORPERCENT modifier**
```java
value = (int)this.getAttributeModified(CalculatedAttribute.ARMORPERCENT, mitigation);
this.getCache().put(CalculatedAttribute.ARMORPERCENT, (int)mitigation);
return (int)mitigation;
```
Compare `getDamage`, which does it correctly (`Charakter.java:777-779`). Twin identical
(`repo:782/784/785`) → **author bug**. The FIGHTER perk ElementalStudy
(`FighterPerks.java:39-46`) and any ARMORPERCENT equipment/condition contribute **nothing**. **high**

**5 | wrong-constant / missing case | `Charakter.java:981-982`, `:977-1000` | WATER resistance returns WIND resistance; LIGHTNING is always 0**
```java
      case WATER:
         calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
         break;
      case WIND:
         calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
```
No `case LIGHTNING` exists, yet `LIGHTNINGRESISTANCE` is routed to `getResistance(DamageType.LIGHTNING)`,
which falls through to `0`. Twin identical (`repo:904-905`) → author bug. **high**

**6 | inverted condition / unreachable branch | `Charakter.java:1243-1256` | Trainer ATTEND always gets the "catastrophic command" warning**
```java
         } else if (activity.getType() != ActivityType.ATTEND
            || this.getTraits().contains(Trait.LEGACYSTRIPPER) && this.getTraits().contains(Trait.LEGACYBARTENDER)) {
...
         } else {
            warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.catastrophicCommand", this, arguments)));
         }
```
Counter-evidence in the same build: `Attend.java:2602` penalises only when the character has
**neither** trait (i.e. either suffices), and the two perks are mutually exclusive
(`SkillTreeItem.java:553` makes LEGACYBARTENDER a perk *un-requirement* of LEGACYSTRIPPER). So `&&`
should be `||`; the safe branch can never be entered. **high** (medium for player-visible severity)

**7 | ignored modifier | `Pregnancy.java:27`, `:48-60` | PREGNANCYDURATIONMODIFIER ignored (days hard-coded 60 in both branches)**
```java
   if (otherParent instanceof Charakter) {
      ...
      this.days = 60;
   } else {
      this.days = 60;
   }
```
Dead initializer `private int days = 30;` at `:27`. The twin (`repo:53-58`) shows the intended formula
(`days = (int)(days * mother.getPregnancyDurationModifier()/100f)`, averaged with the father's).
`getPregnancyDurationModifier()` is used nowhwre else except `MonsterPregnancy.java:32`, and
`EquipmentChangeCalculatedAttributeFixed.java:111` exists specifically to modify it. **high** (code),
**medium** that 60 is not a deliberate rebalance

**8 | cache-before-clamp | `Charakter.java:327-333` | getFinalValue caches the UNCLAMPED value**
```java
this.getCache().put(attributeType, (int)value);
if (value < attribute.getMinValue()) {
   value = attribute.getMinValue();
}
return (int)value;
```
Twin identical (`repo:320-325`). Every `AttributeType.getDefaultMin()` is 0
(`BaseAttributeTypes:20-22`, `EssentialAttributes:26-28`, `Sextype:67-69`, `SpecializationAttribute:31-33`)
and `Attribute.minValue` is transient so it stays 0 after load — the clamp is live for negative
effective values (Flu, SleepDeprivation, Unmotivated). **The same getter disagrees with itself within
one shift**: the first caller after a cache clear sees 0, later callers see the negative value. **high**

**9 | lost state / iteration bug | `EventManager.java:344-349` | A character is skipped for NEXTSHIFT/NEXTDAY whenever another is removed during the event**
```java
      Charakter c = null;
      try {
         List<Charakter> characters = Jasbro.getInstance().getData().getCharacters();
         for (int i = 0; i < characters.size(); ++i) {
            characters.get(i).handleEvent(event);
         }
```
Deaths/removals legitimately happen inside `handleEvent` (`EventManager.java:398` health-zero,
`SleepDeprivation.java:79/88`, `Charakter.java:511` unpaid CONTRACT at NEXTDAY). The twin used an
enhanced for-loop (`repo:397-399`) — **the index loop is an R0.1.2 change.** The character that slides
into the removed index never receives that shift's condition/Buff/counter processing (missed buff
expiry, NOSLEEP tick, illness progression). **high**

**10 | content loss on the shipped startup path | `CharacterFileLoader.java:234`, `:251-254` | customtext is only read when an image has ≥1 `<tag>`**
```java
               NodeList elements2 = element.getElementsByTagName("customtext");
               if (elements2 != null && elements2.getLength() > 0) {
                  imageData.setCustomText(elements2.item(0).getTextContent());
               }
```
Inside `for (int j = 0; j < attributes.getLength(); j++)`. The shipped game **always** uses this
path: `Jasbro.java:415` `loadAllCharacters(true)` → `loadCharacterOptimized`. The non-optimized twin
has it outside the tag loop (`repo:197-200`) but **also nested in the optimized one** (`repo:301-304`),
so it is an author bug in both revisions. Every image without a `<tag>` silently loses its caption.
**high**
*Already reproduced in the port* — see `CharacterPropertiesParser.cs:208-218` and
[`CONTENT-PARITY.md`](../CONTENT-PARITY.md).

**11 | comparison that can never be true | `Charakter.java:492-497` | `Class.equals(instance)`**
```java
         for (Condition condition : this.getConditions()) {
            if (condition.getClass().equals(new Buff.Unmotivated(this)) || condition.getClass().equals(new Buff.MotivatedTwo(this))) {
               this.removeCondition(condition);
            }
         }
```
`Class.equals(Object)` is **always false**. Both buffs have `remainingTime` 1 (`Buff.java:506, 749`)
and the condition loop (`Charakter.java:450`) runs earlier, so they normally self-expire: the branch
is dead code that also constructs two throwaway Buffs per call. **high** (bug), **low** (impact)

**12 | no-op branch / regression | `Buff.java:527-538` | Buff.Pretty's COSMETICS check multiplies by 1**
```java
int charismaIncrease = 1 + skill / 10;
if (sourceCharacter.getTraits().contains(Trait.COSMETICS)) {
   charismaIncrease *= 1;
}
```
The twin had `charismaIncrease += 10;` (`repo:263-264`) — **COSMETICS now has no effect on Pretty at
all.** **high**

**13 | stuck state | `MonsterPregnancy.java:63-79` | The condition is only removed if the "MonsterEgg" item exists**
```java
if (this.days <= 0 && Jasbro.getInstance().getItems().containsKey("MonsterEgg")) {
   Item item = Jasbro.getInstance().getItems().get("MonsterEgg");
   this.getCharacter().getConditions().remove(this);
```
Twin same structure (`repo:75-78`). With content lacking MonsterEgg (mod / partial install) `days`
keeps decrementing and the character stays **permanently pregnant**, blocking all further pregnancies
(`EventManager.java:440-444`). **high**

**14 | unguarded lookup during spawn | `CharacterManipulationManager.java:106-107, 122-123, 138-139, 153-154`; `CharacterSpawner.java:190-191` | `options.get(Util.getInt(0, options.size()))` with a possibly empty list**
```java
List<CharacterBase> bases = Jasbro.getInstance().getUnusedBases();
characterBase = bases.get(Util.getInt(0, bases.size()));
```
With `size()==0`, `Util.getInt(0,0)` → `nextInt(0)` throws `IllegalArgumentException` **before** the
`get()` runs. A content set with no INFANT/CHILD/TEENAGER base for the relevant gender (or no free
base) throws mid-birth instead of falling back. **high** (code), **medium** (reachability)

**15 | non-terminating retry loop | `CharacterSpawner.java:142-157`, `:163-180` | getMotherSpec's index range does not match its filtered set**
```java
while (true) {
   spec = getMotherSpec(mother);
   if (null != spec) { ... break; ... continue; }
...
int selection = Util.getInt(0, mother.getSpecializations().size());
for (SpecializationType spec : mother.getSpecializations()) {
   if (spec != SpecializationType.FURRY && spec != SpecializationType.SEX && ...) { if (i == selection) return spec; i++; } else { i++; }
}
return null;
```
`selection` is drawn from the **full** set size but compared against the index in the same full-order
walk, so most draws return null and the caller busy-spins. If the mother's set is exactly
{FURRY, SEX, SLAVE, TRAINER} (size 4 > the gate of 3 at `:142`) **no draw can ever succeed** → infinite
loop. Line 148 `if (SEX != spec || SLAVE != spec || TRAINER != spec)` is a tautology, making the
`continue` at `:152` unreachable. **high** (code), **low** (reachability)

**16 | transient/lazy field | `Charakter.java:407-413` | getAgeProgressionData dereferences the transient `base` field instead of getBase()**
```java
if (this.ageProgressionData == null) {
   this.ageProgressionData = new AgeProgressionData(this.base.getAgeProgressionData());
```
Twin identical (`repo:399-407`) → intended code, but `base` is transient and only re-bound by
`Jasbro.load` for `data.getCharacters()` + `auctionHouse.getSlaves()` (`Jasbro.java:148-160`). Any
other holder across a load (slave-market list, quest-held slave, an unborn child inside a Pregnancy)
that has a null `ageProgressionData` and is asked to age/spawn hits NPE instead of the lazy
re-resolution `getBase()` would have performed (`Charakter.java:86-90`). **medium**

---

## LOW

**17 | broken asset path | `Buff.java:32` | `"imates/cons/plus.jpg"` typo — Buff.Exhausted has no icon**
```java
private ImageData icon = new ImageData("imates/cons/plus.jpg");
```
`Buff.Exhausted` (`Buff.java:304-307`) never calls `setIcon`, and it **is** used
(`Strip.java:965`, `Attend.java:1030`), so it keeps this path. Twin identical (`repo:31`). **high**

**18 | diagnostic NPE | `EventManager.java:344`, `:351-355` | The CME handler itself throws NPE**
```java
      Charakter c = null;
      try { ... } catch (ConcurrentModificationException e) {
         System.err.println("Last character before error: " + c.getName());
```
`c` is never assigned, so `c.getName()` always NPEs and **replaces the original
ConcurrentModificationException** — the diagnostic destroys the evidence it exists to capture. **high**

**19 | never-false comparison | `Pregnancy.java:191-195`; `MonsterPregnancy.java:112-116` | Pregnancy image-tag stage gates are dead**
```java
if (this.days < 180) { imageTags.add(ImageTag.PREGNANT); }   // days starts at 60 and only decreases
if (this.days < 55)   // days starts at 7*modifier/100
```
Both always true; no behavioural change, but the stage gate is vestigial. **high**

**20 | bypasses addCondition | `SellSlaveQuest.java:60` | Questtimer inserted straight into the conditions list**
```java
this.getSlave().getConditions().add(new Questtimer(this.getTimeRemaining(), SellSlaveQuest.this));
```
`addCondition` sets the owning character, fires STATUSCHANGE and calls `init()`; this path skips all
three, so `Condition.character` stays null and `Questtimer.getCharacter()` falls back to the O(n) scan
in `Condition.java:30-37`. No immediate failure (`Questtimer.init` is empty), but it is the only
inconsistent insertion point found. **high** (code), **low** (impact)

**21 | dead parameter | `Sextype.java:102-104` | getPossibleSextypes(Gender) ignores its argument**
```java
public static List<Sextype> getPossibleSextypes(Gender gender) { return new ArrayList<>(possibleSextypesNormal); }
```
A male/male or futa caller gets the straight list. **high** (dead parameter), **medium** (intent)

**22 | dead ternary | `SpecializationAttribute.java:36-38` | `this == EXPERIENCE ? 20 : 20`** — both arms
identical. **high**

**23 | unguarded lookup | `PlannedActivity.java:130-137` (and `:98-105`) | `possibleActivities.get(0)` on a possibly empty list; `source.fireEvent(e)` NPEs when source is null.** **high** (code), **medium** (reachability)

**24 | archive parity | `NpcFileLoader.java:54-68` | Enemies packaged in `.zip` archives are never loaded**
```java
if (file.isFile() && file.getName().equals("properties.xml")) {
   ...
} else if (depth > 0 && file.isDirectory()) {
```
`CharacterFileLoader` explicitly accepts `file.isFile() && file.getName().endsWith(".zip")`
(`CharacterFileLoader.java:73`) and skips `template.zip`; `NpcFileLoader` has **no archive branch**,
so `npcs/enemies/<pack>.zip` is silently ignored. **medium** (truevfs archive-listing behaviour not
directly tested)

**25 | editor round-trip asymmetry | `CharacterFileLoader.java:371-377`, `:293-313`; `CharacterBase.java:77-83` | Saving a base writes 6 for every unset attribute; spawn then starts those at 1**
```java
element.appendChild(doc.createTextNode(character.getAttribute(attribute) + ""));
```
`CharacterBase.getAttribute` returns **6** for an absent key (`CharacterBase.java:81`), `load` stores
only values > 0 (`CharacterFileLoader.java:305`), and `CharacterSpawner.create` gives unlisted
attributes the `Attribute` constructor's start value **1** (`CharacterSpawner.java:80-86`,
`Attribute.java:22-28`). A base edited and re-saved in the character editor spawns characters with
**6 in every attribute the author never specified** (previously 1). `CharacterBase.getFullDescription`
(`:218-227`) never matched spawn output anyway. **high** (mechanism), **medium** (intent)

---

## Checked and NOT a bug

- **`CharacterFileLoader.java:171`** NodeList reassignment — the loop using `elements` as its
  condition (`:132-139`) has already finished; the enclosing loop iterates `imageList`.
- **Conditions removing themselves mid-event** (`Buff.java:96/142/144`, `Pregnancy.java:95`,
  `ItemCooldown.java:23`, `CoolDown.java:28`, `Questtimer.java:25/27`, `SunEffect:48`,
  `StartingAtTheBottom:21`) — no CME: `Charakter.handleEvent` iterates a copy (`Charakter.java:447-448`).
- **`Jasbro.removeCharacter`** iterating activities while `removeActivity` puts null — non-structural
  `EnumMap.put`, no CME.
- **`SelectionScreen.select`** blocks until a choice (`:129-136`), so the `getSelectionObject()`
  dereferences in `advanceAge:38` and `Jasbro.java:486` are safe.
- **`advanceAge` always returning false** is harmless — its only caller (`UsableItemAdvanceAge.java:15`)
  ignores the result.
- **Transient `Attribute.minValue`** loses nothing: every `getDefaultMin()` is 0.
- **`CharacterStuffCounter.map`** is dead and unsaved — no impact.
- **`Condition.init` dedupe via `isInstance`** (`Condition.java:62`) leaves the discarded condition
  weakly registered — at most transient spurious delivery, not a leak.
- **`CharacterStuffCounter`'s housework else-if chain** (`:73-142`) binds as intended: the `% 300`
  HELPFUL branch applies only to BARTEND/COOK/CLEAN/SELLFOOD.
- **`getControl`'s negative slave value** (`Charakter.java:1026-1051`) is intentional —
  `IconAttributePanel.java:48-52` renders negative control as "control used"; twin identical.
- **`Jasbro.load`'s `if (!log.isDebugEnabled())`** (`:163-175`) is deliberate: production removes
  characters with a missing base, debug substitutes a random one. Same in the twin.
- **`PerkHandler.getBaseRequirement`'s identical arms** (`:85-89`) are dead but consistent with the
  same revision's `requiredSkill` rebalance (`:79`).
- **`AttributeModification.applyModification`'s zero-delta re-entry** (`:34`) re-adds a clamped 0 —
  cosmetic event duplication only.
- **`Stun`'s `duration == 0`** path is reached exactly, and NEXTSHIFT cleanup exists (`:18-20`).
- **`getResistance`'s DARKNESS case** lacking `break` (`:996-998`) — it is the last case.

---

## Flagged, NOT counted (needs empirical evidence)

### ✅ RESOLVED — WeakReference round-trips, and preserves identity

**Question:** can XStream 1.4.7 round-trip `AgeProgressionData`'s
`WeakReference<Charakter> mother/father`? If not, every character with age data silently loses its
parents on load.

**Answered empirically** — `fixtures/java/AgeProgressionFixture.java`, two cases, both occurring in
real play:

| Case | Result |
|---|---|
| Parent **also** in `data.characters` (normal) | XStream writes `<referent class="..." reference="../../../../jasbro.game.character.Charakter">` — a **reference, not a copy**. After a round trip the weak target is **the same instance** as the character in the list. |
| Parent **garbage-collected** | Saves and loads cleanly as empty. Nothing throws. |

**This explains `nameMother`/`nameFather`.** The class carries both a weak reference *and* plain
Strings — the strings are the durable record, the reference is a cheap live link. A port modelling
only the reference loses parentage exactly when the parent has been collected, which is the case the
strings exist to cover.

Also produced the **unindexed reference form** (`.../Charakter`, no `[n]`) — a path shape distinct
from the indexed `.../Charakter[2]` seen elsewhere. The C# reader resolves both, verified against
this fixture (12 checks in `Simbro.Verify`). See [`../SAVE-FORMAT.md`](../SAVE-FORMAT.md).

### Still open

1. **`SpecializationType.getImage()` NPE for UNDERAGE** — no matching SkillTree
   (`SkillTree.java:6-23`); callers not enumerated, reachability unproven.
2. **`Pregnancy` has no no-arg constructor** — XStream does not require one and all fields are
   persisted, so deserialization looks sound but was not tested.
