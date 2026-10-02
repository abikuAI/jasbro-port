import urllib.request, ssl, re, sys
ctx = ssl.create_default_context()
for art in ["cfr", "procyon-decompiler", "vineflower"]:
    grp = {"cfr":"org/benf","procyon-decompiler":"org/bitbucket/mstrobel","vineflower":"org/vineflower"}[art]
    url = f"https://repo1.maven.org/maven2/{grp}/{art}/maven-metadata.xml"
    try:
        with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent":"curl/8"}), context=ctx, timeout=60) as r:
            x = r.read().decode()
        rel = re.search(r"<release>(.*?)</release>", x)
        vers = re.findall(r"<version>(.*?)</version>", x)
        print(f"{art:24} release={rel.group(1) if rel else '?'}  latest_few={vers[-4:]}")
    except Exception as e:
        print(f"{art:24} ERROR {e}")
