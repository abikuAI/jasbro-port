package jasbro.game.character.conditions;

import jasbro.game.character.Condition;

public enum ConditionType {
   FLU(Illness.Flu.class),
   SMALLPOX(Illness.Smallpox.class),
   ILLNESS(Illness.class);

   private Class<? extends Condition> conditionClass;

   ConditionType(Class<? extends Condition> conditionClass) {
      this.conditionClass = conditionClass;
   }

   public Class<? extends Condition> getConditionClass() {
      return this.conditionClass;
   }
}
