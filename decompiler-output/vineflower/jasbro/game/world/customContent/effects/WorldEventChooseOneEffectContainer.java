package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Util;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import java.util.List;

public class WorldEventChooseOneEffectContainer extends WorldEventEffectContainer {
   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      List<WorldEventEffect> effects = this.getSubEffects();
      int amountChance = 0;
      if (effects.size() > 0) {
         for (WorldEventEffect worldEventEffect : effects) {
            if (worldEventEffect instanceof WorldEventEffectChance) {
               amountChance++;
            }
         }

         if (amountChance != effects.size()) {
            effects.get(Util.getInt(0, effects.size())).perform(worldEvent);
         } else {
            int sumChances = 0;

            for (WorldEventEffect itemEffect : effects) {
               sumChances += ((WorldEventEffectChance)itemEffect).getChance();
            }

            int selected = Util.getInt(0, sumChances);
            sumChances = 0;

            for (WorldEventEffect itemEffect : effects) {
               sumChances += ((WorldEventEffectChance)itemEffect).getChance();
               if (sumChances > selected) {
                  ((WorldEventEffectChance)itemEffect).applyOverride(worldEvent);
                  break;
               }
            }
         }
      }
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.CHOOSEONEEFFECT;
   }
}
