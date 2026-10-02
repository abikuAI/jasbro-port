import zipfile, os, sys
wf = sys.argv[1]
ref   = os.path.join(wf,"original","JaSBro-reference.jar")
cls   = os.path.join(wf,"decompiled","classes-merged")
lib   = os.path.join(wf,"original","lib")
outjar= os.path.join(wf,"decompiled","JaSBro-from-decompiled.jar")

cp = " ".join("./lib/"+n for n in sorted(os.listdir(lib)) if n.endswith(".jar"))
def wrap(line):
    b = line.encode(); 
    if len(b) <= 72: return line+"\r\n"
    parts=[b[:72]]; rest=b[72:]
    while rest:
        parts.append(b" "+rest[:71]); rest=rest[71:]
    return b"".join(p+b"\r\n" for p in parts).decode()
man = ("Manifest-Version: 1.0\r\nImplementation-Title: JaSBro\r\n"
       "Implementation-Version: 1.0\r\nMain-Class: jasbro.Jasbro\r\n"
       + wrap("Class-Path: "+cp) + "\r\n")

n_cls = n_res = 0
with zipfile.ZipFile(outjar,"w",zipfile.ZIP_DEFLATED) as z:
    z.writestr("META-INF/MANIFEST.MF", man.encode())
    for r,_,fs in os.walk(cls):
        for f in fs:
            p=os.path.join(r,f); z.write(p, os.path.relpath(p,cls).replace("\\","/")); n_cls+=1
    # carry over the shipped jar's non-class resources (properties, log4j2 config, houses/)
    with zipfile.ZipFile(ref) as src:
        for nm in src.namelist():
            if nm.startswith("META-INF/") or nm.endswith("/") or nm.endswith(".class"): continue
            z.writestr(nm, src.read(nm)); n_res+=1
print("classes:", n_cls, " resources:", n_res)
print("jar MB:", round(os.path.getsize(outjar)/1048576,2))
