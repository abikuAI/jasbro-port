namespace Simbro.Core.Events;

/// <summary>
/// Mirrors the legacy <c>jasbro.game.events.EventType</c> enum.
/// </summary>
/// <remarks>
/// Ported from the decompiled shipped build. The boolean flag marks event types
/// that custom content (the BeanShell event/quest scripts) can react to — see
/// <c>isCustomContentRelevant()</c> in the legacy code.
/// </remarks>
public enum EventType
{
    ACTIVITY,
    ACTIVITYPERFORMED,
    ACTIVITYFINISHED,
    ACTIVITYCREATED,
    CHARACTERLOST,
    CHARACTERGAINED,
    SLAVESOLD,
    ATTRIBUTECHANGE,
    ATTRIBUTECHANGED,
    ACTIVITYCHANGE,
    ENERGYZERO,
    HEALTHZERO,
    CHARACTERDEATH,
    NEXTSHIFT,
    NEXTDAY,
    NEXTSHIFTSTARTED,
    ITEMUSED,
    SHIFTSTART,
    CUSTOMERSARRIVE,
    MONEYEARNED,
    MONEYSPENT,
    BROKE,
    ATTACK,
    ATTACKMISS,
    ATTACKBLOCK,
    ATTACKCRIT,
    ATTACKHIT,
    STATUSCHANGE,
    MOTIVATIONLOW,
    MOTIVATIONHIGH,
    MOTIVATIONNORMAL,
    GAMESTART,
}

public static class EventTypeExtensions
{
    private static readonly HashSet<EventType> CustomContentRelevant = new()
    {
        EventType.ACTIVITY,
        EventType.ACTIVITYPERFORMED,
        EventType.ACTIVITYFINISHED,
        EventType.ACTIVITYCREATED,
        EventType.HEALTHZERO,
        EventType.NEXTSHIFT,
        EventType.NEXTDAY,
        EventType.SHIFTSTART,
        EventType.MOTIVATIONLOW,
        EventType.MOTIVATIONHIGH,
        EventType.MOTIVATIONNORMAL,
        EventType.GAMESTART,
    };

    /// <summary>Legacy: <c>EventType.isCustomContentRelevant()</c>.</summary>
    public static bool IsCustomContentRelevant(this EventType type) =>
        CustomContentRelevant.Contains(type);
}
