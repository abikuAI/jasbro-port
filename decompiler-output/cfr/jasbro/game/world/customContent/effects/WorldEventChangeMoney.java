/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventChangeMoney
extends WorldEventEffect {
    private long moneyModifier;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        if (this.moneyModifier < 0L) {
            Jasbro.getInstance().getData().spendMoney(-this.moneyModifier, null);
        } else {
            Jasbro.getInstance().getData().earnMoney(this.moneyModifier, null);
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.CHANGEMONEY;
    }

    public long getMoneyModifier() {
        return this.moneyModifier;
    }

    public void setMoneyModifier(long moneyModifier) {
        this.moneyModifier = moneyModifier;
    }
}

