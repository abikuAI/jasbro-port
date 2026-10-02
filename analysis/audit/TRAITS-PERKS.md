# Audit — Traits & Perks

Scope: `game/character/traits/` — `Trait.java` (584 L), `TraitEffect.java` (1064 L),
`SkillTreeItem.java` (1011 L), `SkillTree.java`, `PerkHandler.java`, `Perks.java`, and all 28 files
under `perktrees/`. Cross-checked against the commented older source in `repo/` where it exists, plus
call-sites and XML content.

## How it works

`Trait` is a ~370-constant enum where every constant carries a value modifier, an optional "opposing"
trait, and an optional `TraitEffect` (or a `true, cost` perk flag). Behaviour lives in `TraitEffect`
and its subclasses (`Perks`, `perktrees.*`). `SkillTreeItem` builds **17 DAG skill trees** (one per
`SpecializationType`, name-matched in `PerkHandler.getConnectedSpecializationType`) out of
`create`/`link` calls; `PerkHandler` computes skill points from specialisation attributes, per-node
level/base requirement, and used/available points.

**Two facts that dominate the port:**

1. **Effects are singleton instances** created once in the enum initialiser — so **any mutable field
   on a `TraitEffect` is global state shared by all characters** (plus a handful of `static Buff`
   singletons in `FurryPerks`).
2. **A large share of perk behaviour is not in the trait at all** — it is checked *by trait name* in
   activity classes (`Strip`, `Attend`, `Bartend`, `WhoreStreets`, `Advertise`, …) and in content XML
   `trait="…"` requirements.

Traits serialise **by name** (Java and XML); no ordinal dependence was found.

---

## CRITICAL

**1. `MarketingPerks.java:177` — all 8 marketing "Target"/"Niche marketing" perks are no-ops**
```java
List<House> listHouses = new ArrayList<>();
int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 30;
skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10;
bonusCustomers.add(new SpawnData.CustomerData(CustomerType.SOLDIER, skill));

for (House house : listHouses) {          // ← never populated
```
8 identical blocks (lines 177/201/225/249/272/296/320/344): `listHouses` is allocated and iterated,
but nothing ever calls `.add(...)`, so `SpawnData.addFixedAmountCustomers` is dead. The older
commented source has the same empty list with `//Spawn them` above the loop — the intent is obvious.
**Impact: the entire core mechanic of the Marketing Expert tree (8 purchasable perks, 100 pts each)
does nothing.** The port must decide whether to restore `Jasbro.getData().getHouses()` (the reference
`MaidPerks.Elegant` uses) or faithfully reproduce the no-op. Confidence: **high**.

---

## HIGH

**2. `FurryPerks.java:360` — `BeastInHeat` NPEs on any non-customer activity**
```java
if (e.getType() == EventType.ACTIVITY) {
   RunningActivity activity = (RunningActivity)e.getSource();
   activity.getMainCustomer().addToSatisfaction(activity.getMainCustomer().getSatisfactionAmount() / 2, this);
```
`getMainCustomer()` returns null when there are no main customers (`RunningActivity.java:369-371`) and
there is no `instanceof Whore` / `hasMainCustomer()` guard. `BEASTINHEAT` is granted by `Vulpine` at
stage 5, so on heat days (`day % 30 == 0`) any Sleep/Clean/Cook/Advertise from that character throws
inside `RunningActivity.performActivity` → `Charakter.handleEvent` (no try/catch on that path; only
`EventManager.handleEvent:372` swallows NPEs). Same pattern at lines 383 and 410. Confidence: **high**.

**3. `Trait.java:100` — `FLABBY` penalises HEALTH instead of ENERGY**
```java
HEALTHY(400, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 10)),
SICKLY(-300, HEALTHY, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),
PERSEVERING(400, new TraitEffect.AttributeMaxModifier(EssentialAttributes.ENERGY, 10)),
FLABBY(-300, PERSEVERING, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),
```
`FLABBY` is the declared opposing trait of `PERSEVERING` (ENERGY +10) but duplicates `SICKLY`
verbatim. ENERGY has no negative counterpart and HEALTH gets a second −10. Identical in the older
source — a genuine shipped bug. Confidence: **high**.

