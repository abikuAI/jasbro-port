package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
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

public class Nurse extends RunningActivity {
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
      String message = TextUtil.t("nurse.basic", this.nurse);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, this.nurse);
      MessageData messageData = new MessageData(message, image, this.getCharacter().getBackground());

      for (Charakter character : this.getCharacters()) {
         if (character != this.nurse) {
            List<ImageTag> tags = character.getBaseTags();
            tags.add(0, ImageTag.SLEEP);
            image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
            messageData.addImage(image);
         }
      }

      return messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      this.modifyIncome(-10 * this.getCharacters().size());
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.5F, SpecializationAttribute.MEDICALKNOWLEDGE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.05F, BaseAttributeTypes.INTELLIGENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, -25.0F, EssentialAttributes.ENERGY));
      if (this.nurse.getTraits().contains(Trait.MAGICALHEALING)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.25F, SpecializationAttribute.MAGIC));
      }

      if (this.nurse.getTraits().contains(Trait.ALTRUISTIC)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.4F, EssentialAttributes.MOTIVATION));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, -0.5F, EssentialAttributes.MOTIVATION));
      }

      if (this.nurse.getTraits().contains(Trait.BENEVOLENT)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2F, EssentialAttributes.MOTIVATION));
      }

      for (Charakter character : this.getCharacters()) {
         if (character != this.nurse) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 40.0F, EssentialAttributes.ENERGY));
            modifications.add(
               new RunningActivity.ModificationData(
                  RunningActivity.TargetType.SINGLE,
                  character,
                  15 + this.nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 2,
                  EssentialAttributes.HEALTH
               )
            );
            if ("SPAAREA".equals(this.getRoom().getRoomInfo().getId()) && this.nurse.getTraits().contains(Trait.AQUATICNURSE)) {
               modifications.add(
                  new RunningActivity.ModificationData(
                     RunningActivity.TargetType.SINGLE,
                     character,
                     5 + this.nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 10,
                     EssentialAttributes.HEALTH
                  )
               );
            }
         }
      }

      return modifications;
   }

   public Charakter getNurse() {
      return this.nurse;
   }
}
