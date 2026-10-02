# JaSBro — Codebase Audit & Bug-Hunt Plan

**Target:** `decompiled/source/` — the 616-file recovered source that matches your working game
**Size:** 616 files · 67,248 lines · 1,637 classes
**Date:** 2026-09-30

---

## Why this plan looks the way it does

Three facts from reconnaissance shape everything below:

1. **The decompiled source has no comments.** 36 comment lines total, and those are decompiler
   artifacts. The GitHub repo has **1,108 real comment lines** for the 533 classes it shares.
   So we audit the decompiled code but *read intent from the repo* wherever a class exists there.
   This is the single highest-leverage trick available.
2. **It's a 67k-line codebase — no single pass will cover it.** The audit has to fan out by package
   and then converge. Anything claiming "reviewed the whole codebase" in one pass is guessing.
3. **We already have two confirmed bugs from your own log**, before the audit even started. That
   validates the approach and gives us a template for what to hunt.

---

## Already found (from `C:\Games\Jasbro_Final\jasbro.log`)

### B1 — `TraitRequirementParser` crashes hard on unknown trait ⚠️ *fragility remains*
```java
// jasbro/game/world/xml/TraitRequirementParser.java:12-14
String trait = requirementElement.getAttribute("trait");
Validate.notBlank(trait);
return new TraitRequirement(Trait.valueOf(trait));   // <-- unguarded
```
`Trait.valueOf()` throws `IllegalArgumentException` for an unknown name. From `rooms.xml` that
propagates through `RoomLoader.loadRooms` → `RoomInfoUtil.<clinit>` → **`ExceptionInInitializerError`**,
which permanently poisons the class and crashes new-game creation. Your log shows exactly this
(`Trait.BEAUTICIAN`).

**Status:** the *trigger* is gone — all current content is clean (I verified every trait reference
across 233 content files against all 372 enum constants). **The defect is still there:** any
future content edit with a typo takes the game down instead of logging a warning.

**Contrast:** `CharacterFileLoader.java:319` guards the identical call with try/catch and logs
`"Error on loading trait"`. Same operation, two different failure policies. That inconsistency is
the actual bug.

### B2 — NPE dragging equipment in the inventory ⚠️ *live, reproducible*
```java
// jasbro/gui/dnd/MyEquipmentTransferHandler.java:32-35
Item item = ((Inventory.ItemData)((JList)c).getSelectedValue()).getItem();  // getSelectedValue() can be null
if (item instanceof Equipment) {
   Equipment equipment = (Equipment)item;
   this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Mode.AUTOMATIC));
```
`JList.getSelectedValue()` returns `null` when nothing is selected → immediate NPE. Your log has
this **three times** across separate sessions. Same class, more unguarded dereferences:
- L77 `this.getItem(support).getEquipmentType()` — `getItem()` returns `null` on any error (L109-111)
- L106-108 `(Equipment) Jasbro.getInstance().getItems().get(...)` — `get()` returns `null` for an unknown id

**This is a one-line fix and a guaranteed win.** It's also our template: *unguarded dereference of a
lookup that can fail.*

---

## Reconnaissance numbers (the audit's raw material)

**Largest files — complexity hotspots, audit these first:**

| Lines | File |
|---|---|
| 2347 | `game/character/activities/sub/business/Attend.java` |
| 1087 | `game/character/traits/perktrees/FurryPerks.java` |
| 1067 | `game/character/Charakter.java` |
| 1032 | `game/character/activities/sub/business/Bartend.java` |
| 1008 | `game/character/activities/sub/business/Strip.java` |
| 963 | `game/character/traits/TraitEffect.java` |
| 947 | `game/character/traits/SkillTreeItem.java` |
| 501 | `gui/pictures/ImageUtil.java` |

**Package sizes — natural audit units:**

| Unit | Files | Lines |
|---|---|---|
| `game/character` | 161 | 27,261 |
| `game/world` | 106 | 6,424 |
| `gui/pages` | 30 | 5,471 |
| `gui/town` | 18 | 4,028 |
| `util/eventEditor` | 49 | 3,245 |
| `util/itemEditor` | 39 | 2,764 |
| `game/events` | 30 | 2,521 |
| `gui/objects` | 30 | 2,422 |
| `game/items` | 52 | 2,282 |

**Risk-pattern census (pre-audit triage, not findings yet):**

| Pattern | Hits | Files |
|---|---|---|
| `catch (Exception …)` | 85 | 42 |
| **Empty / swallowing catch** | **32** | **23** |
| `e.printStackTrace()` | 31 | 11 |
| `Thread.sleep` | 19 | 11 |
| `synchronized` | 36 | **4** |
| `return null` | 68 | 38 |
| `== null` checks | 279 | 119 |
| `System.out/err` | 5 | 3 |

The 32 empty catches are concentrated in the **file loaders** (`CharacterFileLoader` ×2,
`EventAndQuestFileLoader` ×2, `NpcFileLoader`, `FameUnlockLoader`) — meaning corrupt content is
skipped silently. That is very likely why bugs in this game are hard to reproduce.

---

## The plan

### Phase 0 — Make findings verifiable (small, do first)
The audit is worthless if we can't reproduce what we find.
- A **headless smoke harness**: boot the game, load all content, exit; assert no exceptions.
  Gives every finding a pass/fail check.
