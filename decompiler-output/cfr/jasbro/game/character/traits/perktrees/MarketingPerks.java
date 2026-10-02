/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Advertise;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.housing.House;
import java.util.ArrayList;

public class MarketingPerks {

    public static class TargetGroups
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 50;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.GROUP, skill));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetCelebrities
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 50;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.CELEBRITY, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetLords
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 45;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.LORD, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetNobles
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 40;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.MINORNOBLE, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetBusinessmen
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 35;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.BUSINESSMAN, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetSoldiers
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 30;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.SOLDIER, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetPeasants
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 25;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.PEASANT, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class TargetBum
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                ArrayList<SpawnData.CustomerData> bonusCustomers = new ArrayList<SpawnData.CustomerData>();
                ArrayList listHouses = new ArrayList();
                int skill = character.getFinalValue(SpecializationAttribute.ADVERTISING) / 20;
                bonusCustomers.add(new SpawnData.CustomerData(CustomerType.BUM, skill += character.getFinalValue(BaseAttributeTypes.CHARISMA) / 10));
                for (House house : listHouses) {
                    SpawnData spawnData = house.getSpawnData();
                    for (SpawnData.CustomerData bonusCustData : bonusCustomers) {
                        spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                    }
                }
            }
        }
    }

    public static class Spirited
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise && character.getEnergy() > 80) {
                Advertise advertiseActivity = (Advertise)activity;
                advertiseActivity.setStartingEffectiveness(advertiseActivity.getStartingEffectiveness() + 10);
                activity.getAttributeModifications().add(new AttributeModification(0.1f, EssentialAttributes.MOTIVATION, character));
            }
        }
    }

    public static class OverwriteOral
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.ORAL);
                }
            }
        }
    }

    public static class OverwriteTitfuck
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.TITFUCK);
                }
            }
        }
    }

    public static class OverwriteForeplay
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.FOREPLAY);
                }
            }
        }
    }

    public static class OverwriteAnal
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.ANAL);
                }
            }
        }
    }

    public static class OverwriteVaginal
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.VAGINAL);
                }
            }
        }
    }

    public static class OverwriteMonster
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.MONSTER);
                }
            }
        }
    }

    public static class OverwriteBondage
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    if (Util.getInt(1, 20) != 5 || customer.getType() == CustomerType.GROUP) continue;
                    customer.setPreferredSextype(Sextype.BONDAGE);
                }
            }
        }
    }

    public static class Salesperson
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Advertise) {
                Advertise advertiseActivity = (Advertise)activity;
                advertiseActivity.setStartingEffectiveness(advertiseActivity.getStartingEffectiveness() + 10);
            }
        }
    }

    public static class Recognized
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent event, Charakter character, Trait trait) {
            if (event.getType() == EventType.CUSTOMERSARRIVE) {
                CustomersArriveEvent customerEvent = (CustomersArriveEvent)event;
                for (Customer customer : customerEvent.getCustomers()) {
                    customer.setInitialMoney((int)((double)customer.getInitialMoney() * 1.2));
                }
            }
        }
    }
}

