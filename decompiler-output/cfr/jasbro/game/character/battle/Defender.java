/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.battle;

import jasbro.game.character.battle.Attack;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.Unit;
import java.util.EnumMap;
import java.util.Map;

public class Defender {
    private Unit unit;
    private int armorPercent;
    private int blockChance;
    private int blockAmount;
    private int dodge;
    private boolean blockSuccessful = false;
    private Map<DamageType, Integer> resistances = new EnumMap<DamageType, Integer>(DamageType.class);

    public Defender(Unit unit) {
        this.unit = unit;
        this.armorPercent = unit.getArmor();
        this.blockChance = unit.getBlockChance();
        this.blockAmount = unit.getBlockAmount();
        this.dodge = unit.getDodge();
        for (DamageType damageType : DamageType.values()) {
            if (damageType == DamageType.REGULAR) continue;
            this.resistances.put(damageType, unit.getResistance(damageType));
        }
    }

    public float takeAttack(Attack attack) {
        float damageSum = 0.0f;
        for (Map.Entry<DamageType, Float> damageData : attack.getDamageMap().entrySet()) {
            float damage = damageData.getValue().floatValue();
            if (attack.isCrit()) {
                damage += damage * (float)attack.getCritBonus() / 100.0f;
            }
            if (damageData.getKey() == DamageType.REGULAR) {
                if (this.isBlockSuccessful()) {
                    damage -= damage * (float)this.blockAmount / 100.0f;
                }
                if ((damage -= damage * (float)this.armorPercent / 100.0f) < 0.0f) {
                    damage = 0.0f;
                }
            } else if (damage > 0.0f) {
                int resistance = this.resistances.get((Object)damageData.getKey());
                if (resistance < 200) {
                    if ((damage -= damage * (float)this.resistances.get((Object)damageData.getKey()).intValue() / 100.0f) < 0.0f) {
                        damage = 0.0f;
                    }
                } else {
                    int bonus = resistance - 200;
                    if (bonus > 100) {
                        bonus = 100;
                    }
                    damage = -damage * (float)this.resistances.get((Object)damageData.getKey()).intValue() / 100.0f;
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

