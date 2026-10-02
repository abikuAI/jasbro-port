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

public class MinimumCharacterAmountRequirement
extends TriggerRequirement {
    private int minimum;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return triggerParent.getCharacters().size() >= this.minimum;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.MINIMUMCHARACTERAMOUNTREQUIREMENT;
    }
}

