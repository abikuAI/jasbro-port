package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import java.util.List;

public class MinimumCharacterRequirement implements ActivityRequirement {
   final CharacterRequirement requirement;
   final int minimum;

   public MinimumCharacterRequirement(CharacterRequirement requirement, int minimum) {
      this.requirement = requirement;
      this.minimum = minimum;
   }

   @Override
   public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      int count = 0;

      for (Charakter c : characters) {
         if (this.requirement.isValid(activity, c)) {
            count++;
         }
      }

      return count >= this.minimum;
   }
}
