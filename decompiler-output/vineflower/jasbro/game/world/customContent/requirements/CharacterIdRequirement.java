package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public class CharacterIdRequirement extends TriggerRequirement implements CharacterRequirement {
   private String characterId;

   @Override
   public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
      return this.characterId == null ? false : character.getBaseId().toUpperCase().equals(this.characterId.toUpperCase());
   }

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return triggerParent.getCharacters() != null && triggerParent.getCharacters().size() != 0
         ? this.isValid(triggerParent.getCharacters().get(0), triggerParent)
         : false;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.CHARACTERIDREQUIREMENT;
   }

   public String getCharacterId() {
      return this.characterId;
   }

   public void setCharacterId(String characterId) {
      this.characterId = characterId;
   }
}
