package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.WorldEvent;

public class SameCharacterRequirement extends TriggerRequirement {
   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      CustomQuest quest = triggerParent.getQuest();
      return quest != null ? quest.getVariable(WorldEvent.WorldEventVariables.character.toString()) == triggerParent.getCharacter() : false;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.SAMECHARACTERREQUIREMENT;
   }
}
