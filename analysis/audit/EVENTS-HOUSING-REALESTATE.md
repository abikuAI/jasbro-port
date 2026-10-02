# Audit — Events, Quests, Housing, Real Estate, RoomLoader

Scope: `game/events/` (8 + 12 business + 10 rooms), `game/quests/` (7), `game/housing/` (14),
`game/realestate/` (3), `game/world/RoomLoader.java`. Read-only review of the decompiled shipped
build (R0.1.2), cross-checked against the commented repo source and re-verified through the CFR and
Vineflower decompiles wherever a decompiler artifact was suspected.

## System overview

`EventManager` drives the shift loop (idle / other-location / house activities), spawns customers per
house via `SpawnData` + `BusinessCalculations` (fame / satisfaction / advertising / merc security),
performs activities, then advances time. Housing owns `RoomSlot` → `Room` → `RoomInfo` (loaded by
`RoomLoader` from `rooms.xml`) with `RoomEventHandler` hooks for room-specific modifiers. Quests are
`MyEvent` listeners with serialised `currentStage`/`variables`, rewarded via `Reward`.
`realestate` is a new, **entirely unwired** subsystem.

---

## HIGH

**1. `game/realestate/BuyPlotMapMenu.java:114-126` — plot purchase takes 100,000 with no affordability check and records nothing**
```java
if (JOptionPane.showConfirmDialog(...) == 0) {
   Jasbro.getInstance().getData().spendMoney(100000L, "");
}
```
`GameData.spendMoney` (`GameData.java:125-131`) has no guard and allows the balance to go negative.
The click is repeatable and **no plot or house state is ever written** — the player loses 100k per
click for zero effect. Confidence: **high**.
*Note:* every other plot button in all four maps has an empty listener (placeholder), so this one
button is the entire feature.

**2. `game/world/RoomLoader.java:51-64` — caught parse failure leaves `doc` null, then it is dereferenced**
```java
catch (FileNotFoundException e) { LOG.error("Failed to find room file", e); }
...
parseDocument(doc, rooms);          // then: Element root = doc.getDocumentElement();   (line 69)
```
Also: `new FileInputStream(file)` with `file = "rooms.xml"` (lines 41/47/52) — a **CWD-relative path**,
unlike `HouseUtil` which uses the classpath. Godot has no CWD-relative game-data concept.
Confidence: **high**.

---

## MEDIUM

**3. `game/events/business/BusinessCalculations.java:161-198` — `chance` leaks across activities**
```java
int chance = 66;                    // declared once per customer, OUTSIDE the activity loop
for (BusinessSecondaryActivity curActivity : secondaryActivities) {
   if (curActivity instanceof Strip && ...) chance = 33;
   if (curActivity instanceof Bartend && ...) chance = 85;   // independent ifs, not else-if
   ...
   if (Util.getInt(0, 100) < chance) ...
```
Any activity type with no matching rule inherits the *previous* activity's value — e.g. a PEASANT who
saw a Strip gets 33% for every later unmatched activity instead of 66%. "Last match wins" also makes
the outcome depend on appeal-sort order. Confidence: high that it is a bug; verified identical
scoping in both CFR and Vineflower.

**4. `game/housing/HouseUtil.java:19-24` — `properties.load(null)` NPE escapes the `catch (IOException)`**
```java
try (InputStream is = HouseUtil.class.getResourceAsStream("/houses/" + id + ".properties")) {
   houseProps.load(is);
} catch (IOException e) { LOG.error(...); return null; }
```
`getResourceAsStream` returns `null` for a missing entry, and `Properties.load(null)` throws
`NullPointerException` — which the `IOException` catch cannot catch. The intended graceful
`return null` is unreachable. Confidence: high on Java semantics; medium on it firing (all 9
`/houses/*.properties` exist).

**5. `game/events/rooms/Garden.java:48-72` — duplicate `CATNIP` branch is unreachable**
```java
} else if (this.plant == Garden.Plant.CATNIP) { this.growth = this.growth + 1 + this.growth / 7; this.quality -= 3; }
...
} else if (this.plant == Garden.Plant.CATNIP) { this.growth = this.growth + 1 + this.growth / 10; this.quality -= 4; }
```
The second can never execute (same guard), so that tier is dead and `WEED`/`GRASS`/`PEACHES` silently
fall into the final `else` (`/15`, `-6`). Identical in the older commented `Garden.java:62` — an
upstream bug faithfully preserved. Confidence: high (dead branch), medium (intent).

**6. `game/realestate/RealEstateSystem.java:28-38, 70-79` — `createPlot` never populates `freePlots`**
`createPlot` does `this.plots.put(id, p); return p;` — `freePlots` is only ever added to by `sellPlot`.
`buyPlot` requires `getRequiredFreePlot(id)` (`Validate.isTrue(this.freePlots.contains(p), ...)`), and
`getFreePlots()` returns an unmodifiable view. **No API path makes a new plot purchasable.**
Confidence: **high**.

**7. `game/realestate/RealEstateSystem.java:88-95, 118-120` — `buildHouse` NPEs on an unknown or non-owned plot**
```java
Plot plot = this.getOwnedPlot(plotId);
if (this.canPlaceHouse(plotId, house)) { plot.setHouse(house); }   // canPlaceHouse calls getOwnedPlot again
```
`getOwnedPlot` returns null for a bad id, so the "probably shouldn't have been reached" `LOG.warn`
path is unreachable — it always throws NPE. `buyPlot`/`sellPlot` use `Validate`; `buildHouse` does not.
Confidence: **high**.

**8. `game/realestate/*` (whole package) — dead code** ⚠️ *port-relevant*
`RealEstateSystem` appears **only in its own file** across the entire decompiled source; nothing
constructs it, and `BuyPlotMapMenu` does not reference it. `RealEstateMenu`/`RealEstatePanel` (GUI)
do all the live work. **For the port: do not treat `RealEstateSystem`/`Plot` as live behaviour.**
Confidence: **high**.

