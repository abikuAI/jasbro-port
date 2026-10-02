package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Train extends RunningActivity {
   private Charakter teacher;
   private List<Charakter> students = new ArrayList<>();
   private SpecializationType specialization;

   @Override
   public void init() {
      List<SelectionData<SpecializationType>> options = this.getTrainOptions(this.getCharacters());
      if (this.getPlannedActivity().getSelectedOption() == null) {
         SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>()
            .select(
               options,
               ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.teacher),
               null,
               this.teacher.getBackground(),
               TextUtil.t("train.option.description", this.teacher, TextUtil.listCharacters(this.students))
            );
         this.specialization = selectedOption.getSelectionObject();
      } else {
         this.specialization = (SpecializationType)this.getPlannedActivity().getSelectedOption().getSelectionObject();
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String message = TextUtil.t("train.basic", this.teacher, TextUtil.listCharacters(this.students));
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, this.teacher);
      MessageData messageData = new MessageData(message, image, this.getCharacter().getBackground());

      for (Charakter character : this.students) {
         List<ImageTag> tags = character.getBaseTags();
         tags.add(0, ImageTag.STUDY);
         image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
         messageData.addImage(image);
      }

      return messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.specialization != null) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.2F, BaseAttributeTypes.OBEDIENCE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.2F, BaseAttributeTypes.COMMAND));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05F, BaseAttributeTypes.INTELLIGENCE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0F, EssentialAttributes.ENERGY));

         for (AttributeType attributeType : this.specialization.getAssociatedAttributes()) {
            float modTeacher;
            if (attributeType instanceof BaseAttributeTypes) {
               modTeacher = 0.005F;
            } else {
               modTeacher = 0.05F;
            }

            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.teacher, modTeacher, attributeType));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.teacher, 0.3F, SpecializationAttribute.EXPERIENCE));
         }

         float modifier = 1.0F - 0.1F * (this.students.size() - 1);

         for (Charakter student : this.students) {
            for (AttributeType attributeType : this.specialization.getAssociatedAttributes()) {
               float baseMod;
               if (attributeType instanceof BaseAttributeTypes) {
                  baseMod = 0.08F;
               } else {
                  baseMod = 0.8F;
               }

               AttributeType attributeType2 = attributeType;
               if (attributeType == BaseAttributeTypes.COMMAND && student.getType() == CharacterType.SLAVE) {
                  attributeType2 = BaseAttributeTypes.OBEDIENCE;
               }

               int diff = this.teacher.getFinalValue(attributeType) - (int)student.getAttribute(attributeType2).getInternValue();
               if (diff > 0) {
                  baseMod += diff / 5.0F;
               }

               baseMod += this.teacher.getFinalValue(attributeType) / 20.0F;
               float expMod = 0.0F;
               expMod += baseMod * ((this.teacher.getIntelligence() + student.getIntelligence() - 12) / 2) / 100.0F;
               if (this.teacher.getType() == CharacterType.TRAINER && student.getType() == CharacterType.SLAVE) {
                  expMod += baseMod * ((this.teacher.getCommand() + student.getObedience() - 12) / 2) / 100.0F;
               }

               modifications.add(
                  new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, student, (baseMod + expMod) * modifier, attributeType2)
               );
            }
         }
      }

      return modifications;
   }

   @Override
   public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
      return plannedActivity.getCharacters().size() < 2 ? null : new ArrayList<>(this.getTrainOptions(plannedActivity.getCharacters()));
   }

   public List<SelectionData<SpecializationType>> getTrainOptions(List<Charakter> characters) {
      for (Charakter character : characters) {
         if (this.teacher == null) {
            this.teacher = character;
         } else if ((character.getType() != CharacterType.TRAINER || this.teacher.getType() == CharacterType.TRAINER)
            && (
               character.getType() != this.teacher.getType()
                  || character.getFinalValue(BaseAttributeTypes.COMMAND) <= this.teacher.getFinalValue(BaseAttributeTypes.COMMAND)
                     && (
                        character.getFinalValue(BaseAttributeTypes.COMMAND) != this.teacher.getFinalValue(BaseAttributeTypes.COMMAND)
                           || character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) <= this.teacher.getFinalValue(BaseAttributeTypes.INTELLIGENCE)
                     )
            )) {
            this.students.add(character);
         } else {
            this.students.add(this.teacher);
            this.teacher = character;
         }
      }

      List<SelectionData<SpecializationType>> options = new ArrayList<>();

      for (SpecializationType specialization : this.teacher.getSpecializations()) {
         if (specialization.isTeachable() && Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations().contains(specialization)) {
            SelectionData<SpecializationType> option = new SelectionData<>();
            option.setSelectionObject(specialization);
            if (specialization != SpecializationType.TRAINER && specialization != SpecializationType.SLAVE) {
               option.setButtonText(TextUtil.t("train.option", TextUtil.listCharacters(this.students), specialization.getText()));
            } else {
               option.setButtonText(TextUtil.t("train.option." + specialization.toString(), this.students));
            }

            option.setShortText(specialization.getText());
            options.add(option);
         }
      }

      return options;
   }
}
