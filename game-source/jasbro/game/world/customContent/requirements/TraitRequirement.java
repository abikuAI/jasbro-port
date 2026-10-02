package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.world.customContent.TriggerParent;

public class TraitRequirement extends TriggerRequirement implements CharacterRequirement {
   private Trait trait;

   @Override
   public boolean isValid(Charakter character, TriggerParent triggerParent) {
      return character.getTraits().contains(this.trait);
   }

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      return this.isValid(triggerParent.getCharacters().get(0), triggerParent);
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.TRAITREQUIREMENT;
   }

   public Trait getTrait() {
      return this.trait;
   }

   public void setTrait(Trait trait) {
      this.trait = trait;
   }
}
