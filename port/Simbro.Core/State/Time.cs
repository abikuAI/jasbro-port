namespace Simbro.Core.State;

/// <summary>
/// Shift within the day. Mirrors the legacy <c>jasbro.game.world.Time</c> enum exactly.
/// </summary>
/// <remarks>
/// The legacy game serialises this by name via XStream, so names are part of the
/// save contract. Note there is no EVENING — the day is MORNING, AFTERNOON, NIGHT.
/// </remarks>
public enum Time
{
    MORNING,
    AFTERNOON,
    NIGHT,
}

public static class TimeExtensions
{
    /// <summary>Legacy: <c>Time.getNextTimeOfDay()</c>. MORNING -> AFTERNOON -> NIGHT -> MORNING.</summary>
    public static Time NextTimeOfDay(this Time time) => time switch
    {
        Time.MORNING => Time.AFTERNOON,
        Time.AFTERNOON => Time.NIGHT,
        Time.NIGHT => Time.MORNING,
        _ => throw new ArgumentOutOfRangeException(nameof(time), time, null),
    };

    /// <summary>Legacy: <c>Time.getPreviousTimeOfDay()</c>. Inverse of <see cref="NextTimeOfDay"/>.</summary>
    public static Time PreviousTimeOfDay(this Time time) => time switch
    {
        Time.MORNING => Time.NIGHT,
        Time.AFTERNOON => Time.MORNING,
        Time.NIGHT => Time.AFTERNOON,
        _ => throw new ArgumentOutOfRangeException(nameof(time), time, null),
    };

    /// <summary>Legacy: <c>Time.isNewDay()</c> — a new day starts at MORNING.</summary>
    public static bool IsNewDay(this Time time) => time == Time.MORNING;
}
