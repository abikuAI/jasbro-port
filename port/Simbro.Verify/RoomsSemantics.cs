// NOTE: deliberately NO namespace declaration - Program.cs uses top-level statements and therefore
// lives in the global namespace. ContentGolden.cs and RoomsGolden.cs have the same shape.

using Simbro.Core.Content;
using Simbro.Core.Content.Rooms;

/// <summary>A character whose three requirement-relevant properties are supplied directly.</summary>
/// <remarks>
/// The C# counterpart of the Java fixture's <c>FakeCharakter</c>. It exists so the requirement
/// predicates can be exercised without a full character model, which is exactly the reason
/// <see cref="ICharacterRequirementSubject"/> is narrow.
/// </remarks>
internal sealed class FakeSubject : ICharacterRequirementSubject
{
    public CharacterType Type { get; }
    public IReadOnlyCollection<Trait> Traits { get; }
    public IReadOnlyCollection<SpecializationType> Specializations { get; }

    public FakeSubject(CharacterType type,
                       IReadOnlyCollection<Trait>? traits = null,
                       IReadOnlyCollection<SpecializationType>? specializations = null)
    {
        Type = type;
        Traits = traits ?? Array.Empty<Trait>();
        Specializations = specializations ?? Array.Empty<SpecializationType>();
    }
}

/// <summary>
/// Rebuilds the character configurations used by <c>fixtures/java/RoomsSemanticsFixture.java</c>.
/// </summary>
/// <remarks>
/// <para>
/// The ordering rules must match the Java fixture exactly, because the golden file records one bit
/// per configuration <b>by position</b>. Java collects the used traits/specializations/char-types
/// in a <c>TreeSet</c> with <c>Comparator.comparing(Enum::name)</c>, i.e. sorted by constant NAME;
/// the C# side sorts by name with an ordinal comparer to match.
/// </para>
/// <para>
/// If these two ever disagree the bitstrings will still have the same length (the configuration
/// <i>count</i> comes from the same content), so a mismatch would show up as many wrong bits rather
/// than a clear error. The per-config labels are therefore dumped and compared too, which turns an
/// ordering drift into an obvious diff.
/// </para>
/// </remarks>
internal static class RoomsSemantics
{
    internal sealed record Config(string Label, IReadOnlyList<ICharacterRequirementSubject> Characters);

    /// <summary>Collects every trait / specialization / char-type named anywhere in the loaded rooms.</summary>
    public static (SortedSet<Trait>, SortedSet<SpecializationType>, SortedSet<CharacterType>) Harvest(
        IReadOnlyDictionary<string, RoomDefinition> rooms)
    {
        var traits = new SortedSet<Trait>(Comparer<Trait>.Create((a, b) => string.CompareOrdinal(a.ToString(), b.ToString())));
        var specs = new SortedSet<SpecializationType>(Comparer<SpecializationType>.Create((a, b) => string.CompareOrdinal(a.ToString(), b.ToString())));
        var types = new SortedSet<CharacterType>(Comparer<CharacterType>.Create((a, b) => string.CompareOrdinal(a.ToString(), b.ToString())));

        foreach (var room in rooms.Values)
        {
            foreach (var req in room.ActivityRequirements.Values) HarvestActivity(req, traits, specs, types);
            foreach (var req in room.ChildCareActivityRequirements.Values) HarvestActivity(req, traits, specs, types);
        }

        return (traits, specs, types);
    }

    private static void HarvestActivity(
        ActivityRequirement r,
        SortedSet<Trait> traits, SortedSet<SpecializationType> specs, SortedSet<CharacterType> types)
    {
        switch (r)
        {
            case AndActivityRequirement and:
                foreach (var child in and.Requirements) HarvestActivity(child, traits, specs, types);
                break;
            case MinimumCharacterRequirement minc:
                HarvestCharacter(minc.Requirement, traits, specs, types);
                break;
            case AllCharacterRequirement allc:
                HarvestCharacter(allc.Requirement, traits, specs, types);
                break;
        }
    }

    private static void HarvestCharacter(
        CharacterRequirement r,
        SortedSet<Trait> traits, SortedSet<SpecializationType> specs, SortedSet<CharacterType> types)
    {
        switch (r)
        {
            case TraitRequirement t:
                traits.Add(t.Trait);
                break;
            case SpecializationRequirement s:
                specs.Add(s.Specialization);
                break;
            case CharacterTypeRequirement ct:
                types.Add(ct.CharacterType);
                break;
            case OrCharacterRequirement or:
                foreach (var child in or.Requirements) HarvestCharacter(child, traits, specs, types);
                break;
        }
    }

