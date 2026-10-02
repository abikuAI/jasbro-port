/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;
import java.util.List;

public class NoChildPresentRequirement
extends TriggerRequirement {
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        List<Charakter> characters = triggerParent.getCharacters();
        if (characters == null) {
            return true;
        }
        for (Charakter character : characters) {
            if (!character.getType().isChildType()) continue;
            return false;
        }
        return true;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.NOCHILDPRESENTREQUIREMENT;
    }
}

