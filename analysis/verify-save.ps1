<#
.SYNOPSIS
  Cross-language save-compatibility test: C# writes a save, the REAL Java game reads it.

.DESCRIPTION
  This is the strongest evidence that the port preserves save compatibility. It runs in three steps:

    1. Generate golden fixtures using the real decompiled game classes (Java -> XML),
       so the port's READER is tested against authentic output.
    2. Run the C# verifier, which asserts against those fixtures and WRITES a save of its own.
    3. Feed the C#-written save to the real Java game and confirm it loads with the right values.

  Step 3 is the one that matters most: it proves compatibility in the direction that would
  otherwise silently break — the original game reading port-written data.

.EXAMPLE
  pwsh -File verify-save.ps1
#>
[CmdletBinding()]
param(
    [switch]$SkipFixtureRegeneration
)

$ErrorActionPreference = 'Stop'
$wf  = $PSScriptRoot
$jdk = Join-Path $wf 'tools\jdk8\bin'
$jar = Join-Path $wf 'decompiled\JaSBro-from-decompiled.jar'
$out = Join-Path $wf 'fixtures\out'
$javaSrc = Join-Path $wf 'fixtures\java'

if (-not (Test-Path $jar)) { throw "missing $jar" }
if (-not (Test-Path (Join-Path $jdk 'javac.exe'))) { throw "missing JDK8 at $jdk" }

$libs = (Get-ChildItem (Join-Path $wf 'original\lib\*.jar') | ForEach-Object { $_.FullName }) -join ';'
$cp = "$jar;$libs"

# This machine's javac defaults to MS932, which chokes on non-ASCII in comments.
$javacArgs = @('-encoding', 'UTF-8')

New-Item -ItemType Directory -Force -Path $out | Out-Null

function Step($n, $msg) { Write-Host ''; Write-Host "=== [$n] $msg ===" -ForegroundColor Cyan }

