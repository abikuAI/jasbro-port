/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.battle;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.Enemy;
import jasbro.game.character.battle.MonsterDickType;

public class SimpleEnemy
extends Enemy {
    private Gender gender;
    private MonsterDickType dick;
    private DamageType damageType;
    private String name;
    private String femRape;
    private String femSubmit;
    private String reverseFemRape;
    private String maleRape;
    private String maleSubmit;
    private String reverseMaleRape;

    public SimpleEnemy() {
    }

    public SimpleEnemy(String name, Gender gender, int hitpoints, double damage, double armorPercent, double dodge, double hit, double critChance, double critAmount, double blockChance, double blockAmount, double speed) {
        this.name = name;
        this.gender = gender;
        this.setHitpoints(hitpoints);
        this.setAttribute(CalculatedAttribute.DAMAGE, damage);
        this.setAttribute(CalculatedAttribute.BLOCKCHANCE, blockChance);
        this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, blockAmount);
        this.setAttribute(CalculatedAttribute.ARMORPERCENT, armorPercent);
        this.setAttribute(CalculatedAttribute.CRITCHANCE, critChance);
        this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, critAmount);
        this.setAttribute(CalculatedAttribute.DODGE, dodge);
        this.setAttribute(CalculatedAttribute.HIT, hit);
        this.setAttribute(CalculatedAttribute.SPEED, speed);
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MonsterDickType getDick() {
        return this.dick;
    }

    public void setDick(MonsterDickType dick) {
        this.dick = dick;
    }

    public DamageType getElement() {
        return this.damageType;
    }

    public void setElement(DamageType damageType) {
        this.damageType = damageType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFemaleSubmit() {
        return this.femSubmit;
    }

    public void setFemaleSubmit(String femSubmit) {
        this.femSubmit = femSubmit;
    }

    public String getFemaleRape() {
        return this.femRape;
    }

    public void setFemaleRape(String femRape) {
        this.femRape = femRape;
    }

    public String getReverseFemaleRape() {
        return this.femRape;
    }

    public void setReverseFemaleRape(String reverseFemRape) {
        this.reverseFemRape = reverseFemRape;
    }

    public String getMaleSubmit() {
        return this.maleSubmit;
    }

    public void setMaleSubmit(String maleSubmit) {
        this.maleSubmit = maleSubmit;
    }

    public String getMaleRape() {
        return this.maleRape;
    }

    public void setMaleRape(String maleRape) {
        this.maleRape = maleRape;
    }

    public String getReverseMaleRape() {
        return this.reverseMaleRape;
    }

    public void setReverseMaleRape(String reverseMaleRape) {
        this.reverseMaleRape = reverseMaleRape;
    }

    @Override
    public void initCombat() {
    }
}

