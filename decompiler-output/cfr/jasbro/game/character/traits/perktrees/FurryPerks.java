/*
 * Decompiled with CFR 0.152.
 */
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

    public static class BeastInHeat
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                boolean heat = false;
                boolean pregnant = false;
                if (Jasbro.getInstance().getData().getDay() % 30 == 0) {
                    heat = true;
                    for (Condition con : character.getConditions()) {
                        if (!(con instanceof PregnancyInterface)) continue;
                        pregnant = true;
                        break;
                    }
                }
                List<Condition> list = character.getConditions();
                if (heat && !pregnant) {
                    character.addCondition(h1);
                    character.addCondition(new BattleCondition(character){

                        @Override
                        public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
                            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                                return currentValue + 10.0;
                            }
                            return currentValue;
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
                } else if (list.contains(h1)) {
                    character.addCondition(h2);
                    character.removeCondition(h1);
                    character.addCondition(new BattleCondition(character){

                        @Override
                        public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
                            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                                return currentValue + 20.0;
                            }
                            return currentValue;
                        }

                        @Override
                        public void handleEvent(MyEvent e) {
                            if (e.getType() == EventType.ACTIVITY) {
                                RunningActivity activity = (RunningActivity)e.getSource();
                                activity.getMainCustomer().addToSatisfaction((int)((double)activity.getMainCustomer().getSatisfactionAmount() * 0.75), this);
                            } else if (e.getType() == EventType.NEXTDAY) {
                                this.getCharacter().removeCondition(this);
                            }
                            super.handleEvent(e);
                        }
                    });
                } else if (list.contains(h2)) {
                    character.addCondition(h3);
                    character.removeCondition(h2);
                    character.addCondition(new BattleCondition(character){

                        @Override
                        public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
                            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                                return currentValue + 50.0;
                            }
                            if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
                                return currentValue + 10.0;
                            }
                            return currentValue;
                        }

                        @Override
                        public void handleEvent(MyEvent e) {
                            if (e.getType() == EventType.NEXTDAY) {
                                this.getCharacter().removeCondition(this);
                            } else if (e.getType() == EventType.ACTIVITY) {
                                RunningActivity activity = (RunningActivity)e.getSource();
                                activity.getMainCustomer().addToSatisfaction((int)((double)activity.getMainCustomer().getSatisfactionAmount() * 1.5), this);
                            }
                        }
                    });
                }
            }
        }
    }

    public static class Vulpine
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("VULPINESTAGE" + this.stage);
            switch (this.stage) {
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.BEASTINHEAT);
                }
            }
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            float value = attribute.getInternValue();
            float multiplier = (float)this.stage * 0.05f;
            if (attribute.getAttributeType() == SpecializationAttribute.SEDUCTION) {
                return value * multiplier;
            }
            return 0.0f;
        }

        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.CONTROL) {
                return currentValue * 2.0;
            }
            return currentValue;
        }

        @Override
        public boolean removeTrait(Charakter character) {
            Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
            Perks.PerkUtil.removeMaybe(character, Trait.BEASTINHEAT);
            return super.removeTrait(character);
        }
    }

    public static class ReptilianMotivation
    extends TraitEffect {
    }

    public static class ReptilianDownside
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity.isAbort()) {
                    return;
                }
                if (activity instanceof Whore && activity.getType() != ActivityType.TEASE) {
                    if (activity instanceof BusinessMainActivity) {
                        int chance;
                        int rnd;
                        BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                        if (businessMainActivity.getMainCustomers().size() > 0 && (rnd = Util.getInt(0, 100)) < (chance = 5)) {
                            MessageData message = activity.getMessages().get(0);
                            message.addToMessage(TextUtil.t("REPTILIAN.hit", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                            businessMainActivity.getMainCustomers().get(0).addToSatisfaction((int)(-character.getDamage() * 10.0f * 2.0f), trait);
                            activity.getAttributeModifications().add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
                            int chanceKo = (int)character.getDamage();
                            rnd = Util.getInt(0, 100);
                            if (rnd < chanceKo) {
                                activity.setAbort(true);
                                ArrayList<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
                                attributeModifications.add(new AttributeModification(-20.0f, EssentialAttributes.ENERGY, character));
                                attributeModifications.add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
                                message.addToMessage(TextUtil.t("REPTILIAN.hitKO", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                                message.setBackground(character.getBackground());
                                message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                                message.setAttributeModifications(attributeModifications);
                                message.createMessageScreen();
                            } else {
                                int chanceAbort;
                                rnd = Util.getInt(0, 100);
                                if (rnd < (chanceAbort = -businessMainActivity.getMainCustomers().get(0).getSatisfactionAmount() - (int)(character.getDamage() * 4.0f) - character.getCharisma() / 2)) {
                                    activity.setAbort(true);
                                    ArrayList<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
                                    attributeModifications.add(new AttributeModification(-20.0f, EssentialAttributes.ENERGY, character));
                                    attributeModifications.add(new AttributeModification(-10.0f, EssentialAttributes.HEALTH, character));
                                    attributeModifications.add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
                                    message.addToMessage(TextUtil.t("REPTILIAN.hitBack", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                                    message.setBackground(character.getBackground());
                                    message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                                    message.setAttributeModifications(attributeModifications);
                                    message.createMessageScreen();
                                } else {
                                    message.addToMessage(TextUtil.t("REPTILIAN.continue", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                                }
                            }
                        }
                    } else if (activity instanceof BusinessSecondaryActivity) {
                        // empty if block
                    }
                }
            }
        }
    }

    public static class ReptilianArmor
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.ARMORVALUE) {
                return currentValue * 1.2;
            }
            return currentValue;
        }
    }

    public static final class ExtractableClaws
    extends TraitEffect {
        @Override
        public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
            for (Equipment item : character.getCharacterInventory().listEquipment()) {
                if (item.getAccessoryType() != AccessoryType.ONEHANDED && item.getAccessoryType() != AccessoryType.TWOHANDED) continue;
                return;
            }
            if (attacks.get(0) instanceof Attack.StandardAttack) {
                attacks.remove(0);
            }
            attacks.add(new Attack.ClawAttack(character));
        }
    }

    public static final class FlameBreath
    extends TraitEffect {
        @Override
        public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
            attacks.add(new Attack.FlameBreath(character));
        }
    }

    public static class Reptilian
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("REPTILIANSTAGE" + this.stage);
            Attribute HP = character.getAttribute(EssentialAttributes.HEALTH);
            switch (this.stage) {
                case 3: {
                    if (HP.getMaxValue() >= 160) break;
                    HP.setMaxValue(HP.getMaxValue() + 60);
                    break;
                }
                case 4: {
                    if (HP.getMaxValue() >= 180) break;
                    HP.setMaxValue(HP.getMaxValue() + 20);
                    break;
                }
                case 5: {
                    if (HP.getMaxValue() >= 200) break;
                    HP.setMaxValue(HP.getMaxValue() + 20);
                }
            }
            switch (this.stage) {
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.FLAMEBREATH);
                }
                case 4: {
                    Perks.PerkUtil.addMaybe(character, Trait.REPTILIANMOTIVATION);
                }
                case 3: {
                    Perks.PerkUtil.addMaybe(character, Trait.REPTILIANARMOR);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.EXTRACTABLECLAWS);
                }
                case 1: {
                    Perks.PerkUtil.addMaybe(character, Trait.REPTILIANDOWNSIDE);
                }
            }
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            float value = attribute.getInternValue();
            float multiplier = (float)this.stage * 0.05f;
            if (attribute.getAttributeType() == BaseAttributeTypes.STRENGTH) {
                return value * multiplier;
            }
            if (attribute.getAttributeType() == EssentialAttributes.HEALTH) {
                // empty if block
            }
            return 0.0f;
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
                character.getAttribute(EssentialAttributes.HEALTH).setMaxValue(character.getAttribute(EssentialAttributes.HEALTH).getMaxValue() - 40 - this.stage * 20);
            }
            return super.removeTrait(character);
        }
    }

    public static class LagomorphOrgy
    extends TraitEffect {
    }

    public static class LagomorphHorny
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            int rnd;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.BARTEND && (rnd = Util.getRnd().nextInt(101)) < 51) {
                for (AttributeModification a : activity.getAttributeModifications()) {
                    if (a.getAttributeType() != EssentialAttributes.MOTIVATION) continue;
                    a.addModificator(Float.valueOf(2.0f));
                }
                activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LAGOMORPH.motivation", character));
            }
        }
    }

    public static class LagomorphEndurance
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Bartend) {
                for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                    if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) * 0.3f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
                Bartend bartend = (Bartend)activity;
                bartend.setBonus(bartend.getBonus() + 10);
            }
        }
    }

    public static class Lagomorph
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("LAGOMORPHSTAGE" + this.stage);
            switch (this.stage) {
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHORGY);
                }
                case 4: {
                    Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHHORNY);
                }
                case 3: {
                    Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHENDURANCE);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.LAGOMORPHQUICKY);
                }
            }
            if (e.getType() == EventType.ACTIVITY) {
                RunningActivity activity = (RunningActivity)e.getSource();
                List<AttributeModification> mods = activity.getAttributeModifications();
                boolean lago = false;
                float modification = 0.0f;
                for (AttributeModification mod : mods) {
                    if (mod.getAttributeType() != EssentialAttributes.MOTIVATION) continue;
                    modification = mod.getBaseAmount();
                    Room r = activity.getRoom();
                    if (r == null || r.getAmountPeople() <= 1) continue;
                    lago = true;
                }
                if (!lago) {
                    if (modification < 0.0f) {
                        activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.MOTIVATION, character));
                    }
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("LAGOMORPH.lonely", character));
                }
            }
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            float value = attribute.getInternValue();
            float multiplier = (float)this.stage * 0.05f;
            if (attribute.getAttributeType() == SpecializationAttribute.BARTENDING) {
                return value * multiplier;
            }
            return 0.0f;
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

    public static class InsectsEverywhere
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.CLEAN) {
                Clean c = (Clean)activity;
                float dirt = c.getDirtModification();
                c.setDirtModification(0.0f);
                List<House> houses = Jasbro.getInstance().getData().getHouses();
                int nrHouses = houses.size();
                int dph = (int)(dirt / (float)nrHouses);
                for (House h : houses) {
                    h.modDirt(dph);
                }
            }
        }
    }

    public static class InsectEfficiency
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.CLEAN) {
                int insects = 0;
                for (Room r : activity.getHouse().getRooms()) {
                    for (Charakter c : r.getCurrentUsage().getCharacters()) {
                        if (!c.getTraits().contains(Trait.INSECT)) continue;
                        ++insects;
                    }
                }
                Clean clean = (Clean)activity;
                float dirt = clean.getDirtModification();
                dirt += dirt * 0.1f * (float)insects;
                clean.setDirtModification(dirt);
            }
        }
    }

    public static class InsectGather
    extends TraitEffect {
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
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("INSECT.found", (Person)character, foundItem.getName()));
                    } else {
                        System.err.println("Error: No Ingredient could be found to be gathered.");
                    }
                }
            }
        }
    }

    public static class InsectResilience
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() != ActivityType.FIGHT) {
                for (AttributeModification mod : activity.getAttributeModifications()) {
                    if (mod.getAttributeType() != EssentialAttributes.HEALTH || !(mod.getBaseAmount() < 100.0f)) continue;
                    mod.setBaseAmount(0.0f);
                }
            }
        }
    }

    public static class Insect
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("INSECTSTAGE" + this.stage);
            switch (this.stage) {
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.INSECTSEVERYWHERE);
                }
                case 4: {
                    Perks.PerkUtil.addMaybe(character, Trait.INSECTEFFICIENCY);
                }
                case 3: {
                    Perks.PerkUtil.addMaybe(character, Trait.INSECTGATHER);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.INSECTRESILIENCE);
                }
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
            float multiplier = (float)this.stage * 0.05f;
            if (attribute.getAttributeType() == SpecializationAttribute.CLEANING) {
                return value * multiplier;
            }
            if (attribute.getAttributeType() == SpecializationAttribute.COOKING) {
                return value * multiplier;
            }
            return 0.0f;
        }
    }

    public static class CatNap
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Sleep) {
                activity.getAttributeModifications().add(new AttributeModification(15.0f, EssentialAttributes.ENERGY, character));
                activity.getAttributeModifications().add(new AttributeModification(10.0f, EssentialAttributes.HEALTH, character));
            }
        }
    }

    public static class Nocturnal
    extends TraitEffect {
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

    public static class FelineStrip
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.STRIP) {
                Strip strip = (Strip)activity;
                strip.setBonus(strip.getBonus() + 5);
            }
        }
    }

    public static class Feline
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("FELINESTAGE" + this.stage);
            switch (this.stage) {
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.FELINEHEAT);
                }
                case 4: {
                    Perks.PerkUtil.addMaybe(character, Trait.FELINESTRIP);
                }
                case 3: {
                    Perks.PerkUtil.addMaybe(character, Trait.NOCTURNAL);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.CATNAP);
                }
            }
            if (this.stage >= 4 && e.getType() == EventType.ACTIVITYCREATED && (activity = (RunningActivity)e.getSource()) instanceof Strip) {
                Strip strip = (Strip)activity;
                strip.setBonus(strip.getBonus() + 5);
            }
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            float value = attribute.getInternValue();
            float multiplier1 = (float)this.stage * 0.05f;
            float multiplier2 = -((float)this.stage * 0.1f);
            if (attribute.getAttributeType() == SpecializationAttribute.STRIP) {
                return value * multiplier1;
            }
            if (attribute.getAttributeType() == BaseAttributeTypes.OBEDIENCE) {
                boolean fed = false;
                List<Condition> temp = attribute.getCharacter().getConditions();
                for (Condition con : temp) {
                    if (!(con instanceof Buff.Satiated)) continue;
                    fed = true;
                }
                if (!fed) {
                    return value * multiplier2;
                }
            }
            return 0.0f;
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

    public static class CanineCommand
    extends TraitEffect {
        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (attribute.getAttributeType() == BaseAttributeTypes.COMMAND) {
                return attribute.getInternValue() * 0.25f;
            }
            return 0.0f;
        }

        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.CONTROL && currentValue < 0.0) {
                double temp = Math.abs(currentValue) * 0.75;
                return currentValue + temp;
            }
            return currentValue;
        }
    }

    public static class CanineSleep
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.SLEEP && activity.getRoom().getAmountPeople() < 2) {
                float modification = 0.0f;
                for (AttributeModification mod : activity.getAttributeModifications()) {
                    if (mod.getAttributeType() != EssentialAttributes.ENERGY) continue;
                    modification = mod.getBaseAmount();
                }
                modification = (float)((double)modification * -1.3);
                activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.ENERGY, character));
                activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CANINE.sleep", character));
            }
        }
    }

    public static class Canine
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("CANINESTAGE" + this.stage);
            switch (this.stage) {
                case 4: 
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.CANINECOMMAND);
                }
            }
            Perks.PerkUtil.addMaybe(character, Trait.CANINESLEEP);
        }

        @Override
        public boolean removeTrait(Charakter character) {
            Perks.PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
            return super.removeTrait(character);
        }
    }

    public static class Bovine
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("BOVINESTAGE" + this.stage);
            switch (this.stage) {
                default: 
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
                float multiplier = (float)this.stage * 0.5f;
                return value * multiplier;
            }
            return 0.0f;
        }
    }

    public static class AvianBirdbrain
    extends TraitEffect {
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

    public static class AvianPickup
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            int chance;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.ADVERTISE && (chance = Util.getInt(0, 100)) > 50) {
                List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.LOOT);
                int item = Util.getInt(0, loot.size());
                Item foundItem = loot.get(item);
                Jasbro.getInstance().getData().getInventory().addItem(foundItem);
                activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AVIAN.found", (Person)character, foundItem.getName()));
            }
        }
    }

    public static class Avian
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("AVIANSTAGE" + this.stage);
            switch (this.stage) {
                case 3: 
                case 4: 
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.AVIANDRAG);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.AVIANPICKUP);
                }
                case 1: {
                    Perks.PerkUtil.addMaybe(character, Trait.AVIANFLIGHT);
                }
            }
            Perks.PerkUtil.addMaybe(character, Trait.AVIANBIRDBRAIN);
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
                float multiplier = (float)this.stage * 0.5f;
                return value * multiplier;
            }
            return 0.0f;
        }
    }

    public static class ArachnidSleep
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()).getType() == ActivityType.SLEEP) {
                List<Charakter> chars = activity.getCharacters();
                for (Charakter c : chars) {
                    if (c.getTraits().contains(Trait.ARACHNID)) continue;
                    c.addCondition(new Buff.BadDreams(c));
                }
            }
        }
    }

    public static class InhumanPregnancy
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                return currentValue + 10.0;
            }
            return currentValue;
        }
    }

    public static class Oviposition
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.NEXTDAY) {
                boolean notPregnant = true;
                for (Condition condition : character.getConditions()) {
                    if (!(condition instanceof PregnancyInterface)) continue;
                    notPregnant = false;
                    break;
                }
                if (notPregnant && Util.getInt(0, 100) < 6) {
                    character.addCondition(new OvipositionPregnancy());
                    MessageData messageData = new MessageData(TextUtil.t("oviposition.message"), ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character), character.getBackground(), true);
                    messageData.createMessageScreen();
                }
            }
        }
    }

    public static class HeartOfTheSwarm
    extends TraitEffect {
        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                return currentValue + 30.0;
            }
            if (calculatedAttribute == CalculatedAttribute.CHANCEADDITIONALCHILD) {
                return currentValue + 20.0;
            }
            return currentValue;
        }
    }

    public static class Arachnid
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("ARACHNIDSTAGE" + this.stage);
            switch (this.stage) {
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.AUTONOMOUSPERK);
                }
                case 4: {
                    Perks.PerkUtil.addMaybe(character, Trait.HEARTOFTHESWARM);
                }
                case 3: {
                    Perks.PerkUtil.addMaybe(character, Trait.OVIPOSITION);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.INHUMANPREGNANCY);
                }
            }
            Perks.PerkUtil.addMaybe(character, Trait.ARACHNIDSLEEP);
        }

        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
                return currentValue + 5.0 + (double)(5 * this.stage);
            }
            return currentValue;
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

    public static class RelentlessBeast
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ENERGYZERO && !e.isCancelled()) {
                e.setCancelled(true);
                AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent)e;
                RunningActivity activity = attributeChangedEvent.getActivity();
                if (activity != null) {
                    activity.getAttributeModifications().add(new AttributeModification(-10.0f, EssentialAttributes.HEALTH, character));
                } else {
                    character.getAttribute(EssentialAttributes.HEALTH).addToValue(-10.0f);
                }
            }
            if (e.getType() == EventType.STATUSCHANGE) {
                for (Condition con : character.getConditions()) {
                    if (!(con instanceof Illness)) continue;
                    character.removeCondition(con);
                }
            }
        }
    }

    public static class AquaticNurse
    extends TraitEffect {
    }

    public static class AquaticDownside
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            boolean dosomething = false;
            boolean dry = true;
            if (e.getType() == EventType.NEXTDAY) {
                dosomething = true;
                dry = true;
            } else if (e.getType() == EventType.ACTIVITY && ((activity = (RunningActivity)e.getSource()).getType() == ActivityType.SWIM || activity.getType() == ActivityType.SOAK || activity.getType() == ActivityType.BATHATTENDANT || activity.getType() == ActivityType.BATHE)) {
                dosomething = true;
                dry = false;
            }
            if (dosomething) {
                boolean found = false;
                for (Condition con : character.getConditions()) {
                    if (!(con instanceof Buff.AquaticTrait)) continue;
                    int stage = ((Buff.AquaticTrait)con).getStage();
                    if (dry) {
                        if (stage > 1) {
                            --stage;
                        }
                    } else if (stage < 5) {
                        ++stage;
                    }
                    character.removeCondition(con);
                    character.addCondition(new Buff.AquaticTrait(character, stage));
                    found = true;
                    break;
                }
                if (!found) {
                    character.addCondition(new Buff.AquaticTrait(character, 3));
                }
            }
        }
    }

    public static class AquaticSwim
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Swim) {
                for (AttributeModification a : activity.getAttributeModifications()) {
                    if (a.getAttributeType() == EssentialAttributes.ENERGY) {
                        a.addModificator(Float.valueOf(Math.abs(a.getBaseAmount()) + 10.0f));
                    }
                    if (a.getAttributeType() != EssentialAttributes.HEALTH) continue;
                    a.addModificator(Float.valueOf(5.0f));
                }
                int chance = Util.getInt(0, 100);
                if (chance > 50) {
                    List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.LOOT);
                    int item = Util.getInt(0, loot.size());
                    Item foundItem = loot.get(item);
                    Jasbro.getInstance().getData().getInventory().addItem(foundItem);
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AQUATIC.found", (Person)character, foundItem.getName()));
                }
            }
        }
    }

    public static class Aquatic
    extends TraitEffect {
        int stage = 0;
        int stagelimit = 5;
        int stagestep = 10;

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();
            this.stage = (int)(value / (float)this.stagestep);
            if (this.stage > this.stagelimit) {
                this.stage = this.stagelimit;
            }
            Perks.PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("AQUATICSTAGE" + this.stage);
            switch (this.stage) {
                case 4: 
                case 5: {
                    Perks.PerkUtil.addMaybe(character, Trait.RELENTLESSBEAST);
                }
                case 3: {
                    Perks.PerkUtil.addMaybe(character, Trait.AQUATICNURSE);
                }
                case 2: {
                    Perks.PerkUtil.addMaybe(character, Trait.AQUATICSWIM);
                }
            }
            Perks.PerkUtil.addMaybe(character, Trait.AQUATICDOWNSIDE);
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
            float multiplier = (float)this.stage * 0.05f;
            if (attribute.getAttributeType() == SpecializationAttribute.MEDICALKNOWLEDGE) {
                return value * multiplier;
            }
            if (attribute.getAttributeType() == SpecializationAttribute.MAGIC) {
                return value * multiplier;
            }
            return 0.0f;
        }
    }

    public static class BestialFeatures
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (!character.getTraits().contains(Trait.FURRY)) {
                character.addTrait(Trait.BESTIAL);
            }
            if (character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue() < 10.0f) {
                character.getAttribute(SpecializationAttribute.TRANSFORMATION).setInternValue(10.0f);
            }
        }
    }
}

