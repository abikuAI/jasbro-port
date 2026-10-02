/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.battle;

import jasbro.Util;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.battle.Defender;
import jasbro.game.character.battle.Unit;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Battle {
    private List<Unit> sideA = new ArrayList<Unit>();
    private List<Unit> sideB = new ArrayList<Unit>();
    private String combatText = "";
    private int round = 1;
    private List<Unit> order = new ArrayList<Unit>();
    private Attack attack;
    private List<Unit> attackers = new ArrayList<Unit>();
    private List<Unit> targets = new ArrayList<Unit>();
    private List<Defender> targetData = new ArrayList<Defender>();

    public Battle() {
    }

    public Battle(Unit combatant1, Unit combatant2) {
        this.sideA.add(combatant1);
        this.sideB.add(combatant2);
    }

    public void doRound() {
        ArrayList<Unit> allCombatantsUnordered = new ArrayList<Unit>();
        allCombatantsUnordered.addAll(this.sideA);
        allCombatantsUnordered.addAll(this.sideB);
        this.order = new ArrayList<Unit>();
        int i = 0;
        block0: do {
            if (allCombatantsUnordered.size() == 1) {
                this.order.add((Unit)allCombatantsUnordered.get(0));
                allCombatantsUnordered.clear();
                continue;
            }
            int sum = 0;
            for (Unit unit : allCombatantsUnordered) {
                sum += unit.getSpeed();
            }
            int rndInt = Util.getRnd().nextInt(sum);
            sum = 0;
            for (Unit unit : allCombatantsUnordered) {
                if (unit.getSpeed() > rndInt) {
                    this.order.add(unit);
                    allCombatantsUnordered.remove(unit);
                    continue block0;
                }
                sum += unit.getSpeed();
            }
        } while (allCombatantsUnordered.size() > 0 && ++i < 20);
        this.order.addAll(allCombatantsUnordered);
        for (int j = 0; j < this.order.size(); ++j) {
            Unit target;
            Unit unit = this.order.get(j);
            if (unit.getHitpoints() <= 0 || this.isOver()) continue;
            this.attackers.add(unit);
            if (this.sideA.contains(unit)) {
                while ((target = this.sideB.get(Util.getInt(0, this.sideB.size()))).getHitpoints() < 1) {
                }
                this.targets.add(target);
                this.targetData.add(new Defender(target));
            } else {
                while ((target = this.sideA.get(Util.getInt(0, this.sideA.size()))).getHitpoints() < 1) {
                }
                this.targets.add(target);
                this.targetData.add(new Defender(target));
            }
            this.attack = unit.getAttack(this);
            MyEvent event = new MyEvent(EventType.ATTACK, this);
            for (Unit curUnit : this.attackers) {
                curUnit.handleEvent(event);
            }
            for (Unit curUnit : this.targets) {
                curUnit.handleEvent(event);
            }
            if (!this.attack.isAbort()) {
                for (Defender target2 : this.targetData) {
                    Object[] arguments = new Object[]{unit.getHitpoints()};
                    this.addToCombatText(TextUtil.firstCharUpper(TextUtil.t(this.attack.getAttackMessageKey(), (Person)unit, (Person)target2.getUnit(), arguments) + " "));
                    if (Util.getInt(0, 100) < 100 + this.attack.getHit() - target2.getDodge()) {
                        boolean crit = false;
                        boolean block = false;
                        if (this.attack.isCanCrit() && Util.getInt(0, 100) < this.attack.getCritChance()) {
                            crit = true;
                        }
                        if (this.attack.isBlockable() && Util.getInt(0, 100) < target2.getBlockChance()) {
                            block = true;
                        }
                        if (crit == block) {
                            crit = false;
                            block = false;
                            this.attack.setCrit(false);
                            event = new MyEvent(EventType.ATTACKHIT, this);
                        } else if (!crit && block) {
                            this.attack.setCrit(false);
                            target2.setBlockSuccessful(true);
                            event = new MyEvent(EventType.ATTACKBLOCK, this);
                        } else {
                            this.attack.setCrit(true);
                            event = new MyEvent(EventType.ATTACKCRIT, this);
                        }
                        for (Unit curUnit : this.attackers) {
                            curUnit.handleEvent(event);
                        }
                        target2.getUnit().handleEvent(event);
                        float damage = target2.takeAttack(this.attack);
                        this.attack.attackHits(target2, this);
                        Object[] arguments2 = new Object[]{Float.valueOf((float)Math.round(Math.abs(damage) * 100.0f) / 100.0f), target2.getHitpoints()};
                        if (crit == block) {
                            this.addToCombatText(TextUtil.firstCharUpper(TextUtil.t(this.attack.getHitMessageKey(), (Person)unit, (Person)target2.getUnit(), arguments2)));
                            continue;
                        }
                        if (!crit && block) {
                            this.addToCombatText(TextUtil.t("fight.combatText.block", (Person)unit, (Person)target2.getUnit(), arguments2));
                            continue;
                        }
                        this.addToCombatText(TextUtil.t("fight.combatText.crit", (Person)unit, (Person)target2.getUnit(), arguments2));
                        continue;
                    }
                    if (this.attack.getHit() <= -100) continue;
                    event = new MyEvent(EventType.ATTACKMISS, this);
                    for (Unit curUnit : this.attackers) {
                        curUnit.handleEvent(event);
                    }
                    target2.getUnit().handleEvent(event);
                    this.addToCombatText(TextUtil.t("fight.combatText.miss", (Person)unit, (Person)target2.getUnit(), arguments));
                }
            }
            this.addToCombatText("\n");
            this.targets.clear();
            this.targetData.clear();
            this.attackers.clear();
            this.attack = null;
        }
        ++this.round;
    }

    public String getCombatText() {
        return this.combatText;
    }

    public int getRound() {
        return this.round;
    }

    public List<Unit> getSideA() {
        return this.sideA;
    }

    public List<Unit> getSideB() {
        return this.sideB;
    }

    public List<Unit> getEnemies(Unit unit) {
        if (this.sideA.contains(unit)) {
            return this.sideB;
        }
        if (this.sideB.contains(unit)) {
            return this.sideA;
        }
        return null;
    }

    public boolean isTarget(Unit unit) {
        return this.targets.contains(unit);
    }

    public boolean isAttacker(Unit unit) {
        return this.attackers.contains(unit);
    }

    public Attack getAttack() {
        return this.attack;
    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    public List<Unit> getAttackers() {
        return this.attackers;
    }

    public List<Defender> getTargetData() {
        return this.targetData;
    }

    public void addToCombatText(String message) {
        this.combatText = this.combatText + message;
        if (this.combatText.length() > 0 && this.combatText.charAt(this.combatText.length() - 1) != '\n' && this.combatText.charAt(this.combatText.length() - 1) != ' ') {
            this.combatText = this.combatText + " ";
        }
    }

    public List<Unit> getOrder() {
        return this.order;
    }

    public boolean isOver() {
        boolean alive = false;
        for (Unit unit : this.sideA) {
            if (unit.getHitpoints() <= 0) continue;
            alive = true;
            break;
        }
        if (!alive) {
            return true;
        }
        for (Unit unit : this.sideB) {
            if (unit.getHitpoints() <= 0) continue;
            return false;
        }
        return true;
    }
}

