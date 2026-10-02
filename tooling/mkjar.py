import os, sys, zipfile
wf = sys.argv[1]
libdir = os.path.join(wf, "original", "lib")
cls = os.path.join(wf, "build-out", "classes-main")
res = os.path.join(wf, "repo", "src", "main", "resources")
outjar = os.path.join(wf, "build-out", "JaSBro-rebuilt.jar")

libs = sorted(os.listdir(libdir))
cp = " ".join("./lib/" + n for n in libs if n.endswith(".jar"))

def wrap(line):
    # JAR manifest: max 72 bytes per line, continuation begins with a single space
    b = line.encode("utf-8")
    if len(b) <= 72: return line + "\r\n"
    parts, cur = [], b[:72]
    rest = b[72:]
    parts.append(cur)
    while rest:
        cur = rest[:71]; rest = rest[71:]
        parts.append(b" " + cur)
    return b"".join(p + b"\r\n" for p in parts).decode("utf-8")

man = ("Manifest-Version: 1.0\r\n"
       "Implementation-Title: JaSBro\r\n"
       "Implementation-Version: 1.0\r\n"
       "Main-Class: jasbro.Jasbro\r\n"
       + wrap("Class-Path: " + cp) + "\r\n")

with zipfile.ZipFile(outjar, "w", zipfile.ZIP_DEFLATED) as z:
    z.writestr("META-INF/MANIFEST.MF", man.encode("utf-8"))
    n = 0
    for root, _, files in os.walk(cls):
        for f in files:
            full = os.path.join(root, f)
            z.write(full, os.path.relpath(full, cls).replace("\\", "/")); n += 1
    for root, _, files in os.walk(res):
        for f in files:
            full = os.path.join(root, f)
            z.write(full, os.path.relpath(full, res).replace("\\", "/")); n += 1
print("wrote", outjar, "entries:", n, "size MB:", round(os.path.getsize(outjar)/1048576, 2))
