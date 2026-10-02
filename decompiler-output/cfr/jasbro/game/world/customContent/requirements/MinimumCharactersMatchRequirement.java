/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.CharacterRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementContainer;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class MinimumCharactersMatchRequirement
extends TriggerRequirementContainer {
    private int minimum;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        int count = 0;
        for (Charakter c : triggerParent.getCharacters()) {
            if (!((CharacterRequirement)((Object)this.getSubRequirements().get(0))).isValid(c, triggerParent)) continue;
            ++count;
        }
        return count >= this.minimum;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
        return triggerRequirement instanceof CharacterRequirement && this.getSubRequirements().size() < 1;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.MINIMUMCHARACTERSMATCHREQUIREMENT;
    }
}

