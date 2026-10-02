package jasbro.game.events.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Enemy;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Enemy implements Person {
   private CustomerStatus status;
   private CustomerType type;
   private int initialMoney;
   private int money;
   private int satisfactionAmount;
   private Sextype preferredSextype;
   private Gender gender = Gender.MALE;
   private float importance;
   private int initialSatisfaction;
   private List<Customer.SatisfactionModifier> satisfactionModifiers = new ArrayList<>();
   private float payModifier = 1.0F;

   public Customer() {
   }

   public Customer(CustomerType type, int money, int startingSatisfaction, float importance) {
      this.type = type;
      this.initialMoney = money;
      this.money = money;
      this.satisfactionAmount = startingSatisfaction;
      this.initialSatisfaction = startingSatisfaction;
      this.importance = importance;
   }

   @Override
   public void initCombat() {
      int customerJob = Util.getInt(1, 7);
      double bonusDamage = 0.0;
      double bonusCritChance = 0.0;
      double bonusHit = 0.0;
      int bonusHealth = 0;
      double bonusDodge = 0.0;
      double bonusCritDmg = 0.0;
      double bonusBlockChance = 0.0;
      double bonusBlockAmount = 0.0;
      switch (customerJob) {
         case 1:
            bonusHealth = 15;
            bonusDamage = 1.0;
            bonusBlockAmount = 15.0;
            break;
         case 2:
            bonusHealth = -20;
            bonusDamage = 1.0;
            bonusDodge = -3.0;
            break;
         case 3:
            bonusHealth = 40;
            bonusDamage = 2.0;
            bonusHit = -10.0;
            break;
         case 4:
            bonusDodge = 10.0;
            bonusCritChance = 10.0;
            bonusCritDmg = 10.0;
            bonusHealth = -10;
            break;
         case 5:
            bonusHit = 15.0;
            bonusDamage = 1.0;
            bonusBlockChance = 10.0;
            bonusBlockAmount = 10.0;
            break;
         case 6:
            bonusHealth = -15;
            bonusCritChance = 10.0;
            bonusCritDmg = 30.0;
            break;
         case 7:
            bonusHealth = -50;
            bonusDamage = 5.0;
            bonusDodge = -10.0;
      }

      if (this.type == CustomerType.BUM) {
         this.setHitpoints(70 + bonusHealth);
      } else {
         this.setHitpoints(100 + bonusHealth);
      }

      if (this.type == CustomerType.SOLDIER) {
         this.setAttribute(CalculatedAttribute.DAMAGE, 3.0 + bonusDamage);
      } else if (this.type == CustomerType.CELEBRITY) {
         this.setAttribute(CalculatedAttribute.DAMAGE, 1.0 + bonusDamage);
      } else {
         this.setAttribute(CalculatedAttribute.DAMAGE, 0.4 + bonusDamage + (int)(this.getImportance() / 5.0F));
      }

      int amount = 10 + (int)(this.getImportance() * 60.0F);
      if (this.type == CustomerType.SOLDIER) {
         amount = 500;
      } else if (this.type == CustomerType.CELEBRITY) {
         amount = 200;
      }

      this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 3.0 + bonusBlockAmount);
      double value = 0.3;
      double mitigation = 4.0;

      do {
         mitigation += value;
         amount--;
         value /= 1.005F;
      } while (amount > 0);

      this.setAttribute(CalculatedAttribute.ARMORPERCENT, mitigation);
      if (this.getType() == CustomerType.SOLDIER) {
         this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0 + bonusCritChance);
         this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 13.0 + bonusCritDmg);
         this.setAttribute(CalculatedAttribute.DODGE, 10.0 + bonusDodge);
         this.setAttribute(CalculatedAttribute.HIT, 5.0 + bonusHit);
         this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 30.0 + bonusBlockChance);
      } else if (this.getType() == CustomerType.CELEBRITY) {
         this.setAttribute(CalculatedAttribute.CRITCHANCE, 1.0 + bonusCritChance);
         this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 10.0 + bonusCritDmg);
         this.setAttribute(CalculatedAttribute.DODGE, 5.0 + bonusDodge);
      } else {
         this.setAttribute(CalculatedAttribute.CRITCHANCE, this.getImportance() + bonusCritChance);
         this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 10.0 + bonusCritDmg);
         this.setAttribute(CalculatedAttribute.DODGE, this.getImportance() + bonusDodge);
      }

      this.setMaxHitpoints(this.getHitpoints());
   }

   @Override
   public Attack getAttack(Battle battle) {
      List<Attack> attacks = this.getPossibleAttacks(battle);
      int sum = 0;

      for (Attack attack : attacks) {
         sum += attack.getSelectionModifier();
      }

      int rnd = Util.getInt(0, sum);
      sum = 0;

      for (Attack attack : attacks) {
         sum += attack.getSelectionModifier();
         if (sum >= rnd) {
            return attack;
         }
      }

      return attacks.get(0);
   }

   public List<Attack> getPossibleAttacks(Battle battle) {
      List<Attack> attacks = new ArrayList<>();
      attacks.add(new Attack.StandardAttack(this));
      if (Util.getInt(0, 100) < 50) {
         attacks.add(new Attack.MightyStrike(this));
      }

      if (Util.getInt(0, 100) < 60) {
         attacks.add(new Attack.CleanHit(this));
      }

      if (Util.getInt(0, 100) < 30) {
         attacks.add(new Attack.Firebolt(this));
      }

      if (Util.getInt(0, 100) < 30) {
         attacks.add(new Attack.Thunderbolt(this));
      }

      if (Util.getInt(0, 100) < 60) {
         attacks.add(new Attack.Uppercut(this));
      }

      if (Util.getInt(0, 100) < 60) {
         attacks.add(new Attack.LowKick(this));
      }

      if (Util.getInt(0, 100) < 60) {
         attacks.add(new Attack.Heal2(this));
      }

      if (attacks.size() == 0) {
         attacks.add(new Attack.StandardAttack(this));
      }

      return attacks;
   }

   @Override
   public String getName() {
      return this.type.getText(this);
   }

   public String getStatusName() {
      return this.status.getText(this);
   }

   public void setStatus(CustomerStatus status) {
      this.status = status;
   }

   public CustomerStatus getStatus() {
      return this.status;
   }

   public int getMoney() {
      return this.money;
   }

   public int getSatisfactionAmount() {
      return this.satisfactionAmount;
   }

   public Sextype getPreferredSextype() {
      return this.preferredSextype;
   }

   public void setPreferredSextype(Sextype preferredSextype) {
      this.preferredSextype = preferredSextype;
   }

   @Override
   public Gender getGender() {
      return this.gender;
   }

   public void setGender(Gender gender) {
      this.gender = gender;
   }

   public CustomerType getType() {
      return this.type;
   }

   public void setType(CustomerType type) {
      this.type = type;
   }

   public int getMaxSecondaryActivities() {
      return this.type.getMaxSecondaryActivities();
   }

   public float getImportance() {
      return this.importance;
   }

   public void setImportance(float importance) {
      this.importance = importance;
   }

   public Satisfaction getSatisfaction() {
      return Satisfaction.getSatisfaction(this.getSatisfactionAmount());
   }

   public int getInitialSatisfaction() {
      return this.initialSatisfaction;
   }

   public void addToSatisfaction(int mod, Object satisfactionModifier) {
      this.satisfactionAmount += mod;
      this.satisfactionModifiers.add(new Customer.SatisfactionModifier(mod, satisfactionModifier));
   }

   public int payFixed(int amount) {
      if (amount > this.getMoney()) {
         amount = this.getMoney();
      }

      if (amount < 0) {
         amount = 0;
      }

      this.money -= amount;
      return amount;
   }

   public int pay(int amount, float modifier) {
      amount = (int)(amount * modifier * this.payModifier);
      if (amount > this.getMoney()) {
         amount = this.getMoney();
      }

      if (amount < 0) {
         amount = 0;
      }

      this.money -= amount;
      return amount;
   }

   public int pay(float modifier) {
      int payment = 0;
      payment = this.getInitialMoney() / 10 + 50;
      payment = (int)Util.getPercent(payment, this.getSatisfaction().getMoneyModifierPercent());
      return this.pay(payment, modifier);
   }

   public List<Customer.SatisfactionModifier> getSatisfactionModifiers() {
      return this.satisfactionModifiers;
   }

   public void changePayModifier(float modifier) {
      this.payModifier += modifier;
   }

   public void setInitialMoney(int initialMoney) {
      this.initialMoney = initialMoney;
      this.money = initialMoney;
   }

   public int getInitialMoney() {
      return this.initialMoney;
   }

   public List<Inventory.ItemData> spawnItems() {
      List<Inventory.ItemData> items = new ArrayList<>();
      items.add(this.getItem());
      List<Inventory.ItemData> normalized = Util.getItemListNormalized(items);

      for (int i = 0; i < normalized.size(); i++) {
         if (normalized.get(i) == null) {
            normalized.remove(i);
            i--;
         }
      }

      return normalized;
   }

   public Inventory.ItemData getItem() {
      List<Inventory.ItemData> items = new ArrayList<>();
      List<ItemSpawnData> spawnChances = new ArrayList<>();
      int sum = 0;

      for (Item item : Jasbro.getInstance().getAvailableItemsByLocation(ItemLocation.valueOf(this.getType().toString()))) {
         for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
            if (itemSpawnData.getItemLocation() == ItemLocation.valueOf(this.getType().toString())) {
               Inventory.ItemData itemData = new Inventory.ItemData(item, Util.getInt(itemSpawnData.getMinAmount(), itemSpawnData.getMaxAmount() + 1));
               if (itemData.getAmount() > 0) {
                  items.add(itemData);
                  spawnChances.add(itemSpawnData);
                  sum += itemSpawnData.getChance();
               }
            }
         }
      }

      if (items.size() > 0) {
         int selected = Util.getInt(0, sum);
         int curValue = 0;

         for (int i = 0; i < items.size(); i++) {
            curValue += spawnChances.get(i).getChance();
            if (curValue >= selected) {
               return items.get(i);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public class SatisfactionModifier {
      private int modifiedBy;
      private Object source;

      public SatisfactionModifier() {
      }

      public SatisfactionModifier(int modidifyBy, Object source) {
         this.modifiedBy = modidifyBy;
         this.source = source;
      }

      public int getModifiedBy() {
         return this.modifiedBy;
      }

      public void setModifiedBy(int modidifyBy) {
         this.modifiedBy = modidifyBy;
      }

      public Object getSource() {
         return this.source;
      }

      public void setSource(Object source) {
         this.source = source;
      }
   }
}
