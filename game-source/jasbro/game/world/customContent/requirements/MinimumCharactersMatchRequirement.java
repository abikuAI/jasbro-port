package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public class MinimumCharactersMatchRequirement extends TriggerRequirementContainer {
   private int minimum;

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      int count = 0;

      for (Charakter c : triggerParent.getCharacters()) {
         if (((CharacterRequirement)this.getSubRequirements().get(0)).isValid(c, triggerParent)) {
            count++;
         }
      }

      return count >= this.minimum;
   }

   public int getMinimum() {
      return this.minimum;
   }

   public void setMinimum(int minimum) {
      this.minimum = minimum;
   }

   @Override
   public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
      return triggerRequirement instanceof CharacterRequirement && this.getSubRequirements().size() < 1;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.MINIMUMCHARACTERSMATCHREQUIREMENT;
   }
}
