package jasbro.game.character.activities.requirements;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import java.util.List;

public class AllCharacterRequirement implements ActivityRequirement {
   private final CharacterRequirement requirement;

   public AllCharacterRequirement(CharacterRequirement requirement) {
      this.requirement = requirement;
   }

   @Override
   public boolean isValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      for (Charakter c : characters) {
         if (!this.requirement.isValid(activity, c)) {
            return false;
         }
      }

      return true;
   }
}
