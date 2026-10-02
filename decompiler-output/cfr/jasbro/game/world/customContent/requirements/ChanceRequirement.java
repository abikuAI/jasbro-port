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

public class ChanceRequirement
extends TriggerRequirement {
    private int chance = 100;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return Util.getInt(0, 100) < this.chance;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.CHANCEREQUIREMENT;
    }

    public int getChance() {
        return this.chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}

