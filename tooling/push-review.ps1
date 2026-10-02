<#
.SYNOPSIS
  Stage, commit and push the JaSBro review repository.

.DESCRIPTION
  Exists because pushing from inside the DSH sandbox has two obstacles that are not obvious:

  1. Git on this machine is configured with `credential.helper=manager` system-wide, plus a
     github.com-specific helper pointing at `gh auth git-credential`. That helper spawns sh.exe,
     and the sandbox forbids opening the named pipe it needs:
         sh.exe: *** fatal error - couldn't create signal pipe, Win32 error 5
     So both helpers are cleared for the push and an auth header is supplied directly instead.

  2. The account's email is private, and GitHub rejects commits that would expose it (GH007).
     Commits are therefore authored with the GitHub noreply address.

  Outside the sandbox none of this is needed: run `gh auth setup-git` once in a normal shell and
  plain `git push` works.

.EXAMPLE
  pwsh -File push-review.ps1 -Message "Add trait singleton findings"
#>
[CmdletBinding()]
param(
    [string]$Work   = (Split-Path -Parent $PSScriptRoot),
    [string]$Dest   = 'C:\Users\Computer\Documents\deepseek-harness\default-workspace\jasbro-review',
    [string]$Repo   = 'abikuAI/jasbro-port',
    [Parameter(Mandatory)][string]$Message,
    [switch]$SkipRestage
)

$ErrorActionPreference = 'Stop'
$user = 'abikuAI'
$id   = '301110229'
$mail = "$id+$user@users.noreply.github.com"

# ---------------------------------------------------------------- 1. re-stage from source of truth
if (-not $SkipRestage) {
    Write-Host '--- re-staging ---' -ForegroundColor Cyan
    & pwsh -NoProfile -File (Join-Path $Work 'tools\stage-review-repo.ps1') -Destination $Dest
    if ($LASTEXITCODE -ne 0) { throw 'staging failed' }
}

Set-Location $Dest

# ---------------------------------------------------------------- 2. commit
Write-Host ''
Write-Host '--- commit ---' -ForegroundColor Cyan
& git add -A
$changes = (& git diff --cached --name-only | Measure-Object).Count
if ($changes -eq 0) {
    Write-Host 'nothing to commit' -ForegroundColor Yellow
} else {
    Write-Host "$changes path(s) changed"
    & git -c user.name=$user -c user.email=$mail commit -q -m $Message
    if ($LASTEXITCODE -ne 0) { throw 'commit failed' }
    & git log -1 --format='%h %s'
}

# ---------------------------------------------------------------- 3. push
Write-Host ''
Write-Host '--- push ---' -ForegroundColor Cyan
$t = (& gh auth token 2>&1 | Select-Object -First 1).Trim()
if (-not $t -or $t.Length -lt 20) { throw 'could not obtain a gh token; run: gh auth login' }

$out = & git -c http.sslBackend=openssl `
             -c credential.helper= `
             -c 'credential.https://github.com.helper=' `
             push "https://x-access-token:$t@github.com/$Repo.git" main:main 2>&1
$code = $LASTEXITCODE

# Never echo the token, even if git prints it back in an error.
$out | ForEach-Object { $_ -replace [regex]::Escape($t), '<token>' } | Select-Object -Last 15
if ($code -ne 0) { throw "push failed (exit $code)" }

Write-Host ''
Write-Host "pushed to https://github.com/$Repo" -ForegroundColor Green
