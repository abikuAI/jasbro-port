// Godot's SDK does not enable implicit usings, so System and System.Linq are explicit.
using System;
using System.Linq;
using Godot;
using Simbro.Core.Content;
using Simbro.Core.Save;
using Simbro.Core.State;

namespace Simbro.Godot;

/// <summary>
/// Proof-of-integration scene for the port.
/// </summary>
/// <remarks>
/// This node does no presentation work yet. Its single job is to demonstrate — and let anyone
/// re-verify in seconds — that the Godot layer can drive the domain assembly:
///
/// <list type="bullet">
///   <item>construct and mutate <see cref="GameData"/> (the legacy save root);</item>
///   <item>build a character;</item>
///   <item>serialise it with <see cref="SaveFileWriter"/> — the writer the SHIPPED Java game has
///     been proven to load;</item>
///   <item>read it back with <see cref="SaveFileReader"/>.</item>
/// </list>
///
/// If this runs, the whole chain works: Godot → Simbro.Core → XStream-compatible save.
/// All of that logic lives outside Godot, so it is equally verifiable from a plain console run.
/// </remarks>
public partial class Main : Control
{
    public override void _Ready()
    {
        var report = new System.Text.StringBuilder();

        // --- the domain: no Godot types involved ---------------------------------
        var data = new GameData();
        data.Day = 1;
        data.SpendMoney(120);            // 500 -> 380

        var loli = new CharacterSaveState
        {
            BaseId = "Loli",
            Name = "Loli",
            Type = CharacterType.CHILD,
            Gender = Gender.FEMALE,
        }.WithTraits(Trait.LOLI, Trait.FRAGILE, Trait.LOYAL);

        var xml = SaveFileWriter.WriteGameData(data, new[] { loli });

        report.AppendLine("Simbro.Core reached from Godot.");
        report.AppendLine($"  day={data.Day}  time={data.Time}  money={data.Money}");
        report.AppendLine($"  save bytes={xml.Length}");
        report.AppendLine($"  XStream declaration: {xml.StartsWith(SaveFileWriter.Declaration)}");
        report.AppendLine($"  never self-closes:   {!xml.Contains("/>")}");

        // --- read it back ---------------------------------------------------------
        var doc = SaveFileReader.Parse(xml);
        var character = doc.Root.Child("characters")?.Children.FirstOrDefault();

        report.AppendLine($"  round-tripped day:   {doc.Root.ChildInt("day")}");
        report.AppendLine($"  round-tripped money: {doc.Root.ChildLong("money")}");
        report.AppendLine($"  character name:      {character?.ChildText("name")}");
        report.AppendLine($"  character baseId:    {character?.ChildText("baseId")}");
        report.AppendLine($"  traits:              {string.Join(", ", character?.Child("traits")?.Children.Select(t => t.Text) ?? Array.Empty<string>())}");

        // --- content parity, over the real content folder -------------------------
        var contentDir = @"C:\Games\Jasbro_Final\characters";
        if (System.IO.Directory.Exists(contentDir))
        {
            var props = System.IO.Path.Combine(contentDir, "Loli", "properties.xml");
            if (System.IO.File.Exists(props))
            {
                var parsed = CharacterPropertiesParser.Parse(
                    System.IO.File.ReadAllText(props),
                    System.IO.Path.Combine(contentDir, "Loli"),
                    "Loli");
                report.AppendLine($"  parsed real Loli:    {parsed.Definition?.Images.Count} images, " +
                                  $"{parsed.Definition?.Traits.Count} traits");
            }
        }

        GD.Print(report.ToString());

        // Visible confirmation when run with a window.
        var label = new Label
        {
            Text = report.ToString(),
            Position = new Vector2(20, 20),
        };
        AddChild(label);
    }
}
