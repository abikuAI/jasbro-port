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
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class MaximumCharacterAmountRequirement
extends TriggerRequirement {
    private int maximum;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return triggerParent.getCharacters().size() <= this.maximum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.MAXIMUMCHARACTERAMOUNTREQUIREMENT;
    }
}

