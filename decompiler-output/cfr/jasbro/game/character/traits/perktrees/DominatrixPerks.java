/*
 * Decompiled with CFR 0.152.
 */
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
    public static final Condition aggressiveAdvertiser = new Condition(){};

    public static class LeatherMistress
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                List<House> listHouses = Jasbro.getInstance().getData().getHouses();
                block0: for (House thisHouse : listHouses) {
                    List<Room> listRooms = thisHouse.getRooms();
                    for (Room firstWalkThroughRooms : listRooms) {
                        if (!firstWalkThroughRooms.getCurrentUsage().getCharacters().contains(character)) continue;
                        for (Room iterateAllRooms : listRooms) {
                            List<Charakter> listCharacter = iterateAllRooms.getCurrentUsage().getCharacters();
                            for (Charakter thisCharacter : listCharacter) {
                                if (thisCharacter == character || thisCharacter.getType() != CharacterType.SLAVE) continue;
                                AttributeModification attributeModification = new AttributeModification(0.2f, BaseAttributeTypes.OBEDIENCE, thisCharacter);
                                attributeModification.applyModification();
                            }
                        }
                        continue block0;
                    }
                }
            }
        }
    }

    public static class MyWhipIsAllINeed
    extends TraitEffect {
    }

    public static class Sadist
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.DOMINATE) {
                int bonusSatisfaction = character.getFinalValue(EssentialAttributes.ENERGY) / 2 - 30;
                activity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
            }
        }
    }

    public static class PleasureInPain
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                Whore whore;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity.getType() == ActivityType.SUBMIT) {
                    character.addCondition(new Buff.MasochisticDelight(character));
                } else if (activity.getType() == ActivityType.WHORE && (whore = (Whore)activity).getSexType() == Sextype.BONDAGE) {
                    character.addCondition(new Buff.MasochisticDelight(character));
                }
            }
        }
    }

    public static class PleasureThroughPain
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.DOMINATE) {
                character.addCondition(new Buff.SadisticDelight(character));
            }
        }
    }

    public static class PleasureAndPain
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && ((attributeModification = (AttributeModification)e.getSource()).getAttributeType() == SpecializationAttribute.DOMINATE || attributeModification.getAttributeType() == Sextype.BONDAGE)) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static class CruelMaster
    extends TraitEffect {
        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {
                return attribute.getCharacter().getAttribute(BaseAttributeTypes.STRENGTH).getInternValue() / 4.0f;
            }
            return 0.0f;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Dominate dom;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.DOMINATE && (dom = (Dominate)activity).getAmountActions().floatValue() > 1.0f) {
                dom.setActionCost(1.0f);
            }
        }
    }

    public static class HitMeHarder
    extends TraitEffect {
        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (attribute.getAttributeType() == Sextype.BONDAGE) {
                return attribute.getCharacter().getAttribute(BaseAttributeTypes.STAMINA).getInternValue() / 4.0f;
            }
            return 0.0f;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            block4: {
                Whore whore;
                RunningActivity activity;
                block5: {
                    if (e.getType() != EventType.ACTIVITY) break block4;
                    activity = (RunningActivity)e.getSource();
                    if (activity.getType() != ActivityType.SUBMIT) break block5;
                    for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                        float change;
                        float modification;
                        if (attributeModification.getAttributeType() != EssentialAttributes.HEALTH) continue;
                        if (character.getHealth() > 30) {
                            modification = attributeModification.getBaseAmount();
                            change = Math.abs(modification) * 2.0f;
                            attributeModification.addModificator(Float.valueOf(-change));
                            continue;
                        }
                        if (character.getHealth() > 30) continue;
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 1.0f;
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                    break block4;
                }
                if (activity.getType() != ActivityType.WHORE || (whore = (Whore)activity).getSexType() != Sextype.BONDAGE) break block4;
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    float change;
                    float modification;
                    if (attributeModification.getAttributeType() != EssentialAttributes.HEALTH) continue;
                    if (character.getHealth() > 30) {
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 1.0f;
                        attributeModification.addModificator(Float.valueOf(-change));
                        continue;
                    }
                    if (character.getHealth() > 30) continue;
                    modification = attributeModification.getBaseAmount();
                    change = Math.abs(modification) * 1.0f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class GiveAndTake
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && ((attributeModification = (AttributeModification)e.getSource()).getAttributeType() == BaseAttributeTypes.STRENGTH || attributeModification.getAttributeType() == BaseAttributeTypes.STAMINA)) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * 0.2f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static class GoodMastersAreTheBestSubs
    extends TraitEffect {
        @Override
        public float getAttributeModifier(Attribute attribute) {
            float dominate = attribute.getCharacter().getAttribute(SpecializationAttribute.DOMINATE).getInternValue();
            float bondage = attribute.getCharacter().getAttribute(Sextype.BONDAGE).getInternValue();
            float value = dominate - bondage;
            float percentBonus = 0.2f;
            if (value > 0.0f) {
                if (attribute.getAttributeType() == Sextype.BONDAGE) {
                    return value;
                }
            } else if (value < 0.0f) {
                if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {
                    return -value;
                }
            } else if (value == 0.0f) {
                if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {
                    return dominate * percentBonus;
                }
                if (attribute.getAttributeType() == Sextype.BONDAGE) {
                    return bondage * percentBonus;
                }
            }
            return 0.0f;
        }
    }

    public static class AggressiveAdvertisement
    extends TraitEffect {
        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (attribute.getAttributeType() == SpecializationAttribute.ADVERTISING) {
                return attribute.getCharacter().getAttribute(SpecializationAttribute.DOMINATE).getInternValue() / 20.0f;
            }
            return 0.0f;
        }

        @Override
        public boolean removeTrait(Charakter character) {
            character.removeCondition(aggressiveAdvertiser);
            return true;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                if (!character.getConditions().contains(aggressiveAdvertiser)) {
                    character.addCondition(aggressiveAdvertiser);
                }
            } else if (e.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)e;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) > 2 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.BONDAGE);
                }
            }
        }
    }

    public static class EveryoneBehave
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITYCREATED && (activity = (RunningActivity)e.getSource()) instanceof Bartend) {
                Bartend bartend = (Bartend)activity;
                bartend.setBonus(bartend.getBonus() + 30);
            }
        }
    }

    public static class SquealAndHeal
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            block3: {
                Whore whore;
                float nurse;
                RunningActivity activity;
                block4: {
                    if (e.getType() != EventType.ACTIVITY) break block3;
                    activity = (RunningActivity)e.getSource();
                    nurse = 0.0f;
                    if (character.getSpecializations().contains(SpecializationType.NURSE) && (double)(nurse = (float)character.getAnyAttributeValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 1000.0f) > 0.9) {
                        nurse = 0.9f;
                    }
                    if (activity.getType() != ActivityType.SUBMIT) break block4;
                    for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                        if (attributeModification.getAttributeType() != EssentialAttributes.HEALTH) continue;
                        float modification = attributeModification.getBaseAmount();
                        float change = Math.abs(modification) * nurse;
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                    break block3;
                }
                if (activity.getType() != ActivityType.WHORE || (whore = (Whore)activity).getSexType() != Sextype.BONDAGE) break block3;
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.HEALTH) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) * nurse;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            }
        }
    }

    public static class LickItUp
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Whore whore;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && ((activity = (RunningActivity)e.getSource()).getType() == ActivityType.WHORE || activity.getType() == ActivityType.SUBMIT) && (whore = (Whore)activity).getSexType() == Sextype.BONDAGE && character.getSpecializations().contains(SpecializationType.MAID)) {
                House house = activity.getHouse();
                int clean = 0;
                for (SpecializationType spec : character.getSpecializations()) {
                    if (spec != SpecializationType.MAID) continue;
                    double cleaning = character.getAnyAttributeValue(SpecializationAttribute.CLEANING) / 100.0;
                    clean = (int)((double)clean - cleaning);
                }
                activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LICKITUP.clean", character));
                character.getAttribute(SpecializationAttribute.CLEANING).addToValue(1.0f);
                house.modDirt(clean);
                int random = Util.getInt(0, 100);
                if (random < 1) {
                    character.addCondition(new Illness.Flu());
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LICKITUP.flu", character));
                }
            }
        }
    }

    public static class MasterAndSlave
    extends TraitEffect {
    }

    public static class Masochist
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Whore whoreActivity;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && (whoreActivity = (Whore)activity).getSexType() == Sextype.BONDAGE) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    float change;
                    float modification;
                    if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 0.5f;
                        attributeModification.addModificator(Float.valueOf(change));
                        continue;
                    }
                    if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 0.5f;
                        attributeModification.addModificator(Float.valueOf(change));
                        continue;
                    }
                    if (attributeModification.getAttributeType() != EssentialAttributes.MOTIVATION) continue;
                    modification = attributeModification.getBaseAmount();
                    change = Math.abs(modification) * 0.8f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
                int bonusSatisfaction = (100 - character.getFinalValue(EssentialAttributes.ENERGY)) * 2;
                whoreActivity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
            }
        }
    }

    public static class KillYou
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            Whore whoreActivity;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && Util.getInt(0, 100) < 5 && ((whoreActivity = (Whore)activity).getSexType() == Sextype.BONDAGE || whoreActivity.getSexType() == Sextype.GROUP)) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    float change;
                    float modification;
                    if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                        modification = attributeModification.getBaseAmount();
                        change = -Math.abs(modification) * 3.0f;
                        attributeModification.addModificator(Float.valueOf(change - 1.0f));
                    }
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    modification = attributeModification.getBaseAmount();
                    change = -Math.abs(modification) * 4.5f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
                whoreActivity.getMainCustomers().get(0).addToSatisfaction(200, trait);
                Attribute attribute = character.getAttribute(EssentialAttributes.HEALTH);
                attribute.setMaxValue(attribute.getMaxValue() + 1);
                activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("KILLYOU.rough", character));
                character.addCondition(new Buff.RoughenedUp());
            }
        }
    }

    public static class LeatherQueen
    extends TraitEffect {
        @Override
        public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
            rating = customer.getPreferredSextype() == Sextype.BONDAGE ? (rating *= 3) : (int)((float)rating * 0.5f);
            return rating;
        }
    }
}

