/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.conditions.ItemCooldown;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;

public class UsableItemCooldown
extends UsableItemEffect {
    private int time;

    @Override
    public String getName() {
        return "Cooldown";
    }

    @Override
    public void apply(Charakter character, Item item) {
        character.addCondition(new ItemCooldown(this.time, item.getId()));
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.COOLDOWN;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

