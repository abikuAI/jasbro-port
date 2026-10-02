using Simbro.Core.Content;

namespace Simbro.Core.Save;

/// <summary>
/// The save-facing state of one character.
/// </summary>
/// <remarks>
/// Mirrors the fields that <c>jasbro.game.character.Charakter</c> actually persists, in the legacy
/// declaration order (which XStream writes in). Field order and names are part of the on-disk
/// contract, so do not reorder or rename casually.
///
/// <para><b>The critical property here is <see cref="BaseId"/>.</b> The legacy
/// <c>Charakter.base</c> field is <c>transient</c>, so the content definition
/// (<c>CharacterBase</c> — name, description, images, content attributes) is <b>never written to a
/// save</b>. A saved character carries its OWN copies of the mutable fields and a
/// <see cref="BaseId"/> linking back to its <c>properties.xml</c>. On load the legacy build
/// re-resolves that link by scanning the loaded content for a matching id.</para>
///
/// <para>Consequence the port must respect: <b>a save is only interpretable alongside the matching
/// content set.</b> If a character folder is removed, or its id changes, the character loads with
/// no base — silently losing its images and description.</para>
/// </remarks>
public sealed class CharacterSaveState
{
    public required string BaseId { get; init; }

    /// <summary>Legacy: <c>Charakter.name</c> — the character's own copy, not the content name.</summary>
    public required string Name { get; init; }

    public CharacterType? Type { get; set; }

    public Gender? Gender { get; set; }

    /// <summary>
    /// Legacy: <c>List&lt;Trait&gt; traits</c>.
    /// </summary>
    /// <remarks>
    /// A <b>List</b> here but a <c>Set&lt;Trait&gt;</c> on <c>CharacterBase</c> — different types
    /// with different XStream output (a list writes each element named with the enum's
    /// fully-qualified type). Do not unify them without checking both call sites.
    /// </remarks>
    public List<Trait> Traits { get; } = new();

    /// <summary>Legacy: <c>Map&lt;AttributeType, Attribute&gt; attributes</c>.</summary>
    public Dictionary<string, int> Attributes { get; } = new();

    public double Fame { get; set; }

    /// <summary>Legacy <c>Ownership</c>; only <c>OWNED</c> and <c>NOTOWNED</c> are observed.</summary>
    public string Ownership { get; set; } = "OWNED";

    public int BonusPerks { get; set; }

    public int NumberTrees { get; set; }

    /// <summary>Convenience for building fixtures and new characters.</summary>
    public CharacterSaveState WithTraits(params Trait[] traits)
    {
        Traits.AddRange(traits);
        return this;
    }
}
