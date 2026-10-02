package jasbro.game.character.activities.sub.childcare;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Play extends RunningActivity {
   @Override
   public MessageData getBaseMessage() {
      MessageData messageData = new MessageData();

      for (Charakter character : this.getCharacters()) {
         messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.PLAY, character));
      }

      Object[] arguments = new Object[]{this.getCharacterLocation()};
      if (this.getHouse() != null) {
         messageData.addToMessage(TextUtil.t("play.basic.room", this.getCharacters(), arguments));
      } else {
         messageData.addToMessage(TextUtil.t("play.basic.outside", this.getCharacters(), arguments));
      }

      return messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -30.0F, EssentialAttributes.ENERGY));
      return modifications;
   }
}
