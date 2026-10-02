/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

public interface CharacterRequirement {
    public boolean isValid(ActivityType var1, Charakter var2);
}

