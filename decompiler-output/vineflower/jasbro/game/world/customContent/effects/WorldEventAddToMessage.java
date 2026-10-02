package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MessageData;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.texts.TextUtil;

public class WorldEventAddToMessage extends WorldEventEffect {
   private String text;
   private boolean changeToPriorityMessage = false;

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      RunningActivity activity = worldEvent.getActivity();
      MessageData messageData = activity.getMessages().get(activity.getMessages().size() - 1);
      if (this.text != null) {
         messageData.addToMessage(TextUtil.getInstance().applyTemplates(this.text, worldEvent.getPeople(), worldEvent.generateAttributeMap()));
      }

      if (this.changeToPriorityMessage) {
         messageData.setPriorityMessage(true);
      }
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.ADDTOMESSAGE;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public boolean isChangeToPriorityMessage() {
      return this.changeToPriorityMessage;
   }

   public void setChangeToPriorityMessage(boolean changeToPriorityMessage) {
      this.changeToPriorityMessage = changeToPriorityMessage;
   }
}
