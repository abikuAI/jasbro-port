# JaSBro — Diagnosis of the Non-Compiling Fork

**Date:** 2026-09-30
**Repo analyzed:** https://github.com/kingpingpong/JaSBro @ `11a9560` (2015-04-18, `master`)
**Reference game:** `C:\Games\Jasbro_Final` (JaSBro.jar, 2,377,147 bytes, "Jasbro R0.1.2 final")

---

## TL;DR

1. **This is not Flash or JavaScript — it is Java** (Swing GUI, built with Gradle). The "stream code" you're
   thinking of is the game's *content*: XStream-serialized XML for characters, items, events, and quests.
2. **The fork fails to compile for exactly one reason: one missing nested class.**
   `Perks.java:3144` calls `new Buff.AlreadyFull()`, but `Buff.AlreadyFull` does not exist in the
   tree Gradle actually builds. **One error in 533 files.** Adding the class makes the whole project
   compile cleanly and produce a working, launchable `JaSBro.jar`. ← *verified, see below*
3. **The bigger discovery:** the GitHub repo is an **older build** than the game you have working.
   The shipped `JaSBro.jar` contains **80 top-level classes that do not exist anywhere in the repo**,
   including three entire packages (`traits/perktrees/`, `gui/town/`, `game/realestate/`).
   So "fix the compile error" gets you a *runnable* game — but **not the same game** as your working jar.
4. **The shipped jar was therefore decompiled.** 616 source files recovered, compiling with
   **0 errors** and running the game with **zero exceptions**. See [`DECOMPILATION.md`](DECOMPILATION.md).

---

## 1. Why it does not compile

### The error

```
src/main/java/jasbro/game/character/traits/Perks.java:3144: error: cannot find symbol
                    character.addCondition(new Buff.AlreadyFull());
                                               ^
  symbol:   class AlreadyFull
  location: class Buff
1 error
```

That is the *complete* compiler output. 533 files, one error.

### The cause: a half-finished refactor that split the source in two

The repo contains **two complete copies of the source tree**:

| Tree | Files | Status |
|---|---|---|
| `src/main/java/jasbro/` | 533 | ← **this is what Gradle compiles** (default sourceSet) |
| `src/jasbro/` | 538 | ← added later, **ignored by the build** |

`build.gradle` has no `sourceSets` block, so Gradle uses its default: `src/main/java`.
The `src/jasbro` tree is dead weight as far as compilation is concerned.

The two trees are near-identical — **only 2 files differ**:

- `jasbro/Jasbro.java`
- `jasbro/game/character/conditions/Buff.java`  ← the important one

The last commit (`11a9560`) says it *"initialized inner class AlreadyFull in class Buff...
removes error message present in class Perks."* The author fixed the problem — **but in `src/jasbro`,
the tree the build ignores**, and left a commit message claiming it had deleted `src/main`.
It never did. `src/main/java` is still tracked, still canonical for Gradle, and still broken.

### The fix (verified working)

Add the missing nested class to `src/main/java/jasbro/game/character/conditions/Buff.java`
(it exists in `src/jasbro/.../Buff.java` as an empty stub):

```java
public class Buff extends Condition {
	public static class AlreadyFull extends Condition {
		private static final long serialVersionUID = -3313640460601389318L;
	}

	private String nameKey;
	...
```

