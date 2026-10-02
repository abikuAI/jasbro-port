# Audit 5 — Inventory, items & economy

*Shipped R0.1.2 decompiled source. Read-only: no file was modified.*

**40 findings — 15 high, 16 medium, 9 low.**

Paths are relative to `decompiled/source/jasbro/`; `FORK` = `repo/src/main/java/jasbro/`.
Where a fork twin is cited, it was read directly — the point is to distinguish **original author
intent** from **decompiler artifact**.

---

## The headline: money is created and destroyed continuously

The single largest theme. There are **two different payment APIs** in this codebase:

```java
int payFixed(int amount)   // clamps to the customer's balance, RETURNS what was actually taken
int pay(int amount, ...)   // same idea
```

The engine's economy is only correct if callers **use the return value**. Many don't. The result is
a steady birth and death of gold that no one designed:

- **H1/H3** — every drink, grope, lap-sit and blowjob is credited at the *nominal* price even when
  `payFixed` clamped it. A broke customer still generates full income.
- **H4/H5** — tips computed by *reading* a customer's money and never charging them. Pure faucet.
- **H6/H7** — the mirror image: the entire GROUP path charges customers and credits **nothing**.
  Gold is destroyed where nobody looks.

These are not exotic edge cases; they are ordinary shifts.

---

## HIGH

**H1 | money created | `game/character/activities/sub/business/Bartend.java:88-90` | Drink revenue credited at nominal; payFixed return discarded**
```java
pay = (int)((float)customer.getMoney() * portionPay * (float)Util.getInt(50, 125) / 100.0f);
customer.payFixed(pay += 5 + customer.getMoney() * bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 1200);
amountEarned += pay;
```
Every customer is credited the full nominal `pay` even when payFixed clamped it to their remaining
gold (BUMs are paid to 0), so each shift mints the difference. **high**
*Fork twin proves original intent, not an artifact:* `FORK/.../Bartend.java:52-58` —
`amountEarned += PROFITDRINK * amountDrinks;` then `customer.payFixed(COSTDRINK * amountDrinks);`
(return ignored) then `modifyIncome(amountEarned);`.
Same defect at `Attend.java:120-123`.

**H2 | money created | `Bartend.java:945-948` | +800 gold faucet with no payer**
```java
} else if (randomChat == 6) {
   chatBonus = 10;
   message = message + TextUtil.t("barevent.loudbunch.drink", bartender2);
   this.modifyIncome(800);
```
The LOUD BUNCH "drink" branch credits 800 gold while no customer is charged anywhere in the case
(lines 953-955 only add satisfaction). **high**

**H3 | money created | `Bartend.java` (many lines) | payFixed return ignored, nominal amount credited**
```java
cust.payFixed(blowjobPay);
this.modifyIncome(blowjobPay);
```
All sites where a customer is charged but the credited value is the pre-clamp argument:
`:225-226` (GROPE), `:350`+`:360` (SITLAP `payFixed(200)`/`modifyIncome(200)`), `:401-403`
(SITGROUP `100`), `:423-425` (`400`), `:488-489` (BLOWJOB), `:494-495` (`blowjobPay*(100-i)/100`),
`:518-519` (`100`), `:570` (LAGOMORPHORGY, charged, never credited → destroyed), `:595-596`
(FUCK/WENCH — safe only because `:583` pre-clamps with `Math.min`), `:603-604`+`614` (`total` of
nominal line-pay while each `customer.y` may be smaller), `:618` (charged, never credited),
`:636-637`, `:670`/`:692`/`:706`/`:720` (GROUP, charged, never credited — see H6), `:753`+`:756`
(`boobPayTotal`). **high** (every listed line read verbatim)
*Widened further:* `getCustomers()` may hold `CustomerGroup` instances whose `payFixed` divides the
amount over members and returns the real sum (`game/events/business/CustomerGroup.java:118-126`).
`Attend.java` mirrors these at `:2020-2021`, `:2002-2003`, `:2187`, `:2236-2240`, GROUP case
`:2052-2196`.

