# JaSBro — Startup / Load Performance Diagnosis

**Status: DIAGNOSIS ONLY.** No code was changed. Per your instruction, this documents the cause
and the evidence; fixes are a separate decision.

Traced in `decompiled/source/`, cross-checked with `javap` against `original/JaSBro-reference.jar`,
and measured against the live data in `C:\Games\Jasbro_Final`.

---

## Headline — this partly contradicts the premise

**The main-menu startup path never scans `characters/` or `images/`, and character loading runs in
`optimized=true` mode, which does not read image files from disk at all.**

Bytecode-verified in the shipped jar: `Jasbro.loadBases()` pushes `iconst_1` before calling
`CharacterFileLoader.loadAllCharacters(boolean)`.

So the work that scales with your content **does not happen at launch**. It happens at the first
`getCharacterBases()` (opening the New Game screen, or loading a save) and `getItems()`
(starting a new game). The main menu itself does a constant amount of work.

**This matters for how you read your own symptom.** If the long pause is *before* the main menu
appears, nothing on that path scales with file count — the cause would be outside these loaders
(JVM start, classpath, antivirus scanning, disk). If the pause is when you **start or load** a
game, everything below applies.

---

## Measured data

| | |
|---|---|
| `characters/` | 2,142 files · 413.7 MB · 17 dirs |
| `properties.xml` files in `characters/` | **11** |
| `<image>` nodes across those | 506 |
| `<tag>` nodes across those | 636 |
| Folders with **no** `properties.xml` | 6 — holding **1,627 loose images** (Lusamine 410, Rinko Iori 791, Nemu Kurokuchi 138, Nefertari Vivi 109, Retsu Unohana 97, Villeta Nu 82) |
| `images/` | 1,918 files · 57.8 MB |
| Item / event / quest / NPC XML | 155 / 51 / 11 / 5 = **222** files |

**Key number: for N images on disk, startup decodes 0 of them.** Only 11 of your characters have a
`properties.xml`; the other 1,627 images are never read by the game at all — just *listed*.

---

## Findings, ranked by impact

### F1 — `CharacterFileLoader.addCharacters`: recursive walk of `characters/`, depth 5 — HIGH
`jasbro/game/character/CharacterFileLoader.java:63-105`

```java
public synchronized List<CharacterBase> loadAllCharacters(boolean optimized) {
   TFile characterFolder = new TFile("characters");
   this.addCharacters(characterFolder, characters, 5, optimized);   // 66
```
```java
for (TFile file : characterFolder.listFiles()) {                    // 72
   if ((file.isFile() && file.getName().endsWith(".zip") || file.isDirectory()) ...
      String[] files = file.list();                                 // 76
      if (files != null) {
         if (Arrays.asList(files).contains("properties.xml")) { ...  // 78
         } else if (depth > 0) {
            this.addCharacters(file, characters, depth - 1, optimized);  // 99
```

One `listFiles()`/`list()` per directory plus per-entry stat calls, and **folders without a
`properties.xml` are still recursed into and fully enumerated** — that's the 1,627 loose images.
It only lists; it never decodes. Runs on the Swing EDT, so the UI freezes for the whole walk.

### F2 — `properties.xml` parsing; cost grows with image/tag entries — HIGH
`CharacterFileLoader.java:202-272` and `274-347`

One `ImageData` allocated per `<image>` node, plus a `Collections.sort` over images (O(n log n)
string compares). `load()` re-scans the DOM ~15 times via `getElementsByTagName` per attribute enum.

**This is the one legitimate "more images = slower" path**: if you add images *listed in a character's
`properties.xml`*, loading that character costs more. It is proportional to XML entries, not to
image bytes.

### F3 — `NpcFileLoader.scanForImages`: real recursive image walk + O(n²) — HIGH
`jasbro/game/world/customContent/npc/NpcFileLoader.java:155-179`

For each NPC, every image file in the folder and 2 levels of subfolders is enumerated, then
`if (!npc.getImages().contains(imageData))` does a **linear scan per image → O(n²)**. Triggered at
first enemy spawn, not at startup.

### F4 — `ImageUtil.isImage()` builds a `MimetypesFileTypeMap` **per file** — HIGH (allocation)
`jasbro/gui/pictures/ImageUtil.java:357-362`

