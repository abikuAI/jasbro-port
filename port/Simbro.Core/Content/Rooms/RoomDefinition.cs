using Simbro.Core.Content;

namespace Simbro.Core.Content.Rooms;

/// <summary>
/// A room type: what it costs, how many characters fit, where it can be built, and what may be
/// done in it. Ported from <c>jasbro.game.housing.RoomInfo</c>.
/// </summary>
public sealed class RoomDefinition
{
    public string Id { get; }
    public int Cost { get; }
    public int MaxOccupancy { get; }

    /// <summary>
    /// The background image path, exactly as written in <c>rooms.xml</c>.
    /// </summary>
    /// <remarks>
    /// Java wraps this in an <c>ImageData</c> at construction. Kept as a raw string here because
    /// <see cref="ImageEntry"/> (the character-side equivalent) carries tags and dimensions that a
    /// room background has no equivalent for, and because nothing in room loading reads the image.
    /// </remarks>
    public string Image { get; }

    /// <summary>
    /// Slots this room fits in, in <b>enum declaration order</b>.
    /// </summary>
    /// <remarks>
    /// <para>
    /// <b>Never empty.</b> The loader substitutes defaults when the XML omits <c>slots</c> or
    /// provides none that parse.
    /// </para>
    /// <para>
    /// Order is enum declaration order, not insertion order, because Java stores these in an
    /// <c>EnumSet</c> — a bitset iterated by ordinal. Any game code that walks the slot set sees
    /// declaration order, so a port that preserved XML order would diverge on rooms whose XML
    /// lists slots out of order. Since <see cref="RoomSlotType"/> declares its members in the same
    /// order as the Java enum, ordering by ordinal reproduces <c>EnumSet</c> exactly.
    /// </para>
    /// </remarks>
    public IReadOnlyList<RoomSlotType> SlotTypes =>
        _slotTypes.OrderBy(s => (int)s).ToList();

    private readonly HashSet<RoomSlotType> _slotTypes = new();

    /// <summary>
    /// Activity requirements, keyed by activity. Java uses an <c>EnumMap</c>.
    /// </summary>
    public IReadOnlyDictionary<ActivityType, ActivityRequirement> ActivityRequirements => _activityRequirements;

    private readonly Dictionary<ActivityType, ActivityRequirement> _activityRequirements = new();

    /// <summary>Child-care activity requirements. Java uses a separate map, not a flag.</summary>
    public IReadOnlyDictionary<ActivityType, ActivityRequirement> ChildCareActivityRequirements => _childCareRequirements;

    private readonly Dictionary<ActivityType, ActivityRequirement> _childCareRequirements = new();

    /// <summary>
    /// Activities in first-seen order.
    /// </summary>
    /// <remarks>
    /// Java keeps a parallel <c>List</c> alongside the map so ordering is insertion order, and
    /// guards duplicate adds. Note this list does <b>not</b> include child-care activities —
    /// <c>addChildCareActivity</c> writes only to its own map. That asymmetry is preserved.
    /// </remarks>
    public IReadOnlyList<ActivityType> Activities => _activities;

    private readonly List<ActivityType> _activities = new();

    public RoomDefinition(string id, int cost, int maxOccupancy, string image)
    {
        Id = id;
        Cost = cost;
        MaxOccupancy = maxOccupancy;
        Image = image;
    }

    /// <summary>Adds an activity, recording its order only the first time.</summary>
    public void AddActivity(ActivityType activity, ActivityRequirement requirement)
    {
        _activityRequirements[activity] = requirement;
        if (!_activities.Contains(activity))
        {
            _activities.Add(activity);
        }
    }

    /// <summary>Adds a child-care activity. Deliberately does not touch <see cref="Activities"/>.</summary>
    public void AddChildCareActivity(ActivityType activity, ActivityRequirement requirement)
        => _childCareRequirements[activity] = requirement;

    /// <summary>Adds a slot type. Set semantics, like Java's <c>EnumSet</c>.</summary>
    public void AddSlotType(RoomSlotType slotType) => _slotTypes.Add(slotType);

    public bool FitsInSlot(RoomSlotType slot) => _slotTypes.Contains(slot);
}
