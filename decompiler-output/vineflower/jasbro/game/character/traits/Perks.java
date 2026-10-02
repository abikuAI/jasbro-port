package jasbro.game.character.traits;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import java.util.ArrayList;
import java.util.List;

public class Perks {
   public static class AttributeInfluence extends TraitEffect {
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
         return attribute.getAttributeType() == this.targetAttributeType
            ? attribute.getCharacter().getAttribute(this.sourceAttributeType).getInternValue() * this.modifier
            : 0.0F;
      }
   }

   public static class AttributeMaxInfluence extends TraitEffect {
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
         if (e.getType() == EventType.ATTRIBUTECHANGED) {
            AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent)e;
            Attribute attribute = attributeChangedEvent.getAttribute();
            if (attribute.getAttributeType() == this.sourceAttributeType) {
               Attribute targetAttribute = character.getAttribute(this.targetAttributeType);
               int newModifier = (int)(attribute.getInternValue() * this.modifier);
               int oldModifier = (int)((attribute.getInternValue() - attributeChangedEvent.getAmount()) * this.modifier);
               targetAttribute.setMaxValue(targetAttribute.getMaxValue() + (newModifier - oldModifier));
            }
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

   public static class PerkUtil {
      public static boolean addMaybe(Charakter character, Trait trait) {
         if (!character.getTraits().contains(trait)) {
            character.addTrait(trait);
            return true;
         } else {
            return false;
         }
      }

      public static boolean removeMaybe(Charakter character, Trait trait) {
         if (character.getTraits().contains(trait)) {
            character.removeTrait(trait);
            return true;
         } else {
            return false;
         }
      }

      public static Trait getTraitFrom(Charakter character, Trait trait) {
         for (Trait t : character.getTraits()) {
            if (t == trait) {
               return t;
            }
         }

         return null;
      }

      public static Trait addAndReturn(Charakter character, Trait trait) {
         addMaybe(character, trait);
         return getTraitFrom(character, trait);
      }
   }

   public static class SimpleTraitEffect extends TraitEffect {
      private List<CalculatedAttribute> calculatableAttributes = new ArrayList<>();
      private List<AttributeType> otherAttributes = new ArrayList<>();
      private Double addValue;
      private Double multValue;

      public SimpleTraitEffect(Double addValue, Double multValue, AttributeType... attributes) {
         for (AttributeType attributeType : attributes) {
            if (attributeType instanceof CalculatedAttribute) {
               this.calculatableAttributes.add((CalculatedAttribute)attributeType);
            } else {
               this.otherAttributes.add(attributeType);
            }
         }

         this.addValue = addValue;
         this.multValue = multValue;
      }

      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         if (this.calculatableAttributes.contains(calculatedAttribute)) {
            double temp = currentValue;
            if (this.multValue != null) {
               temp *= this.multValue;
            }

            if (this.addValue != null) {
               temp += this.addValue;
            }

            return temp;
         } else {
            return currentValue;
         }
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         if (this.otherAttributes.contains(attribute.getAttributeType())) {
            double temp = 0.0;
            if (this.multValue != null) {
               temp = (this.multValue - 1.0) * attribute.getValue();
            }

            if (this.addValue != null) {
               temp += this.addValue;
            }

            return (float)temp;
         } else {
            return super.getAttributeModifier(attribute);
         }
      }
   }
}
