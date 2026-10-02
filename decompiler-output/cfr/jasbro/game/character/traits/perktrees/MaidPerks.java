/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Cook;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;
import java.util.ArrayList;

public class MaidPerks {

    public static class AlwaysImprove
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                Attribute attribute;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity instanceof SellFood && Util.getInt(0, 100) > 10) {
                    attribute = character.getAttribute(SpecializationAttribute.COOKING);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("MAID.improvecooking", character));
                    attribute.setMaxValue(attribute.getMaxValue() + 1);
                    activity.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                }
                if (activity instanceof Cook && Util.getInt(0, 100) > 10) {
                    attribute = character.getAttribute(SpecializationAttribute.COOKING);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("MAID.improvecooking", character));
                    activity.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                    attribute.setMaxValue(attribute.getMaxValue() + 1);
                }
            }
        }
    }

    public static class Chef
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof SellFood) {
                activity.setFameModifier(activity.getFameModifier() + 0.5f);
                for (Customer customer : activity.getCustomers()) {
                    if (customer.getStatus() != CustomerStatus.TIRED) continue;
                    customer.setStatus(CustomerStatus.LIVELY);
                }
            }
        }
    }

    public static class CulinaryDelights
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            House house;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Cook && (house = activity.getHouse()) != null) {
                for (Room room : house.getRooms()) {
                    for (Charakter target : room.getCurrentUsage().getCharacters()) {
                        float modification = 0.1f + (float)character.getAttribute(SpecializationAttribute.COOKING).getValue() / 150.0f;
                        if (modification >= 1.0f) {
                            modification = 1.0f;
                        }
                        activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.MOTIVATION, target));
                    }
                }
            }
        }
    }

    public static class MotherlyCare
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            House house;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Cook && (house = activity.getHouse()) != null) {
                for (Room room : house.getRooms()) {
                    for (Charakter target : room.getCurrentUsage().getCharacters()) {
                        activity.getAttributeModifications().add(new AttributeModification(7.0f, EssentialAttributes.ENERGY, target));
                        activity.getAttributeModifications().add(new AttributeModification(12.0f, EssentialAttributes.HEALTH, target));
                    }
                }
            }
        }
    }

    public static class Elegant
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                SpawnData spawnData;
                int skill;
                ArrayList<SpawnData.CustomerData> bonusCustomers;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity instanceof Clean) {
                    bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                    skill = character.getFinalValue(SpecializationAttribute.CLEANING) + character.getFinalValue(BaseAttributeTypes.CHARISMA);
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.CELEBRITY, (skill /= 3) / 30));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.LORD, skill / 25));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.MINORNOBLE, skill / 22));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.GROUP, skill / 18));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.BUSINESSMAN, skill / 20));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.MERCHANT, skill / 16));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.SOLDIER, skill / 14));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.PEASANT, skill / 12));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.BUM, skill / 10));
                    if (activity.getHouse() != null) {
                        spawnData = activity.getHouse().getSpawnData();
                        for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                            spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                        }
                    }
                }
                if (activity instanceof Cook) {
                    bonusCustomers = new ArrayList();
                    skill = character.getFinalValue(SpecializationAttribute.COOKING) + character.getFinalValue(BaseAttributeTypes.CHARISMA);
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.CELEBRITY, (skill /= 3) / 30));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.LORD, skill / 25));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.MINORNOBLE, skill / 22));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.GROUP, skill / 18));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.BUSINESSMAN, skill / 20));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.MERCHANT, skill / 16));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.SOLDIER, skill / 14));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.PEASANT, skill / 12));
                    bonusCustomers.add(new SpawnData.CustomerData(CustomerType.BUM, skill / 10));
                    if (activity.getHouse() != null) {
                        spawnData = activity.getHouse().getSpawnData();
                        for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                            spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                        }
                    }
                }
            }
        }
    }

    public static class HouseFairy
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && ((activity = (RunningActivity)e.getSource()) instanceof Clean || activity instanceof Cook)) {
                activity.getAttributeModifications().add(new AttributeModification(0.07f, BaseAttributeTypes.INTELLIGENCE, character));
            }
        }
    }

    public static class Professional
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                House house;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity instanceof Clean) {
                    Clean cleanActivity = (Clean)activity;
                    cleanActivity.setDirtModification(cleanActivity.getDirtModification() - 10.0f);
                }
                if (activity instanceof Cook && (house = activity.getHouse()) != null) {
                    for (Room room : house.getRooms()) {
                        for (Charakter target : room.getCurrentUsage().getCharacters()) {
                            activity.getAttributeModifications().add(new AttributeModification(10.0f, EssentialAttributes.ENERGY, target));
                            activity.getAttributeModifications().add(new AttributeModification(2.0f, EssentialAttributes.HEALTH, target));
                        }
                    }
                }
            }
        }
    }

    public static class WashingAndIroning
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            House house;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Clean && (house = activity.getHouse()) != null) {
                int skill = 10 + character.getFinalValue(SpecializationAttribute.CLEANING) / 4;
                if (skill > 50) {
                    skill = 50;
                }
                for (Room room : house.getRooms()) {
                    for (Charakter target : room.getCurrentUsage().getCharacters()) {
                        if (Util.getInt(0, 3) != 2) continue;
                        target.addCondition(new Buff.AllDolledUp(skill, target));
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("MAID.dolledup", (Person)character, target));
                    }
                }
            }
        }
    }

    public static class CordonBleu
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            House house;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Cook && (house = activity.getHouse()) != null) {
                int skill = 10 + character.getFinalValue(SpecializationAttribute.COOKING) / 4;
                if (skill > 50) {
                    skill = 50;
                }
                for (Room room : house.getRooms()) {
                    for (Charakter target : room.getCurrentUsage().getCharacters()) {
                        if (Util.getInt(0, 3) != 2) continue;
                        target.addCondition(new Buff.Satiated(skill, target));
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("MAID.cordonbleu", (Person)character, target));
                    }
                }
            }
        }
    }

    public static class DietExpert
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            House house;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Cook && (house = activity.getHouse()) != null) {
                for (Room room : house.getRooms()) {
                    for (Charakter target : room.getCurrentUsage().getCharacters()) {
                        activity.getAttributeModifications().add(new AttributeModification(0.02f, BaseAttributeTypes.CHARISMA, target));
                        activity.getAttributeModifications().add(new AttributeModification(0.02f, BaseAttributeTypes.STAMINA, target));
                    }
                }
            }
        }
    }

    public static class CompulsiveCleaner
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            House house;
            if (e.getType() == EventType.ACTIVITY && (house = (activity = (RunningActivity)e.getSource()).getHouse()) != null) {
                house.modDirt(-5 - character.getFinalValue(SpecializationAttribute.CLEANING) / 10);
            }
        }
    }

    public static class TimeManipulation
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                float change;
                float modification;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity instanceof Clean) {
                    character.getCounter().reset(CharacterStuffCounter.CounterNames.NOSLEEP.toString());
                    character.getCounter().add(CharacterStuffCounter.CounterNames.NOSLEEP.toString(), (Long)-1L);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("MAID.nap", character));
                    for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                        if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 2.0f;
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                }
                if (activity instanceof Cook) {
                    character.getCounter().reset(CharacterStuffCounter.CounterNames.NOSLEEP.toString());
                    character.getCounter().add(CharacterStuffCounter.CounterNames.NOSLEEP.toString(), (Long)-1L);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("MAID.nap", character));
                    for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                        if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                        modification = attributeModification.getBaseAmount();
                        change = Math.abs(modification) * 2.0f;
                        attributeModification.addModificator(Float.valueOf(change));
                    }
                }
            }
        }
    }
}

