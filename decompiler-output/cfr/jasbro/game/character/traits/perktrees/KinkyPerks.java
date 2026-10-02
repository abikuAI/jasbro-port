/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Advertise;
import jasbro.game.character.activities.sub.business.SubmitToMonster;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.texts.TextUtil;

public class KinkyPerks {

    public static class FleshToy
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (customer.getType() != CustomerType.GROUP && customer.getPreferredSextype() != Sextype.BONDAGE && customer.getPreferredSextype() != Sextype.MONSTER) continue;
                    customer.setInitialMoney((int)((double)customer.getInitialMoney() * 1.6));
                }
            }
        }
    }

    public static class PublicUse
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.PUBLICUSE) {
                int reduction = (100 - character.getFinalValue(EssentialAttributes.ENERGY)) * 2;
                if (reduction > 90) {
                    reduction = 90;
                }
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) / 2.0f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class GangbangQueen
    extends TraitEffect {
        @Override
        public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
            if (customer.getType() == CustomerType.GROUP) {
                CustomerGroup group = (CustomerGroup)customer;
                rating *= group.getCustomers().size() * 2;
            } else {
                rating = (int)((float)rating * 0.3f);
            }
            return rating;
        }
    }

    public static class Submissive
    extends TraitEffect {
        @Override
        public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
            return 0;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                Whore whoreActivity = (Whore)activity;
                if (Util.getInt(1, 5) == 1 || Util.getInt(1, 2) == 1 && (whoreActivity.getMainCustomer().getStatus() == CustomerStatus.PISSED || whoreActivity.getMainCustomer().getStatus() == CustomerStatus.DRUNK)) {
                    for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                        float change;
                        float modification;
                        if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                            modification = attributeModification.getBaseAmount();
                            change = -Math.abs(modification) * 0.1f;
                            attributeModification.addModificator(Float.valueOf(change - 1.0f));
                        }
                        if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                        modification = attributeModification.getBaseAmount();
                        change = -Math.abs(modification) * 0.4f;
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                    whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() + 0.15f);
                    whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier() - 0.1f);
                    whoreActivity.getMainCustomers().get(0).addToSatisfaction(whoreActivity.getMainCustomers().get(0).getSatisfactionAmount() / 5, trait);
                    if (whoreActivity.getSexType() == Sextype.ORAL) {
                        if (Util.getInt(0, 10) < 5) {
                            activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SUBMISSIVE.rough.oral.one", character));
                        } else {
                            activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SUBMISSIVE.rough.oral.two", character));
                        }
                    }
                    if (whoreActivity.getSexType() == Sextype.VAGINAL || whoreActivity.getSexType() == Sextype.ANAL) {
                        if (Util.getInt(0, 10) < 5) {
                            activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SUBMISSIVE.rough.one", character));
                        } else {
                            activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SUBMISSIVE.rough.two", character));
                        }
                    }
                    if (whoreActivity.getSexType() == Sextype.GROUP) {
                        if (Util.getInt(0, 10) < 5) {
                            activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SUBMISSIVE.rough.group.one", character));
                        } else {
                            activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SUBMISSIVE.rough.group.two", character));
                        }
                        character.addCondition(new Buff.RoughenedUp());
                    }
                }
            }
        }
    }

    public static class Pervert
    extends TraitEffect {
        @Override
        public boolean addTrait(Charakter character) {
            character.getFame().modifyFame(5000.0);
            return true;
        }

        @Override
        public boolean removeTrait(Charakter character) {
            character.getFame().modifyFame(-5000.0);
            return true;
        }
    }

    public static class AnyplaceAnywhereAnytime
    extends TraitEffect {
    }

    public static class FromDuskTillDawn
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.WHORE) {
                float servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()).longValue();
                float modifier = servedToday / 50.0f;
                int satisfaction = (int)((float)activity.getMainCustomer().getSatisfactionAmount() * modifier);
                activity.getMainCustomer().addToSatisfaction(satisfaction, trait);
            }
        }
    }

    public static final class CumSlut
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                Whore whoreActivity;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity instanceof Whore && ((whoreActivity = (Whore)activity).getSexType() == Sextype.GROUP && whoreActivity.getMainCustomers().size() > 0 || whoreActivity.getSexType() == Sextype.ORAL)) {
                    int magnitude = whoreActivity.getMainCustomers().size() * 2;
                    character.getAttribute(EssentialAttributes.ENERGY).addToValue((float)magnitude * 4.0f, activity);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CUMSLUT.enjoy", character));
                    activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
                    if (Util.getInt(0, 100) > 90) {
                        character.addCondition(new Buff.Satiated(magnitude, character));
                        character.getAttribute(EssentialAttributes.HEALTH).addToValue((float)magnitude * 1.0f, activity);
                        activity.getMessages().get(0).addToMessage(" " + TextUtil.t("CUMSLUT.drink", character));
                    }
                    if (Util.getInt(0, 100) > 90) {
                        character.addCondition(new Buff.SmoothSkin(magnitude, character));
                        activity.getMessages().get(0).addToMessage(" " + TextUtil.t("CUMSLUT.smear", character));
                    }
                }
                if (activity instanceof SubmitToMonster) {
                    character.getAttribute(EssentialAttributes.ENERGY).addToValue(4.0f, activity);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CUMSLUT.enjoy.monster", character));
                    if (Util.getInt(0, 100) > 50) {
                        character.addCondition(new Buff.Satiated(15, character));
                        character.getAttribute(EssentialAttributes.HEALTH).addToValue(4.0f, activity);
                        activity.getMessages().get(0).addToMessage(" " + TextUtil.t("CUMSLUT.drink.monster", character));
                    }
                    if (Util.getInt(0, 100) > 50) {
                        character.addCondition(new Buff.SmoothSkin(20, character));
                        activity.getMessages().get(0).addToMessage(" " + TextUtil.t("CUMSLUT.smear.monster", character));
                    }
                }
            }
        }
    }

    public static class SexAddict
    extends TraitEffect {
        @Override
        public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
            rating = customer.getStatus() == CustomerStatus.STRONGSTATUS ? (int)((float)rating * 1.1f) : (int)((float)rating * 0.9f);
            return rating;
        }

        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
                return currentValue + 40.0;
            }
            return currentValue;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY || Util.getInt(0, 100) <= 75) continue;
                    ((Whore)activity).setCooldownTime(0);
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification);
                    attributeModification.addModificator(Float.valueOf(change));
                    int rand = Util.getInt(0, 100);
                    if (rand < 25) {
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SEXADDICT.another.one", character));
                        continue;
                    }
                    if (rand < 50) {
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SEXADDICT.another.two", character));
                        continue;
                    }
                    if (rand < 75) {
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SEXADDICT.another.three", character));
                        continue;
                    }
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SEXADDICT.another.four", character));
                }
            }
        }
    }

    public static class MonsterSow
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity && activity instanceof SubmitToMonster) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    float change;
                    float modification;
                    if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 1.0f;
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    modification = attributeModification.getBaseAmount();
                    change = Math.abs(modification) * 0.5f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class MeatToilet
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && ((activity = (RunningActivity)e.getSource()) instanceof Whore || activity instanceof SubmitToMonster || activity.getType() == ActivityType.PUBLICUSE)) {
                int reduction = (100 - character.getFinalValue(EssentialAttributes.ENERGY)) * 2;
                if (reduction > 90) {
                    reduction = 90;
                }
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY || Util.getInt(0, 100) <= 60) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) * (float)reduction / 100.0f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class SexManiac
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Whore whoreActivity;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && ((whoreActivity = (Whore)activity).getSexType() == Sextype.BONDAGE || whoreActivity.getSexType() == Sextype.GROUP || whoreActivity.getSexType() == Sextype.MONSTER)) {
                activity.setFameModifier(activity.getFameModifier() + 0.5f);
            }
        }
    }

    public static class Breeder
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                return currentValue + 10.0;
            }
            if (calculatedAttribute == CalculatedAttribute.MINCHILDREN) {
                return currentValue + 1.0;
            }
            if (calculatedAttribute == CalculatedAttribute.MAXCHILDREN) {
                return currentValue + 2.0;
            }
            if (calculatedAttribute == CalculatedAttribute.CHANCEADDITIONALCHILD) {
                return currentValue + 25.0;
            }
            return currentValue;
        }
    }

    public static class Exhibitionist
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                Advertise advertiseActivity = (Advertise)activity;
                advertiseActivity.setStartingEffectiveness(advertiseActivity.getStartingEffectiveness() + 10);
                activity.getAttributeModifications().add(new AttributeModification(0.1f, EssentialAttributes.MOTIVATION, character));
            }
        }
    }

    public static class Kinky
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                activity.getMainCustomers().get(0).addToSatisfaction(10, trait);
            }
        }
    }
}

