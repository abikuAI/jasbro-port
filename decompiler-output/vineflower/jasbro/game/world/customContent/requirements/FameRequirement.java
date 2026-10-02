package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public class FameRequirement extends TriggerRequirement implements CharacterRequirement {
   private long fameRequired;

   @Override
   public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
      return character.getFame().getFame() >= this.fameRequired;
   }

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return triggerParent.getCharacters() != null && triggerParent.getCharacters().size() != 0
         ? this.isValid(triggerParent.getCharacters().get(0), triggerParent)
         : false;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.FAMEREQUIREMENT;
   }

   public long getFameRequired() {
      return this.fameRequired;
   }

   public void setFameRequired(long fameRequired) {
      this.fameRequired = fameRequired;
   }
}
