/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.CharacterRequirement;

public class OrCharacterRequirement
implements CharacterRequirement {
    private final CharacterRequirement[] requirements;

    public OrCharacterRequirement(CharacterRequirement ... requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean isValid(ActivityType activity, Charakter character) {
        for (CharacterRequirement requirement : this.requirements) {
            if (!requirement.isValid(activity, character)) continue;
            return true;
        }
        return false;
    }
}

