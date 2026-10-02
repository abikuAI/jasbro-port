using Simbro.Core.Content;
using Simbro.Core.Content.Rooms;
using Simbro.Core.Events;
using Simbro.Core.Save;
using Simbro.Core.State;
using Simbro.Core.State.Attributes;

// Parity checks for the ported domain core.
// Deliberately dependency-free (no xunit) so it runs without NuGet access.
// Exits non-zero if anything fails.

int passed = 0, failed = 0;

void Check(string name, bool condition, string? detail = null)
{
    if (condition)
    {
        passed++;
        Console.WriteLine($"  PASS  {name}");
    }
    else
    {
        failed++;
        Console.WriteLine($"  FAIL  {name}{(detail is null ? "" : $"  -- {detail}")}");
    }
}

Console.WriteLine("=== Time enum parity (legacy jasbro.game.world.Time) ===");

Check("enum has exactly 3 values (no EVENING)",
    Enum.GetValues<Time>().Length == 3,
    $"got {Enum.GetValues<Time>().Length}");

Check("MORNING -> AFTERNOON", Time.MORNING.NextTimeOfDay() == Time.AFTERNOON);
Check("AFTERNOON -> NIGHT", Time.AFTERNOON.NextTimeOfDay() == Time.NIGHT);
Check("NIGHT -> MORNING", Time.NIGHT.NextTimeOfDay() == Time.MORNING);

Check("MORNING -> NIGHT (previous)", Time.MORNING.PreviousTimeOfDay() == Time.NIGHT);
Check("AFTERNOON -> MORNING (previous)", Time.AFTERNOON.PreviousTimeOfDay() == Time.MORNING);
Check("NIGHT -> AFTERNOON (previous)", Time.NIGHT.PreviousTimeOfDay() == Time.AFTERNOON);

Check("cycle returns to start after 3 steps",
    Time.MORNING.NextTimeOfDay().NextTimeOfDay().NextTimeOfDay() == Time.MORNING);

Check("next and previous are inverses for all values",
    Enum.GetValues<Time>().All(t => t.NextTimeOfDay().PreviousTimeOfDay() == t));

Check("IsNewDay only for MORNING",
    Time.MORNING.IsNewDay() && !Time.AFTERNOON.IsNewDay() && !Time.NIGHT.IsNewDay());

Console.WriteLine();
Console.WriteLine("=== GameData defaults (legacy GameData field initialisers) ===");

var gd = new GameData();
Check("day defaults to 1", gd.Day == 1, $"got {gd.Day}");
Check("money defaults to 500", gd.Money == 500L, $"got {gd.Money}");
Check("time defaults to MORNING", gd.Time == Time.MORNING, $"got {gd.Time}");

Console.WriteLine();
Console.WriteLine("=== Money semantics parity ===");

var log = new RecordingSink();
var g = new GameData(log);

g.EarnMoney(100, "test");
Check("earn applies amount", g.Money == 600L, $"got {g.Money}");
Check("earn raised exactly one event", log.Events.Count == 1, $"got {log.Events.Count}");
Check("earn raised MONEYEARNED", log.Events[0].Type == EventType.MONEYEARNED);
Check("earn notified status change", log.StatusNotifications == 1, $"got {log.StatusNotifications}");

log.Clear();
g.SpendMoney(50, "test");
Check("spend applies amount", g.Money == 550L, $"got {g.Money}");
Check("spend raised exactly one event", log.Events.Count == 1, $"got {log.Events.Count}");
Check("spend raised MONEYSPENT", log.Events[0].Type == EventType.MONEYSPENT);

// Legacy quirk: balance may go negative, and BROKE is raised only on crossing below zero.
log.Clear();
g.SpendMoney(10_000L, "overspend");
Check("balance is allowed to go negative", g.Money < 0L, $"got {g.Money}");
Check("BROKE raised when balance goes negative",
    log.Events.Any(e => e.Type == EventType.BROKE), $"events: {string.Join(",", log.Events.Select(e => e.Type))}");

log.Clear();
g.EarnMoney(999_999L, "recover");
Check("BROKE is NOT raised when recovering",
    !log.Events.Any(e => e.Type == EventType.BROKE));

Console.WriteLine();
Console.WriteLine("=== MoneyChangedEvent ordering (event before mutation) ===");

var orderSink = new OrderingSink();
var g2 = new GameData(orderSink);
orderSink.Data = g2;   // let the sink observe the live balance when the event fires
g2.EarnMoney(100);
Check("earn raises event before balance changes (sink saw 500, final is 600)",
    orderSink.BalanceAtEvent == 500L && g2.Money == 600L,
    $"at-event={orderSink.BalanceAtEvent}, final={g2.Money}");

Console.WriteLine();
Console.WriteLine("=== EventType parity ===");
Check("EventType has 32 values", Enum.GetValues<EventType>().Length == 32,
    $"got {Enum.GetValues<EventType>().Length}");
