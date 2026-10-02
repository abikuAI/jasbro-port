/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.CharacterManipulationManager;
import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;

public class UsableItemAdvanceAge
extends UsableItemEffect {
    @Override
    public String getName() {
        return "Advance age";
    }

    @Override
    public void apply(Charakter character, Item item) {
        CharacterManipulationManager.advanceAge(character);
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.ADVANCEAGE;
    }
}

