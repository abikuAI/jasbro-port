package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import java.util.List;

public class ChildCareRequirement implements ActivityRequirement {
   @Override
   public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      return typeAmounts.getInfantAmount() > 0 ? typeAmounts.getChildAmount() == 0 && typeAmounts.getTeenAmount() == 0 && typeAmounts.isAdultPresent() : true;
   }
}
