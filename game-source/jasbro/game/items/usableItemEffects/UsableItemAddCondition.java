package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.conditions.ConditionType;
import jasbro.game.items.Item;

public class UsableItemAddCondition extends UsableItemEffect {
   private ConditionType conditionType;

   @Override
   public String getName() {
      return "Add Condition";
   }

   @Override
   public void apply(Charakter character, Item item) {
      if (this.conditionType != null) {
         try {
            character.addCondition(this.conditionType.getConditionClass().newInstance());
         } catch (InstantiationException e) {
            e.printStackTrace();
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         }
      }
   }

   @Override
   public UsableItemEffectType getType() {
      return UsableItemEffectType.ADDCONDITION;
   }

   public ConditionType getConditionType() {
      return this.conditionType;
   }

   public void setConditionType(ConditionType conditionType) {
      this.conditionType = conditionType;
   }
}
