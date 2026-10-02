namespace Simbro.Core.Content;

/// <summary>A single image declared in a character's <c>properties.xml</c>.</summary>
/// <remarks>
/// Legacy <c>ImageData</c> as produced by <c>CharacterFileLoader.loadCharacterOptimized</c>.
/// The <see cref="Key"/> is the folder-qualified path — it is the identity used by
/// image lookups and caches, so it must be constructed exactly as the legacy build
/// does (folder path + separator + filename, with backslashes normalised to '/').
///
/// <para><b>Tags are a SET, not a list.</b> Legacy declares
/// <c>private Set&lt;ImageTag&gt; tags = new HashSet&lt;&gt;()</c> and <c>addTag</c> guards with
/// <c>contains</c> first, so a repeated <c>&lt;tag&gt;</c> collapses to one. No shipped content
/// actually repeats a tag (all 506 images were checked), so this is latent — but it is real
/// semantics for user-authored content.</para>
///
/// <para>Consequently <b>tag order is not part of the content contract</b>. Because the legacy
/// field is a <c>HashSet</c>, iteration follows the JVM's hash layout for the enum constants:
/// stable on a given JVM, guaranteed nowhere, and invisible to content authors. The port keeps
/// insertion order purely for reproducible output; nothing may depend on it. This was verified
/// against the real loader rather than assumed.</para>
/// </remarks>
public sealed class ImageEntry
{
    public required string Key { get; init; }

    public required string Filename { get; init; }

    /// <summary>Tags, deduplicated. Insertion order is preserved for reproducible output only.</summary>
    public List<ImageTag> Tags { get; } = new();

    /// <summary>Legacy <c>ImageData.addTag</c> — adds only if absent.</summary>
    public void AddTag(ImageTag tag)
    {
        if (!Tags.Contains(tag))
        {
            Tags.Add(tag);
        }
    }

    /// <summary>Legacy <c>ImageData.customText</c> — optional per-image override text.</summary>
    public string? CustomText { get; set; }
}

/// <summary>
/// A character as defined by a content <c>properties.xml</c>.
/// </summary>
/// <remarks>
/// This is the content-facing definition, not the runtime character. The legacy
/// build parses into a <c>CharacterBase</c> which is then combined with per-save
/// state to form a <c>Charakter</c>.
/// </remarks>
public sealed class CharacterDefinition
{
    /// <summary>Legacy: <c>CharacterBase.id</c> — the folder name with any ".zip" suffix stripped.</summary>
    public required string Id { get; init; }

    /// <summary>Legacy: <c>name</c>. Required — the legacy loader throws if absent.</summary>
    public required string Name { get; init; }

    /// <summary>
    /// Legacy allows this to be <c>null</c> when <c>&lt;type&gt;</c> is missing or empty.
    /// </summary>
    public CharacterType? Type { get; set; }

    /// <summary>Legacy leaves this null when <c>&lt;gender&gt;</c> is missing or empty.</summary>
    public Gender? Gender { get; set; }

    public SpecializationType? InitialSpecialization { get; set; }

    public string? Description { get; set; }

    /// <summary>Legacy <c>youngerBase</c> / <c>olderBase</c> — used for child ageing.</summary>
    public string? YoungerBase { get; set; }

    public string? OlderBase { get; set; }

    /// <summary>
    /// Base attributes. Keyed by the legacy name.
    /// </summary>
    /// <remarks>
    /// Note: <c>COMMAND</c> is deliberately absent — the legacy loader skips it
    /// (<c>if (attribute != BaseAttributeTypes.COMMAND)</c>), and only values
    /// strictly greater than zero are stored.
    /// </remarks>
    public Dictionary<State.Attributes.BaseAttributeTypes, int> Attributes { get; } = new();

    /// <summary>
    /// Traits. Unknown trait names are skipped, mirroring the legacy try/catch.
    /// </summary>
    public List<Trait> Traits { get; } = new();

    /// <summary>Traits that failed to parse, kept for diagnostics rather than silently dropped.</summary>
    public List<string> UnknownTraits { get; } = new();

    public List<ImageEntry> Images { get; } = new();
}

/// <summary>Outcome of parsing a character file.</summary>
public sealed class CharacterParseResult
{
    /// <summary>The parsed definition, or null if parsing failed fatally.</summary>
    public CharacterDefinition? Definition { get; set; }

    /// <summary>Non-fatal problems (unknown traits, unparseable numbers, unknown tags).</summary>
    public List<string> Warnings { get; } = new();

    /// <summary>Fatal problems that produced no definition at all.</summary>
    public List<string> Errors { get; } = new();

    public bool Success => Definition is not null && Errors.Count == 0;
}
