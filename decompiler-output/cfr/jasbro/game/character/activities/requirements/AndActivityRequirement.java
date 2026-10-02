/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import java.util.List;

public class AndActivityRequirement
implements ActivityRequirement {
    final ActivityRequirement[] requirements;

    public AndActivityRequirement(ActivityRequirement ... requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
        for (ActivityRequirement ar : this.requirements) {
            if (ar.isValid(activity, characters, typeAmounts)) continue;
            return false;
        }
        return true;
    }
}

