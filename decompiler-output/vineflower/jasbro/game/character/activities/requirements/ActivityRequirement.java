package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import java.util.List;

public interface ActivityRequirement {
   boolean isValid(ActivityType var1, List<Charakter> var2, Util.TypeAmounts var3);
}
