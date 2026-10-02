# Decompiling `JaSBro.jar` — Results

**Goal:** recover Java source for the game you actually have working (`R0.1.2 final`),
since the GitHub fork turned out to be an older build.

**Result: success.** The reconstructed source compiles cleanly and the jar rebuilt from it
runs the game with no exceptions.

| Metric | Value |
|---|---|
| Classes in shipped jar | 1,638 (606 top-level) |
| Source files recovered | **616** |
| Compile result | **0 errors** (javac 1.8, 5 s) |
| Classes produced on rebuild | **1,637** |
| Classes in jar but not rebuilt | **1** — `ActivityType$1` (synthetic switch-map, see below) |
| Classes rebuilt but not in jar | **0** |
| Manual fixes required | **2 files** |
| Runtime test | **Launches, no exceptions** |

---

## Where things are

| Path | What |
|---|---|
| **`decompiled/source/`** | **The deliverable.** 616 `.java` files, 100% compilable. |
| `decompiled/JaSBro-from-decompiled.jar` | Jar packaged from that source — runnable. |
| `decompiled/cfr/` | Raw CFR output (606 files) |
| `decompiled/vineflower/` | Raw Vineflower output (617 files) |
| `decompiled/merged/` | The merge, before the last two fixes |
| `decompiled/classes-*/` | Compile outputs at each stage |
| `tools/` | `cfr-0.152.jar`, `vineflower-1.12.0.jar`, JDK 8 + JDK 21, helper scripts |

---

## Method

The jar is **not obfuscated** — all package, class, field and method names survive, which is
why the output is readable rather than `a/b/c`.

No single decompiler handled the whole jar, so I ran two and merged:

| Decompiler | Files | Time | Broken classes |
|---|---|---|---|
| **CFR 0.152** (JDK 8) | 606 | 20 s | `Attend`, `Strip` |
| **Vineflower 1.12.0** (JDK 21) | 617 | 12 s | `ActivityType`, `Attend`, `Bartend`, `Offerings`, `EventManager`, `BusinessCalculations`, `MyGifImageObject`, `ItemEditorPanel` |

The failure sets barely overlap, so the merge is mostly free:

- **Base:** Vineflower (better structured output)
- **Substituted from CFR (7 classes):** `ActivityType`, `Bartend`, `Offerings`, `EventManager`,
  `BusinessCalculations`, `MyGifImageObject`, `ItemEditorPanel`
  — for `ActivityType`, Vineflower's separate `ActivityType$1.java` was removed because CFR
    inlines the synthetic switch-map class.
- **`Attend`** was broken by *both* → fixed by hand (below)

Merge logic: `tools/mergeplan.py` (diagnose) and `tools/domerge.py` (apply).

### Manual fix 1 — `Attend.java`: restore per-case braces

The decompilers flattened the `switch` bodies, dropping the braces that originally delimited
each case. Case-level locals from *different* cases therefore landed in one shared block:

```java
case GROPE:
    Iterator i$x = ...;   // case-level
case LOOK:
    Iterator i$x = ...;   // <-- "already defined in method perform()"
```

Java forbids shadowing a local from an enclosing block, hence **26 errors**. Wrapping each case
body in `{ }` restores the original scoping. Braces do not affect fall-through (that is control
flow, not scope), so semantics are unchanged.

`tools/brace_cases.py` found **4 switch bodies** and braced **54 case labels**.

> An earlier attempt (`tools/fix_dups.py`) tried to rename the duplicate variables instead. That is
> the wrong fix here: several duplicates shared one block, so renaming one variable's span would
> swallow the next declaration. Kept only as a record of the dead end.

### Manual fix 2 — `ItemEditorPanel.java`: split a merged variable

The decompiler merged two differently-scoped locals into one name. The first is captured by an
anonymous `DocumentListener` (so it must stay `final`), which makes the second assignment illegal:

```java
final JTextArea textArea  = new JTextArea();   // tab 1, referenced by anonymous listener
...
        textArea = new JTextArea();            // tab 2 — "cannot assign a value to final variable"
```

Fixed by giving the second tab its own `final JTextArea textArea2`.

---

## Verification

**1. Compile** — all 616 files against the game's own `lib/`:

```
javac exit: 0   errors: 0   time: 5s
classes produced: 1637
```

**2. Class-set diff** against the shipped jar:

```
shipped classes : 1638
recompiled      : 1637
in shipped but NOT recompiled: 1
   - jasbro/game/character/activities/ActivityType$1.class
recompiled but NOT in shipped: 0
```

`ActivityType$1` is a **compiler-generated synthetic** class holding the `$SwitchMap` array that
`javac` emits for a `switch` over an enum. CFR rewrote that switch as if/else chains, so no
synthetic class is generated. It is not a real class and its absence changes no behaviour.

**3. Runtime** — launched against the real game data (`items/`, `npcs/`, `events/`, `quests/`,
`images/`):

```
RESULT: RUNNING after 45s - game launched
exceptions in game log: NONE
```

The only log line is a benign log4j2 config notice (`Unable to locate appender Console`).

---

## Reproduce it

```powershell
# 1. decompile with both tools
tools\jdk8\bin\java.exe  -jar tools\cfr-0.152.jar original\JaSBro-reference.jar --outputdir decompiled\cfr
tools\jdk21\bin\java.exe -jar tools\vineflower-1.12.0.jar -dgs=1 original\JaSBro-reference.jar decompiled\vineflower

# 2. merge (Vineflower base + CFR for the 7 classes it breaks)
& $py tools\domerge.py .

# 3. apply the two manual fixes (already applied in decompiled\source)
& $py tools\brace_cases.py .

# 4. compile against the game's own lib
tools\jdk8\bin\javac.exe -nowarn -encoding UTF-8 -cp "<lib\*.jar>" -d out @sources.txt
```

---

## Caveats

- **This is decompiled code, not original source.** Comments, local variable names, formatting,
  generics and lambda/loop shapes are all reconstructed. It is faithful enough to recompile and
  run, but it is not what the author wrote.
- **Only `Attend.java` and `ItemEditorPanel.java` were edited by hand.** Both edits are structural
  (block scoping), not behavioural.
- **The bytecode is Java 7-era.** It recompiles under JDK 8. A newer JDK will complain about
  `source/target 7`; use the bundled `tools/jdk8`.
- **Attribution:** JaSBro was originally created by **Teferus**. Keep the attribution intact if you
  republish anything derived from this.
