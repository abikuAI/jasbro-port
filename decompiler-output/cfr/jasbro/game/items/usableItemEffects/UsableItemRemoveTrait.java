/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;

public class UsableItemRemoveTrait
extends UsableItemEffect {
    private Trait trait;

    @Override
    public void apply(Charakter character, Item item) {
        if (this.trait != null) {
            character.removeTrait(this.trait);
        }
    }

    @Override
    public String getName() {
        return "Remove Trait";
    }

    public Trait getTrait() {
        return this.trait;
    }

    public void setTrait(Trait trait) {
        this.trait = trait;
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.REMOVETRAIT;
    }
}

