package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.items.Inventory;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerGroup extends Customer {
   private Logger log = LogManager.getLogger(CustomerGroup.class);
   private List<Customer> customers = new ArrayList<>();

   @Override
   public String getName() {
      Object[] attributesTmp = new Object[0];
      Object[] attributes = new Object[]{
         this.customers.size(), TextUtil.t("groupgender", this.customers.get(0), attributesTmp), this.customers.get(0).getName()
      };
      String key;
      if (this.getGender() == Gender.MALE) {
         key = "nocheck.groupname." + this.customers.get(0).getType().toString() + ".male";
      } else {
         key = "nocheck.groupname." + this.customers.get(0).getType().toString() + ".female";
      }

      String text = TextUtil.t(key, attributes);
      return !key.equals(text) ? text : TextUtil.t("groupname", attributes);
   }

   @Override
   public CustomerType getType() {
      return CustomerType.GROUP;
   }

   @Override
   public Gender getGender() {
      Util.GenderAmounts genderAmounts = Util.getGenderAmounts(new ArrayList<>(this.customers));
      if (genderAmounts.getGenderAmount(Gender.MALE) == 0 && genderAmounts.getGenderAmount(Gender.FUTA) == 0) {
         return Gender.FEMALE;
      }

      if (genderAmounts.getGenderAmount(Gender.FEMALE) == 0 && genderAmounts.getGenderAmount(Gender.FUTA) == 0) {
         return Gender.MALE;
      }

      if (genderAmounts.getGenderAmount(Gender.MALE) == 0 && genderAmounts.getGenderAmount(Gender.FEMALE) == 0) {
         return Gender.FUTA;
      }

      this.log.error("No valid group gender found.");
      return Gender.MALE;
   }

   public List<Customer> getCustomers() {
      return this.customers;
   }

   @Override
   public int getInitialSatisfaction() {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.getInitialSatisfaction();
      }

      return sum;
   }

   @Override
   public int getMoney() {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.getMoney();
      }

      return sum;
   }

   @Override
   public int getSatisfactionAmount() {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.getSatisfactionAmount();
      }

      return sum / this.customers.size();
   }

   @Override
   public int getMaxSecondaryActivities() {
      return 0;
   }

   @Override
   public float getImportance() {
      float sum = 0.0F;

      for (Customer customer : this.customers) {
         sum += customer.getImportance();
      }

      return sum / this.customers.size();
   }

   @Override
   public void addToSatisfaction(int mod, Object satisfactionModifier) {
      for (Customer customer : this.customers) {
         customer.addToSatisfaction(mod, satisfactionModifier);
      }
   }

   @Override
   public int payFixed(int amount) {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.payFixed(amount / this.customers.size());
      }

      return sum;
   }

   @Override
   public int pay(int amount, float modifier) {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.pay(amount / this.customers.size(), modifier);
      }

      return sum;
   }

   @Override
   public int pay(float modifier) {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.pay(modifier / this.customers.size());
      }

      return sum;
   }

   @Override
   public List<Customer.SatisfactionModifier> getSatisfactionModifiers() {
      List<Customer.SatisfactionModifier> satisfactionModifiers = new ArrayList<>();

      for (Customer customer : this.customers) {
         satisfactionModifiers.addAll(customer.getSatisfactionModifiers());
      }

      return satisfactionModifiers;
   }

   @Override
   public void changePayModifier(float modifier) {
      for (Customer customer : this.customers) {
         customer.changePayModifier(modifier);
      }
   }

   @Override
   public int getHitpoints() {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.getHitpoints();
      }

      return sum;
   }

   @Override
   public int getMaxHitpoints() {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.getMaxHitpoints();
      }

      return sum;
   }

   @Override
   public float modifyHitpoints(float modifier) {
      for (Customer customer : this.customers) {
         if (customer.getHitpoints() > 0 && customer.getMaxHitpoints() > customer.getHitpoints()) {
            return customer.modifyHitpoints(modifier);
         }
      }

      return this.customers.get(0).modifyHitpoints(modifier);
   }

   @Override
   public float getDamage() {
      float sum = 0.0F;

      for (Customer customer : this.customers) {
         if (customer.getHitpoints() > 0) {
            sum += customer.getDamage();
         }
      }

      return sum;
   }

   @Override
   public int getArmor() {
      for (Customer customer : this.customers) {
         if (customer.getHitpoints() > 0) {
            return customer.getArmor();
         }
      }

      return 0;
   }

   @Override
   public float takeDamage(float power) {
      for (Customer customer : this.customers) {
         if (customer.getHitpoints() > 0) {
            return customer.takeDamage(power);
         }
      }

      return 0.0F;
   }

   @Override
   public int getInitialMoney() {
      int sum = 0;

      for (Customer customer : this.customers) {
         sum += customer.getInitialMoney();
      }

      return sum;
   }

   @Override
   public List<Inventory.ItemData> spawnItems() {
      List<Inventory.ItemData> items = new ArrayList<>();

      for (Customer customer : this.getCustomers()) {
         items.addAll(customer.spawnItems());
      }

      return Util.getItemListNormalized(items);
   }

   @Override
   public Inventory.ItemData getItem() {
      return this.getCustomers().size() > 0 ? this.getCustomers().get(Util.getInt(0, this.getCustomers().size())).getItem() : null;
   }
}
