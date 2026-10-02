/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.battle;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.Defender;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.world.Time;
import java.util.EnumMap;
import java.util.Map;

public class Attack {
    private Map<DamageType, Float> damageMap = new EnumMap<DamageType, Float>(DamageType.class);
    private boolean dodgeable = true;
    private boolean blockable = true;
    private boolean canCrit = true;
    private boolean isCrit = false;
    private int hit;
    private int critChance;
    private int critBonus;
    private int selectionModifier = 10;
    private boolean abort = false;
    private String attackMessageKey = "fight.combatText";
    private String hitMessageKey = "fight.combatText.hit";

    public Attack() {
    }

    public Attack(DamageType damageType, Float damage, boolean dodgeable, boolean blockable, int hit, int critChance, int critBonus) {
        this.damageMap.put(damageType, damage);
        this.dodgeable = dodgeable;
        this.blockable = blockable;
        this.hit = hit;
        this.critChance = critChance;
        this.critBonus = critBonus;
    }

    public Attack(Unit unit) {
        this.damageMap.put(DamageType.REGULAR, Float.valueOf(unit.getDamage()));
        this.hit = unit.getHit();
        this.critChance = unit.getCritChance();
        this.critBonus = unit.getCritDamageBonus();
    }

    public void addDamageModifier(DamageType damageType, Float damage) {
        if (this.damageMap.containsKey((Object)damageType)) {
            this.damageMap.put(damageType, Float.valueOf(this.damageMap.get((Object)damageType).floatValue() + damage.floatValue()));
        } else {
            this.damageMap.put(damageType, damage);
        }
    }

    public String getText() {
        return null;
    }

    public void attackHits(Defender target, Battle battle) {
    }

    public boolean isDodgeable() {
        return this.dodgeable;
    }

    public void setDodgeable(boolean dodgeable) {
        this.dodgeable = dodgeable;
    }

    public boolean isBlockable() {
        return this.blockable;
    }

    public void setBlockable(boolean blockable) {
        this.blockable = blockable;
    }

