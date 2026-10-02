/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;

public class UsableItemSpeedUpPregnancy
extends UsableItemEffect {
    private int days = 90;

    @Override
    public String getName() {
        return "Speed up pregnancy";
    }

    @Override
    public void apply(Charakter character, Item item) {
        for (Condition condition : character.getConditions()) {
            if (!(condition instanceof PregnancyInterface)) continue;
            ((PregnancyInterface)((Object)condition)).modifyDays(-this.days);
        }
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.SPEEDUPPREGNANCY;
    }

    public int getDays() {
        return this.days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}

