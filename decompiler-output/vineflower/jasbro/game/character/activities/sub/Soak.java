package jasbro.game.character.activities.sub;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Soak extends RunningActivity {
   @Override
   public MessageData getBaseMessage() {
      String message = TextUtil.t("soak.basic", this.getCharacter());
      if (Util.getInt(0, 3) == 2) {
         this.getCharacter().addCondition(new Buff.SmoothSkin(4, this.getCharacter()));
      }

      return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, this.getCharacter()), this.getCharacterLocation().getImage());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.3F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 20.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.CHARISMA));
      return modifications;
   }
}
