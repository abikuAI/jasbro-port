/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.rooms.AbstractRoomEventHandler;

public class SickroomEventHandler
extends AbstractRoomEventHandler {
    public SickroomEventHandler() {
        this.setHandledType(EventType.ACTIVITYPERFORMED);
    }

    @Override
    protected void handleEventInternal(MyEvent event) {
        RunningActivity activity = (RunningActivity)event.getSource();
        for (AttributeModification attributeModification : activity.getAttributeModifications()) {
            if (attributeModification.getAttributeType() != EssentialAttributes.HEALTH) continue;
            attributeModification.setBaseAmount((float)Util.getPercent(attributeModification.getBaseAmount(), 200));
        }
    }
}

