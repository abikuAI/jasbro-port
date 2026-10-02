package jasbro.game.character.activities.sub.whore;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.housing.House;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Whore extends RunningActivity implements BusinessMainActivity {
   private static final Logger log = LogManager.getLogger(Whore.class);
   private House house;
   private MessageData messageData = new MessageData();
   private Sextype sexType;
   private float amountActions = 0.0F;
   private float energyMultiplier = 1.0F;
   private int executionTime = 10;
   private int cooldownTime = 0;
   private float executionModifier = 0.0F;
   private float cooldownModifier = 0.0F;

   @Override
   public void init() {
      this.house = this.getHouse();
      this.sexType = this.getMainCustomer().getPreferredSextype();
      Charakter whore = this.getCharacters().get(0);
      if (this.sexType == Sextype.GROUP) {
         CustomerGroup group = (CustomerGroup)this.getMainCustomer();
         this.energyMultiplier = group.getCustomers().size() / 1.3F;
      }

      if (this.getMainCustomer().getStatus() == CustomerStatus.HORNYSTATUS) {
         this.setExecutionModifier(this.getExecutionModifier() + 0.6F);
         this.setCooldownModifier(this.getCooldownModifier() + 0.3F);
         this.energyMultiplier *= 1.7F;
      }

      if (this.getMainCustomer().getStatus() == CustomerStatus.VERYHORNY) {
         this.setExecutionModifier(this.getExecutionModifier() + 0.8F);
         this.setCooldownModifier(this.getCooldownModifier() + 0.4F);
         this.energyMultiplier *= 2.4F;
      }

      if (this.getMainCustomer().getStatus() == CustomerStatus.STRONGSTATUS) {
         this.setExecutionModifier(this.getExecutionModifier() + 0.1F);
         this.setCooldownModifier(this.getCooldownModifier() + 0.3F);
         this.energyMultiplier *= 1.3F;
      }

      if (this.getMainCustomer().getStatus() == CustomerStatus.LIVELY) {
         this.setExecutionModifier(this.getExecutionModifier() + 0.2F);
         this.setCooldownModifier(this.getCooldownModifier() + 0.2F);
         this.energyMultiplier *= 1.1F;
      }

      if (this.getMainCustomer().getStatus() == CustomerStatus.TIRED) {
         this.setExecutionModifier(this.getExecutionModifier() - 0.2F);
         this.setCooldownModifier(this.getCooldownModifier() - 0.2F);
         this.energyMultiplier *= 0.7F;
      }

      if (this.getMainCustomer().getStatus() == CustomerStatus.SHYSTATUS) {
         this.setExecutionModifier(this.getExecutionModifier() + Util.getInt(-1, 1) * 0.1F);
         this.setCooldownModifier(this.getCooldownModifier() + 0.0F);
         this.energyMultiplier *= 0.8F;
      }

      String locationName;
      if (this.house != null) {
         locationName = this.house.getName();
      } else {
         locationName = this.getCharacterLocation().getName();
      }

      String message;
      if (this.house != null && this.house.getInternName() != null && !this.house.getInternName().trim().equals("")) {
         message = TextUtil.t(
               "whore.basic2", whore, this.getMainCustomer(), this.getMainCustomer().getStatusName(), this.getMainCustomer().getName(), locationName
            )
            + " ";
      } else {
         message = TextUtil.t(
               "whore.basic1", whore, this.getMainCustomer(), this.getMainCustomer().getStatusName(), this.getMainCustomer().getName(), locationName
            )
            + " ";
      }

      message = message + TextUtil.t("whore.service", whore, this.getMainCustomer());
      if (this.getMainCustomer().getStatus() == CustomerStatus.DRUNK && Util.getInt(1, 15) == 1
         || this.getMainCustomer().getStatus() == CustomerStatus.DRUNK && Util.getInt(1, 10) == 1) {
         message = message + "\n" + TextUtil.t("whore.drunkcustomer");
         this.energyMultiplier = 0.1F;
         this.setExecutionModifier(this.getExecutionModifier() - 0.7F);
         this.setCooldownModifier(this.getCooldownModifier() - 0.7F);
      }

      message = message + "\n";
      CharacterLocation characterLocation = whore.getActivity().getSource();
      message = message + this.checkPossible(this, whore);
      this.messageData = new MessageData(message, null, characterLocation.getImage());
      if (this.sexType != null) {
         int satisfaction = 5 + whore.getFinalValue(this.sexType) / 10;
         satisfaction += whore.getFinalValue(BaseAttributeTypes.OBEDIENCE) / 10;
         satisfaction += whore.getFinalValue(BaseAttributeTypes.CHARISMA) / 10;
         satisfaction += whore.getFinalValue(SpecializationAttribute.SEDUCTION) / 10;
         if (this.getHouse() != null) {
            satisfaction += this.getHouse().getCleanState().getSatisfactionModifier();
         }

         if (this.getMainCustomer().getStatus() == CustomerStatus.PISSED || this.getMainCustomer().getStatus() == CustomerStatus.SAD) {
            satisfaction /= 2;
         }

         this.getMainCustomer().addToSatisfaction(satisfaction, this);
         this.setMinimumObedience(this.sexType.getObedienceRequired());
      }
   }

   public String checkPossible(RunningActivity activity, Charakter whore) {
      String message = "";
      List<Sextype> possibleSextypes = this.getPossibleSextypes();
      boolean obedienceTooLow = whore.getRealMinObedience(this.sexType.getObedienceRequired(), activity) > whore.getObedience();
      boolean isSextypeNotAllowed = !whore.getAllowedServices().isAllowed(this.sexType);
      boolean notPossible = !possibleSextypes.contains(this.getMainCustomer().getPreferredSextype());
      if (!obedienceTooLow && !isSextypeNotAllowed && !notPossible) {
         message = message + TextUtil.t("whore.sextype." + this.sexType.toString(), whore, this.getMainCustomer()) + ".";
      } else {
         int minReqObdience = Integer.MAX_VALUE;

         for (Sextype sextype : possibleSextypes) {
            int obedienceRequired = whore.getRealMinObedience(sextype.getObedienceRequired(), activity);
            if (obedienceRequired < minReqObdience) {
               minReqObdience = obedienceRequired;
            }
         }

         if (!isSextypeNotAllowed && whore.getRealMinObedience(this.sexType.getObedienceRequired(), activity) == minReqObdience) {
            return "";
         }

         if (possibleSextypes.size() == 0) {
            log.error("No possible sextypes {} {}", new Object[]{this.getCharacter().getName(), this.getMainCustomer().getName()});
            this.sexType = null;
            return "";
         }

         if (obedienceTooLow) {
            this.getMainCustomer()
               .addToSatisfaction(-10 + (whore.getObedience() - whore.getRealMinObedience(this.sexType.getObedienceRequired(), activity)) * 10, activity);
            if (this.getMainCustomer().getStatus() == CustomerStatus.PISSED || this.getMainCustomer().getStatus() == CustomerStatus.HORNYSTATUS) {
               this.getMainCustomer().addToSatisfaction(-100, activity);
            }
         } else if (isSextypeNotAllowed) {
            this.getMainCustomer().addToSatisfaction(-40, activity);
            if (this.getMainCustomer().getStatus() == CustomerStatus.PISSED || this.getMainCustomer().getStatus() == CustomerStatus.HORNYSTATUS) {
               this.getMainCustomer().addToSatisfaction(-100, activity);
            }
         } else {
            this.getMainCustomer().addToSatisfaction(-15, activity);
         }

         message = message + TextUtil.t("whore.sextype." + this.sexType.toString(), whore, this.getMainCustomer());
         if (!obedienceTooLow && !isSextypeNotAllowed) {
            message = message + TextUtil.t("whore.notpossible", whore, this.getMainCustomer());
         } else {
            message = message + TextUtil.t("whore.refuse", whore, this.getMainCustomer());
         }

         do {
            this.sexType = possibleSextypes.get(Util.getInt(0, possibleSextypes.size()));
         } while (
            whore.getRealMinObedience(this.sexType.getObedienceRequired(), activity) > whore.getObedience()
               && whore.getRealMinObedience(this.sexType.getObedienceRequired(), activity) != minReqObdience
         );

         if (this.sexType != null) {
            message = message + " " + TextUtil.t("whore.replacement." + this.sexType.toString(), whore, this.getMainCustomer());
         } else {
            this.getMainCustomer().addToSatisfaction(-100, activity);
            message = message + " " + TextUtil.t("whore.noreplacement", whore, this.getMainCustomer());
         }
      }

      switch (this.sexType) {
         case VAGINAL:
            this.setExecutionTime(25);
            this.setCooldownTime(25);
            break;
         case ANAL:
            this.setExecutionTime(20);
            this.setCooldownTime(30);
            break;
         case ORAL:
            this.setExecutionTime(25);
            this.setCooldownTime(15);
            break;
         case FOREPLAY:
            this.setExecutionTime(30);
            this.setCooldownTime(10);
            break;
         case TITFUCK:
            this.setExecutionTime(25);
            this.setCooldownTime(15);
            break;
         case BONDAGE:
            this.setExecutionTime(30);
            this.setCooldownTime(50);
            break;
         case GROUP:
            CustomerGroup group = (CustomerGroup)this.getMainCustomer();
            this.setExecutionTime(20 + 5 * group.getCustomers().size());
            this.setCooldownTime(20 + 5 * group.getCustomers().size());
      }

      return message;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modificationData = new ArrayList<>();
      if (this.sexType != null) {
         modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.energyMultiplier * 0.5F, this.sexType));
         modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.STAMINA));
         modificationData.add(
            new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, this.energyMultiplier * 0.2F, SpecializationAttribute.SEDUCTION)
         );
         if (!this.getCharacter().getTraits().contains(Trait.NYMPHO)
            && !this.getCharacter().getTraits().contains(Trait.SEXADDICT)
            && !this.getCharacter().getTraits().contains(Trait.BEDROOMPRINCESS)) {
            modificationData.add(
               new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.1F * this.energyMultiplier, EssentialAttributes.MOTIVATION)
            );
         } else {
            modificationData.add(
               new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.08F * this.energyMultiplier, EssentialAttributes.MOTIVATION)
            );
         }

         float obedienceModifier = 0.01F;
         switch (this.sexType) {
            case BONDAGE:
               modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0F, EssentialAttributes.ENERGY));
               modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0F, EssentialAttributes.HEALTH));
               modificationData.add(
                  new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3F * this.energyMultiplier, EssentialAttributes.MOTIVATION)
               );
               obedienceModifier = 0.05F;
               break;
            case GROUP:
               modificationData.add(
                  new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -7.0F * this.energyMultiplier, EssentialAttributes.ENERGY)
               );
               modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -1.0F, EssentialAttributes.HEALTH));
               break;
            case MONSTER:
               modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -35.0F, EssentialAttributes.ENERGY));
               modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0F, EssentialAttributes.HEALTH));
               modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0F, EssentialAttributes.MOTIVATION));
               break;
            default:
               modificationData.add(
                  new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -6.0F * this.energyMultiplier, EssentialAttributes.ENERGY)
               );
         }

         modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, obedienceModifier, BaseAttributeTypes.OBEDIENCE));
         if (!this.getCharacter().getTraits().contains(Trait.LEGACYWHORE)) {
            modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.2F, BaseAttributeTypes.COMMAND));
         }
      }

      return modificationData;
   }

   @Override
   public MessageData getBaseMessage() {
      List<ImageTag> tags = this.getCharacter().getBaseTags();
      if (this.sexType != null) {
         tags.add(0, this.sexType.getAssociatedImageTag());
         tags.addAll(ImageTag.getAssociatedImageTags(this.getCharacter(), this.getMainCustomer()));
      } else {
         tags.add(0, ImageTag.STANDARD);
      }

      if (tags.contains(ImageTag.FUTA)) {
         tags.add(0, ImageTag.FUTA);
      }

      if (tags.contains(ImageTag.LESBIAN)) {
         tags.add(0, ImageTag.LESBIAN);
      }

      final ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages());
      this.messageData.setImage(image);
      if (this.sexType != null) {
         this.messageData
            .addFuture(
               Jasbro.getThreadpool()
                  .submit(
                     new Callable<MessageData>() {
                        public MessageData call() throws Exception {
                           int chance = 5;
                           if (chance <= 4) {
                              ImageTag specificTag = ImageTag.getSpecificTag(Whore.this.sexType.getAssociatedImageTag(), image.getTags());
                              String message = null;
                              if (image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
                                 message = "\n"
                                    + TextUtil.t("whore.sextype2.dominant." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              }

                              if (image.getTags().contains(ImageTag.SELF)) {
                                 message = "\n"
                                    + TextUtil.t("whore.sextype2.self." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              } else if (image.getTags().contains(ImageTag.SUBMISSIVEPOSITION)) {
                                 message = "\n"
                                    + TextUtil.t("whore.sextype2.submissive." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              }

                              if (message == null) {
                                 message = "\n" + TextUtil.t("whore.sextype2." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              }

                              return message != null && !message.startsWith("whore.sextype2.") ? new MessageData(message, null, null, null) : null;
                           } else {
                              ImageTag specificTag = ImageTag.getSpecificTag(Whore.this.sexType.getAssociatedImageTag(), image.getTags());
                              String message = null;
                              if (image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
                                 message = "\n"
                                    + TextUtil.t("sex.specific.dominant." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              }

                              if (image.getTags().contains(ImageTag.SELF)) {
                                 message = "\n"
                                    + TextUtil.t("sex.specific.self." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              } else if (image.getTags().contains(ImageTag.SUBMISSIVEPOSITION)) {
                                 message = "\n"
                                    + TextUtil.t("sex.specific.submissive." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              }

                              if (!image.getTags().contains(ImageTag.SUBMISSIVEPOSITION)
                                 || !image.getTags().contains(ImageTag.SELF)
                                 || !image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
                                 message = "\n"
                                    + TextUtil.t("sex.specific.general." + specificTag, Whore.this.getCharacter(), Whore.this.getMainCustomer(), false);
                              }

                              if (message == null) {
                                 return null;
                              } else {
                                 return message != null && !message.startsWith("sex.sextype.") ? new MessageData(message, null, null, null) : null;
                              }
                           }
                        }
                     }
                  )
            );
      }

      return this.messageData;
   }

   @Override
   public void perform() {
      if (this.sexType != null) {
         int payment = 0;
         if (this.sexType == Sextype.GROUP) {
            CustomerGroup group = (CustomerGroup)this.getMainCustomer();

            for (Customer cust : group.getCustomers()) {
               payment += 1
                  + cust.pay(
                     this.getMainCustomer().getMoney() * this.getMainCustomer().getSatisfactionAmount() / 200 / (group.getCustomers().size() - 1),
                     this.getCharacter().getMoneyModifier()
                  );
            }
         } else {
            payment += 1
               + this.getMainCustomer()
                  .pay(this.getMainCustomer().getMoney() * this.getMainCustomer().getSatisfactionAmount() / 200, this.getCharacter().getMoneyModifier());
         }

         if (this.getMainCustomer().getStatus() == CustomerStatus.HORNYSTATUS) {
            payment += 1 + payment;
         }

         if (this.getMainCustomer().getStatus() == CustomerStatus.VERYHORNY) {
            payment += 1 + payment;
            payment += 1 + payment;
         }

         this.minPayment(payment);
         this.modifyIncome(payment);
      }

      String end;
      if (this.getMainCustomer().getPreferredSextype() == this.sexType) {
         end = "end";
      } else {
         end = "end2";
      }

      Object[] arguments = new Object[]{this.getMainCustomer().getSatisfaction().getText(), this.getIncome()};
      this.messageData.addToMessage("\n\n" + TextUtil.t("whore." + end, this.getCharacter(), this.getMainCustomer(), arguments));
      if (this.sexType != null && this.getHouse() != null) {
         House house = this.getHouse();
         house.modDirt(1);
      }

      float mult = 1.0F;
      mult = 1.0F - (this.getCharacters().get(0).getFinalValue(this.sexType) - 20) / 100.0F + this.getExecutionModifier();
      if (mult <= 0.0F) {
         mult = 0.01F;
      }

      this.setExecutionTime((int)(this.getExecutionTime() * mult));
      if (this.getExecutionTime() < 5) {
         this.setExecutionTime(5);
      }

      mult = 1.0F - (this.getCharacters().get(0).getFinalValue(BaseAttributeTypes.STAMINA) - 20) / 100.0F + this.getCooldownModifier();
      if (mult <= 0.0F) {
         mult = 0.01F;
      }

      this.setCooldownTime((int)(this.getCooldownTime() * mult));
      this.setAmountActions(this.getExecutionTime() + this.getCooldownTime());
      if (!this.getCharacter().getTraits().contains(Trait.ONENIGHT)) {
         if (this.getCooldownTime() < 5) {
            if (Util.getInt(0, 100) > 50) {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.veryshort.one", this.getCharacter()));
            } else {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.veryshort.two", this.getCharacter()));
            }
         } else if (this.getCooldownTime() < 15) {
            if (Util.getInt(0, 100) > 50) {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.short.one", this.getCharacter()));
            } else {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.short.two", this.getCharacter()));
            }
         } else if (this.getCooldownTime() < 30) {
            if (Util.getInt(0, 100) > 50) {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.normal.one", this.getCharacter()));
            } else {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.normal.two", this.getCharacter()));
            }
         } else if (this.getCooldownTime() < 60) {
            if (Util.getInt(0, 100) > 50) {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.long.one", this.getCharacter()));
            } else {
               this.messageData.addToMessage("\n" + TextUtil.t("whore.break.long.two", this.getCharacter()));
            }
         } else if (Util.getInt(0, 100) > 50) {
            this.messageData.addToMessage("\n" + TextUtil.t("whore.break.verylong.one", this.getCharacter()));
         } else {
            this.messageData.addToMessage("\n" + TextUtil.t("whore.break.verylong.two", this.getCharacter()));
         }

         if (ConfigHandler.isShowTimeTaken()) {
            Object[] detail = new Object[]{this.getExecutionTime(), this.getCooldownTime()};
            this.messageData.addToMessage("\n" + TextUtil.t("whore.timetaken", this.getCharacter(), detail));
         }
      }
   }

   @Override
   public int rateCustomer(Customer customer) {
      Charakter character = this.getCharacter();
      if (customer.getGender() == Gender.MALE && !character.getAllowedServices().isServiceMales()) {
         return 0;
      }

      if (customer.getGender() == Gender.FEMALE && !character.getAllowedServices().isServiceFemales()) {
         return 0;
      }

      if (customer.getGender() == Gender.FUTA && !character.getAllowedServices().isServiceFutas()) {
         return 0;
      }

      List<Sextype> sexTypes = this.getPossibleSextypes(customer);
      if (sexTypes.size() == 0) {
         return 0;
      }

      if (customer.getPreferredSextype().getObedienceRequired() > character.getObedience()) {
         int rating = 4 + (character.getObedience() - customer.getPreferredSextype().getObedienceRequired());
         if (rating < 1) {
            rating = 1;
         }

         return rating;
      } else {
         return !sexTypes.contains(customer.getPreferredSextype())
            ? 4
            : (int)(
               1.0F
                  + customer.getImportance()
                  + character.getObedience()
                  + character.getCharisma()
                  + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 5
            );
      }
   }

   public List<Sextype> getPossibleSextypes(Customer customer) {
      List<Sextype> sextypes = Sextype.getPossibleSextypes(customer, this.getCharacter());
      if (this.getHouse() == null) {
         sextypes.remove(Sextype.BONDAGE);
      }

      return sextypes;
   }

   private List<Sextype> getPossibleSextypes() {
      return this.getPossibleSextypes(this.getMainCustomer());
   }

   public Float getAmountActions() {
      return this.amountActions;
   }

   public Integer minPayment(int payment) {
      if (payment <= 6) {
         payment = 7;
      }

      return payment;
   }

   public void setAmountActions(float amountActions) {
      this.amountActions = amountActions;
   }

   public Sextype getSexType() {
      return this.sexType;
   }

   public void setSexType(Sextype sexType) {
      this.sexType = sexType;
   }

   public MessageData getMessageData() {
      return this.messageData;
   }

   public int getExecutionTime() {
      return this.executionTime;
   }

   public void setExecutionTime(int executionTime) {
      this.executionTime = executionTime;
   }

   public int getCooldownTime() {
      return this.cooldownTime;
   }

   public void setCooldownTime(int cooldownTime) {
      this.cooldownTime = cooldownTime;
   }

   public float getExecutionModifier() {
      return this.executionModifier;
   }

   public void setExecutionModifier(float f) {
      this.executionModifier = f;
   }

   public float getCooldownModifier() {
      return this.cooldownModifier;
   }

   public void setCooldownModifier(float f) {
      this.cooldownModifier = f;
   }
}
