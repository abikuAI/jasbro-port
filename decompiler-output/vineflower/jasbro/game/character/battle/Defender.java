package jasbro.game.character.battle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public class Defender {
   private Unit unit;
   private int armorPercent;
   private int blockChance;
   private int blockAmount;
   private int dodge;
   private boolean blockSuccessful = false;
   private Map<DamageType, Integer> resistances = new EnumMap<>(DamageType.class);

   public Defender(Unit unit) {
      this.unit = unit;
      this.armorPercent = unit.getArmor();
      this.blockChance = unit.getBlockChance();
      this.blockAmount = unit.getBlockAmount();
      this.dodge = unit.getDodge();

      for (DamageType damageType : DamageType.values()) {
         if (damageType != DamageType.REGULAR) {
            this.resistances.put(damageType, unit.getResistance(damageType));
         }
      }
   }

   public float takeAttack(Attack attack) {
      float damageSum = 0.0F;

      for (Entry<DamageType, Float> damageData : attack.getDamageMap().entrySet()) {
         float damage = damageData.getValue();
         if (attack.isCrit()) {
            damage += damage * attack.getCritBonus() / 100.0F;
         }

         if (damageData.getKey() == DamageType.REGULAR) {
            if (this.isBlockSuccessful()) {
               damage -= damage * this.blockAmount / 100.0F;
            }

            damage -= damage * this.armorPercent / 100.0F;
            if (damage < 0.0F) {
               damage = 0.0F;
            }
         } else if (damage > 0.0F) {
            int resistance = this.resistances.get(damageData.getKey());
            if (resistance < 200) {
               damage -= damage * this.resistances.get(damageData.getKey()).intValue() / 100.0F;
               if (damage < 0.0F) {
                  damage = 0.0F;
               }
            } else {
               int bonus = resistance - 200;
               if (bonus > 100) {
                  int var8 = 100;
               }

               damage = -damage * this.resistances.get(damageData.getKey()).intValue() / 100.0F;
            }
         }

         damageSum += damage;
      }

      this.unit.takeDamage(damageSum);
      return damageSum;
   }

   public int getBlockChance() {
      return this.blockChance;
   }

   public void setBlockChance(int blockChance) {
      this.blockChance = blockChance;
   }

   public int getBlockAmount() {
      return this.blockAmount;
   }

   public void setBlockAmount(int blockAmount) {
      this.blockAmount = blockAmount;
   }

   public int getDodge() {
      return this.dodge;
   }

   public void setDodge(int dodgeChance) {
      this.dodge = dodgeChance;
   }

   public Unit getUnit() {
      return this.unit;
   }

   public int getArmorPercent() {
      return this.armorPercent;
   }

   public void setArmorPercent(int armorPercent) {
      this.armorPercent = armorPercent;
   }

   public boolean isBlockSuccessful() {
      return this.blockSuccessful;
   }

   public void setBlockSuccessful(boolean blockSuccessful) {
      this.blockSuccessful = blockSuccessful;
   }

   public int getHitpoints() {
      return this.unit.getHitpoints();
   }

   public Map<DamageType, Integer> getResistances() {
      return this.resistances;
   }
}
