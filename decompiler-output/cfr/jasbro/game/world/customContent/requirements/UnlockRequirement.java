/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class UnlockRequirement
extends TriggerRequirement {
    private UnlockObject unlockObject;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (this.unlockObject == null) {
            return false;
        }
        if (!this.unlockObject.isLocked()) {
            return true;
        }
        return Jasbro.getInstance().getData().getUnlocks().getUnlockedObjects().contains(this.unlockObject);
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.UNLOCKREQUIREMENT;
    }

    public UnlockObject getUnlockObject() {
        return this.unlockObject;
    }

    public void setUnlockObject(UnlockObject unlockObject) {
        this.unlockObject = unlockObject;
    }
}

