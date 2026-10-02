package jasbro.game.character.battle;

import jasbro.game.character.Condition;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;

public interface Unit extends Person {
   int getHitpoints();

   int getMaxHitpoints();

   float modifyHitpoints(float var1);

   float getDamage();

   int getArmor();

   float takeDamage(float var1);

   int getDodge();

   int getHit();

   int getSpeed();

   int getCritChance();

   int getCritDamageBonus();

   int getBlockChance();

   int getBlockAmount();

   Attack getAttack(Battle var1);

   void handleEvent(MyEvent var1);

   int getResistance(DamageType var1);

   void addCondition(Condition var1);

   void removeCondition(Condition var1);
}
