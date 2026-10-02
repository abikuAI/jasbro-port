# Audit — Business / Activity Classes

Scope: `game/character/activities/sub/business/` and `..\whore\Whore.java` — the densest game rules in
the codebase. Read-only review of the decompiled shipped build (R0.1.2) as ground truth.

Files read: `Attend`, `Bartend`, `Strip`, `PublicUse`, `BathAttendant`, `Catshow`, `Fight`,
`Massage`, `MonsterFight`, `Offerings`, `SellFood`, `Submit`, `SubmitToMonster`, `Whore`.

> **Note:** the GitHub fork versions of `PublicUse`/`BathAttendant`/`Bartend`/`Strip`/`Attend` are
> *completely different implementations* (older game systems). They were only useful for
> cross-checking `Whore`/`Massage`. Findings below come from the decompiled code alone.

> **Independently verified:** the two critical loop/index findings (#1 `Bartend.java:493-494` and
> #2 `Attend.java:65`) were re-read directly in the source and confirmed verbatim.

---

## Confirmed semantics the port must reproduce

| Behaviour | Detail |
|---|---|
| `Util.getInt(a, b)` | **`[a, b)`** — exclusive upper bound, via `Random.nextInt(b - a)`. Many call sites appear to assume inclusive. |
| `Customer.payFixed(p)` | Clamps the payment to available money and **returns the amount actually taken**. Several call sites ignore the return value. |
| `AttributeModification.applyModification()` | **Does nothing when `realModification != 0`.** Must be reproduced exactly for `Fight`/`MonsterFight`. |

---

## CRITICAL

**1. `Bartend.java:493-494`, `Attend.java:1783-1784` — index past the customer list (SLURPYSLURP)**
```java
for (int z = 0; z < i + bartender2.getFinalValue(Sextype.ORAL) / 20; ++z) {
   this.getCustomers().get(z).payFixed(...
```
`i = getInt(1,5)` ≤ 4, plus `ORAL/20` — and `ORAL` can reach the hundreds. → `IndexOutOfBoundsException`.
Confidence: **high**.

**2. `Attend.java:65` — picks from an empty bartender list**
```java
Charakter bartender = this.bartenders.get(Util.getInt(0, this.bartenders.size()));
```
`init()` (line 42) classifies up to 2 DANCER-specialised characters as dancers and **everything else**
(including the only character) as bartenders. A single DANCER character leaves `bartenders` empty → `get` on an empty list.
Confidence: **high**.

**3. `SubmitToMonster.java:48-54` — empty list then `nextInt(0)`**
```java
possibleMonsters.get(Util.getInt(0, possibleMonsters.size()))
```
`possibleMonsters` is only filled with monsters where `getMoney() > rank*2*rank`. If none qualify the list is
empty → `nextInt(0)` throws `IllegalArgumentException`.
Confidence: **high**.

---

## HIGH

**4. `BathAttendant.java:216-242` — `switch` with no `break` (fall-through)**
`switch(customer.getPreferredSextype())` — `VAGINAL` falls through `ANAL` → `ORAL` → `TITFUCK` → `FOREPLAY`,
incrementing/adding satisfaction for **every** case. A VAGINAL customer gets 5 counters, and `TITFUCK`/`FOREPLAY`
double-count. Confidence: **high**.

**5. `BathAttendant.java:100 vs 117-165` — accumulator never reset**
```java
int skill = 0;   // declared OUTSIDE the for-each over characters
```
Only `+=` / `/=` inside the loop, never reset — so every character after the first inherits the previous,
already-divided value. Confidence: **high**.

**6. `BathAttendant.java:280-341` — wrong variable in every branch**
The `ANAL`/`ORAL`/`FOREPLAY`/`GROUP` branches all use the **VAGINAL** counter/attribute:
```java
// inside the ANAL branch:
character.getCounter().add(...CUSTOMERSSERVEDTODAY..., (long)amountVaginal);
new AttributeModification(amountVaginal * 0.05F, Sextype.ANAL, character)
```
Wrong counters and wrong attribute gains on all four paths. Confidence: **high**.

**7. `Whore.java:412-413` — return value ignored, so a pay floor never applies**
```java
this.minPayment(payment);
this.modifyIncome(payment);
```
`minPayment` (line 543: `if (payment <= 6) payment = 7;`) therefore never raises income.
Confidence: **high**.

**8. `Bartend.java:523-541`, `Attend.java:1815-1849` — money destroyed (LAGOMORPHQUICKIE)**
`tips` accumulates from `cust2.payFixed(tip)` but `modifyIncome` is never called; `quickieArgs` is only
shown in text. Customer money is removed and the brothel receives nothing. Confidence: **high**.

**9. `Strip.java:997-1007`, `Attend.java:1067-1078` — dead branch (FONDLE)**
```java
if (BIGBOOBS && rnd == 0) { message = ...bigbreasts }      // no else
if (SMALLBOOBS && rnd == 1) ... else message = ...butt;    // overwrites the above
```
The big-breasts message can never be shown. Confidence: **high**.

**10. `Bartend.java:988-994` — divide inside the accumulation loop**
```java
for (Charakter chara : this.getCharacters()) { app += …; app /= 2; }
```
Multi-bartender appeal is not `sum/2`. Confidence: **high**.

**11. `Whore.java:110-111` — `DRUNK` tested twice**
```java
if (status == DRUNK && getInt(1,15) == 1 || status == DRUNK && getInt(1,10) == 1)
```
`VERYDRUNK` customers never reach the drunk-customer branch. Confidence: high (code), medium (intent).

**12. `Whore.java:322-325` — constant-false condition, dead code**
```java
int chance = 5;
if (chance <= 4) { … }   // entire branch (324-344) unreachable
```
Confidence: high (dead), medium (intent).

**13. `MonsterFight.java:225` — impossible comparison**
```java
if (Util.getInt(1, 100) > 100) { … }   // getInt(1,100) <= 99
```
The HURT-image branch is unreachable — and unlike the `else` it never adds `messageData` to `getMessages()`.
Confidence: **high**.

**14. `Bartend.java:208-212`, `Attend.java:1267-1275` — unreachable `DRUNK` test**
`setStatus(CustomerStatus.PISSED)` runs first, then the code tests for `DRUNK`/`VERYDRUNK` — always false —
making the following `setStatus(TIRED)` unreachable. Confidence: high (dead), medium (intent).

**15. `Strip.java:699-701`, `Attend.java:742-744` — duplicated trait test**
```java
contains(Trait.WENCH) || contains(Trait.WENCH) || contains(Trait.COMETOMOMMY)
```
One intended trait gate is missing. Confidence: high (duplicate), medium (intent).

**16. `Bartend.java:1014-1041`, `Strip.java:1100-1118` — enum members that can never occur**
`BarAction.SLAP` and `DODGE` have no `actions.add(...)` anywhere (`Bartend:1159` adds `SLAP` only in `Attend`,
which itself has no case for it); `StripAction.WILD`/`BIGTITS`/`CLUMSY` are never added. Their switch cases are dead.
Confidence: **high**.

---

## MEDIUM

**17. `Whore.java:431` — NPE on a null `Sextype`**
```java
mult = 1.0F - (this.getCharacters().get(0).getFinalValue(this.sexType) - 20) / 100.0F …
```
Runs even when `sexType == null` (which `checkPossible` sets when `possibleSextypes` is empty, line 164).
Confidence: medium-high.

**18. `Whore.java:393` — integer divide-by-zero**
`… / (group.getCustomers().size() - 1)` → zero for a 1-customer group. Confidence: medium.

**19. `Bartend.java:560-563`, `Attend.java:1875-1878` — divide-by-zero**
```java
int fraction = Util.getInt(7, 10) - bartender2.getFinalValue(SpecializationAttribute.TRANSFORMATION) / 10;
… 1 + getCustomers().size() / fraction
```
`TRANSFORMATION ≥ 70` makes `fraction` 0 → `ArithmeticException`. Confidence: medium.

**20. `Bartend.java:422-436`, `Attend.java:1634-1672` — loop-invariant branch, wrong target**
Inside the `for (b …)` loop, the branch is chosen by loop-invariant `a < getCustomers().size() / 10` — so
either *every* customer is charged 400 or customer `a` is penalised repeatedly. The parallel TEASELOT lose
(`Bartend:466-469`) even writes `getCustomers().get(a).setStatus(...)` while iterating `b`.
Confidence: medium.

**21. `Bartend.java:617-624`, `Attend.java:1966-1991`** — the `z` loop mutates only `getCustomers().get(a)`,
so FUCK's after-effects hit one customer N times. Confidence: medium.

**22. `Whore.java:361-363`, `344`, `371` — OR should be AND; wrong prefixes**
```java
if (!contains(SUBMISSIVEPOSITION) || !contains(SELF) || !contains(DOMINANTPOSITION))
```
The generic message overwrites the dominant/self/submissive message unless the image has all three tags.
The guards also test `message.startsWith("whore.sextype2.")` on *translated* text, and `"sex.sextype."` while the
messages are `sex.specific.*`. Confidence: medium.

**23. `Whore.java:83-87` — exclusive bound defeats the intent**
`Util.getInt(-1, 1) * 0.1F` yields only `-0.1` or `0.0` (upper bound exclusive), so SHYSTATUS can never give
the intended `+0.1` execution modifier. Confidence: medium.

**24. `Attend.java:2617-2618` — wrong skill used for appeal**
```java
appeal += (bartender.getCharisma() + bartender.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
```
Should be `BARTENDING`; dancers correctly use `STRIP` at line 2614. Confidence: medium-high.

**25. `Bartend.java:122-123`, `Attend.java:122-123` — books the requested pay, not the clamped amount**
```java
customer.payFixed(pay);
amountEarned += pay;      // should use payFixed's return
```
Income can exceed what the customer actually paid — inconsistent with `SellFood` etc.
Confidence: medium.

**26. `Strip.java:115-125` — duplicated threshold**
SEXSMELL added three times with thresholds `>7`, `>14`, `>14` (the third duplicates the second; probably `>21`).
Same duplicated pair at `Attend:151-161`. Confidence: medium.

**27. `Strip.java:187-189`, `Attend.java:224-226` — money from nothing (EXTRAS)**
`extra` is computed and `modifyIncome(extra)` is called, but the customer is never charged. The older `Strip`
computed `extra` and never used it at all — this looks like a wiring bug. Confidence: medium.

**28. `Strip.java:1022-1033`, `Attend.java:1017-1034` — per-customer work applied N times (FELINEORGY)**
`-0.5 ENERGY`, `+0.5 GROUP`, `+RoughenedUp` and the counter increment sit **inside** the per-customer loop,
so energy drain scales quadratically with crowd size. Confidence: medium.

**29. `MonsterFight.java:232-258` — `==` on `String`, and a never-assigned image**
```java
if (this.rapeText == "") { … }   // reference equality
```
A non-interned `""` from a `ComplexEnemy` makes the check fail and `applyTemplates` gets the wrong string.
Also `MonsterFight.java:219` passes `image`, declared `null` at line 210 and never assigned.
Confidence: medium.

**30. `Bartend.java:180`, `Strip.java:178`, `Attend.java:214` — off-by-one in customer selection**
`Util.getInt(0, getCustomers().size() - 1)` excludes the last customer (only `size-2` is ever picked).
`Util.getInt(1, size-1)` at `Bartend:192/208` has the same bias. Confidence: high (code), low impact.

**31. `Fight.java:171` — dead guard**
`if (i <= 100)` is always true because the `do/while` caps `i` at 50, so the intended "endless fight" guard
never fires. Confidence: high.

---

## LOW

**32. `PublicUse.java:190-196`, `202-208` — unreachable branch**
The second `else if (FinalValue(ANAL/VAGINAL) > 90 && getInt(0,100) > 50)` is a strict subset of the first and
assigns the same value `2`, so an intended lower-skill branch never applies. Confidence: high (unreachable), medium (intent).

**33. `PublicUse.java:47-69` — `SEXADDICT` tested twice**
```java
Trait.SEXADDICT at line 47 and again at line 67
```
Gives `+50` energy instead of `+25`; one was probably a different trait. Confidence: medium.

**34. `Offerings.java:42`, `48`**
`getMessages().get(0)` dereferenced with no size check; and `Util.getInt(0,100) < skill + initialSatisfaction/5`
can exceed 100, making loot guaranteed. Confidence: low.

---

## Port risks noted while reading

- **`HashMap<Charakter,…>` iteration order** (`PublicUse`, `BathAttendant`, `Bartend`) — the port must use
  deterministic ordering or faithfully preserve map semantics, because outcomes depend on iteration order.
- **`short` arithmetic** in `PublicUse.perform` (`totalTips`/`energy`/`timeTaken`) silently wraps in Java;
  C# will not without explicit unchecked casts. Must be mirrored exactly.
- **`int` division truncation** everywhere (`getInt`, `skill/2`, `size/10`) must be mirrored exactly.
- **`AttributeModification`'s `realModification != 0` no-op rule** must be reproduced for `Fight`/`MonsterFight`.
