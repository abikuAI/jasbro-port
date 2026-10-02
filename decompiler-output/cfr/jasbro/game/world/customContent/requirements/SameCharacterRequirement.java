/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class SameCharacterRequirement
extends TriggerRequirement {
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        CustomQuest quest = triggerParent.getQuest();
        if (quest != null) {
            return quest.getVariable(WorldEvent.WorldEventVariables.character.toString()) == triggerParent.getCharacter();
        }
        return false;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.SAMECHARACTERREQUIREMENT;
    }
}

