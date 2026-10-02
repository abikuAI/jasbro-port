/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class LegacyPerks {

    public static class LegacyNone
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                RunningActivity activity = (RunningActivity)e.getSource();
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    float amount;
                    if (attributeModification.getAttributeType() != BaseAttributeTypes.COMMAND || !((amount = attributeModification.getBaseAmount()) < 0.0f)) continue;
                    float change = Math.abs(amount) * 0.2f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class BenefactorKingdom
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                Jasbro.getInstance().getData().spendMoney(50000L, this);
            }
        }
    }

    public static class BenefactorCarpenters
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                Jasbro.getInstance().getData().spendMoney(12000L, this);
            }
        }
    }

    public static class BenefactorSlavemarket
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                Jasbro.getInstance().getData().spendMoney(10000L, this);
            }
        }
    }

    public static class BenefactorShops
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                Jasbro.getInstance().getData().spendMoney(5000L, this);
            }
        }
    }

    public static class BenefactorStreets
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                Jasbro.getInstance().getData().spendMoney(1000L, this);
            }
        }
    }

    public static class Genius
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.BARTEND) {
                character.addCondition(new Buff.HornyBuff(character));
            }
        }
    }
}

