/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.traits.Trait;

public class TraitRequirement
implements CharacterRequirement {
    private final Trait trait;

    public TraitRequirement(Trait trait) {
        this.trait = trait;
    }

    @Override
    public boolean isValid(ActivityType activity, Charakter character) {
        return character.getTraits().contains(this.trait);
    }
}

