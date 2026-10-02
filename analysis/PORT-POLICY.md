# Port policy — how the port treats the original's defects

*Decided by the project owner. This governs every porting decision from here.*

---

## The decision

> **Hybrid: fix crashes and pure arithmetic; preserve behaviour that shapes game balance.**

The 151 confirmed findings are sorted into three classes. Where a defect falls decides what the port
does with it — and every individual decision is recorded rather than left implicit.

| Class | Port action | Rationale |
|---|---|---|
| **A. Crash / state corruption** | **Fix.** | Nothing is preserved by reproducing an exception. A crash is not "how the game plays" — it is the game failing to play. |
| **B. Pure arithmetic slip** | **Fix.** | Integer overflow, `short` wraparound, truncating accumulators, negative spends. These are unambiguous mistakes with no design intent behind them. |
| **C. Balance-affecting behaviour** | **Preserve, documented.** | If the original's economy or difficulty depends on it, changing it silently makes the port a different game. |

---

## Why not the extremes

**Full bug-for-bug fidelity** was rejected because it would carry forward out-of-bounds exceptions
and `nextInt(0)` crashes into a project whose entire point is to be a *working* version. The audit
found crashes reachable in **normal play**, not just hostile edge cases.

**Fix everything** was rejected because a large share of the findings are *not* obviously bugs in
intent — they are balance decisions, or at least indistinguishable from them. Consider H1: drinks are
credited at the nominal price even when the customer couldn't pay. That inflates bartending income,
but it is a *consistent* inflation, and "the bar earns well" may be exactly the intended feel.
Silently correcting it would shift the difficulty curve of the whole early game with no way for the
owner to notice.

The hybrid keeps the port **playable** without making it **unrecognisable**.

---

## Class A — fixing these (crashes and state corruption)

