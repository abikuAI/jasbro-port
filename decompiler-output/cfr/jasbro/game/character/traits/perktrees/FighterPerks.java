/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.TraitEffect;

public class FighterPerks {

    public static class Distraction
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.HIT || calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
                return currentValue + (double)(character.getFinalValue(SpecializationAttribute.SEDUCTION) / 10);
            }
            return currentValue;
        }
    }

    public static class Plunderer
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.ITEMLOOTCHANCEMODIFIER) {
                return 100.0;
            }
            return currentValue;
        }
    }

    public static class CastTime
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
                return currentValue + 10.0;
            }
            if (calculatedAttribute == CalculatedAttribute.SPEED) {
                if ((currentValue -= 10.0) <= 0.0) {
                    currentValue = 1.0;
                }
                return currentValue;
            }
            return currentValue;
        }
    }

    public static class EtherShield
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.BLOCKCHANCE) {
                return currentValue + (double)(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) * 3 / 2);
            }
            return currentValue;
        }
    }

    public static class IronBody
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.BLOCKCHANCE) {
                return currentValue + (double)(character.getFinalValue(BaseAttributeTypes.STRENGTH) * 3 / 2);
            }
            return currentValue;
        }
    }

    public static class ElementalStudy
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.ARMORPERCENT) {
                return currentValue + (double)(character.getFinalValue(SpecializationAttribute.MAGIC) / 7);
            }
            return currentValue;
        }
    }

    public static class MindOfTheFighter
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
                return currentValue + (double)(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 2);
            }
            return currentValue;
        }
    }
}

