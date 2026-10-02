/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.rooms.RoomEventHandler;

public class MasterBedroomEventHandler
implements RoomEventHandler {
    @Override
    public void handleEvent(MyEvent event) {
        RunningActivity activity;
        if (event.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity2 = (RunningActivity)event.getSource();
            if (activity2.getPlannedActivity().getType() == ActivityType.SLEEP) {
                for (AttributeModification attributeModification : activity2.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    attributeModification.setBaseAmount((float)Util.getPercent(attributeModification.getBaseAmount(), 120));
                }
            } else if (activity2.getPlannedActivity().getType() == ActivityType.WHORE) {
                activity2.setIncome((int)Util.getPercent(activity2.getIncome(), 120));
            }
        } else if (event.getType() == EventType.ACTIVITY && (activity = (RunningActivity)event.getSource()).getPlannedActivity().getType() == ActivityType.WHORE) {
            Whore whore = (Whore)activity;
            whore.getMainCustomer().addToSatisfaction(15, this);
        }
    }
}

