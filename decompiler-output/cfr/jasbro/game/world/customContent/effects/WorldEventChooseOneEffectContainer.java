/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Util;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.game.world.customContent.effects.WorldEventEffectChance;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import java.util.List;

public class WorldEventChooseOneEffectContainer
extends WorldEventEffectContainer {
    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        List<WorldEventEffect> effects = this.getSubEffects();
        int amountChance = 0;
        if (effects.size() > 0) {
            for (WorldEventEffect worldEventEffect : effects) {
                if (!(worldEventEffect instanceof WorldEventEffectChance)) continue;
                ++amountChance;
            }
            if (amountChance != effects.size()) {
                effects.get(Util.getInt(0, effects.size())).perform(worldEvent);
            } else {
                int sumChances = 0;
                for (WorldEventEffect itemEffect : effects) {
                    sumChances += ((WorldEventEffectChance)itemEffect).getChance();
                }
                int selected = Util.getInt(0, sumChances);
                sumChances = 0;
                for (WorldEventEffect itemEffect : effects) {
                    if ((sumChances += ((WorldEventEffectChance)itemEffect).getChance()) <= selected) continue;
                    ((WorldEventEffectChance)itemEffect).applyOverride(worldEvent);
                    break;
                }
            }
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.CHOOSEONEEFFECT;
    }
}

