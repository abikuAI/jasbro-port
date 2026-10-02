package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

public interface CharacterRequirement {
   boolean isValid(ActivityType var1, Charakter var2);
}
