/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class MoneyRequirement
extends TriggerRequirement {
    private long money = 0L;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return this.money <= Jasbro.getInstance().getData().getMoney();
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.MONEYREQUIREMENT;
    }

    public long getMoney() {
        return this.money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}

