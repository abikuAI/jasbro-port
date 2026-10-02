import urllib.request, ssl, sys, os
ctx = ssl.create_default_context()
url = "https://repo1.maven.org/maven2/org/benf/cfr/0.152/cfr-0.152.jar"
out = sys.argv[1]
with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent":"curl/8"}), context=ctx, timeout=120) as r, open(out,"wb") as f:
    n=0
    while True:
        b=r.read(1<<20)
        if not b: break
        f.write(b); n+=len(b)
print("downloaded", out, n, "bytes")
