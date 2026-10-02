/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventProtectCharacter
extends WorldEventEffect {
    private String target = "character";

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        worldEvent.getProtectedCharacters().add((Charakter)worldEvent.getAttribute(this.target));
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.PROTECTCHARACTER;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

