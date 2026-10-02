/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffectType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class WorldEventEffect
implements Serializable {
    public abstract void perform(WorldEvent var1) throws EvalError;

    public abstract WorldEventEffectType getType();

    public List<WorldEventEffect> getSubEffects() {
        return new ArrayList<WorldEventEffect>();
    }

    public String getName() {
        return this.getType().getText();
    }

    public boolean canAddSubEffect() {
        return false;
    }
}

