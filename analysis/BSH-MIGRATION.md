# The BeanShell Problem — content that is executable

**This is the single biggest port risk in JaSBro.** It needs a decision before any port code is written.

---

## ✅ DECISION (recorded) — Option A, with Option C where the block is declarative

**Decided:** transpile the BeanShell blocks to C#, applying the data-driven treatment (Option C)
opportunistically to blocks that turn out to be pure expressions.

**Rationale.** C# and BeanShell share C-family syntax for everything these blocks use, and BeanShell
is dynamically typed — meaning behaviour that is *implicit* today must become *explicit* in the port.
That is a feature: it forces the two variable stores (below) into the open instead of leaving them as
an invisible contract. Keeping an interpreter (Option B) would re-import exactly the ambiguity we are
trying to remove, plus Java integer-division semantics we would have to re-implement anyway.

**How the 166 blocks are handled by tier:**

| Tier | Count | Treatment |
|---|---|---|
| expression | 95 | Transpile to C# expressions; candidate for data-driven replacement |
| simple | 11 | Transpile to a statement or two |
| api-call | 1 | Direct method call |
| script | 38 | Transpile to a C# method |
| **script + api** | **21** | Transpile to a C# method **with tests**, since these are the algorithms that must match exactly |

The four contest scripts share one near-identical structure and collapse to a single parameterised
C# implementation.

**Two constraints the decision MUST respect** (both surfaced by the content-scripting audit —
see `audit/CONTENT-SCRIPTING.md`):

1. **There are two parallel variable stores, and content depends on the split.**
   `CustomQuest.getAttributeMap()` is a *different map* from `Quest.variables`, which is what
   `CustomQuestStage.customize` reads and `WorldEventSaveVariable` writes. **56 content elements**
   depend on this. Collapsing them into one dictionary changes behaviour in both directions.
2. **`eval("return this.variables;")` reflects over the script's own namespace.**
   Any variable a script assigns silently becomes game state. In the port these must be *declared*
   explicitly, or they vanish on save.

**Because of (1) and (2), the port models script state as two explicitly named stores** — not one
bag — and every variable that crosses the content/save boundary is enumerated in code.

**Status:** decided; execution is in progress. The save and content foundations this constrains are
already built and verified (`port/Simbro.Core`, `SAVE-FORMAT.md`).

---

## What's going on

JaSBro lets content files define logic in **BeanShell** (a Java-like scripting language), executed at
runtime through a shared `bsh.Interpreter`:

| Location | Code |
|---|---|
| `WorldEventCode.java:19` | `worldEvent.getInterpreter().eval(this.code);` |
| `CodeRequirement.java:16` | `return (Boolean)triggerParent.getInterpreter().eval(this.code);` |
| `CustomQuest.java:255` | `(String[])this.getInterpreter().eval("return this.variables;")` |
| `Jasbro.java:608-638` | `getInterpreter()` / `cleanupInterpreter()` — one shared interpreter |

**In your install: 166 code blocks across 40 XML files.**

### Where they live

| Count | Parent element |
|---|---|
| 81 | `<requirement>` |
| 79 | `<jasbro.game.world.customContent.effects.WorldEventCode>` |
| 6 | `<jasbro.game.world.customContent.requirements.CodeRequirement>` |

The heaviest files are the contest quests: `contestQuestSexContest.xml` (32 blocks),
`contestQuestBartenderContest.xml` (15), `contestQuestDancerContest.xml` (15),
`contestQuestMaidContest.xml` (13).

### How complex are they?

| Kind | Count | Meaning |
|---|---|---|
| expression | 95 | A single expression — trivially translatable |
| simple | 11 | One short statement |
| api-call | 1 | Single API invocation |
| script | 38 | Control flow, self-contained |
| **script + api** | **21** | **Real algorithms calling into the game API** |

**107 of 166 are trivial. 59 have control flow, and 21 of those are substantial.**

---

## This is not toy scripting

These are genuine game algorithms. The bartender contest scoring, verbatim:

```java
int score = 0;
score += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 4;
score += character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 4;
score += character.getFinalValue(SpecializationAttribute.BARTENDING);

if (bonusPointsVersion == 1) {
  score += character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) * 4;
} else {
  score += character.getFinalValue(SpecializationAttribute.COOKING);
}

int targetScore = difficulty * difficulty * 10;

int place;
if (score > targetScore) {
  place = 1;
} else {
  skillPoints = 0;
  if (score > targetScore * 19 / 20) {
    place = 2;
    priceMoney = priceMoney / (place * place * difficulty);
  } else if (score > targetScore * 18 / 20) { ... }
```