Check("MONEYEARNED is not custom-content relevant", !EventType.MONEYEARNED.IsCustomContentRelevant());
Check("NEXTDAY is custom-content relevant", EventType.NEXTDAY.IsCustomContentRelevant());
Check("GAMESTART is custom-content relevant", EventType.GAMESTART.IsCustomContentRelevant());
Check("BROKE is not custom-content relevant", !EventType.BROKE.IsCustomContentRelevant());

Console.WriteLine();
Console.WriteLine("=== Attribute type parity ===");

Check("BaseAttributeTypes has 6 values", Enum.GetValues<BaseAttributeTypes>().Length == 6,
    $"got {Enum.GetValues<BaseAttributeTypes>().Length}");
Check("CalculatedAttribute has 30 values", Enum.GetValues<CalculatedAttribute>().Length == 30,
    $"got {Enum.GetValues<CalculatedAttribute>().Length}");
Check("SpecializationAttribute has 17 values", Enum.GetValues<SpecializationAttribute>().Length == 17,
    $"got {Enum.GetValues<SpecializationAttribute>().Length}");

Check("Base metadata is (0,20,10,1)",
    AttributeTypes.Meta(default(BaseAttributeTypes)) == (0, 20, 10, 1),
    $"got {AttributeTypes.Meta(default(BaseAttributeTypes))}");
Check("Specialization metadata is (0,20,10,0)",
    AttributeTypes.Meta(default(SpecializationAttribute)) == (0, 20, 10, 0),
    $"got {AttributeTypes.Meta(default(SpecializationAttribute))}");
Check("Calculated max is int.MaxValue",
    AttributeTypes.Meta(default(CalculatedAttribute)).Max == int.MaxValue);

Check("All enumerates 6+30+17 = 53 attribute keys",
    AttributeTypes.All.Count() == 53, $"got {AttributeTypes.All.Count()}");
Check("attribute keys are distinct", AttributeTypes.All.Distinct().Count() == 53,
    $"got {AttributeTypes.All.Distinct().Count()}");

Check("TryParse resolves CHARISMA to a base attribute",
    AttributeTypes.TryParse("CHARISMA", out var k1) && k1 == AttributeKey.Base(BaseAttributeTypes.CHARISMA),
    $"got {k1}");
Check("TryParse resolves BARTENDING to a specialization",
    AttributeTypes.TryParse("BARTENDING", out var k2) && k2 == AttributeKey.Specialization(SpecializationAttribute.BARTENDING),
    $"got {k2}");
Check("TryParse resolves DAMAGE to a calculated attribute",
    AttributeTypes.TryParse("DAMAGE", out var k3) && k3 == AttributeKey.Calculated(CalculatedAttribute.DAMAGE),
    $"got {k3}");

// The legacy build crashes on unguarded Enum.valueOf of content-supplied names.
// TryParse must return false instead of throwing.
Check("TryParse returns false for an unknown name (no throw)",
    !AttributeTypes.TryParse("BEAUTICIAN", out _));
Check("TryParse returns false for empty input", !AttributeTypes.TryParse("", out _));
Check("TryParse is case sensitive, matching legacy valueOf",
    !AttributeTypes.TryParse("charisma", out _));

Check("AttributeKey is usable as a dictionary key",
    new Dictionary<AttributeKey, int> { [AttributeKey.Base(BaseAttributeTypes.CHARISMA)] = 7 }
        [AttributeKey.Base(BaseAttributeTypes.CHARISMA)] == 7);

Console.WriteLine();
Console.WriteLine("=== Character properties.xml parser (against the real content folder) ===");

const string contentRoot = @"C:\Games\Jasbro_Final\characters";

