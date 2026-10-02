/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class LocationTypeRequirement
extends TriggerRequirement {
    private LocationTypeInterface locationType;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        LocationTypeInterface locationTypeInterface = triggerParent.getLocation();
        if (locationTypeInterface == null) {
            return false;
        }
        return locationTypeInterface == this.locationType;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.LOCATIONTYPEREQUIREMENT;
    }

    public LocationTypeInterface getLocationType() {
        return this.locationType;
    }

    public void setLocationType(LocationTypeInterface locationType) {
        this.locationType = locationType;
    }
}

