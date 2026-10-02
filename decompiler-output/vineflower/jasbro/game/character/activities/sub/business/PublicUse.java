package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicUse extends RunningActivity implements BusinessSecondaryActivity {
   private MessageData messageData;
   private int bonus;
   private Charakter thatOneGirl = null;
   private List<Charakter> girls = new ArrayList<>();
   private Map<Charakter, Short> groupSize = new HashMap<>();
   private Map<Charakter, Short> remainingTime = new HashMap<>();
   private Map<Charakter, Short> totalServed = new HashMap<>();
   private Map<Charakter, Short> energy = new HashMap<>();
   private Map<Charakter, Short> energySpent = new HashMap<>();
   private Map<Charakter, Short> maxEnergy = new HashMap<>();
   private Map<Charakter, PublicUse.PublicUseEvent> event = new HashMap<>();
   private Map<Charakter, Short> girlStatus = new HashMap<>();

   @Override
   public void init() {
      this.girls.addAll(this.getCharacters());
      Collections.shuffle(this.girls);
      short en = 0;
      boolean someoneTookAll = false;

      for (Charakter currentGirl : this.girls) {
         en = (short)(100 + currentGirl.getStamina() / 10);
         if (currentGirl.getTraits().contains(Trait.SEXADDICT)) {
            en = (short)(en + 25);
         }

         if (currentGirl.getTraits().contains(Trait.GANGBANGQUEEN)) {
            en = (short)(en + 25);
         }

         if (currentGirl.getTraits().contains(Trait.KEEPEMCOMING)) {
            en = (short)(en + 25);
         }

         if (currentGirl.getTraits().contains(Trait.PERSEVERING)) {
            en = (short)(en + 10);
         }

         if (currentGirl.getTraits().contains(Trait.NYMPHO)) {
            en = (short)(en + 10);
         }

         if (currentGirl.getTraits().contains(Trait.SEXADDICT)) {
            en = (short)(en + 25);
         }

         if (currentGirl.getTraits().contains(Trait.FRAGILE)) {
            en = (short)(en - 25);
         }

         if (currentGirl.getTraits().contains(Trait.FLABBY)) {
            en = (short)(en - 25);
         }

         if (currentGirl.getTraits().contains(Trait.SINGLEMINDED)) {
            en = (short)(en - 25);
         }

         this.energy.put(currentGirl, en);
         this.energySpent.put(currentGirl, (short)0);
         this.maxEnergy.put(currentGirl, en);
         this.totalServed.put(currentGirl, (short)0);
         this.girlStatus.put(currentGirl, (short)0);
         if (currentGirl.getTraits().contains(Trait.PUBLICUSE)) {
            this.remainingTime.put(currentGirl, (short)400);
         } else {
            this.remainingTime.put(currentGirl, (short)300);
         }

         List<PublicUse.PublicUseEvent> actions = new ArrayList<>();
         actions.add(PublicUse.PublicUseEvent.ROUGHCUSTOMER);
         actions.add(PublicUse.PublicUseEvent.GENTLECUSTOMER);
         if (this.getCustomers().size() > 25) {
            actions.add(PublicUse.PublicUseEvent.LINE);
         }

         if (this.getCustomers().size() > 25) {
            actions.add(PublicUse.PublicUseEvent.SWARM);
         }

         actions.add(PublicUse.PublicUseEvent.NOBREAK);
         actions.add(PublicUse.PublicUseEvent.NICECUSTOMER);
         actions.add(PublicUse.PublicUseEvent.NOBREAK);
         actions.add(PublicUse.PublicUseEvent.ALLANAL);
         actions.add(PublicUse.PublicUseEvent.ALLVAGINAL);
         actions.add(PublicUse.PublicUseEvent.BUKKAKE);
         actions.add(PublicUse.PublicUseEvent.NORMAL);
         actions.add(PublicUse.PublicUseEvent.NORMAL);
         if (this.getCustomers().size() > 25) {
            actions.add(PublicUse.PublicUseEvent.OVERTIME);
         }

         if (currentGirl.getTraits().contains(Trait.SEXADDICT) && this.getCustomers().size() > 25 && Util.getInt(0, 100) < 5) {
            this.event.put(currentGirl, PublicUse.PublicUseEvent.ALL);
            this.remainingTime.put(currentGirl, (short)30000);
         } else if (currentGirl.getTraits().contains(Trait.GANGBANGQUEEN)
            && this.getCharacters().size() != 1
            && !someoneTookAll
            && this.getCustomers().size() > 25
            && Util.getInt(0, 100) < 5) {
            this.event.put(currentGirl, PublicUse.PublicUseEvent.ALONE);
            this.thatOneGirl = currentGirl;
            this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) * 150 / 100));
            someoneTookAll = true;

            for (Charakter otherGirl : this.getCharacters()) {
               if (otherGirl != currentGirl) {
                  this.event.put(otherGirl, PublicUse.PublicUseEvent.NONE);
               }
            }
         }

         if (this.event.get(currentGirl) != PublicUse.PublicUseEvent.ALONE
            && this.event.get(currentGirl) != PublicUse.PublicUseEvent.NONE
            && this.event.get(currentGirl) != PublicUse.PublicUseEvent.ALL) {
            this.event.put(currentGirl, actions.get(Util.getInt(0, actions.size())));
         }
      }
   }

   @Override
   public void perform() {
      if (this.getCustomers().size() >= 5) {
         short totalTips = 0;
         short timeTaken = 0;
         short spentEnergy = 0;
         short customersServed = 0;
         short servedThisRound = 1;
         short breakTime = 100;
         short energyCostFactor = 100;
         Sextype sex = null;
         short rand = 0;

         while (customersServed <= this.getCustomers().size() && this.isTimeLeft(this.getCharacters())) {
            for (Charakter currentGirl : this.girls) {
               servedThisRound = 0;
               this.groupSize.put(currentGirl, (short)(Util.getInt(2, 3) + currentGirl.getFinalValue(Sextype.GROUP) / 20));
               if (currentGirl.getTraits().contains(Trait.GANGBANGQUEEN)) {
                  this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + Util.getInt(0, 1)));
               }

               if (currentGirl.getTraits().contains(Trait.PUBLICUSE)) {
                  this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + Util.getInt(0, 2)));
               }

               if (currentGirl.getTraits().contains(Trait.MULTIFACETED)) {
                  this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + Util.getInt(0, 1)));
               }

               switch ((PublicUse.PublicUseEvent)this.event.get(currentGirl)) {
                  case ROUGHCUSTOMER:
                     energyCostFactor = 130;
                     break;
                  case GENTLECUSTOMER:
                     energyCostFactor = 70;
                     break;
                  case NICECUSTOMER:
                     breakTime = 130;
                     break;
                  case LINE:
                     this.groupSize.put(currentGirl, (short)1);
                     energyCostFactor = 30;
                     breakTime = 5;
                     break;
                  case ALLANAL:
                     if (currentGirl.getFinalValue(Sextype.ANAL) > 90 && Util.getInt(0, 100) > 20) {
                        this.groupSize.put(currentGirl, (short)2);
                     } else if (currentGirl.getFinalValue(Sextype.ANAL) > 90 && Util.getInt(0, 100) > 50) {
                        this.groupSize.put(currentGirl, (short)2);
                     } else {
                        this.groupSize.put(currentGirl, (short)1);
                     }

                     energyCostFactor = 110;
                     breakTime = 10;
                     break;
                  case ALLVAGINAL:
                     if (currentGirl.getFinalValue(Sextype.VAGINAL) > 90 && Util.getInt(0, 100) > 20) {
                        this.groupSize.put(currentGirl, (short)2);
                     } else if (currentGirl.getFinalValue(Sextype.VAGINAL) > 90 && Util.getInt(0, 100) > 50) {
                        this.groupSize.put(currentGirl, (short)2);
                     } else {
                        this.groupSize.put(currentGirl, (short)1);
                     }

                     energyCostFactor = 110;
                     breakTime = 5;
                     break;
                  case BUKKAKE:
                     this.groupSize.put(currentGirl, (short)(2 + currentGirl.getFinalValue(Sextype.ORAL) / 20));
                     energyCostFactor = 30;
                     breakTime = 80;
                     break;
                  case SWARM:
                     this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + 2));
                     energyCostFactor = 110;
                     break;
                  case NOBREAK:
                     if (this.energy.get(currentGirl) > 10) {
                        breakTime = 0;
                     }
                     break;
                  case OVERTIME:
                     this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) * 150 / 100));
                     break;
                  case ALONE:
                     this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) * 130 / 100));
                     breakTime = 2;
                     this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + 2));
               }

               if (this.groupSize.get(currentGirl) > this.getCustomers().size()) {
                  this.groupSize.put(currentGirl, (short)this.getCustomers().size());
               }

               energyCostFactor += this.totalServed.get(currentGirl);
               if (this.energy.get(currentGirl) > 5 && this.remainingTime.get(currentGirl) > 10 && this.event.get(currentGirl) != PublicUse.PublicUseEvent.NONE
                  )
                {
                  for (short i = customersServed; i < this.getCustomers().size(); i++) {
                     rand = (short)Util.getInt(0, 100);
                     if (rand < 20 || this.event.get(currentGirl) == PublicUse.PublicUseEvent.BUKKAKE) {
                        sex = Sextype.ORAL;
                     } else if (rand >= 60 && this.event.get(currentGirl) != PublicUse.PublicUseEvent.ALLVAGINAL) {
                        sex = Sextype.ANAL;
                     } else {
                        sex = Sextype.VAGINAL;
                     }

                     if (this.event.get(currentGirl) == PublicUse.PublicUseEvent.BUKKAKE) {
                        sex = Sextype.ORAL;
                     }

                     if (this.event.get(currentGirl) == PublicUse.PublicUseEvent.ALLVAGINAL) {
                        sex = Sextype.VAGINAL;
                     }

                     if (this.event.get(currentGirl) == PublicUse.PublicUseEvent.ALLANAL) {
                        sex = Sextype.ANAL;
                     }

                     short energyBefore = this.energy.get(currentGirl);
                     currentGirl.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                     this.totalServed.put(currentGirl, (short)(this.totalServed.get(currentGirl) + 1));
                     servedThisRound++;
                     customersServed++;
                     timeTaken = (short)(12 - this.groupSize.get(currentGirl) / 2);
                     timeTaken = (short)(timeTaken - timeTaken * (currentGirl.getFinalValue(sex) + currentGirl.getFinalValue(Sextype.GROUP)) / 250);
                     totalTips = (short)(totalTips + this.getCustomers().get(i).payFixed(Util.getInt(1, 5 + currentGirl.getFinalValue(sex))));
                     this.getCustomers().get(i).addToSatisfaction(2 + currentGirl.getFinalValue(sex) / 16, this);
                     this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) - timeTaken));
                     this.getAttributeModifications().add(new AttributeModification(0.015F, BaseAttributeTypes.OBEDIENCE, currentGirl));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, sex, currentGirl));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, Sextype.GROUP, currentGirl));
                     this.getAttributeModifications().add(new AttributeModification(0.02F, BaseAttributeTypes.STAMINA, currentGirl));
                     if (Util.getInt(0, 100) < 100) {
                        currentGirl.getFame().modifyFame(1.0);
                     }

                     this.getHouse().modDirt(2);
                     if (servedThisRound == this.groupSize.get(currentGirl)) {
                        spentEnergy = (short)(servedThisRound * 10 * (energyCostFactor + Util.getInt(-10, 10)) / 100);
                        this.energy.put(currentGirl, (short)(this.energy.get(currentGirl) - spentEnergy));
                        this.energySpent.put(currentGirl, (short)(this.energySpent.get(currentGirl) + spentEnergy));
                        if (energyBefore > 0 && this.energy.get(currentGirl) <= 0) {
                           this.girlStatus.put(currentGirl, (short)(this.girlStatus.get(currentGirl) + 1));
                        }
                        break;
                     }
                  }
               }

               if (this.energy.get(currentGirl) < 10) {
                  breakTime = (short)(breakTime + 100);
               }

               if (currentGirl.getTraits().contains(Trait.PUBLICUSE)) {
                  this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) - breakTime / 30 - 1));
               } else {
                  this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) - breakTime / 20 - 1));
               }

               this.energy
                  .put(currentGirl, (short)(this.energy.get(currentGirl) + 5 + (currentGirl.getStamina() / 5 + this.getCharacters().size()) * breakTime / 100));
            }
         }

         this.modifyIncome(totalTips);

         for (Charakter character : this.getCharacters()) {
            if (this.totalServed.get(character) < this.groupSize.get(character)) {
               this.groupSize.put(character, this.totalServed.get(character));
            }

            if (this.totalServed.get(character) == 0 && this.event.get(character) != PublicUse.PublicUseEvent.NONE) {
               this.event.put(character, PublicUse.PublicUseEvent.NOCUSTOMER);
            }

            Object[] arguments = new Object[]{this.totalServed.get(character), this.groupSize.get(character), this.girlStatus.get(character)};
            this.messageData.addToMessage("\n");
            switch ((PublicUse.PublicUseEvent)this.event.get(character)) {
               case ROUGHCUSTOMER:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.rough", character, arguments));
                  break;
               case GENTLECUSTOMER:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.gentle", character, arguments));
                  break;
               case NICECUSTOMER:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.nice", character, arguments));
                  break;
               case LINE:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.line", character, arguments));
                  break;
               case ALLANAL:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.anal", character, arguments));
                  break;
               case ALLVAGINAL:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.vaginal", character, arguments));
                  break;
               case BUKKAKE:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.bukkake", character, arguments));
                  break;
               case SWARM:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.swarm", character, arguments));
                  break;
               case NOBREAK:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.nobreak", character, arguments));
                  break;
               case OVERTIME:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.overtime", character, arguments));
                  break;
               case ALONE:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.alone", character, arguments));
                  if (this.totalServed.get(character) == this.getCustomers().size()) {
                     this.messageData.addToMessage("\n" + TextUtil.t("public.alone.success", character, arguments));
                  } else {
                     this.messageData.addToMessage("\n" + TextUtil.t("public.alone.failure", character, arguments));
                  }
                  break;
               case NORMAL:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.normal", character, arguments));
                  break;
               case NONE:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.none", character, this.thatOneGirl, arguments));
                  break;
               case NOCUSTOMER:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.nocustomer", character, this.thatOneGirl, arguments));
                  break;
               case ALL:
                  this.messageData.addToMessage("\n" + TextUtil.t("public.all", character, arguments));
            }

            if (this.totalServed.get(character) != 0) {
               if (this.energySpent.get(character) < this.maxEnergy.get(character)) {
                  this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.easy", character, arguments));
                  this.getAttributeModifications()
                     .add(new AttributeModification(-30.0F - this.girlStatus.get(character).shortValue() * 2.0F, EssentialAttributes.ENERGY, character));
               } else if (this.energySpent.get(character) < this.maxEnergy.get(character) * 15 / 10) {
                  this.getAttributeModifications()
                     .add(new AttributeModification(-45.0F - this.girlStatus.get(character).shortValue() * 3.0F, EssentialAttributes.ENERGY, character));
                  this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.normal", character, arguments));
                  if (this.girlStatus.get(character) == 1) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.normal.faint.once", character, arguments));
                  }

                  if (this.girlStatus.get(character) == 2) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.normal.faint.twice", character, arguments));
                  }

                  if (this.girlStatus.get(character) > 2) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.normal.faint.more", character, arguments));
                  }
               } else if (this.energySpent.get(character) < this.maxEnergy.get(character) * 2) {
                  this.getAttributeModifications()
                     .add(new AttributeModification(-60.0F - this.girlStatus.get(character).shortValue() * 4.0F, EssentialAttributes.ENERGY, character));
                  this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.hard", character, arguments));
                  if (this.girlStatus.get(character) == 1) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.hard.faint.once", character, arguments));
                  }

                  if (this.girlStatus.get(character) == 2) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.hard.faint.twice", character, arguments));
                  }

                  if (this.girlStatus.get(character) > 2) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.hard.faint.more", character, arguments));
                  }
               } else {
                  this.getAttributeModifications()
                     .add(new AttributeModification(-75.0F - this.girlStatus.get(character).shortValue() * 5.0F, EssentialAttributes.ENERGY, character));
                  this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.exhausted", character, arguments));
                  if (this.girlStatus.get(character) == 1) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.exhausted.faint.once", character, arguments));
                  }

                  if (this.girlStatus.get(character) == 2) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.exhausted.faint.twice", character, arguments));
                  }

                  if (this.girlStatus.get(character) > 2) {
                     this.messageData.addToMessage(" " + TextUtil.t("public.stamina.exhausted.faint.more", character, arguments));
                  }
               }
            }
         }

         Object[] arguments = new Object[]{totalTips};
         this.messageData.addToMessage("\n\n" + TextUtil.t("public.result.final", arguments));
      } else {
         this.messageData.addToMessage(TextUtil.t("public.notenoughcustomers"));
      }
   }

   @Override
   public MessageData getBaseMessage() {
      Object[] arguments = new Object[]{TextUtil.listCharacters(this.getCharacters()), this.getCustomers().size()};
      String messageText = TextUtil.t("public.basic", arguments);
      this.messageData = new MessageData(messageText, null, this.getBackground());

      for (Charakter character : this.getCharacters()) {
         if (this.getCustomers().size() >= 5) {
            switch ((PublicUse.PublicUseEvent)this.event.get(character)) {
               case ROUGHCUSTOMER:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case GENTLECUSTOMER:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case NICECUSTOMER:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case LINE:
                  if (Util.getInt(0, 100) < 50) {
                     this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
                  } else {
                     this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
                  }
                  break;
               case ALLANAL:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
                  break;
               case ALLVAGINAL:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
                  break;
               case BUKKAKE:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, character));
                  break;
               case SWARM:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case NOBREAK:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case OVERTIME:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case ALONE:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case NORMAL:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                  break;
               case NONE:
               case NOCUSTOMER:
               default:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
                  break;
               case ALL:
                  this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
            }
         } else {
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
         }
      }

      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.getCustomers().size() >= 10) {
         for (Charakter character : this.getCharacters()) {
            if (character.getTraits().contains(Trait.SEXADDICT)) {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -0.75F, EssentialAttributes.MOTIVATION));
            } else {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -1.5F, EssentialAttributes.MOTIVATION));
            }

            if (character.getType() == CharacterType.TRAINER && !character.getTraits().contains(Trait.LEGACYWHORE)) {
               modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -0.5F, BaseAttributeTypes.COMMAND));
            }
         }
      }

      return modifications;
   }

   @Override
   public int getAppeal() {
      return 1;
   }

   @Override
   public int getMaxAttendees() {
      return 60 + this.getCharacters().size() * 40;
   }

   public int getBonus() {
      return this.bonus;
   }

   public void setBonus(int bonus) {
      this.bonus = bonus;
   }

   private boolean isTimeLeft(List<Charakter> list) {
      for (Charakter character : list) {
         if (this.remainingTime.get(character) > 0) {
            return true;
         }
      }

      return false;
   }

   public enum PublicUseEvent {
      ROUGHCUSTOMER,
      GENTLECUSTOMER,
      LINE,
      SWARM,
      NOBREAK,
      NORMAL,
      NICECUSTOMER,
      ALLANAL,
      ALLVAGINAL,
      BUKKAKE,
      OVERTIME,
      ALL,
      ALONE,
      NONE,
      NOCUSTOMER;
   }
}
