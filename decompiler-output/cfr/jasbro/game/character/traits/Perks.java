/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import java.util.ArrayList;
import java.util.List;

public class Perks {

    public static class PerkUtil {
        public static boolean addMaybe(Charakter character, Trait trait) {
            if (!character.getTraits().contains(trait)) {
                character.addTrait(trait);
                return true;
            }
            return false;
        }

        public static boolean removeMaybe(Charakter character, Trait trait) {
            if (character.getTraits().contains(trait)) {
                character.removeTrait(trait);
                return true;
            }
            return false;
        }

        public static Trait getTraitFrom(Charakter character, Trait trait) {
            for (Trait t : character.getTraits()) {
                if (t != trait) continue;
                return t;
            }
            return null;
        }

        public static Trait addAndReturn(Charakter character, Trait trait) {
            PerkUtil.addMaybe(character, trait);
            return PerkUtil.getTraitFrom(character, trait);
        }
    }

    public static class AttributeInfluence
    extends TraitEffect {
        private AttributeType targetAttributeType;
        private AttributeType sourceAttributeType;
        private float modifier;

        public AttributeInfluence(AttributeType targetAttributeType, AttributeType sourceAttributeType, float modifier) {
            this.targetAttributeType = targetAttributeType;
            this.sourceAttributeType = sourceAttributeType;
            this.modifier = modifier;
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (attribute.getAttributeType() == this.targetAttributeType) {
                return attribute.getCharacter().getAttribute(this.sourceAttributeType).getInternValue() * this.modifier;
            }
            return 0.0f;
        }
    }

    public static class AttributeMaxInfluence
    extends TraitEffect {
        private AttributeType targetAttributeType;
        private AttributeType sourceAttributeType;
        private float modifier;

        public AttributeMaxInfluence(AttributeType targetAttributeType, AttributeType sourceAttributeType, float modifier) {
            this.targetAttributeType = targetAttributeType;
            this.sourceAttributeType = sourceAttributeType;
            this.modifier = modifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeChangedEvent attributeChangedEvent;
            Attribute attribute;
            if (e.getType() == EventType.ATTRIBUTECHANGED && (attribute = (attributeChangedEvent = (AttributeChangedEvent)e).getAttribute()).getAttributeType() == this.sourceAttributeType) {
                Attribute targetAttribute = character.getAttribute(this.targetAttributeType);
                int newModifier = (int)(attribute.getInternValue() * this.modifier);
                int oldModifier = (int)((attribute.getInternValue() - attributeChangedEvent.getAmount()) * this.modifier);
                targetAttribute.setMaxValue(targetAttribute.getMaxValue() + (newModifier - oldModifier));
            }
        }

        @Override
        public boolean addTrait(Charakter character) {
            Attribute sourceAttribute = character.getAttribute(this.sourceAttributeType);
            Attribute targetAttribute = character.getAttribute(this.targetAttributeType);
            targetAttribute.setMaxValue(targetAttribute.getMaxValue() + (int)(sourceAttribute.getInternValue() * this.modifier));
            return true;
        }

        @Override
        public boolean removeTrait(Charakter character) {
            Attribute sourceAttribute = character.getAttribute(this.sourceAttributeType);
            Attribute targetAttribute = character.getAttribute(this.targetAttributeType);
            targetAttribute.setMaxValue(targetAttribute.getMaxValue() - (int)(sourceAttribute.getInternValue() * this.modifier));
            return true;
        }
    }

    public static class SimpleTraitEffect
    extends TraitEffect {
        private List<CalculatedAttribute> calculatableAttributes = new ArrayList<CalculatedAttribute>();
        private List<AttributeType> otherAttributes = new ArrayList<AttributeType>();
        private Double addValue;
        private Double multValue;

        public SimpleTraitEffect(Double addValue, Double multValue, AttributeType ... attributes) {
            for (AttributeType attributeType : attributes) {
                if (attributeType instanceof CalculatedAttribute) {
                    this.calculatableAttributes.add((CalculatedAttribute)attributeType);
                    continue;
                }
                this.otherAttributes.add(attributeType);
            }
            this.addValue = addValue;
            this.multValue = multValue;
        }

        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (this.calculatableAttributes.contains(calculatedAttribute)) {
                double temp = currentValue;
                if (this.multValue != null) {
                    temp *= this.multValue.doubleValue();
                }
                if (this.addValue != null) {
                    temp += this.addValue.doubleValue();
                }
                return temp;
            }
            return currentValue;
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (this.otherAttributes.contains(attribute.getAttributeType())) {
                double temp = 0.0;
                if (this.multValue != null) {
                    temp = (this.multValue - 1.0) * (double)attribute.getValue();
                }
                if (this.addValue != null) {
                    temp += this.addValue.doubleValue();
                }
                return (float)temp;
            }
            return super.getAttributeModifier(attribute);
        }
    }
}

