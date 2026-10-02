package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Util;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import java.util.ArrayList;
import java.util.List;

public class WorldEventEffectChance extends WorldEventEffect {
   private List<WorldEventEffect> subEffects = new ArrayList<>();
   private int chance;

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      if (Util.getInt(0, 100) < this.chance) {
         for (WorldEventEffect itemEffect : this.subEffects) {
            itemEffect.perform(worldEvent);
         }
      }
   }

   public void applyOverride(WorldEvent worldEvent) throws EvalError {
      for (WorldEventEffect itemEffect : this.subEffects) {
         itemEffect.perform(worldEvent);
      }
   }

   public void addEffect(WorldEventEffect itemEffect) {
      this.subEffects.add(itemEffect);
   }

   @Override
   public List<WorldEventEffect> getSubEffects() {
      return this.subEffects;
   }

   public int getChance() {
      return this.chance;
   }

   public void setChance(int chance) {
      this.chance = chance;
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.EFFECTCHANCE;
   }
}
