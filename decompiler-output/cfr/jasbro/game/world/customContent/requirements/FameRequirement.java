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
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class FameRequirement
extends TriggerRequirement
implements CharacterRequirement {
    private long fameRequired;

    @Override
    public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
        return character.getFame().getFame() >= this.fameRequired;
    }

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
            return false;
        }
        return this.isValid(triggerParent.getCharacters().get(0), triggerParent);
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.FAMEREQUIREMENT;
    }

    public long getFameRequired() {
        return this.fameRequired;
    }

    public void setFameRequired(long fameRequired) {
        this.fameRequired = fameRequired;
    }
}

