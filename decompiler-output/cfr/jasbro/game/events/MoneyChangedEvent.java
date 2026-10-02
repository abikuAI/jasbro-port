/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events;

import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class MoneyChangedEvent
extends MyEvent {
    private long amount;

    public MoneyChangedEvent(EventType type, Object source, long amount) {
        super(type, source);
        this.amount = amount;
    }

    public long getAmount() {
        return this.amount;
    }
}