if (!Directory.Exists(contentRoot))
{
    Console.WriteLine($"  SKIP  content folder not found: {contentRoot}");
}
else
{
    var propertyFiles = Directory.GetFiles(contentRoot, "properties.xml", SearchOption.AllDirectories);
    Check("found character properties.xml files", propertyFiles.Length > 0, $"found {propertyFiles.Length}");

    var parsed = new List<CharacterDefinition>();
    var problems = new List<string>();

    foreach (var path in propertyFiles)
    {
        var folder = Path.GetDirectoryName(path)!;
        var id = Path.GetFileName(folder);
        var res = CharacterPropertiesParser.Parse(File.ReadAllText(path), folder, id);

        if (res.Success && res.Definition is not null)
        {
            parsed.Add(res.Definition);
        }
        else
        {
            problems.Add($"{id}: {string.Join("; ", res.Errors)}");
        }

        // Unknown traits are expected in this content set — surface them rather than hide.
        foreach (var w in res.Warnings.Where(w => w.StartsWith("Unknown trait")))
        {
            Console.WriteLine($"        note: {id}: {w}");
        }
    }

    Check($"all {propertyFiles.Length} character files parsed without error",
        problems.Count == 0,
        string.Join(" | ", problems));

    var totalImages = parsed.Sum(p => p.Images.Count);
    var totalTags = parsed.Sum(p => p.Images.Sum(i => i.Tags.Count));
    Console.WriteLine($"        parsed {parsed.Count} characters, {totalImages} images, {totalTags} tags");

    Check("every character has a non-empty name", parsed.All(p => !string.IsNullOrWhiteSpace(p.Name)));

    // Legacy semantics: a missing/empty <type> sets the type to null, so null is legitimate.
    // (In this content set, Mei / Patchouli / Shizuka / Touko / Zoe have no <type>.)
    var withoutType = parsed.Count(p => p.Type is null);
    Console.WriteLine($"        characters with no <type> (legacy sets null): {withoutType}");
    Check("missing <type> yields null rather than a parse failure",
        withoutType > 0 && parsed.Any(p => p.Type is not null),
        $"null={withoutType}, non-null={parsed.Count(p => p.Type is not null)}");

    // Legacy upper-cases <type> before parsing, so the lowercase "slave" in
    // characters\template must resolve to SLAVE.
    Check("lowercase <type>slave</type> resolves to SLAVE (legacy upper-cases)",
        parsed.Any(p => p.Type == CharacterType.SLAVE),
        "no character resolved to SLAVE");

    Check("every image has a non-empty key", parsed.All(p => p.Images.All(i => !string.IsNullOrEmpty(i.Key))));
    Check("image keys contain no backslashes after normalisation",
        parsed.All(p => p.Images.All(i => !i.Filename.Contains('\\'))));

    // The Loli character is a known fixture with exact expected values.
    var loli = parsed.FirstOrDefault(p => p.Name == "Loli");
    Check("Loli fixture was parsed", loli is not null);
    if (loli is not null)
    {
        Check("Loli type is CHILD", loli.Type == CharacterType.CHILD, $"got {loli.Type}");
        Check("Loli gender is FEMALE", loli.Gender == Gender.FEMALE, $"got {loli.Gender}");
        Check("Loli initialSpecialization is WHORE",
            loli.InitialSpecialization == SpecializationType.WHORE, $"got {loli.InitialSpecialization}");
        Check("Loli has traits LOLI, FRAGILE, LOYAL",
            loli.Traits.SequenceEqual(new[] { Trait.LOLI, Trait.FRAGILE, Trait.LOYAL }),
            $"got {string.Join(",", loli.Traits)}");
        Check("Loli has 5 base attributes at 6",
            loli.Attributes.Count == 5 && loli.Attributes.Values.All(v => v == 6),
            $"got {string.Join(",", loli.Attributes.Select(kv => $"{kv.Key}={kv.Value}"))}");
        Check("Loli has no COMMAND attribute (legacy skips it)",
            !loli.Attributes.ContainsKey(BaseAttributeTypes.COMMAND));
        Check("Loli has 5 images", loli.Images.Count == 5, $"got {loli.Images.Count}");
        Check("Loli image filename is normalised",
            loli.Images.All(i => i.Filename.Contains('.')));
    }

    // Unknown traits must be reported, not silently dropped.
    var withUnknown = parsed.Where(p => p.UnknownTraits.Count > 0).ToList();
    Console.WriteLine($"        characters with unrecognised traits: {withUnknown.Count}");
}

Console.WriteLine();
Console.WriteLine("=== Save format — golden fixture generated by the real Java build ===");

// fixtures/save-golden.xml is produced by fixtures/java/SaveFixture.java, which runs the actual
// decompiled game classes with the game's own XStream configuration. It is the reference the
// port's save reader must match.
var goldenPath = LocateFixture("save-golden.xml");
if (goldenPath is null)
{
    Console.WriteLine("  SKIP  fixtures/save-golden.xml not found");
}
else
{
    Console.WriteLine($"        fixture: {goldenPath}");
    var root = SaveFileReader.Parse(File.ReadAllText(goldenPath)).Root;

    Check("root element is the fully-qualified Java class name",
        root.Name == SaveFileReader.GameDataRootElement,
        $"got <{root.Name}>");

    Check("day round-trips", root.ChildInt("day") == 42, $"got {root.ChildInt("day")}");
    Check("money round-trips", root.ChildLong("money") == 1234L, $"got {root.ChildLong("money")}");

    var time = root.ChildText("time");
    Check("time serialises as the enum NAME, not the ordinal", time == "NIGHT", $"got '{time}'");

    Check("empty collections serialise as empty elements, not as absent",
        root.Child("houses") is not null && root.Child("houses")!.Children.Count == 0);
    Check("empty character list is present and empty",
        root.Child("characters") is { } chars && chars.Children.Count == 0);

    var inv = root.Child("inventory");
    Check("inventory is present with an items element",
        inv?.Child("items") is not null);

    Check("null fields are OMITTED entirely (shop/questManager/otherLocationMap absent)",
        root.Child("shop") is null && root.Child("questManager") is null &&
        root.Child("otherLocationMap") is null);

    Check("unlocks serialises as an empty element", root.Child("unlocks") is not null);

    var prefs = root.Child("defaultPreferences");
    Check("defaultPreferences is present", prefs is not null);
    Check("preferences carry per-gender service flags",
        prefs?.Child("allowedServicesFemale")?.ChildText("serviceFemales") == "true",
        $"got '{prefs?.Child("allowedServicesFemale")?.ChildText("serviceFemales")}'");

    // This is a genuine save-format hazard worth pinning down: EventManager stores listeners in a
    // WeakList, and XStream serialises that collection's INTERNAL fields (its lock, its queue, its
    // backing array) straight into the save file. A port cannot model listeners as a plain list
    // without changing the on-disk format.
    var em = root.Child("eventManager");
    Check("eventManager is present", em is not null);
    var listeners = em?.Child("listeners");
    Check("listeners element carries its concrete class in a class attribute",
        listeners?.ExplicitType == "jasbro.WeakList",
        $"got '{listeners?.ExplicitType}'");
    Check("WeakList leaks its internal lock/queue structure into the save",
        listeners?.Child("copyListLock") is not null &&
        listeners?.Child("queue") is not null &&
        listeners?.Child("list") is not null);
}

