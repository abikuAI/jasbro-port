package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publicize extends RunningActivity {
   private MessageData message;
   private Charakter character;
   private int effectiveness = 8;
   private Map<Charakter, Publicize.AdvAction> characterAction = new HashMap<>();

   @Override
   public void init() {
      this.character = this.getCharacter();
   }

   @Override
   public MessageData getBaseMessage() {
      if (this.message == null) {
         List<ImageTag> tags = this.character.getBaseTags();
         tags.add(0, ImageTag.ADVERTISE);
         tags.add(1, ImageTag.CLEANED);
         Object[] arguments = new Object[]{this.getCharacterLocation().getName()};
         this.message = new MessageData(
            TextUtil.t("advertise.basic", this.character, arguments),
            ImageUtil.getInstance().getImageDataByTags(tags, this.character.getImages()),
            this.getCharacterLocation().getImage()
         );
         if (!this.character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
            this.message.addToMessage(TextUtil.t("advertise.hintTraining", this.character));
         }
      }

      return this.message;
   }

   @Override
   public void perform() {
      List<Publicize.AdvAction> actions = new ArrayList<>();
      actions.add(Publicize.AdvAction.NORMAL);
      actions.add(Publicize.AdvAction.NORMAL);
      if (this.character.getTraits().contains(Trait.BIGBOOBS) && (this.character.getObedience() > 10 || this.character.getType() == CharacterType.TRAINER)) {
         actions.add(Publicize.AdvAction.BIGBREASTS);
      }

      if (this.character.getTraits().contains(Trait.LOLI)) {
         actions.add(Publicize.AdvAction.LOLI);
      }

      if (this.character.getTraits().contains(Trait.CLUMSY)) {
         actions.add(Publicize.AdvAction.CLUMSY);
      }

      if (this.character.getTraits().contains(Trait.SHY)
         && this.character.getCharisma() > 20
         && this.character.getFinalValue(SpecializationAttribute.ADVERTISING) > 30) {
         actions.add(Publicize.AdvAction.SHY);
      }

      if (this.character.getTraits().contains(Trait.OUTGOING)) {
         actions.add(Publicize.AdvAction.BRIGHT);
      }

      if (this.character.getTraits().contains(Trait.UNINHIBITED)) {
         actions.add(Publicize.AdvAction.BRIGHT);
      }

      if (this.character.getTraits().contains(Trait.NICEBODY)) {
         actions.add(Publicize.AdvAction.NICEBODY);
      }

      if (this.character.getTraits().contains(Trait.EXHIBITIONIST)) {
         actions.add(Publicize.AdvAction.NAKED);
      }

      if (this.character.getTraits().contains(Trait.EXHIBITIONIST) && this.character.getTraits().contains(Trait.SEXFREAK)) {
         actions.add(Publicize.AdvAction.BODYPAINT);
      }

      if (this.character.getTraits().contains(Trait.SHOWINGTHEGOODS) && this.character.getTraits().contains(Trait.EXHIBITIONIST)) {
         actions.add(Publicize.AdvAction.DANCER);
      }

      if (this.character.getTraits().contains(Trait.SHOWINGTHEGOODS)) {
         actions.add(Publicize.AdvAction.DANCERNAKED);
      }

      if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS)
         && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 15
         && (this.character.getObedience() > 10 || this.character.getTraits().contains(Trait.SENSITIVE))) {
         actions.add(Publicize.AdvAction.WHOREFONDLE);
      }

      if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS)
         && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 30
         && (this.character.getObedience() > 12 || this.character.getTraits().contains(Trait.SENSUALTONGUE))) {
         actions.add(Publicize.AdvAction.WHOREBLOWJOB);
      }

      if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS)
         && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 35
         && (this.character.getObedience() > 15 || this.character.getTraits().contains(Trait.BIGBOOBS))) {
         actions.add(Publicize.AdvAction.WHORETITFUCK);
      }

      if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS)
         && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 45
         && (this.character.getObedience() > 20 || this.character.getTraits().contains(Trait.NATURAL))) {
         actions.add(Publicize.AdvAction.WHOREFUCK);
      }

      if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS)
         && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 60
         && (this.character.getObedience() > 25 || this.character.getTraits().contains(Trait.SEXADDICT))) {
         actions.add(Publicize.AdvAction.WHOREORGY);
      }

      if (this.character.getTraits().contains(Trait.HEYGUYSBOOSE)) {
         actions.add(Publicize.AdvAction.BARTENDER);
      }

      if (this.character.getTraits().contains(Trait.AVIANFLIGHT)) {
         actions.add(Publicize.AdvAction.AVIANFLY);
      }

      if (this.character.getTraits().contains(Trait.AVIANDRAG)) {
         actions.add(Publicize.AdvAction.AVIANDRAG);
      }

      if (this.character.getTraits().contains(Trait.APHRODISIACS)) {
         actions.add(Publicize.AdvAction.ALCHEMISTDRUGS);
      }

      this.characterAction.put(this.character, actions.get(Util.getInt(0, actions.size())));
      List<House> listHouses = new ArrayList<>();
      new ArrayList();

      for (House house : Jasbro.getInstance().getData().getHouses()) {
         for (Room room : house.getRooms()) {
            if (room.getAmountPeople() > 0 && room.getSelectedActivity().isCustomerDependent()) {
               listHouses.add(house);
               break;
            }
         }
      }

      int amountHouses = listHouses.size();
      long skill = this.effectiveness;
      int bonus = Util.getInt(80, 120);
      skill += this.character.getCharisma() / 4;
      skill += this.character.getFinalValue(SpecializationAttribute.ADVERTISING) / 4;
      switch ((Publicize.AdvAction)this.characterAction.get(this.character)) {
         case NORMAL:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.normal", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += Util.getInt(-20, 20);
            break;
         case BIGBREASTS:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.bigbreasts", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += this.character.getCharisma() * 4 / 5;
            break;
         case LOLI:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.loli", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += this.character.getCharisma() * 3 / 5;
            break;
         case CLUMSY:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.clumsy", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += this.character.getCharisma() * Util.getInt(50, 100) / 100;
            break;
         case SHY:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.shy", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += 10 + this.character.getCharisma() * 2 / 5;
            break;
         case BRIGHT:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.bright", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += 15 + this.character.getCharisma() * 2 / 5;
            break;
         case NICEBODY:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.nicebody", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += 22 + this.character.getCharisma() * 4 / 5;
            break;
         case NAKED:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.naked", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.character));
            bonus += 40 + this.character.getCharisma() / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, this.character));
            break;
         case BODYPAINT:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.bodypaint", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.character));
            bonus += 60 + this.character.getCharisma();
            this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, this.character));
            break;
         case DANCER:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.dancer", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, this.character));
            bonus += 10 + this.character.getFinalValue(SpecializationAttribute.STRIP) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.STRIP, this.character));
            break;
         case DANCERNAKED:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.dancernaked", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, this.character));
            bonus += this.character.getCharisma() / 2;
            bonus += 10 + this.character.getFinalValue(SpecializationAttribute.STRIP) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.STRIP, this.character));
            this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, this.character));
            break;
         case WHOREFONDLE:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.whorefondle", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, this.character));
            bonus += 20 + this.character.getCharisma() / 4;
            bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
            bonus += this.character.getFinalValue(Sextype.FOREPLAY) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.SEDUCTION, this.character));
            this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.FOREPLAY, this.character));
            break;
         case WHORETITFUCK:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.whorefondle", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, this.character));
            bonus += 30 + this.character.getCharisma() / 4;
            bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
            bonus += this.character.getFinalValue(Sextype.TITFUCK) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.SEDUCTION, this.character));
            this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.TITFUCK, this.character));
            break;
         case WHOREBLOWJOB:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.whoreblowjob", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, this.character));
            bonus += 40 + this.character.getCharisma() / 4;
            bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
            bonus += this.character.getFinalValue(Sextype.ORAL) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.SEDUCTION, this.character));
            this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.ORAL, this.character));
            break;
         case WHOREFUCK:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.whorefuck", this.character));
            if (Util.getInt(0, 100) < 50) {
               bonus += this.character.getFinalValue(Sextype.VAGINAL) / 2;
               this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.character));
               this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.VAGINAL, this.character));
            } else {
               bonus += this.character.getFinalValue(Sextype.ANAL) / 2;
               this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, this.character));
               this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.ANAL, this.character));
            }

            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.SEDUCTION, this.character));
            bonus += 50 + this.character.getCharisma() / 4;
            bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
            break;
         case WHOREORGY:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.whoreorgy", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.character));
            bonus += 70 + this.character.getCharisma() / 4;
            bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
            bonus += this.character.getFinalValue(Sextype.GROUP) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.GROUP, this.character));
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.SEDUCTION, this.character));
            break;
         case BARTENDER:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.bartending", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += this.character.getCharisma() / 3;
            bonus += 10 + this.character.getFinalValue(SpecializationAttribute.BARTENDING) / 2;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.BARTENDING, this.character));
            this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, this.character));
            break;
         case ALCHEMISTDRUGS:
            this.message.addToMessage("\n\n" + TextUtil.t("advertise.alchemist", this.character));
            this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
            bonus += this.character.getCharisma();
            bonus += 10 + this.character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) * Util.getInt(50, 220) / 100;
            this.getAttributeModifications().add(new AttributeModification(0.1F, SpecializationAttribute.PLANTKNOWLEDGE, this.character));
      }

      skill *= bonus / 100;
      long fame = this.character.getFame().getFame();
      if (fame > 100L) {
         long fameBonus = 0L;
         int divider = 500;

         do {
            fame /= divider;
            fameBonus++;
            divider *= 4;
         } while (fame > 0L);

         skill += fameBonus;
      }

      List<House> houses = Jasbro.getInstance().getData().getHouses();
      long increaseFameBy = skill * 50L / houses.size();

      for (House house : houses) {
         house.getFame().modifyFame(increaseFameBy);
      }

      Object[] arg = new Object[]{(int)increaseFameBy};
      this.message.addToMessage("\n\n" + TextUtil.t("advertise.increaseFame", this.character, arg));
      this.character.getFame().modifyFame(skill);
   }

   public int getStartingEffectiveness() {
      return this.effectiveness;
   }

   public void setStartingEffectiveness(int effectiveness) {
      this.effectiveness = effectiveness;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.51F, SpecializationAttribute.ADVERTISING));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, SpecializationAttribute.ADVERTISING));
      }

      if (this.character.getTraits().contains(Trait.SPIRITED)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3F, EssentialAttributes.MOTIVATION));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3F, EssentialAttributes.MOTIVATION));
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.CHARISMA));
      return modifications;
   }

   public enum AdvAction {
      NORMAL,
      BIGBREASTS,
      LOLI,
      CLUMSY,
      SHY,
      BRIGHT,
      NICEBODY,
      NAKED,
      BODYPAINT,
      DANCER,
      DANCERNAKED,
      BARTENDER,
      WHOREBLOWJOB,
      WHOREFONDLE,
      WHOREFUCK,
      WHORETITFUCK,
      WHOREORGY,
      AVIANFLY,
      AVIANDRAG,
      ALCHEMISTDRUGS;
   }
}
