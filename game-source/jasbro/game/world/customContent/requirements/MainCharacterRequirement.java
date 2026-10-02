package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public class MainCharacterRequirement extends TriggerRequirementContainer implements CharacterRequirement {
   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return this.getSubRequirements().size() < 1
         ? false
         : ((CharacterRequirement)this.getSubRequirements().get(0)).isValid(Jasbro.getInstance().getData().getProtagonist(), triggerParent);
   }

   @Override
   public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
      return triggerRequirement instanceof CharacterRequirement && this.getSubRequirements().size() < 1;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.MAINCHARACTERREQUIREMENT;
   }

   @Override
   public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
      return character == Jasbro.getInstance().getData().getProtagonist();
   }
}
