/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectContainer;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;
import java.util.ArrayList;
import java.util.List;

public class UsableItemEffectContainerImpl
extends UsableItemEffect
implements UsableItemEffectContainer {
    private List<UsableItemEffect> subEffects = new ArrayList<UsableItemEffect>();

    @Override
    public void addEffect(UsableItemEffect itemEffect) {
        this.subEffects.add(itemEffect);
    }

    @Override
    public String getName() {
        return "Effect container";
    }

    @Override
    public void apply(Charakter character, Item item) {
        for (UsableItemEffect itemEffect : this.subEffects) {
            itemEffect.apply(character, item);
        }
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.EFFECTCONTAINER;
    }

    @Override
    public List<UsableItemEffect> getSubEffects() {
        return this.subEffects;
    }
}

