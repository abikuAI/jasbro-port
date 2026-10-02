package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class WhorePerks {
   public static class BeautySleep extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Sleep) {
               activity.getAttributeModifications().add(new AttributeModification(0.05F, BaseAttributeTypes.CHARISMA, character));
            }
         }
      }
   }

   public static class Chatty extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity && activity instanceof Whore && activity.getMainCustomer().getType() != CustomerType.BUM) {
               activity.getMainCustomers().get(0).addToSatisfaction(1 + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 2, trait);
               if (activity.getMainCustomers().get(0).getStatus() == CustomerStatus.HAPPY) {
                  activity.getAttributeModifications().add(new AttributeModification(0.4F, EssentialAttributes.MOTIVATION, character));
                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CHATTY.happycustomer", character));
               }

               if (activity.getMainCustomers().get(0).getStatus() == CustomerStatus.SAD) {
                  activity.getMainCustomers().get(0).setStatus(CustomerStatus.HAPPY);
                  activity.getMainCustomers().get(0).addToSatisfaction(10 + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 4, trait);
                  activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CHATTY.sadcustomer", character));
               }
            }
         }
      }
   }

   public static class Competitive extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            int bonus = 0;
            if (activity instanceof Whore) {
               Whore whoreActivity = (Whore)activity;
               if (whoreActivity.getSexType() == Sextype.ANAL
                  || whoreActivity.getSexType() == Sextype.VAGINAL
                  || whoreActivity.getSexType() == Sextype.FOREPLAY
                  || whoreActivity.getSexType() == Sextype.GROUP) {
                  House house = activity.getHouse();
                  List<Charakter> li = new ArrayList<>();
                  List<Charakter> li2 = new ArrayList<>();
                  if (house != null) {
                     for (Room room : house.getRooms()) {
                        for (Charakter cha : room.getCurrentUsage().getCharacters()) {
                           if (cha.getName() != character.getName()
                              && Util.getInt(0, 100) < 5
                              && activity.getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION) > 10) {
                              cha.addCondition(new Buff.HornyBuff(cha));
                              li.add(cha);
                              if (Util.getInt(0, 100) < cha.getFinalValue(SpecializationAttribute.SEDUCTION)) {
                                 li2.add(cha);
                                 activity.getAttributeModifications().add(new AttributeModification(0.4F, SpecializationAttribute.SEDUCTION, cha));
                              }
                           }
                        }
                     }

                     if (li.size() != 0) {
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("sexevent.loud", li) + " " + TextUtil.t("sexevent.loudtwo", character));
                     }

                     if (li2.size() != 0) {
                        activity.getMessages().get(0).addToMessage(" " + TextUtil.t("sexevent.loud.pickup", li2));
                     }
                  } else if (Util.getInt(0, 100) < 10) {
                     activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("sexevent.loudstreet", character));
                     character.getFame().modifyFame(character.getFinalValue(SpecializationAttribute.SEDUCTION));
                  }
               }
            }
         }
      }
   }

   public static class Endurance extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                  if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                     float modification = attributeModification.getBaseAmount();
                     float change = Math.abs(modification) * 0.2F;
                     attributeModification.addModificator(change);
                  }
               }
            }
         }
      }
   }

   public static class HighClass extends TraitEffect {
      @Override
      public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
         if (customer.getType() == CustomerType.CELEBRITY) {
            rating = (int)(rating * 5.0F);
         } else if (customer.getType() == CustomerType.LORD) {
            rating = (int)(rating * 2.0F);
         } else if (customer.getType() == CustomerType.MINORNOBLE) {
            rating = (int)(rating * 1.6F);
         } else if (customer.getType() == CustomerType.BUSINESSMAN) {
            rating = (int)(rating * 1.2F);
         } else if (customer.getType() == CustomerType.MERCHANT) {
            rating = (int)(rating * 0.8F);
         } else if (customer.getType() == CustomerType.SOLDIER) {
            rating = (int)(rating * 0.5F);
         } else if (customer.getType() == CustomerType.PEASANT) {
            rating = (int)(rating * 0.0F);
         } else if (customer.getType() == CustomerType.BUM) {
            rating = (int)(rating * 0.0F);
         }

         return rating;
      }
   }

   public static class KeepEmComing extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
               if (servedToday > 12L) {
                  Whore whoreActivity = (Whore)activity;
                  whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.3F);
                  whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier() - 0.3F);
               }
            }
         }
      }
   }

   public static class MyFirst extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
               if (servedToday == 0L) {
                  activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount() / 4, trait);
               }
            }
         }
      }
   }

   public static class OneNight extends TraitEffect {
      @Override
      public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
         if (customer.getStatus() != CustomerStatus.HORNYSTATUS && customer.getStatus() != CustomerStatus.VERYHORNY) {
            rating = (int)(rating * 0.9F);
         } else {
            rating = (int)(rating * 1.6);
         }

         return rating;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               ((Whore)activity).setExecutionTime(100000);
               activity.getMainCustomers().get(0).changePayModifier(10.0F);
               activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount() * 3, trait);

               for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                  if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                     float modification = attributeModification.getBaseAmount();
                     float change = -Math.abs(modification);
                     attributeModification.addModificator(change);
                  }

                  if (attributeModification.getAttributeType() == Sextype.ORAL
                     || attributeModification.getAttributeType() == Sextype.VAGINAL
                     || attributeModification.getAttributeType() == Sextype.ANAL
                     || attributeModification.getAttributeType() == Sextype.TITFUCK
                     || attributeModification.getAttributeType() == Sextype.FOREPLAY
                     || attributeModification.getAttributeType() == Sextype.BONDAGE
                     || attributeModification.getAttributeType() == Sextype.GROUP) {
                     float modification = attributeModification.getBaseAmount();
                     float change = Math.abs(modification) * 2.0F;
                     attributeModification.addModificator(change);
                  }
               }
            }
         }
      }
   }

   public static class OurTime extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               activity.getMainCustomers().get(0).addToSatisfaction(15, trait);
            }
         }
      }
   }

   public static class Renowed extends TraitEffect {
      @Override
      public void handleEvent(MyEvent event, Charakter character, Trait trait) {
         if (event.getType() == EventType.CUSTOMERSARRIVE) {
            CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;

            for (Customer customer : customerEvent.getCustomers()) {
               if (customer.getType() == CustomerType.CELEBRITY) {
                  customer.setInitialMoney(customer.getInitialMoney() + 5000);
               }
            }
         }
      }
   }

   public static class SitBack extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
               if (servedToday > 5L) {
                  Whore whoreActivity = (Whore)activity;
                  whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.2F);
                  if (Util.getInt(0, 100) < servedToday * 3L) {
                     if (Util.getInt(0, 100) < 50) {
                        activity.getMainCustomers().get(0).addToSatisfaction(-10, trait);
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SITBACK.unhappy", character));
                     } else {
                        activity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SITBACK.happy", character));
                     }
                  }

                  for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                     if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * 0.5F;
                        attributeModification.addModificator(change);
                     }
                  }
               }
            }
         }
      }
   }

   public static class Sloppy extends TraitEffect {
      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT && Jasbro.getInstance().getData().getDay() % 30 == 0
            ? currentValue + 32.0
            : currentValue;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore && Jasbro.getInstance().getData().getDay() % 30 == 0) {
               Whore whoreActivity = (Whore)activity;
               whoreActivity.setCooldownTime(0);
               whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier() - 0.7F);

               for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                  if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                     float modification = attributeModification.getBaseAmount();
                     float change = Math.abs(modification);
                     attributeModification.addModificator(change);
                  }
               }
            }
         }
      }
   }

   public static class YouAndMe extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore && activity.getRoom() != null && activity.getRoom().getAmountPeople() == 1) {
               activity.getMainCustomers().get(0).addToSatisfaction(10, trait);
            }
         }
      }
   }
}
