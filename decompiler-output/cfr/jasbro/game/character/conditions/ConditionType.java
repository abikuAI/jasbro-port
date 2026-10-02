/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.game.character.Condition;
import jasbro.game.character.conditions.Illness;

public enum ConditionType {
    FLU(Illness.Flu.class),
    SMALLPOX(Illness.Smallpox.class),
    ILLNESS(Illness.class);

    private Class<? extends Condition> conditionClass;

    private ConditionType(Class<? extends Condition> conditionClass) {
        this.conditionClass = conditionClass;
    }

    public Class<? extends Condition> getConditionClass() {
        return this.conditionClass;
    }
}

