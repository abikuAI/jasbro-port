package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Fight extends RunningActivity implements BusinessMainActivity, BusinessSecondaryActivity {
   private Unit fighter1;
   private Unit fighter2;
   private MessageData message;

   @Override
   public void init() {
      this.fighter1 = this.getCharacter();
      if (this.getCharacters().size() == 2) {
         this.fighter2 = this.getCharacters().get(1);
      } else if (this.getMainCustomer() != null) {
         this.fighter2 = this.getMainCustomer();
      }
   }

   @Override
   public MessageData getBaseMessage() {
      this.message = new MessageData();
      this.message.setBackground(this.getCharacter().getBackground());
      if (this.fighter2 != null) {
         Object[] arguments = new Object[]{this.getCustomers().size()};
         if (this.getCharacters().size() == 2) {
            ImageData image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.getCharacters().get(0));
            ImageData image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.getCharacters().get(1));
            this.message.setImage(image1);
            this.message.setImage2(image2);
            this.message.setMessage(TextUtil.t("fight.basic1", this.getCharacter(), this.getCharacters().get(1), arguments));
         } else {
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.getCharacter());
            this.message.setImage(image);
            this.message.setMessage(TextUtil.t("fight.basic2", this.getCharacter(), this.getMainCustomer(), arguments));
         }
      } else {
         ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter());
         this.message.setImage(image);
         this.message.setMessage(TextUtil.t("fight.noEnemy", this.getCharacter()));
      }

      return this.message;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modificationData = new ArrayList<>();
      if (this.fighter2 != null) {
         modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2F, BaseAttributeTypes.STRENGTH));
         modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8F, SpecializationAttribute.VETERAN));
         modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -40.0F, EssentialAttributes.ENERGY));
         if (this.getCharacters().get(0).getTraits().contains(Trait.FIRSTAID)) {
            modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 10.0F, EssentialAttributes.HEALTH));
         }

         if (this.getCharacters().get(0).getFinalValue(SpecializationAttribute.AGILITY) > 10) {
            modificationData.add(
               new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.getCharacters().get(0), 0.8F, SpecializationAttribute.AGILITY)
            );
         }

         if (this.getCharacters().get(0).getFinalValue(SpecializationAttribute.MAGIC) > 10) {
            modificationData.add(
               new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.getCharacters().get(0), 0.8F, SpecializationAttribute.MAGIC)
            );
         }

         if (this.getCharacters().size() == 2) {
            if (this.getCharacters().get(1).getFinalValue(SpecializationAttribute.AGILITY) > 10) {
               modificationData.add(
                  new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.getCharacters().get(1), 0.8F, SpecializationAttribute.AGILITY)
               );
            }

            if (this.getCharacters().get(1).getFinalValue(SpecializationAttribute.MAGIC) > 10) {
               modificationData.add(
                  new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.getCharacters().get(1), 0.8F, SpecializationAttribute.MAGIC)
               );
            }
         }

         if (!this.getCharacter().getTraits().contains(Trait.LEGACYADVENTURER)) {
            modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.5F, BaseAttributeTypes.COMMAND));
         }

         return modificationData;
      } else {
         return new Idle().getStatModifications();
      }
   }

   @Override
   public int getAppeal() {
      return Util.getInt(2, 8);
   }

   @Override
   public void perform() {
      if (this.fighter2 != null) {
         float entertainmentRating = 0.3F;
         int i = 0;
         float mod1 = 0.0F;
         float mod2 = 0.0F;
         int startHitPoints1 = this.fighter1.getHitpoints();
         int startHitPoints2 = this.fighter2.getHitpoints();
         Battle battle = new Battle(this.fighter1, this.fighter2);

         do {
            battle.doRound();
            entertainmentRating += 0.003F;
            i++;
         } while (
            i < 50
               && this.fighter1.getHitpoints() > 20
               && this.fighter2.getHitpoints() > 20
               && this.fighter1.getHitpoints() > startHitPoints1 - 45
               && this.fighter2.getHitpoints() > startHitPoints2 - 45
         );

         mod1 = startHitPoints1 - this.fighter1.getHitpoints();
         mod2 = startHitPoints2 - this.fighter2.getHitpoints();
         entertainmentRating = entertainmentRating
            + Math.abs(mod1 + mod2) / 100.0F
            + this.fighter1.getDamage() / 20.0F
            + this.fighter1.getArmor() / 1000.0F
            + this.fighter2.getDamage() / 20.0F
            + this.fighter2.getArmor() / 1000.0F;

         for (Customer customer : this.getCustomers()) {
            customer.addToSatisfaction((int)(entertainmentRating * 25.0F), this);
         }

         this.addAttributeModification(this, mod1, this.getCharacter(), EssentialAttributes.HEALTH);
         if (this.getCharacters().size() == 2) {
            this.addAttributeModification(this, mod2, this.getCharacters().get(1), EssentialAttributes.HEALTH);
         }

         int winnings = 0;

         for (Customer customer : this.getCustomers()) {
            winnings += customer.pay(entertainmentRating);
         }

         this.modifyIncome(winnings);
         Unit winner = null;
         if (i <= 100) {
            if (this.fighter1.getHitpoints() > 20 && this.fighter1.getHitpoints() > startHitPoints1 - 45) {
               winner = this.fighter1;
            } else {
               winner = this.fighter2;
            }

            if (this.getMainCustomer() != null) {
               if (this.getMainCustomer().getType() == CustomerType.SOLDIER) {
                  this.getMainCustomer().addToSatisfaction(40, this);
               }

               if (winner == this.getMainCustomer()) {
                  this.getMainCustomer().addToSatisfaction(40, this);
               }
            }
         }

         this.message.addToMessage("\n\n" + battle.getCombatText());
         Object[] arguments = new Object[]{winnings};
         String message = TextUtil.t("fight.result", arguments);
         if (winner == null) {
            message = message + "\n\n" + TextUtil.t("fight.draw");
         } else {
            arguments[0] = winner.getName();
            if (i < 8) {
               message = message + "\n\n" + TextUtil.t("fight.onesided", arguments);
            } else {
               message = message + "\n\n" + TextUtil.t("fight.longMatch", arguments);
            }
         }

         if (this.getCharacters().size() > 1) {
            ImageData image1;
            ImageData image2;
            if (winner == null) {
               image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacters().get(0));
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacters().get(1));
            } else if (winner == this.getCharacters().get(0)) {
               image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, this.getCharacters().get(0));
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacters().get(1));
               if (this.getCharacters().get(0).getTraits().contains(Trait.REPTILIANMOTIVATION)) {
                  this.addAttributeModification(this, 2.0F, this.getCharacters().get(0), EssentialAttributes.MOTIVATION);
               }
            } else {
               image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacters().get(0));
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, this.getCharacters().get(1));
               if (this.getCharacters().get(1).getTraits().contains(Trait.REPTILIANMOTIVATION)) {
                  this.addAttributeModification(this, 2.0F, this.getCharacters().get(1), EssentialAttributes.MOTIVATION);
               }
            }

            this.getMessages().add(new MessageData(message, image1, image2, this.getCharacter().getBackground()));
         } else if (winner == this.getCharacters().get(0)) {
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, this.getCharacter());
            this.getMessages().add(new MessageData(message, image, this.getCharacter().getBackground()));
         } else if (this.getCharacter().getGender() == Gender.FEMALE && Util.getInt(1, 3) == 2 && this.getMainCustomer().getGender() == Gender.MALE) {
            ImageData image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacter());
            ImageData image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.getCharacter());
            message = message + "\n\n" + TextUtil.t("fight.lost", this.getCharacter());
            if (this.getMainCustomer().getPreferredSextype() == Sextype.ANAL) {
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, this.getCharacter());
               this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ANAL, this.getCharacter()));
               message = message + "\n" + TextUtil.t("fight.lost.anal", this.getCharacter());
            } else if (this.getMainCustomer().getPreferredSextype() == Sextype.VAGINAL) {
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.getCharacter());
               this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.VAGINAL, this.getCharacter()));
               message = message + "\n" + TextUtil.t("fight.lost.vaginal", this.getCharacter());
            } else if (this.getMainCustomer().getPreferredSextype() == Sextype.ORAL) {
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, this.getCharacter());
               this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ORAL, this.getCharacter()));
               message = message + "\n" + TextUtil.t("fight.lost.oral", this.getCharacter());
            } else if (this.getMainCustomer().getPreferredSextype() == Sextype.BONDAGE) {
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.BONDAGE, this.getCharacter());
               this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.BONDAGE, this.getCharacter()));
               message = message + "\n" + TextUtil.t("fight.lost.bondage", this.getCharacter());
            } else {
               image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.getCharacter());
               this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.GROUP, this.getCharacter()));
               message = message + "\n" + TextUtil.t("fight.lost.group", this.getCharacter());
            }

            this.getMessages().add(new MessageData(message, image1, image2, this.getCharacter().getBackground()));
         } else {
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacter());
            this.getMessages().add(new MessageData(message, image, this.getCharacter().getBackground()));
         }
      }
   }

   public void addAttributeModification(RunningActivity activity, float amount, Charakter character, AttributeType attributeType) {
      AttributeModification attributeModification = new AttributeModification(0.0F, attributeType, character);
      attributeModification.setRealModification(amount);
      this.getAttributeModifications().add(attributeModification);
   }

   @Override
   public int rateCustomer(Customer customer) {
      if (this.getCharacters().size() > 1) {
         return 0;
      } else if (customer.getType() == CustomerType.SOLDIER) {
         return 100;
      } else {
         return customer.getType() == CustomerType.GROUP && this.getCharacter().getFinalValue(SpecializationAttribute.VETERAN) > 200
            ? Util.getInt(90, 125)
            : (int)customer.getImportance();
      }
   }

   @Override
   public int getMaxAttendees() {
      return 40;
   }

   private enum FightAction {
      DUEL,
      GROUP,
      ROYALRUMBLE,
      SLAVEFIGHT,
      TEAMMATCH;
   }
}
