<#
.SYNOPSIS
  Stage the JaSBro review/collaboration repository.

.DESCRIPTION
  Includes everything a collaborator needs: the decompiled game, every analysis document, the port,
  all fixtures and tooling, the reference JAR and its dependencies (so the original build actually
  works), the decompiler intermediates (so the reconstruction is traceable), the public fork clone
  (for provenance), the rebuilt jars, and the new Godot project.

  EXCLUDED, and why - two groups, both for hard reasons rather than preference:

  1. TOOLCHAINS (~890 MB). GitHub rejects any file over 100 MB, and five do:
       tools/jdk21.zip                     195.6 MB
       tools/godot/.../Godot_..._win64.exe 173.0 MB
       tools/jdk21/lib/modules             134.4 MB
       tools/godot-download/Godot_...zip   111.2 MB
       tools/jdk8.zip                      101.5 MB
     They are also unmodified third-party downloads. BUILDING.md records exact versions and URLs,
     and tools/get-jdk.py / tools/get-godot.py fetch and hash-verify them.

  2. ARTWORK (~471 MB). characters/ (413.7 MB) and images/ (57.8 MB) are the original author's
     artwork, not part of this project's work. Item icons inside original/ are excluded too.
     BUILDING.md explains how to obtain the game folder.

  Everything else - including binaries - is included.

.EXAMPLE
  pwsh -File stage-review-repo.ps1 -Destination C:\path\to\jasbro
#>
[CmdletBinding()]
param(
    [string]$Work = (Split-Path -Parent $PSScriptRoot),
    [Parameter(Mandatory)][string]$Destination,
    [string]$GameFolder = 'C:\Games\Jasbro_Final'
)

$ErrorActionPreference = 'Stop'

if (Test-Path $Destination) {
    # PRESERVE .git. Clearing it destroys the repository on every re-stage, which is exactly what
    # happened the first time this was run. Only the tracked working tree is rebuilt.
    Write-Host "clearing $Destination (keeping .git)" -ForegroundColor Yellow
    Get-ChildItem $Destination -Force | Where-Object { $_.Name -ne '.git' } |
        Remove-Item -Recurse -Force
} else {
    New-Item -ItemType Directory -Force -Path $Destination | Out-Null
}

# Only raster artwork is excluded. Archives, jars and class files ARE included here: a collaborator
# needs them to build and to check the reconstruction.
$Script:AssetExtensions = @(
    '.png','.jpg','.jpeg','.gif','.bmp','.webp','.tga','.ico',
    '.wav','.ogg','.mp3','.mp4','.ttf','.otf','.woff','.woff2'
)

# Regenerable scratch that would add ~150 MB without telling anyone anything new.
# '.git' matters: vendoring the fork's own history would add 72 MB, and a nested repository would
# confuse the outer one. The fork's files are included; its history is not.
$Script:SkipPaths = @(
    '.git',
    '.godot',
    'bin',
    'obj',
    'build-out\run',
    'build-out\run-dec',
    'build-out\stage',
    'decompiled\classes-check',
    'decompiled\procyon',
    'decompiled\classes-vf',
    'tools\jdk8', 'tools\jdk21', 'tools\godot', 'tools\godot-download', 'tools\procyon',
    'characters', 'images'
)

