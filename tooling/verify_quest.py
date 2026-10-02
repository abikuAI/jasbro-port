import os, re
live = r"C:\Games\Jasbro_Final"
for pat in ["freeBunnysuit.xml", "FreeBunnySuitQuest.xml", "dungeon1UnlockEvent.xml"]:
    found = []
    for r,_,fs in os.walk(live):
        for f in fs:
            if f == pat:
                found.append(os.path.join(r,f))
    for p in found:
        t = open(p, encoding="utf-8", errors="replace").read()
        print("="*72)
        print(p)
        print("  bytes:", len(t))
        # stage ids
        print("  <questStage> values:", re.findall(r'<questStage>(.*?)</questStage>', t))
        print("  <itemId> values:", re.findall(r'<itemId>(.*?)</itemId>', t))
        print("  CustomQuestStage count:", len(re.findall(r'<CustomQuestStage>', t)))
        # find the order of the effects near GainItem / SetQuestStatus
        for m in re.finditer(r'<(WorldEvent\w+|CustomQuestStage)(>|\s)', t):
            pass
        seq = re.findall(r'<(/?)WorldEvent(GainItem|SetQuestStatus)>', t)
        print("  event element order:", seq)
