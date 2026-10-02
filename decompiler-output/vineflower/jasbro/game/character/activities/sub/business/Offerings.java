package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.items.Inventory;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Offerings extends RunningActivity implements BusinessSecondaryActivity {
   private MessageData messageData;
   private int bonus;

   @Override
   public void perform() {
      Charakter character = this.getCharacter();
      int skill = Util.getInt(0, 5) * character.getCharisma() / 8;
      if (skill > 20) {
         skill = 20;
      }

      int amountEarned = 0;
      int amountHappy = 0;
      int overalltips = 0;

      for (Customer customer : this.getCustomers()) {
         List<Inventory.ItemData> loot = new ArrayList<>();
         if (Util.getInt(0, 100) < skill + customer.getInitialSatisfaction() / 5) {
            Inventory.ItemData itemStolen = customer.getItem();
            if (itemStolen != null) {
               loot.add(itemStolen);
            }
         }

         if (loot.size() > 0) {
            Jasbro.getInstance().getData().getInventory().addItems(loot);
            Object[] arguments = new Object[]{TextUtil.listItems(loot)};
            this.getMessages().get(0).addToMessage("\n" + TextUtil.t("offerings.loot", character, arguments));
         }

         if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
            amountHappy++;
            customer.addToSatisfaction(skill, this);
            int tips = 0;
            short var12;
            switch (customer.getType()) {
               case PEASANT:
                  var12 = 10;
                  break;
               case SOLDIER:
                  var12 = 20;
                  break;
               case MERCHANT:
                  var12 = 40;
                  break;
               case BUSINESSMAN:
                  var12 = 80;
                  break;
               case MINORNOBLE:
                  var12 = 160;
                  break;
               case LORD:
                  var12 = 320;
                  break;
               case CELEBRITY:
                  var12 = 640;
                  break;
               default:
                  var12 = 5;
            }

            var12 = customer.pay(var12, this.getCharacter().getMoneyModifier());
            overalltips += var12;
            amountEarned += var12;
         } else {
            customer.addToSatisfaction(skill / 4, this);
         }
      }

      this.modifyIncome(amountEarned);
      if (amountEarned > 0) {
         this.messageData
            .addToMessage("\n\n" + TextUtil.t("offerings.result", this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips));
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String messageText = TextUtil.t("offerings.basic", this.getCharacter());
      this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), this.getBackground());
      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05F, BaseAttributeTypes.CHARISMA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.02F, BaseAttributeTypes.OBEDIENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.02F, BaseAttributeTypes.COMMAND));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8F, EssentialAttributes.MOTIVATION));
      return modifications;
   }

   @Override
   public int getAppeal() {
      return Util.getInt(1, 30);
   }

   @Override
   public int getMaxAttendees() {
      return 15;
   }

   public int getBonus() {
      return this.bonus;
   }

   public void setBonus(int bonus) {
      this.bonus = bonus;
   }
}
