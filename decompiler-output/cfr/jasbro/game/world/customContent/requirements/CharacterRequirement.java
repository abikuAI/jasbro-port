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

public interface CharacterRequirement {
    public boolean isValid(Charakter var1, TriggerParent var2) throws EvalError;
}

