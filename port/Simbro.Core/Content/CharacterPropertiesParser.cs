using System.Xml.Linq;
using Simbro.Core.State.Attributes;

namespace Simbro.Core.Content;

/// <summary>
/// Parses a character's <c>properties.xml</c>.
/// </summary>
/// <remarks>
/// Ported from <c>CharacterFileLoader.load</c> (lines 274-347) and
/// <c>loadCharacterOptimized</c> (lines 202-272) of the decompiled shipped build.
///
/// Deliberately preserved legacy behaviour (parity matters — content depends on it):
/// <list type="bullet">
///   <item><c>&lt;type&gt;</c>, <c>&lt;gender&gt;</c> and <c>&lt;initialSpecialization&gt;</c> are
///     upper-cased before parsing; <c>&lt;trait&gt;</c> values are NOT.</item>
///   <item><c>COMMAND</c> is never read from a character file.</item>
///   <item>Attribute values are stored only when strictly greater than zero.</item>
///   <item>Unknown traits and unknown image tags are skipped, not fatal.</item>
///   <item>Legacy looked attribute elements up by enum name and fell back to the
///     localised text from <c>TextUtil</c>. The translated fallback is not reproduced
///     here (it needs the language files); English content uses the enum names.</item>
/// </list>
///
/// Fixed rather than copied:
/// <list type="bullet">
///   <item>Legacy dereferenced <c>&lt;name&gt;</c> unguarded and would throw
///     <c>NullPointerException</c> on a file without one. Here that is a reported error.</item>
///   <item>Legacy swallowed malformed numbers in an empty catch. Here they become warnings.</item>
/// </list>
/// </remarks>
public static class CharacterPropertiesParser
{
    /// <summary>Legacy image-tag aliases, applied before the enum lookup.</summary>
    private static readonly Dictionary<string, ImageTag> TagAliases = new()
    {
        ["ASS"] = ImageTag.ANAL,
        ["CLOTHING"] = ImageTag.CLOTHED,
        ["STRAIGHT"] = ImageTag.VAGINAL,
        ["EATOUT"] = ImageTag.CUNNILINGUS,
    };

