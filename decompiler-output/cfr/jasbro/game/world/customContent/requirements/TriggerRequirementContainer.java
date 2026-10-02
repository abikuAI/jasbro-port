/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.util.ArrayList;
import java.util.List;

public abstract class TriggerRequirementContainer
extends TriggerRequirement {
    private List<TriggerRequirement> requirements = new ArrayList<TriggerRequirement>();

    @Override
    public List<TriggerRequirement> getSubRequirements() {
        return this.requirements;
    }

    @Override
    public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
        return true;
    }
}

