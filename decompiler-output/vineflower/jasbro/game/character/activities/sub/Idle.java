package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Illness;
import jasbro.game.events.MessageData;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Idle extends RunningActivity {
   @Override
   public MessageData getBaseMessage() {
      Charakter character = this.getCharacters().get(0);
      ImageData image;
      String message;
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         message = TextUtil.t("idle.basic", character);
         image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
      } else {
         message = TextUtil.t("idle.sleep", character);
         image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
      }

      return new MessageData(message, image, character.getBackground(), true);
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0F, EssentialAttributes.ENERGY));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 20.0F, EssentialAttributes.ENERGY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, EssentialAttributes.HEALTH));
      }

      return modifications;
   }

   @Override
   public void perform() {
      if (!this.getCharacter().getType().isChildType()) {
         if (Jasbro.getInstance().getData().getTime() == Time.NIGHT && Util.getRnd().nextBoolean()) {
            Illness.Flu flu = new Illness.Flu(false);
            this.getCharacter().addCondition(flu);
            this.getMessages().get(0).addToMessage("\n\n" + flu.getTextStart());
         }
      } else {
         Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(-50000.0);
         this.getMessages().get(0).setMessage(TextUtil.t("idle.child", this.getCharacter()));
         this.getMessages().get(0).setPriorityMessage(true);
         Jasbro.getInstance().removeCharacter(this.getCharacter());
      }
   }
}
