namespace Simbro.Core.Events;

/// <summary>
/// Raised when the player's balance changes. Mirrors <c>MoneyChangedEvent</c>.
/// </summary>
/// <remarks>
/// In the legacy build the event carries an <c>Object source</c> — whatever game
/// object caused the change. That source is consumed by content scripts, so it is
/// preserved here as an opaque reference rather than dropped.
/// </remarks>
public sealed class MoneyChangedEvent
{
    public MoneyChangedEvent(EventType type, object? source, long amount)
    {
        if (type != EventType.MONEYEARNED && type != EventType.MONEYSPENT)
        {
            throw new ArgumentException(
                $"MoneyChangedEvent requires MONEYEARNED or MONEYSPENT, got {type}.", nameof(type));
        }

        Type = type;
        Source = source;
        Amount = amount;
    }

    public EventType Type { get; }

    /// <summary>The game object responsible for the change. Opaque by design.</summary>
    public object? Source { get; }

    /// <summary>The magnitude of the change. Always non-negative in legacy usage.</summary>
    public long Amount { get; }
}

/// <summary>A game event with an optional payload. Mirrors <c>MyEvent</c>.</summary>
public sealed class GameEvent
{
    public GameEvent(EventType type, object? payload = null)
    {
        Type = type;
        Payload = payload;
    }

    public EventType Type { get; }

    public object? Payload { get; }
}
