import zipfile, sys
z = zipfile.ZipFile(sys.argv[1])
names = [n for n in z.namelist() if n.endswith(".class")]
for n in names:
    if "decompiler" in n.lower() and ("Command" in n or "Main" in n or n.count("/")<=3):
        print(n)
print("--- manifest ---")
try:
    print(z.read("META-INF/MANIFEST.MF").decode())
except Exception as e:
    print(e)
