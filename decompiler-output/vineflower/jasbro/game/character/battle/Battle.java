package jasbro.game.character.battle;

import jasbro.Util;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Battle {
   private List<Unit> sideA = new ArrayList<>();
   private List<Unit> sideB = new ArrayList<>();
   private String combatText = "";
   private int round = 1;
   private List<Unit> order = new ArrayList<>();
   private Attack attack;
   private List<Unit> attackers = new ArrayList<>();
   private List<Unit> targets = new ArrayList<>();
   private List<Defender> targetData = new ArrayList<>();

   public Battle() {
   }

   public Battle(Unit combatant1, Unit combatant2) {
      this.sideA.add(combatant1);
      this.sideB.add(combatant2);
   }

   public void doRound() {
      List<Unit> allCombatantsUnordered = new ArrayList<>();
      allCombatantsUnordered.addAll(this.sideA);
      allCombatantsUnordered.addAll(this.sideB);
      this.order = new ArrayList<>();
      int i = 0;

      do {
         if (allCombatantsUnordered.size() == 1) {
            this.order.add(allCombatantsUnordered.get(0));
            allCombatantsUnordered.clear();
         } else {
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
                  break;
               }

               sum += unit.getSpeed();
            }
         }
      } while (allCombatantsUnordered.size() > 0 && ++i < 20);

      this.order.addAll(allCombatantsUnordered);

      for (int j = 0; j < this.order.size(); j++) {
         Unit unit = this.order.get(j);
         if (unit.getHitpoints() > 0 && !this.isOver()) {
            this.attackers.add(unit);
            if (this.sideA.contains(unit)) {
               while (true) {
                  Unit target = this.sideB.get(Util.getInt(0, this.sideB.size()));
                  if (target.getHitpoints() >= 1) {
                     this.targets.add(target);
                     this.targetData.add(new Defender(target));
                     break;
                  }
               }
            } else {
               Unit target;
               do {
                  target = this.sideA.get(Util.getInt(0, this.sideA.size()));
               } while (target.getHitpoints() < 1);

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
               for (Defender target : this.targetData) {
                  Object[] arguments = new Object[]{unit.getHitpoints()};
                  this.addToCombatText(TextUtil.firstCharUpper(TextUtil.t(this.attack.getAttackMessageKey(), unit, target.getUnit(), arguments) + " "));
                  if (Util.getInt(0, 100) < 100 + this.attack.getHit() - target.getDodge()) {
                     boolean crit = false;
                     boolean block = false;
                     if (this.attack.isCanCrit() && Util.getInt(0, 100) < this.attack.getCritChance()) {
                        crit = true;
                     }

                     if (this.attack.isBlockable() && Util.getInt(0, 100) < target.getBlockChance()) {
                        block = true;
                     }

                     if (crit == block) {
                        crit = false;
                        block = false;
                        this.attack.setCrit(false);
                        event = new MyEvent(EventType.ATTACKHIT, this);
                     } else if (!crit && block) {
                        this.attack.setCrit(false);
                        target.setBlockSuccessful(true);
                        event = new MyEvent(EventType.ATTACKBLOCK, this);
                     } else {
                        this.attack.setCrit(true);
                        event = new MyEvent(EventType.ATTACKCRIT, this);
                     }

                     for (Unit curUnit : this.attackers) {
                        curUnit.handleEvent(event);
                     }

                     target.getUnit().handleEvent(event);
                     float damage = target.takeAttack(this.attack);
                     this.attack.attackHits(target, this);
                     Object[] arguments2 = new Object[]{Math.round(Math.abs(damage) * 100.0F) / 100.0F, target.getHitpoints()};
                     if (crit == block) {
                        this.addToCombatText(TextUtil.firstCharUpper(TextUtil.t(this.attack.getHitMessageKey(), unit, target.getUnit(), arguments2)));
                     } else if (!crit && block) {
                        this.addToCombatText(TextUtil.t("fight.combatText.block", unit, target.getUnit(), arguments2));
                     } else {
                        this.addToCombatText(TextUtil.t("fight.combatText.crit", unit, target.getUnit(), arguments2));
                     }
                  } else if (this.attack.getHit() > -100) {
                     event = new MyEvent(EventType.ATTACKMISS, this);

                     for (Unit curUnit : this.attackers) {
                        curUnit.handleEvent(event);
                     }

                     target.getUnit().handleEvent(event);
                     this.addToCombatText(TextUtil.t("fight.combatText.miss", unit, target.getUnit(), arguments));
                  }
               }
            }

            this.addToCombatText("\n");
            this.targets.clear();
            this.targetData.clear();
            this.attackers.clear();
            this.attack = null;
         }
      }

      this.round++;
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
      } else {
         return this.sideB.contains(unit) ? this.sideA : null;
      }
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
      if (this.combatText.length() > 0
         && this.combatText.charAt(this.combatText.length() - 1) != '\n'
         && this.combatText.charAt(this.combatText.length() - 1) != ' ') {
         this.combatText = this.combatText + " ";
      }
   }

   public List<Unit> getOrder() {
      return this.order;
   }

   public boolean isOver() {
      boolean alive = false;

      for (Unit unit : this.sideA) {
         if (unit.getHitpoints() > 0) {
            alive = true;
            break;
         }
      }

      if (!alive) {
         return true;
      }

      for (Unit unit : this.sideB) {
         if (unit.getHitpoints() > 0) {
            return false;
         }
      }

      return true;
   }
}
