// NOTE: deliberately NO namespace declaration.
//
// Program.cs uses top-level statements, which places its code in the GLOBAL namespace. A type
// declared inside `namespace Simbro.Verify` would not be visible to it without a using directive,
// and adding one would be papering over the mismatch. ContentGolden.cs next door has the same
// shape for the same reason.

/// <summary>
/// One room as recorded in <c>fixtures/rooms-golden.txt</c>.
/// </summary>
internal sealed class RoomsGoldenEntry
{
    public string Id { get; set; } = "";
    public int Cost { get; set; }
    public int MaxOccupancy { get; set; }
    public string Image { get; set; } = "";
    public string Slots { get; set; } = "";
    public string Activities { get; set; } = "";

    /// <summary>Activity requirements keyed by activity name, e.g. <c>SEX = exact-occupant(2)</c>.</summary>
    public Dictionary<string, string> Requirements { get; } = new(StringComparer.Ordinal);

    /// <summary>Child-care requirements, kept separate because Java keeps them in a separate map.</summary>
    public Dictionary<string, string> ChildRequirements { get; } = new(StringComparer.Ordinal);
}

/// <summary>
/// Parses <c>rooms-golden.txt</c>, the output of <c>fixtures/java/RoomsFixture.java</c>.
/// </summary>
/// <remarks>
/// Deliberately a hand-rolled line parser rather than a real format. The file is a flat dump whose
/// whole purpose is to be trivially diffable, and a tolerant parser would hide drift in the dump
/// itself.
/// </remarks>
internal static class RoomsGolden
{
    public static List<RoomsGoldenEntry> Parse(string[] lines)
    {
        var rooms = new List<RoomsGoldenEntry>();
        RoomsGoldenEntry? current = null;

        foreach (var raw in lines)
        {
            var line = raw.TrimEnd();
            if (line.Length == 0) continue;
            if (line.StartsWith("ROOMS=", StringComparison.Ordinal)) continue;

            if (line.StartsWith("ROOM ", StringComparison.Ordinal))
            {
                current = new RoomsGoldenEntry { Id = line[5..].Trim() };
                rooms.Add(current);
                continue;
            }

            if (current is null) continue;

            var trimmed = line.Trim();
            var eq = trimmed.IndexOf('=');
            if (eq < 0) continue;
            var key = trimmed[..eq].Trim();
            var value = trimmed[(eq + 1)..].Trim();

            switch (key)
            {
                case "cost":
                    current.Cost = ParseIntOrZero(value);
                    break;
                case "maxOccupancy":
                    current.MaxOccupancy = ParseIntOrZero(value);
                    break;
                case "image":
                    current.Image = value;
                    break;
                case "slots":
                    current.Slots = value;
                    break;
                case "activities":
                    current.Activities = value;
                    break;
                default:
                    if (key.StartsWith("childreq ", StringComparison.Ordinal))
                    {
                        current.ChildRequirements[key[9..].Trim()] = value;
                    }
                    else if (key.StartsWith("req ", StringComparison.Ordinal))
                    {
                        // Checked AFTER childreq, because "childreq " does not start with "req " so
                        // there is no ambiguity - but ordering it this way keeps that explicit.
                        current.Requirements[key[4..].Trim()] = value;
                    }
                    break;
            }
        }

        return rooms;
    }

    /// <summary>
    /// Parses an integer, yielding 0 when the dump is malformed.
    /// </summary>
    /// <remarks>
    /// Returning 0 rather than throwing is safe here: the caller compares this value against the
    /// C# loader's result, so a malformed dump shows up as a mismatch on that room instead of as an
    /// unhandled exception that hides every other result.
    /// </remarks>
    private static int ParseIntOrZero(string s)
        => int.TryParse(s, System.Globalization.NumberStyles.AllowLeadingSign,
                        System.Globalization.CultureInfo.InvariantCulture, out var v) ? v : 0;
}
