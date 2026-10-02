# Which engine for the JaSBro port?

**Decision (confirmed after re-examination): Godot 4.7.2 Mono + C#, with the simulation in a plain
`Simbro.Core` .NET 8 assembly and Godot strictly as the presentation layer.**

> **Version note.** Earlier revisions of this document said **4.7.1**. Verified against the GitHub
> release API: current stable is **4.7.2** (published 2026-08-18). The Windows x64 Mono build is
> `Godot_v4.7.2-stable_mono_win64.zip`. Export templates are a separate ~1.2 GB download and are only
> needed to *export*, not to build and run.

---

## The decision was re-examined, and one thing changed

The engine call was reviewed again *after* the audits. The conclusion held, but **the reasoning
behind it shifted**, and the honest version is worth recording.

The original case leaned partly on Godot's rendering and scene system. After 151 findings, that is
the wrong emphasis. The game's substance is roughly **10,000 lines of dense simulation rules** —
`Attend.java` alone is 2,347 lines, `Bartend` 1,032, `Strip` 1,008 — against ~105 GUI files that are
mostly forms, lists and image panes.

**Therefore the engine choice is much lower-stakes than this document originally implied.** Because
`Simbro.Core` is a plain .NET 8 assembly that has no knowledge of Godot, every engine option shares
the same hard 90%. **The decision is reversible:** the presentation layer could be swapped for
another toolkit without touching the simulation.

### Where Godot genuinely fits

- **Image-heavy content** — 413 MB of character art, 57 MB of images, GIF animation. Native and good.
- **C# ≈ Java** — makes the 166 BeanShell blocks a mechanical transpile rather than a rewrite.
- **Filesystem-first** — matches the on-disk XML content model; no asset-DB import lock-in.
- **MIT licence**, no royalties, one-click export to Windows/Linux/macOS.

### Where it genuinely does not — stated plainly

**Godot's Control nodes are adequate for panels but were not designed for dense data. There is no
real data grid.** A character list with a dozen sortable columns, filters and rich tooltips must be
hand-built from `Tree`/`ItemList`. Tooltips are basic and text layout is not made for document-like
density. For a game that is fundamentally *spreadsheets with pictures*, this is the weak spot.

**The closest genuine alternative is Avalonia UI (.NET).** It uses the same `Simbro.Core` assembly
with zero interop, and XAML data binding is purpose-built for exactly this kind of dense data UI —
`DataGrid`, `ItemsRepeater`, rich styling and text all first-class. For JaSBro as it exists today,
**Avalonia is arguably the better technical fit.** It was not chosen because it gives up game-engine
feel — no scene system, manual animation — and Godot keeps the door open to animation and effects.

**The hedge that makes this safe:** finish `Simbro.Core` before investing in presentation, then
spike one real screen (the character list with images). If Godot's data-UI friction proves painful at
that scale, switching costs a spike rather than a port.

---

## What kind of game is it, technically?

Not an action game. It is:

- a **dense, panel-based management UI** — lots of lists, tabs, tabs-in-tabs, forms, tooltips;
- **2D static and animated images** (jpg/png/gif) presented full-screen or in panels;
- **content-driven** — characters, rooms, items, quests, events all defined in XML on disk;
- **low-frequency simulation** — shifts and days tick, not frames;
- heavy on **state and rules**, light on physics, animation blending, and 3D.

That profile points at a **UI toolkit + a testable domain core**, not a game engine's rendering muscle.

---

## Why Godot fits

| Requirement | Godot 4 |
|---|---|
| Dense panel UI | Control nodes + containers handle this well; themeable |
| 2D images/GIF/animation | Strong built-in 2D; `AnimatedTexture`/`SpriteFrames` |
| Lazy/large asset sets | Resource loading is explicit — you control what loads and when |
| Packaging | One-click export to Windows/Linux/macOS |
| Modding / content folders | Plain filesystem access, no asset-DB import lock-in |
| C# for domain logic | Full .NET 8 — records, immutable collections, plain unit tests |
| License/cost | MIT, no royalties, no runtime fees |

