package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventChangeFame extends WorldEventEffect {
   private int value = 0;
   private String target = WorldEvent.WorldEventVariables.character.toString();

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      Charakter character = (Charakter)worldEvent.getAttribute(this.target);
      character.getFame().modifyFame(this.value);
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.CHANGEFAME;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String target) {
      this.target = target;
   }
}