    /// <summary>Builds the same configuration list, in the same order, as the Java fixture.</summary>
    public static List<Config> BuildConfigs(
        SortedSet<Trait> traits, SortedSet<SpecializationType> specs, SortedSet<CharacterType> types)
    {
        var configs = new List<Config>();

        static ICharacterRequirementSubject Ch(CharacterType t) => new FakeSubject(t);
        static ICharacterRequirementSubject ChTrait(Trait tr) => new FakeSubject(CharacterType.SLAVE, new[] { tr });
        static ICharacterRequirementSubject ChSpec(SpecializationType sp) => new FakeSubject(CharacterType.SLAVE, null, new[] { sp });

        // empty group - exercises the vacuous-truth edges of and / all-character
        configs.Add(new Config("EMPTY", Array.Empty<ICharacterRequirementSubject>()));

        // one bare character of each type
        foreach (var t in Enum.GetValues<CharacterType>())
            configs.Add(new Config($"ONE_{t}", new[] { Ch(t) }));

        // two bare characters of each type
        foreach (var t in Enum.GetValues<CharacterType>())
            configs.Add(new Config($"TWO_{t}", new[] { Ch(t), Ch(t) }));

        // each used trait: alone, all-slaves, and trainer+slave
        foreach (var tr in traits)
        {
            configs.Add(new Config($"TRAIT_{tr}", new[] { ChTrait(tr) }));
            configs.Add(new Config($"TRAITALL_{tr}", new[] { ChTrait(tr), ChTrait(tr), ChTrait(tr) }));
            configs.Add(new Config($"TRAITMIX_{tr}", new[] { ChTrait(tr), Ch(CharacterType.TRAINER) }));
        }

        // each used specialization: alone, trainer+slave, and two
        foreach (var sp in specs)
        {
            configs.Add(new Config($"SPEC_{sp}", new[] { ChSpec(sp) }));
            configs.Add(new Config($"SPECMIX_{sp}", new[] { ChSpec(sp), Ch(CharacterType.TRAINER) }));
            configs.Add(new Config($"SPECTWO_{sp}", new[] { ChSpec(sp), ChSpec(sp) }));
        }

        // each used char-type paired with an adult
        foreach (var t in types)
            configs.Add(new Config($"TYPEADULT_{t}", new[] { Ch(t), Ch(CharacterType.TRAINER) }));

        // child-care shapes
        configs.Add(new Config("CARE_INFANT_TRAINER", new[] { Ch(CharacterType.INFANT), Ch(CharacterType.TRAINER) }));
        configs.Add(new Config("CARE_INFANT_ONLY", new[] { Ch(CharacterType.INFANT) }));
        configs.Add(new Config("CARE_INFANT_CHILD_TRAINER", new[] { Ch(CharacterType.INFANT), Ch(CharacterType.CHILD), Ch(CharacterType.TRAINER) }));
        configs.Add(new Config("CARE_INFANT_TEEN_TRAINER", new[] { Ch(CharacterType.INFANT), Ch(CharacterType.TEENAGER), Ch(CharacterType.TRAINER) }));
        configs.Add(new Config("CARE_CHILD_TRAINER", new[] { Ch(CharacterType.CHILD), Ch(CharacterType.TRAINER) }));
        configs.Add(new Config("CARE_TEEN_TRAINER", new[] { Ch(CharacterType.TEENAGER), Ch(CharacterType.TRAINER) }));
        configs.Add(new Config("CARE_TWO_INFANT_TRAINER", new[] { Ch(CharacterType.INFANT), Ch(CharacterType.INFANT), Ch(CharacterType.TRAINER) }));
        configs.Add(new Config("CARE_INFANT_TWO_TRAINER", new[] { Ch(CharacterType.INFANT), Ch(CharacterType.TRAINER), Ch(CharacterType.TRAINER) }));

        // occupancy boundaries, including sizes above the shipped max of 5
        for (var n = 1; n <= 6; n++)
            configs.Add(new Config($"SIZE_{n}", Enumerable.Repeat(Ch(CharacterType.SLAVE), n).ToArray()));

        return configs;
    }

    /// <summary>The golden truth table: config labels, plus one bitstring per room/activity.</summary>
    internal sealed class Golden
    {
        public List<string> ConfigLabels { get; } = new();
        public Dictionary<string, string> Rows { get; } = new(StringComparer.Ordinal);
    }

    public static Golden ParseGolden(string[] lines)
    {
        var g = new Golden();
        foreach (var raw in lines)
        {
            var line = raw.TrimEnd();
            if (line.Length == 0 || line.StartsWith("CONFIGS=", StringComparison.Ordinal)) continue;

            if (line.StartsWith("CONFIG ", StringComparison.Ordinal))
            {
                // "CONFIG 3 ONE_INFANT" - index then label
                var parts = line.Split(' ', 3);
                if (parts.Length == 3) g.ConfigLabels.Add(parts[2]);
                continue;
            }

            if (line.StartsWith("V ", StringComparison.Ordinal))
            {
                // "V ROOM/ACT 0101..."
                var parts = line.Split(' ', 3);
                if (parts.Length == 3) g.Rows[parts[1]] = parts[2];
            }
        }
        return g;
    }
}
