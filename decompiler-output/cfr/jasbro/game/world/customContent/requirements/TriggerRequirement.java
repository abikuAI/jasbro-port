/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;
import java.util.ArrayList;
import java.util.List;

public abstract class TriggerRequirement {
    public abstract boolean isValid(TriggerParent var1) throws EvalError;

    public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
        return false;
    }

    public List<TriggerRequirement> getSubRequirements() {
        return new ArrayList<TriggerRequirement>();
    }

    public abstract TriggerRequirementType getType();

    public static enum Comparison {
        GREATERTHAN(">"),
        LESSTHAN("<"),
        EQUAL("=");

        private String displayName;

        private Comparison(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }
}

