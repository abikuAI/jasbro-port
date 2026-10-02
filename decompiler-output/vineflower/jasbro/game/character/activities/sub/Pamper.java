package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Pamper extends RunningActivity {
   private Charakter nurse;

   @Override
   public void init() {
      for (Charakter character : this.getCharacters()) {
         if (character.getSpecializations().contains(SpecializationType.NURSE)
            && (
               this.nurse == null
                  || character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > this.nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)
            )) {
            this.nurse = character;
         }
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String message = TextUtil.t("pamper.basic", this.nurse);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, this.nurse);
      return new MessageData(message, image, this.getCharacter().getBackground());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      this.modifyIncome(-10 * this.getCharacters().size());
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.nurse.getTraits().contains(Trait.BENEVOLENT)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2F, EssentialAttributes.MOTIVATION));
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 1.5F, SpecializationAttribute.MEDICALKNOWLEDGE));
      if (this.nurse.getTraits().contains(Trait.MAGICALHEALING)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.75F, SpecializationAttribute.MAGIC));
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, -15.0F, EssentialAttributes.ENERGY));
      if (this.nurse.getTraits().contains(Trait.ALTRUISTIC)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.6F, EssentialAttributes.MOTIVATION));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, -0.6F, EssentialAttributes.MOTIVATION));
      }

      for (Charakter character : this.getCharacters()) {
         if (character != this.nurse) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 5.0F, EssentialAttributes.HEALTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 1.0F, EssentialAttributes.MOTIVATION));
            character.addCondition(new Buff.Pretty(this.nurse, character));
         }
      }

      return modifications;
   }
}
