package jasbro.game.interfaces;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;

public interface MinObedienceModifier {
   int getMinObedienceModified(int var1, Charakter var2, RunningActivity var3);
}
