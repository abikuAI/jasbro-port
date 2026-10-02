package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;

public class AndRequirement extends TriggerRequirementContainer {
   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      for (TriggerRequirement ar : this.getSubRequirements()) {
         if (!ar.isValid(triggerParent)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.ANDREQUIREMENT;
   }
}
