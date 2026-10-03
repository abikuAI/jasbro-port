using Simbro.Core.Content;

namespace Simbro.Core.Content.Rooms;

/// <summary>
/// A tally of how many characters of each <see cref="CharacterType"/> are present, plus child/adult
/// flags. Ported from <c>jasbro.Util.TypeAmounts</c>.
/// </summary>
/// <remarks>
/// <para>
/// This exists because room requirements are not all expressible as per-character predicates.
/// <c>ChildCareRequirement</c> asks about the <b>composition</b> of the whole group — "is an infant
/// present and no child or teenager, with an adult" — which no single character can answer.
/// </para>
/// <para>
/// Java builds this by iterating the character list once in <c>Util.getTypeAmounts</c>. The port
/// exposes <see cref="From"/> to do the same, so the two never drift.
/// </para>
/// </remarks>
public sealed class TypeAmounts
{
    /// <summary>
    /// Count per character type.
    /// </summary>
    /// <remarks>
    /// <b>Every</b> <see cref="CharacterType"/> is present with an initial count of zero, matching
    /// the Java constructor's pre-population. That matters: Java's accessors do a bare
    /// <c>map.get(key)</c> with no null check, so a missing key would throw rather than return 0.
    /// Pre-populating reproduces the original's "always a number" guarantee.
    /// </remarks>
    private readonly Dictionary<CharacterType, int> _typeAmounts = new();

    private bool _childPresent;
    private bool _adultPresent;

    public TypeAmounts()
    {
        foreach (var type in Enum.GetValues<CharacterType>())
        {
            _typeAmounts[type] = 0;
        }
    }

    /// <summary>Increments the count for <paramref name="type"/>.</summary>
    public void Add(CharacterType type) => _typeAmounts[type] = _typeAmounts[type] + 1;

    public IReadOnlyDictionary<CharacterType, int> TypeAmountMap => _typeAmounts;

    public int TrainerAmount => _typeAmounts[CharacterType.TRAINER];
    public int SlaveAmount => _typeAmounts[CharacterType.SLAVE];
    public int InfantAmount => _typeAmounts[CharacterType.INFANT];
    public int ChildAmount => _typeAmounts[CharacterType.CHILD];
    public int TeenAmount => _typeAmounts[CharacterType.TEENAGER];

    public bool IsChildPresent => _childPresent;
    public void SetChildPresent(bool value) => _childPresent = value;

    public bool IsAdultPresent => _adultPresent;
    public void SetAdultPresent(bool value) => _adultPresent = value;

    /// <summary>
    /// Builds a tally from a group of characters, exactly as <c>Util.getTypeAmounts</c> does.
    /// </summary>
    /// <remarks>
    /// The child/adult split is driven by <see cref="CharacterTypeExtensions.IsChildType"/>, so
    /// INFANT, CHILD and TEENAGER set <see cref="IsChildPresent"/> while SLAVE and TRAINER set
    /// <see cref="IsAdultPresent"/>. Note that a group can have <b>neither</b> flag set — when it is
    /// empty — which several requirements treat as a distinct case.
    /// </remarks>
    public static TypeAmounts From(IEnumerable<ICharacterRequirementSubject> people)
    {
        var amounts = new TypeAmounts();
        foreach (var person in people)
        {
            amounts.Add(person.Type);
            if (person.Type.IsChildType())
            {
                amounts.SetChildPresent(true);
            }
            else
            {
                amounts.SetAdultPresent(true);
            }
        }
        return amounts;
    }
}

/// <summary>
/// The child/adult classification carried by the Java <c>CharacterType</c> enum constructor.
/// </summary>
/// <remarks>
/// In Java each constant is declared <c>INFANT(true)</c> etc. C# enums cannot carry per-constant
/// data, so the mapping lives here — which also makes the non-obvious grouping explicit, since
/// "TEENAGER is a child type" is not something the name implies.
/// </remarks>
public static class CharacterTypeExtensions
{
    /// <summary>True for INFANT, CHILD and TEENAGER; false for SLAVE and TRAINER.</summary>
    public static bool IsChildType(this CharacterType type) => type switch
    {
        CharacterType.INFANT => true,
        CharacterType.CHILD => true,
        CharacterType.TEENAGER => true,
        CharacterType.SLAVE => false,
        CharacterType.TRAINER => false,
        _ => throw new ArgumentOutOfRangeException(
            nameof(type), type, "CharacterType has no recorded child/adult classification."),
    };
}

/// <summary>
/// The parts of a character that room requirements can inspect.
/// </summary>
/// <remarks>
/// <para>
/// Java's predicates take a concrete <c>Charakter</c>, but across all eleven requirement classes
/// they touch exactly three things: <c>getType()</c>, <c>getTraits()</c> and
/// <c>getSpecializations()</c>. Narrowing to those here means the requirement logic can be written
/// and <b>verified</b> before the full character model exists — and it documents precisely what a
/// future <c>Charakter</c> must expose for rooms to work.
/// </para>
/// <para>
/// When the character model lands it should implement this interface rather than the requirements
/// being rewritten to depend on it.
/// </para>
/// </remarks>
public interface ICharacterRequirementSubject
{
    CharacterType Type { get; }
    IReadOnlyCollection<Trait> Traits { get; }
    IReadOnlyCollection<SpecializationType> Specializations { get; }
}
