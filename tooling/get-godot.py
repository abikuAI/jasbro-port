"""Download the Godot 4.7.2 Mono editor for Windows x64.

This machine's schannel TLS stack is broken (SEC_E_NO_CREDENTIALS), so Invoke-WebRequest,
curl.exe and Git's curl all fail. Python's urllib uses its own TLS path and works.

Downloads the archive AND the release's SHA512-SUMS.txt, verifies the hash, and only then
leaves the archive in place. A truncated or tampered download is deleted rather than extracted.
"""

import hashlib
import os
import pathlib
import sys
import urllib.request

VERSION = "4.7.2-stable"
ASSET = f"Godot_v{VERSION}_mono_win64.zip"
BASE = f"https://github.com/godotengine/godot/releases/download/{VERSION}"

DEST = pathlib.Path(sys.argv[1]) if len(sys.argv) > 1 else pathlib.Path("godot-download")
DEST.mkdir(parents=True, exist_ok=True)

archive = DEST / ASSET
sums = DEST / "SHA512-SUMS.txt"


def fetch(url, path):
    print(f"GET {url}", flush=True)
    req = urllib.request.Request(url, headers={"User-Agent": "dsh-port/1.0"})
    with urllib.request.urlopen(req, timeout=120) as r, open(path, "wb") as f:
        total = int(r.headers.get("Content-Length") or 0)
        done = 0
        step = 8 * 1024 * 1024
        while True:
            chunk = r.read(1024 * 256)
            if not chunk:
                break
            f.write(chunk)
            done += len(chunk)
            if total:
                pct = done * 100 // total
                if done % step < 1024 * 256:
                    print(f"  {done/1e6:7.1f} / {total/1e6:.1f} MB  ({pct}%)", flush=True)
    print(f"  wrote {path} ({path.stat().st_size} bytes)", flush=True)


# ---- sums first (small) -------------------------------------------------
if not sums.exists():
    fetch(f"{BASE}/SHA512-SUMS.txt", sums)

expected = None
for line in sums.read_text(encoding="utf-8", errors="replace").splitlines():
    parts = line.split()
    if len(parts) >= 2 and parts[-1].lstrip("*") == ASSET:
        expected = parts[0].lower()
        break

if expected is None:
    print(f"WARNING: {ASSET} not listed in SHA512-SUMS.txt; skipping verification", flush=True)
else:
    print(f"expected sha512: {expected[:32]}...", flush=True)

# ---- archive -------------------------------------------------------------
if archive.exists() and expected:
    h = hashlib.sha512()
    with open(archive, "rb") as f:
        for block in iter(lambda: f.read(1024 * 1024), b""):
            h.update(block)
    if h.hexdigest().lower() == expected:
        print("archive already present and hash matches; nothing to do", flush=True)
        sys.exit(0)
    print("existing archive failed verification; re-downloading", flush=True)
    archive.unlink()

if not archive.exists():
    fetch(f"{BASE}/{ASSET}", archive)

if expected:
    h = hashlib.sha512()
    with open(archive, "rb") as f:
        for block in iter(lambda: f.read(1024 * 1024), b""):
            h.update(block)
    actual = h.hexdigest().lower()
    if actual != expected:
        print(f"FAILED verification: got {actual}", flush=True)
        archive.unlink()
        sys.exit(1)
    print("SHA512 verified OK", flush=True)
else:
    print("no hash to verify against", flush=True)

print("DONE", flush=True)
