namespace Simbro.Core.State.Attributes;

/// <summary>Which family an attribute belongs to. Legacy: the three implementations of <c>AttributeType</c>.</summary>
public enum AttributeKind
{
    /// <summary>Legacy <c>BaseAttributeTypes</c> — the core trainable stats.</summary>
    Base,

    /// <summary>Legacy <c>CalculatedAttribute</c> — derived combat/utility values.</summary>
    Calculated,

    /// <summary>Legacy <c>SpecializationAttribute</c> — job/activity skills.</summary>
    Specialization,
}

/// <summary>
/// Legacy <c>jasbro.game.character.attributes.BaseAttributeTypes</c> — 6 values.
/// </summary>
/// <remarks>Defaults: min 0, max 20, raiseMaxBy 10, startValue 1.</remarks>
public enum BaseAttributeTypes
{
    CHARISMA,
    OBEDIENCE,
    COMMAND,
    STAMINA,
    INTELLIGENCE,
    STRENGTH,
}

/// <summary>
/// Legacy <c>jasbro.game.character.attributes.CalculatedAttribute</c> — 30 values.
/// </summary>
/// <remarks>Defaults: min 0, max int.MaxValue, raiseMaxBy 0, startValue 0.</remarks>
public enum CalculatedAttribute
{
    DAMAGE,
    ARMORVALUE,
    ARMORPERCENT,
    HIT,
    DODGE,
    SPEED,
    BLOCKCHANCE,
    BLOCKAMOUNT,
    CRITCHANCE,
    CRITDAMAGEAMOUNT,
    SKILLPOINTS,
    AMOUNTCUSTOMERSPERSHIFT,
    ITEMLOOTCHANCEMODIFIER,
    STEALCHANCE,
    STEALAMOUNTMODIFIER,
    STEALITEMCHANCE,
    FIRERESISTANCE,
    WATERRESISTANCE,
    WINDRESISTANCE,
    EARTHRESISTANCE,
    LIGHTNINGRESISTANCE,
    MAGICRESISTANCE,
    HOLYRESISTANCE,
    DARKNESSRESISTANCE,
    PREGNANCYCHANCE,
    MINCHILDREN,
    MAXCHILDREN,
    CHANCEADDITIONALCHILD,
    PREGNANCYDURATIONMODIFIER,
    CONTROL,
}

/// <summary>
/// Legacy <c>jasbro.game.character.specialization.SpecializationAttribute</c> — 17 values.
/// </summary>
/// <remarks>Defaults: min 0, max 20, raiseMaxBy 10, startValue 0.</remarks>
public enum SpecializationAttribute
{
    COOKING,
    CLEANING,
    SEDUCTION,
    EXPERIENCE,
    VETERAN,
    BARTENDING,
    PICKPOCKETING,
    CATGIRL,
    AGILITY,
    MEDICALKNOWLEDGE,
    MAGIC,
    STRIP,
    DOMINATE,
    PLANTKNOWLEDGE,
    ADVERTISING,
    TRANSFORMATION,
    GENETICADAPTABILITY,
}

/// <summary>
/// A type-erased attribute reference usable as a map key.
/// </summary>
/// <remarks>
/// The legacy game keys attribute maps on the <c>AttributeType</c> interface, so
/// attributes of all three families share one namespace. This is the port's
/// equivalent. Serialised as <c>"Kind:NAME"</c> — the legacy format stores bare
/// names in some places, so the importer maps those by name lookup.
/// </remarks>
public readonly record struct AttributeKey(AttributeKind Kind, string Name)
{
    public override string ToString() => $"{Kind}:{Name}";

    public static AttributeKey Base(BaseAttributeTypes t) => new(AttributeKind.Base, t.ToString());
    public static AttributeKey Calculated(CalculatedAttribute t) => new(AttributeKind.Calculated, t.ToString());
    public static AttributeKey Specialization(SpecializationAttribute t) => new(AttributeKind.Specialization, t.ToString());
}

/// <summary>Attribute metadata and enumeration. Legacy: the <c>AttributeType</c> interface methods.</summary>
public static class AttributeTypes
{
    /// <summary>Legacy <c>BaseAttributeTypes.getDefaultMin/Max/RaiseMaxBy/StartValue</c>.</summary>
    public static (int Min, int Max, int RaiseMaxBy, int StartValue) Meta(BaseAttributeTypes _) => (0, 20, 10, 1);

    /// <summary>Legacy <c>CalculatedAttribute.*</c> — max is int.MaxValue, no raising.</summary>
    public static (int Min, int Max, int RaiseMaxBy, int StartValue) Meta(CalculatedAttribute _) =>
        (0, int.MaxValue, 0, 0);

    /// <summary>Legacy <c>SpecializationAttribute.*</c> — max 20 for every value.</summary>
    public static (int Min, int Max, int RaiseMaxBy, int StartValue) Meta(SpecializationAttribute _) => (0, 20, 10, 0);

    public static IEnumerable<AttributeKey> All =>
        Enum.GetValues<BaseAttributeTypes>().Select(AttributeKey.Base)
            .Concat(Enum.GetValues<CalculatedAttribute>().Select(AttributeKey.Calculated))
            .Concat(Enum.GetValues<SpecializationAttribute>().Select(AttributeKey.Specialization));

    /// <summary>
    /// Resolve a bare attribute name (as it appears in content XML) to a key.
    /// </summary>
    /// <remarks>
    /// Content references attributes by bare name — e.g. a character properties.xml
    /// uses <c>&lt;CHARISMA&gt;</c> and event XML uses <c>attributeType</c> values.
    /// Returns false rather than throwing, because unguarded <c>valueOf</c> on
    /// content-supplied strings is a confirmed crash class in the legacy build.
    /// </remarks>
    public static bool TryParse(string name, out AttributeKey key)
    {
        if (Enum.TryParse<BaseAttributeTypes>(name, ignoreCase: false, out var b))
        {
            key = AttributeKey.Base(b);
            return true;
        }

        if (Enum.TryParse<CalculatedAttribute>(name, ignoreCase: false, out var c))
        {
            key = AttributeKey.Calculated(c);
            return true;
        }

        if (Enum.TryParse<SpecializationAttribute>(name, ignoreCase: false, out var s))
        {
            key = AttributeKey.Specialization(s);
            return true;
        }

        key = default;
        return false;
    }
}