**H4 | money created | `game/character/traits/perktrees/DancerPerks.java:227-228` | Tips credited without charging the customer**
```java
int tips = (int)(richest.getMoney() / (200.0 / character.getFinalValue(SpecializationAttribute.STRIP)) + Util.getInt(10, 20));
activity.modifyIncome(tips);
```
The richest customer's money is only *read* — no `pay()`/`payFixed()` call exists in the block
(lines 226-238) — so the whole tip is created from nothing. No fork twin exists. **high**

**H5 | money created | `game/character/activities/sub/business/Strip.java:188-189` | EXTRAS income from customer money, customer never charged**
```java
int extra = this.getCustomers().get(a).getMoney() * character.getFinalValue(SpecializationAttribute.STRIP) / 300;
this.modifyIncome(extra);
```
**Regression vs fork:** the only `modifyIncome` in `FORK/Strip.java` is line 54 (tips path); the fork
computes the same `extra` at line 69 but never credits it — R0.1.2 added the credit without adding a
payment. **high**
*Checked and fine:* the normal tip path `Strip.java:74-79` correctly does
`tip = customer.pay(tip, ...)` and sums returns.

**H6 | money destroyed | `Bartend.java:669-670` (also `:569-570`, `:617-618`, `:692`, `:706`, `:720`; `Attend.java:2052-2053`, `:2187`) | Customers charged, income never credited**
```java
for (int b = 0; b < this.getCustomers().size() / size; ++b) {
    this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
```
The entire GROUP case (`Bartend.java:654-735`, `Attend.java:2031-2197`) contains four `payFixed`
calls and **zero** `modifyIncome`/`setIncome`, so every group customer's payment vanishes; same for
LAGOMORPHORGY (`:569-574`) and the all-customers-pay-100 branch (`:617-624`). **high**

