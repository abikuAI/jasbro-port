// No namespace: Program.cs uses top-level statements, which compile into the global namespace.
// Declaring one here would hide these types from it.

/// <summary>
/// One character as recorded in the golden content fixture.
/// </summary>
public sealed class GoldenCharacter
{
    public required string Id { get; init; }
    public required string Name { get; init; }
    public string? Type { get; init; }
    public string? Gender { get; init; }
    public string? Spec { get; init; }
    public string? Description { get; init; }
    public List<string> Attributes { get; init; } = new();
    public List<string> Traits { get; init; } = new();
    public int ImageCount { get; init; }
    public List<GoldenImage> Images { get; init; } = new();
}

public sealed class GoldenImage
{
    public required string Filename { get; init; }
    public List<string> Tags { get; init; } = new();
    public string? CustomText { get; init; }
}

/// <summary>
/// Parses <c>fixtures/content-golden.txt</c>, the record of what the SHIPPED Java game's
/// <c>CharacterFileLoader</c> actually read out of the real <c>characters/</c> folder.
/// </summary>
/// <remarks>
/// Deliberately a tiny hand-rolled reader rather than a serialiser: the fixture is a
/// human-readable diff artifact first, and making it round-trippable through a library would
/// add a dependency and hide the format. The format is fixed by
/// <c>fixtures/java/ContentFixture.java</c>.
///
/// <para>The literal string <c>null</c> means Java null — the loader writes
/// <c>"name=" + value</c>, so an absent field is indistinguishable from a null one, which is
/// precisely the distinction that matters for <c>&lt;type&gt;</c>.</para>
/// </remarks>
public static class ContentGolden
{
    public static List<GoldenCharacter> Parse(IEnumerable<string> lines)
    {
        var result = new List<GoldenCharacter>();
        GoldenCharacter? current = null;

        // Builder state — GoldenCharacter is immutable after construction.
        string id = "", name = "";
        string? type = null, gender = null, spec = null, description = null;
        var attrs = new List<string>();
        var traits = new List<string>();
        var images = new List<GoldenImage>();
        var imageCount = 0;

        void Flush()
        {
            if (current is null) return;
            result.Add(new GoldenCharacter
            {
                Id = id,
                Name = name,
                Type = type,
                Gender = gender,
                Spec = spec,
                Description = description,
                Attributes = attrs,
                Traits = traits,
                ImageCount = imageCount,
                Images = images,
            });
        }

        foreach (var raw in lines)
        {
            var line = raw.TrimEnd();
            if (line.Length == 0 || line.StartsWith("CWD=") || line.StartsWith("COUNT="))
            {
                continue;
            }

            if (line.StartsWith("CHAR id="))
            {
                Flush();
                id = line["CHAR id=".Length..];
                name = ""; type = null; gender = null; spec = null; description = null;
                attrs = new List<string>();
                traits = new List<string>();
                images = new List<GoldenImage>();
                imageCount = 0;
                current = new GoldenCharacter { Id = id, Name = name };
                continue;
            }

            if (current is null) continue;

            var trimmed = line.TrimStart();

            if (trimmed.StartsWith("IMG "))
            {
                var rest = trimmed[4..];
                var fnStart = rest.IndexOf("filename=", StringComparison.Ordinal);
                var tagsStart = rest.IndexOf(" tags=", StringComparison.Ordinal);
                var ctStart = rest.IndexOf(" customText=", StringComparison.Ordinal);
                if (fnStart < 0) continue;

                var filename = rest[(fnStart + 9)..(tagsStart >= 0 ? tagsStart : rest.Length)];
                var tagText = tagsStart >= 0
                    ? rest[(tagsStart + 6)..(ctStart >= 0 ? ctStart : rest.Length)]
                    : "";
                var customText = ctStart >= 0 ? rest[(ctStart + 12)..] : null;

                images.Add(new GoldenImage
                {
                    Filename = filename,
                    Tags = tagText.Split(',', StringSplitOptions.RemoveEmptyEntries).ToList(),
                    CustomText = customText == "null" ? null : customText,
                });
                continue;
            }

            var eq = trimmed.IndexOf('=');
            if (eq < 0) continue;
            var key = trimmed[..eq];
            var value = trimmed[(eq + 1)..];

            switch (key)
            {
                case "name": name = value; break;
                case "type": type = value == "null" ? null : value; break;
                case "gender": gender = value == "null" ? null : value; break;
                case "spec": spec = value == "null" ? null : value; break;
                case "description": description = value == "null" ? null : value; break;
                case "attributes":
                    attrs = value.Split(' ', StringSplitOptions.RemoveEmptyEntries).ToList();
                    break;
                case "traits":
                    traits = value.Split(' ', StringSplitOptions.RemoveEmptyEntries).ToList();
                    break;
                case "images":
                    int.TryParse(value, out imageCount);
                    break;
            }
        }

        Flush();
        return result;
    }
}