**4. `TraitEffect.java:970` — `Uninhibited` "like" branch penalises exactly like "dislike"**
```java
} else if (rnd < 15) {
   message.addToMessage("\n" + TextUtil.t("UNINHIBITED.like", character));
   businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
} else if (rnd > 85) {
   message.addToMessage("\n" + TextUtil.t("UNINHIBITED.dislike", character));
   businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
```
Only the message differs; both cost 5 satisfaction. The `hot.like` branch correctly adds +5 at line 966.
Same sign error in the older source. Confidence: **high**.

**5. `FurryPerks.java:787` — `InsectResilience` zeroes *healing*, not just small damage**
```java
if (mod.getAttributeType() == EssentialAttributes.HEALTH && mod.getBaseAmount() < 100.0F) {
   mod.setBaseAmount(0.0F);
```
The test is on **magnitude only**, so every positive HEALTH modification below 100 is annulled too —
Sleep (+5), Camp (+5), Idle (+1), Eat, Relax, Pamper all heal 0 — while a hit of exactly −100 or more
is *not* reduced at all. The intended form is almost certainly
`getBaseAmount() < 0 && getBaseAmount() > -100`. Confidence: **high**.

**6. `FurryPerks.java:639` — `Feline.removeTrait` *adds* `FELINESTRIP`**
```java
public boolean removeTrait(Charakter character) {
   Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
   Perks.PerkUtil.addMaybe(character, Trait.FELINESTRIP);      // ← should be removeMaybe
   Perks.PerkUtil.removeMaybe(character, Trait.NOCTURNAL);
```
Every other line — and every other morph's `removeTrait` — uses `removeMaybe`. Dropping the Feline
mutation leaves the character holding the `FELINESTRIP` perk it should have lost. Confidence: **high**.

**7. `FurryPerks.java:1089` — `Reptilian.removeTrait` subtracts more max HEALTH than was ever added**
```java
if (this.stage >= 3) {
   character.getAttribute(EssentialAttributes.HEALTH)
      .setMaxValue(character.getAttribute(EssentialAttributes.HEALTH).getMaxValue() - 40 - this.stage * 20);
}
```
The additive side (1036-1051) totals at most +100 (stage 3 `+60`, stage 4 `+20`, stage 5 `+20`), while
removal at stage 5 is −140 → a **permanent −40 max-HP loss** for any character that loses the perk
(stage 3: −100 vs +60). The addition is conditional on max-value thresholds; the removal is not.
Additionally `this.stage` is a field of the shared enum-singleton effect (finding 10), so the
subtraction can use **another character's** stage. Confidence: **high** for the asymmetry.

---

## MEDIUM

**8. `Trait.java:365` — purchasable perks with no implementation anywhere**
`SHOWOFF` (1000 pts), `CATCHY` (1000 pts, Advertising tree), `SHOWTIME` (1000 pts, Trait.java:263,
Fighter tree), `SMELLSLIKEKITTEN` (100 pts, Dancer tree) carry no `TraitEffect`, appear in no
`contains(...)` check, no `<code>` block and no XML `trait="…"`. Their neighbours
(`SAMPLINGTHEGOODS`, `SHOWINGTHEGOODS`, `HEYGUYSBOOSE`, `SPIRITED`) *are* implemented in
`Advertise`/`Publicize`, which makes the omission look accidental. Also inert: `FELINESTRIP` (see 6),
`AUTONOMOUSPERK`, `LAGOMORPHORGY`. **Impact: up to 3,100 points buyable with zero effect.**

**9. `Trait.java:543` — `Trait.TRANSFORMATION.text` is mutated globally and never reset**
```java
public void setText(String text) { this.text = text; }
```
Called from 10 morph effects (`FurryPerks.java:1034` etc.):
`Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("REPTILIANSTAGE" + this.stage)`.
`Trait` is an enum singleton, so this **overwrites the display name for every character** — whichever
character morphed last wins, and description lookups then use e.g. `"AQUATICSTAGE3.description"`.
`resetText()` exists but is called from nowhere.

**10. `FurryPerks.java:54` — `static Buff h1/h2/h3` singletons shared across characters**
```java
static Buff h1 = new Buff.Heat1();
static Buff h2 = new Buff.Heat2();
static Buff h3 = new Buff.Heat3();
```
Used via `character.addCondition(FurryPerks.h1)`. `Charakter.addCondition` sets the instance's
character pointer and registers it as a listener; `Buff.handleEvent` later removes itself from
`getCharacter().getConditions()`. With two heat characters, the single instance's character pointer,
listener registration and remaining time are shared — the losing character keeps a `Heat1` that never
expires. **The same flaw applies to every mutable `stage` field on a `TraitEffect` singleton**
(`FurryPerks.java:59, 188, 605, 1022`): it is recomputed per event but read by
`getAttributeModifier`/`removeTrait` *between* events, so it can belong to another character.