**H7 | money destroyed | `Bartend.java:536-538` (`Attend.java:1841-1843`) | LAGOMORPHQUICKIE tips taken, never credited**
```java
tip = Util.getInt(5, 10) + bartender2.getFinalValue(sex2) / 3;
tip = cust2.payFixed(tip);
tips += tip;
```
`tips` is only interpolated into the message at `:540` (`Attend :1848`) and the case ends; the
collected gold never reaches activity income. **high** (payFixed's return is used correctly here)

**H8 | money created | `Attend.java:2002-2003` (`Bartend.java:649-650`) | Double evaluation: credited amount re-read after payment**
```java
cust2.payFixed(Math.min(100, cust2.getMoney()));
this.modifyIncome(Math.min(100, cust2.getMoney()));
```
The second `Math.min` re-reads the **post-payment** balance, so a customer who paid 100 is credited
**0** — the player is charged-to-nothing instead of paid. **high**

**H9 | money created | `game/character/specialization/ThiefEventHandler.java:53-55` | Cumulative stolen total re-earned for every victim**
```java
amountStolen += customer.payFixed((int)(stealAmount * 10 + customer.getMoney() * stealAmount / 100.0F));
if (amountStolen > 0) {
   Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
```
`amountStolen` is cumulative but credited **inside** the loop, so victim *n* re-earns the sum of
victims 1..*n* (3 victims of 50/30/20 credit 50+80+100 = 230 instead of 100). **high**

**H10 | money created | `game/character/conditions/SleepDeprivation.java:81` | Negative spend on death ADDS gold**
```java
Jasbro.getInstance().getData().spendMoney(-200L, this.getCharacter().getName());
```
`spendMoney` subtracts, so the "death of a slave from zero intelligence" branch *gives* 200 gold
(and fires a negative MONEYSPENT stat event); the sibling stamina-death branch charges nothing.
**high**
*Provenance:* identical in **both** fork twins — `FORK/SleepDeprivation.java:76` and
`repo/src/jasbro/.../SleepDeprivation.java:76` → original author code.

**H11 | overflow | `game/character/activities/sub/business/PublicUse.java:148, :273, :312` | Tips accumulated in a short → income flips negative**
```java
short totalTips = 0;
totalTips = (short)(totalTips + this.getCustomers().get(i).payFixed(Util.getInt(1, 5 + currentGirl.getFinalValue(sex))));
this.modifyIncome(totalTips);
```
Tip per customer is 1..(4+sex skill); a high-skill girl serving a few hundred customers exceeds
32767, wrapping to a negative short that is then charged as income. **high**

**H12 | money created | `game/character/activities/sub/business/SellFood.java:28-30` | Flat profit credited although the meal charge was clamped**
```java
amountEarned += 12;
int tips = customer.getMoney() / 200 + Util.getInt(1, 4) + this.getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 10;
customer.payFixed(20);
```
The 12-gold margin is credited for every customer regardless of whether `payFixed(20)` collected
anything (broke customer → 12 gold created per customer). **high**

**H13 | overflow | `gui/pages/subView/ShopPanel.java:167-169` (`gui/town/ShopMenu.java:248-249`) | int overflow can invert a purchase into income**
```java
&& Jasbro.getInstance().getData().getMoney() >= amount * item.getValue() * discount / 100) {
Jasbro.getInstance().getData().spendMoney(item.getValue() * amount * discount / 100, item.getName());
```
`amount * value * discount` is evaluated in `int`; with discount=100 it overflows above ~21.4M
(e.g. value=100000, amount=300 → −1294967296 → /100 = −12949672), the affordability guard passes a
negative and `spendMoney(-12,949,672)` **adds** ~13M gold. `ShopMenu.java:213` and `:249` have the
same int product. **high** on the arithmetic, **medium** on reachability (spinner has no max; the
buy paths are bounded by stock).

**H14 | unchecked affordability | `game/character/Charakter.java:551` | Trainer wage charged with no affordability guard**
```java
Jasbro.getInstance().getData().spendMoney(wage, this);
```
The CONTRACT branch 42 lines above does exactly this check and fires `"trainer.cannotafford"`
instead (`:508-515`); the OWNED-TRAINER branch has no guard, so daily wages drive money negative and
emit BROKE. **high**

**H15 | unchecked affordability | `game/housing/House.java:268`, `game/quests/StandardSlaveQuest.java:223`, `game/quests/SellSlaveQuest.java:89/:96/:128`, `game/character/activities/sub/Rob.java:205` | Unconditional spendMoney/setIncome on daily sinks**
```java
Jasbro.getInstance().getData().spendMoney(pay, this);            // House.java:268
gameData.spendMoney(penalty, StandardSlaveQuest.this.getTitle()); // StandardSlaveQuest.java:223
this.setIncome(-1500);                                            // Rob.java:205
```
None consult `canAfford`, so a poor player is silently driven negative by upkeep, quest penalties
and the Rob fee (`RunningActivity.java:173-174` then calls `spendMoney` for the negative income).
**high**

---

## MEDIUM

**M1 | zero price | `game/world/market/CharacterSchool.java:183-207` | First extra specialization is free**
```java
int amountSpecialisations = this.character.getSpecializations().size() - 1;
...
default:
   return 10L * (amountSpecialisations * amountSpecialisations * amountSpecialisations * amountSpecialisations * amountSpecialisations / 1000 * 1000);
```
The switch has cases 1..7 only, so a character with exactly one specialization takes `default`,
0^5 → price **0**, and gets a new specialization for nothing (UI guard at
`gui/town/SchoolMenu.java:269` compares against that 0). **medium** (reachable shapes are
content-defined)

**M2 | integer truncation | `stats/StatCollector.java:429-436` | Money totals truncated through an int accumulator**
```java
public long calculateSum(List<StatCollector.MoneyChangeData> moneyChangeDataList) {
   int sum = 0;
   for (StatCollector.MoneyChangeData moneyChangeData : moneyChangeDataList) {
      sum = (int)(sum + moneyChangeData.getAmount());
   }
   return sum;
```
Declared `long` but summed in `int`, so a heavy day reports wrong earned/spent totals
(`gui/pages/subView/StatPanel.java:190`). Same in the fork
(`FORK/StatCollector.java:227-228`) → original. **high** that the cast exists, **medium** on impact
(display only).

**M3 | unguarded arithmetic | `game/items/usableItemEffects/UsableItemChooseOneEffectContainer.java:26-29` | Zero total chance crashes item use**
```java
int sumChances = 0;
for (UsableItemEffect itemEffect : effects) { sumChances += ((UsableItemEffectChance)itemEffect).getChance(); }
int selected = Util.getInt(0, sumChances);
```
`Util.getInt(a,b)` = `nextInt(b-a)+a`, so `sumChances == 0` throws
`IllegalArgumentException("bound must be positive")` during use. **high**

**M4 | unguarded list index | `Bartend.java:491-494` | get(z) past the end of the customer list**
```java
int i = Util.getInt(1, 5);
...
for (int z = 0; z < i + bartender2.getFinalValue(Sextype.ORAL) / 20; ++z) {
    this.getCustomers().get(z).payFixed(blowjobPay * (100 - i) / 100);
```
The loop bound grows with the ORAL skill (skill 100 → z up to 8) while the index is never checked
against `getCustomers().size()` → IndexOutOfBoundsException mid-shift. **medium-high**

**M5 | off-by-one / crash | `Bartend.java:180, :547, :554, :658`; `Attend.java:214, :1253, :1857, :1867, :2038`; `Strip.java:178` | getInt(0, size-1) is exclusive, so it is both biased and unsafe**
```java
int a = Util.getInt(0, this.getCustomers().size() - 1);
```
Because the upper bound is exclusive, the last customer can **never** be selected, and with exactly
one customer (or zero) `getInt(0,0)`/`getInt(0,-1)` throws. `Strip.java:178` is protected by the
`size() > 10` guard at `:174`; the Bartend/Attend sites are not all guarded. **high** on semantics,
**medium** per-site.

**M6 | null unboxing | `game/world/market/Auction.java:295-301` | getSlaveValue() NPEs when no slave is set**
```java
public long getSlaveValue() {
   if (this.slaveValue == null && this.slave != null) { this.slaveValue = this.slave.calculateValue(); }
   return this.slaveValue;
```
The `Long slaveValue` is returned **unboxed**, so `slave == null` yields NPE. Fork twin identical
(`FORK/Auction.java:302-307`) → original, latent. **high**

**M7 | unguarded lookup | `game/items/CharacterInventory.java:76-77` (and `:118`) | Equipped-item id with no backing file NPEs on unequip**
```java
Equipment equipment = this.getEquipmentForId(this.itemMap.get(equipmentSlot));
equipment.unequip(equipmentSlot, this.getCharacter());
```
`getEquipmentForId` returns null for an id absent from `Jasbro.getItems()` (ids come from filenames,
`ItemFileLoader.java:76`); `removeInvalidAccessory:117-118` has the same shape. Fork twin identical.
**high**

**M8 | concurrent modification | `game/world/market/QuestManager.java:119-125` and `:241-246` | Map entry removed while iterating values()**
```java
for (WorldEvent event : Jasbro.getInstance().getWorldEvents().values()) {
   try { event.handleEvent(e); } catch (Exception ex) {
      ...
      Jasbro.getInstance().getWorldEvents().remove(event.getId());
```
Removing from the live HashMap inside the for-each makes the next iteration throw
ConcurrentModificationException, swallowed by the outer catch at `:130-132`/`:251-253` — **silently
aborting all remaining world events for that day.** **high**

**M9 | divide by zero | `game/world/market/CharacterSchool.java:307-322` | ArithmeticException when a specialization has no attributes**
```java
return sum / amount
   >= attribute.getAttributeType().getDefaultMax() - ...
```
`amount` counts `specializationType.getAssociatedAttributes()`, so an attribute-less specialization
divides by zero, and `attribute` is likewise null → NPE at `:319`.
`SpecializationTraining.getDescription:247` also calls `getAssociatedAttributes().get(0)`
unconditionally → IndexOutOfBounds. **medium** (content-defined)

**M10 | infinite loop risk | `game/world/market/SlaveMarket.java:41-53` (`AuctionHouse.java:49-52`) | Retry loop never terminates if every generated slave is UNSELLABLE**
```java
for (int i = 0; i < amountSlaves; i++) {
   this.slave = Jasbro.getInstance().generateBasicSlave();
   if (!this.slave.getTraits().contains(Trait.UNSELLABLE) && ...) { ... } else { i--; }
```
`i--` retries forever. **high** on code shape, **low-medium** on reachability.

**M11 | null dereference order | `gui/town/SlavePensMenu.java:139-145` (`gui/pages/subView/SlaveMarketPanel.java:82-92`) | Null check after the dereference**
```java
Object[] arguments = new Object[]{SlavePensMenu.this.selectedSlave.getName()};
int price = 500 + (int)SlavePensMenu.this.selectedSlave.calculateValue();
...
if (SlavePensMenu.this.selectedSlave != null && Jasbro.getInstance().getData().canAfford(price)) {
```
`getName()`/`calculateValue()` run before the `!= null` test, so clicking Buy with nothing selected
NPEs. **medium** (UI-state dependent)

**M12 | unchecked affordability (TOCTOU) | `gui/character/SpecializationPanel.java:157, :172` | Affordability checked at listener registration, not at apply()**
```java
if (specializationTraining.fulfillsRequirements() && Jasbro.getInstance().getData().getMoney() >= specializationTraining.getPrice()) {
   warning.addMouseListener(new MouseAdapter() { ... specializationTraining.apply(); ...
```
The check is evaluated in the pool thread that registers the listener; `apply()` (ending in
`spendMoney(price,"School")`, `CharacterSchool.java:265`) runs later from the click with no re-check,
unlike the rebuilt panels (`SchoolMenu.java:269`, `SchoolPanel.java:209`). **medium**

**M13 | scaling anomaly | `game/world/market/Shop.java:55-57` | Stock multiplier applied asymmetrically**
```java
&& (itemSpawnData.getChance() == 0 || Util.getInt(0, 100) < itemSpawnData.getChance() * chanceModifier)) {
   inventory.addItems(item, amountModifier * Util.getInt(itemSpawnData.getMinAmount(), amountModifier * itemSpawnData.getMaxAmount() + 1));
```
For `Trait.BENEFACTORSHOPS` `amountModifier=2`, giving `2*[min, 2*max+1)` = `[2*min, 4*max+2)` where
`[2*min, 2*max]` was presumably intended (top end doubled again). The fork twin has **no**
multiplier (`FORK/Shop.java:42-44`), so this expression is new in R0.1.2. **medium** (intent
inference)

**M14 | ignored return | `game/character/activities/sub/whore/Whore.java:412` (with `:543-548`) | Designed minimum payment never applied**
```java
this.minPayment(payment);
this.modifyIncome(payment);
...
public Integer minPayment(int payment) { if (payment <= 6) { payment = 7; } return payment; }
```
`minPayment` returns the floored value and the caller discards it (the local is unchanged), so the
7-gold floor is dead. **high**

**M15 | overflow / O(n) blowup | `game/items/Equipment.java:137-149` | calculateValue() loops amountEffects times and overflows to Infinity**
```java
for (int i = 0; i < effect.getAmountEffects(); i++) {
   if (effect.getValue() >= 0.0) { valueExp *= 1.0 + effect.getValueExponential(); } else { valueExp /= -(1.0 + effect.getValueExponential()); }
}
double valueFinal = (long)(valueSum * valueExp);
```
The iteration count is content-authored (`EquipmentChangeAttribute:59` returns `amount`,
`EquipmentChangeAttributeMax:72` likewise); a large amount is an O(amount) loop per value query, and
`valueExp → Infinity` makes `(long)Infinity` = `Long.MAX_VALUE` as the item's price. **medium**

**M16 | unchecked cast | `game/items/Inventory.java:17-18` (`Jasbro.java:582-583`) | UnlockItem cast on a type flag**
```java
if (this.checkTriggerUnlock(item)) { this.unlock((UnlockItem)item); }
...
if (item.getType() == ItemType.UNLOCK) { if (!...contains(((UnlockItem)item).getUnlockObject())) {
```
Both sites gate only on `getType() == ItemType.UNLOCK`; an XML item that sets type UNLOCK while
instantiating another class yields ClassCastException/NPE instead of a logged content error.
**medium** (content-authored)

---

## LOW

**L1 | dead code / stale state | `game/items/Inventory.java:88` (and `:106`) | removeAll() against a list of a different element type is a no-op**
```java
itemList.removeAll(removeList);
```
`itemList` holds `Inventory.ItemData`, `removeList` holds `String` ids, so unknown ids are never
purged from `items` (they keep counting in `getAmount`) — the clear intent was to drop them. Fork
twin identical (`FORK/Inventory.java:98`) → original. **high** (behaviour), **low** severity.

**L2 | item loss | `Jasbro.java:432-437` | Same-basename item files silently overwrite each other**
```java
for (Item item : ItemFileLoader.getInstance().loadAllItems()) { this.items.put(item.getId(), item); }
```
Item ids are filenames and `addItems` recurses 5 levels, so two XML files with the same basename in
different folders collide and one is lost. **Shipped content is clean: 155 item xml files, 0
duplicate basenames.** **high** (latent, mod-facing)

**L3 | rewards lost | `game/quests/Reward.java:22-25` | skillPoints constructor argument dropped**
```java
public Reward(int amountMoney, Quest quest, int skillPoints) {
   this.rewardMoney = amountMoney;
   this.quest = quest;
}
```
`perkPoints` is never assigned (only the setter at `:115` can), so every quest reward built with the
3-arg constructor silently grants no perks. **high**

**L4 | shared mutable content | `game/items/usableItemEffects/UsableItemChangeAttribute.java:21-25` | Use mutates the shared item definition**
```java
if (this.maxChange < this.minChange) { int tmp = this.minChange; this.minChange = this.maxChange; this.maxChange = tmp; }
```
Items are global singletons from `ItemFileLoader`, so the swap rewrites the (saveable) item template
rather than a local copy; same code in `IngredientItemChangeAttribute.java:21-25`. **high**, low
impact.
*Related:* `UsableItem.java:33` `this.itemEffect.apply(character, this)` NPEs when a USABLE item has
no effect; `UsableItemChangeAttributeMax.java:20` dereferences `this.attribute` with no null check
(contrast `UsableItemChangeAttribute.java:20`).

**L5 | aliasing | `game/items/Item.java:27-36` | Copy constructor shares the spawnData list**
```java
public Item(Item item) { ... this.spawnData = item.spawnData; ... }
```
Subclass copies share one `List<ItemSpawnData>` with the source, so editing spawn data on a copy
edits the original (and ConcurrentModification is possible if both are serialized/iterated).
**high**

**L6 | dead state | `game/world/market/AuctionHouse.java:22, :616-618` | static slaveJob is never assigned**
```java
private static String slaveJob;
...
public static String getSlaveJob() { return slaveJob; }
```
The only writer in the whole tree is `Charakter.setSlaveJob` (a per-character instance field), so
this static always returns null; it is also process-wide shared state in a serializable object.
**high**

**L7 | comparator contract | `game/world/market/CharacterSchool.java:341-362` | Inconsistent Comparable may abort Collections.sort**
```java
if (this instanceof CharacterSchool.AttributeSpecializationTraining) { ... return 1; }
```
`compareTo` returns 1 for two `AttributeSpecializationTraining` instances (never 0) and 0 for mixed
types, violating the contract; TimSort throws
`IllegalArgumentException("Comparison method violates its general contract!")` once the list reaches
the merge threshold (`getTrainingOpportunities` sorts at `:61`). **medium** (size dependent)

**L8 | truncation | `game/world/market/Auction.java:156, :175` (`Bidder.java:66-67`) | int cast on a long bid**
```java
this.profit = (int)Util.getPercent(this.maxBid, 80);
```
A bid above ~2.68B gold truncates the profit (and `spendMoney(maxBid)` stays long, so the seller's
cut and the buyer's charge disagree). Also `Auction.run()` sleeps 2s holding the monitor
(`synchronized` at `:153`) and `resetTimer():105` divides by `auctionTime/20`. **medium/low**

**L9 | NPE on empty content list | `game/character/CharacterManipulationManager.java:107, :123, :139, :154` (also `game/character/traits/perktrees/FurryPerks.java:322`) | getInt(0, options.size()) on an empty list**
```java
CharacterBase base = options.get(Util.getInt(0, options.size()));
```
`getInt(0,0)` throws and `options.get(...)` would throw for an empty list; shipped content always
has entries. **medium** (content-dependent)

---

## Checked and NOT a bug

Recorded so these are not re-audited.

- **Room income modifiers are correctly ordered.** `EmptyRoomEventHandler.java:12/:25`,
  `OrgyRoomEventHandler.java:16/:27` and `MasterBedroomEventHandler` use `ACTIVITYPERFORMED`, which
  `RunningActivity` fires at `:156` **after** `perform()` has set income — so
  `setIncome(getIncome()/2)` and `Util.getPercent(getIncome(),140)` operate on real values (unlike
  `EventType.ACTIVITY`, fired at `:110-111` *before* `perform()`).
- **`BartenderPerks.CatMaid`** (`BartenderPerks.java:33-41`) reads `customer.getMoney()` and
  immediately pays exactly that (`payBum += customer.getMoney(); customer.payFixed(customer.getMoney());`),
  so nominal == collected; its `setIncome(payBum)` cannot wipe the drink revenue because ACTIVITY
  precedes the additive `modifyIncome(amountEarned)` at `Bartend.java:93`.
- **Correct use of the clamp return:** `Rob.java:267-269`, `ThiefEventHandler.java:107`,
  `BathAttendant.java:175`, `Catshow.java:36`, `Massage.java:38-39`, `SellFood.java:31`,
  `Whore.java:390-400`.
- **`FurryPerks.java:306` `activity.setIncome(0)`** is the deliberate AvianBirdbrain penalty.
- **Equipment round-trips are balanced:** `CharacterScreenInventoryPanel.java:86-93`,
  `EquippedItemPanel.java:75-79`, `CharacterInventory.equip` failure path (`:63-67`),
  `removeInvalidAccessory` (`:110-131`).
- **Slave purchases are affordability-guarded** with the price computed before the guard
  (`SlavePensMenu.java:140-148`, `SlaveMarketPanel.java:83-95`; DISCOUNTSLAVES `/2` truncation is by
  design). Shop buy guards match their charge expressions; shop sell pays exactly half by design.
- **`Customer.payFixed`/`pay` clamp both directions** (`Customer.java:280-305`: amount > money →
  money; amount < 0 → 0).
- **Auction bid affordability** is enforced at `Auction.java:80`; the SLAAVESOLD flow nets out as
  intended (`:158` credits the 80% profit, `SellSlaveQuest.java:116` then charges
  `getProfit() - reward`).
- **`Item.id` being `transient`** is safe: `ItemFileLoader.loadItem` restores it from the filename
  at `:75-76` before `Jasbro.loadItems`.
- **`Inventory.getAmount` is null-safe** (`:67-73`); `removeItems` takes the stack to 0 then deletes
  the key (`:57-65`).
- **`getAvailableItemsByLocation`** returning one item once per matching `ItemSpawnData`
  (`Jasbro.java:576-594`) is the spawn-chance mechanism, not duplication.
- **`GameData`'s transient StatCollector** is rebuilt and re-registered lazily (`:208-215`), so
  post-load events do not NPE on it.
- **`Util.getInt` is `nextInt(end - start) + start`** (`Util.java:32-35`) — used consistently as an
  exclusive bound everywhere except the size-1 sites in M5.

---

## Why this matters for the port

Three patterns need **decisions**, not mechanical translation:

1. **Payment returns are load-bearing.** The port must make the clamped-return contract impossible
   to ignore — not just "call it correctly". Every one of H1/H3/H6/H7/H8/H12 is a caller silently
   discarding or re-deriving a value the API already computed.
2. **`int` arithmetic is pervasive and unsafe.** H11 (short), H13 (int overflow in price math), M2
   (int accumulator), M15 (Infinity → Long.MAX_VALUE). The port should use `long`/`decimal` at the
   economy boundary and treat overflow as a bug class.
3. **Silent-failure paths hide real defects.** M8's swallowed ConcurrentModificationException
   aborts a whole day of world events with no visible error.

`perkPoints`/L3 and the entirely-dead `realestate` package (see
[`EVENTS-HOUSING-REALESTATE.md`](EVENTS-HOUSING-REALESTATE.md)) are examples of features that were
**never wired up** — the port should not "restore" them without deciding they were intended.
