package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Util;
import jasbro.game.world.customContent.TriggerParent;

public class ChildCareRequirement extends TriggerRequirement {
   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      Util.TypeAmounts typeAmounts = triggerParent.getTypeAmounts();
      if (typeAmounts == null) {
         return false;
      } else {
         return typeAmounts.getInfantAmount() > 0
            ? typeAmounts.getChildAmount() == 0 && typeAmounts.getTeenAmount() == 0 && typeAmounts.isAdultPresent()
            : true;
      }
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.CHILDCAREREQUIREMENT;
   }
}
