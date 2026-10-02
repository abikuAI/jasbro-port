<#
    build.ps1 - Build JaSBro from the GitHub fork source WITHOUT Gradle.

    Why no Gradle: build.gradle uses Gradle-2.x syntax (compile/testCompile/archiveName/
    uploadArchives) that Gradle 7+ rejects, and no wrapper is committed. Since all 34
    dependency jars already ship in the game's lib/ folder, we can bypass Gradle entirely
    and drive javac directly. This is faster and has no moving parts.

    Usage:
        pwsh -File build.ps1              # compile + package
        pwsh -File build.ps1 -Clean       # wipe build-out first
        pwsh -File build.ps1 -ApplyFix    # also write the Buff.AlreadyFull fix into the repo

    The fix is applied to the working copy in repo\. It is NOT committed.
#>
[CmdletBinding()]
param(
    [switch]$Clean,
    [switch]$ApplyFix
)

$ErrorActionPreference = 'Stop'
$root    = $PSScriptRoot
$repo    = Join-Path $root 'repo'
$srcRoot = Join-Path $repo 'src\main\java'
$resRoot = Join-Path $repo 'src\main\resources'
$libDir  = Join-Path $root 'original\lib'
$out     = Join-Path $root 'build-out'
$classes = Join-Path $out 'classes'
$logs    = Join-Path $out 'logs'
$javac   = Join-Path $root 'tools\jdk8\bin\javac.exe'
$jarExe  = Join-Path $root 'tools\jdk8\bin\jar.exe'
$py      = 'C:\Users\Computer\.dsh\dsh-runtimes\dsh-primary-runtime\dependencies\python\python.exe'
$buffJava = Join-Path $srcRoot 'jasbro\game\character\conditions\Buff.java'

function Say($m) { Write-Host "[build] $m" }

# --- preflight -------------------------------------------------------------
foreach ($p in @($javac, $srcRoot, $libDir)) {
    if (-not (Test-Path $p)) { throw "Missing required path: $p" }
}

if ($Clean) {
    Say "cleaning $out"
    Remove-Item $out -Recurse -Force -ErrorAction SilentlyContinue
}
New-Item -ItemType Directory -Force -Path $classes, $logs | Out-Null

# --- optional: apply the single-line fix -----------------------------------
if ($ApplyFix) {
    $body = Get-Content $buffJava -Raw
    if ($body -notmatch 'class\s+AlreadyFull') {
        Say "applying Buff.AlreadyFull fix to $buffJava"
        $needle = "public class Buff extends Condition {"
        $insert = @"
public class Buff extends Condition {
	public static class AlreadyFull extends Condition {
		private static final long serialVersionUID = -3313640460601389318L;
	}

"@
        $body = $body.Replace($needle, $insert)
        Set-Content -Path $buffJava -Value $body -Encoding UTF8 -NoNewline
    } else {
        Say "fix already present in Buff.java"
    }
}

# --- compile ---------------------------------------------------------------
$cp   = (Get-ChildItem $libDir -Filter *.jar | ForEach-Object { $_.FullName }) -join ';'
$srcs = Get-ChildItem $srcRoot -Recurse -Filter *.java | ForEach-Object { $_.FullName }
Say "compiling $($srcs.Count) java files"
$argfile = Join-Path $logs 'sources.txt'
$srcs | Set-Content $argfile -Encoding ASCII

$output = & $javac -nowarn -encoding UTF-8 -cp $cp -d $classes "@$argfile" 2>&1
$output | Set-Content (Join-Path $logs 'compile.log')
$errCount = ($output | Select-String -Pattern 'error:').Count

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "COMPILE FAILED - $errCount error(s). Full log: $logs\compile.log" -ForegroundColor Red
    $output | Select-Object -First 40 | ForEach-Object { Write-Host "  $_" }
    if ($output -match 'Buff\.AlreadyFull' -or $output -match 'symbol:\s+class AlreadyFull') {
        Write-Host ""
        Write-Host "This is the known issue. Re-run with -ApplyFix to patch it." -ForegroundColor Yellow
    }
    exit 1
}
$n = (Get-ChildItem $classes -Recurse -Filter *.class).Count
Say "compiled OK - 0 errors, $n class files"

# --- package ---------------------------------------------------------------
$stamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$outJar = Join-Path $out "JaSBro-rebuilt-$stamp.jar"

$libNames = (Get-ChildItem $libDir -Filter *.jar | Sort-Object Name | ForEach-Object { "./lib/$($_.Name)" }) -join ' '
$manifest = Join-Path $out 'MANIFEST.MF'

# JAR spec: manifest lines max 72 bytes, continuations start with a single space
function Wrap-Manifest([string]$line) {
    $b = [System.Text.Encoding]::UTF8.GetBytes($line)
    if ($b.Length -le 72) { return $line + "`r`n" }
    $sb = New-Object System.Text.StringBuilder
    $first = $b[0..71]; [void]$sb.Append([System.Text.Encoding]::UTF8.GetString($first) + "`r`n")
    $rest = $b[72..($b.Length-1)]
    while ($rest.Length -gt 0) {
        $take = [Math]::Min(71, $rest.Length)
        $chunk = $rest[0..($take-1)]
        if ($rest.Length -gt 71) { $rest = $rest[71..($rest.Length-1)] } else { $rest = @() }
        [void]$sb.Append(" " + [System.Text.Encoding]::UTF8.GetString($chunk) + "`r`n")
    }
    return $sb.ToString()
}

$mf = "Manifest-Version: 1.0`r`n"
$mf += "Implementation-Title: JaSBro`r`n"
$mf += "Implementation-Version: 1.0`r`n"
$mf += "Main-Class: jasbro.Jasbro`r`n"
$mf += Wrap-Manifest ("Class-Path: " + $libNames) + "`r`n"
[System.IO.File]::WriteAllText($manifest, $mf, (New-Object System.Text.UTF8Encoding($false)))

Say "packaging $outJar"
$stage = Join-Path $out 'stage'
Remove-Item $stage -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $stage | Out-Null
Copy-Item "$classes\*" $stage -Recurse -Force
if (Test-Path $resRoot) { Copy-Item "$resRoot\*" $stage -Force }

& $jarExe cfm $outJar $manifest -C $stage . | Out-Null
Remove-Item $stage -Recurse -Force -ErrorAction SilentlyContinue

if (-not (Test-Path $outJar)) { throw "jar packaging failed" }
$mb = [math]::Round((Get-Item $outJar).Length / 1MB, 2)
Say "done - $outJar ($mb MB)"

# --- how to run ------------------------------------------------------------
Say ""
Say "To run, drop the jar next to the game's lib/ and data folders, e.g.:"
Say "    cd C:\Games\Jasbro_Final"
Say "    `"$root\tools\jdk8\bin\java.exe`" -jar $outJar"
