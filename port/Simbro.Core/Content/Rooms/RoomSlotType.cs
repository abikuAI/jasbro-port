namespace Simbro.Core.Content.Rooms;

/// <summary>
/// Where a room may be built. Ported from <c>jasbro.game.housing.RoomSlotType</c>.
/// </summary>
/// <remarks>
/// <para>
/// The Java enum carries a <c>downTime</c> per constant. It is not obvious from the name, so it is
/// recorded as an explicit mapping in <see cref="RoomSlotTypes.DownTime"/> rather than left implicit
/// — and because the values are non-obvious (OUTDOOR is 8, UNDERGROUND is 5, LARGEROOM is 3) they
/// are asserted in tests rather than trusted.
/// </para>
/// <para>Names are load-bearing: <c>rooms.xml</c> resolves them with <c>valueOf</c>.</para>
/// </remarks>
public enum RoomSlotType
{
    SMALLROOM,
    LARGEROOM,
    OUTDOOR,
    UNDERGROUND,
}

/// <summary>Companion data for <see cref="RoomSlotType"/> that the Java enum carried inline.</summary>
public static class RoomSlotTypes
{
    /// <summary>
    /// Downtime per slot type, from the <c>RoomSlotType(int)</c> constructor.
    /// </summary>
    /// <remarks>
    /// Handles for every constant. If a constant is ever added without an entry this throws rather
    /// than silently returning 0 — a slot with no downtime would quietly make rooms infinitely
    /// reusable.
    /// </remarks>
    public static int DownTime(RoomSlotType type) => type switch
    {
        RoomSlotType.SMALLROOM => 1,
        RoomSlotType.LARGEROOM => 3,
        RoomSlotType.UNDERGROUND => 5,
        RoomSlotType.OUTDOOR => 8,
        _ => throw new ArgumentOutOfRangeException(
            nameof(type), type, "RoomSlotType has no recorded downtime; the Java enum was extended."),
    };
}
