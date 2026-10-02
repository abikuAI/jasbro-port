package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Talk extends RunningActivity {
   private Charakter character1;
   private Charakter character2;
   private Talk.TalkType talkType;

   @Override
   public void init() {
      Charakter tmpCharacter1 = this.getCharacters().get(0);
      Charakter tmpCharacter2 = this.getCharacters().get(1);
      if ((tmpCharacter1.getType() != CharacterType.TRAINER || tmpCharacter2.getType() == CharacterType.TRAINER)
         && (
            tmpCharacter1.calculateValue() <= tmpCharacter2.calculateValue()
               || tmpCharacter1.getType() != CharacterType.TRAINER && tmpCharacter2.getType() == CharacterType.TRAINER
         )) {
         this.character1 = tmpCharacter2;
         this.character2 = tmpCharacter1;
      } else {
         this.character1 = tmpCharacter1;
         this.character2 = tmpCharacter2;
      }

      if (this.getPlannedActivity().getSelectedOption() == null) {
         List<SelectionData<Talk.TalkType>> options = this.getTalkOptions(this.character1, this.character2);
         SelectionData<Talk.TalkType> selectedOption = new SelectionScreen<Talk.TalkType>()
            .select(
               options,
               ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character1),
               null,
               this.character1.getBackground(),
               TextUtil.t("talk.decision", this.character1, this.character2)
            );
         this.talkType = selectedOption.getSelectionObject();
      } else {
         this.talkType = (Talk.TalkType)this.getPlannedActivity().getSelectedOption().getSelectionObject();
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String message = this.talkType.getTalkDescription(this.character1, this.character2);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character2);
      if (this.talkType != Talk.TalkType.PET) {
         image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character2);
      } else {
         image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CATGIRL, this.character2);
      }

      return new MessageData(message, image, this.getCharacter().getBackground());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.character1.getType() == CharacterType.TRAINER && this.character2.getType() == CharacterType.SLAVE) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.4F, BaseAttributeTypes.OBEDIENCE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.4F, BaseAttributeTypes.COMMAND));
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2F, BaseAttributeTypes.INTELLIGENCE));
      if (this.talkType == Talk.TalkType.PET) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character2, 15.0F, EssentialAttributes.ENERGY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character2, 1.5F, SpecializationAttribute.CATGIRL));
      }

      return modifications;
   }

   @Override
   public void perform() {
      this.talkType.applyModifier(this.character1, this.character2);
   }

   @Override
   public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
      return plannedActivity.getCharacters().size() < 2
         ? null
         : new ArrayList<>(this.getTalkOptions(plannedActivity.getCharacters().get(0), plannedActivity.getCharacters().get(1)));
   }

   public List<SelectionData<Talk.TalkType>> getTalkOptions(Charakter character1, Charakter character2) {
      if ((character1.getType() != CharacterType.TRAINER || character2.getType() == CharacterType.TRAINER)
         && (
            character1.calculateValue() <= character2.calculateValue()
               || character1.getType() != CharacterType.TRAINER && character2.getType() == CharacterType.TRAINER
         )) {
         Charakter tmpCharacter = character1;
         character1 = character2;
         character2 = tmpCharacter;
      }

      List<SelectionData<Talk.TalkType>> options = new ArrayList<>();

      for (Talk.TalkType talk : Talk.TalkType.values()) {
         if (talk != Talk.TalkType.PET || character2.getSpecializations().contains(SpecializationType.CATGIRL)) {
            SelectionData<Talk.TalkType> selectionData = new SelectionData<>();
            selectionData.setButtonText(talk.getText());
            selectionData.setSelectionObject(talk);
            selectionData.setTooltipText(talk.getDescription(character1, character2));
            selectionData.setShortText(talk.getText());
            options.add(selectionData);
         }
      }

      return options;
   }

   public enum TalkType {
      INTIMIDATE,
      MOTIVATE,
      PET;

      public String getText() {
         return TextUtil.t("talk." + this.toString());
      }

      public String getDescription(Charakter character1, Charakter character2) {
         return TextUtil.t("talk." + this.toString() + ".description", character1, character2);
      }

      public void applyModifier(Charakter character1, Charakter character2) {
         if (this == INTIMIDATE) {
            character2.addCondition(new Buff.Intimidated(character1));
         } else if (this == MOTIVATE) {
            character2.addCondition(new Buff.Motivated(character1));
         }
      }

      public String getTalkDescription(Charakter character1, Charakter character2) {
         return TextUtil.t("talk." + this.toString() + ".basic", character1, character2);
      }
   }
}
