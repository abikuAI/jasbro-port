package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.housing.Room;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Govern extends RunningActivity {
   private SpecializationType specialization;
   private List<Charakter> students = new ArrayList<>();
   private BaseAttributeTypes statBoost;
   private SpecializationAttribute skillBoost1 = null;
   private SpecializationAttribute skillBoost2 = null;
   private Sextype sexBoost1 = null;
   private Sextype sexBoost2 = null;

   @Override
   public void init() {
      List<SelectionData<SpecializationType>> options = this.getSuperviseOptions(this.getCharacter());
      if (this.getPlannedActivity().getSelectedOption() == null) {
         SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>()
            .select(
               options,
               ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacters().get(0)),
               null,
               this.getCharacters().get(0).getBackground(),
               TextUtil.t("train.option.description", this.getCharacters().get(0), TextUtil.listCharacters(this.students))
            );
         this.specialization = selectedOption.getSelectionObject();
      } else {
         this.specialization = (SpecializationType)this.getPlannedActivity().getSelectedOption().getSelectionObject();
      }

      for (Room room : this.getHouse().getRooms()) {
         for (Charakter character : room.getCurrentUsage().getCharacters()) {
            if (character.getActivity().getType() == ActivityType.WHORE && this.specialization == SpecializationType.WHORE) {
               this.students.add(character);
               if (Util.getInt(0, 10) > 5) {
                  this.statBoost = BaseAttributeTypes.CHARISMA;
               } else {
                  this.statBoost = BaseAttributeTypes.STAMINA;
               }

               this.skillBoost1 = SpecializationAttribute.SEDUCTION;
               switch (Util.getInt(0, 7)) {
                  case 1:
                     this.sexBoost1 = Sextype.TITFUCK;
                     break;
                  case 2:
                     this.sexBoost1 = Sextype.ANAL;
                     break;
                  case 3:
                     this.sexBoost1 = Sextype.ORAL;
                     break;
                  case 4:
                     this.sexBoost1 = Sextype.ANAL;
                     break;
                  case 5:
                     this.sexBoost2 = Sextype.FOREPLAY;
                     break;
                  default:
                     this.sexBoost1 = Sextype.VAGINAL;
               }

               switch (Util.getInt(0, 7)) {
                  case 1:
                     this.sexBoost2 = Sextype.TITFUCK;
                     break;
                  case 2:
                     this.sexBoost2 = Sextype.ANAL;
                     break;
                  case 3:
                     this.sexBoost2 = Sextype.ORAL;
                     break;
                  case 4:
                     this.sexBoost2 = Sextype.ANAL;
                     break;
                  case 5:
                     this.sexBoost2 = Sextype.FOREPLAY;
                     break;
                  default:
                     this.sexBoost2 = Sextype.VAGINAL;
               }
            } else if (character.getActivity().getType() == ActivityType.PUBLICUSE && this.specialization == SpecializationType.KINKYSEX) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.STAMINA;
               this.sexBoost1 = Sextype.GROUP;
            } else if (character.getActivity().getType() == ActivityType.CLEAN && this.specialization == SpecializationType.KINKYSEX) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.OBEDIENCE;
               this.skillBoost1 = SpecializationAttribute.CLEANING;
            } else if (character.getActivity().getType() == ActivityType.COOK && this.specialization == SpecializationType.KINKYSEX) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.OBEDIENCE;
               this.skillBoost1 = SpecializationAttribute.COOKING;
            } else if (character.getActivity().getType() == ActivityType.SELLFOOD && this.specialization == SpecializationType.KINKYSEX) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.OBEDIENCE;
               this.skillBoost1 = SpecializationAttribute.COOKING;
            } else if (character.getActivity().getType() == ActivityType.SUCK && this.specialization == SpecializationType.WHORE) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.STAMINA;
               this.skillBoost1 = SpecializationAttribute.SEDUCTION;
               this.sexBoost1 = Sextype.ORAL;
            } else if (character.getActivity().getType() == ActivityType.TEASE && this.specialization == SpecializationType.WHORE) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.STAMINA;
               this.skillBoost1 = SpecializationAttribute.SEDUCTION;
               this.sexBoost1 = Sextype.FOREPLAY;
            } else if (character.getActivity().getType() == ActivityType.BARTEND && this.specialization == SpecializationType.BARTENDER) {
               this.students.add(character);
               if (Util.getInt(0, 10) > 5) {
                  this.statBoost = BaseAttributeTypes.CHARISMA;
               } else {
                  this.statBoost = BaseAttributeTypes.INTELLIGENCE;
               }

               this.skillBoost1 = SpecializationAttribute.BARTENDING;
            } else if (character.getActivity().getType() == ActivityType.STRIP && this.specialization == SpecializationType.DANCER) {
               this.students.add(character);
               if (Util.getInt(0, 10) > 5) {
                  this.statBoost = BaseAttributeTypes.CHARISMA;
               } else {
                  this.statBoost = BaseAttributeTypes.STAMINA;
               }

               this.skillBoost1 = SpecializationAttribute.STRIP;
            } else if (this.specialization == SpecializationType.TRAINER) {
               this.students.add(character);
               this.statBoost = BaseAttributeTypes.OBEDIENCE;
            }
         }
      }
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.08F, BaseAttributeTypes.COMMAND));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.08F, SpecializationAttribute.EXPERIENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      if (this.students != null) {
         for (Charakter character : this.students) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1F, EssentialAttributes.MOTIVATION));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05F, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.02F, this.statBoost));
            if (this.skillBoost1 != null) {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.5F, this.skillBoost1));
            }

            if (this.skillBoost2 != null) {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.5F, this.skillBoost2));
            }

            if (this.sexBoost1 != null) {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.8F, this.sexBoost1));
            }

            if (this.sexBoost2 != null) {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.8F, this.sexBoost2));
            }
         }
      }

      return modifications;
   }

   @Override
   public MessageData getBaseMessage() {
      Charakter character = this.getCharacters().get(0);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);
      String message = TextUtil.t("govern.basic1", character, character.getBackground());
      if (this.students.size() != 0 && this.specialization != SpecializationType.TRAINER) {
         message = message + " " + TextUtil.t("govern.specific", this.students);
      } else if (this.specialization == SpecializationType.TRAINER) {
         message = TextUtil.t("govern.basic2", character, character.getBackground());
      } else {
         message = TextUtil.t("govern.none", character, character.getBackground());
      }

      return new MessageData(message, image, null);
   }

   @Override
   public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
      return new ArrayList<>(this.getSuperviseOptions(plannedActivity.getCharacters().get(0)));
   }

   public List<SelectionData<SpecializationType>> getSuperviseOptions(Charakter character) {
      List<SelectionData<SpecializationType>> options = new ArrayList<>();

      for (SpecializationType specialization : character.getSpecializations()) {
         if (specialization.isTeachable()
            && Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations().contains(specialization)
            && (
               specialization == SpecializationType.WHORE
                  || specialization == SpecializationType.BARTENDER
                  || specialization == SpecializationType.TRAINER
                  || specialization == SpecializationType.DANCER
                  || specialization == SpecializationType.KINKYSEX
                  || specialization == SpecializationType.NURSE
            )) {
            SelectionData<SpecializationType> option = new SelectionData<>();
            option.setSelectionObject(specialization);
            switch (specialization) {
               case TRAINER:
                  option.setButtonText(TextUtil.t("supervise.option.all"));
                  option.setShortText(specialization.getText());
                  break;
               case WHORE:
                  option.setButtonText(TextUtil.t("supervise.option.whore"));
                  option.setShortText(specialization.getText());
                  break;
               case BARTENDER:
                  option.setButtonText(TextUtil.t("supervise.option.bartender"));
                  option.setShortText(specialization.getText());
                  break;
               case DANCER:
                  option.setButtonText(TextUtil.t("supervise.option.dancer"));
                  option.setShortText(specialization.getText());
                  break;
               case KINKYSEX:
                  option.setButtonText(TextUtil.t("supervise.option.kinky"));
                  option.setShortText(specialization.getText());
                  break;
               case NURSE:
                  option.setButtonText(TextUtil.t("supervise.option.nurse"));
                  option.setShortText(specialization.getText());
            }

            option.setShortText(specialization.getText());
            options.add(option);
         }
      }

      return options;
   }
}
