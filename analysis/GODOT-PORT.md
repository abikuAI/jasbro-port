# The Godot presentation layer

**Status: verified working.** Godot 4.7.2 Mono builds the project and reaches the `Simbro.Core`
domain assembly at runtime, including parsing the real shipped content.

---

## What was verified, and how

```
Godot Engine v4.7.2.stable.mono.official.ed1daf0bf

Simbro.Core reached from Godot.
  day=1  time=MORNING  money=380          <- Godot drove the domain: 500 - 120
  save bytes=1915
  XStream declaration: True
  never self-closes:   True
  round-tripped day:   1
  round-tripped money: 380
  character name:      Loli
  character baseId:    Loli
  traits:              LOLI, FRAGILE, LOYAL
  parsed real Loli:    5 images, 3 traits  <- read the REAL content folder through the port's parser
```

Reproduce with:

```powershell
cd godot
$env:NUGET_PACKAGES = "<work>\.nuget-packages"   # required in this sandbox, see below
dotnet build

& "<godot>/Godot_v4.7.2-stable_mono_win64_console.exe" --headless --path . --quit-after 3
```

This exercises the full chain **Godot → Simbro.Core → XStream-compatible save → back**, plus content
parsing against `C:\Games\Jasbro_Final\characters\Loli\properties.xml`.

The only runtime errors are environmental and non-fatal: the sandbox denies writing to
`%LOCALAPPDATA%` (Godot's `user://` log), and the machine's certificate store is unreadable
(the same broken schannel that breaks `Invoke-WebRequest`). Exit code is 0.

---

## Architecture: why the domain is engine-agnostic

`Simbro.Core` contains **no reference to Godot whatsoever** — not a `using Godot`, not a `Node`.
Godot's only knowledge of the domain is `Simbro.Godot.csproj` referencing `Simbro.Core.csproj`.

This is deliberate, and it buys three things:

1. **The simulation is testable without an engine.** `dotnet run --project Simbro.Verify` checks
   128 assertions with no window, no boot, no flakiness.
2. **The engine choice is reversible.** Swapping Godot for another toolkit costs the presentation
   layer only, never the simulation. This is what makes the [engine decision](ENGINE-CHOICE.md)
   low-risk despite genuine reservations about Godot's data-UI story.
3. **The port can progress without Godot installed at all.** Only the thin adapter needs it.

`Simbro.Godot` is therefore kept as small as possible by design. If logic starts accumulating there
because it is "easier in the scene", that is the signal the design is being violated.

---

## Build setup, and three traps specific to this machine

### The Godot SDK comes from inside the editor download

Godot ships its own NuGet packages at
`tools/godot/Godot_v4.7.2-stable_mono_win64/GodotSharp/Tools/nupkgs` —
`Godot.NET.Sdk`, `Godot.SourceGenerators`, `GodotSharp`, `GodotSharpEditor`.

`godot/nuget.config` clears every inherited source and adds that folder as a local feed. Without it,
restore tries to reach nuget.org and fails slowly. **If you bump the Godot version, the version in
`Simbro.Godot.csproj` and the path in `nuget.config` must both change together.**

### 🪤 NuGet cannot write to its global packages folder

The default global packages folder is `%USERPROFILE%\.nuget\packages`, which is **outside the
workspace** and therefore read-only to the sandbox. Restore fails with a deep stack trace ending in:

```
error : at System.IO.FileSystem.CreateDirectory(String fullPath, Byte[] securityDescriptor)
error MSB4236: The SDK 'Godot.NET.Sdk/4.7.2' specified could not be found.
```

The misleading part is the final line — it looks like a missing SDK, but the SDK *is* in the local
feed; NuGet simply could not extract it. Fix by redirecting the packages folder inside the workspace:

```powershell
$env:NUGET_PACKAGES = "<work>\.nuget-packages"
```

This is a sandbox artifact, not a project defect. Outside the sandbox no redirect is needed.

### 🪤 Godot's SDK does not enable implicit usings

`Godot.NET.Sdk` leaves `ImplicitUsings` off, so `System` and `System.Linq` must be imported
explicitly. Without them a file that looks perfectly ordinary fails with:

```
error CS1061: 'List<XStreamNode>' does not contain a definition for 'FirstOrDefault'
error CS0103: The name 'Array' does not exist in the current context
```

Both are just missing usings.

### 🪤 Godot cannot write its own editor cache here

```
ERROR: Could not create editor cache directory: C:/Users/Computer/AppData/Local/Godot
```

Harmless for headless runs — Godot continues and exits 0. It only matters if you need editor
settings to persist.

---

## What is not built yet

The presentation layer is a **scaffold with a proof-of-integration scene**, not a game UI.

- Only `src/Main.cs` exists, and it prints a diagnostic report rather than drawing anything.
- No character list, no activity screens, no map, no save/load UI.
- Settings and theming are untouched.

On the domain side, the two halves of the content model are in very different states:

| Content system | State |
|---|---|
| **`rooms.xml`** (rooms, slots, activity requirements) | **Ported and verified** — all 29 rooms match the shipped loader field-for-field |
| **`events/` + `quests/`** (XStream, 166 BeanShell blocks) | **Not started** — the largest remaining domain item |

The next real step is therefore System 1: world events, custom quests, requirements, effects, and the
BeanShell question. See [`CONTENT-MODEL.md`](CONTENT-MODEL.md) for the two-system split and
[`BSH-MIGRATION.md`](BSH-MIGRATION.md) for the script decision.

> **Worth restating before investing in UI:** Godot's Control nodes have no real data grid, and this
> game is fundamentally dense tables with pictures. The plan is to build `Simbro.Core` first and
> then spike one screen (the character list with images and tooltips) so the pain, if any, shows up
> while it is still cheap to change engines.
