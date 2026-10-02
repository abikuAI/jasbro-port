/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class SlavePerks {

    public static class GoodSlave
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITYPERFORMED && (activity = (RunningActivity)e.getSource()) instanceof Train) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getTargetCharacter() != character || attributeModification.getAttributeType() instanceof EssentialAttributes) continue;
                    attributeModification.addModificator(Float.valueOf(attributeModification.getBaseAmount() * 0.3f));
                }
            }
        }
    }
}