Console.WriteLine();
Console.WriteLine("=== Save format — character fixture (the demanding case) ===");

var charFixPath = LocateFixture("save-golden-character.xml");
if (charFixPath is null)
{
    Console.WriteLine("  SKIP  fixtures/save-golden-character.xml not found");
}
else
{
    var croot = SaveFileReader.Parse(File.ReadAllText(charFixPath)).Root;
    var chars = croot.Child("characters");
    Check("characters element exists", chars is not null);

    var ch = chars?.Children.FirstOrDefault();
    Check("character element name is the fully-qualified class name",
        ch?.Name == "jasbro.game.character.Charakter", $"got <{ch?.Name}>");
    Check("character name round-trips", ch?.ChildText("name") == "Test Subject",
        $"got '{ch?.ChildText("name")}'");
    Check("character type is stored on the character itself", ch?.ChildText("type") == "SLAVE");
    Check("character gender is stored on the character itself", ch?.ChildText("gender") == "FEMALE");

    // THE key finding: Charakter.base is transient, so the CharacterBase is not in the save.
    // The character carries its own copies plus a baseId linking back to the content file.
    Check("CharacterBase is NOT serialised (base is transient)",
        ch?.Child("base") is null);
    Check("baseId links the save back to the content definition",
        ch?.ChildText("baseId") == "TestChar", $"got '{ch?.ChildText("baseId")}'");

    // A List<Trait> writes each element with its FULLY-QUALIFIED TYPE as the element name.
    var traits = ch?.Child("traits");
    Check("traits list is present", traits is not null);
    Check("each trait element is named with its fully-qualified enum type",
        traits?.Children.All(t => t.Name == "jasbro.game.character.traits.Trait") == true,
        $"got {string.Join(",", traits?.Children.Select(t => t.Name) ?? [])}");
    Check("trait values are enum names, in order",
        traits?.Children.Select(t => t.Text).SequenceEqual(new[] { "LOYAL", "FIT" }) == true,
        $"got {string.Join(",", traits?.Children.Select(t => t.Text) ?? [])}");

    // EnumMap / EnumSet get a special XStream spelling the port must reproduce.
    var acts = ch?.Child("activities");
    Check("EnumMap serialises with class=\"enum-map\" and an enum-type attribute",
        acts?.Attributes.GetValueOrDefault("class") == "enum-map" &&
        acts?.Attributes.GetValueOrDefault("enum-type") == "jasbro.game.world.Time",
        $"got class='{acts?.Attributes.GetValueOrDefault("class")}' enum-type='{acts?.Attributes.GetValueOrDefault("enum-type")}'");

    var specs = ch?.Child("specializations");
    Check("EnumSet serialises with class=\"enum-set\" and an enum-type attribute",
        specs?.Attributes.GetValueOrDefault("class") == "enum-set" &&
        specs?.Attributes.GetValueOrDefault("enum-type") == "jasbro.game.character.specialization.SpecializationType",
        $"got class='{specs?.Attributes.GetValueOrDefault("class")}'");

    Check("nested value objects serialise as nested elements (fame)",
        ch?.Child("fame")?.ChildText("fame") == "0.0");
    Check("ownership enum round-trips", ch?.ChildText("ownership") == "OWNED");
}

Console.WriteLine();
Console.WriteLine("=== Save format — object references (shared/cyclic graphs) ===");

var refFixPath = LocateFixture("save-golden-reference.xml");
if (refFixPath is null)
{
    Console.WriteLine("  SKIP  fixtures/save-golden-reference.xml not found");
}
else
{
    var refDoc = SaveFileReader.Parse(File.ReadAllText(refFixPath));
    var rroot = refDoc.Root;

    var charsEl = rroot.Child("characters")!;
    Check("two same-class siblings are both written inline", charsEl.Children.Count == 2,
        $"got {charsEl.Children.Count}");

    var prot = rroot.Child("protagonist");
    Check("a shared object is written as a reference, not duplicated",
        prot is not null && prot.IsReference, $"got <{prot?.Name}> ref={prot?.ReferencePath}");
    Check("reference path presents as the raw attribute", prot?.ReferencePath is not null);

    // Depth-first enumeration must not follow references (that would loop).
    var total = refDoc.AllNodes().Count();
    Check("tree walk terminates despite the reference", total > 20, $"visited {total} nodes");

    if (prot is not null && prot.IsReference)
    {
        var target = refDoc.Resolve(prot);
        Check("reference resolves to a node named with the concrete class",
            target.Name == "jasbro.game.character.Charakter", $"got <{target.Name}>");

        // The decisive check: the reference must land on the SECOND character, proving the
        // 1-based index reading is correct.
        Check("1-based [2] resolves to the SECOND sibling, not the first",
            target.ChildText("name") == "Second Subject", $"got '{target.ChildText("name")}'");

        // Identity, not equality: this is what stops cyclic graphs from exploding.
        Check("resolution preserves object identity (same instance)",
            ReferenceEquals(refDoc.Resolve(prot), target));
        Check("resolved target is the very node inside <characters>",
            ReferenceEquals(target, charsEl.Children[1]));

        // A plain (non-reference) node resolves to itself.
        Check("non-reference nodes resolve to themselves",
            ReferenceEquals(refDoc.Resolve(rroot.Child("money")!), rroot.Child("money")));
    }
}

