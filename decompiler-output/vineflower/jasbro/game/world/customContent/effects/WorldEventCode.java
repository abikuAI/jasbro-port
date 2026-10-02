package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventCode extends WorldEventEffect {
   private static final Logger log = LogManager.getLogger(WorldEventCode.class);
   private String code;
   private int displayHeight;

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      if (this.code != null && !this.code.equals("")) {
         try {
            worldEvent.getInterpreter().eval(this.code);
         } catch (EvalError e) {
            log.error("({}} Error in this code: {}", new Object[]{worldEvent.getId(), this.code});
            throw (EvalError)log.throwing(e);
         }
      }
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.CODE;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public int getDisplayHeight() {
      if (this.displayHeight < 1) {
         this.displayHeight = 1;
      }

      return this.displayHeight;
   }

   public void setDisplayHeight(int displayHeight) {
      this.displayHeight = displayHeight;
   }
}