    /// <summary>
    /// Parse a properties.xml document.
    /// </summary>
    /// <param name="xml">The file contents.</param>
    /// <param name="folderPath">
    /// The character's folder, used to build each image's key. Legacy:
    /// <c>character.getFolder().getPath()</c>.
    /// </param>
    /// <param name="id">The character id. Legacy: folder name with any ".zip" suffix stripped.</param>
    public static CharacterParseResult Parse(string xml, string folderPath, string id)
    {
        var result = new CharacterParseResult();
        XDocument doc;
        try
        {
            doc = XDocument.Parse(xml);
        }
        catch (Exception ex)
        {
            result.Errors.Add($"Malformed XML: {ex.Message}");
            return result;
        }

        // Legacy used getElementsByTagName, which matches descendants at any depth.
        var root = doc.Root!;

        // --- name (required) -------------------------------------------------
        var name = Text(root, "name");
        if (string.IsNullOrEmpty(name))
        {
            result.Errors.Add("Missing required <name> element (legacy build threw NullPointerException here).");
            return result;
        }

        var def = new CharacterDefinition { Id = id, Name = name };

        // --- type (upper-cased; may be absent, leaving it null) --------------
        var typeText = Text(root, "type");
        if (!string.IsNullOrEmpty(typeText))
        {
            if (Enum.TryParse<CharacterType>(typeText.ToUpperInvariant(), out var ct))
            {
                def.Type = ct;
            }
            else
            {
                result.Warnings.Add($"Unknown character type '{typeText}'");
            }
        }

        // --- gender (upper-cased) -------------------------------------------
        var genderText = Text(root, "gender");
        if (!string.IsNullOrEmpty(genderText))
        {
            if (Enum.TryParse<Gender>(genderText.ToUpperInvariant(), out var g))
            {
                def.Gender = g;
            }
            else
            {
                result.Warnings.Add($"Unknown gender '{genderText}'");
            }
        }

        // --- base attributes (COMMAND excluded; only values > 0) ------------
        foreach (var attr in Enum.GetValues<BaseAttributeTypes>())
        {
            if (attr == BaseAttributeTypes.COMMAND)
            {
                continue;   // legacy explicitly skips COMMAND
            }

            var raw = Text(root, attr.ToString());
            if (raw is null)
            {
                continue;
            }

            if (int.TryParse(raw.Trim(), out var value))
            {
                if (value > 0)
                {
                    def.Attributes[attr] = value;
                }
            }
            else
            {
                // Legacy swallowed this in an empty catch.
                result.Warnings.Add($"Unparseable value '{raw}' for attribute {attr}");
            }
        }

        // --- traits (NOT upper-cased; unknown ones skipped) ------------------
        foreach (var el in root.Descendants("trait"))
        {
            var text = el.Value;
            if (Enum.TryParse<Trait>(text, out var t))
            {
                def.Traits.Add(t);
            }
            else
            {
                def.UnknownTraits.Add(text);
                result.Warnings.Add($"Unknown trait '{text}'");
            }
        }

        // --- optional scalars ------------------------------------------------
        var desc = Text(root, "description");
        if (desc is not null)
        {
            def.Description = desc;
        }

        var spec = Text(root, "initialSpecialization");
        if (!string.IsNullOrEmpty(spec))
        {
            if (Enum.TryParse<SpecializationType>(spec.ToUpperInvariant(), out var st))
            {
                def.InitialSpecialization = st;
            }
            else
            {
                result.Warnings.Add($"Unknown specialization '{spec}'");
            }
        }

        def.YoungerBase = Text(root, "youngerBase");
        def.OlderBase = Text(root, "olderBase");

        // --- images -----------------------------------------------------------
        foreach (var el in root.Descendants("image"))
        {
            var raw = el.Attribute("name")?.Value;
            if (raw is null)
            {
                continue;
            }

            var filename = raw.Replace('\\', '/');
            var entry = new ImageEntry
            {
                Key = folderPath + Path.DirectorySeparatorChar + filename,
                Filename = filename,
            };

            var tags = el.Descendants("tag").ToList();

            foreach (var tagEl in tags)
            {
                var tagText = tagEl.Value;
                if (TagAliases.TryGetValue(tagText, out var alias))
                {
                    // AddTag deduplicates, matching the legacy Set<ImageTag>.
                    entry.AddTag(alias);
                }
                else if (Enum.TryParse<ImageTag>(tagText, out var tag))
                {
                    entry.AddTag(tag);
                }
                else
                {
                    result.Warnings.Add($"Unknown image tag '{tagText}' on {filename}");
                }
            }

            // Legacy read <customtext> INSIDE the per-tag loop, so an image with no
            // tags never had its custom text read. Reproduced here for parity; see
            // the parser's remarks. (This is a legacy defect, not intended behaviour.)
            if (tags.Count > 0)
            {
                var custom = el.Descendants("customtext").FirstOrDefault();
                if (custom is not null)
                {
                    entry.CustomText = custom.Value;
                }
            }

            def.Images.Add(entry);
        }

        // Legacy: Collections.sort(character.getImages(), new Comparators.ImageDataComparator()).
        //
        // The comparator is o1.getFilename().compareTo(o2.getFilename()) — a plain lexicographic
        // comparison of the FILENAME (not the full key). Verified against the real loader: the
        // "Toddler" character is the one whose file order differs from its sorted order, which is
        // how this was found.
        //
        // MUST be ordinal. Java's String.compareTo compares UTF-16 code units; C#'s default
        // string comparison IS culture-sensitive and would order non-ASCII filenames differently
        // (and inconsistently with the shipped game).
        def.Images.Sort((a, b) => string.CompareOrdinal(a.Filename, b.Filename));

        result.Definition = def;
        return result;
    }

    /// <summary>First descendant text by local name, or null. Mirrors getElementsByTagName(...).item(0).</summary>
    private static string? Text(XElement root, string localName) =>
        root.Descendants(localName).FirstOrDefault()?.Value;
}
