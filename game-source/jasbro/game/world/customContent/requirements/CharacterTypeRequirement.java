package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public class CharacterTypeRequirement extends TriggerRequirement implements CharacterRequirement {
   private CharacterType characerType;

   @Override
   public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
      return this.characerType == null ? true : character.getType().equals(this.characerType);
   }

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return triggerParent.getCharacters() != null && triggerParent.getCharacters().size() != 0
         ? this.isValid(triggerParent.getCharacters().get(0), triggerParent)
         : false;
   }

   public CharacterType getCharacterType() {
      return this.characerType;
   }

   public void setCharacterType(CharacterType characerType) {
      this.characerType = characerType;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.CHARACTERTYPEREQUIREMENT;
   }
}
