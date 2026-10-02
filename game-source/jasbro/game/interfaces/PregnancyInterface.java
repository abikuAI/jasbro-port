package jasbro.game.interfaces;

import jasbro.game.character.Charakter;
import jasbro.game.events.MyEvent;

public interface PregnancyInterface {
   void reduceDays(int var1);

   void modifyDays(int var1);

   int getDays();

   void setCharacter(Charakter var1);

   void handleEvent(MyEvent var1);
}
