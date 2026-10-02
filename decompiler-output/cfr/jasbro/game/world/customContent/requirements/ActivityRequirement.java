/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class ActivityRequirement
extends TriggerRequirement {
    private ActivityType activityType;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return triggerParent.getActivity().getType() == this.activityType;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.ACTIVITYREQUIREMENT;
    }
}

