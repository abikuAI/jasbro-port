package jasbro.game.events.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.perktrees.DominatrixPerks;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnData implements MyEventListener {
   private Map<CustomerType, Integer> spawnChanceModificatorMap;
   private Map<CustomerType, Integer> spawnAmountModificatorMap;
   private Map<Gender, Integer> genderSpawnMap;
   private int bonusCustomers = 0;
   private float modCustomerAmount = 0.0F;
   private transient List<SpawnData.CustomerData> chances;
   private Gender lastModified = Gender.MALE;
   private Gender lastModifiedPassive = Gender.FEMALE;

   public SpawnData() {
      this.spawnChanceModificatorMap = new HashMap<>();
      this.spawnAmountModificatorMap = new HashMap<>();
      this.genderSpawnMap = new HashMap<>();
      this.genderSpawnMap.put(Gender.MALE, 90);
      this.genderSpawnMap.put(Gender.FEMALE, 10);
      this.genderSpawnMap.put(Gender.FUTA, 0);
   }

   public SpawnData(SpawnData spawnData) {
      this.spawnChanceModificatorMap = new HashMap<>(spawnData.spawnChanceModificatorMap);
      this.spawnAmountModificatorMap = new HashMap<>(spawnData.spawnAmountModificatorMap);
      this.genderSpawnMap = new HashMap<>(spawnData.genderSpawnMap);
   }

   public void addFixedAmountCustomers(CustomerType customerType, int amount) {
      if (!this.spawnAmountModificatorMap.containsKey(customerType)) {
         this.spawnAmountModificatorMap.put(customerType, 0);
      }

      this.spawnAmountModificatorMap.put(customerType, this.spawnAmountModificatorMap.get(customerType) + amount);
   }

   public void addToSpawnChance(CustomerType customerType, int amount) {
      if (!this.spawnChanceModificatorMap.containsKey(customerType)) {
         this.spawnChanceModificatorMap.put(customerType, 0);
      }

      this.spawnChanceModificatorMap.put(customerType, this.spawnChanceModificatorMap.get(customerType) + amount);
   }

   public Map<CustomerType, Integer> getSpawnChanceModificatorMap() {
      return this.spawnChanceModificatorMap;
   }

   public void setSpawnChanceModificatorMap(Map<CustomerType, Integer> spawnChanceModificator) {
      this.spawnChanceModificatorMap = spawnChanceModificator;
   }

   public Map<CustomerType, Integer> getSpawnAmountModificatorMap() {
      return this.spawnAmountModificatorMap;
   }

   public void setSpawnAmountModificatorMap(Map<CustomerType, Integer> spawnAmountModificator) {
      this.spawnAmountModificatorMap = spawnAmountModificator;
   }

   public int getPercentFemale() {
      return this.genderSpawnMap.get(Gender.FEMALE);
   }

   public int getPercentFuta() {
      return this.genderSpawnMap.get(Gender.FUTA);
   }

   public int getPercentMale() {
      return this.genderSpawnMap.get(Gender.MALE);
   }

   public int getChance(Gender gender) {
      if (!this.genderSpawnMap.containsKey(gender)) {
         this.genderSpawnMap.put(gender, 0);
      }

      return this.genderSpawnMap.get(gender);
   }

   public void increaseChance(Gender gender) {
      if (this.modify(gender, true)) {
         if (this.lastModified != gender) {
            Gender genderTmp = Gender.getRemaining(gender, this.lastModified);
            if (this.modify(genderTmp, false)) {
               this.lastModifiedPassive = genderTmp;
            } else {
               this.modify(this.lastModified, false);
               this.lastModifiedPassive = this.lastModified;
            }
         } else {
            Gender genderTmp = Gender.getRemaining(gender, this.lastModifiedPassive);
            if (this.modify(genderTmp, false)) {
               this.lastModifiedPassive = genderTmp;
            } else {
               this.modify(this.lastModifiedPassive, false);
            }
         }

         this.lastModified = gender;
      }
   }

   public void decreaseChance(Gender gender) {
      if (this.modify(gender, false)) {
         if (this.lastModified != gender) {
            Gender genderTmp = Gender.getRemaining(gender, this.lastModified);
            if (this.modify(genderTmp, true)) {
               this.lastModifiedPassive = genderTmp;
            } else {
               this.modify(this.lastModified, true);
               this.lastModifiedPassive = this.lastModified;
            }
         } else {
            Gender genderTmp = Gender.getRemaining(gender, this.lastModifiedPassive);
            if (this.modify(genderTmp, true)) {
               this.lastModifiedPassive = genderTmp;
            } else {
               this.modify(this.lastModifiedPassive, true);
            }
         }

         this.lastModified = gender;
      }
   }

   public boolean modify(Gender gender, boolean positive) {
      int value = this.genderSpawnMap.get(gender);
      if (positive) {
         if (value >= 100) {
            return false;
         }

         this.genderSpawnMap.put(gender, value + 10);
         return true;
      } else {
         if (value <= 0) {
            return false;
         }

         this.genderSpawnMap.put(gender, value - 10);
         return true;
      }
   }

   public Gender generateGender() {
      int chance = Util.getInt(0, 100);
      int sumChance = 0;
      Gender[] genders = Gender.values();

      for (int i = 0; i < genders.length - 1; i++) {
         if (chance < this.getChance(genders[i]) + sumChance) {
            return genders[i];
         }

         sumChance += this.getChance(genders[i]);
      }

      return genders[genders.length - 1];
   }

   public List<Customer> spawn(int baseAmountCustomers, Fame fame) {
      BusinessCalculations calc = new BusinessCalculations();
      List<Customer> customers = new ArrayList<>();
      this.chances = calc.getBaseChances(fame);
      int finalAmountCustomers = (int)(baseAmountCustomers * (1.0F + this.modCustomerAmount) + this.bonusCustomers);

      for (SpawnData.CustomerData chance : this.chances) {
         if (this.spawnChanceModificatorMap.containsKey(chance.getCustomerType())) {
            chance.setValue(chance.getValue() + this.spawnChanceModificatorMap.get(chance.getCustomerType()));
         }
      }

      for (int i = 0; i < finalAmountCustomers; i++) {
         customers.add(this.spawnCustomer(0));
      }

      for (CustomerType customerType : CustomerType.values()) {
         if (this.spawnAmountModificatorMap.containsKey(customerType)) {
            for (int i = 0; i < this.spawnAmountModificatorMap.get(customerType); i++) {
               customers.add(this.createCustomer(customerType));
            }
         }
      }

      Collections.shuffle(customers);
      return customers;
   }

   private Customer spawnCustomer(int modifier) {
      for (SpawnData.CustomerData customerData : this.chances) {
         if (Util.getInt(0, 100) - modifier < customerData.getValue()) {
            return this.createCustomer(customerData.getCustomerType());
         }
      }

      return this.createCustomer(CustomerType.BUM);
   }

   public Customer createCustomer(CustomerType customerType) {
      Customer customer = CustomerType.generateCustomer(customerType);
      if (customer instanceof CustomerGroup) {
         this.initGroup((CustomerGroup)customer);
      }

      customer.setGender(this.generateGender());
      customer.setPreferredSextype(Sextype.getPreferredSextype(customer));
      int i = Util.getInt(0, 65);
      if (i < 5) {
         customer.setStatus(CustomerStatus.PISSED);
      } else if (i < 11) {
         customer.setStatus(CustomerStatus.SHYSTATUS);
      } else if (i < 16) {
         customer.setStatus(CustomerStatus.SAD);
      } else if (i < 22) {
         customer.setStatus(CustomerStatus.STRONGSTATUS);
      } else if (i < 32) {
         customer.setStatus(CustomerStatus.HAPPY);
      } else if (i < 42) {
         customer.setStatus(CustomerStatus.HORNYSTATUS);
      } else if (i < 45) {
         customer.setStatus(CustomerStatus.TIRED);
      } else if (i < 55) {
         customer.setStatus(CustomerStatus.LIVELY);
      } else if (i < 60) {
         customer.setStatus(CustomerStatus.DRUNK);
      } else {
         customer.setStatus(CustomerStatus.VERYHORNY);
      }

      for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
         if (character.getSkillTrees().contains(SkillTree.DOMINATRIX)
            && character.getConditions().contains(DominatrixPerks.aggressiveAdvertiser)
            && character.getActivity().getType() == ActivityType.ADVERTISE) {
            int j = Util.getInt(0, 100);
            if (j < 30) {
               customer.setStatus(CustomerStatus.HORNYSTATUS);
            } else if (j < 40) {
               customer.setStatus(CustomerStatus.VERYHORNY);
            }
         }
      }

      return customer;
   }

   private void initGroup(CustomerGroup customerGroup) {
      int chance = Util.getInt(0, 100);
      int amount;
      if (chance < 30) {
         amount = 2;
      } else if (chance < 50) {
         amount = 3;
      } else if (chance < 70) {
         amount = 4;
      } else if (chance < 80) {
         amount = 5;
      } else if (chance < 85) {
         amount = 6;
      } else if (chance < 90) {
         amount = 7;
      } else if (chance < 95) {
         amount = 8;
      } else if (chance < 98) {
         amount = 9;
      } else {
         amount = 10;
      }

      amount++;

      Customer subCustomer;
      do {
         subCustomer = this.spawnCustomer(-10);
      } while (subCustomer instanceof CustomerGroup);

      customerGroup.getCustomers().add(subCustomer);

      for (int i = 0; i < amount - 1; i++) {
         Customer customer = this.createCustomer(subCustomer.getType());
         customer.setGender(subCustomer.getGender());
         customerGroup.getCustomers().add(customer);
      }
   }

   public void reset() {
      this.spawnChanceModificatorMap.clear();
      this.spawnAmountModificatorMap.clear();
      this.bonusCustomers = 0;
      this.modCustomerAmount = 0.0F;
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTSHIFT) {
         this.reset();
      }
   }

   public void addBonusCustomers(int amount) {
      this.bonusCustomers += amount;
   }

   public Map<Gender, Integer> getGenderSpawnMap() {
      return this.genderSpawnMap;
   }

   public void addToModCustomerAmount(float modAmount) {
      this.modCustomerAmount += modAmount;
   }

   public static class CustomerData {
      private CustomerType customerType;
      private Integer value;

      public CustomerData() {
      }

      public CustomerData(CustomerType customerType, Integer value) {
         this.customerType = customerType;
         this.value = value;
      }

      public CustomerType getCustomerType() {
         return this.customerType;
      }

      public void setCustomerType(CustomerType customerType) {
         this.customerType = customerType;
      }

      public Integer getValue() {
         return this.value;
      }

      public void setValue(Integer value) {
         this.value = value;
      }
   }
}
