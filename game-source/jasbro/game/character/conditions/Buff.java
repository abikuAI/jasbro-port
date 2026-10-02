package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Buff extends Condition {
   private String nameKey;
   private Map<AttributeType, Integer> attributeModifiers = new HashMap<>();
   private ImageData icon = new ImageData("imates/cons/plus.jpg");
   private int remainingTime = 10;

   public Buff() {
   }

   public Buff(String nameKey, ImageData icon, int time) {
      this.nameKey = nameKey;
      this.icon = icon;
      this.remainingTime = time;
   }

   @Override
   public String getName() {
      return TextUtil.t(this.nameKey);
   }

   @Override
   public String getDescription() {
      Object[] arguments = new Object[2];
      String text = this.getName() + "\n";
      text = text + TextUtil.t("buff.description");
      List<String> modifierStrings = new ArrayList<>();
      boolean newline = false;

      for (Entry<AttributeType, Integer> entry : this.getAttributeModifiers().entrySet()) {
         StringBuilder builder = new StringBuilder();
         if (newline) {
            builder.append('\n');
         }

         builder.append(TextUtil.t("buff.by", entry.getKey().getText(), entry.getValue()));
         modifierStrings.add(builder.toString());
         newline = !newline;
      }

      text = text + TextUtil.listStrings(modifierStrings);
      arguments[0] = this.remainingTime;
      return text + "\n" + TextUtil.t("buff.duration", arguments) + " ";
   }

   @Override
   public ImageData getIcon() {
      return this.icon;
   }

   @Override
   public float getAttributeModifier(Attribute attribute) {
      int modifier = 0;

      for (Entry<AttributeType, Integer> entry : this.attributeModifiers.entrySet()) {
         if (entry.getKey() == attribute.getAttributeType()) {
            modifier += entry.getValue();
         }
      }

      return modifier;
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTSHIFT) {
         this.remainingTime--;
         if (this.remainingTime <= 0) {
            this.getCharacter().getConditions().remove(this);
         }
      }
   }

   public void addAttributeBuff(AttributeType attributeType, int amount) {
      this.attributeModifiers.put(attributeType, amount);
   }

   public String getNameKey() {
      return this.nameKey;
   }

   public void setNameKey(String nameKey) {
      this.nameKey = nameKey;
   }

   public Map<AttributeType, Integer> getAttributeModifiers() {
      return this.attributeModifiers;
   }

   public void setIcon(ImageData icon) {
      this.icon = icon;
   }

   public int getRemainingTime() {
      return this.remainingTime;
   }

   public void setRemainingTime(int remainingTime) {
      this.remainingTime = remainingTime;
   }

   @Override
   public void init() {
      Buff existingBuff = this.getExistingBuff();
      if (existingBuff != null) {
         if (existingBuff.getRemainingTime() >= 3) {
            for (Entry<AttributeType, Integer> entry : existingBuff.getAttributeModifiers().entrySet()) {
               if (entry.getKey() instanceof BaseAttributeTypes) {
                  entry.setValue(entry.getValue() + 1);
               } else {
                  entry.setValue(entry.getValue() + 10);
               }
            }

            this.getCharacter().getConditions().remove(this);
         } else {
            this.getCharacter().getConditions().remove(existingBuff);
         }
      }
   }

   public Buff getExistingBuff() {
      Buff existingBuff = null;

      for (Condition condition : this.getCharacter().getConditions()) {
         if (condition instanceof Buff && condition != this && condition.getClass().equals(this.getClass())) {
            existingBuff = (Buff)condition;
            break;
         }
      }

      return existingBuff;
   }

   public static class AllDolledUp extends Buff {
      public AllDolledUp(int skill, Charakter target) {
         super("buff.dolledup", new ImageData(), 6);
         this.setIcon(new ImageData("images/icons/ribbon.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 2 + skill / 10);
      }
   }

   public static class Angry extends Buff {
      public Angry(Charakter sourceCharacter) {
         super("buff.angry", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/perks/evenassholeshavestandards.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 5);
      }
   }

   public static class AquaticTrait extends Buff {
      int stage = 3;

      public AquaticTrait(Charakter source, int stage) {
         super("buff.aquatictrait" + stage, new ImageData(), 4);
         this.stage = stage;
         double buff = 1.0;
         switch (stage) {
            case 1:
               this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
               buff = -0.75;
               break;
            case 2:
               this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
               buff = -0.25;
               break;
            case 3:
               this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
               buff = 0.05;
               break;
            case 4:
               this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
               buff = 0.1;
               break;
            case 5:
               this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
               buff = 0.2;
         }

         if (source.getType() == CharacterType.TRAINER) {
            this.addAttributeBuff(BaseAttributeTypes.COMMAND, (int)(source.getCommand() * buff));
         }

         if (source.getType() == CharacterType.SLAVE) {
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, (int)(source.getObedience() * buff));
         }

         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, (int)(source.getCharisma() * buff));
         this.addAttributeBuff(BaseAttributeTypes.INTELLIGENCE, (int)(source.getIntelligence() * buff));
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, (int)(source.getStamina() * buff));
         this.addAttributeBuff(BaseAttributeTypes.STRENGTH, (int)(source.getStrength() * buff));
      }

      public int getStage() {
         return this.stage;
      }
   }

   public static class BadDreams extends Buff {
      public BadDreams(Charakter source) {
         super("buff.baddreams", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
         float buff = -0.5F;
         if (source.getType() == CharacterType.TRAINER) {
            this.addAttributeBuff(BaseAttributeTypes.COMMAND, (int)(source.getCommand() * buff));
         }

         if (source.getType() == CharacterType.SLAVE) {
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, (int)(source.getObedience() * buff));
         }

         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, (int)(source.getCharisma() * buff));
         this.addAttributeBuff(BaseAttributeTypes.INTELLIGENCE, (int)(source.getIntelligence() * buff));
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, (int)(source.getStamina() * buff));
         this.addAttributeBuff(BaseAttributeTypes.STRENGTH, (int)(source.getStrength() * buff));
      }
   }

   public static class BaseStatBlessing extends Buff {
      private BaseAttributeTypes statName = null;
      private String godName = null;

      public void setStatName(BaseAttributeTypes statName) {
         this.statName = statName;
      }

      public void setGodName(String godName) {
         this.godName = godName;
      }

      public BaseStatBlessing(BaseAttributeTypes stat, String godName) {
         super("buff.blessing", new ImageData(), 10);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         this.setStatName(stat);
         this.setGodName(godName);
      }

      @Override
      public String getDescription() {
         Object[] arguments = new Object[]{this.godName, this.statName.getText(), this.getRemainingTime()};
         return TextUtil.t("buff.basestatblessing.description", this.getCharacter(), arguments);
      }

      @Override
      public void handleEvent(MyEvent e) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == this.statName) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 1.5F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class DarkGodSeed extends Buff {
      public DarkGodSeed() {
         super("buff.DarkGodSeed", new ImageData(), 13);
         this.setIcon(new ImageData("images/icons/Darkness.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 7);
         this.addAttributeBuff(BaseAttributeTypes.INTELLIGENCE, 7);
         this.addAttributeBuff(SpecializationAttribute.MAGIC, 20);
      }
   }

   public static class Depressed extends Buff {
      public Depressed(Charakter sourceCharacter) {
         super("buff.depressed", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, -1 * sourceCharacter.getStamina() / 4);
         this.addAttributeBuff(BaseAttributeTypes.STRENGTH, -1 * sourceCharacter.getStrength() / 4);
         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, -1 * sourceCharacter.getObedience() / 4);
      }
   }

   public static class Exhausted extends Buff {
      public Exhausted() {
         super("buff.roughenedup", new ImageData(), 6);
      }

      @Override
      public String getDescription() {
         return TextUtil.t("buff.exhausted.description", this.getCharacter());
      }

      @Override
      public void handleEvent(MyEvent e) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY && attributeModification.getBaseAmount() > 0.0F) {
               float modification = attributeModification.getBaseAmount();
               float change = -modification / 2.0F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class Happy extends Buff {
      public Happy(Charakter sourceCharacter) {
         super("buff.happy", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2 + sourceCharacter.getStamina() / 4);
         this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 2 + sourceCharacter.getStrength() / 4);
         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2 + sourceCharacter.getObedience() / 4);
      }
   }

   public static class Heat1 extends Buff {
      public Heat1() {
         super("buff.heat1", new ImageData(), 4);
      }

      @Override
      public void init() {
         this.setIcon(new ImageData("images/icons/heart1.png"));
         super.init();
      }

      @Override
      public String getDescription() {
         return TextUtil.t("buff.heat1.description", this.getCharacter());
      }
   }

   public static class Heat2 extends Buff {
      public Heat2() {
         super("buff.heat2", new ImageData(), 4);
      }

      @Override
      public void init() {
         this.setIcon(new ImageData("images/icons/heart2.png"));
         super.init();
      }

      @Override
      public String getDescription() {
         return TextUtil.t("buff.heat2.description", this.getCharacter());
      }
   }

   public static class Heat3 extends Buff {
      public Heat3() {
         super("buff.heat3", new ImageData(), 3);
      }

      @Override
      public void init() {
         this.setIcon(new ImageData("images/icons/heart3.png"));
         super.init();
      }

      @Override
      public String getDescription() {
         return TextUtil.t("buff.heat3.description", this.getCharacter());
      }
   }

   public static class HornyBuff extends Buff {
      public HornyBuff(Charakter sourceCharacter) {
         super("buff.horny", new ImageData(), 6);
         this.setIcon(new ImageData("images/icons/Hearts.png"));
         int bonus = 0;
         bonus = (int)(5.0F + sourceCharacter.getAttribute(BaseAttributeTypes.CHARISMA).getInternValue() / 10.0F);
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, bonus);
         bonus = (int)(5.0F + sourceCharacter.getAttribute(BaseAttributeTypes.STAMINA).getInternValue() / 10.0F);
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, bonus);

         for (Sextype sex : Sextype.values()) {
            bonus = (int)(5.0F + sourceCharacter.getAttribute(sex).getInternValue() / 10.0F);
            this.addAttributeBuff(sex, bonus);
         }
      }
   }

   public static class HornyTwoBuff extends Buff {
      public HornyTwoBuff(Charakter sourceCharacter) {
         super("buff.horny", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/Hearts.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 2 + sourceCharacter.getCharisma() / 4);
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2 + sourceCharacter.getStamina() / 4);
      }
   }

   public static class Hyperactive extends Buff {
      public Hyperactive(Charakter sourceCharacter) {
         super("buff.hyperactive", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/Hearts.png"));
      }

      @Override
      public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
         return calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT ? currentValue + 20.0 : currentValue;
      }
   }

   public static class Intimidated extends Buff {
      public Intimidated(Charakter sourceCharacter) {
         super("buff.intimidated", new ImageData(), 10);
         this.setIcon(new ImageData("images/icons/intimidate.png"));
         int command = sourceCharacter.getCommand();
         int amount;
         if (command > 45) {
            amount = 7;
         } else if (command > 30) {
            amount = 6;
         } else if (command > 20) {
            amount = 5;
         } else if (command > 12) {
            amount = 4;
         } else if (command > 5) {
            amount = 3;
         } else if (command > 3) {
            amount = 2;
         } else {
            amount = 1;
         }

         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, amount);
      }
   }

   public static class LightTan extends Buff {
      public LightTan() {
         super("buff.lightTan", new ImageData(), 21);
         this.setIcon(new ImageData("images/icons/light_tan.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 5);
      }
   }

   public static class MasochisticDelight extends Buff {
      public MasochisticDelight(Charakter source) {
         super("buff.masochisticdelight", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/perks/bleeding-heart.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 2 + source.getCharisma() / 6);
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2 + source.getStamina() / 6);
         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2 + source.getCommand() / 4);
      }
   }

   public static class Motivated extends Buff {
      public Motivated(Charakter sourceCharacter) {
         super("buff.motivated", new ImageData(), 10);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         int command = sourceCharacter.getCommand();
         if (command > 45) {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 4);
            this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 3);
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 3);
         } else if (command > 30) {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 3);
            this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 2);
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2);
         } else if (command > 20) {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2);
            this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 2);
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 1);
         } else if (command > 12) {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2);
            this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 1);
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 1);
         } else if (command > 5) {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 1);
            this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 1);
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 1);
         } else if (command > 3) {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 1);
            this.addAttributeBuff(BaseAttributeTypes.STRENGTH, 1);
         } else {
            this.addAttributeBuff(BaseAttributeTypes.STAMINA, 1);
         }
      }
   }

   public static class MotivatedTwo extends Buff {
      public MotivatedTwo(Charakter sourceCharacter) {
         super("buff.motivatedtwo", new ImageData(), 1);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         this.addAttributeBuff(BaseAttributeTypes.COMMAND, sourceCharacter.getCommand() * 15 / 100);
         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, sourceCharacter.getObedience() * 15 / 100);

         for (SpecializationType specialization : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
            if (specialization != SpecializationType.TRAINER && specialization != SpecializationType.SLAVE) {
               for (AttributeType attribute : specialization.getAssociatedAttributes()) {
                  this.addAttributeBuff(attribute, sourceCharacter.getAttribute(attribute).getValue() * 15 / 100);
               }
            }
         }
      }

      @Override
      public String getDescription() {
         Object[] arg = new Object[]{this.getRemainingTime()};
         return TextUtil.t("buff.motivated.description", this.getCharacter(), arg);
      }
   }

   public static class Pretty extends Buff {
      public Pretty(Charakter sourceCharacter, Charakter target) {
         super("buff.pretty", new ImageData(), 14);
         this.setIcon(new ImageData("images/icons/cucumber.png"));
         int skill = sourceCharacter.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
         int charismaIncrease = 1 + skill / 10;
         if (sourceCharacter.getTraits().contains(Trait.COSMETICS)) {
            charismaIncrease *= 1;
         }

         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, charismaIncrease);
      }
   }

   public static class RoughenedUp extends Buff {
      public RoughenedUp() {
         super("buff.roughenedup", new ImageData(), 6);
      }

      @Override
      public String getDescription() {
         int staminaLoss = 0;
         int strengthLoss = 0;
         int var4 = 5;
         int var5 = 5;
         Object[] arguments = new Object[]{Integer.valueOf(var4), Integer.valueOf(var5)};
         return TextUtil.t("buff.roughenedup.description", this.getCharacter(), arguments);
      }

      @Override
      public void init() {
         this.setIcon(new ImageData("images/icons/roughenedup.png"));
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, -5);
         this.addAttributeBuff(BaseAttributeTypes.STRENGTH, -5);
         super.init();
      }
   }

   public static class SadisticDelight extends Buff {
      public SadisticDelight(Charakter source) {
         super("buff.sadisticdelight", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/perks/ifrit.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 2 + source.getCharisma() / 6);
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2 + source.getStamina() / 6);
         this.addAttributeBuff(BaseAttributeTypes.COMMAND, 2 + source.getCommand() / 4);
      }
   }

   public static class Satiated extends Buff {
      public Satiated(int FoodRank, Charakter sourceCharacter) {
         super("buff.satiated", new ImageData(), 4);
         this.setIcon(new ImageData("images/icons/chicken.png"));
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, FoodRank / 6);
         this.addAttributeBuff(BaseAttributeTypes.STRENGTH, FoodRank / 6);
         if (sourceCharacter.getTraits().contains(Trait.CATSAREASSHOLES)) {
            this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 6);
         }
      }
   }

   public static class Seductive extends Buff {
      public Seductive(Charakter sourceCharacter) {
         super("buff.seductive", new ImageData(), 3);
         this.setIcon(new ImageData("images/icons/Hearts.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 2 + sourceCharacter.getCharisma() / 4);
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2 + sourceCharacter.getStamina() / 4);
      }
   }

   public static class SexBlessing extends Buff {
      private Sextype statName = null;
      private String godName = null;

      public void setStatName(Sextype statName) {
         this.statName = statName;
      }

      public void setGodName(String godName) {
         this.godName = godName;
      }

      public SexBlessing(Sextype stat, String godName) {
         super("buff.blessing", new ImageData(), 10);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         this.setStatName(stat);
         this.setGodName(godName);
      }

      @Override
      public String getDescription() {
         Object[] arguments = new Object[]{this.godName, this.statName.getText(), this.getRemainingTime()};
         return TextUtil.t("buff.basestatblessing.description", this.getCharacter(), arguments);
      }

      @Override
      public void handleEvent(MyEvent e) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == this.statName) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 1.5F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class SkillBlessing extends Buff {
      private SpecializationAttribute statName = null;
      private String godName = null;

      public void setStatName(SpecializationAttribute statName) {
         this.statName = statName;
      }

      public void setGodName(String godName) {
         this.godName = godName;
      }

      public SkillBlessing(SpecializationAttribute stat, String godName) {
         super("buff.blessing", new ImageData(), 10);
         this.setIcon(new ImageData("images/icons/motivated.png"));
         this.setStatName(stat);
         this.setGodName(godName);
      }

      @Override
      public String getDescription() {
         Object[] arguments = new Object[]{this.godName, this.statName.getText(), this.getRemainingTime()};
         return TextUtil.t("buff.basestatblessing.description", this.getCharacter(), arguments);
      }

      @Override
      public void handleEvent(MyEvent e) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == this.statName) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 1.5F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class SmoothSkin extends Buff {
      public SmoothSkin(int magnitude, Charakter target) {
         super("buff.smoothskin", new ImageData(), 6);
         this.setIcon(new ImageData("images/icons/cucumber.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 2 + magnitude / 2);
      }
   }

   public static class Sore extends Buff {
      public Sore() {
         super("buff.sore", new ImageData(), 6);
      }

      @Override
      public String getDescription() {
         int sore = 0;
         if (this.getCharacter().getGender() == Gender.FEMALE) {
            sore = this.getCharacter().getFinalValue(Sextype.VAGINAL) / 4;
         } else {
            sore = this.getCharacter().getFinalValue(Sextype.ANAL) / 4;
         }

         Object[] arguments = new Object[]{this.getRemainingTime(), sore};
         return this.getCharacter().getGender() == Gender.FEMALE
            ? TextUtil.t("buff.sore.description.female", this.getCharacter(), arguments)
            : TextUtil.t("buff.sore.description.male", this.getCharacter(), arguments);
      }

      @Override
      public void init() {
         this.setIcon(new ImageData("images/icons/perks/flame.png"));
         if (this.getCharacter().getGender() == Gender.FEMALE) {
            this.addAttributeBuff(Sextype.VAGINAL, -1 * this.getCharacter().getFinalValue(Sextype.VAGINAL) / 4);
         } else {
            this.addAttributeBuff(Sextype.ANAL, -1 * this.getCharacter().getFinalValue(Sextype.ANAL) / 4);
         }

         super.init();
      }
   }

   public static class Stoned extends Buff {
      public Stoned(int skill, Charakter target, int intellBonus) {
         super("buff.stoned", new ImageData(), 6);
         this.setIcon(new ImageData("images/icons/stoned.png"));
         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2 + skill / 10);
         this.addAttributeBuff(BaseAttributeTypes.STAMINA, 2);
         this.addAttributeBuff(BaseAttributeTypes.INTELLIGENCE, intellBonus / 5);
      }
   }

   public static class Sunburn extends Buff {
      public Sunburn() {
         super("buff.sunburn", new ImageData(), 21);
         this.setIcon(new ImageData("images/icons/light_tan.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, -10);
      }
   }

   public static class Tan extends Buff {
      public Tan() {
         super("buff.tan", new ImageData(), 21);
         this.setIcon(new ImageData("images/icons/light_tan.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 10);
      }
   }

   public static class Tanlines extends Buff {
      public Tanlines() {
         super("buff.tanlines", new ImageData(), 90);
         this.setIcon(new ImageData("images/icons/light_tan.png"));
         this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 10);
      }
   }

   public static class Unmotivated extends Buff {
      public Unmotivated(Charakter sourceCharacter) {
         super("buff.unmotivated", new ImageData(), 1);
         this.setIcon(new ImageData("images/icons/intimidate.png"));
         this.addAttributeBuff(BaseAttributeTypes.COMMAND, -sourceCharacter.getCommand() * 45 / 100);
         this.addAttributeBuff(BaseAttributeTypes.OBEDIENCE, -sourceCharacter.getObedience() * 45 / 100);

         for (SpecializationType specialization : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
            if (specialization != SpecializationType.TRAINER && specialization != SpecializationType.SLAVE) {
               for (AttributeType attribute : specialization.getAssociatedAttributes()) {
                  this.addAttributeBuff(attribute, -sourceCharacter.getAttribute(attribute).getValue() * 40 / 100);
               }
            }
         }
      }

      @Override
      public String getDescription() {
         return TextUtil.t("buff.unmotivated.description", this.getCharacter());
      }
   }

   public static class Watched extends Buff {
      public Watched(int value) {
         super("buff.watched", new ImageData(), 7);
         this.setIcon(new ImageData("images/icons/intimidate.png"));
         this.addAttributeBuff(SpecializationAttribute.PICKPOCKETING, -value / 3);
      }

      @Override
      public String getDescription() {
         return TextUtil.t("buff.watched.description", this.getCharacter());
      }
   }
}