```java
public boolean isImage(TFile file) {
   MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
   typeMap.addMimeTypes("image png tif jpg jpeg bmp gif");
   String mimeType = typeMap.getContentType(file);
```

Bytecode-confirmed in the shipped jar. Constructing this loads JAF mime-type configuration — doing
it once per file is the classic hot-loop defect. Only bites NPC scans and the editor tools today
(character loading uses the `optimized` path, which skips it).

### F5 — A fresh `XStream` + annotation autodetection **per XML file** — HIGH
`ItemFileLoader.java:61-72`, `EventAndQuestFileLoader.java:71-84,179-192`, `NpcFileLoader.java:76-89`

```java
XStream xstream = new XStream(new StaxDriver());
xstream.autodetectAnnotations(true);
String xml = "";
do { line = bufferedReader.readLine(); if (line != null) { xml = xml + line + "\n"; } } while (line != null);
```

**222 fresh XStream instances per full load.** The string concatenation is also quadratic in file
length. Scales with XML *file count*.

### F6 — No on-disk index; every load is re-done each launch — HIGH
`Jasbro.java:303-311, 414-438, 516-562` — lazy `synchronized` in-memory caches only.

A tree-wide grep found **no cache file, no serialized index, no preload**. Everything is re-walked
and re-parsed on every launch. All these methods are `synchronized` and called from the EDT, so
loading is serial and blocks painting.

### F7 — Decoded-image cache is tiny and expires — HIGH
`ImageUtil.java:52-60`

```java
long memoryMB = Runtime.getRuntime().maxMemory() / 1048576L;
long cacheSize = memoryMB / 15L;
this.images = CacheBuilder.newBuilder().maximumSize(cacheSize).expireAfterAccess(1L, TimeUnit.MINUTES).build();
```

Your log's first line — `Max-memory: 247 Cache size: 16` — confirms **16 decoded images cached,
4 resized**, expiring after 60 s. Browsing re-decodes constantly. Separately, loading a GIF calls
`this.clearCache()` (line 109), wiping the entire image cache.

### F8 — "Async" image selection is immediately joined — HIGH
`ImageUtil.java:305-311` submits a task, but `ImageData` getters call `future.get()` on first access
(`ImageData.java:41-50,56-65,100-109`). So the first `getTags()/getKey()/getFilename()` blocks the
caller on the pool task — **no latency is actually hidden**. Each image pick is also O(images × tags).

### F9 — Not scaling problems — HIGH
`RoomLoader.loadRooms` reads exactly one `rooms.xml` (43 KB); `ConfigHandler` reads one `config.ini`
(405 bytes); `Settings` is just an enum. None scale with content size.

---

## Threading

- The only executor is a cached thread pool (`Jasbro.java:69`), used for image resize and tag matching.
- **All file loading is single-threaded and runs on the Swing EDT**, with the loaders additionally
  `synchronized`. Nothing loads characters/items/events/quests in parallel.
- **No `Thread.sleep` on the startup path.** The sleeps at `Jasbro.java:212,245` are turn processing;
  `CharacterFileLoader.java:455` is a save-retry.

---

## Your log's timing gaps — weak evidence, flagged as such

`jasbro.log` shows `ImageUtil.<init>` (main-menu paint) → `RoomLoader.loadRooms` (fires inside
`startNewGame`) at 16 s, 5 s, 4 s, and 10 s across sessions. **But that window includes the human
clicking through the UI**, so I am not treating these as load-time measurements. They'd need a
clean instrumented run to mean anything.

---

## Bottom line

"More images = slower" is real, but only through:

1. **F2** — growth of `characters/*/properties.xml`
2. **F1** — more folders/entries being listed, including recursing into folders with no `properties.xml`
3. **F3** — NPC image scans (at first enemy spawn)
4. **F7/F8** — decode churn and O(n) image selection *during play*

It is **not** caused by bulk-decoding `images/` at startup — that folder is never walked by the game,
only by `CharacterEditor`/`EventEditor`.

**Confidence:** allocation counts (per-file `MimetypesFileTypeMap`, per-file `XStream`, 16-entry
image cache, `optimized=true`) are read directly from code, bytecode, and your log — high.
Absolute wall-clock estimates for F4/F5 are inferred from the code pattern, not measured — medium.
