import urllib.request, ssl, sys, os
url = "https://api.adoptium.net/v3/binary/latest/8/ga/windows/x64/jdk/hotspot/normal/eclipse"
out = sys.argv[1]
ctx = ssl.create_default_context()
print("python ssl:", ssl.OPENSSL_VERSION, flush=True)
req = urllib.request.Request(url, headers={"User-Agent":"curl/8"})
with urllib.request.urlopen(req, context=ctx, timeout=120) as r, open(out,"wb") as f:
    total=0
    while True:
        b = r.read(1<<20)
        if not b: break
        f.write(b); total += len(b)
    print("bytes:", total)
