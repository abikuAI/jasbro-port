/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.CharacterRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class CharacterTypeRequirement
extends TriggerRequirement
implements CharacterRequirement {
    private CharacterType characerType;

    @Override
    public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
        if (this.characerType == null) {
            return true;
        }
        return character.getType().equals((Object)this.characerType);
    }

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
            return false;
        }
        return this.isValid(triggerParent.getCharacters().get(0), triggerParent);
    }

    public CharacterType getCharacterType() {
        return this.characerType;
    }

    public void setCharacterType(CharacterType characerType) {
        this.characerType = characerType;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.CHARACTERTYPEREQUIREMENT;
    }
}

