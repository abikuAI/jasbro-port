using Simbro.Core.Content.Rooms;

namespace Simbro.Core.Content.Rooms;

/// <summary>
/// Decides whether a set of characters may perform an activity in a room.
/// Ported from <c>jasbro.game.character.activities.requirements.ActivityRequirement</c>.
/// </summary>
/// <remarks>
/// This file models the <b>structure</b> the parser produces, which is what <c>rooms.xml</c>
/// describes. The <c>IsValid</c> predicates depend on character state and the game's
/// <c>Util.TypeAmounts</c> tally, and are deliberately not implemented yet — see the note at the
/// bottom of this file.
/// </remarks>
public abstract class ActivityRequirement
{
    /// <summary>
    /// Compact, stable rendering used by content-parity tests.
    /// </summary>
    /// <remarks>
    /// Deliberately human-readable and deterministic: tests compare these strings against a golden
    /// dump taken from the real loader, so any formatting drift should look like an obvious diff
    /// rather than a silent mismatch. Do not reformat without regenerating the golden file.
    /// </remarks>
    public abstract string Describe();
}

/// <summary>A character-level predicate, used by <see cref="MinimumCharacterRequirement"/> and <see cref="AllCharacterRequirement"/>.</summary>
public abstract class CharacterRequirement
{
    public abstract string Describe();
}

// ---------------------------------------------------------------------------------------------
// Activity requirements
// ---------------------------------------------------------------------------------------------

/// <summary>`type="none"` — always satisfiable. The most common requirement (49 uses).</summary>
public sealed class NoActivityRequirement : ActivityRequirement
{
    public override string Describe() => "none";
}

/// <summary>`type="min-occupant" count="N"` — at least N characters.</summary>
public sealed class MinimumOccupantRequirement : ActivityRequirement
{
    public int Count { get; }
    public MinimumOccupantRequirement(int count) => Count = count;
    public override string Describe() => $"min-occupant({Count})";
}

/// <summary>`type="max-occupant" count="N"` — at most N characters.</summary>
public sealed class MaximumOccupantRequirement : ActivityRequirement
{
    public int Count { get; }
    public MaximumOccupantRequirement(int count) => Count = count;
    public override string Describe() => $"max-occupant({Count})";
}

/// <summary>`type="exact-occupant" count="N"` — exactly N characters.</summary>
public sealed class ExactOccupantRequirement : ActivityRequirement
{
    public int Count { get; }
    public ExactOccupantRequirement(int count) => Count = count;
    public override string Describe() => $"exact-occupant({Count})";
}

/// <summary>
/// `type="min-character" count="N"` wrapping a character requirement — at least N characters
/// matching the inner predicate. The second most common requirement (83 uses).
/// </summary>
public sealed class MinimumCharacterRequirement : ActivityRequirement
{
    public CharacterRequirement Requirement { get; }
    public int Count { get; }

    public MinimumCharacterRequirement(CharacterRequirement requirement, int count)
    {
        Requirement = requirement;
        Count = count;
    }

    public override string Describe() => $"min-character({Count}, {Requirement.Describe()})";
}

/// <summary>`type="all-character"` — every character must match the inner predicate (21 uses).</summary>
public sealed class AllCharacterRequirement : ActivityRequirement
{
    public CharacterRequirement Requirement { get; }
    public AllCharacterRequirement(CharacterRequirement requirement) => Requirement = requirement;
    public override string Describe() => $"all-character({Requirement.Describe()})";
}

/// <summary>`type="and"` — every child must hold. The most common requirement (89 uses).</summary>
public sealed class AndActivityRequirement : ActivityRequirement
{
    public IReadOnlyList<ActivityRequirement> Requirements { get; }
    public AndActivityRequirement(IReadOnlyList<ActivityRequirement> requirements) => Requirements = requirements;
    public override string Describe() => $"and[{string.Join(", ", Requirements.Select(r => r.Describe()))}]";
}

/// <summary>
/// `type="child-care"` — takes no attributes; the predicate is a marker (14 uses).
/// </summary>
public sealed class ChildCareRequirement : ActivityRequirement
{
    public override string Describe() => "child-care";
}

// ---------------------------------------------------------------------------------------------
// Character requirements
// ---------------------------------------------------------------------------------------------

/// <summary>`char-requirement type="trait" trait="X"` — the dominant character gate (53 uses).</summary>
public sealed class TraitRequirement : CharacterRequirement
{
    public Trait Trait { get; }
    public TraitRequirement(Trait trait) => Trait = trait;
    public override string Describe() => $"trait({Trait})";
}

/// <summary>`char-requirement type="specialization" specialization="X"` (28 uses).</summary>
public sealed class SpecializationRequirement : CharacterRequirement
{
    public SpecializationType Specialization { get; }
    public SpecializationRequirement(SpecializationType specialization) => Specialization = specialization;
    public override string Describe() => $"specialization({Specialization})";
}

/// <summary>
/// `char-requirement type="char-type" char-type="X"` (24 uses).
/// </summary>
/// <remarks>
/// <b>Reads the <c>char-type</c> attribute, not <c>type</c>.</b> <c>type</c> has already been
/// consumed for dispatch, which is why the value lives under a second, differently-named attribute.
/// </remarks>
public sealed class CharacterTypeRequirement : CharacterRequirement
{
    public CharacterType CharacterType { get; }
    public CharacterTypeRequirement(CharacterType characterType) => CharacterType = characterType;
    public override string Describe() => $"char-type({CharacterType})";
}

/// <summary>`char-requirement type="or"` — any child suffices. Used just twice in all content.</summary>
public sealed class OrCharacterRequirement : CharacterRequirement
{
    public IReadOnlyList<CharacterRequirement> Requirements { get; }
    public OrCharacterRequirement(IReadOnlyList<CharacterRequirement> requirements) => Requirements = requirements;
    public override string Describe() => $"or[{string.Join(", ", Requirements.Select(r => r.Describe()))}]";
}

// ---------------------------------------------------------------------------------------------
// Not yet ported
// ---------------------------------------------------------------------------------------------
//
// The IsValid(...) predicates on the above are intentionally absent. They consume
// (ActivityType, List<Charakter>, Util.TypeAmounts), and TypeAmounts is a tally the game computes
// elsewhere. Adding the predicates before the character model is complete would mean inventing a
// TypeAmounts shape from guesswork. Parsing is the part that can be verified right now, against the
// real rooms.xml, so it goes first.