# ---------------------------------------------------------------- 1. fixtures
if (-not $SkipFixtureRegeneration) {
    Step 1 'Generating golden fixtures with the REAL Java game classes'
    & (Join-Path $jdk 'javac.exe') @javacArgs -cp $cp -d $out (Join-Path $javaSrc 'SaveFixture.java')
    if ($LASTEXITCODE -ne 0) { throw 'SaveFixture failed to compile' }

    & (Join-Path $jdk 'java.exe') -cp "$out;$cp" SaveFixture `
        (Join-Path $wf 'fixtures\save-golden.xml') `
        (Join-Path $wf 'fixtures\save-golden-character.xml') `
        (Join-Path $wf 'fixtures\save-golden-reference.xml') | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'SaveFixture failed to run' }
    Write-Host '  wrote save-golden.xml, save-golden-character.xml, save-golden-reference.xml'

    # --- age-progression weak reference ------------------------------------
    # Emits the ONLY fixture exercising an UNINDEXED reference whose final path segment is a bare
    # type name - the form a real save uses whenever a character carries ageProgressionData.
    & (Join-Path $jdk 'javac.exe') @javacArgs -cp $cp -d $out (Join-Path $javaSrc 'AgeProgressionFixture.java')
    if ($LASTEXITCODE -ne 0) { throw 'AgeProgressionFixture failed to compile' }

    & (Join-Path $jdk 'java.exe') -cp "$out;$cp" AgeProgressionFixture `
        (Join-Path $wf 'fixtures\save-golden-ageref.xml') 2>$null | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'AgeProgressionFixture failed to run' }
    Write-Host "  wrote save-golden-ageref.xml ($((Get-Item (Join-Path $wf 'fixtures\save-golden-ageref.xml')).Length) bytes)"

    # --- content fixture ---------------------------------------------------
    # This one must run with the working directory set to the GAME FOLDER, because the
    # legacy loader opens a CWD-relative path: new TFile("characters").
    $gameDir = 'C:\Games\Jasbro_Final'
    if (-not (Test-Path (Join-Path $gameDir 'characters'))) {
        Write-Host "  SKIP  content fixture: no characters folder at $gameDir" -ForegroundColor Yellow
    } else {
        & (Join-Path $jdk 'javac.exe') @javacArgs -cp $cp -d $out (Join-Path $javaSrc 'ContentFixture.java')
        if ($LASTEXITCODE -ne 0) { throw 'ContentFixture failed to compile' }

        # The game's own log4j2.xml writes "jasbro.log" into the working directory, which is
        # denied here (and would fail on a read-only install). Redirect logging to the console.
        $logCfg = Join-Path $wf 'fixtures\log4j2-harness.xml'
        Push-Location $gameDir
        try {
            & (Join-Path $jdk 'java.exe') "-Dlog4j.configurationFile=$logCfg" `
                -cp "$out;$cp" ContentFixture (Join-Path $wf 'fixtures\content-golden.txt') 2>$null | Out-Null
        } finally {
            Pop-Location
        }
        if ($LASTEXITCODE -ne 0) { throw 'ContentFixture failed to run' }

        $cf = Join-Path $wf 'fixtures\content-golden.txt'
        $count = (Select-String -Path $cf -Pattern '^COUNT=' | Select-Object -First 1).Line
        Write-Host "  wrote content-golden.txt ($count characters, $((Get-Item $cf).Length) bytes)"
    }
    # --- rooms fixtures ----------------------------------------------------
    # These must run with the working directory set to the folder holding rooms.xml, because
    # RoomLoader opens it with a CWD-relative `new FileInputStream("rooms.xml")`.
    #
    # Two fixtures, two different claims:
    #   RoomsFixture          - the port READS rooms.xml the same way (structure, field for field).
    #   RoomsSemanticsFixture - the port EVALUATES rooms.xml the same way (63 character groups x
    #                           every room/activity pair = 11,529 validity decisions).
    $roomDir = Join-Path $wf 'original'
    if (-not (Test-Path (Join-Path $roomDir 'rooms.xml'))) {
        Write-Host "  SKIP  rooms fixtures: no rooms.xml at $roomDir" -ForegroundColor Yellow
    } else {
        foreach ($fx in @(
            @{ Src = 'RoomsFixture.java';          Class = 'RoomsFixture'
               Out = 'rooms-golden.txt';           What = 'rooms' }
            @{ Src = 'RoomsSemanticsFixture.java'; Class = 'RoomsSemanticsFixture'
               Out = 'rooms-semantics-golden.txt'; What = 'room validity decisions' }
        )) {
            & (Join-Path $jdk 'javac.exe') @javacArgs -cp $cp -d $out (Join-Path $javaSrc $fx.Src)
            if ($LASTEXITCODE -ne 0) { throw "$($fx.Class) failed to compile" }

            Push-Location $roomDir
            try {
                & (Join-Path $jdk 'java.exe') -cp "$out;$cp" $fx.Class `
                    (Join-Path $wf "fixtures\$($fx.Out)") 2>$null | Out-Null
            } finally {
                Pop-Location
            }
            if ($LASTEXITCODE -ne 0) { throw "$($fx.Class) failed to run" }

            $f = Join-Path $wf "fixtures\$($fx.Out)"
            Write-Host "  wrote $($fx.Out) ($($fx.What), $((Get-Item $f).Length) bytes)"
        }
    }
} else {
    Step 1 'Skipping fixture regeneration (-SkipFixtureRegeneration)'
}

# ---------------------------------------------------------------- 2. C# verifier
Step 2 'Running the C# verifier (reads fixtures, writes its own save)'
Push-Location $wf
try {
    & dotnet run --project (Join-Path $wf 'port\Simbro.Verify\Simbro.Verify.csproj') -v q --nologo
    $verifyExit = $LASTEXITCODE
} finally {
    Pop-Location
}