    public int getHit() {
        return this.hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getCritChance() {
        return this.critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public int getCritBonus() {
        return this.critBonus;
    }

    public void setCritBonus(int critBonus) {
        this.critBonus = critBonus;
    }

    public int getSelectionModifier() {
        return this.selectionModifier;
    }

    public void setSelectionModifier(int selectionModifier) {
        this.selectionModifier = selectionModifier;
    }

    public boolean isCanCrit() {
        return this.canCrit;
    }

    public void setCanCrit(boolean canCrit) {
        this.canCrit = canCrit;
    }

    public boolean isCrit() {
        return this.isCrit;
    }

    public void setCrit(boolean isCrit) {
        this.isCrit = isCrit;
    }

    public boolean isAbort() {
        return this.abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public Map<DamageType, Float> getDamageMap() {
        return this.damageMap;
    }

    public String getAttackMessageKey() {
        return this.attackMessageKey;
    }

    public void setAttackMessageKey(String attackMessageKey) {
        this.attackMessageKey = attackMessageKey;
    }

    public String getHitMessageKey() {
        return this.hitMessageKey;
    }

    public void setHitMessageKey(String hitMessageKey) {
        this.hitMessageKey = hitMessageKey;
    }

    public static class ShadowSlicer
    extends Attack {
        public ShadowSlicer(Charakter character) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(2.0f + character.getDamage() * (100.0f + (float)character.getFinalValue(SpecializationAttribute.AGILITY)) / 100.0f));
            this.setAttackMessageKey("SHADOWSLICER.attack");
            this.setCritChance(100);
        }
    }

    public static class GracefulKick
    extends Attack {
        public GracefulKick(Charakter character) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(1.0f + (float)character.getFinalValue(SpecializationAttribute.STRIP) / 8.0f));
            this.setHit(character.getFinalValue(SpecializationAttribute.STRIP));
            this.setCritBonus(character.getFinalValue(SpecializationAttribute.STRIP) / 10);
            this.setAttackMessageKey("GRACEFULKICKS.attack");
            this.setDodgeable(false);
        }
    }

    public static class Midnight
    extends Attack {
        public Midnight(Charakter character) {
            int bonus = 0;
            if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
                bonus = 13;
            }
            this.getDamageMap().put(DamageType.DARKNESS, Float.valueOf((float)bonus + (float)character.getFinalValue(SpecializationAttribute.MAGIC) / 6.0f));
            this.setDodgeable(false);
            this.setAttackMessageKey("MIDNIGHT.attack");
        }
    }

    public static class StarlightBreaker
    extends Attack {
        public StarlightBreaker(Charakter character) {
            int bonus = 0;
            if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
                bonus = 12;
            }
            this.getDamageMap().put(DamageType.HOLY, Float.valueOf((float)bonus + (float)character.getFinalValue(SpecializationAttribute.MAGIC) / 6.0f));
            this.setAttackMessageKey("STARLIGHTBREAKER.attack");
        }
    }

    public static class RollingThunder
    extends Attack {
        public RollingThunder(Charakter character) {
            this.getDamageMap().put(DamageType.LIGHTNING, Float.valueOf(6.0f + (float)character.getFinalValue(SpecializationAttribute.MAGIC) / 7.0f));
            this.setHit(250);
            this.setCanCrit(false);
            this.setBlockable(false);
            this.setAttackMessageKey("ROLLINGTHUNDER.attack");
        }
    }

    public static class IceStingers
    extends Attack {
        public IceStingers(Charakter character) {
            this.getDamageMap().put(DamageType.WATER, Float.valueOf(7.0f + (float)character.getFinalValue(SpecializationAttribute.MAGIC) / 10.0f));
            this.setCritChance(character.getCritChance() + 20);
            this.setCritBonus(300);
            this.setAttackMessageKey("ICESTINGERS.attack");
        }
    }

    public static class FireWave
    extends Attack {
        public FireWave(Charakter character) {
            this.getDamageMap().put(DamageType.FIRE, Float.valueOf(5.0f + (float)character.getFinalValue(SpecializationAttribute.MAGIC) / 10.0f));
            this.setAttackMessageKey("FIREWAVE.attack");
            this.setHit(character.getHit() + 10);
            this.setCanCrit(false);
        }
    }

    public static class EtherStrike
    extends Attack {
        public EtherStrike(Charakter character) {
            this.getDamageMap().put(DamageType.MAGIC, Float.valueOf((float)character.getFinalValue(SpecializationAttribute.MAGIC) / 10.0f));
            this.setAttackMessageKey("ETHERSTRIKE.attack");
        }
    }

    public static class ButtSmash
    extends Attack {
        public ButtSmash(Charakter character) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf((character.getFinalValue(SpecializationAttribute.SEDUCTION) + character.getFinalValue(SpecializationAttribute.SEDUCTION)) / 10));
            this.setCanCrit(false);
            this.setBlockable(false);
            this.setAttackMessageKey("BUTTSMASH.attack");
        }
    }

    public static class PreciseStrike
    extends Attack {
        public PreciseStrike(Charakter character) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(character.getDamage() * 0.7f));
            this.setCritChance(character.getCritChance() + 50);
            this.setCritBonus(200);
            this.setAttackMessageKey("PRECISESTRIKE.attack");
        }
    }

    public static class SwiftStrike
    extends Attack {
        public SwiftStrike(Charakter character) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(2.0f + (float)(character.getFinalValue(SpecializationAttribute.AGILITY) / 8)));
            this.setHit(60 + character.getFinalValue(SpecializationAttribute.AGILITY) / 2);
            this.setBlockable(false);
            this.setAttackMessageKey("SWIFTSTRIKE.attack");
        }
    }

    public static class StrongStrike
    extends Attack {
        public StrongStrike(Charakter character) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(character.getDamage() * 1.5f));
            this.setAttackMessageKey("STRONGSTRIKE.attack");
        }
    }

    public static class Heal2
    extends Attack {
        public Heal2(Unit unit) {
            super(unit);
            this.getDamageMap().put(DamageType.HOLY, Float.valueOf(0.0f));
            this.setHit(-1000);
            this.setSelectionModifier(2);
            unit.modifyHitpoints(10.0f);
            this.setAttackMessageKey("HEAL.attack");
        }
    }

    public static class ClawAttack
    extends Attack {
        public ClawAttack(Unit unit) {
            super(unit);
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(this.getDamageMap().get((Object)DamageType.REGULAR).floatValue() * 1.5f));
            this.setAttackMessageKey("EXTRACTABLECLAWS.attack");
        }
    }

    public static class Heal
    extends Attack {
        public Heal(Charakter character) {
            this.getDamageMap().put(DamageType.HOLY, Float.valueOf(0.0f));
            this.setHit(-1000);
            this.setSelectionModifier(2);
            character.modifyHitpoints(5.0f + (float)(character.getFinalValue(SpecializationAttribute.MAGIC) / 10));
            this.setAttackMessageKey("HEAL.attack");
        }
    }

    public static class FlameBreath
    extends Attack {
        public FlameBreath(Charakter character) {
            float damage = 5.0f + (float)character.getStrength() / 15.0f + (float)character.getStamina() / 15.0f;
            this.getDamageMap().put(DamageType.FIRE, Float.valueOf(damage));
            this.setHit(character.getHit() + 10);
            this.setCrit(false);
            this.setSelectionModifier(2);
            this.setAttackMessageKey("FLAMEBREATH.attack");
        }
    }

    public static class Thunderbolt
    extends Attack {
        public Thunderbolt(Unit unit) {
            this.getDamageMap().put(DamageType.LIGHTNING, Float.valueOf(unit.getDamage() * 2.7f));
            this.setHit(250);
            this.setCanCrit(false);
            this.setBlockable(false);
            this.setAttackMessageKey("ROLLINGTHUNDER.attack");
        }
    }

    public static class Firebolt
    extends Attack {
        public Firebolt(Unit unit) {
            this.getDamageMap().put(DamageType.FIRE, Float.valueOf(unit.getDamage() * 2.5f));
            this.setHit(unit.getHit() + 10);
            this.setCanCrit(false);
            this.setAttackMessageKey("FIREWAVE.attack");
        }
    }

    public static class LowKick
    extends Attack {
        public LowKick(Unit unit) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(unit.getDamage() * 1.5f));
            this.setBlockable(false);
            this.setAttackMessageKey("SWIFTSTRIKE.attack");
        }
    }

    public static class CleanHit
    extends Attack {
        public CleanHit(Unit unit) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(unit.getDamage() * 1.5f));
            this.setCritChance(unit.getCritChance() + 50);
            this.setCritBonus(200);
            this.setAttackMessageKey("PRECISESTRIKE.attack");
        }
    }

    public static class Uppercut
    extends Attack {
        public Uppercut(Unit unit) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(unit.getDamage() * 2.5f));
            this.setAttackMessageKey("STRONGSTRIKE.attack");
        }
    }

    public static class MightyStrike
    extends Attack {
        public MightyStrike(Unit unit) {
            this.getDamageMap().put(DamageType.REGULAR, Float.valueOf(unit.getDamage() * 1.5f));
            this.setAttackMessageKey("STRONGSTRIKE.attack");
        }
    }

    public static class StandardAttack
    extends Attack {
        public StandardAttack() {
        }

        public StandardAttack(DamageType damageType, Float damage, boolean dodgeable, boolean blockable, int hit, int critChance, int critBonus) {
            super(damageType, damage, dodgeable, blockable, hit, critChance, critBonus);
        }

        public StandardAttack(Unit unit) {
            super(unit);
        }
    }
}

