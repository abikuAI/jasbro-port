package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventCreateQuest extends WorldEventEffect {
   private String questId;

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      CustomQuest quest = new CustomQuest(this.questId);
      worldEvent.putAttribute(WorldEvent.WorldEventVariables.questInstance, quest);
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.CREATEQUEST;
   }

   public String getQuestId() {
      return this.questId;
   }

   public void setQuestId(String questId) {
      this.questId = questId;
   }
}