- A **content validator**: walk `characters/ items/ npcs/ events/ quests/ rooms.xml fameUnlocks.xml`
  and report every reference that doesn't resolve (traits, items, images, enum names, ids).
  This is the generalized version of B1 — it turns "the game crashed" into "these 40 content files
  are broken", up front.
- **Baseline startup timing**, so performance work has a before/after.

*Deliverable:* `tools/validate-content.py`, `tools/smoke-boot.ps1`, baseline numbers.

### Phase 1 — Structural map (understand before judging)
- Call/dependency graph; identify entry points (`Jasbro.main`), the startup path, the save/load path.
- Per-file complexity: largest methods, deepest nesting, fan-in/fan-out.
- **Cross-reference map** from each decompiled class to its GitHub-repo twin, so the audit can pull
  the original comments and intent for the 533 shared classes.

*Deliverable:* `MAP.md` — architecture, entry points, hotspots, and the repo-twin index.

### Phase 2 — Per-package deep audit (the bulk, fans out)
Audit each package as an independent unit, in priority order (biggest + most bug-dense first):
`game/character` → `game/world` → `gui/pages` → `gui/town` → `game/events` → `game/items` →
`util/*editors` → remainder.

Each unit reports findings against a fixed taxonomy, with file:line and a code quote.

*Deliverable:* one findings file per package.

### Phase 3 — Cross-cutting bug hunts (targeted, high yield)
Specific defect *classes*, each searched across the whole codebase — much more productive than
reading files top to bottom:

| Hunt | What we look for |
|---|---|
| **Unguarded lookups** | `Map.get()`, `valueOf()`, `getSelectedValue()`, `getParent()` dereferenced without a null check — **this is B1 + B2 generalized** |
| **Exception swallowing** | the 32 empty catches; confirm each is deliberate or a bug |
| **Resource leaks** | streams/readers/zip handles not closed — relevant: truevfs is used heavily |
| **Concurrent modification** | collection modified while iterated (a classic in games) |
| **Swing EDT violations** | UI mutated off the event thread; `Thread.sleep` on the EDT (19 sites) |
| **Thread safety** | 36 `synchronized` in only 4 files — check what's *not* guarded |
| **Numeric hazards** | divide-by-zero, int overflow, `Float`/`Double` equality |
| **Save/load compat** | XStream field changes that break old saves — a major real-world bug source |
| **Content/code drift** | enum values referenced from data that don't exist (B1's family) |

### Phase 4 — Startup & load performance (your main complaint)
A dedicated investigation, not a side note:
- Profile the actual startup path; measure where wall-clock time goes.
- Determine **what scales with image/folder count** — recursive walks, eager decode vs lazy load,
  cache rebuilt each start, missing index file, O(n²) scans.
- Produce a measurable before/after, and concrete fixes (lazy loading, a precomputed index,
  parallel decode, caching).

**✅ DIAGNOSIS COMPLETE — see [`PERF-STARTUP.md`](PERF-STARTUP.md).** Traced the full startup path and
measured against your live content folder. Headline: the main menu does **not** scan `characters/`
or `images/` — character loading runs in `optimized=true` mode and skips image files entirely. The
scale-dependent work happens at New Game / load-save, via recursive directory walks (F1), growth of
`properties.xml` (F2), per-file `MimetypesFileTypeMap` allocation (F4), a fresh `XStream` per XML
file (F5), and a 16-entry image cache (F7). No code changed — diagnosis only, as requested.

### Phase 5 — Consolidate
- Deduplicate findings, rank by severity × likelihood × fix cost.
- Produce a **prioritized defect register** with reproductions.
- Separate **real bugs** from **decompiler artifacts** — critical, since we're reading decompiled
  code and some oddities are not the author's.

---

## Deliverables

| File | Contents |
|---|---|
| `MAP.md` | Architecture, entry points, hotspots, repo-twin index |
| `AUDIT-FINDINGS.md` | Consolidated defect register, ranked |
| `audit/<package>.md` | Per-package findings |
| `PERF-STARTUP.md` | Startup analysis with measurements |
| `tools/validate-content.py` | Standalone content validator (useful forever) |
| `tools/smoke-boot.ps1` | Headless boot test |

---

## Scope options

The full Phase 0-5 on 67k lines is a **large** job. Realistic options:

| Option | Covers | Effort |
|---|---|---|
| **A. Targeted** | Phase 0 + Phase 3 + Phase 4 — hunting your actual symptoms (crashes, startup) | Moderate |
| **B. Full audit** | Phase 0-5, everything above | Large |
| **C. Quick wins first** | Just fix B1 + B2 and ship the content validator, then decide | Small |

**My recommendation: C → A → B.** Fix the two known bugs and stand up the content validator first
(you get immediate value and a regression net), then run the targeted hunts, then expand to a full
audit if the findings justify it.

---

## Caveats I want on record

- **Decompiled ≠ original.** Some "bugs" will be decompiler artifacts. Every finding gets checked
  against the GitHub repo twin and, where possible, reproduced — before it goes in the register.
- **No comments in the audit target.** Intent comes from the repo for the 533 shared classes; for
  the 83 classes that exist *only* in the shipped game (`traits/perktrees/`, `gui/town/`,
  `game/realestate/`, …) we have behaviour only — those are the hardest and most interesting.
- **I already produced one false positive during reconnaissance** (a trait audit regex that missed
  bare enum constants like `LOYAL,`). Caught and corrected, but it's the exact failure mode to
  expect from automated pattern-hunting — which is why Phase 0 exists and why findings get verified.
