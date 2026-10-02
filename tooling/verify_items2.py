import os
root = r"C:\Games\Jasbro_Final\items"
names = set()
for r,_,fs in os.walk(root):
    for f in fs:
        if f.lower().endswith(".xml"):
            names.add(f[:-4])
for probe in ["XX_JSbro_Head Uniform Bartending", "XX_JSbro_Dress Uniform Bartending", "XX_JSbro_Dress Uniform Bertending"]:
    print(f"  {'OK  ' if probe in names else 'MISS'} {probe}")
print()
print("All 'Head Uniform' / 'Bunny' / 'Map' item ids:")
for n in sorted(names):
    if "head uniform" in n.lower() or "bunny" in n.lower() or "map" in n.lower():
        print("   ", n)
