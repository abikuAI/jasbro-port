/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.CharacterRequirement;

public class CharacterTypeRequirement
implements CharacterRequirement {
    private final CharacterType type;

    public CharacterTypeRequirement(CharacterType type) {
        this.type = type;
    }

    @Override
    public boolean isValid(ActivityType activity, Charakter character) {
        return character.getType().equals((Object)this.type);
    }
}

