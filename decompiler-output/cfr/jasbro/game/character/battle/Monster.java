/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.battle;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.battle.Enemy;

public class Monster
extends Enemy {
    private MonsterType monsterType;

    public Monster(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public String getName() {
        return this.monsterType.toString();
    }

    @Override
    public void initCombat() {
        if (this.monsterType == MonsterType.DEMONFOX) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 30.0);
            this.setAttribute(CalculatedAttribute.HIT, 5.0);
        }
        if (this.monsterType == MonsterType.WEREWOLF) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 10.0);
            this.setAttribute(CalculatedAttribute.HIT, 5.0);
        }
        if (this.monsterType == MonsterType.MUDWALKER) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 5.0);
            this.setAttribute(CalculatedAttribute.HIT, 5.0);
        }
        if (this.monsterType == MonsterType.DRAGON) {
            this.setHitpoints(300);
            this.setAttribute(CalculatedAttribute.DAMAGE, 3.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 35.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 30.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.CENTAUR) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 3.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 5.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 10.0);
            this.setAttribute(CalculatedAttribute.HIT, 2.0);
        }
        if (this.monsterType == MonsterType.NAGA) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.SPRIGGAN) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.GARGOYLE) {
            this.setHitpoints(400);
            this.setAttribute(CalculatedAttribute.DAMAGE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 35.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 5.0);
            this.setAttribute(CalculatedAttribute.HIT, 5.0);
        }
        if (this.monsterType == MonsterType.NINETAILS) {
            this.setHitpoints(300);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 15.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 20.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 40.0);
            this.setAttribute(CalculatedAttribute.DODGE, 45.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.ALPHAWEREWOLF) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.SWAMPWALKER) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.ELDERDRAGON) {
            this.setHitpoints(500);
            this.setAttribute(CalculatedAttribute.DAMAGE, 6.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 40.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 30.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.DODGE, 5.0);
            this.setAttribute(CalculatedAttribute.HIT, 5.0);
        }
        if (this.monsterType == MonsterType.SAGITARIUS) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.NAGAELITE) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.SPRIGGANQUEEN) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        if (this.monsterType == MonsterType.GRANITGARGOYLE) {
            this.setHitpoints(150);
            this.setAttribute(CalculatedAttribute.DAMAGE, 4.0);
            this.setAttribute(CalculatedAttribute.BLOCKCHANCE, 5.0);
            this.setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50.0);
            this.setAttribute(CalculatedAttribute.ARMORPERCENT, 10.0);
            this.setAttribute(CalculatedAttribute.CRITCHANCE, 10.0);
            this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
            this.setAttribute(CalculatedAttribute.DODGE, 0.0);
            this.setAttribute(CalculatedAttribute.HIT, 0.0);
        }
        this.setMaxHitpoints(this.getHitpoints());
    }

    public static enum MonsterType {
        DEMONFOX,
        WEREWOLF,
        MUDWALKER,
        DRAGON,
        CENTAUR,
        NAGA,
        SPRIGGAN,
        GARGOYLE,
        NINETAILS,
        ALPHAWEREWOLF,
        SWAMPWALKER,
        ELDERDRAGON,
        SAGITARIUS,
        NAGAELITE,
        SPRIGGANQUEEN,
        GRANITGARGOYLE;

    }
}

