package jasbro.game.character.battle;

import jasbro.game.character.Condition;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.conditions.BattleCondition;
import jasbro.game.events.MyEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Enemy implements Unit {
   private List<BattleCondition> conditions = new ArrayList<>();
   private Map<CalculatedAttribute, Double> attributeMap = new EnumMap<>(CalculatedAttribute.class);
   private int hitpoints = 100;
   private int maxHitpoints = 100;
   private boolean combatInitialized = false;
   public String rapeText;

   public abstract void initCombat();

   private synchronized void initializeCombat() {
      if (!this.combatInitialized) {
         this.combatInitialized = true;
         this.initCombat();
      }
   }

   @Override
   public void handleEvent(MyEvent event) {
      for (Condition condition : new ArrayList<>(this.conditions)) {
         condition.handleEvent(event);
      }
   }

   @Override
   public void addCondition(Condition condition) {
      if (condition instanceof BattleCondition) {
         this.conditions.add((BattleCondition)condition);
      }
   }

   @Override
   public void removeCondition(Condition condition) {
      this.conditions.remove(condition);
   }

   @Override
   public int getDodge() {
      return (int)this.getAttribute(CalculatedAttribute.DODGE);
   }

   @Override
   public int getHit() {
      return (int)this.getAttribute(CalculatedAttribute.HIT);
   }

   @Override
   public int getSpeed() {
      return (int)this.getAttribute(CalculatedAttribute.SPEED);
   }

   @Override
   public int getCritDamageBonus() {
      return (int)this.getAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT);
   }

   @Override
   public int getBlockChance() {
      return (int)this.getAttribute(CalculatedAttribute.BLOCKCHANCE);
   }

   @Override
   public int getBlockAmount() {
      return (int)this.getAttribute(CalculatedAttribute.BLOCKAMOUNT);
   }

   @Override
   public Attack getAttack(Battle battle) {
      return new Attack(this);
   }

   @Override
   public float getDamage() {
      return (float)this.getAttribute(CalculatedAttribute.DAMAGE);
   }

   @Override
   public int getArmor() {
      return (int)this.getAttribute(CalculatedAttribute.ARMORPERCENT);
   }

   @Override
   public int getResistance(DamageType damageType) {
      CalculatedAttribute calculatedAttribute = null;
      switch (damageType) {
         case FIRE:
            calculatedAttribute = CalculatedAttribute.FIRERESISTANCE;
            break;
         case WATER:
            calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
            break;
         case WIND:
            calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
            break;
         case EARTH:
            calculatedAttribute = CalculatedAttribute.EARTHRESISTANCE;
            break;
         case MAGIC:
            calculatedAttribute = CalculatedAttribute.MAGICRESISTANCE;
            break;
         case HOLY:
            calculatedAttribute = CalculatedAttribute.HOLYRESISTANCE;
            break;
         case DARKNESS:
            calculatedAttribute = CalculatedAttribute.DARKNESSRESISTANCE;
      }

      return calculatedAttribute != null ? (int)this.getAttribute(calculatedAttribute) : 0;
   }

   public double getAttribute(CalculatedAttribute calculatedAttribute) {
      this.initializeCombat();
      double value;
      if (this.attributeMap.containsKey(calculatedAttribute)) {
         value = this.attributeMap.get(calculatedAttribute);
      } else {
         value = 0.0;
      }

      for (Condition condition : this.conditions) {
         value = condition.modifyCalculatedAttribute(calculatedAttribute, value, this);
      }

      return value;
   }

   public void setAttribute(CalculatedAttribute attribute, Double value) {
      this.attributeMap.put(attribute, value);
   }

   @Override
   public int getHitpoints() {
      this.initializeCombat();
      return this.hitpoints;
   }

   @Override
   public int getMaxHitpoints() {
      this.initializeCombat();
      return this.maxHitpoints;
   }

   @Override
   public float modifyHitpoints(float modifier) {
      this.hitpoints = (int)(this.hitpoints + modifier);
      return modifier;
   }

   @Override
   public float takeDamage(float power) {
      return this.modifyHitpoints(-power);
   }

   public void setHitpoints(int hitpoints) {
      this.hitpoints = hitpoints;
   }

   public void setMaxHitpoints(int maxHitpoints) {
      this.maxHitpoints = maxHitpoints;
   }

   @Override
   public int getCritChance() {
      return (int)this.getAttribute(CalculatedAttribute.CRITCHANCE);
   }
}
