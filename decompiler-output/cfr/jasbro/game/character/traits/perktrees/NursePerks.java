/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.BodyWrap;
import jasbro.game.character.activities.sub.Pamper;
import jasbro.game.character.activities.sub.business.Massage;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.SunEffect;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Item;
import jasbro.texts.TextUtil;

public class NursePerks {

    public static class SeXpert
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY && Util.getInt(0, 100) < 15) {
                int s = Util.getInt(0, 6);
                if (s == 0) {
                    character.getAttribute(Sextype.ANAL).addToValue(1.0f, true);
                }
                if (s == 1) {
                    character.getAttribute(Sextype.VAGINAL).addToValue(1.0f, true);
                }
                if (s == 2) {
                    character.getAttribute(Sextype.ORAL).addToValue(1.0f, true);
                }
                if (s == 3) {
                    character.getAttribute(Sextype.FOREPLAY).addToValue(1.0f, true);
                }
                if (s == 4) {
                    character.getAttribute(Sextype.TITFUCK).addToValue(1.0f, true);
                }
                if (s == 5) {
                    character.getAttribute(Sextype.GROUP).addToValue(1.0f, true);
                }
            }
        }
    }

    public static class BlessedAura
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            PlannedActivity activity;
            if (e.getType() == EventType.NEXTDAY && (activity = character.getActivity()).getSource() instanceof Room && Util.getInt(0, 100) < 15) {
                Room room = (Room)activity.getSource();
                House house = room.getHouse();
                for (Room room2 : house.getRooms()) {
                    for (Charakter chara : room2.getCurrentUsage().getCharacters()) {
                        if (chara.equals(character)) continue;
                        chara.getAttribute(EssentialAttributes.MOTIVATION).addToValue(10.0f);
                    }
                }
            }
        }
    }

    public static final class Soapy
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Massage) {
                Massage massageActivity = (Massage)activity;
                int rnd = Util.getInt(0, 100);
                if (rnd > 90) {
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage("\n" + TextUtil.t("SOAPY.like", (Person)character, activity.getMainCustomer()));
                    massageActivity.getMainCustomers().get(0).addToSatisfaction(70, trait);
                }
            }
        }
    }

    public static final class Oily
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Massage) {
                Massage massageActivity = (Massage)activity;
                int rnd = Util.getInt(0, 100);
                if (rnd > 75) {
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage("\n" + TextUtil.t("OILY.like"));
                    massageActivity.getMainCustomers().get(0).addToSatisfaction(30, trait);
                }
            }
        }
    }

    public static class LoveAndCare
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            PlannedActivity activity;
            if (e.getType() == EventType.NEXTDAY && (activity = character.getActivity()).getSource() instanceof Room) {
                Room room = (Room)activity.getSource();
                House house = room.getHouse();
                for (Room room2 : house.getRooms()) {
                    for (Charakter chara : room2.getCurrentUsage().getCharacters()) {
                        if (chara.equals(character)) continue;
                        for (Condition condition : chara.getConditions()) {
                            if (condition instanceof SunEffect && ((SunEffect)condition).isSunburn() && Util.getInt(0, 100) < 50) {
                                chara.removeCondition(condition);
                            }
                            if (condition instanceof Buff.RoughenedUp && Util.getInt(0, 100) < 50) {
                                chara.removeCondition(condition);
                            }
                            if (!(condition instanceof Buff.Exhausted) || Util.getInt(0, 100) >= 50) continue;
                            chara.removeCondition(condition);
                        }
                    }
                }
            }
        }
    }

    public static class Brewer
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                int chance = character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
                Item item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 1");
                chance += character.getFinalValue(SpecializationAttribute.MAGIC);
                chance += character.getFinalValue(BaseAttributeTypes.INTELLIGENCE);
                if (Util.getInt(0, 90) < (chance /= 6)) {
                    chance = Util.getInt(0, 100);
                    item = chance < 10 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 50 ? Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 3") : (chance < 20 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 50 ? Jasbro.getInstance().getItems().get("XX_JSbro_Potion Health 3") : (chance < 35 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 25 ? Jasbro.getInstance().getItems().get("XX_JSbro_Potion Health 2") : (chance < 50 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 25 ? Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 2") : (chance < 70 ? Jasbro.getInstance().getItems().get("XX_JSbro_Potion Health 1") : Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 1")))));
                    Jasbro.getInstance().getData().getInventory().addItems(item, 1);
                }
            }
        }
    }

    public static final class Dermatologist
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && ((activity = (RunningActivity)e.getSource()) instanceof Pamper || activity instanceof BodyWrap)) {
                for (Charakter other : activity.getCharacters()) {
                    if (other.equals(character)) continue;
                    Condition sunburn = null;
                    for (Condition condition : other.getConditions()) {
                        if (!(condition instanceof SunEffect) || !((SunEffect)condition).isSunburn()) continue;
                        sunburn = condition;
                    }
                    if (sunburn == null) continue;
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage(TextUtil.t("pamper.sunburn", (Person)character, other));
                    other.removeCondition(sunburn);
                }
            }
        }
    }
}