$written = Join-Path $out 'csharp-written.xml'
if (-not (Test-Path $written)) { throw "the port did not write $written" }
if ($verifyExit -ne 0) { throw "C# verifier reported failures (exit $verifyExit)" }

# ---------------------------------------------------------------- 3. Java reads C# output
Step 3 'REAL JAVA GAME loading the C#-written saves'
& (Join-Path $jdk 'javac.exe') @javacArgs -cp $cp -d $out (Join-Path $javaSrc 'SaveLoader.java')
if ($LASTEXITCODE -ne 0) { throw 'SaveLoader failed to compile' }

$noise = 'Unable to locate appender|SLF4J|StaticLoggerBinder|See http'

$loaded = & (Join-Path $jdk 'java.exe') -cp "$out;$cp" SaveLoader (Join-Path $out 'csharp-written.xml') 2>&1
$loaded | Where-Object { $_ -notmatch $noise } | ForEach-Object { Write-Host "  $_" }

$loadedChar = & (Join-Path $jdk 'java.exe') -cp "$out;$cp" SaveLoader (Join-Path $out 'csharp-written-character.xml') 2>&1
$loadedChar | Where-Object { $_ -notmatch $noise } | ForEach-Object { Write-Host "  $_" }

# ---------------------------------------------------------------- verdict
Step 'RESULT' 'Cross-language save compatibility'

$checks = @(
    @{ Text = ($loaded -join "`n");     Need = 'day=99';                        Label = 'scalar: day' }
    @{ Text = ($loaded -join "`n");     Need = 'time=NIGHT';                    Label = 'scalar: time' }
    @{ Text = ($loaded -join "`n");     Need = 'money=497';                     Label = 'scalar: money' }
    @{ Text = ($loaded -join "`n");     Need = 'inventory=jasbro.game.items.Inventory'; Label = 'component: inventory' }
    @{ Text = ($loaded -join "`n");     Need = 'eventManager=jasbro.game.events.EventManager'; Label = 'component: eventManager' }
    @{ Text = ($loaded -join "`n");     Need = 'defaultPreferences=jasbro.game.DefaultPreferences'; Label = 'component: defaultPreferences' }
    @{ Text = ($loaded -join "`n");     Need = 'unlocks=jasbro.game.world.Unlocks'; Label = 'component: unlocks' }
    @{ Text = ($loadedChar -join "`n"); Need = 'CHAR[0].name=Loli';             Label = 'character: name' }
    @{ Text = ($loadedChar -join "`n"); Need = 'CHAR[0].type=CHILD';            Label = 'character: type' }
    @{ Text = ($loadedChar -join "`n"); Need = 'CHAR[0].gender=FEMALE';         Label = 'character: gender' }
    @{ Text = ($loadedChar -join "`n"); Need = 'CHAR[0].baseId=Loli';           Label = 'character: baseId (content link)' }
    @{ Text = ($loadedChar -join "`n"); Need = 'CHAR[0].traits=[LOLI, FRAGILE, LOYAL]'; Label = 'character: traits' }
)

$ok = $true
foreach ($c in $checks) {
    # NOTE: use .Contains(), NOT -like. PowerShell's -like treats '[' and ']' as wildcard
    # character classes, so a pattern like 'CHAR[0].name=Loli' silently matches nothing.
    if ($c.Text.Contains($c.Need)) {
        Write-Host "  PASS  Java reconstructed $($c.Label)" -ForegroundColor Green
    } else {
        Write-Host "  FAIL  Java did not reconstruct $($c.Label)  (expected '$($c.Need)')" -ForegroundColor Red
        $ok = $false
    }
}

Write-Host ''
if ($ok) {
    Write-Host 'COMPATIBILITY VERIFIED: the shipped Java game loads saves written by the C# port,' -ForegroundColor Green
    Write-Host 'including a full character with its traits and content link.' -ForegroundColor Green
    exit 0
} else {
    Write-Host 'COMPATIBILITY FAILED.' -ForegroundColor Red
    exit 1
}
