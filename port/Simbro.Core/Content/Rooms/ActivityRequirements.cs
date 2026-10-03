using Simbro.Core.Content.Rooms;

namespace Simbro.Core.Content.Rooms;

/// <summary>
/// Decides whether a set of characters may perform an activity in a room.
/// Ported from <c>jasbro.game.character.activities.requirements.ActivityRequirement</c>.
/// </summary>
public abstract class ActivityRequirement
{
    /// <summary>
    /// True when the group may perform <paramref name="activity"/> in this room.
    /// </summary>
    /// <remarks>
    /// <paramref name="activity"/> is threaded through the whole tree but <b>no</b> requirement in
    /// the shipped game reads it — every implementation ignores it. It is kept because the
    /// interface declares it and future requirement types may use it.
    /// </remarks>
    public abstract bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts);

    /// <summary>Compact, stable rendering used by content-parity tests.</summary>
    public abstract string Describe();
}

/// <summary>A per-character predicate, used by <see cref="MinimumCharacterRequirement"/> and <see cref="AllCharacterRequirement"/>.</summary>
public abstract class CharacterRequirement
{
    public abstract bool IsValid(ActivityType activity, ICharacterRequirementSubject character);
    public abstract string Describe();
}

// ---------------------------------------------------------------------------------------------
// Activity requirements
// ---------------------------------------------------------------------------------------------

/// <summary>`type="none"` — always satisfiable. The most common requirement (49 uses).</summary>
public sealed class NoActivityRequirement : ActivityRequirement
{
    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts) => true;
    public override string Describe() => "none";
}

/// <summary>`type="min-occupant" count="N"` — at least N characters.</summary>
public sealed class MinimumOccupantRequirement : ActivityRequirement
{
    public int Count { get; }
    public MinimumOccupantRequirement(int count) => Count = count;

    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
        => characters.Count >= Count;

    public override string Describe() => $"min-occupant({Count})";
}

/// <summary>`type="max-occupant" count="N"` — at most N characters.</summary>
public sealed class MaximumOccupantRequirement : ActivityRequirement
{
    public int Count { get; }
    public MaximumOccupantRequirement(int count) => Count = count;

    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
        => characters.Count <= Count;

    public override string Describe() => $"max-occupant({Count})";
}

/// <summary>`type="exact-occupant" count="N"` — exactly N characters.</summary>
public sealed class ExactOccupantRequirement : ActivityRequirement
{
    public int Count { get; }
    public ExactOccupantRequirement(int count) => Count = count;

    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
        => characters.Count == Count;

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

    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
    {
        var matching = 0;
        foreach (var c in characters)
        {
            if (Requirement.IsValid(activity, c))
            {
                matching++;
            }
        }
        return matching >= Count;
    }

    public override string Describe() => $"min-character({Count}, {Requirement.Describe()})";
}

/// <summary>
/// `type="all-character"` — every character must match the inner predicate (21 uses).
/// </summary>
/// <remarks>
/// <b>Vacuously true for an empty group</b> — the loop never runs, so it returns true. That is the
/// original's behaviour and is preserved deliberately: an empty group therefore satisfies
/// `all-character(anything)`, and whether that is reachable depends on the calling code, not on
/// this predicate.
/// </remarks>
public sealed class AllCharacterRequirement : ActivityRequirement
{
    public CharacterRequirement Requirement { get; }
    public AllCharacterRequirement(CharacterRequirement requirement) => Requirement = requirement;

    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
    {
        foreach (var c in characters)
        {
            if (!Requirement.IsValid(activity, c))
            {
                return false;
            }
        }
        return true;
    }

    public override string Describe() => $"all-character({Requirement.Describe()})";
}

/// <summary>`type="and"` — every child must hold. The most common requirement (89 uses).</summary>
/// <remarks>Like <see cref="AllCharacterRequirement"/>, vacuously true when it has no children.</remarks>
public sealed class AndActivityRequirement : ActivityRequirement
{
    public IReadOnlyList<ActivityRequirement> Requirements { get; }
    public AndActivityRequirement(IReadOnlyList<ActivityRequirement> requirements) => Requirements = requirements;

    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
    {
        foreach (var r in Requirements)
        {
            if (!r.IsValid(activity, characters, typeAmounts))
            {
                return false;
            }
        }
        return true;
    }

    public override string Describe() => $"and[{string.Join(", ", Requirements.Select(r => r.Describe()))}]";
}

/// <summary>
/// `type="child-care"` — a <b>group composition</b> gate, not a per-character one (14 uses).
/// </summary>
/// <remarks>
/// <para>
/// The rule: if any infant is present, then no child and no teenager may be, and an adult must be.
/// If no infant is present, it passes unconditionally — so this only ever <i>restricts</i> groups
/// that contain an infant.
/// </para>
/// <para>
/// Preserved exactly as written in the original, including the fact that <c>isAdultPresent</c> is
/// only consulted on the infant-present branch.
/// </para>
/// </remarks>
public sealed class ChildCareRequirement : ActivityRequirement
{
    public override bool IsValid(ActivityType activity, IReadOnlyList<ICharacterRequirementSubject> characters, TypeAmounts typeAmounts)
        => typeAmounts.InfantAmount > 0
            ? typeAmounts.ChildAmount == 0 && typeAmounts.TeenAmount == 0 && typeAmounts.IsAdultPresent
            : true;

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

    public override bool IsValid(ActivityType activity, ICharacterRequirementSubject character)
        => character.Traits.Contains(Trait);

    public override string Describe() => $"trait({Trait})";
}

/// <summary>`char-requirement type="specialization" specialization="X"` (29 uses).</summary>
public sealed class SpecializationRequirement : CharacterRequirement
{
    public SpecializationType Specialization { get; }
    public SpecializationRequirement(SpecializationType specialization) => Specialization = specialization;

    public override bool IsValid(ActivityType activity, ICharacterRequirementSubject character)
        => character.Specializations.Contains(Specialization);

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

    public override bool IsValid(ActivityType activity, ICharacterRequirementSubject character)
        => character.Type == CharacterType;

    public override string Describe() => $"char-type({CharacterType})";
}

/// <summary>`char-requirement type="or"` — any child suffices. Used just twice in all content.</summary>
/// <remarks>
/// <b>Vacuously false when it has no children</b> — the opposite default from
/// <see cref="AndActivityRequirement"/>. The two composites disagree on the empty case, which is
/// correct in both instances but easy to get backwards when porting.
/// </remarks>
public sealed class OrCharacterRequirement : CharacterRequirement
{
    public IReadOnlyList<CharacterRequirement> Requirements { get; }
    public OrCharacterRequirement(IReadOnlyList<CharacterRequirement> requirements) => Requirements = requirements;

    public override bool IsValid(ActivityType activity, ICharacterRequirementSubject character)
    {
        foreach (var r in Requirements)
        {
            if (r.IsValid(activity, character))
            {
                return true;
            }
        }
        return false;
    }

    public override string Describe() => $"or[{string.Join(", ", Requirements.Select(r => r.Describe()))}]";
}

// ---------------------------------------------------------------------------------------------
// Deliberately NOT ported
// ---------------------------------------------------------------------------------------------
//
// jasbro.game.character.activities.requirements.OrSpecializationRequirement exists in the
// decompiled source but is NOT registered in RoomLoader's CHAR_REQUIREMENTS map (which has exactly
// char-type, or, specialization and trait). No content can reach it through rooms.xml, so porting
// it would add surface with no way to test it. If it turns out to be constructed in code, that
// call site should be found first.
