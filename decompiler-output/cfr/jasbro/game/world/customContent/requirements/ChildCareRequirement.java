/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Util;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class ChildCareRequirement
extends TriggerRequirement {
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        Util.TypeAmounts typeAmounts = triggerParent.getTypeAmounts();
        if (typeAmounts == null) {
            return false;
        }
        if (typeAmounts.getInfantAmount() > 0) {
            return typeAmounts.getChildAmount() == 0 && typeAmounts.getTeenAmount() == 0 && typeAmounts.isAdultPresent();
        }
        return true;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.CHILDCAREREQUIREMENT;
    }
}

