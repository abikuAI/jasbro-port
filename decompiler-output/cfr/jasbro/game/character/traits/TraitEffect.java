/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.business.SubmitToMonster;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public abstract class TraitEffect {
    private static final float ATTRIBUTEMODIFICATOR = 0.3f;

    public void handleEvent(MyEvent e, Charakter character, Trait trait) {
    }

    public boolean removeTrait(Charakter character) {
        return true;
    }

    public boolean addTrait(Charakter character) {
        return true;
    }

    public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
        return curMinObedience;
    }

    public SkillTree getSkillTree() {
        return null;
    }

    public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
        return currentValue;
    }

    public int modifyCustomerRating(int initialRating, Customer customer, BusinessMainActivity businessMainActivity) {
        return initialRating;
    }

    public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
    }

    public float getAttributeModifier(Attribute attribute) {
        return 0.0f;
    }

    public void morphRandom(Charakter character) {
        ArrayList<String> morphType = new ArrayList<String>();
        character.addTrait(Trait.BESTIALFEATURES);
        if (character.getTraits().contains(Trait.MORPHCANINE)) {
            morphType.add("CANINE");
        }
        if (character.getTraits().contains(Trait.MORPHFELINE)) {
            morphType.add("FELINE");
        }
        if (character.getTraits().contains(Trait.MORPHVULPINE)) {
            morphType.add("VULPINE");
        }
        if (character.getTraits().contains(Trait.MORPHREPTILIAN)) {
            morphType.add("REPTILIAN");
        }
        if (character.getTraits().contains(Trait.MORPHAVIAN)) {
            morphType.add("AVIAN");
        }
        if (character.getTraits().contains(Trait.MORPHAQUATIC)) {
            morphType.add("AQUATIC");
        }
        if (character.getTraits().contains(Trait.MORPHINSECT)) {
            morphType.add("INSECT");
        }
        if (character.getTraits().contains(Trait.MORPHLAGOMORPH)) {
            morphType.add("LAGOMORPH");
        }
        if (morphType.size() <= 0) {
            return;
        }
        int random = morphType.size();
        int choice = Util.getInt(0, random);
        String chosenType = (String)morphType.get(choice);
        if (chosenType == "FELINE") {
            character.addTrait(Trait.FELINE);
        } else if (chosenType == "CANINE") {
            character.addTrait(Trait.CANINE);
        } else if (chosenType == "VULPINE") {
            character.addTrait(Trait.VULPINE);
        } else if (chosenType == "REPTILIAN") {
            character.addTrait(Trait.REPTILIAN);
        } else if (chosenType == "AVIAN") {
            character.addTrait(Trait.AVIAN);
        } else if (chosenType == "AQUATIC") {
            character.addTrait(Trait.AQUATIC);
        } else if (chosenType == "INSECT") {
            character.addTrait(Trait.INSECT);
        } else if (chosenType == "ARACHNID") {
            character.addTrait(Trait.ARACHNID);
        } else if (chosenType == "LAGOMORPH") {
            character.addTrait(Trait.LAGOMORPH);
        }
        character.removeTrait(Trait.MORPHFELINE);
        character.removeTrait(Trait.MORPHCANINE);
        character.removeTrait(Trait.MORPHVULPINE);
        character.removeTrait(Trait.MORPHREPTILIAN);
        character.removeTrait(Trait.MORPHAVIAN);
        character.removeTrait(Trait.MORPHAQUATIC);
        character.removeTrait(Trait.MORPHINSECT);
        character.removeTrait(Trait.MORPHLAGOMORPH);
    }

    public static class CancelAttributeLoss
    extends TraitEffect {
        private AttributeType attributeType;

        public CancelAttributeLoss(AttributeType attributeType) {
            this.attributeType = attributeType;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == this.attributeType && attributeModification.getBaseAmount() < 0.0f) {
                attributeModification.setCancelled(true);
            }
        }
    }

    public static class InfluenceAttributeLoss
    extends TraitEffect {
        private AttributeType attributeType;
        private float attributeModifier;

        public InfluenceAttributeLoss(AttributeType attributeType, float attributeModifier) {
            this.attributeType = attributeType;
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == this.attributeType && attributeModification.getBaseAmount() < 0.0f) {
                float modification = attributeModification.getBaseAmount();
                float change = modification * this.attributeModifier;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Unsellable
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.UNSELLABLE)) {
                character.removeTrait(Trait.UNSELLABLE);
            }
        }
    }

    public static final class Nympho
    extends TraitEffect {
        @Override
        public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
            if (activity != null) {
                if (activity instanceof Whore || activity instanceof Sex || activity instanceof Orgy) {
                    return curMinObedience - 2;
                }
                return curMinObedience;
            }
            return curMinObedience;
        }

        @Override
        public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
            if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
                return currentValue + 10.0;
            }
            return currentValue;
        }
    }

    public static final class Feisty
    extends TraitEffect {
        @Override
        public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
            return curMinObedience + 2;
        }
    }

    public static final class Obedient
    extends TraitEffect {
        @Override
        public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
            return curMinObedience - 2;
        }
    }

    public static final class Tsundere
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
                        BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                        if (businessMainActivity.getMainCustomers().size() > 0) {
                            int rnd = Util.getInt(0, 100);
                            int chance = 25 - character.getObedience() * 2;
                            if (businessMainActivity instanceof Whore) {
                                chance += 10;
                            }
                            if (chance < 1) {
                                chance = 1;
                            }
                            if (rnd < chance) {
                                MessageData message = activity.getMessages().get(0);
                                message.addToMessage(TextUtil.t("TSUNDERE.hit", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                                businessMainActivity.getMainCustomers().get(0).addToSatisfaction((int)(-character.getDamage() * 10.0f * 2.0f), trait);
                                activity.getAttributeModifications().add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
                                int chanceKo = (int)(character.getDamage() * 10.0f);
                                rnd = Util.getInt(0, 100);
                                if (rnd < chanceKo) {
                                    activity.setAbort(true);
                                    ArrayList<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
                                    attributeModifications.add(new AttributeModification(-20.0f, EssentialAttributes.ENERGY, character));
                                    attributeModifications.add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
                                    message.addToMessage(TextUtil.t("TSUNDERE.hitKO", (Person)character, businessMainActivity.getMainCustomers().get(0)));
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
                                        message.addToMessage(TextUtil.t("TSUNDERE.hitBack", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                                        message.setBackground(character.getBackground());
                                        message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                                        message.setAttributeModifications(attributeModifications);
                                        message.createMessageScreen();
                                    } else {
                                        message.addToMessage(TextUtil.t("TSUNDERE.continue", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                                    }
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

    public static final class Wild
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ATTRIBUTECHANGE) {
                float change;
                float modification;
                AttributeModification attributeModification = (AttributeModification)e.getSource();
                if (attributeModification.getAttributeType() == Sextype.MONSTER || attributeModification.getAttributeType() == BaseAttributeTypes.STAMINA || attributeModification.getAttributeType() == BaseAttributeTypes.STRENGTH) {
                    modification = attributeModification.getBaseAmount();
                    change = Math.abs(modification) * 0.3f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
                if (attributeModification.getAttributeType() == BaseAttributeTypes.OBEDIENCE) {
                    modification = attributeModification.getBaseAmount();
                    change = -Math.abs(modification) * 0.3f;
                    attributeModification.addModificator(Float.valueOf(change));
                }
            } else if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity) {
                BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                if (activity instanceof SubmitToMonster) {
                    if (businessMainActivity.getMainCustomers().size() == 1) {
                        MessageData message = activity.getMessages().get(0);
                        message.addToMessage("\n" + TextUtil.t("WILD.like", character));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(10, trait);
                        for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                            float change;
                            float modification;
                            if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                                modification = attributeModification.getBaseAmount();
                                change = Math.abs(modification) * 0.2f;
                                attributeModification.addModificator(Float.valueOf(change));
                            }
                            if (attributeModification.getAttributeType() != EssentialAttributes.ENERGY) continue;
                            modification = attributeModification.getBaseAmount();
                            change = Math.abs(modification) * 0.1f;
                            attributeModification.addModificator(Float.valueOf(change));
                        }
                    }
                } else if (activity instanceof Whore) {
                    Whore whoreActivity = (Whore)activity;
                    whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.2f);
                    int rnd = Util.getInt(0, 100);
                    if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.STRONGSTATUS && rnd < 40 && whoreActivity.getSextype() != Sextype.ORAL && whoreActivity.getSextype() != Sextype.FOREPLAY && whoreActivity.getSextype() != Sextype.TITFUCK) {
                        whoreActivity.getMessages().get(0).addToMessage("\n" + TextUtil.t("WILD.strongcustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(7, trait);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.LIVELY && rnd < 40) {
                        whoreActivity.getMessages().get(0).addToMessage("\n" + TextUtil.t("WILD.livelycustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(7, trait);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.TIRED && rnd < 40) {
                        whoreActivity.getMessages().get(0).addToMessage("\n" + TextUtil.t("WILD.tiredcustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(15, trait);
                        whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.1f);
                    }
                }
            }
        }
    }

    public static final class Fragile
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity) {
                BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                if (activity instanceof SubmitToMonster) {
                    if (businessMainActivity.getMainCustomers().size() == 1) {
                        character.getAttribute(EssentialAttributes.HEALTH).addToValue(-15.0f, activity);
                        character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0f, activity);
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("FRAGILE.monster", character));
                        character.addCondition(new Buff.RoughenedUp());
                    }
                } else if (activity instanceof Whore) {
                    Whore whoreActivity = (Whore)activity;
                    ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2f);
                    int rnd = Util.getInt(0, 100);
                    if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.STRONGSTATUS && rnd < 50) {
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("FRAGILE.strongcustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                        character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0f, activity);
                        character.addCondition(new Buff.RoughenedUp());
                        ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2f);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.LIVELY && rnd < 40) {
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("FRAGILE.livelycustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                        character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0f, activity);
                        ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.1f);
                    }
                    if ((whoreActivity.getSexType() == Sextype.BONDAGE || whoreActivity.getSexType() == Sextype.GROUP) && businessMainActivity.getMainCustomers().size() > 0) {
                        character.getAttribute(EssentialAttributes.HEALTH).addToValue(-7.0f, activity);
                        character.getAttribute(EssentialAttributes.ENERGY).addToValue(-7.0f, activity);
                        activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("FRAGILE.kinky", character));
                        character.addCondition(new Buff.RoughenedUp());
                        ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2f);
                    }
                }
            }
        }
    }

    public static final class Slut
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            block2: {
                RunningActivity activity;
                block3: {
                    BusinessMainActivity businessMainActivity;
                    block4: {
                        if (e.getType() != EventType.ACTIVITY) break block2;
                        activity = (RunningActivity)e.getSource();
                        if (!(activity instanceof BusinessMainActivity)) break block3;
                        businessMainActivity = (BusinessMainActivity)((Object)activity);
                        if (!(activity instanceof Whore)) break block2;
                        if (activity.getMainCustomer().getType() != CustomerType.GROUP && activity.getMainCustomer().getType() != CustomerType.CELEBRITY && activity.getMainCustomer().getType() != CustomerType.LORD && activity.getMainCustomer().getType() != CustomerType.MINORNOBLE && activity.getMainCustomer().getType() != CustomerType.BUSINESSMAN) break block4;
                        ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2f);
                        ((Whore)activity).setExecutionModifier(((Whore)activity).getExecutionModifier() + 0.1f);
                        MessageData message = activity.getMessages().get(0);
                        message.addToMessage("\n" + TextUtil.t("SLUT.rich", (Person)character, activity.getMainCustomer()));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(10, trait);
                        break block2;
                    }
                    if (activity.getMainCustomer().getType() != CustomerType.BUM && activity.getMainCustomer().getType() != CustomerType.PEASANT) break block2;
                    MessageData message = activity.getMessages().get(0);
                    ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() - 0.2f);
                    ((Whore)activity).setExecutionModifier(((Whore)activity).getExecutionModifier() - 0.1f);
                    message.addToMessage("\n" + TextUtil.t("SLUT.poor", (Person)character, activity.getMainCustomer()));
                    businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-20, trait);
                    break block2;
                }
                if (activity instanceof Strip && activity.getCustomers().size() >= 20) {
                    activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SLUT.strip", character));
                    for (Customer customer : activity.getCustomers()) {
                        customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.STRIP) / 15, activity);
                    }
                }
            }
        }
    }

    public static final class Nicebody
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == SpecializationAttribute.STRIP) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class SensualTongue
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.ORAL) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Biter
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.ORAL) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class SingleMinded
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.GROUP) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Multifaceted
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.GROUP) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Numb
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.FOREPLAY) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Sensitive
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.FOREPLAY) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class DeadFish
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && ((attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.VAGINAL || attributeModification.getAttributeType() == Sextype.ANAL)) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.15f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class AmbitousLover
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && ((attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.VAGINAL || attributeModification.getAttributeType() == Sextype.ANAL)) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * 0.15f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Reserved
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && ((attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.GROUP || attributeModification.getAttributeType() == Sextype.MONSTER || attributeModification.getAttributeType() == Sextype.BONDAGE)) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class OpenMinded
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && ((attributeModification = (AttributeModification)e.getSource()).getAttributeType() == Sextype.GROUP || attributeModification.getAttributeType() == Sextype.MONSTER || attributeModification.getAttributeType() == Sextype.BONDAGE)) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class Uninhibited
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == SpecializationAttribute.SEDUCTION) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity && activity instanceof BusinessMainActivity) {
                BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                if (!(activity instanceof SubmitToMonster) && businessMainActivity.getMainCustomers().size() == 1) {
                    int rnd = Util.getInt(0, 100);
                    MessageData message = activity.getMessages().get(0);
                    if ((businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.LIVELY || businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.HORNYSTATUS || businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.VERYHORNY) && rnd > 30) {
                        message.addToMessage("\n" + TextUtil.t("UNINHIBITED.hot.like", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.SHYSTATUS && rnd < 20) {
                        message.addToMessage("\n" + TextUtil.t("UNINHIBITED.shycustomer.dislike", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-10, trait);
                    } else if (rnd < 15) {
                        message.addToMessage("\n" + TextUtil.t("UNINHIBITED.like", character));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                    } else if (rnd > 85) {
                        message.addToMessage("\n" + TextUtil.t("UNINHIBITED.dislike", character));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                    }
                }
            }
        }
    }

    public static final class Shy
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == SpecializationAttribute.SEDUCTION) {
                float modification = attributeModification.getBaseAmount();
                float change = -Math.abs(modification) * 0.3f;
                attributeModification.addModificator(Float.valueOf(change));
            }
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore) {
                BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                if (!(activity instanceof SubmitToMonster) && businessMainActivity.getMainCustomers().size() == 1) {
                    int rnd = Util.getInt(0, 100);
                    MessageData message = activity.getMessages().get(0);
                    if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.SHYSTATUS && rnd > 30) {
                        message.addToMessage("\n" + TextUtil.t("SHY.shycustomer.like", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.SHYSTATUS && rnd < 20) {
                        message.addToMessage("\n" + TextUtil.t("SHY.shycustomer.dislike", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.HORNYSTATUS && rnd < 80) {
                        message.addToMessage("\n" + TextUtil.t("SHY.hornycustomer.dislike", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-10, trait);
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.DRUNK && rnd < 50) {
                        message.addToMessage("\n" + TextUtil.t("SHY.drunkcustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                    } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.PISSED && rnd < 50) {
                        message.addToMessage("\n" + TextUtil.t("SHY.pissedcustomer", (Person)character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                    } else if (rnd < 15) {
                        message.addToMessage("\n" + TextUtil.t("SHY.like", character));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                    } else if (rnd > 85) {
                        message.addToMessage("\n" + TextUtil.t("SHY.dislike", character));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                    }
                }
            }
        }
    }

    public static final class Absorption
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && ((activity = (RunningActivity)e.getSource()).getType() == ActivityType.ORGY || activity instanceof Whore && activity.getMainCustomer() != null && activity.getMainCustomer().getType() == CustomerType.GROUP)) {
                MessageData message = activity.getMessages().get(0);
                message.addToMessage("\n" + TextUtil.t("ABSORPTION.perform", character));
                int amountPeople = activity.getType() == ActivityType.ORGY ? activity.getCharacters().size() : ((CustomerGroup)activity.getMainCustomer()).getCustomers().size();
                activity.getAttributeModifications().add(new AttributeModification(2.5f * (float)amountPeople, EssentialAttributes.ENERGY, character));
            }
        }
    }

    public static final class MorphLagomorph
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHLAGOMORPH);
            }
        }
    }

    public static final class MorphInsect
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHINSECT);
            }
        }
    }

    public static final class MorphAquatic
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHAQUATIC);
            }
        }
    }

    public static final class MorphAvian
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHAVIAN);
            }
        }
    }

    public static final class MorphVulpine
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHVULPINE);
            }
        }
    }

    public static final class MorphReptilian
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHREPTILIAN);
            }
        }
    }

    public static final class MorphCanine
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHCANINE);
            }
        }
    }

    public static final class MorphFeline
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (character.getTraits().contains(Trait.FURRY) || character.getTraits().contains(Trait.BESTIAL)) {
                this.morphRandom(character);
            } else {
                character.removeTrait(Trait.MORPHFELINE);
            }
        }
    }

    public static final class Bestial
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            character.addSpecialization(SpecializationType.FURRY);
            if (character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue() < 10.0f) {
                character.getAttribute(SpecializationAttribute.TRANSFORMATION).setInternValue(10.0f);
                character.getAttribute(SpecializationAttribute.TRANSFORMATION).setMaxValue(50);
            }
        }
    }

    public static final class Furry
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            BusinessMainActivity businessMainActivity;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && (businessMainActivity = (BusinessMainActivity)((Object)activity)).getMainCustomers().size() == 1) {
                int rnd = Util.getInt(0, 100);
                MessageData message = activity.getMessages().get(0);
                if (rnd < 40) {
                    message.addToMessage("\n" + TextUtil.t("FURRY.like", character));
                    businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                } else if (rnd > 80) {
                    message.addToMessage("\n" + TextUtil.t("FURRY.dislike", character));
                    businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                }
            }
            character.addSpecialization(SpecializationType.FURRY);
        }
    }

    public static final class Loli
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            BusinessMainActivity businessMainActivity;
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof Whore && (businessMainActivity = (BusinessMainActivity)((Object)activity)).getMainCustomers().size() == 1) {
                int rnd = Util.getInt(0, 100);
                MessageData message = activity.getMessages().get(0);
                if (rnd < 30) {
                    message.addToMessage("\n" + TextUtil.t("LOLI.like", character));
                    businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                } else if (rnd > 70) {
                    message.addToMessage("\n" + TextUtil.t("LOLI.dislike", character));
                    businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                }
            }
        }
    }

    public static final class SmallBoobs
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            RunningActivity activity;
            if (e.getType() == EventType.ACTIVITY && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity) {
                BusinessMainActivity businessMainActivity = (BusinessMainActivity)((Object)activity);
                if (!(activity instanceof SubmitToMonster) && businessMainActivity.getMainCustomers().size() == 1) {
                    int rnd = Util.getInt(0, 100);
                    MessageData message = activity.getMessages().get(0);
                    if (rnd < 20) {
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(3, trait);
                    } else if (rnd > 70) {
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-3, trait);
                    }
                }
            }
        }
    }

    public static final class BigBoobs
    extends TraitEffect {
        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ACTIVITY) {
                Object businessMainActivity;
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity instanceof BusinessMainActivity) {
                    businessMainActivity = (BusinessMainActivity)((Object)activity);
                    for (Customer customer : businessMainActivity.getMainCustomers()) {
                        customer.addToSatisfaction(3, trait);
                    }
                }
                if (activity instanceof BusinessSecondaryActivity) {
                    businessMainActivity = (BusinessSecondaryActivity)((Object)activity);
                    for (Customer customer : businessMainActivity.getCustomers()) {
                        customer.addToSatisfaction(1, trait);
                    }
                }
            }
        }
    }

    public static final class AttributeMaxModifier
    extends TraitEffect {
        private AttributeType attributeType;
        private int attributeMaxModifier;

        public AttributeMaxModifier(AttributeType attributeType, int attributeMaxModifier) {
            this.attributeType = attributeType;
            this.attributeMaxModifier = attributeMaxModifier;
        }

        @Override
        public boolean addTrait(Charakter character) {
            Attribute attribute = character.getAttribute(this.attributeType);
            attribute.setMaxValue(attribute.getMaxValue() + this.attributeMaxModifier);
            return true;
        }

        @Override
        public boolean removeTrait(Charakter character) {
            Attribute attribute = character.getAttribute(this.attributeType);
            attribute.setMaxValue(attribute.getMaxValue() - this.attributeMaxModifier);
            return true;
        }
    }

    public static final class MultipleAttributeChangeInfluence
    extends TraitEffect {
        private AttributeType[] attributeTypes;
        private float attributeModifier;

        public MultipleAttributeChangeInfluence(float attributeModifier, AttributeType ... attributeTypes) {
            this.attributeTypes = attributeTypes;
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            if (e.getType() == EventType.ATTRIBUTECHANGE) {
                AttributeModification attributeModification = (AttributeModification)e.getSource();
                for (AttributeType attributeType : this.attributeTypes) {
                    if (attributeModification.getAttributeType() != attributeType) continue;
                    float modification = attributeModification.getBaseAmount();
                    float change = Math.abs(modification) * this.attributeModifier;
                    attributeModification.addModificator(Float.valueOf(change));
                    break;
                }
            }
        }
    }

    public static class SpecializationAttributeChangeInfluence
    extends TraitEffect {
        private float attributeModifier;

        public SpecializationAttributeChangeInfluence(float attributeModifier) {
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() instanceof SpecializationAttribute && attributeModification.getBaseAmount() > 0.0f) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * this.attributeModifier;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static class SexAttributeChangeInfluence
    extends TraitEffect {
        private float attributeModifier;

        public SexAttributeChangeInfluence(float attributeModifier) {
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() instanceof Sextype && attributeModification.getBaseAmount() > 0.0f) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * this.attributeModifier;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static class BaseAttributeChangeInfluence
    extends TraitEffect {
        private float attributeModifier;

        public BaseAttributeChangeInfluence(float attributeModifier) {
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() instanceof BaseAttributeTypes && attributeModification.getBaseAmount() > 0.0f) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * this.attributeModifier;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static class AllAttributeChangeInfluence
    extends TraitEffect {
        private float attributeModifier;

        public AllAttributeChangeInfluence(float attributeModifier) {
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && !((attributeModification = (AttributeModification)e.getSource()).getAttributeType() instanceof EssentialAttributes) && attributeModification.getBaseAmount() > 0.0f) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * this.attributeModifier;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }

    public static final class AttributeChangeInfluence
    extends TraitEffect {
        private AttributeType attributeType;
        private float attributeModifier;

        public AttributeChangeInfluence(AttributeType attributeType, float attributeModifier) {
            this.attributeType = attributeType;
            this.attributeModifier = attributeModifier;
        }

        @Override
        public void handleEvent(MyEvent e, Charakter character, Trait trait) {
            AttributeModification attributeModification;
            if (e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == this.attributeType) {
                float modification = attributeModification.getBaseAmount();
                float change = Math.abs(modification) * this.attributeModifier;
                attributeModification.addModificator(Float.valueOf(change));
            }
        }
    }
}

