package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;

public class NotRequirement extends TriggerRequirementContainer {
   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return this.getSubRequirements().size() < 1 ? true : !this.getSubRequirements().get(0).isValid(triggerParent);
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.NOTREQUIREMENT;
   }

   @Override
   public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
      return this.getSubRequirements().size() < 1;
   }
}
