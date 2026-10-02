/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.rooms;

import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.rooms.RoomEventHandler;

public abstract class AbstractRoomEventHandler
implements RoomEventHandler {
    private EventType handledType;

    public void setHandledType(EventType handledType) {
        this.handledType = handledType;
    }

    @Override
    public void handleEvent(MyEvent event) {
        if (event.getType() == this.handledType) {
            this.handleEventInternal(event);
        }
    }

    protected abstract void handleEventInternal(MyEvent var1);
}

