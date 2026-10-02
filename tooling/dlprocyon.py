import urllib.request, ssl, re, os, sys
ctx = ssl.create_default_context()
base = "https://repo1.maven.org/maven2/org/bitbucket/mstrobel/"
arts = ["procyon-compilertools","procyon-core","procyon-expressions","procyon-reflection"]
dest = sys.argv[1]
os.makedirs(dest, exist_ok=True)
for a in arts:
    with urllib.request.urlopen(urllib.request.Request(base+a+"/maven-metadata.xml", headers={"User-Agent":"curl/8"}), context=ctx, timeout=60) as r:
        x = r.read().decode()
    ver = re.search(r"<release>(.*?)</release>", x).group(1)
    url = f"{base}{a}/{ver}/{a}-{ver}.jar"
    out = os.path.join(dest, f"{a}-{ver}.jar")
    with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent":"curl/8"}), context=ctx, timeout=180) as r, open(out,"wb") as f:
        f.write(r.read())
    print(f"{a}-{ver}.jar  {os.path.getsize(out)} bytes", flush=True)
