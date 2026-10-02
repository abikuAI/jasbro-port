# Building and reproducing

Two things are intentionally **not** in this repository, both for hard reasons. This file records
exactly how to obtain them.

---

## 1. Toolchains (~890 MB, excluded)

GitHub rejects any file over 100 MB, and five of these exceed it:

| File | Size |
|---|---|
| `tools/jdk21.zip` | 195.6 MB |
| `tools/godot/.../Godot_v4.7.2-stable_mono_win64.exe` | 173.0 MB |
| `tools/jdk21/lib/modules` | 134.4 MB |
| `tools/godot-download/Godot_v4.7.2-stable_mono_win64.zip` | 111.2 MB |
| `tools/jdk8.zip` | 101.5 MB |

They are also unmodified third-party downloads with nothing project-specific about them.

### What is needed for what

| Task | Needs |
|---|---|
| Compile the decompiled Java | **JDK 8** (the source is Java 7-era; JDK 8's `javac` accepts it) |
| Run the decompiler yourself | JDK 21 + CFR + Vineflower |
| Build the C# port | **.NET 8 SDK** (already present: 8.0.131) |
| Build the Godot layer | **Godot 4.7.2 Mono** + .NET 8 SDK |

### Fetching them

`tooling/get-godot.py` downloads the Godot 4.7.2 Mono editor and **verifies its SHA512** against the
release's own `SHA512-SUMS.txt`, deleting the archive if the hash does not match.

```powershell
$py = "<path to python>"
& $py tooling\get-godot.py .\tools\godot-download
Expand-Archive .\tools\godot-download\Godot_v4.7.2-stable_mono_win64.zip .\tools\godot
```

**JDK 8** — used for compiling the decompiled source:

- Eclipse Temurin 8 (`1.8.0_504` was used here): <https://adoptium.net/temurin/releases/?version=8>
- JDK 21, for running Vineflower: <https://adoptium.net/temurin/releases/?version=21>

The project's own `analysis/build.ps1` expects JDKs at `tools/jdk8` and `tools/jdk21`.

> **A note specific to this machine.** Its schannel TLS stack is broken
> (`SEC_E_NO_CREDENTIALS`), so `Invoke-WebRequest`, `curl.exe` and Git's bundled curl all fail
> against HTTPS. Python's `urllib` works, which is why the fetchers are Python. Git operations need
> `git -c http.sslBackend=openssl`.

---

## 2. The game folder and its artwork (~471 MB, excluded)

`characters/` (413.7 MB) and `images/` (57.8 MB) are **the original author's artwork**. They are not
this project's work, they are far too large for a git repository, and redistributing them is a
different act from studying the code.

**What is included instead:** every character `properties.xml` and all item/event/quest/room XML —
i.e. the *structure* the code operates on — with the images removed.

**To run the original game or the parity harness, obtain the game itself.** It is freely distributed
by its author; the analysis here was performed against a build at `C:\Games\Jasbro_Final` with
window title *"Jasbro R0.1.2 final"*.

The layout expected by the harnesses:

```
<game>/
  JaSBro.jar
  characters/<Character>/properties.xml   + images
  images/
  items/  events/  quests/  npcs/
  rooms.xml  config.ini  fameUnlocks.xml
  lib/*.jar
```

---

## 3. Reproducing the verification

### The save/content parity harness

`analysis/verify-save.ps1` runs in three stages, and **exits non-zero on any failure**:

1. **Generate golden fixtures with the real Java classes.** `fixtures/java/*.java` are compiled with
   JDK 8 against the original `JaSBro-reference.jar` + `lib/`, and executed. This includes the
   content fixture, which must run with its **working directory set to the game folder** because
   `CharacterFileLoader` resolves `characters/` relative to the CWD.
2. **Run the C# verifier** (`port/Simbro.Verify`) against those fixtures.
3. **Feed C#-written saves back to the real Java game** and assert the Java side reconstructs them.

Stage 3 is the important one: it proves compatibility in the direction that actually matters —
that the *shipped game* accepts what the port produces.

> **Requires the reference JAR and the game folder**, neither of which is in this repository.

### The port

```powershell
cd port
dotnet build
dotnet run --project Simbro.Verify
```

`port/nuget.config` clears all package sources. `Simbro.Core` has **no external dependencies**, so it
builds offline — which is what makes it buildable at all on a machine with broken TLS.

### The Godot layer

```powershell
cd godot
& "<godot console exe>" --headless --quit
```

`godot/nuget.config` clears all sources and adds **Godot's bundled feed** at
`tools/godot/.../GodotSharp/Tools/nupkgs`. That local feed is what lets a Godot C# project restore
offline. Keep the version in `Simbro.Godot.csproj` and the path in `nuget.config` in step.

---

## 4. Rebuilding the decompilation

`analysis/DECOMPILATION.md` records the full procedure and every hand-fix. In short:

- **CFR 0.152** (JDK 8) and **Vineflower 1.12.0** (JDK 21) were both run.
- The merged result is `game-source/` — **Vineflower as the base, with CFR substituted for 7 classes**
  where Vineflower was wrong.
- `decompiler-output/cfr/` and `decompiler-output/vineflower/` are both included so you can compare
  them. **That comparison is how you tell a decompiler artifact from an author bug** — a construct
  present in only one output is suspect; one present in both is almost certainly real.
- Compiling the result required three fixes, all documented: a CFR `1.$SwitchMap` artifact, 26
  "variable already defined" cases needing per-case braces, and a split `final` local.

> Always pass `-encoding UTF-8` to `javac`. It defaults to the platform codepage (MS932 on this
> machine) and an em-dash in a comment will fail the build with a confusing error.
