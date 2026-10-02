package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.BathAttendant;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.conditions.BattleCondition;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.Illness;
import jasbro.game.character.conditions.OvipositionPregnancy;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Perks;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.items.AccessoryType;
import jasbro.game.items.Equipment;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class FurryPerks {
   static Buff h1 = new Buff.Heat1();
   static Buff h2 = new Buff.Heat2();
   static Buff h3 = new Buff.Heat3();

   public static class Aquatic extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("AQUATICSTAGE" + this.stage);
         switch (this.stage) {
            case 4:
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.RELENTLESSBEAST);
            case 3:
               Perks.PerkUtil.addMaybe(character, Trait.AQUATICNURSE);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.AQUATICSWIM);
            case 1:
            default:
               Perks.PerkUtil.addMaybe(character, Trait.AQUATICDOWNSIDE);
         }
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.AQUATICSWIM);
         Perks.PerkUtil.removeMaybe(character, Trait.AQUATICDOWNSIDE);
         Perks.PerkUtil.removeMaybe(character, Trait.AQUATICNURSE);
         Perks.PerkUtil.removeMaybe(character, Trait.RELENTLESSBEAST);
         return super.removeTrait(character);
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         float multiplier = this.stage * 0.05F;
         if (attribute.getAttributeType() == SpecializationAttribute.MEDICALKNOWLEDGE) {
            return value * multiplier;
         } else {
            return attribute.getAttributeType() == SpecializationAttribute.MAGIC ? value * multiplier : 0.0F;
         }
      }
   }

   public static class AquaticDownside extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         boolean dosomething = false;
         boolean dry = true;
         if (e.getType() == EventType.NEXTDAY) {
            dosomething = true;
            dry = true;
         } else if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.SWIM
               || activity.getType() == ActivityType.SOAK
               || activity.getType() == ActivityType.BATHATTENDANT
               || activity.getType() == ActivityType.BATHE) {
               dosomething = true;
               dry = false;
            }
         }

         if (dosomething) {
            boolean found = false;

            for (Condition con : character.getConditions()) {
               if (con instanceof Buff.AquaticTrait) {
                  int stage = ((Buff.AquaticTrait)con).getStage();
                  if (dry) {
                     if (stage > 1) {
                        stage--;
                     }
                  } else if (stage < 5) {
                     stage++;
                  }

                  character.removeCondition(con);
                  character.addCondition(new Buff.AquaticTrait(character, stage));
                  found = true;
                  break;
               }
            }

            if (!found) {
               character.addCondition(new Buff.AquaticTrait(character, 3));
            }
         }
      }
   }

   public static class AquaticNurse extends TraitEffect {
   }

   public static class AquaticSwim extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Swim) {
               for (AttributeModification a : activity.getAttributeModifications()) {
                  if (a.getAttributeType() == EssentialAttributes.ENERGY) {
                     a.addModificator(Math.abs(a.getBaseAmount()) + 10.0F);
                  }

                  if (a.getAttributeType() == EssentialAttributes.HEALTH) {
                     a.addModificator(5.0F);
                  }
               }

               int chance = Util.getInt(0, 100);
               if (chance > 50) {
                  List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.LOOT);
                  int item = Util.getInt(0, loot.size());
                  Item foundItem = loot.get(item);
                  Jasbro.getInstance().getData().getInventory().addItem(foundItem);
                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AQUATIC.found", character, foundItem.getName()));
               }
            }
         }
      }
   }

   public static class Arachnid extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("ARACHNIDSTAGE" + this.stage);
         switch (this.stage) {
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.AUTONOMOUSPERK);
            case 4:
               Perks.PerkUtil.addMaybe(character, Trait.HEARTOFTHESWARM);
            case 3:
               Perks.PerkUtil.addMaybe(character, Trait.OVIPOSITION);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.INHUMANPREGNANCY);
            case 1:
            default:
               Perks.PerkUtil.addMaybe(character, Trait.ARACHNIDSLEEP);
         }
      }

      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE ? currentValue + 5.0 + 5 * this.stage : currentValue;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.HEARTOFTHESWARM);
         Perks.PerkUtil.removeMaybe(character, Trait.OVIPOSITION);
         Perks.PerkUtil.removeMaybe(character, Trait.INHUMANPREGNANCY);
         Perks.PerkUtil.removeMaybe(character, Trait.AUTONOMOUSPERK);
         Perks.PerkUtil.removeMaybe(character, Trait.ARACHNIDSLEEP);
         return super.removeTrait(character);
      }
   }

   public static class ArachnidSleep extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.SLEEP) {
               for (Charakter c : activity.getCharacters()) {
                  if (!c.getTraits().contains(Trait.ARACHNID)) {
                     c.addCondition(new Buff.BadDreams(c));
                  }
               }
            }
         }
      }
   }

   public static class Avian extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("AVIANSTAGE" + this.stage);
         switch (this.stage) {
            case 3:
            case 4:
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.AVIANDRAG);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.AVIANPICKUP);
            case 1:
               Perks.PerkUtil.addMaybe(character, Trait.AVIANFLIGHT);
            default:
               Perks.PerkUtil.addMaybe(character, Trait.AVIANBIRDBRAIN);
         }
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.AVIANDRAG);
         Perks.PerkUtil.removeMaybe(character, Trait.AVIANFLIGHT);
         Perks.PerkUtil.removeMaybe(character, Trait.AVIANPICKUP);
         Perks.PerkUtil.removeMaybe(character, Trait.AVIANBIRDBRAIN);
         return super.removeTrait(character);
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         if (attribute.getAttributeType() == SpecializationAttribute.ADVERTISING) {
            float multiplier = this.stage * 0.5F;
            return value * multiplier;
         } else {
            return 0.0F;
         }
      }
   }

   public static class AvianBirdbrain extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity = (RunningActivity)e.getSource();
            int chance = Util.getInt(0, 100);
            if (activity.getIncome() > 0 && chance > 70) {
               activity.setIncome(0);
               activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AVIAN.birdbrain", character));
            }
         }
      }
   }

   public static class AvianPickup extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.ADVERTISE) {
               int chance = Util.getInt(0, 100);
               if (chance > 50) {
                  List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.LOOT);
                  int item = Util.getInt(0, loot.size());
                  Item foundItem = loot.get(item);
                  Jasbro.getInstance().getData().getInventory().addItem(foundItem);
                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AVIAN.found", character, foundItem.getName()));
               }
            }
         }
      }
   }

   public static class BeastInHeat extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.NEXTDAY) {
            boolean heat = false;
            boolean pregnant = false;
            if (Jasbro.getInstance().getData().getDay() % 30 == 0) {
               heat = true;

               for (Condition con : character.getConditions()) {
                  if (con instanceof PregnancyInterface) {
                     pregnant = true;
                     break;
                  }
               }
            }

            List<Condition> list = character.getConditions();
            if (heat && !pregnant) {
               character.addCondition(FurryPerks.h1);
               character.addCondition(new BattleCondition(character) {
                  @Override
                  public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
                     return calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE ? currentValue + 10.0 : currentValue;
                  }

                  @Override
                  public void handleEvent(MyEvent e) {
                     if (e.getType() == EventType.ACTIVITY) {
                        RunningActivity activity = (RunningActivity)e.getSource();
                        activity.getMainCustomer().addToSatisfaction(activity.getMainCustomer().getSatisfactionAmount() / 2, this);
                     } else if (e.getType() == EventType.NEXTDAY) {
                        this.getCharacter().removeCondition(this);
                     }

                     super.handleEvent(e);
                  }
               });
            } else if (list.contains(FurryPerks.h1)) {
               character.addCondition(FurryPerks.h2);
               character.removeCondition(FurryPerks.h1);
               character.addCondition(new BattleCondition(character) {
                  @Override
                  public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
                     return calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE ? currentValue + 20.0 : currentValue;
                  }

                  @Override
                  public void handleEvent(MyEvent e) {
                     if (e.getType() == EventType.ACTIVITY) {
                        RunningActivity activity = (RunningActivity)e.getSource();
                        activity.getMainCustomer().addToSatisfaction((int)(activity.getMainCustomer().getSatisfactionAmount() * 0.75), this);
                     } else if (e.getType() == EventType.NEXTDAY) {
                        this.getCharacter().removeCondition(this);
                     }

                     super.handleEvent(e);
                  }
               });
            } else if (list.contains(FurryPerks.h2)) {
               character.addCondition(FurryPerks.h3);
               character.removeCondition(FurryPerks.h2);
               character.addCondition(new BattleCondition(character) {
                  @Override
                  public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
                     if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                        return currentValue + 50.0;
                     } else {
                        return calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT ? currentValue + 10.0 : currentValue;
                     }
                  }

                  @Override
                  public void handleEvent(MyEvent e) {
                     if (e.getType() == EventType.NEXTDAY) {
                        this.getCharacter().removeCondition(this);
                     } else if (e.getType() == EventType.ACTIVITY) {
                        RunningActivity activity = (RunningActivity)e.getSource();
                        activity.getMainCustomer().addToSatisfaction((int)(activity.getMainCustomer().getSatisfactionAmount() * 1.5), this);
                     }
                  }
               });
            }
         }
      }
   }

   public static class BestialFeatures extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY)) {
            character.addTrait(Trait.BESTIAL);
         }

         if (character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue() < 10.0F) {
            character.getAttribute(SpecializationAttribute.TRANSFORMATION).setInternValue(10.0F);
         }
      }
   }

   public static class Bovine extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("BOVINESTAGE" + this.stage);
         switch (this.stage) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
         }
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         return super.removeTrait(character);
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         if (attribute.getAttributeType() == BaseAttributeTypes.STAMINA) {
            float multiplier = this.stage * 0.5F;
            return value * multiplier;
         } else {
            return 0.0F;
         }
      }
   }

   public static class Canine extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("CANINESTAGE" + this.stage);
         switch (this.stage) {
            case 4:
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.CANINECOMMAND);
            case 1:
            case 2:
            case 3:
            default:
               Perks.PerkUtil.addMaybe(character, Trait.CANINESLEEP);
         }
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         return super.removeTrait(character);
      }
   }

   public static class CanineCommand extends TraitEffect {
      @Override
      public float getAttributeModifier(Attribute attribute) {
         return attribute.getAttributeType() == BaseAttributeTypes.COMMAND ? attribute.getInternValue() * 0.25F : 0.0F;
      }

      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         if (calculatedAttribute == CalculatedAttribute.CONTROL && currentValue < 0.0) {
            double temp = Math.abs(currentValue) * 0.75;
            return currentValue + temp;
         } else {
            return currentValue;
         }
      }
   }

   public static class CanineSleep extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.SLEEP && activity.getRoom().getAmountPeople() < 2) {
               float modification = 0.0F;

               for (AttributeModification mod : activity.getAttributeModifications()) {
                  if (mod.getAttributeType() == EssentialAttributes.ENERGY) {
                     modification = mod.getBaseAmount();
                  }
               }

               modification = (float)(modification * -1.3);
               activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.ENERGY, character));
               activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CANINE.sleep", character));
            }
         }
      }
   }

   public static class CatNap extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Sleep) {
               activity.getAttributeModifications().add(new AttributeModification(15.0F, EssentialAttributes.ENERGY, character));
               activity.getAttributeModifications().add(new AttributeModification(10.0F, EssentialAttributes.HEALTH, character));
            }
         }
      }
   }

   public static final class ExtractableClaws extends TraitEffect {
      @Override
      public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
         for (Equipment item : character.getCharacterInventory().listEquipment()) {
            if (item.getAccessoryType() == AccessoryType.ONEHANDED || item.getAccessoryType() == AccessoryType.TWOHANDED) {
               return;
            }
         }

         if (attacks.get(0) instanceof Attack.StandardAttack) {
            attacks.remove(0);
         }

         attacks.add(new Attack.ClawAttack(character));
      }
   }

   public static class Feline extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("FELINESTAGE" + this.stage);
         switch (this.stage) {
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.FELINEHEAT);
            case 4:
               Perks.PerkUtil.addMaybe(character, Trait.FELINESTRIP);
            case 3:
               Perks.PerkUtil.addMaybe(character, Trait.NOCTURNAL);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.CATNAP);
            case 1:
            default:
               if (this.stage >= 4 && e.getType() == EventType.ACTIVITYCREATED) {
                  RunningActivity activity = (RunningActivity)e.getSource();
                  if (activity instanceof Strip) {
                     Strip strip = (Strip)activity;
                     strip.setBonus(strip.getBonus() + 5);
                  }
               }
         }
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         float multiplier1 = this.stage * 0.05F;
         float multiplier2 = -(this.stage * 0.1F);
         if (attribute.getAttributeType() == SpecializationAttribute.STRIP) {
            return value * multiplier1;
         }

         if (attribute.getAttributeType() == BaseAttributeTypes.OBEDIENCE) {
            boolean fed = false;

            for (Condition con : attribute.getCharacter().getConditions()) {
               if (con instanceof Buff.Satiated) {
                  fed = true;
               }
            }

            if (!fed) {
               return value * multiplier2;
            }
         }

         return 0.0F;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.addMaybe(character, Trait.FELINESTRIP);
         Perks.PerkUtil.removeMaybe(character, Trait.NOCTURNAL);
         Perks.PerkUtil.removeMaybe(character, Trait.CATNAP);
         Perks.PerkUtil.removeMaybe(character, Trait.FELINEHEAT);
         return super.removeTrait(character);
      }
   }

   public static class FelineStrip extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.STRIP) {
               Strip strip = (Strip)activity;
               strip.setBonus(strip.getBonus() + 5);
            }
         }
      }
   }

   public static final class FlameBreath extends TraitEffect {
      @Override
      public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
         attacks.add(new Attack.FlameBreath(character));
      }
   }

   public static class HeartOfTheSwarm extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
            return currentValue + 30.0;
         } else {
            return calculatedAttribute == CalculatedAttribute.CHANCEADDITIONALCHILD ? currentValue + 20.0 : currentValue;
         }
      }
   }

   public static class InhumanPregnancy extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE ? currentValue + 10.0 : currentValue;
      }
   }

   public static class Insect extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("INSECTSTAGE" + this.stage);
         switch (this.stage) {
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.INSECTSEVERYWHERE);
            case 4:
               Perks.PerkUtil.addMaybe(character, Trait.INSECTEFFICIENCY);
            case 3:
               Perks.PerkUtil.addMaybe(character, Trait.INSECTGATHER);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.INSECTRESILIENCE);
            case 1:
         }
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.INSECTGATHER);
         Perks.PerkUtil.removeMaybe(character, Trait.INSECTRESILIENCE);
         Perks.PerkUtil.removeMaybe(character, Trait.INSECTEFFICIENCY);
         Perks.PerkUtil.removeMaybe(character, Trait.INSECTSEVERYWHERE);
         return super.removeTrait(character);
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         float multiplier = this.stage * 0.05F;
         if (attribute.getAttributeType() == SpecializationAttribute.CLEANING) {
            return value * multiplier;
         } else {
            return attribute.getAttributeType() == SpecializationAttribute.COOKING ? value * multiplier : 0.0F;
         }
      }
   }

   public static class InsectEfficiency extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.CLEAN) {
               int insects = 0;

               for (Room r : activity.getHouse().getRooms()) {
                  for (Charakter c : r.getCurrentUsage().getCharacters()) {
                     if (c.getTraits().contains(Trait.INSECT)) {
                        insects++;
                     }
                  }
               }

               Clean clean = (Clean)activity;
               float dirt = clean.getDirtModification();
               dirt += dirt * 0.1F * insects;
               clean.setDirtModification(dirt);
            }
         }
      }
   }

   public static class InsectGather extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            int chance = Util.getInt(0, 100);
            if (chance > 90) {
               List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.INGREDIENT);
               if (loot.size() > 0) {
                  int item = Util.getInt(0, loot.size());
                  Item foundItem = loot.get(item);
                  Jasbro.getInstance().getData().getInventory().addItem(foundItem);
                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("INSECT.found", character, foundItem.getName()));
               } else {
                  System.err.println("Error: No Ingredient could be found to be gathered.");
               }
            }
         }
      }
   }

   public static class InsectResilience extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() != ActivityType.FIGHT) {
               for (AttributeModification mod : activity.getAttributeModifications()) {
                  if (mod.getAttributeType() == EssentialAttributes.HEALTH && mod.getBaseAmount() < 100.0F) {
                     mod.setBaseAmount(0.0F);
                  }
               }
            }
         }
      }
   }

   public static class InsectsEverywhere extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.CLEAN) {
               Clean c = (Clean)activity;
               float dirt = c.getDirtModification();
               c.setDirtModification(0.0F);
               List<House> houses = Jasbro.getInstance().getData().getHouses();
               int nrHouses = houses.size();
               int dph = (int)(dirt / nrHouses);

               for (House h : houses) {
                  h.modDirt(dph);
               }
            }
         }
      }
   }

   public static class Lagomorph extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("LAGOMORPHSTAGE" + this.stage);
         switch (this.stage) {
            case 1:
            default:
               break;
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHORGY);
            case 4:
               Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHHORNY);
            case 3:
               Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHENDURANCE);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHQUICKY);
         }

         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            List<AttributeModification> mods = activity.getAttributeModifications();
            boolean lago = false;
            float modification = 0.0F;

            for (AttributeModification mod : mods) {
               if (mod.getAttributeType() == EssentialAttributes.MOTIVATION) {
                  modification = mod.getBaseAmount();
                  Room r = activity.getRoom();
                  if (r != null && r.getAmountPeople() > 1) {
                     lago = true;
                  }
               }
            }

            if (!lago) {
               if (modification < 0.0F) {
                  activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.MOTIVATION, character));
               }

               activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LAGOMORPH.lonely", character));
            }
         }
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         float multiplier = this.stage * 0.05F;
         return attribute.getAttributeType() == SpecializationAttribute.BARTENDING ? value * multiplier : 0.0F;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.LAGOMORPHQUICKY);
         Perks.PerkUtil.removeMaybe(character, Trait.LAGOMORPHHORNY);
         Perks.PerkUtil.removeMaybe(character, Trait.LAGOMORPHENDURANCE);
         Perks.PerkUtil.removeMaybe(character, Trait.LAGOMORPHORGY);
         return super.removeTrait(character);
      }
   }

   public static class LagomorphEndurance extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Bartend) {
               for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                  if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                     float modification = attributeModification.getBaseAmount();
                     float change = Math.abs(modification) * 0.3F;
                     attributeModification.addModificator(change);
                  }
               }

               Bartend bartend = (Bartend)activity;
               bartend.setBonus(bartend.getBonus() + 10);
            }
         }
      }
   }

   public static class LagomorphHorny extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.BARTEND) {
               int rnd = Util.getRnd().nextInt(101);
               if (rnd < 51) {
                  for (AttributeModification a : activity.getAttributeModifications()) {
                     if (a.getAttributeType() == EssentialAttributes.MOTIVATION) {
                        a.addModificator(2.0F);
                     }
                  }

                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LAGOMORPH.motivation", character));
               }
            }
         }
      }
   }

   public static class LagomorphOrgy extends TraitEffect {
   }

   public static class Nocturnal extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY && Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity && activity instanceof Whore) {
               activity.getMainCustomers().get(0).addToSatisfaction(95, trait);
            }

            if (activity instanceof Strip) {
               for (Customer customer : activity.getCustomers()) {
                  customer.addToSatisfaction(20, activity);
               }
            }

            if (activity instanceof Bartend) {
               for (Customer customer : activity.getCustomers()) {
                  customer.addToSatisfaction(10, activity);
               }
            }

            if (activity instanceof BathAttendant) {
               for (Customer customer : activity.getCustomers()) {
                  customer.addToSatisfaction(15, activity);
               }
            }

            if (activity instanceof SellFood) {
               for (Customer customer : activity.getCustomers()) {
                  customer.addToSatisfaction(5, activity);
               }
            }
         }
      }
   }

   public static class Oviposition extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.NEXTDAY) {
            boolean notPregnant = true;

            for (Condition condition : character.getConditions()) {
               if (condition instanceof PregnancyInterface) {
                  notPregnant = false;
                  break;
               }
            }

            if (notPregnant && Util.getInt(0, 100) < 6) {
               character.addCondition(new OvipositionPregnancy());
               MessageData messageData = new MessageData(
                  TextUtil.t("oviposition.message"),
                  ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character),
                  character.getBackground(),
                  true
               );
               messageData.createMessageScreen();
            }
         }
      }
   }

   public static class RelentlessBeast extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ENERGYZERO && !e.isCancelled()) {
            e.setCancelled(true);
            AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent)e;
            RunningActivity activity = attributeChangedEvent.getActivity();
            if (activity != null) {
               activity.getAttributeModifications().add(new AttributeModification(-10.0F, EssentialAttributes.HEALTH, character));
            } else {
               character.getAttribute(EssentialAttributes.HEALTH).addToValue(-10.0F);
            }
         }

         if (e.getType() == EventType.STATUSCHANGE) {
            for (Condition con : character.getConditions()) {
               if (con instanceof Illness) {
                  character.removeCondition(con);
               }
            }
         }
      }
   }

   public static class Reptilian extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("REPTILIANSTAGE" + this.stage);
         Attribute HP = character.getAttribute(EssentialAttributes.HEALTH);
         switch (this.stage) {
            case 3:
               if (HP.getMaxValue() < 160) {
                  HP.setMaxValue(HP.getMaxValue() + 60);
               }
               break;
            case 4:
               if (HP.getMaxValue() < 180) {
                  HP.setMaxValue(HP.getMaxValue() + 20);
               }
               break;
            case 5:
               if (HP.getMaxValue() < 200) {
                  HP.setMaxValue(HP.getMaxValue() + 20);
               }
         }

         switch (this.stage) {
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.FLAMEBREATH);
            case 4:
               Perks.PerkUtil.addMaybe(character, Trait.REPTILIANMOTIVATION);
            case 3:
               Perks.PerkUtil.addMaybe(character, Trait.REPTILIANARMOR);
            case 2:
               Perks.PerkUtil.addMaybe(character, Trait.EXTRACTABLECLAWS);
            case 1:
               Perks.PerkUtil.addMaybe(character, Trait.REPTILIANDOWNSIDE);
         }
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         float multiplier = this.stage * 0.05F;
         if (attribute.getAttributeType() == BaseAttributeTypes.STRENGTH) {
            return value * multiplier;
         }

         if (attribute.getAttributeType() == EssentialAttributes.HEALTH) {
         }

         return 0.0F;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.FLAMEBREATH);
         Perks.PerkUtil.removeMaybe(character, Trait.REPTILIANDOWNSIDE);
         Perks.PerkUtil.removeMaybe(character, Trait.EXTRACTABLECLAWS);
         Perks.PerkUtil.removeMaybe(character, Trait.REPTILIANMOTIVATION);
         Perks.PerkUtil.removeMaybe(character, Trait.REPTILIANARMOR);
         if (this.stage >= 3) {
            character.getAttribute(EssentialAttributes.HEALTH)
               .setMaxValue(character.getAttribute(EssentialAttributes.HEALTH).getMaxValue() - 40 - this.stage * 20);
         }

         return super.removeTrait(character);
      }
   }

   public static class ReptilianArmor extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.ARMORVALUE ? currentValue * 1.2 : currentValue;
      }
   }

   public static class ReptilianDownside extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.isAbort()) {
               return;
            }

            if (activity instanceof Whore && activity.getType() != ActivityType.TEASE) {
               if (activity instanceof BusinessMainActivity) {
                  BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
                  if (businessMainActivity.getMainCustomers().size() > 0) {
                     int rnd = Util.getInt(0, 100);
                     int chance = 5;
                     if (rnd < chance) {
                        MessageData message = activity.getMessages().get(0);
                        message.addToMessage(TextUtil.t("REPTILIAN.hit", character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction((int)(-character.getDamage() * 10.0F * 2.0F), trait);
                        activity.getAttributeModifications().add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, character));
                        int chanceKo = (int)character.getDamage();
                        rnd = Util.getInt(0, 100);
                        if (rnd < chanceKo) {
                           activity.setAbort(true);
                           List<AttributeModification> attributeModifications = new ArrayList<>();
                           attributeModifications.add(new AttributeModification(-20.0F, EssentialAttributes.ENERGY, character));
                           attributeModifications.add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, character));
                           message.addToMessage(TextUtil.t("REPTILIAN.hitKO", character, businessMainActivity.getMainCustomers().get(0)));
                           message.setBackground(character.getBackground());
                           message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                           message.setAttributeModifications(attributeModifications);
                           message.createMessageScreen();
                        } else {
                           rnd = Util.getInt(0, 100);
                           int chanceAbort = -businessMainActivity.getMainCustomers().get(0).getSatisfactionAmount()
                              - (int)(character.getDamage() * 4.0F)
                              - character.getCharisma() / 2;
                           if (rnd < chanceAbort) {
                              activity.setAbort(true);
                              List<AttributeModification> attributeModifications = new ArrayList<>();
                              attributeModifications.add(new AttributeModification(-20.0F, EssentialAttributes.ENERGY, character));
                              attributeModifications.add(new AttributeModification(-10.0F, EssentialAttributes.HEALTH, character));
                              attributeModifications.add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, character));
                              message.addToMessage(TextUtil.t("REPTILIAN.hitBack", character, businessMainActivity.getMainCustomers().get(0)));
                              message.setBackground(character.getBackground());
                              message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                              message.setAttributeModifications(attributeModifications);
                              message.createMessageScreen();
                           } else {
                              message.addToMessage(TextUtil.t("REPTILIAN.continue", character, businessMainActivity.getMainCustomers().get(0)));
                           }
                        }
                     }
                  }
               } else if (activity instanceof BusinessSecondaryActivity) {
               }
            }
         }
      }
   }

   public static class ReptilianMotivation extends TraitEffect {
   }

   public static class Vulpine extends TraitEffect {
      int stage = 0;
      int stagelimit = 5;
      int stagestep = 10;

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
         this.stage = (int)(value / this.stagestep);
         if (this.stage > this.stagelimit) {
            this.stage = this.stagelimit;
         }

         Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("VULPINESTAGE" + this.stage);
         switch (this.stage) {
            case 5:
               Perks.PerkUtil.addMaybe(character, Trait.BEASTINHEAT);
            case 1:
            case 2:
            case 3:
            case 4:
         }
      }

      @Override
      public float getAttributeModifier(Attribute attribute) {
         float value = attribute.getInternValue();
         float multiplier = this.stage * 0.05F;
         return attribute.getAttributeType() == SpecializationAttribute.SEDUCTION ? value * multiplier : 0.0F;
      }

      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.CONTROL ? currentValue * 2.0 : currentValue;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
         Perks.PerkUtil.removeMaybe(character, Trait.BEASTINHEAT);
         return super.removeTrait(character);
      }
   }
}
