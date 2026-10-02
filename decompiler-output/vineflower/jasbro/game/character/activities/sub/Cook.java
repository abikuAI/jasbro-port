package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Cook extends RunningActivity {
   private static final float BASEMODIFICATION = 1.0F;
   private static final float OBEDIENCEMODIFICATION = 0.01F;

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      float modification = 1.0F;
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, modification, SpecializationAttribute.COOKING));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.OBEDIENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      if (!this.getCharacter().getTraits().contains(Trait.LEGACYMAID)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.5F, BaseAttributeTypes.COMMAND));
      }

      if (this.getCharacter().getTraits().contains(Trait.RESTAURATEUR)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3F, EssentialAttributes.MOTIVATION));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3F, EssentialAttributes.MOTIVATION));
      }

      modifications.add(
         new RunningActivity.ModificationData(
            RunningActivity.TargetType.ALLHOUSE,
            0.8F + this.getCharacter().getAttribute(SpecializationAttribute.COOKING).getValue() / 12.0F,
            EssentialAttributes.HEALTH
         )
      );
      modifications.add(
         new RunningActivity.ModificationData(
            RunningActivity.TargetType.ALLHOUSE,
            4.0F + this.getCharacter().getAttribute(SpecializationAttribute.COOKING).getValue() / 6.0F,
            EssentialAttributes.ENERGY
         )
      );
      return modifications;
   }

   @Override
   public MessageData getBaseMessage() {
      Charakter character = this.getCharacters().get(0);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);
      String message = TextUtil.t("cook.basic", character);
      return new MessageData(message, image, null);
   }
}
