package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public class AllCharactersRequirement extends TriggerRequirementContainer {
   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      if (this.getSubRequirements().size() >= 1 && triggerParent.getCharacters() != null && triggerParent.getCharacters().size() != 0) {
         for (Charakter c : triggerParent.getCharacters()) {
            if (!((CharacterRequirement)this.getSubRequirements().get(0)).isValid(c, triggerParent)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
      return triggerRequirement instanceof CharacterRequirement && this.getSubRequirements().size() < 1;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.ALLCHARACTERSREQUIREMENT;
   }
}
