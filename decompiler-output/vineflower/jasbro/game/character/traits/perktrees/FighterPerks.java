package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.TraitEffect;

public class FighterPerks {
   public static class CastTime extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
            return currentValue + 10.0;
         }

         if (calculatedAttribute == CalculatedAttribute.SPEED) {
            currentValue -= 10.0;
            if (currentValue <= 0.0) {
               currentValue = 1.0;
            }

            return currentValue;
         } else {
            return currentValue;
         }
      }
   }

   public static class Distraction extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute != CalculatedAttribute.HIT && calculatedAttribute != CalculatedAttribute.CRITCHANCE
            ? currentValue
            : currentValue + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 10;
      }
   }

   public static class ElementalStudy extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.ARMORPERCENT
            ? currentValue + character.getFinalValue(SpecializationAttribute.MAGIC) / 7
            : currentValue;
      }
   }

   public static class EtherShield extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.BLOCKCHANCE
            ? currentValue + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) * 3 / 2
            : currentValue;
      }
   }

   public static class IronBody extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.BLOCKCHANCE
            ? currentValue + character.getFinalValue(BaseAttributeTypes.STRENGTH) * 3 / 2
            : currentValue;
      }
   }

   public static class MindOfTheFighter extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.CRITCHANCE
            ? currentValue + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 2
            : currentValue;
      }
   }

   public static class Plunderer extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.ITEMLOOTCHANCEMODIFIER ? 100.0 : currentValue;
      }
   }
}
