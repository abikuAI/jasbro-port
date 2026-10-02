/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.CharacterRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementContainer;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class MainCharacterRequirement
extends TriggerRequirementContainer
implements CharacterRequirement {
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (this.getSubRequirements().size() < 1) {
            return false;
        }
        return ((CharacterRequirement)((Object)this.getSubRequirements().get(0))).isValid(Jasbro.getInstance().getData().getProtagonist(), triggerParent);
    }

    @Override
    public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
        return triggerRequirement instanceof CharacterRequirement && this.getSubRequirements().size() < 1;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.MAINCHARACTERREQUIREMENT;
    }

    @Override
    public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
        return character == Jasbro.getInstance().getData().getProtagonist();
    }
}

