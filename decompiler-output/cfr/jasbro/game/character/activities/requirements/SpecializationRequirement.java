/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.specialization.SpecializationType;

public class SpecializationRequirement
implements CharacterRequirement {
    final SpecializationType specialization;

    public SpecializationRequirement(SpecializationType specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean isValid(ActivityType activity, Charakter character) {
        return character.getSpecializations().contains(this.specialization);
    }
}

