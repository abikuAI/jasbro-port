package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Catshow extends RunningActivity implements BusinessSecondaryActivity {
   private MessageData messageData;
   private int bonus;

   @Override
   public void perform() {
      Charakter character = this.getCharacter();
      int skill = character.getCharisma() + character.getFinalValue(SpecializationAttribute.CATGIRL) / 4 + 1;
      int amountEarned = 0;
      int amountHappy = 0;
      int overalltips = 0;

      for (Customer customer : this.getCustomers()) {
         if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
            amountHappy++;
            customer.addToSatisfaction(skill, this);
            int tips = (int)(customer.getMoney() / (1500.0 / skill) + Util.getInt(10, 20));
            tips = customer.pay(tips, this.getCharacter().getMoneyModifier());
            overalltips += tips;
            amountEarned += tips;
         } else {
            customer.addToSatisfaction(skill / 4, this);
         }
      }

      this.modifyIncome(amountEarned);
      if (character.getFinalValue(SpecializationAttribute.CATGIRL) > 75 && amountEarned > 5000) {
         this.messageData
            .addToMessage(
               "\n\n" + TextUtil.t("catshow.result.skill", this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips)
            );
      } else if (amountEarned > 2000) {
         this.messageData
            .addToMessage(
               "\n\n" + TextUtil.t("catshow.result.owned", this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips)
            );
      } else {
         this.messageData
            .addToMessage(
               "\n\n" + TextUtil.t("catshow.result.basic", this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips)
            );
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String messageText = TextUtil.t("catshow.basic", this.getCharacter());
      this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.CATGIRL, this.getCharacter()), this.getBackground());
      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2F, SpecializationAttribute.CATGIRL));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.02F, BaseAttributeTypes.STRENGTH));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.02F, BaseAttributeTypes.STAMINA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05F, BaseAttributeTypes.CHARISMA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.02F, BaseAttributeTypes.OBEDIENCE));
      if (!this.getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3F, BaseAttributeTypes.COMMAND));
      }

      return modifications;
   }

   @Override
   public int getAppeal() {
      return (this.getCharacter().getCharisma() + this.getCharacter().getFinalValue(SpecializationAttribute.CATGIRL) / 4) / 4;
   }

   @Override
   public int getMaxAttendees() {
      return 25 + this.bonus;
   }

   public int getBonus() {
      return this.bonus;
   }

   public void setBonus(int bonus) {
      this.bonus = bonus;
   }
}
