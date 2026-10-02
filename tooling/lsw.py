import zipfile,sys
with zipfile.ZipFile(sys.argv[1]) as z:
    for n in z.namelist():
        if not n.endswith(".class") and not n.startswith("META-INF"):
            print("  ", n)
