package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.whore.Dominate;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.Illness;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.texts.TextUtil;
import java.util.List;

public class DominatrixPerks {
   public static final Condition aggressiveAdvertiser = new Condition() {};

   public static class AggressiveAdvertisement extends TraitEffect {
      @Override
      public float getAttributeModifier(Attribute attribute) {
         return attribute.getAttributeType() == SpecializationAttribute.ADVERTISING
            ? attribute.getCharacter().getAttribute(SpecializationAttribute.DOMINATE).getInternValue() / 20.0F
            : 0.0F;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         character.removeCondition(DominatrixPerks.aggressiveAdvertiser);
         return true;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            if (!character.getConditions().contains(DominatrixPerks.aggressiveAdvertiser)) {
               character.addCondition(DominatrixPerks.aggressiveAdvertiser);
            }
         } else if (e.getType() == EventType.CUSTOMERSARRIVE) {
            CustomersArriveEvent customerEvent = (CustomersArriveEvent)e;

            for (Customer customer : customerEvent.getCustomers()) {
               if (Util.getInt(1, 20) <= 2 && customer.getType() != CustomerType.GROUP) {
                  customer.setPreferredSextype(Sextype.BONDAGE);
               }
            }
         }
      }
   }

   public static class CruelMaster extends TraitEffect {
      @Override
      public float getAttributeModifier(Attribute attribute) {
         return attribute.getAttributeType() == SpecializationAttribute.DOMINATE
            ? attribute.getCharacter().getAttribute(BaseAttributeTypes.STRENGTH).getInternValue() / 4.0F
            : 0.0F;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.DOMINATE) {
               Dominate dom = (Dominate)activity;
               if (dom.getAmountActions() > 1.0F) {
                  dom.setActionCost(1.0F);
               }
            }
         }
      }
   }

   public static class EveryoneBehave extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITYCREATED) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Bartend) {
               Bartend bartend = (Bartend)activity;
               bartend.setBonus(bartend.getBonus() + 30);
            }
         }
      }
   }

   public static class GiveAndTake extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == BaseAttributeTypes.STRENGTH
               || attributeModification.getAttributeType() == BaseAttributeTypes.STAMINA) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.2F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class GoodMastersAreTheBestSubs extends TraitEffect {
      @Override
      public float getAttributeModifier(Attribute attribute) {
         float dominate = attribute.getCharacter().getAttribute(SpecializationAttribute.DOMINATE).getInternValue();
         float bondage = attribute.getCharacter().getAttribute(Sextype.BONDAGE).getInternValue();
         float value = dominate - bondage;
         float percentBonus = 0.2F;
         if (value > 0.0F) {
            if (attribute.getAttributeType() == Sextype.BONDAGE) {
               return value;
            }
         } else if (value < 0.0F) {
            if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {
               return -value;
            }
         } else if (value == 0.0F) {
            if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {
               return dominate * percentBonus;
            }

            if (attribute.getAttributeType() == Sextype.BONDAGE) {
               return bondage * percentBonus;
            }
         }

         return 0.0F;
      }
   }

   public static class HitMeHarder extends TraitEffect {
      @Override
      public float getAttributeModifier(Attribute attribute) {
         return attribute.getAttributeType() == Sextype.BONDAGE
            ? attribute.getCharacter().getAttribute(BaseAttributeTypes.STAMINA).getInternValue() / 4.0F
            : 0.0F;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.SUBMIT) {
               for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                  if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                     if (character.getHealth() > 30) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * 2.0F;
                        attributeModification.addModificator(-change);
                     } else if (character.getHealth() <= 30) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * 1.0F;
                        attributeModification.addModificator(change);
                     }
                  }
               }
            } else if (activity.getType() == ActivityType.WHORE) {
               Whore whore = (Whore)activity;
               if (whore.getSexType() == Sextype.BONDAGE) {
                  for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                     if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        if (character.getHealth() > 30) {
                           float modification = attributeModification.getBaseAmount();
                           float change = Math.abs(modification) * 1.0F;
                           attributeModification.addModificator(-change);
                        } else if (character.getHealth() <= 30) {
                           float modification = attributeModification.getBaseAmount();
                           float change = Math.abs(modification) * 1.0F;
                           attributeModification.addModificator(change);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static class KillYou extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore && Util.getInt(0, 100) < 5) {
               Whore whoreActivity = (Whore)activity;
               if (whoreActivity.getSexType() == Sextype.BONDAGE || whoreActivity.getSexType() == Sextype.GROUP) {
                  for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                     if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        float modification = attributeModification.getBaseAmount();
                        float change = -Math.abs(modification) * 3.0F;
                        attributeModification.addModificator(change - 1.0F);
                     }

                     if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                        float modification = attributeModification.getBaseAmount();
                        float change = -Math.abs(modification) * 4.5F;
                        attributeModification.addModificator(change);
                     }
                  }

                  whoreActivity.getMainCustomers().get(0).addToSatisfaction(200, trait);
                  Attribute attribute = character.getAttribute(EssentialAttributes.HEALTH);
                  attribute.setMaxValue(attribute.getMaxValue() + 1);
                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("KILLYOU.rough", character));
                  character.addCondition(new Buff.RoughenedUp());
               }
            }
         }
      }
   }

   public static class LeatherMistress extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.NEXTDAY) {
            for (House thisHouse : Jasbro.getInstance().getData().getHouses()) {
               List<Room> listRooms = thisHouse.getRooms();

               for (Room firstWalkThroughRooms : listRooms) {
                  if (firstWalkThroughRooms.getCurrentUsage().getCharacters().contains(character)) {
                     for (Room iterateAllRooms : listRooms) {
                        for (Charakter thisCharacter : iterateAllRooms.getCurrentUsage().getCharacters()) {
                           if (thisCharacter != character && thisCharacter.getType() == CharacterType.SLAVE) {
                              AttributeModification attributeModification = new AttributeModification(0.2F, BaseAttributeTypes.OBEDIENCE, thisCharacter);
                              attributeModification.applyModification();
                           }
                        }
                     }
                     break;
                  }
               }
            }
         }
      }
   }

   public static class LeatherQueen extends TraitEffect {
      @Override
      public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
         if (customer.getPreferredSextype() == Sextype.BONDAGE) {
            rating *= 3;
         } else {
            rating = (int)(rating * 0.5F);
         }

         return rating;
      }
   }

   public static class LickItUp extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.WHORE || activity.getType() == ActivityType.SUBMIT) {
               Whore whore = (Whore)activity;
               if (whore.getSexType() == Sextype.BONDAGE && character.getSpecializations().contains(SpecializationType.MAID)) {
                  House house = activity.getHouse();
                  int clean = 0;

                  for (SpecializationType spec : character.getSpecializations()) {
                     if (spec == SpecializationType.MAID) {
                        double cleaning = character.getAnyAttributeValue(SpecializationAttribute.CLEANING) / 100.0;
                        clean = (int)(clean - cleaning);
                     }
                  }

                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LICKITUP.clean", character));
                  character.getAttribute(SpecializationAttribute.CLEANING).addToValue(1.0F);
                  house.modDirt(clean);
                  int random = Util.getInt(0, 100);
                  if (random < 1) {
                     character.addCondition(new Illness.Flu());
                     activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LICKITUP.flu", character));
                  }
               }
            }
         }
      }
   }

   public static class Masochist extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               Whore whoreActivity = (Whore)activity;
               if (whoreActivity.getSexType() == Sextype.BONDAGE) {
                  for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                     if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * 0.5F;
                        attributeModification.addModificator(change);
                     } else if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * 0.5F;
                        attributeModification.addModificator(change);
                     } else if (attributeModification.getAttributeType() == EssentialAttributes.MOTIVATION) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * 0.8F;
                        attributeModification.addModificator(change);
                     }
                  }

                  int bonusSatisfaction = (100 - character.getFinalValue(EssentialAttributes.ENERGY)) * 2;
                  whoreActivity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
               }
            }
         }
      }
   }

   public static class MasterAndSlave extends TraitEffect {
   }

   public static class MyWhipIsAllINeed extends TraitEffect {
   }

   public static class PleasureAndPain extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == SpecializationAttribute.DOMINATE || attributeModification.getAttributeType() == Sextype.BONDAGE) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class PleasureInPain extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.SUBMIT) {
               character.addCondition(new Buff.MasochisticDelight(character));
            } else if (activity.getType() == ActivityType.WHORE) {
               Whore whore = (Whore)activity;
               if (whore.getSexType() == Sextype.BONDAGE) {
                  character.addCondition(new Buff.MasochisticDelight(character));
               }
            }
         }
      }
   }

   public static class PleasureThroughPain extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.DOMINATE) {
               character.addCondition(new Buff.SadisticDelight(character));
            }
         }
      }
   }

   public static class Sadist extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.DOMINATE) {
               int bonusSatisfaction = character.getFinalValue(EssentialAttributes.ENERGY) / 2 - 30;
               activity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
            }
         }
      }
   }

   public static class SquealAndHeal extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            float nurse = 0.0F;
            if (character.getSpecializations().contains(SpecializationType.NURSE)) {
               nurse = (float)character.getAnyAttributeValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 1000.0F;
               if (nurse > 0.9) {
                  nurse = 0.9F;
               }
            }

            if (activity.getType() == ActivityType.SUBMIT) {
               for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                  if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                     float modification = attributeModification.getBaseAmount();
                     float change = Math.abs(modification) * nurse;
                     attributeModification.addModificator(change);
                  }
               }
            } else if (activity.getType() == ActivityType.WHORE) {
               Whore whore = (Whore)activity;
               if (whore.getSexType() == Sextype.BONDAGE) {
                  for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                     if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * nurse;
                        attributeModification.addModificator(change);
                     }
                  }
               }
            }
         }
      }
   }
}
