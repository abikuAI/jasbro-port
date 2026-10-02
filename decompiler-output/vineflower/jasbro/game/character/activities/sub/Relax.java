package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relax extends RunningActivity {
   private int actionType = 0;
   private Map<Charakter, Relax.relaxAction> characterAction = new HashMap<>();

   @Override
   public void init() {
      List<Relax.relaxAction> action = new ArrayList<>();
      Charakter character = this.getCharacters().get(0);
      Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
      action.add(Relax.relaxAction.NAP);
      if ("POND".equals(this.getRoom().getRoomInfo().getId())) {
         action.add(Relax.relaxAction.DIP);
      }

      action.add(Relax.relaxAction.SING);
      action.add(Relax.relaxAction.SNACK);
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         action.add(Relax.relaxAction.TAN);
      }

      if (character.getFinalValue(SpecializationAttribute.SEDUCTION) > 15) {
         action.add(Relax.relaxAction.NAILS);
      }

      if (character.getFinalValue(SpecializationAttribute.SEDUCTION) > 40 && this.getRoom().getAmountPeople() > 2) {
         action.add(Relax.relaxAction.NAILSEVERYONE);
      }

      if (character.getFinalValue(SpecializationAttribute.STRIP) > 15) {
         action.add(Relax.relaxAction.DANCE);
      }

      if (character.getFinalValue(SpecializationAttribute.STRIP) > 45 && this.getRoom().getAmountPeople() > 1) {
         action.add(Relax.relaxAction.DANCESHOW);
      }

      if (character.getFinalValue(SpecializationAttribute.CLEANING) > 15 || character.getTraits().contains(Trait.HELPFUL)) {
         action.add(Relax.relaxAction.CLEAN);
      }

      if ((character.getFinalValue(SpecializationAttribute.COOKING) > 40 || character.getTraits().contains(Trait.RESTAURATEUR))
         && this.getRoom().getAmountPeople() > 2) {
         action.add(Relax.relaxAction.BARBECCUE);
      }

      if (character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) > 15 || character.getTraits().contains(Trait.CLEVER)) {
         action.add(Relax.relaxAction.READ);
      }

      if ((character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) > 15 || character.getTraits().contains(Trait.CLEVER))
         && this.getRoom().getAmountPeople() > 2) {
         action.add(Relax.relaxAction.READEVERYONE);
      }

      if (character.getFinalValue(SpecializationAttribute.VETERAN) > 30) {
         action.add(Relax.relaxAction.FIGHT);
      }

      if (character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) > 25) {
         action.add(Relax.relaxAction.SMOKE);
      }

      if (servedToday < 2L && character.getTraits().contains(Trait.NYMPHO) || character.getTraits().contains(Trait.INSATIABLE)) {
         action.add(Relax.relaxAction.MASTURBATE);
      }

      if (servedToday > 7L) {
         action.add(Relax.relaxAction.COOLDOWN);
      }

      if (this.getRoom().getAmountPeople() > 1) {
         action.add(Relax.relaxAction.CHAT);
      }

      if ((
            character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 25 && character.getFinalValue(SpecializationAttribute.MAGIC) > 25
               || character.getTraits().contains(Trait.ALTRUISTIC)
         )
         && this.getRoom().getAmountPeople() > 1) {
         action.add(Relax.relaxAction.NURSE);
      }

      this.characterAction.put(character, action.get(Util.getInt(0, action.size())));
   }

   @Override
   public MessageData getBaseMessage() {
      Charakter character = this.getCharacters().get(0);
      List<Charakter> characters = this.getRoom().getCurrentUsage().getCharacters();
      String message = "";
      int a = Util.getInt(0, characters.size());
      message = TextUtil.t("relax.basic", character);
      message = message + "\n";
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);

      for (Condition condition : character.getConditions()) {
         if (condition instanceof Buff && ((Buff)condition).getNameKey() == "RoughenedUp") {
            character.removeCondition(condition);
         }
      }

      switch ((Relax.relaxAction)this.characterAction.get(character)) {
         case NAP:
            message = message + TextUtil.t("relax.nap", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
            break;
         case DIP:
            message = message + TextUtil.t("relax.dip", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, character);
            break;
         case SING:
            message = message + TextUtil.t("relax.sing", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
            break;
         case SNACK:
            message = message + TextUtil.t("relax.snack", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
            break;
         case TAN:
            message = message + TextUtil.t("relax.tan", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SUNBATHE, character);
            if (Util.getInt(0, 100) > 60) {
               if (this.getCharacter().getTraits().contains(Trait.TANLINES)) {
                  this.getCharacter().addCondition(new Buff.Tanlines());
               } else {
                  boolean isTanned = false;

                  for (Condition condition : this.getCharacter().getConditions()) {
                     if (condition instanceof Buff.Tan && !this.getCharacter().getTraits().contains(Trait.SKINCARE)) {
                        this.getCharacter().removeCondition(condition);
                        this.getCharacter().addCondition(new Buff.Sunburn());
                        isTanned = true;
                     } else if (condition instanceof Buff.LightTan) {
                        this.getCharacter().removeCondition(condition);
                        this.getCharacter().addCondition(new Buff.Tan());
                        isTanned = true;
                     }
                  }

                  isTanned = false;
                  if (false) {
                     this.getCharacter().addCondition(new Buff.LightTan());
                  }
               }

               message = message + "\n";
               message = message + TextUtil.t("relax.tanned", character);
            }
            break;
         case NAILS:
            message = message + TextUtil.t("relax.nails", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
            break;
         case NAILSEVERYONE:
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);

            do {
               a = Util.getInt(0, characters.size());
            } while (characters.get(a) == character);

            this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, characters.get(a)));
            message = message + TextUtil.t("relax.nailseveryone", character, characters.get(a));
            break;
         case DANCE:
            message = message + TextUtil.t("relax.dance", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
            break;
         case DANCESHOW:
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

            for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
               if (target.getName() != character.getName()) {
                  this.getAttributeModifications().add(new AttributeModification(0.4F, SpecializationAttribute.STRIP, target));
                  this.getAttributeModifications().add(new AttributeModification(0.5F, EssentialAttributes.MOTIVATION, target));
               }
            }

            message = message + TextUtil.t("relax.danceshow", character);
            break;
         case CLEAN:
            message = message + TextUtil.t("relax.clean", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, character);
            this.getHouse().modDirt(-15);
            break;
         case BARBECCUE:
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);

            for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
               if (target != character) {
                  this.getAttributeModifications().add(new AttributeModification(10.1F, EssentialAttributes.HEALTH, target));
                  this.getAttributeModifications().add(new AttributeModification(0.5F, EssentialAttributes.MOTIVATION, target));
                  character.addCondition(new Buff.Satiated(30, character));
               }
            }

            message = message + TextUtil.t("relax.barbeccue", character);
            break;
         case READ:
            message = message + TextUtil.t("relax.read", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, character);
            break;
         case READEVERYONE:
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);

            for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
               if (target.getName() != character.getName()) {
                  this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, target));
                  this.getAttributeModifications().add(new AttributeModification(0.5F, EssentialAttributes.MOTIVATION, target));
               }
            }

            message = message + TextUtil.t("relax.readeveryone", character);
            break;
         case FIGHT:
            message = message + TextUtil.t("relax.fight", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, character);
            break;
         case SMOKE:
            message = message + TextUtil.t("relax.smoke", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
            character.addCondition(new Buff.Stoned(10, character, Util.getInt(-50, 50)));
            break;
         case MASTURBATE:
            message = message + TextUtil.t("relax.masturbate", character);
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
            break;
         case COOLDOWN:
            Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
            Object[] arg = new Object[]{servedToday};
            message = message + TextUtil.t("relax.cooldown", character);
            if (servedToday > character.getStamina()) {
               message = message + TextUtil.t("relax.cooldown.enough", character, arg);
            } else {
               message = message + TextUtil.t("relax.cooldown.more", character, arg);
            }

            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
            break;
         case CHAT:
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);

            do {
               a = Util.getInt(0, characters.size());
            } while (characters.get(a).getName() == character.getName());

            this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, characters.get(a)));
            this.getAttributeModifications().add(new AttributeModification(0.5F, EssentialAttributes.MOTIVATION, characters.get(a)));
            message = message + TextUtil.t("relax.chat", character, characters.get(a));
            break;
         case NURSE:
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, character);

            for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
               if (target.getName() != character.getName()) {
                  this.getAttributeModifications().add(new AttributeModification(10.1F, EssentialAttributes.HEALTH, target));
                  this.getAttributeModifications().add(new AttributeModification(10.1F, EssentialAttributes.ENERGY, target));
                  this.getAttributeModifications().add(new AttributeModification(0.5F, EssentialAttributes.MOTIVATION, target));
               }
            }

            message = message + TextUtil.t("relax.nurse", character);
      }

      return new MessageData(message, image, this.getCharacter().getBackground());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 40.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.HEALTH));
      switch ((Relax.relaxAction)this.characterAction.get(this.getCharacter())) {
         case NAP:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.HEALTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0F, EssentialAttributes.MOTIVATION));
            break;
         case DIP:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.HEALTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, EssentialAttributes.MOTIVATION));
         case SING:
         case SMOKE:
         case CHAT:
         default:
            break;
         case SNACK:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 10.0F, EssentialAttributes.HEALTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, EssentialAttributes.MOTIVATION));
            break;
         case TAN:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.CHARISMA));
            break;
         case NAILS:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.CHARISMA));
            break;
         case NAILSEVERYONE:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
            break;
         case DANCE:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8F, SpecializationAttribute.STRIP));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, EssentialAttributes.MOTIVATION));
            break;
         case DANCESHOW:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8F, SpecializationAttribute.STRIP));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0F, EssentialAttributes.MOTIVATION));
            break;
         case CLEAN:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8F, SpecializationAttribute.CLEANING));
            break;
         case BARBECCUE:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8F, SpecializationAttribute.COOKING));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0F, EssentialAttributes.MOTIVATION));
            break;
         case READ:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.15F, BaseAttributeTypes.INTELLIGENCE));
            break;
         case READEVERYONE:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.INTELLIGENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, EssentialAttributes.MOTIVATION));
            break;
         case FIGHT:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.01F, SpecializationAttribute.VETERAN));
            break;
         case MASTURBATE:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, Sextype.FOREPLAY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0F, EssentialAttributes.MOTIVATION));
            break;
         case COOLDOWN:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, EssentialAttributes.MOTIVATION));
            break;
         case NURSE:
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, SpecializationAttribute.MEDICALKNOWLEDGE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, SpecializationAttribute.MAGIC));
      }

      return modifications;
   }

   public int getactionType() {
      return this.actionType;
   }

   public void setactionType(int actionType) {
      this.actionType = actionType;
   }

   private enum relaxAction {
      NAP,
      SNACK,
      SING,
      READ,
      READEVERYONE,
      MASTURBATE,
      COOLDOWN,
      DANCE,
      DANCESHOW,
      DIP,
      CAT,
      CLEAN,
      BARBECCUE,
      FIGHT,
      SMOKE,
      NAILS,
      NAILSEVERYONE,
      TAN,
      CHAT,
      NURSE;
   }
}
