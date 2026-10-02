/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.conditions.BattleCondition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class Stun
extends BattleCondition {
    private int duration;

    public Stun(Unit unit, int duration) {
        super(unit);
        this.duration = duration;
    }

    @Override
    public void handleEvent(MyEvent e) {
        Battle battle;
        if (e.getType() == EventType.NEXTSHIFT) {
            this.getUnit().removeCondition(this);
        }
        if (e.getType() == EventType.ATTACK && (battle = (Battle)e.getSource()).isAttacker(this.getUnit())) {
            battle.getAttack().setAbort(true);
            battle.addToCombatText(this.getUnit().getName() + " is stunned.");
            --this.duration;
            if (this.duration == 0) {
                this.getUnit().removeCondition(this);
            }
        }
    }
}

