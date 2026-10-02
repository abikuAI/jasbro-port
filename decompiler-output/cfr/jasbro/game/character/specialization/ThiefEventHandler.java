/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.specialization;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.MyCharacterEventListener;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.game.items.ItemType;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class ThiefEventHandler
implements MyCharacterEventListener {
    @Override
    public void handleEvent(MyEvent e, Charakter character) {
        RunningActivity activity;
        if (e.getType() == EventType.ACTIVITYPERFORMED && (activity = (RunningActivity)e.getSource()).getType().isCustomerDependent() && activity.getType() != ActivityType.SUBMITTOMONSTER && activity.getType() != ActivityType.TEASE && activity.getType() != ActivityType.PUBLICUSE) {
            int stealChance = character.getStealChance();
            int stealAmount = character.getStealAmountModifier();
            int stealItemChance = character.getStealItemChance();
            if (activity.getMainCustomer() != null) {
                List<Customer> customers = activity.getMainCustomers();
                int amountStolen = 0;
                activity.getMessages().get(0).addToMessage("\n");
                for (Customer customer : customers) {
                    Object[] arguments;
                    Inventory.ItemData stolenItem = null;
                    if (Util.getInt(0, 100) < stealItemChance && (stolenItem = customer.getItem()) != null) {
                        if (character.getTraits().contains(Trait.RESELLER) && stolenItem.getItem().getType() != ItemType.UNLOCK) {
                            Jasbro.getInstance().getData().earnMoney(stolenItem.getItem().getValue() / 2, stolenItem);
                        } else {
                            Jasbro.getInstance().getData().getInventory().addItems(stolenItem.getItem(), stolenItem.getAmount());
                        }
                    }
                    int actualStealChance = stealChance + customer.getInitialSatisfaction() / 5;
                    if (Util.getInt(0, 100) < actualStealChance) {
                        if ((amountStolen += customer.payFixed((int)((float)(stealAmount * 10) + (float)(customer.getMoney() * stealAmount) / 100.0f))) > 0) {
                            Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
                            if (stolenItem == null) {
                                arguments = new Object[]{amountStolen};
                                if (customer.getMoney() <= 0) {
                                    activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer.all", (Person)character, (Person)customer, arguments));
                                } else {
                                    activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer", (Person)character, (Person)customer, arguments));
                                }
                            } else {
                                arguments = new Object[]{stolenItem, amountStolen};
                                if (customer.getMoney() <= 0) {
                                    activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer.item.all", (Person)character, (Person)customer, arguments));
                                } else {
                                    activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer.item", (Person)character, (Person)customer, arguments));
                                }
                            }
                        } else if (stolenItem == null) {
                            activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer.noMoney", (Person)character, customer));
                        } else {
                            arguments = new Object[]{stolenItem};
                            activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer.item.noMoney", (Person)character, (Person)customer, arguments));
                        }
                        if (activity instanceof Whore) {
                            activity.getAttributeModifications().add(new AttributeModification(0.2f, SpecializationAttribute.PICKPOCKETING, character));
                            continue;
                        }
                        activity.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.PICKPOCKETING, character));
                        continue;
                    }
                    if (stolenItem != null) {
                        arguments = new Object[]{stolenItem};
                        activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.mainCustomer.itemOnly", (Person)character, (Person)customer, arguments));
                    }
                    if (activity instanceof Whore) {
                        activity.getAttributeModifications().add(new AttributeModification(0.05f, SpecializationAttribute.PICKPOCKETING, character));
                        continue;
                    }
                    activity.getAttributeModifications().add(new AttributeModification(0.2f, SpecializationAttribute.PICKPOCKETING, character));
                }
            } else if (activity.getCustomers().size() != 0 && activity.getType() != ActivityType.SUBMITTOMONSTER && activity.getType() != ActivityType.PUBLICUSE) {
                activity.getMessages().get(0).addToMessage("\n");
                stealItemChance /= 5;
                ArrayList<Inventory.ItemData> loot = new ArrayList<Inventory.ItemData>();
                int amountStolen = 0;
                int amountPeople = 0;
                for (Customer customer : activity.getCustomers()) {
                    Inventory.ItemData itemStolen;
                    if (Util.getInt(0, 100) < stealChance) {
                        amountStolen += customer.pay((int)((float)Util.getInt(1, 6) + customer.getImportance() * (float)stealAmount / 100.0f), 1.0f);
                        ++amountPeople;
                    }
                    if (Util.getInt(0, 100) >= stealItemChance + customer.getInitialSatisfaction() / 5 || (itemStolen = customer.getItem()) == null) continue;
                    loot.add(itemStolen);
                }
                if (loot.size() > 0) {
                    if (character.getTraits().contains(Trait.RESELLER)) {
                        for (int i = 0; i < loot.size(); ++i) {
                            Jasbro.getInstance().getData().earnMoney(((Inventory.ItemData)loot.get(i)).getItem().getValue() / 2, loot.get(i));
                        }
                    } else {
                        Jasbro.getInstance().getData().getInventory().addItems(loot);
                    }
                    Object[] arguments = new Object[]{TextUtil.listItems(loot), amountStolen};
                    Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
                    activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.secondaryCustomersPlusLoot", (Person)character, arguments));
                    activity.getAttributeModifications().add(new AttributeModification(1.0f, SpecializationAttribute.PICKPOCKETING, character));
                } else if (amountStolen > 0) {
                    Object[] arguments = new Object[]{amountPeople, amountStolen};
                    Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
                    activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.secondaryCustomers", (Person)character, arguments));
                    activity.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.PICKPOCKETING, character));
                } else {
                    activity.getAttributeModifications().add(new AttributeModification(0.05f, SpecializationAttribute.PICKPOCKETING, character));
                }
            }
        }
    }
}