And a contest generator with randomness:

```java
int day = data.day;
int generatedNumber = (Util.getInt(0, 10) + day / 100) / 5;
int amountContests = Math.min(3, Math.max(1, generatedNumber));

List possibleContests = new ArrayList();
possibleContests.add("contestQuestMaidContestGeneration");
...
for (int i = 0; i < amountContests; i++) {
    difficulty = Math.min(3, Math.max(1, Util.getInt(-2, 2) + day / 100 + difficultyModifier));
```

Note the integer division (`score += character.getFinalValue(...) / 4`) — **truncation semantics matter
for parity.** These must produce identical results to the Java build.

---

## The Java API surface content depends on

Extracted from all 166 blocks — this is the shim/API a port must expose to scripting:

| References | Symbol |
|---|---|
| 26 | `TextUtil.getInstance()` |
| 11 | `Util.getInt(...)` |
| 7 | `auctionHouse` (script variable) |
| 6 | `protagonist` (script variable) |
| 5 | `CharacterSpawner.create(...)` |
| 5 | `Option.getSelectionObject()` |
| 5 | `specializations` (script variable) |
| 4 | `Contests.add(...)` |
| 4 | `ImageUtil.getInstance()` |
| 4 | `CharacterSpawner.spawnEnemy(...)` |
| 3 | `Characters`, `data`, `Jasbro.getInstance()` |
| 2 | `CharacterSpawner.spawnChild`, `Events.get/add/size`, `Util.getRnd`, `Math.min/max`, `Id.equals` |

Character statistics are read through `character.getFinalValue(...)` with enum keys like
`BaseAttributeTypes.CHARISMA`, `SpecializationAttribute.BARTENDING`, `Sextype.FOREPLAY`.

---

## Migration options

### Option A — Transpile BeanShell → C# (recommended)
C# and BeanShell are both C-family with near-identical syntax for the constructs used here.
`data.characters.add(x)` → `data.Characters.Add(x)` is mechanical.

- **Pros:** result is compiled, type-checked, unit-testable; no interpreter dependency; no runtime
  eval surface; the 107 trivial blocks convert almost automatically.
- **Cons:** the 21 complex blocks need hand review; some dynamic idioms need explicit typing.
- **Effort:** moderate and *bounded* — 59 real blocks, and the four contest scripts share one
  near-identical structure, so they collapse to one parameterised implementation.

### Option B — Keep an interpreter (embed a JS engine or port BeanShell)
- **Pros:** content stays byte-identical; mods keep working unchanged.
- **Cons:** embeds a scripting runtime in a Godot/.NET app; hard to debug; performance and security
  surface; a JS engine is *not* BeanShell, so `character.getFinalValue(...)` etc. still need a shim
  with correct integer semantics. Reproducing Java's integer division and overflow is a real hazard.

### Option C — Replace scripts with data-driven effects
- **Pros:** cleanest long-term model; content becomes declarative; easiest to validate.
- **Cons:** the largest migration — the scoring algorithms have no natural data representation.

### Recommendation
**Option A, with Option C applied opportunistically.** Transpile the 59 complex blocks to C#, keeping
the 107 simple ones as a declarative expression/eval shim. Collapse the four contest variants into one
parameterised scoring function. Lock behaviour with golden fixtures from the Java build.

**Whatever you choose, decide it first.** It determines the content schema, the save format, and
whether `Simbro.Core` needs an embedded interpreter — so it constrains every other decision.

---

## Parity warning

Two things will silently break parity if not handled deliberately:

1. **Java integer division and overflow.** `score / 4` truncates toward zero; C# `int / int` does the
   same, but any accidental promotion to `float`/`double` changes results. BeanShell is dynamically
   typed, so a value can silently become a double.
2. **`eval("return this.variables;")`** — `CustomQuest` reflects over the *script's own variable
   namespace* to build an attribute map. That is an implicit dynamic contract: any variable a script
   assigns becomes game state. In a compiled port this must be modelled explicitly, or those variables
   vanish.
