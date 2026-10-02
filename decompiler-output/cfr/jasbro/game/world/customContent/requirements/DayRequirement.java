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

public class DayRequirement
extends TriggerRequirement {
    private int day;
    private TriggerRequirement.Comparison dayComparison = TriggerRequirement.Comparison.GREATERTHAN;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        int currentDay = Jasbro.getInstance().getData().getDay();
        switch (this.dayComparison) {
            case GREATERTHAN: {
                return currentDay > this.day;
            }
            case LESSTHAN: {
                return currentDay < this.day;
            }
            case EQUAL: {
                return currentDay == this.day;
            }
        }
        return false;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.DAYREQUIREMENT;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public TriggerRequirement.Comparison getDayComparison() {
        return this.dayComparison;
    }

    public void setDayComparison(TriggerRequirement.Comparison dayComparison) {
        this.dayComparison = dayComparison;
    }
}

