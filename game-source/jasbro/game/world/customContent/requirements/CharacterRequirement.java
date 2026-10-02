package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

public interface CharacterRequirement {
   boolean isValid(Charakter var1, TriggerParent var2) throws EvalError;
}