> Note: this is a **stub with no behaviour**. It satisfies the compiler, but whoever wrote the
> trait `SHESALREADYFULL` presumably intended `AlreadyFull` to actually *do* something
> (the original author's own commit notes say *"Class still requires content"*).
> Treat this as making it compile, not as implementing the feature.

### Proof it works

| Step | Result |
|---|---|
| `javac` over `src/main/java` (533 files), unmodified | **1 error**, exit 1 |
| Apply the 3-line fix above | — |
| `javac` again | **0 errors**, exit 0, **1,267 class files** |
| Package into `JaSBro.jar` with correct manifest | 1.6 MB jar produced |
| `java -jar JaSBro.jar` | **Launches and runs** — initialises config, logging, image cache, GUI stays up |

(The launch test ran with `items/`, `npcs/`, `events/`, `quests/` but without `images/`, so the only
runtime noise was `Image not found: images/backgrounds/sky.jpg` — expected, not a defect.)

### Secondary issue: the build file is ancient (but not the blocker)

`build.gradle` uses Gradle-2.x-era syntax that **modern Gradle rejects outright**:

- `compile` / `testCompile` → removed in Gradle 7 (use `implementation` / `testImplementation`)
- `archiveName` → removed in Gradle 7 (use `archiveFileName`)
- `uploadArchives` → removed in Gradle 7
- `sourceCompatibility = 1.7` → JDK 20+ cannot target 7
- **No Gradle wrapper** is committed, so the build is not reproducible

This *was* built successfully with **Gradle 4.10.3 + JDK 8**. Prior work on this machine confirmed it.
Gradle 4.10.3 is old enough to accept all of the above.

**Dependency resolution is NOT a problem.** Every declared dependency is already in the local
Gradle cache at `~/.gradle/caches/modules-2/`, and all 34 runtime jars ship in the game's `lib/`.
`org.beanshell:bsh-classgen:2.0b4` is present on Maven Central (I checked — one version, 2.0b4).

---

## 2. The bigger finding: the repo is older than your game

I rebuilt the jar, then diffed its class list against the shipped one.

| | Shipped `JaSBro.jar` | Rebuilt from repo |
|---|---|---|
| Total classes | 1,638 | 1,267 |
| **Top-level** classes | **606** | **533** |

**80 top-level classes exist in your working game but nowhere in the GitHub repo**, including
entire packages:

- `jasbro/game/character/traits/perktrees/` — **14 classes** (AlchemistPerks, BartenderPerks,
  DancerPerks, DominatrixPerks, FighterPerks, FurryPerks, KinkyPerks, LegacyPerks, MaidPerks,
  MarketingPerks, NursePerks, SexPerks, SlavePerks, ThiefPerks, TrainerPerks, WhorePerks)
- `jasbro/gui/town/` — **21 classes** (QuestMenu, SlaveMarketMenu, RealEstateMenu, TownMenuNew, …)
- `jasbro/game/realestate/` — Plot, RealEstateSystem, BuyPlotMapMenu
- `jasbro/game/world/xml/` — 14 XStream requirement parsers
- plus `MonsterDickType`, `MonsterUtil`, `LegacyEventHandler`, `Struggle`, `Ritual`, `Rob`,
  `WorkForGuild`, `HouseUtil`, `RoomUnlock`, `SecurityState`, `SlaveMarket`, `IngredientItem`,
  `LootItem`, `SummoningItem`, `Crypt`, `QuestScreen`, `SecurityPanel`, …

Resource divergence confirms the same conclusion:

| Repo has | Shipped game has |
|---|---|
| `log4j.xml` (log4j **1.x**) | `log4j2.xml`, `log4j2-debug.xml` (log4j **2.x**) |
| *(nothing)* | `houses/*.properties` — 9 files (hut, house, mansion, palace, school, shrine, giantcastle, garrison, monastry) |
| *(no `rooms.xml`)* | `rooms.xml` (42 KB, 8,242-byte README.txt) |

**Conclusion:** GitHub `master` is a mid-2015 development snapshot. Your working game is a later
release (`R0.1.2 final`). They are different versions of the same project — the fork is the
*earlier* one. This matches what prior work found for `CharacterEditor.jar` independently.

---

## 3. So what should you actually do?

The fork can be made to **compile and run** — that part is solved and proven. But be clear about
what that buys you: a runnable *older* JaSBro, not a rebuild of your working game.

**If your goal is "make the GitHub repo build"** → the one-line fix is all you need. `build.ps1`
in this folder does it end-to-end.

**If your goal is "recover source for the game I actually have"** → **done.** `JaSBro.jar` was
decompiled into 616 source files that compile with 0 errors and run the game with no exceptions.
See [`DECOMPILATION.md`](DECOMPILATION.md) and `decompiled/source/`.

**If your goal is to modernise/port it** → you have both versions; treat the decompiled
`JaSBro.jar` as canonical for behaviour and the repo as readable reference for the subset it covers.

---

## 4. Decompiling the jar — DONE

**This has been carried out. See [`DECOMPILATION.md`](DECOMPILATION.md) for the full write-up.**

Summary: no single decompiler handled the whole jar, so it was decompiled with **both CFR 0.152
and Vineflower 1.12.0** and the results merged (their failure sets barely overlap). Two files
needed hand-fixing — both purely structural block-scoping repairs, not behavioural changes.

| Metric | Result |
|---|---|
| Source files recovered | **616** |
| Compile result | **0 errors** |
| Classes rebuilt vs jar's 1,638 | **1,637** (only a synthetic switch-map class differs) |
| Runtime test | **Launches, zero exceptions** |

Output: **`decompiled/source/`**, plus a runnable `decompiled/JaSBro-from-decompiled.jar`.

---

## 5. Environment notes

- The machine has **JRE 1.8 only** at `C:\Program Files\Java\jre-1.8` — **no `javac`**.
- A portable JDK 8 and Gradle 4.10.3 used by earlier work at
  `%LOCALAPPDATA%\Temp\simbro-gradle\` have had their `bin\` directories **emptied** — they are gone.
- **This folder now contains a working JDK 8** at `tools\jdk8` (Temurin `1.8.0_504`), downloaded fresh.
- Network note: this machine's schannel TLS is broken (`SEC_E_NO_CREDENTIALS`) — `Invoke-WebRequest`,
  `curl.exe`, and even Git's bundled curl all fail. **Python's `urllib` works** (bundles its own
  OpenSSL). For Git, use `git -c http.sslBackend=openssl ...`. This will bite you again.