| Ref | Finding | Fix |
|---|---|---|
| [BUSINESS-ACTIVITIES](BUSINESS-ACTIVITIES.md) | `Bartend:493`/`Attend:1783` index past the customer list | Bound the loop by list size |
| BUSINESS-ACTIVITIES | `Attend:65` `getInt(0,0)` on an emptied `bartenders` | Guard the empty case |
| BUSINESS-ACTIVITIES | `SubmitToMonster:48` `nextInt(0)` | Guard the empty case |
| BUSINESS-ACTIVITIES | `BathAttendant:216-242` switch missing every `break` | Restore the intended control flow |
| [INVENTORY-ECONOMY](INVENTORY-ECONOMY.md) | M3 `UsableItemChooseOneEffectContainer` zero total chance | Guard before the draw |
| INVENTORY-ECONOMY | M4 `Bartend:491` list overrun | Bounds check |
| INVENTORY-ECONOMY | M5 `getInt(0, size-1)` — biased **and** throws at size ≤ 1 | Use the correct exclusive bound |
| INVENTORY-ECONOMY | M6 `Auction.getSlaveValue` null unboxing | Return a value, don't NPE |
| INVENTORY-ECONOMY | M7 `CharacterInventory` unequip with a dangling id | Guard the lookup |
| INVENTORY-ECONOMY | M8 `QuestManager` CME silently aborting a day of world events | Iterate a snapshot |
| INVENTORY-ECONOMY | M9 `CharacterSchool` divide-by-zero | Guard the empty case |
| INVENTORY-ECONOMY | M10 / CHARACTER-LIFECYCLE 15 non-terminating retry loops | Bound the retries |
| INVENTORY-ECONOMY | L7 inconsistent `Comparable` can abort `Collections.sort` | Honour the comparator contract |
| INVENTORY-ECONOMY | L9 / CHARACTER-LIFECYCLE 14 `getInt(0, options.size())` on empty lists | Guard |
| [CHARACTER-LIFECYCLE](CHARACTER-LIFECYCLE.md) | 1 & 2 **singleton state shared across characters** | Make it per-character instance state |
| CHARACTER-LIFECYCLE | 3 `Trait.setText` mutating JVM-wide enum state | Make display state per-character |
| CHARACTER-LIFECYCLE | 9 index loop skipping a character on removal | Iterate a snapshot |
| CHARACTER-LIFECYCLE | 13 permanent pregnancy when `MonsterEgg` is absent | Always terminate |
| CHARACTER-LIFECYCLE | 18 the CME diagnostic NPEs, destroying the evidence | Fix the diagnostic |
| CHARACTER-LIFECYCLE | 16 transient `base` dereference | Use the lazy accessor |
| CHARACTER-LIFECYCLE | 23 `possibleActivities.get(0)` on an empty list | Guard |
| [TRAITS-PERKS](TRAITS-PERKS.md) | `FurryPerks:360` BeastInHeat NPE on non-customer activity | Guard the activity type |
| [CONTENT-SCRIPTING](CONTENT-SCRIPTING.md) | the two shipped-content defects (typo'd `itemId`, `questStage` out of range) | Fix the content, and validate at load |

**Judgement calls within Class A:**

- **`getResistance` WATER/WIND (CHARACTER-LIFECYCLE 5)** — fixing this *does* change balance (water
  attacks currently use wind resistance). It is filed under Class A because routing water to a
  *completely unrelated* element is unambiguously not a design choice, but the **magnitude** of the
  balance shift should be reviewed before shipping.
- **`getArmor` discarding ARMORPERCENT (4)** and **`getWarnings` inverted `&&` (6)** — both fix
  things that currently do nothing or always fire. The former restores intended perk behaviour, which
  is a *buff*; the latter removes a spurious DANGER warning. Both are Class A by intent, with
  balance side effects to note.
- **`EventManager` skipping a character (9)** — this is silent state loss, and it is an **R0.1.2
  regression** (the fork used an enhanced for-loop). Restoring the fork's behaviour is a fix, not a
  balance change.

## Class B — fixing these (arithmetic)

| Ref | Finding | Fix |
|---|---|---|
| INVENTORY-ECONOMY | H11 `PublicUse` `short` tips overflow → income flips negative | Widen to `long` |
| INVENTORY-ECONOMY | H13 `ShopPanel`/`ShopMenu` `int` overflow inverts a purchase into income | Widen the price arithmetic |
| INVENTORY-ECONOMY | M2 `StatCollector` `long` total summed through `int` | Sum in `long` |
| INVENTORY-ECONOMY | M15 `Equipment.calculateValue` → `Infinity` → `Long.MAX_VALUE` price | Bound/clamp |
| INVENTORY-ECONOMY | L8 `int` cast truncating a long bid | Widen |
| INVENTORY-ECONOMY | H10 `SleepDeprivation` negative spend **adds** 200 gold on death | This is arithmetic sign, not balance — a death penalty should not be an income |
| [BUSINESS-ACTIVITIES](BUSINESS-ACTIVITIES.md) | `BathAttendant:280-341` ANAL/ORAL/FOREPLAY/GROUP all using `amountVaginal` | Wrong variable, not a balance choice |

## Class C — preserving these (balance)

Preserved **deliberately**, each documented at the point of implementation, so nobody later "fixes" a
design decision by accident.

- **H1/H3 — drink and service income credited at the nominal price** even when `payFixed` clamped.
  This is the single biggest economy influence in the game and runs in the player's favour. Preserved.
- **H4/H5 — tip faucets** (`DancerPerks`, `Strip` EXTRAS) that read a customer's money without
  charging them. Preserved.
- **H6/H7 — the GROUP path charging customers and crediting nothing.** Preserved, surprisingly: it is
  a *penalty* on group activities, and removing it would make those activities strictly better.
- **H9 — pickpocketing crediting the cumulative total per victim** (3 victims → 50+80+100 = 230).
  Preserved: it is a skill-reward curve, however accidental.
- **H12 — `SellFood`'s flat 12-gold margin** regardless of collection. Preserved.
- **H14/H15 — unchecked daily sinks** driving the balance negative. Preserved: "you can go broke" is
  a real mechanic here, and `BROKE` fires for a reason.
- **M1 — the first extra specialization being free.** Preserved (and arguably generous-by-design).
- **M13 — `Shop` stock multiplier asymmetry.** Preserved.
- **M14 — `Whore`'s dead 7-gold floor.** Preserved; the practical effect is a lower income ceiling.
- **CHARACTER-LIFECYCLE 7 — 60-day pregnancies** ignoring the duration modifier. Preserved as the
  base duration, **but** the modifier is wired up, because equipment exists specifically to set it and
  currently does nothing. This is the one Class C item where the *modifier* is restored while the
  base value is kept.
- **CHARACTER-LIFECYCLE 10 — untagged images losing `customtext`.** Preserved for content parity: the
  port already reproduces it, and correcting it would change what the shipped content displays.
- **CHARACTER-LIFECYCLE 8 — `getFinalValue` caching the unclamped value.** Preserved; noted as a
  known inconsistency.
- **CHARACTER-LIFECYCLE 11 — the dead `Class.equals` branch.** Preserved (no observable effect).
- **CHARACTER-LIFECYCLE 12 — Pretty's `*= 1`.** Preserved; if COSMETICS should grant +10 charisma
  that is a balance change to request explicitly.
- **INVENTORY-ECONOMY L3 — `Reward`'s dropped `skillPoints`.** Preserved as-is; documented as
  "never wired up", not "restored".

---

## How this is enforced

1. **Every Class C preservation carries a code comment** naming the finding ID and stating that the
   divergence is intentional. A future reader must be able to tell a preserved bug from a new one.
2. **Class A and B fixes are covered by the parity harness** (`verify-save.ps1`) where they touch save
   or content behaviour.
3. **The port never silently "improves" something.** If a behaviour is wrong but load-bearing, it is
   preserved and documented — the owner decides, not the port.

> **Standing caveat:** the boundary between "balance" and "bug" is a judgement call. Where a finding
> sits near the line, it is flagged here rather than decided quietly. The three to review first are
> `getResistance` WATER/WIND, `getArmor`, and `Reward.skillPoints`.
