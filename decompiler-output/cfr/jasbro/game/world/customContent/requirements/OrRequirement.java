/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementContainer;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class OrRequirement
extends TriggerRequirementContainer {
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        for (TriggerRequirement ar : this.getSubRequirements()) {
            if (!ar.isValid(triggerParent)) continue;
            return true;
        }
        return false;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.ORREQUIREMENT;
    }
}

