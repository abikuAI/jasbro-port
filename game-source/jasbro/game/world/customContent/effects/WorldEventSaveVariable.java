package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventSaveVariable extends WorldEventEffect {
   private String source = "character";
   private String target = "character";

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      if (this.source != null && this.target != null) {
         worldEvent.getQuest().setVariable(this.target, worldEvent.getAttribute(this.source));
      }
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.SAVEVARIABLE;
   }

   public String getSource() {
      return this.source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String target) {
      this.target = target;
   }
}
