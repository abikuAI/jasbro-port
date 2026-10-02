package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import java.util.List;

public class ExactOccupantRequirement implements ActivityRequirement {
   private final int count;

   public ExactOccupantRequirement(int count) {
      this.count = count;
   }

   @Override
   public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      return characters.size() == this.count;
   }
}
