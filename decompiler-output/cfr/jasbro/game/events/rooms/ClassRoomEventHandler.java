/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.rooms.AbstractRoomEventHandler;

public class ClassRoomEventHandler
extends AbstractRoomEventHandler {
    public ClassRoomEventHandler() {
        this.setHandledType(EventType.ACTIVITYPERFORMED);
    }

    @Override
    protected void handleEventInternal(MyEvent event) {
        block4: {
            RunningActivity activity;
            block3: {
                activity = (RunningActivity)event.getSource();
                if (!(activity instanceof Train)) break block3;
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() instanceof EssentialAttributes) continue;
                    attributeModification.addModificator(Float.valueOf((float)Util.getPercent(attributeModification.getBaseAmount(), 10)));
                }
                break block4;
            }
            if (!(activity instanceof Sex) && !(activity instanceof Orgy)) break block4;
            for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                if (!(attributeModification.getAttributeType() instanceof EssentialAttributes)) {
                    attributeModification.addModificator(Float.valueOf((float)Util.getPercent(attributeModification.getBaseAmount(), -10)));
                    continue;
                }
                if (!(attributeModification.getAttributeType() instanceof EssentialAttributes) || !(attributeModification.getBaseAmount() < 0.0f)) continue;
                attributeModification.addModificator(Float.valueOf((float)Util.getPercent(attributeModification.getBaseAmount(), 10)));
            }
        }
    }
}

