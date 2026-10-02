import os, re, glob

# Where does the game keep items? Check both the reference copy and the live game.
for root in [r"C:\Users\Computer\Documents\deepseek-harness\default-workspace\JaSBro-work\original\items",
             r"C:\Games\Jasbro_Final\items"]:
    print("="*70)
    print("ITEMS ROOT:", root, "exists:", os.path.isdir(root))
    if not os.path.isdir(root): continue
    names = set()
    for r,_,fs in os.walk(root):
        for f in fs:
            if f.lower().endswith(".xml"):
                names.add(f[:-4])
    hits = sorted(n for n in names if "dress uniform" in n.lower() or "dungeon" in n.lower())
    print(f"  total item ids: {len(names)}")
    print("  dress-uniform / dungeon ids found:")
    for h in hits: print("     ", h)
    print("  EXACT 'XX_JSbro_Dress Uniform Bertending' present:", "XX_JSbro_Dress Uniform Bertending" in names)
    print("  'dungeon1Map' present:", "dungeon1Map" in names)
