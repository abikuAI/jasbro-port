/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import java.util.ArrayList;
import java.util.List;

public class WorldEventEffectContainer
extends WorldEventEffect {
    private List<WorldEventEffect> subEffects = new ArrayList<WorldEventEffect>();

    public void addEffect(WorldEventEffect worldEventEffect) {
        this.subEffects.add(worldEventEffect);
    }

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        for (WorldEventEffect eventEffect : this.subEffects) {
            eventEffect.perform(worldEvent);
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.EFFECTCONTAINER;
    }

    @Override
    public List<WorldEventEffect> getSubEffects() {
        return this.subEffects;
    }

    @Override
    public boolean canAddSubEffect() {
        return true;
    }
}