Console.WriteLine();
Console.WriteLine("=== Save writer — cross-language round trip ===");

// Emit a save written entirely by the C# port. A separate step feeds this file to the REAL
// Java game (fixtures/java/SaveLoader.java) to prove the original can load what the port writes.
var writeOut = LocateFixturesDirectory();
if (writeOut is null)
{
    Console.WriteLine("  SKIP  fixtures directory not found");
}
else
{
    var cs = new Simbro.Core.State.GameData();
    cs.Day = 99;
    cs.Time = Simbro.Core.State.Time.NIGHT;
    cs.SpendMoney(3);            // 500 -> 497, to prove money is not hard-coded

    var xml = SaveFileWriter.WriteGameData(cs);

    Check("writer emits XStream's declaration spelling",
        xml.StartsWith("<?xml version=\"1.0\" ?>"), xml[..Math.Min(30, xml.Length)]);
    Check("writer uses the fully-qualified root class name",
        xml.Contains("<jasbro.game.GameData>"));
    Check("writer never self-closes empty elements (XStream writes <x></x>)",
        !xml.Contains("/>"), "found a self-closed element");
    Check("writer emits the WeakList internals the real game stores",
        xml.Contains("class=\"jasbro.WeakList\"") && xml.Contains("<copyListLock>"));

    var outDir = Path.Combine(writeOut, "out");
    Directory.CreateDirectory(outDir);
    var outPath = Path.Combine(outDir, "csharp-written.xml");
    File.WriteAllText(outPath, xml);
    Console.WriteLine($"        wrote {outPath}");

    // The port must also be able to read back its own output.
    var reread = SaveFileReader.Parse(xml).Root;
    Check("port reads back its own save: day", reread.ChildInt("day") == 99);
    Check("port reads back its own save: time", reread.ChildText("time") == "NIGHT");
    Check("port reads back its own save: money", reread.ChildLong("money") == 497L);

    // ---- And now with a character, the demanding case. ----
    var withChar = SaveFileWriter.WriteGameData(cs, new[]
    {
        new CharacterSaveState
        {
            BaseId = "Loli",
            Name = "Loli",
            Type = CharacterType.CHILD,
            Gender = Gender.FEMALE,
            Fame = 0,
            BonusPerks = 2,
        }.WithTraits(Trait.LOLI, Trait.FRAGILE, Trait.LOYAL),
    });

    Check("character save carries the baseId link back to content",
        withChar.Contains("<baseId>Loli</baseId>"));
    Check("character save does NOT contain the content definition",
        !withChar.Contains("<base>"),
        "found a serialised CharacterBase — base must be transient");
    Check("traits are written named with the fully-qualified enum type",
        withChar.Contains("<jasbro.game.character.traits.Trait>LOLI<"));
    Check("EnumMap/EnumSet spellings are emitted for a character",
        withChar.Contains("class=\"enum-map\"") && withChar.Contains("class=\"enum-set\""));

    var charOutPath = Path.Combine(outDir, "csharp-written-character.xml");
    File.WriteAllText(charOutPath, withChar);
    Console.WriteLine($"        wrote {charOutPath}");

    var cr = SaveFileReader.Parse(withChar).Root.Child("characters")?.Children.FirstOrDefault();
    Check("port reads back its own character", cr?.Name == "jasbro.game.character.Charakter");
    Check("port reads back the character name", cr?.ChildText("name") == "Loli");
    Check("port reads back the character traits",
        cr?.Child("traits")?.Children.Select(t => t.Text).SequenceEqual(new[] { "LOLI", "FRAGILE", "LOYAL" }) == true);
}

Console.WriteLine();
Console.WriteLine("=== Content parity — C# parser vs the REAL Java loader ===");

// content-golden.txt is produced by fixtures/java/ContentFixture.java, which runs the shipped
// game's own CharacterFileLoader over the real characters/ folder. Comparing against it is a
// genuine parity test: it is not a hand-written expectation, it is what the game actually reads.
var goldenContent = LocateFixture("content-golden.txt");
var gameCharsDir = @"C:\Games\Jasbro_Final\characters";

