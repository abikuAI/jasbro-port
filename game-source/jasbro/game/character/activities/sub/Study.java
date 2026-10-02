package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
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

public class Study extends RunningActivity {
   private SpecializationType specialization;

   @Override
   public void init() {
      List<SelectionData<SpecializationType>> options = new ArrayList<>();

      for (SpecializationType specialization : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
         if (specialization != SpecializationType.TRAINER
            && specialization != SpecializationType.SLAVE
            && specialization != SpecializationType.UNDERAGE
            && specialization.isTeachable()) {
            SelectionData<SpecializationType> option = new SelectionData<>();
            option.setSelectionObject(specialization);
            String type;
            if (specialization != SpecializationType.SEX && specialization != SpecializationType.KINKYSEX) {
               type = "specialization";
            } else {
               type = "sex";
            }

            if (this.isBasicTraining(specialization, this.getCharacter())) {
               Object[] arguments = new Object[]{specialization.getText()};
               option.setButtonText(TextUtil.t("study.option." + type + ".free", this.getCharacter(), arguments));
            } else {
               int price = 1000;
               if (this.isHiddenTraining(specialization, this.getCharacter())
                  && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY)) {
                  price = 20000;
               } else if (this.isAdvancedTraining(specialization, this.getCharacter())) {
                  price = 10000;
               }

               if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTLIBRARY)) {
                  price = (int)(price * 0.8);
               }

               Object[] arguments = new Object[]{specialization.getText(), price};
               option.setButtonText(TextUtil.t("study.option." + type + ".costs", this.getCharacter(), arguments));
               if (!Jasbro.getInstance().getData().canAfford(price)) {
                  option.setEnabled(false);
               }
            }

            options.add(option);
         }
      }

      SelectionData<SpecializationType> option = new SelectionData<>();
      option.setButtonText(TextUtil.t("ui.cancel"));
      options.add(option);
      SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>()
         .select(
            options,
            ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()),
            null,
            this.getCharacter().getBackground(),
            TextUtil.t("study.option.description", this.getCharacter())
         );
      this.specialization = selectedOption.getSelectionObject();
      if (this.specialization != null) {
         int price = 0;
         short var11;
         if (this.isHiddenTraining(this.specialization, this.getCharacter())
            && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY)) {
            var11 = 20000;
         } else if (this.isAdvancedTraining(this.specialization, this.getCharacter())) {
            var11 = 10000;
         } else if (!this.isBasicTraining(this.specialization, this.getCharacter())) {
            var11 = 1000;
         } else {
            var11 = 0;
         }

         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTLIBRARY)) {
            var11 *= 0;
         }

         Jasbro.getInstance().getData().spendMoney(var11, this);
      }
   }

   @Override
   public MessageData getBaseMessage() {
      if (this.specialization != null) {
         String message = TextUtil.t("study.basic", this.getCharacter(), this.specialization.getText());
         ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, this.getCharacter());
         return new MessageData(message, image, this.getCharacter().getBackground());
      } else {
         return new MessageData(
            TextUtil.t("idle.basic", this.getCharacter()),
            ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()),
            this.getCharacter().getBackground(),
            true
         );
      }
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      if (this.specialization == null) {
         return new Idle().getStatModifications();
      }

      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.INTELLIGENCE));
      if (this.specialization != null) {
         if (this.getCharacters().get(0).getTraits().contains(Trait.CLEVER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3F, EssentialAttributes.MOTIVATION));
         } else if (this.getCharacters().get(0).getTraits().contains(Trait.STUPID)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3F, EssentialAttributes.MOTIVATION));
         }

         float change = (5.0F + this.specialization.getAssociatedAttributes().size() - 1.0F) / this.specialization.getAssociatedAttributes().size();
         if (this.isHiddenTraining(this.specialization, this.getCharacter())
            && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY)) {
            change *= 4.0F;
         } else if (this.isAdvancedTraining(this.specialization, this.getCharacter())) {
            change *= 1.0F;
         }

         for (AttributeType attributeType : this.specialization.getAssociatedAttributes()) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, change, attributeType));
         }
      }

      return modifications;
   }

   private boolean isBasicTraining(SpecializationType specialization, Charakter character) {
      if (character.getSpecializations().contains(specialization)) {
         return false;
      }

      if (specialization == SpecializationType.DOMINATRIX) {
         return character.getAttribute(SpecializationAttribute.DOMINATE).getInternValue() > 1.0F;
      }

      for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
         if (character.getAttribute(attributeType).getInternValue() > 1.0F) {
            return false;
         }
      }

      return true;
   }

   private boolean isAdvancedTraining(SpecializationType specialization, Charakter character) {
      for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
         if (character.getAttribute(attributeType).getInternValue() >= 20.0F) {
            return true;
         }
      }

      return false;
   }

   private boolean isHiddenTraining(SpecializationType specialization, Charakter character) {
      for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
         if (character.getAttribute(attributeType).getInternValue() >= 50.0F) {
            return true;
         }
      }

      return false;
   }
}
