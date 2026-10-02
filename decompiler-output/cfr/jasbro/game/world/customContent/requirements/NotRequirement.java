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

public class NotRequirement
extends TriggerRequirementContainer {
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (this.getSubRequirements().size() < 1) {
            return true;
        }
        return !this.getSubRequirements().get(0).isValid(triggerParent);
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.NOTREQUIREMENT;
    }

    @Override
    public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
        return this.getSubRequirements().size() < 1;
    }
}

