/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;

public class UsableItemAddSpecialization
extends UsableItemEffect {
    private SpecializationType specializationType;

    @Override
    public String getName() {
        return "Add specialization";
    }

    @Override
    public void apply(Charakter character, Item item) {
        if (this.specializationType != null && !character.getSpecializations().contains(this.specializationType)) {
            if (character.getType() == CharacterType.TRAINER) {
                character.addSpecializationDespiteLimit(this.specializationType);
            } else {
                character.addSpecialization(this.specializationType);
            }
        }
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.ADDSPECIALIZATION;
    }

    public SpecializationType getSpecializationType() {
        return this.specializationType;
    }

    public void setSpecializationType(SpecializationType specializationType) {
        this.specializationType = specializationType;
    }
}

