/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.battle;

import jasbro.game.character.Condition;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.DamageType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;

public interface Unit
extends Person {
    public int getHitpoints();

    public int getMaxHitpoints();

    public float modifyHitpoints(float var1);

    public float getDamage();

    public int getArmor();

    public float takeDamage(float var1);

    public int getDodge();

    public int getHit();

    public int getSpeed();

    public int getCritChance();

    public int getCritDamageBonus();

    public int getBlockChance();

    public int getBlockAmount();

    public Attack getAttack(Battle var1);

    public void handleEvent(MyEvent var1);

    public int getResistance(DamageType var1);

    public void addCondition(Condition var1);

    public void removeCondition(Condition var1);
}

