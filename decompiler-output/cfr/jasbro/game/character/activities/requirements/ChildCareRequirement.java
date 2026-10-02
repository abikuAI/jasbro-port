/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import java.util.List;

public class ChildCareRequirement
implements ActivityRequirement {
    @Override
    public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
        if (typeAmounts.getInfantAmount() > 0) {
            return typeAmounts.getChildAmount() == 0 && typeAmounts.getTeenAmount() == 0 && typeAmounts.isAdultPresent();
        }
        return true;
    }
}