if (goldenContent is null)
{
    Console.WriteLine("  SKIP  content-golden.txt not found");
}
else if (!Directory.Exists(gameCharsDir))
{
    Console.WriteLine($"  SKIP  game content folder not found at {gameCharsDir}");
}
else
{
    var expected = ContentGolden.Parse(File.ReadAllLines(goldenContent));
    Console.WriteLine($"        fixture lists {expected.Count} characters (game loader reported COUNT={expected.Count})");

    var matched = 0;
    var problems = new List<string>();

    foreach (var exp in expected)
    {
        var dir = Path.Combine(gameCharsDir, exp.Id);
        var propsPath = Path.Combine(dir, "properties.xml");
        if (!File.Exists(propsPath))
        {
            problems.Add($"{exp.Id}: no properties.xml at {propsPath}");
            continue;
        }

        var parsed = CharacterPropertiesParser.Parse(File.ReadAllText(propsPath), dir, exp.Id);
        var def = parsed.Definition;
        if (def is null)
        {
            problems.Add($"{exp.Id}: C# parser produced no definition ({string.Join("; ", parsed.Errors)})");
            continue;
        }

        // Compare every field the fixture records.
        if (def.Name != exp.Name) problems.Add($"{exp.Id}: name '{def.Name}' != '{exp.Name}'");
        if (def.Type?.ToString() != exp.Type) problems.Add($"{exp.Id}: type '{def.Type}' != '{exp.Type}'");
        if (def.Gender?.ToString() != exp.Gender) problems.Add($"{exp.Id}: gender '{def.Gender}' != '{exp.Gender}'");
        if (def.InitialSpecialization?.ToString() != exp.Spec)
            problems.Add($"{exp.Id}: spec '{def.InitialSpecialization}' != '{exp.Spec}'");

        // Attributes: compare as a set (the fixture preserves EnumMap declaration order).
        var gotAttrs = def.Attributes
            .OrderBy(kv => kv.Key.ToString())
            .Select(kv => $"{kv.Key}={kv.Value}");
        var wantAttrs = exp.Attributes.OrderBy(a => a.Split('=')[0]);
        if (!gotAttrs.SequenceEqual(wantAttrs))
            problems.Add($"{exp.Id}: attributes [{string.Join(" ", gotAttrs)}] != [{string.Join(" ", wantAttrs)}]");

        // Traits are order-sensitive; the loader preserves file order.
        var gotTraits = def.Traits.Select(t => t.ToString());
        if (!gotTraits.SequenceEqual(exp.Traits))
            problems.Add($"{exp.Id}: traits [{string.Join(" ", gotTraits)}] != [{string.Join(" ", exp.Traits)}]");

        if (def.Images.Count != exp.ImageCount)
            problems.Add($"{exp.Id}: image count {def.Images.Count} != {exp.ImageCount}");

        // Per-image tags, in order — this is where the tag ALIASES (ASS->ANAL, etc.) show up.
        var imagesToCompare = Math.Min(def.Images.Count, exp.ImageCount);
        for (var i = 0; i < imagesToCompare; i++)
        {
            var got = def.Images[i];
            var want = exp.Images[i];
            if (got.Filename != want.Filename)
            {
                problems.Add($"{exp.Id}[{i}]: filename '{got.Filename}' != '{want.Filename}'");
                continue;
            }
            // Per-image tags compared AS SETS, not sequences.
            //
            // Legacy stores tags in a HashSet<ImageTag>, so the order the real loader emits is a
            // JVM hash artifact — stable on this JVM, guaranteed nowhere, and uncontrollable by
            // content authors. It was verified stable across runs, which is exactly what makes it
            // a trap: comparing order would look like a meaningful test while asserting an
            // implementation detail. Set equality is the real contract.
            var gotTags = got.Tags.Select(t => t.ToString()).OrderBy(x => x, StringComparer.Ordinal);
            var wantTags = want.Tags.OrderBy(x => x, StringComparer.Ordinal);
            if (!gotTags.SequenceEqual(wantTags))
                problems.Add($"{exp.Id}[{i}] {want.Filename}: tags {{{string.Join(",", gotTags)}}} != {{{string.Join(",", wantTags)}}}");
        }

        matched++;
    }

    Check($"C# parser reproduces all {expected.Count} characters the Java loader read",
        matched == expected.Count, $"{matched}/{expected.Count}");

    if (problems.Count == 0)
    {
        Check("every field of every character matches the Java loader (name, type, gender, spec, attributes, traits, images, tags)",
            true);
    }
    else
    {
        Check("every field of every character matches the Java loader", false,
            $"{problems.Count} mismatch(es)");
        foreach (var p in problems.Take(15))
        {
            Console.WriteLine($"        - {p}");
        }
    }
}

Console.WriteLine();
Console.WriteLine("=== AgeProgressionData weak references (unindexed reference form) ===");

