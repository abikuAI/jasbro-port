/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectContainer;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;
import java.util.ArrayList;
import java.util.List;

public class UsableItemEffectChance
extends UsableItemEffect
implements UsableItemEffectContainer {
    private List<UsableItemEffect> subEffects = new ArrayList<UsableItemEffect>();
    private int chance;

    @Override
    public void apply(Charakter character, Item item) {
        if (Util.getInt(0, 100) < this.chance) {
            for (UsableItemEffect itemEffect : this.subEffects) {
                itemEffect.apply(character, item);
            }
        }
    }

    public void applyOverride(Charakter character, Item item) {
        for (UsableItemEffect itemEffect : this.subEffects) {
            itemEffect.apply(character, item);
        }
    }

    @Override
    public void addEffect(UsableItemEffect itemEffect) {
        this.subEffects.add(itemEffect);
    }

    @Override
    public String getName() {
        return "Effect chance";
    }

    @Override
    public List<UsableItemEffect> getSubEffects() {
        return this.subEffects;
    }

    public int getChance() {
        return this.chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.EFFECTCHANCE;
    }
}

