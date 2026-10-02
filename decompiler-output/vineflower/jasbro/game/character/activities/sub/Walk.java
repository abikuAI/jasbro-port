package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Walk extends RunningActivity {
   @Override
   public MessageData getBaseMessage() {
      StringBuilder builder = new StringBuilder(TextUtil.t("walk.basic", this.getCharacters()));
      builder.append(" ");
      List<ImageData> images = new ArrayList<>();

      for (Charakter character : this.getCharacters()) {
         images.addAll(character.getImages());
      }

      return new MessageData(builder.toString(), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, images), this.getCharacterLocation().getImage());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 8.0F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0F, EssentialAttributes.ENERGY));
      return modifications;
   }
}
