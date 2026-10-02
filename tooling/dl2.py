import urllib.request, ssl, sys
ctx = ssl.create_default_context()
jobs = [
    ("https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse", sys.argv[1]),
    ("https://repo1.maven.org/maven2/org/vineflower/vineflower/1.12.0/vineflower-1.12.0.jar", sys.argv[2]),
]
for url, out in jobs:
    with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent":"curl/8"}), context=ctx, timeout=600) as r, open(out,"wb") as f:
        n=0
        while True:
            b=r.read(1<<20)
            if not b: break
            f.write(b); n+=len(b)
    print("ok", out, round(n/1048576,1), "MB", flush=True)
