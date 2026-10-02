import os, re
root = r"C:\Games\Jasbro_Final\characters"
for r,_,fs in os.walk(root):
    for f in fs:
        if f == "properties.xml":
            p = os.path.join(r,f)
            t = open(p, encoding="utf-8", errors="replace").read()
            name = re.search(r'<name>(.*?)</name>', t, re.S)
            typ  = re.search(r'<type>(.*?)</type>', t, re.S)
            gen  = re.search(r'<gender>(.*?)</gender>', t, re.S)
            print(f"{os.path.basename(r):28s} name={name.group(1).strip() if name else '-':22s} type={typ.group(1).strip() if typ else '<MISSING>':12s} gender={gen.group(1).strip() if gen else '<MISSING>'}")
