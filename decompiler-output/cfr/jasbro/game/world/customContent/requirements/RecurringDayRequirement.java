/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class RecurringDayRequirement
extends TriggerRequirement {
    private int everyXDays;
    private int offset;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (this.everyXDays == 0) {
            return false;
        }
        int currentDay = Jasbro.getInstance().getData().getDay();
        if (currentDay == 0) {
            return false;
        }
        if (currentDay < this.offset / this.everyXDays) {
            return false;
        }
        return currentDay % this.everyXDays == this.offset % this.everyXDays;
    }

    public int getEveryXDays() {
        return this.everyXDays;
    }

    public void setEveryXDays(int everyXDays) {
        this.everyXDays = everyXDays;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.RECURRINGDAYREQUIREMENT;
    }
}

