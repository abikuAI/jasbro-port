package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.texts.TextUtil;

public class EquipmentChangeCustomerTypeSatisfaction extends EquipmentEffect {
   private CustomerType customerType;
   private int amount;

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.CHANGECUSTOMERTYPESATISFACTION;
   }

   @Override
   public void handleEvent(MyEvent e, Charakter character) {
      if (e.getType() == EventType.ACTIVITY && this.customerType != null) {
         RunningActivity activity = (RunningActivity)e.getSource();
         if (activity instanceof BusinessMainActivity) {
            BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
            if (businessMainActivity.getMainCustomers().size() == 1) {
               Customer customer = activity.getMainCustomer();
               if (customer.getType() == this.customerType) {
                  customer.addToSatisfaction(this.amount, "Equipment");
               }
            }
         }
      }
   }

   public CustomerType getCustomerType() {
      return this.customerType;
   }

   public void setCustomerType(CustomerType customerType) {
      this.customerType = customerType;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   @Override
   public String getDescription() {
      if (this.customerType == null) {
         return "";
      } else {
         return this.amount < 0
            ? TextUtil.t("equipment.customerSatisfactionMinus", this.customerType.getText(), this.amount)
            : TextUtil.t("equipment.customerSatisfactionPlus", this.customerType.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      if (this.customerType == null) {
         return 0.0;
      } else {
         return this.customerType != CustomerType.GROUP ? CustomerType.generateCustomer(this.customerType).getImportance() * 10.0F : 200.0;
      }
   }

   @Override
   public int getAmountEffects() {
      return this.amount;
   }
}
