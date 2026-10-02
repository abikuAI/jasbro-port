/*
 * Decompiled with CFR 0.152.
 */
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

public class WhorePerks {

    public static class Sloppy
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT && Jasbro.getInstance().getData().getDay() % 30 == 0) {
                return currentValue + 32.0;
            }
            return currentValue;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && Jasbro.getInstance().getData().getDay() % 30 == 0) {
                Whore whoreActivity = (Whore)activity;
                whoreActivity.setCooldownTime(0);
                whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier() - 0.7f);
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification);
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class HighClass
    extends TraitEffect {
        @Override
        public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
            if (customer.getType() == CustomerType.CELEBRITY) {
                rating = (int)((float)rating * 5.0f);
            } else if (customer.getType() == CustomerType.LORD) {
                rating = (int)((float)rating * 2.0f);
            } else if (customer.getType() == CustomerType.MINORNOBLE) {
                rating = (int)((float)rating * 1.6f);
            } else if (customer.getType() == CustomerType.BUSINESSMAN) {
                rating = (int)((float)rating * 1.2f);
            } else if (customer.getType() == CustomerType.MERCHANT) {
                rating = (int)((float)rating * 0.8f);
            } else if (customer.getType() == CustomerType.SOLDIER) {
                rating = (int)((float)rating * 0.5f);
            } else if (customer.getType() == CustomerType.PEASANT) {
                rating = (int)((float)rating * 0.0f);
            } else if (customer.getType() == CustomerType.BUM) {
                rating = (int)((float)rating * 0.0f);
            }
            return rating;
        }
    }

    public static class Chatty
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity && activity instanceof Whore && activity.getMainCustomer().getType() != CustomerType.BUM) {
                activity.getMainCustomers().get(0).addToSatisfaction(1 + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 2, trait);
                if (activity.getMainCustomers().get(0).getStatus() == CustomerStatus.HAPPY) {
                    activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
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

    public static class SitBack
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Long servedToday;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && (servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString())) > 5L) {
                Whore whoreActivity = (Whore)activity;
                whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.2f);
                if ((long)Util.getInt(0, 100) < servedToday * 3L) {
                    if (Util.getInt(0, 100) < 50) {
                        activity.getMainCustomers().get(0).addToSatisfaction(-10, trait);
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SITBACK.unhappy", character));
                    } else {
                        activity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SITBACK.happy", character));
                    }
                }
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) * 0.5f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class Endurance
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) * 0.2f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class Competitive
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                Whore whoreActivity;
                RunningActivity activity = (RunningActivity)e.getSource();
                boolean bonus = false;
                if (activity instanceof Whore && ((whoreActivity = (Whore)activity).getSexType() == Sextype.ANAL || whoreActivity.getSexType() == Sextype.VAGINAL || whoreActivity.getSexType() == Sextype.FOREPLAY || whoreActivity.getSexType() == Sextype.GROUP)) {
                    House house = activity.getHouse();
                    ArrayList<Charakter> li = new ArrayList<Charakter>();
                    ArrayList<Charakter> li2 = new ArrayList<Charakter>();
                    if (house != null) {
                        for (Room room : house.getRooms()) {
                            for (Charakter cha : room.getCurrentUsage().getCharacters()) {
                                if (cha.getName() == character.getName() || Util.getInt(0, 100) >= 5 || activity.getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION) <= 10) continue;
                                cha.addCondition(new Buff.HornyBuff(cha));
                                li.add(cha);
                                if (Util.getInt(0, 100) >= cha.getFinalValue(SpecializationAttribute.SEDUCTION)) continue;
                                li2.add(cha);
                                activity.getAttributeModifications().add(new AttributeModification(0.4f, SpecializationAttribute.SEDUCTION, cha));
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

    public static class BeautySleep
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Sleep) {
                activity.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.CHARISMA, character));
            }
        }
    }

    public static class YouAndMe
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && activity.getRoom() != null && activity.getRoom().getAmountPeople() == 1) {
                activity.getMainCustomers().get(0).addToSatisfaction(10, trait);
            }
        }
    }

    public static class Renowed
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (customer.getType() != CustomerType.CELEBRITY) continue;
                    customer.setInitialMoney(customer.getInitialMoney() + 5000);
                }
            }
        }
    }

    public static class MyFirst
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Long servedToday;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && (servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString())) == 0L) {
                activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount() / 4, trait);
            }
        }
    }

    public static class OneNight
    extends TraitEffect {
        @Override
        public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
            rating = customer.getStatus() == CustomerStatus.HORNYSTATUS || customer.getStatus() == CustomerStatus.VERYHORNY ? (int)((double)rating * 1.6) : (int)((float)rating * 0.9f);
            return rating;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                ((Whore)activity).setExecutionTime(100000);
                activity.getMainCustomers().get(0).changePayModifier(10.0f);
                activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount() * 3, trait);
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    float change;
                    float modification;
                    if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                        modification = attributeModification.getBaseAmount();
                        change = -Math.abs(modification);
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                    if (attributeModification.getAttributeType() != Sextype.ORAL && attributeModification.getAttributeType() != Sextype.VAGINAL && attributeModification.getAttributeType() != Sextype.ANAL && attributeModification.getAttributeType() != Sextype.TITFUCK && attributeModification.getAttributeType() != Sextype.FOREPLAY && attributeModification.getAttributeType() != Sextype.BONDAGE && attributeModification.getAttributeType() != Sextype.GROUP) continue;
                    modification = attributeModification.getBaseAmount();
                    change = Math.abs(modification) * 2.0f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class OurTime
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                activity.getMainCustomers().get(0).addToSatisfaction(15, trait);
            }
        }
    }

    public static class KeepEmComing
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Long servedToday;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && (servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString())) > 12L) {
                Whore whoreActivity = (Whore)activity;
                whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.3f);
                whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier() - 0.3f);
            }
        }
    }
}

