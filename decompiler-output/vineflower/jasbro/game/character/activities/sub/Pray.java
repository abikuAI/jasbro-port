package jasbro.game.character.activities.sub;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Pray extends RunningActivity {
   int randomBlessing = Util.getInt(1, 101);
   int charismaBonus = 0;
   int intellBonus = 0;
   int staminaBonus = 0;
   int strengthBonus = 0;
   int seductionBonus = 0;
   int maidBonus = 0;
   int groupBonus = 0;
   int bondageBonus = 0;
   int monsterBonus = 0;
   int bartendBonus = 0;
   int danceBonus = 0;
   int fightBonus = 0;
   int advBonus = 0;
   int sexBonus = 0;
   int medBonus = 0;
   int stealBonus = 0;

   @Override
   public MessageData getBaseMessage() {
      String message = TextUtil.t("pray.basic", this.getCharacters());
      message = message + "\n";
      if (this.randomBlessing < 20) {
         message = message + TextUtil.t("pray.nothing", this.getCharacters());
      } else if (this.randomBlessing < 22) {
         message = message + TextUtil.t("pray.charisma", this.getCharacters());
         this.getCharacter().addCondition(new Buff.BaseStatBlessing(BaseAttributeTypes.CHARISMA, "Him, Dio"));
      } else if (this.randomBlessing < 24) {
         message = message + TextUtil.t("pray.intell", this.getCharacters());
         this.getCharacter().addCondition(new Buff.BaseStatBlessing(BaseAttributeTypes.INTELLIGENCE, "Miwiki"));
      } else if (this.randomBlessing < 26) {
         message = message + TextUtil.t("pray.strength", this.getCharacters());
         this.getCharacter().addCondition(new Buff.BaseStatBlessing(BaseAttributeTypes.STRENGTH, "Sonja"));
      } else if (this.randomBlessing < 28) {
         message = message + TextUtil.t("pray.stamina", this.getCharacters());
         this.getCharacter().addCondition(new Buff.BaseStatBlessing(BaseAttributeTypes.STAMINA, "Muromi"));
      } else if (this.randomBlessing < 30) {
         message = message + TextUtil.t("pray.whore", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.SEDUCTION, "Layla"));
      } else if (this.randomBlessing < 35) {
         message = message + TextUtil.t("pray.bartend", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.BARTENDING, "Lawrence"));
      } else if (this.randomBlessing < 40) {
         message = message + TextUtil.t("pray.dance", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.STRIP, "Ahiru"));
      } else if (this.randomBlessing < 45) {
         message = message + TextUtil.t("pray.steal", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.PICKPOCKETING, "Zidane"));
      } else if (this.randomBlessing < 50) {
         message = message + TextUtil.t("pray.maid", this.getCharacters());
         if (Util.getInt(0, 100) > 50) {
            this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.CLEANING, "Maika"));
         } else {
            this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.COOKING, "Maika"));
         }
      } else if (this.randomBlessing < 55) {
         message = message + TextUtil.t("pray.group", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.GROUP, "Tabitha"));
      } else if (this.randomBlessing < 60) {
         message = message + TextUtil.t("pray.monster", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.MONSTER, "Kero-chan"));
      } else if (this.randomBlessing < 65) {
         message = message + TextUtil.t("pray.sex", this.getCharacters());
         switch (Util.getInt(1, 6)) {
            case 1:
               this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.VAGINAL, "Gerald and Rivia"));
               break;
            case 2:
               this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.ANAL, "Gerald and Rivia"));
               break;
            case 3:
               this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.ORAL, "Gerald and Rivia"));
               break;
            case 4:
               this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.TITFUCK, "Gerald and Rivia"));
               break;
            case 5:
               this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.FOREPLAY, "Gerald and Rivia"));
         }
      } else if (this.randomBlessing < 70) {
         message = message + TextUtil.t("pray.bondage", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SexBlessing(Sextype.BONDAGE, "Kurae"));
      } else if (this.randomBlessing < 75) {
         message = message + TextUtil.t("pray.medic", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.MEDICALKNOWLEDGE, "Elinu"));
      } else if (this.randomBlessing < 80) {
         message = message + TextUtil.t("pray.fight", this.getCharacters());
         this.getCharacter().addCondition(new Buff.SkillBlessing(SpecializationAttribute.VETERAN, "Ashura"));
      } else {
         message = message + TextUtil.t("pray.motivated", this.getCharacters());
      }

      return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter()), this.getCharacterLocation().getImage());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      if (this.randomBlessing < 22 && this.randomBlessing >= 20) {
         this.charismaBonus = this.charismaBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 24) {
         this.intellBonus = this.intellBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 26) {
         this.strengthBonus = this.strengthBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 28) {
         this.staminaBonus = this.staminaBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 30) {
         this.seductionBonus = this.seductionBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 35) {
         this.bartendBonus = this.bartendBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 40) {
         this.danceBonus = this.danceBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 45) {
         this.stealBonus = this.stealBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 50) {
         this.maidBonus = this.maidBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 55) {
         this.groupBonus = this.groupBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 60) {
         this.monsterBonus = this.monsterBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 65) {
         this.sexBonus = this.sexBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 70) {
         this.bondageBonus = this.bondageBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 75) {
         this.medBonus = this.medBonus + Util.getInt(1, 100);
      } else if (this.randomBlessing < 80) {
         this.fightBonus = this.fightBonus + Util.getInt(1, 100);
      }

      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.charismaBonus * 0.005F, BaseAttributeTypes.CHARISMA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.intellBonus * 0.005F, BaseAttributeTypes.INTELLIGENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.staminaBonus * 0.005F, BaseAttributeTypes.STAMINA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.strengthBonus * 0.005F, BaseAttributeTypes.STRENGTH));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.seductionBonus * 0.05F, SpecializationAttribute.SEDUCTION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.maidBonus * 0.05F, SpecializationAttribute.CLEANING));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.maidBonus * 0.05F, SpecializationAttribute.COOKING));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.danceBonus * 0.05F, SpecializationAttribute.STRIP));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.bartendBonus * 0.05F, SpecializationAttribute.BARTENDING));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.medBonus * 0.05F, SpecializationAttribute.MEDICALKNOWLEDGE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.medBonus * 0.05F, SpecializationAttribute.MAGIC));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.stealBonus * 0.05F, SpecializationAttribute.PICKPOCKETING));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.fightBonus * 0.05F, SpecializationAttribute.VETERAN));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.sexBonus * 0.05F, Sextype.ANAL));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.sexBonus * 0.05F, Sextype.VAGINAL));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.sexBonus * 0.05F, Sextype.ORAL));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.sexBonus * 0.05F, Sextype.TITFUCK));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.sexBonus * 0.05F, Sextype.FOREPLAY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.groupBonus * 0.05F, Sextype.GROUP));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.monsterBonus * 0.05F, Sextype.MONSTER));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.bondageBonus * 0.05F, Sextype.BONDAGE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, EssentialAttributes.MOTIVATION));
      return modifications;
   }
}
