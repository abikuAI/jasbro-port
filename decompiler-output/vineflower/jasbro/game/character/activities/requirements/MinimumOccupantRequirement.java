package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import java.util.List;

public class MinimumOccupantRequirement implements ActivityRequirement {
   private final int minimum;

   public MinimumOccupantRequirement(int minimum) {
      this.minimum = minimum;
   }

   @Override
   public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      return characters.size() >= this.minimum;
   }
}