**11. `MarketingPerks.java:25` — 16 perktree effect classes are never instantiated** ⚠️ *port-relevant*
Never referenced by `new …()` anywhere: `MarketingPerks.Overwrite{Anal,Bondage,Foreplay,Monster,Oral,Titfuck,Vaginal}`,
`NursePerks.{Dermatologist,Oily,Soapy}`, `DancerPerks.{SkinCare,TanLines}`, `KinkyPerks.PublicUse`,
`FurryPerks.{FelineStrip,InhumanPregnancy}`, `LegacyPerks.Genius`, plus the six `LegacyPerks$Legacy*.java`
classes. For `Oily`, `PublicUse`, `InhumanPregnancy` and the `Cosmetics` family the real behaviour is a
**name check elsewhere** (`Strip.java:143/640`, `PublicUse.java:88/166/301`, `BathAttendant.java:86`,
`EventManager.java:420/438`, …), so those are dead duplicates. The `Overwrite*` classes,
`NursePerks.Soapy`/`Dermatologist` and the six `LegacyPerks$Legacy*` classes have no counterpart at all.
**For the port: do NOT "restore" these as effects — it would double-count.**

---

## LOW

| # | Location | Finding |
|---|---|---|
| 12 | `PerkHandler.java:85` | `getBaseRequirement` if/else branches are identical (`(level-1)*10`); older source had `*50` vs `*10`. `requiredSkill()` (line 78) is dead. |
| 13 | `SkillTreeItem.java:215` | `SALESPROMOTION → RECOGNIZED` linked **7 times** — duplicate DAG edges. No gameplay effect (`isParentLearned` uses `contains`), but the perk UI could double-draw. |
| 14 | `SkillTreeItem.java:519` | LEGACY tree: `LEGACYNONE` + seven `LEGACY*2` nodes have **no parent**, so `isParentLearned` returns true and base requirement is 0 — they are buyable immediately, with no rule preventing `LEGACYWHORE` + `LEGACYWHORE2`. |
| 15 | `FurryPerks.java:177` | Unguarded `Util.getInt(0, loot.size())` throws `IllegalArgumentException` on an empty list. `InsectGather` (766-769) guards; `AvianPickup` (321-323) does not. Latent in shipped content. |
| 16 | `Trait.java:334` | `TOUGHERMISSIONS5`, `TAXEVASION`, `BENEFACTORKINGDOM` are in **no skill tree** and referenced nowhere — unreachable. (Legacy tree creates `TOUGHERMISSIONS1..4` only.) |
| 17 | `Trait.java:247` | `WEAPONMASTERY` uses `SimpleTraitEffect(null, 1.0, DAMAGE)` — a **1.0 multiplier is the identity**, so the 100-point perk does nothing. Same in the older source. |
| 18 | `TraitEffect.java:128` | Unreachable `ARACHNID` branch in `morphRandom` (`morphType` only receives 8 morph kinds; no `MORPHARACHNID` trait exists), plus `==` string comparisons that work only via literal interning. |

---

## Checked and deliberately NOT reported

- `Trait.isOpposed` (`Trait.java:511-517`) — the complex decompiled boolean is logically equivalent to
  the older source's nested if.
- `PerkHandler.getSkillPoints` int-truncation and the `/10` vs `/15` split — identical in the older source.
- `BartenderPerks.CatMaid` `activity.setIncome(payBum)` — `perform()` runs *after* the ACTIVITY event
  and uses `modifyIncome`, so the bum pay is not lost.
- `SkillTree` ↔ `SpecializationType` name matching — all 17 names match exactly, so
  `getConnectedSpecializationType` never returns null for a real tree.
- Intentional switch fall-through in `FurryPerks.{Aquatic,Feline,Reptilian,Lagomorph,Canine,Avian}`
  (cumulative tiers).
- `LegacyPerks$Legacy*` / `NursePerks$*` being emitted as separate `$`-named top-level classes is a
  decompiler artefact; the only real consequence is that they are unreferenced (finding 11).
- **No ordinal/save-compat hazard:** traits are looked up by name in Java and XML, and `Trait.text` is a
  non-serialised enum field. But **renaming a constant would break saves and XML `trait="…"` /
  BeanShell `Trait.X` references** — worth a port checklist. No current mismatch exists.
