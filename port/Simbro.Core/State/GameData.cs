using Simbro.Core.Events;

namespace Simbro.Core.State;

/// <summary>
/// Receives game events. In the legacy build <c>GameData</c> called straight into
/// the Swing GUI (<c>Jasbro.getInstance().getGui().updateStatus()</c>); the port
/// inverts that so the domain core stays engine-free and testable.
/// </summary>
public interface IGameEventSink
{
    /// <summary>Legacy: <c>EventManager.handleEvent(...)</c>.</summary>
    void Handle(GameEvent gameEvent);

    /// <summary>
    /// Legacy: <c>Jasbro.getInstance().getGui().updateStatus()</c>. The domain no
    /// longer depends on a GUI; presentation layers subscribe instead.
    /// </summary>
    void NotifyStatusChanged();
}

/// <summary>A sink that discards everything — useful for headless tests.</summary>
public sealed class NullEventSink : IGameEventSink
{
    public static readonly NullEventSink Instance = new();

    public void Handle(GameEvent gameEvent) { }

    public void NotifyStatusChanged() { }
}

/// <summary>
/// The root of all game state — the legacy save root.
/// </summary>
/// <remarks>
/// Ported from decompiled <c>jasbro.game.GameData</c>. Only the scalar core
/// (day / time / money) and the money semantics are implemented so far; the
/// object graph (houses, characters, markets, inventory) is added incrementally.
///
/// Behaviour deliberately preserved from the legacy build:
///  - money is a signed 64-bit value and MAY go negative;
///  - the money event fires BEFORE the balance mutates;
///  - crossing below zero fires a BROKE event.
///
/// Behaviour deliberately fixed:
///  - no direct GUI calls from the domain (see <see cref="IGameEventSink"/>).
/// </remarks>
public sealed class GameData
{
    private readonly IGameEventSink _sink;

    public GameData(IGameEventSink? sink = null)
    {
        _sink = sink ?? NullEventSink.Instance;
    }

    /// <summary>Legacy default: day 1.</summary>
    public int Day { get; set; } = 1;

    /// <summary>Legacy default: MORNING.</summary>
    public Time Time { get; set; } = Time.MORNING;

    /// <summary>
    /// Player balance. Legacy default: 500. <c>long</c> in the original — do not narrow.
    /// </summary>
    public long Money { get; private set; } = 500L;

    /// <summary>Legacy: <c>GameData.canAfford(long)</c>.</summary>
    public bool CanAfford(long price) => price <= Money;

    /// <summary>
    /// Legacy: <c>GameData.earnMoney(long, Object)</c>.
    /// Note the event is raised BEFORE the balance changes — content scripts
    /// observing MONEYEARNED see the pre-change balance.
    /// </summary>
    public void EarnMoney(long amount, object? source = null)
    {
        _sink.Handle(new GameEvent(EventType.MONEYEARNED,
            new MoneyChangedEvent(EventType.MONEYEARNED, source, amount)));
        Money += amount;
        _sink.NotifyStatusChanged();
    }

    /// <summary>
    /// Legacy: <c>GameData.spendMoney(long, Object)</c>.
    /// The balance is allowed to go negative; crossing below zero raises BROKE.
    /// </summary>
    public void SpendMoney(long amount, object? source = null)
    {
        _sink.Handle(new GameEvent(EventType.MONEYSPENT,
            new MoneyChangedEvent(EventType.MONEYSPENT, source, amount)));
        Money -= amount;
        _sink.NotifyStatusChanged();

        if (Money < 0L)
        {
            _sink.Handle(new GameEvent(EventType.BROKE));
        }
    }
}
