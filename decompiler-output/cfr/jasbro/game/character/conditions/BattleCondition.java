/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.game.character.Condition;
import jasbro.game.character.battle.Unit;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class BattleCondition
extends Condition {
    private Unit unit;

    public BattleCondition(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTSHIFT) {
            this.getCharacter().removeCondition(this);
        }
        super.handleEvent(e);
    }
}

