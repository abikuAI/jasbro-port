/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events;

import jasbro.game.events.EventType;

public class MyEvent {
    private EventType type;
    private Object source;
    private boolean cancelled = false;

    public MyEvent(EventType type, Object source) {
        this.type = type;
        this.source = source;
    }

    public Object getSource() {
        return this.source;
    }

    public EventType getType() {
        return this.type;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