function Copy-Tree($from, $to, [string[]]$ExtraExclude = @()) {
    if (-not (Test-Path $from)) { Write-Host "  skip (absent): $from" -ForegroundColor DarkGray; return 0 }
    $count = 0
    Get-ChildItem $from -Recurse -File -Force | ForEach-Object {
        if ($Script:AssetExtensions -contains $_.Extension.ToLowerInvariant()) { return }
        $full = $_.FullName
        foreach ($skip in ($Script:SkipPaths + $ExtraExclude)) {
            if ($full -like "*\$skip\*") { return }
        }
        # GitHub's hard per-file limit.
        if ($_.Length -gt 100MB) { Write-Host "  OVER LIMIT, skipped: $($_.Name) ($([math]::Round($_.Length/1MB,1)) MB)" -ForegroundColor Red; return }
        $rel = $full.Substring($from.Length).TrimStart('\')
        $target = Join-Path $to $rel
        New-Item -ItemType Directory -Force -Path (Split-Path $target -Parent) | Out-Null
        Copy-Item $full $target -Force
        $count++
    }
    return $count
}

function Stage($label, $from, $to) {
    $n = Copy-Tree $from $to
    Write-Host ("  {0,-18} {1,5} files" -f $label, $n) -ForegroundColor Gray
}

Write-Host "staging into $Destination" -ForegroundColor Cyan

# ---------------------------------------------------------------- root documents
# These live in the work folder rather than the destination, because this script CLEARS the
# destination on every run and would otherwise delete its own README.
foreach ($doc in @(
    @{ Src = 'REVIEW-README.md';    Dst = 'README.md' },
    @{ Src = 'REVIEW-BUILDING.md';  Dst = 'BUILDING.md' },
    @{ Src = 'REVIEW-gitignore.txt'; Dst = '.gitignore' }
)) {
    $s = Join-Path $Work $doc.Src
    if (Test-Path $s) {
        Copy-Item $s (Join-Path $Destination $doc.Dst) -Force
    } else {
        Write-Host "  MISSING root doc: $($doc.Src)" -ForegroundColor Red
    }
}
Write-Host ("  {0,-22} {1,5} files" -f 'root docs', 3) -ForegroundColor Gray

# ---------------------------------------------------------------- the decompiled game  (THE code)
# source/ is the authoritative merged result (Vineflower base + CFR for 7 classes).
# cfr/ and vineflower/ are kept alongside it deliberately: comparing the two is HOW you tell a
# decompiler artifact from an author bug, which is a core review activity. merged/ and classes-*/
# are staging and compiled duplicates and are not included.
Copy-Tree "$Work\decompiled\source"      "$Destination\game-source" | Out-Null
Write-Host ("  {0,-22} {1,5} files  (the decompiled R0.1.2 build)" -f 'game-source/', (Get-ChildItem "$Destination\game-source" -Recurse -File).Count) -ForegroundColor Gray
Copy-Tree "$Work\decompiled\cfr"         "$Destination\decompiler-output\cfr" | Out-Null
Copy-Tree "$Work\decompiled\vineflower"  "$Destination\decompiler-output\vineflower" | Out-Null
Write-Host ("  {0,-22} {1,5} files  (for artifact triage)" -f 'decompiler-output/', (Get-ChildItem "$Destination\decompiler-output" -Recurse -File).Count) -ForegroundColor Gray
Copy-Item "$Work\decompiled\JaSBro-from-decompiled.jar" "$Destination\game-source.jar" -Force

# ---------------------------------------------------------------- our analysis
New-Item -ItemType Directory -Force -Path "$Destination\analysis" | Out-Null
Get-ChildItem "$Work\*.md" -File | Where-Object { $_.Name -notlike 'REVIEW-*' } |
    ForEach-Object { Copy-Item $_.FullName "$Destination\analysis\" -Force }
Copy-Item "$Work\audit" "$Destination\analysis\audit" -Recurse -Force
Copy-Item "$Work\*.ps1" "$Destination\analysis\" -Force
Write-Host ("  {0,-22} {1,5} files  (findings, save format, policy, engine choice)" -f 'analysis/', (Get-ChildItem "$Destination\analysis" -Recurse -File).Count) -ForegroundColor Gray

# ---------------------------------------------------------------- the port + the Godot layer
Stage 'port/'         "$Work\port"       "$Destination\port"
Stage 'godot/'        "$Work\godot"      "$Destination\godot" -ExtraExclude @('.godot','bin','obj')

# ---------------------------------------------------------------- fixtures, tooling, data
Stage 'fixtures/'     "$Work\fixtures"   "$Destination\fixtures"
New-Item -ItemType Directory -Force -Path "$Destination\tooling" | Out-Null
Get-ChildItem "$Work\tools" -File | Where-Object { $_.Extension -in @('.py','.ps1') } |
    ForEach-Object { Copy-Item $_.FullName "$Destination\tooling\" -Force }
Write-Host ("  {0,-22} {1,5} files  (analysis scripts)" -f 'tooling/', (Get-ChildItem "$Destination\tooling" -File).Count) -ForegroundColor Gray
New-Item -ItemType Directory -Force -Path "$Destination\data" | Out-Null
Get-ChildItem "$Work\build-out\logs\*.json","$Work\build-out\logs\divergence.txt","$Work\build-out\logs\repo-vs-shipped.txt" -File -EA SilentlyContinue |
    ForEach-Object { Copy-Item $_.FullName "$Destination\data\" -Force }
Write-Host ("  {0,-22} {1,5} files  (machine-readable analysis)" -f 'data/', (Get-ChildItem "$Destination\data" -File).Count) -ForegroundColor Gray

# ---------------------------------------------------------------- the game's content (text only)
foreach ($sub in @('items','npcs','events','quests')) {
    Stage "content/$sub" "$Work\original\$sub" "$Destination\content\$sub"
}
foreach ($f in @('rooms.xml','config.ini','fameUnlocks.xml')) {
    if (Test-Path "$Work\original\$f") { Copy-Item "$Work\original\$f" "$Destination\content\" -Force }
}

Write-Output ''
Write-Output '=== staging complete ==='
Write-Output ''
Get-ChildItem $Destination -Force | ForEach-Object {
    if ($_.PSIsContainer) {
        $s = (Get-ChildItem $_.FullName -Recurse -File -Force -EA SilentlyContinue | Measure-Object Length -Sum)
        Write-Output ("  {0,-16} {1,9:N2} MB  {2,6} files" -f $_.Name, ($s.Sum/1MB), $s.Count)
    } else {
        Write-Output ("  {0,-16} {1,9:N2} MB" -f $_.Name, ($_.Length/1MB))
    }
}
$total = Get-ChildItem $Destination -Recurse -File -Force | Measure-Object -Property Length -Sum
Write-Output ''
Write-Output ("TOTAL: {0} files, {1:N1} MB" -f $total.Count, ($total.Sum/1MB))
$over = Get-ChildItem $Destination -Recurse -File -Force | Where-Object { $_.Length -gt 100MB }
if ($over) { Write-Host "WARNING: $($over.Count) file(s) still exceed the 100 MB limit" -ForegroundColor Red }
else { Write-Host "OK: no file exceeds GitHub's 100 MB limit" -ForegroundColor Green }