// Generated by fixtures/java/AgeProgressionFixture.java, which runs the real game classes.
// This fixture closes the open question the character-lifecycle audit raised: AgeProgressionData
// holds java.lang.ref.WeakReference<Charakter> mother/father and IS persisted.
//
// Two things were established empirically rather than assumed:
//   1. XStream DOES round-trip the weak reference, and when the referent is also a character in
//      the same save, it writes a REFERENCE (not a copy) — so identity is preserved.
//   2. That reference is UNINDEXED and its final segment is a bare TYPE NAME:
//          reference="../../../../jasbro.game.character.Charakter"
//      which is a different form from the indexed ".../Charakter[2]" seen elsewhere.
var ageRef = LocateFixture("save-golden-ageref.xml");
if (ageRef is null)
{
    Console.WriteLine("  SKIP  save-golden-ageref.xml not found");
}
else
{
    var doc = SaveFileReader.Parse(File.ReadAllText(ageRef));
    var list = doc.Root;
    Check("age-ref fixture root is a <list>", list.Name == "list", list.Name);

    var chars = list.Children.Where(c => c.Name == "jasbro.game.character.Charakter").ToList();
    Check("age-ref fixture has two characters", chars.Count == 2, chars.Count.ToString());

    var mother = chars[0];
    var child = chars[1];
    Check("first character is the mother", mother.ChildText("name") == "Mother");
    Check("second character is the child", child.ChildText("name") == "Child");

    var ageData = child.Child("ageProgressionData");
    Check("child carries ageProgressionData", ageData is not null);

    var referent = ageData?.Child("mother")?.Child("referent");
    Check("the weak reference is a <referent> element", referent is not null);
    Check("the referent is marked as a reference", referent?.IsReference == true);
    Check("the reference path is UNINDEXED and ends in a bare type name",
        referent?.ReferencePath == "../../../../jasbro.game.character.Charakter",
        referent?.ReferencePath);

    // The decisive check: it must resolve to the SAME node as characters[0], not a copy.
    var resolved = referent is null ? null : doc.Resolve(referent);
    Check("unindexed reference resolves to the mother node", resolved == mother,
        resolved is null ? "<null>" : resolved.ChildText("name") ?? resolved.Name);
    Check("resolved node is the very same instance (identity, not a copy)",
        ReferenceEquals(resolved, mother));

    Check("nameMother survives independently of the reference",
        ageData?.ChildText("nameMother") == "Mother-Name");
    Check("nameFather survives independently of the reference",
        ageData?.ChildText("nameFather") == "Father-Name");
}

// ---------------------------------------------------------------------------------------------
// rooms.xml - the OTHER content system.
//
// rooms.xml is not XStream. It is a DOM walk dispatching on the `type` ATTRIBUTE, and every
// requirement element is spelled either <requirement> or <char-requirement> with the real type in
// the attribute. See CONTENT-MODEL.md.
//
// rooms-golden.txt is produced by fixtures/java/RoomsFixture.java, which runs the shipped game's
// own RoomLoader and reflects into RoomInfo's private requirement maps. So this compares against
// what the game actually built, not a hand-written expectation.
// ---------------------------------------------------------------------------------------------
var goldenRooms = LocateFixture("rooms-golden.txt");
var roomsXml = LocateOriginalFile("rooms.xml");

