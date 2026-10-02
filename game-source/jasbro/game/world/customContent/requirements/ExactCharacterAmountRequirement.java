package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;

public class ExactCharacterAmountRequirement extends TriggerRequirement {
   private int count;

   public ExactCharacterAmountRequirement(int count) {
      this.count = count;
   }

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return triggerParent.getCharacters().size() == this.count;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.EXACTCHARACTERAMOUNTREQUIREMENT;
   }
}