Critically, the **domain logic can live entirely outside Godot** in `Simbro.Core`. That means the
rules engine is testable in a plain `dotnet test` run — no engine boot, no window, no flakiness. For a
game whose correctness is its whole value, that is the decisive property.

---

## Why the alternatives lose

### Unity + C#
Capable, but heavier toolchain, its asset pipeline fights on-disk content folders (the game's core
modding model), licensing has a history of unilateral changes, and running headless tests is clumsier.
No advantage that matters here.

### Web / TypeScript (Electron, or browser)
Genuinely tempting — dense panel UI is what the web does best, and Electron packages to desktop. But
it trades a native runtime for a browser runtime (memory, startup), and the simulation becomes hard to
test in isolation from the DOM. Reasonable second choice, not the best one.

### Rust + GDExtension / Rust-only
No measured performance bottleneck exists to justify it. It would add FFI, DTO duplication, native
packaging burden, and much slower iteration — for a game that spends its time in XML parsing and
integer arithmetic.

### Keep Java (JavaFX or modernize Swing)
Would be the cheapest path to *working*, but it does not satisfy the actual goal: off Java.

### Python + Qt/Pygame
Poor packaging and distribution for a 470 MB content tree; weakest option.

---

## New evidence from this audit: the scripting problem

The strongest new fact is that **content is executable** — 40 XML files contain **166 embedded
BeanShell code blocks**, evaluated at runtime via `bsh.Interpreter.eval(...)`, with lines that call
straight into the game API:

```java
Jasbro.getInstance().getGui().addMessage(new TravellingMerchantScreen());
data.characters.add(capturedChar);
```

This is the single most port-hostile thing in the codebase, and it discriminates between engines:

- **C# is syntactically very close to Java/BeanShell.** Both are C-family with near-identical method
  call, loop, and conditional syntax — `data.characters.add(x)` → `data.Characters.Add(x)` is a
  mechanical rewrite. All 166 blocks can be transpiled with light manual review, and the result is
  compiled, type-checked, testable code rather than an interpreter dependency.
- **A dynamic-language target (JS/TS) would also work**, since BeanShell is dynamically typed.
- **Rust or C++ would be painful**, because the blocks lean on dynamic access, string building, and
  untyped collections.

So the scripting migration is a **bounded, largely mechanical job in C#** — which is a real argument
for the choice, not merely a neutral one.

---

## What I'd change or add to the prior decision

The prior `ENGINE_DECISION.md` is sound. Three additions from this audit:

1. **Treat the BeanShell migration as a Phase-0 design decision**, not an implementation detail.
   Decide up front: transpile the 166 blocks to C#, or replace them with a data-driven effect system.
   Everything else in the architecture depends on it.
2. **Budget for the 90 classes with no repo twin** (`perktrees/`, `gui/town/`, `game/world/xml/`,
   `game/realestate/`). These can only be understood from decompiled code and are the least
   understood part of the game.
3. **The largest port work item is the activity simulation** — `Attend.java` (2,347 lines, tripled
   since the fork), `Bartend` (1,032), `Strip` (1,008). Plan for that being where most effort lands,
   and write golden fixtures for it specifically.

---

## Recommendation

**Proceed with Godot 4 Mono + C#**, domain logic in a standalone `Simbro.Core` .NET assembly, Godot
strictly as the presentation layer — as already decided. The audit found nothing that overturns it,
and the BeanShell discovery actively supports a C-family target language.

**One caveat worth stating plainly:** if your priority shifts from *"a maintainable game we own"* to
*"running with minimum effort"*, the cheapest option by far is to keep the Java build and just fix
its bugs (the audit is finding plenty). The port is the right call for ownership and longevity, but it
is a large project — the released game is 67,000 lines, and ~10,000 of those are dense simulation
rules with no documentation.