if (goldenRooms is null)
{
    Console.WriteLine("  SKIP  rooms-golden.txt not found");
}
else if (roomsXml is null)
{
    Console.WriteLine("  SKIP  rooms.xml not found");
}
else
{
    var expected = RoomsGolden.Parse(File.ReadAllLines(goldenRooms));
    Console.WriteLine($"        fixture lists {expected.Count} rooms (loader reported ROOMS={expected.Count})");

    Dictionary<string, RoomDefinition> actual;
    try
    {
        actual = RoomLoader.LoadRooms(roomsXml);
    }
    catch (Exception e)
    {
        actual = new Dictionary<string, RoomDefinition>();
        Check("C# RoomLoader loads rooms.xml without throwing", false, $"{e.GetType().Name}: {e.Message}");
    }

    Check("C# loader finds the same room count as the Java loader",
        actual.Count == expected.Count, $"C#={actual.Count} Java={expected.Count}");

    var roomProblems = new List<string>();

    foreach (var exp in expected)
    {
        if (!actual.TryGetValue(exp.Id, out var room))
        {
            roomProblems.Add($"{exp.Id}: missing from C# load");
            continue;
        }

        if (room.Cost != exp.Cost) roomProblems.Add($"{exp.Id}: cost {room.Cost} != {exp.Cost}");
        if (room.MaxOccupancy != exp.MaxOccupancy)
            roomProblems.Add($"{exp.Id}: maxOccupancy {room.MaxOccupancy} != {exp.MaxOccupancy}");
        if (room.Image != exp.Image) roomProblems.Add($"{exp.Id}: image '{room.Image}' != '{exp.Image}'");

        // Slot types: Java uses an EnumSet, so declaration order. RoomDefinition matches that.
        var gotSlots = string.Join(",", room.SlotTypes.Select(s => s.ToString()));
        if (gotSlots != exp.Slots) roomProblems.Add($"{exp.Id}: slots [{gotSlots}] != [{exp.Slots}]");

        var gotActs = string.Join(",", room.Activities.Select(a => a.ToString()));
        if (gotActs != exp.Activities) roomProblems.Add($"{exp.Id}: activities [{gotActs}] != [{exp.Activities}]");

        var gotReqs = room.ActivityRequirements
            .OrderBy(kv => kv.Key.ToString(), StringComparer.Ordinal)
            .ToDictionary(kv => kv.Key.ToString(), kv => kv.Value.Describe());
        if (!gotReqs.OrderBy(kv => kv.Key, StringComparer.Ordinal)
                    .SequenceEqual(exp.Requirements.OrderBy(kv => kv.Key, StringComparer.Ordinal)))
        {
            foreach (var kv in exp.Requirements)
            {
                gotReqs.TryGetValue(kv.Key, out var got);
                if (got != kv.Value)
                    roomProblems.Add($"{exp.Id}/{kv.Key}: req '{got ?? "<missing>"}' != '{kv.Value}'");
            }
        }

        var gotChild = room.ChildCareActivityRequirements
            .OrderBy(kv => kv.Key.ToString(), StringComparer.Ordinal)
            .ToDictionary(kv => kv.Key.ToString(), kv => kv.Value.Describe());
        foreach (var kv in exp.ChildRequirements)
        {
            gotChild.TryGetValue(kv.Key, out var got);
            if (got != kv.Value)
                roomProblems.Add($"{exp.Id}/{kv.Key}: childreq '{got ?? "<missing>"}' != '{kv.Value}'");
        }
    }

    // This is what makes the rooms check more than a shape test: it proves the port reproduced the
    // loader's incident of ignoring element names, where rooms.xml:645 writes a <requirement> that
    // is dispatched as a CHARACTER requirement.
    var sellFood = actual.TryGetValue("KITCHEN", out var kitchen)
        && kitchen.ActivityRequirements.TryGetValue(ActivityType.SELLFOOD, out var sf)
        ? sf.Describe()
        : "<not found>";
    Check("mislabelled <requirement> under min-character parses as a CHARACTER requirement",
        sellFood == "min-character(1, specialization(MAID))", sellFood);

    if (roomProblems.Count == 0)
    {
        Check($"all {expected.Count} rooms match the Java loader field-for-field", true);
    }
    else
    {
        Check($"all {expected.Count} rooms match the Java loader field-for-field", false);
        foreach (var p in roomProblems.Take(15))
        {
            Console.WriteLine($"        DIFF  {p}");
        }
    }
}

Console.WriteLine();
Console.WriteLine($"=== {passed} passed, {failed} failed ===");

// Walk up from the running assembly to find JaSBro-work/fixtures.
static string? LocateFixturesDirectory()
{
    var dir = new DirectoryInfo(AppContext.BaseDirectory);
    for (var d = dir; d is not null; d = d.Parent)
    {
        var candidate = Path.Combine(d.FullName, "fixtures");
        if (Directory.Exists(candidate))
        {
            return candidate;
        }
    }
    return null;
}

static string? LocateFixture(string fileName)
{
    var dir = new DirectoryInfo(AppContext.BaseDirectory);
    for (var d = dir; d is not null; d = d.Parent)
    {
        var candidate = Path.Combine(d.FullName, "fixtures", fileName);
        if (File.Exists(candidate))
        {
            return candidate;
        }
    }
    return null;
}

/// <summary>
/// Finds an original shipped content file (rooms.xml and friends).
/// </summary>
/// <remarks>
/// Two layouts must both work, because the same verifier runs in two places:
/// <list type="bullet">
/// <item><c>original/</c> — the working folder, where the file sits beside the reference jar and
/// <c>lib/</c>;</item>
/// <item><c>content/</c> — the published review repo, where toolchains and the jar are stripped and
/// only the text content is kept.</item>
/// </list>
/// Returns null when neither exists, so the caller skips rather than fails: the shipped content is
/// not redistributable, and a missing copy must not be reported as a port defect.
/// </remarks>
static string? LocateOriginalFile(string fileName)
{
    var dir = new DirectoryInfo(AppContext.BaseDirectory);
    for (var d = dir; d is not null; d = d.Parent)
    {
        foreach (var folder in new[] { "original", "content" })
        {
            var candidate = Path.Combine(d.FullName, folder, fileName);
            if (File.Exists(candidate))
            {
                return candidate;
            }
        }
    }
    return null;
}
return failed == 0 ? 0 : 1;

internal sealed class RecordingSink : IGameEventSink
{
    public List<GameEvent> Events { get; } = new();
    public int StatusNotifications { get; private set; }
    public void Handle(GameEvent gameEvent) => Events.Add(gameEvent);
    public void NotifyStatusChanged() => StatusNotifications++;
    public void Clear() { Events.Clear(); StatusNotifications = 0; }
}

internal sealed class OrderingSink : IGameEventSink
{
    /// <summary>Set after construction so the sink can read the live balance.</summary>
    public GameData? Data { get; set; }

    public long BalanceAtEvent { get; private set; } = -1;

    public void Handle(GameEvent gameEvent)
    {
        if (gameEvent.Type == EventType.MONEYEARNED)
        {
            BalanceAtEvent = Data?.Money ?? -1;
        }
    }

    public void NotifyStatusChanged() { }
}