**9. `game/housing/RoomPlanning.java:55-56` — new room added to a throwaway list**
```java
if (i >= this.house.getRooms().size()) { this.house.getRooms().add(newRoom); }
```
`House.getRooms()` (`House.java:75-83`) builds a **fresh `ArrayList`** from the slots on every call, so
the add is a no-op: money is spent, no room appears, no `downTime` is set. `newRoom.setHouse(...)` is
also missing in this branch. Caveat: unreachable through the shipped UI — `InteriorDecorationPanel.java:90-92`
only ever does `remove(id)+add(id, roomInfo)`, so `newRooms.size()` always equals `roomAmount`, which
also makes the expansion branch (`RoomPlanning.java:28-30`) dead. Identical to older
`RoomPlanning.java:49-51` (upstream bug). Confidence: high on the code fact, low on reachability.

**10. `game/housing/RoomInfoUtil.java:45-54` — static init assumes five room ids exist in the loaded map**
```java
roomInfos.get("EMPTYROOM").setEventHandler(new EmptyRoomEventHandler());
roomInfos.get("MASTERBEDROOM")...; roomInfos.get("SICKROOM")...;
roomInfos.get("DUNGEON")...;      roomInfos.get("CLASSROOM")...;
```
`RoomInfoUtil.<clinit>` also calls `RoomLoader.loadRooms(null)` (line 18). Any missing id → NPE at
class init; `newRoom(id)` (lines 40-42) builds a `ConfigurableRoom` with a null `RoomInfo` for an
unknown id, NPEing later in `Room.getMaxPeople`/`roomInfo.getCost`.

**Checked and clean:** verified against the shipped `rooms.xml` — all five room ids, all 41 activity
ids, 6 trait ids, 2 char-types, 6 specializations and 4 slot types resolve to real enum constants.
**No content drift in R0.1.2 itself.** This corroborates the earlier independent trait audit.

---

## LOW

**11. `game/events/EventManager.java:232-236` — remove while index-iterating skips an element**
```java
for (i = 0; i < remainingCustomers.size(); ++i) {
   Customer customer = remainingCustomers.get(i);
   if (customer.getMoney() >= 5) continue;
   remainingCustomers.remove(customer);      // next element shifts into index i, never examined
}
```
Some sub-5-money customers survive into the whoring loop (where `assignCustomers` skips `money <= 0`
anyway). Upstream has the same shape (`EventManager.java:255-260`). Confidence: **high**.

**12. `game/events/EventManager.java:237-280` — `i` is never incremented, so the intended shrinking batch never happens**
```java
i = 0;
do { ... if (selectionSize < (alternative = remainingCustomers.size() / Math.max(1, 8 - i))) ...
} while (assigned && remainingCustomers.size() > 0 && whores.size() > 0);
```
There is no `++i` anywhere in the body (nor did upstream have one), so `8 - i` is permanently 8 and the
divisor never shrinks. Confidence: **high**; impact is behavioural drift from the obvious intent.

**13. `game/events/EventManager.java:344-355` — diagnostic variable is never assigned**
```java
Charakter c = null;
try { ... characters.get(i).handleEvent(event); }
catch (ConcurrentModificationException e) { ... System.err.println("Last character before error: " + c.getName()); throw e; }
```
`c` is never reassigned, so the handler always prints `"null"` — and would itself NPE. Fork-added code
(upstream has no try/catch here). Confidence: **high**.

**14. `game/housing/House.java:161-169` + `BuildingMercSecurity.java:15-18` — security clamp is dead, so security sits far outside 0..100**
```java
public void applySecurityLimit() { if (this.security < 0) ...; if (this.security > 100) { this.security = 100; } }
```
Never called from anywhere (grep: only its own declaration), while `BuildingMercSecurity.perform` does
`house.setSecurity(10000)` and `EventManager.java:202` does `house.setSecurity(house.getSecurity() - 1)`
— that whole block is disabled because `SecurityState.isImplemented()` returns false
(`SecurityState.java:18-20`). `SecurityState.calcState`'s `security > 80 && security < 100` guard shows
the 0..100 range was intended. Confidence: **high**.
Also note mercs cost `(int)(gameData.getMoney() / 33L)` per house per shift with **no minimum**.

**15. `game/quests/Reward.java:22-25` — three-arg constructor silently drops `skillPoints`**
```java
public Reward(int amountMoney, Quest quest, int skillPoints) {
   this.rewardMoney = amountMoney;
   this.quest = quest;          // perkPoints stays 0
}
```
No caller uses it (only `new Reward(reward, null)` at `StandardSlaveQuest.java:142` uses the 2-arg
form), so impact is limited to any BeanShell/content path that does. Identical upstream
(`Reward.java:21-24`). Confidence: **high**.

---

## Explicitly checked and NOT reported (clean or decompiler artifacts)

- `Garden`'s `quality > 100` branch **is** reachable — `Gardening.java:87` raises quality unclamped.
- The `growth != 0` guard is fine — `Gardening.java:112-160` sets `growth = 10` after `setPlant`.
- `ConfigurableRoom.getPossibleActivitiesChildCare` using `getCurrentUsage()` vs `getUsage(time)` is
  harmless: the only public entry point `CharacterLocation.getPossibleActivities()` passes the current time.
- The `GetModifier` / `fameToIncrease * Math.sqrt(fameToIncrease)` maths and the `Satisfaction`
  thresholds match bytecode across all three decompilers — a **deliberate fork rebalance** (`ASCENDED`
  removed), not a bug.
- All `rooms.xml` ids resolve (see finding 10).
