/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.game.items.ItemType;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rob
extends RunningActivity {
    private MessageData messageData;
    private Map<Charakter, EscapeRoutes> characterAction = new HashMap<Charakter, EscapeRoutes>();

    @Override
    public void perform() {
        Object[] argument;
        Charakter character = this.getCharacter();
        ArrayList<EscapeRoutes> actions = new ArrayList<EscapeRoutes>();
        int stealChance = 25 + character.getStealChance();
        int stealAmount = 15 + character.getStealAmountModifier();
        int stealItemChance = 10 + character.getStealItemChance();
        int targetLevel = character.getFinalValue(SpecializationAttribute.PICKPOCKETING) + character.getFinalValue(SpecializationAttribute.AGILITY) + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) + Util.getInt(-20, 20);
        targetLevel *= Util.getInt(40, 110) / 100;
        int failureChance = (int)(100.0 - (double)character.getFinalValue(SpecializationAttribute.AGILITY) / 1.5 - (double)(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 2));
        for (Condition condition : character.getConditions()) {
            if (!(condition instanceof Buff.Watched)) continue;
            failureChance *= 1;
        }
        Customer target = null;
        if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
            failureChance /= 4;
            target = this.generateTarget(targetLevel);
            argument = new Object[]{target.getName()};
            this.messageData.addToMessage("\n" + TextUtil.t("rob.target.day", (Person)character, (Person)target, argument));
        } else {
            stealChance *= 1;
            stealAmount *= 1;
            stealItemChance *= 1;
            if (character.getTraits().contains(Trait.PHANTOMTHIEF)) {
                stealChance *= 2;
                stealAmount *= 2;
                stealItemChance *= 2;
                targetLevel *= 2;
            }
            target = this.generateTarget(targetLevel + 40);
            argument = new Object[]{target.getName()};
            this.messageData.addToMessage("\n" + TextUtil.t("rob.target.night", (Person)character, (Person)target, argument));
        }
        target.setMaxHitpoints(target.getMaxHitpoints() + targetLevel);
        target.setHitpoints(target.getMaxHitpoints());
        if (target != null) {
            if (Util.getInt(0, 100) < (failureChance += target.getInitialSatisfaction() / 5)) {
                if (Util.getInt(0, 100) < 10) {
                    character.addCondition(new Buff.Watched(character.getFinalValue(SpecializationAttribute.PICKPOCKETING)));
                }
                this.messageData.addToMessage("\n" + TextUtil.t("rob.noticed", (Person)character, target));
                if (character.getTraits().contains(Trait.CLEVER)) {
                    actions.add(EscapeRoutes.RUN);
                }
                if (character.getTraits().contains(Trait.FIT)) {
                    actions.add(EscapeRoutes.RUN);
                }
                if (character.getTraits().contains(Trait.STUPID)) {
                    actions.add(EscapeRoutes.CAUGHTANDFIGHT);
                }
                actions.add(EscapeRoutes.CAUGHTANDFIGHT);
                actions.add(EscapeRoutes.RUN);
                actions.add(EscapeRoutes.RUN);
                if (target.getType() != CustomerType.BUM) {
                    actions.add(EscapeRoutes.CALLGUARDS);
                }
                if (target.getType() != CustomerType.BUM) {
                    actions.add(EscapeRoutes.CALLGUARDS);
                }
                if (character.getTraits().contains(Trait.NYMPHO) && character.getGender() != target.getGender()) {
                    actions.add(EscapeRoutes.SEDUCEANDROB);
                    actions.add(EscapeRoutes.SEDUCEANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDROB);
                }
                if (character.getTraits().contains(Trait.SLUT) && character.getGender() != target.getGender()) {
                    actions.add(EscapeRoutes.SEDUCEANDROB);
                    actions.add(EscapeRoutes.SEDUCEANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDROB);
                }
                if (character.getTraits().contains(Trait.LIAISONSDANGEREUSES) && character.getGender() != target.getGender()) {
                    actions.add(EscapeRoutes.SEDUCEANDROB);
                    actions.add(EscapeRoutes.SEDUCEANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDROB);
                    actions.add(EscapeRoutes.SEDUCEANDROB);
                    actions.add(EscapeRoutes.SEDUCEANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDFLEE);
                    actions.add(EscapeRoutes.FUCKANDROB);
                }
                this.characterAction.put(character, (EscapeRoutes)((Object)actions.get(Util.getInt(0, actions.size()))));
            } else {
                this.characterAction.put(character, EscapeRoutes.STEAL);
            }
            if (this.characterAction.get(character) == EscapeRoutes.CAUGHTANDFIGHT) {
                this.messageData.addToMessage("\n" + TextUtil.t("rob.noticed.fight", (Person)character, target));
                this.getAttributeModifications().add(new AttributeModification(1.07f, SpecializationAttribute.VETERAN, character));
                Charakter fighter1 = character;
                Customer fighter2 = target;
                int i = 0;
                int startHitPoints1 = fighter1.getHitpoints();
                int startHitPoints2 = fighter2.getHitpoints();
                Battle battle = new Battle(fighter1, fighter2);
                do {
                    battle.doRound();
                } while (++i < 1000 && fighter1.getHitpoints() > startHitPoints1 - 30 && fighter2.getHitpoints() > 10);
                this.getMessages().get(0).addToMessage("\n\n" + battle.getCombatText());
                if (character.getFinalValue(SpecializationAttribute.MAGIC) > 10) {
                    this.getAttributeModifications().add(new AttributeModification(0.28f, SpecializationAttribute.MAGIC, character));
                }
                if (fighter1.getHitpoints() / startHitPoints1 > fighter2.getHitpoints() / startHitPoints2) {
                    this.messageData.addToMessage(TextUtil.t("rob.fight.run", (Person)character, target));
                    if (Util.getInt(0, 100) < 50) {
                        this.characterAction.put(character, EscapeRoutes.RUN);
                    } else {
                        this.characterAction.put(character, EscapeRoutes.STEAL);
                    }
                } else {
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.fight.defeat", (Person)character, target));
                    if (Util.getInt(0, 100) < 50) {
                        this.characterAction.put(character, EscapeRoutes.CALLGUARDS);
                    } else if (Util.getInt(0, 100) < 50) {
                        this.messageData.addToMessage("\n" + TextUtil.t("rob.defeat.meh", (Person)character, target));
                        this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacter()));
                    } else {
                        this.messageData.addToMessage("\n" + TextUtil.t("rob.defeat.rape", (Person)character, target));
                        this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, this.getCharacter()));
                        this.getAttributeModifications().add(new AttributeModification(0.28f, Sextype.ANAL, character));
                    }
                }
                if (character.getTraits().contains(Trait.FIRSTAID)) {
                    character.getAttribute(EssentialAttributes.HEALTH).addToValue(10.0f);
                    this.messageData.addToMessage("\n" + TextUtil.t("nurse.selfheal", character));
                    this.getAttributeModifications().add(new AttributeModification(0.28f, SpecializationAttribute.MEDICALKNOWLEDGE, character));
                }
            }
            if (this.characterAction.get(character) == EscapeRoutes.CALLGUARDS) {
                this.messageData.addToMessage("\n" + TextUtil.t("rob.callguards", (Person)character, target));
                if ((character.getTraits().contains(Trait.SLUT) || character.getTraits().contains(Trait.LIAISONSDANGEREUSES) || character.getTraits().contains(Trait.NYMPHO)) && Util.getInt(0, 110) < character.getFinalValue(SpecializationAttribute.SEDUCTION)) {
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.fuckguards", (Person)character, target));
                    this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.getCharacter()));
                    this.getAttributeModifications().add(new AttributeModification(0.5f, Sextype.GROUP, character));
                } else if (Util.getInt(0, 100) < 30) {
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.police.pay", (Person)character, target));
                    this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter()));
                    this.setIncome(-1500);
                } else {
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.defeat.police.lesson", (Person)character, target));
                    this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.getCharacter()));
                    this.getAttributeModifications().add(new AttributeModification(0.5f, Sextype.GROUP, character));
                }
            }
            if (this.characterAction.get(character) == EscapeRoutes.FUCKANDROB) {
                this.messageData.addToMessage("\n" + TextUtil.t("rob.fuckandrob", (Person)character, target));
                this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.getCharacter()));
                this.getAttributeModifications().add(new AttributeModification(0.48f, Sextype.VAGINAL, character));
                stealItemChance += 5;
                stealChance += 5;
                this.characterAction.put(character, EscapeRoutes.STEAL);
            }
            if (this.characterAction.get(character) == EscapeRoutes.SEDUCEANDROB) {
                this.messageData.addToMessage("\n" + TextUtil.t("rob.seduceandrob", (Person)character, target));
                this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.getCharacter()));
                this.getAttributeModifications().add(new AttributeModification(0.28f, SpecializationAttribute.SEDUCTION, character));
                this.getAttributeModifications().add(new AttributeModification(0.28f, SpecializationAttribute.STRIP, character));
                stealItemChance += 5;
                stealChance += 5;
                this.characterAction.put(character, EscapeRoutes.STEAL);
            }
            if (this.characterAction.get(character) == EscapeRoutes.SEDUCEANDFLEE) {
                this.messageData.addToMessage("\n" + TextUtil.t("rob.seduceandflee", (Person)character, target));
                this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.getCharacter()));
                this.getAttributeModifications().add(new AttributeModification(0.28f, SpecializationAttribute.SEDUCTION, character));
                this.getAttributeModifications().add(new AttributeModification(0.28f, SpecializationAttribute.STRIP, character));
                this.characterAction.put(character, EscapeRoutes.RUN);
            }
            if (this.characterAction.get(character) == EscapeRoutes.FUCKANDFLEE) {
                this.messageData.addToMessage(TextUtil.t("rob.fuckandflee", (Person)character, target));
                this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.getCharacter()));
                this.getAttributeModifications().add(new AttributeModification(0.48f, Sextype.VAGINAL, character));
                this.characterAction.put(character, EscapeRoutes.RUN);
            }
            if (this.characterAction.get(character) == EscapeRoutes.STEAL) {
                int stolenAmount = 0;
                Inventory.ItemData stolenItem = null;
                if (Util.getInt(0, 100) < stealItemChance && (stolenItem = target.getItem()) != null) {
                    this.getAttributeModifications().add(new AttributeModification(1.07f, EssentialAttributes.MOTIVATION, character));
                    if (character.getTraits().contains(Trait.RESELLER) && stolenItem.getItem().getType() != ItemType.UNLOCK) {
                        Jasbro.getInstance().getData().earnMoney(stolenItem.getItem().getValue() / 2, stolenItem);
                    } else {
                        Jasbro.getInstance().getData().getInventory().addItems(stolenItem.getItem(), stolenItem.getAmount());
                    }
                    Object[] arg2 = new Object[]{stolenItem.getItem().getName(), stolenItem.getAmount()};
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.steal.item", (Person)character, (Person)target, arg2));
                }
                if (Util.getInt(0, 100) < stealChance) {
                    this.getAttributeModifications().add(new AttributeModification(1.07f, EssentialAttributes.MOTIVATION, character));
                    Object[] arg = new Object[]{stolenAmount += target.payFixed((int)((float)(stealAmount * 10) + (float)(target.getMoney() * stealAmount) / 100.0f))};
                    Jasbro.getInstance().getData().earnMoney(stolenAmount, this);
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.steal.money", (Person)character, (Person)target, arg));
                }
                if (stolenAmount == 0 && stolenItem == null) {
                    this.messageData.addToMessage("\n" + TextUtil.t("rob.steal.nothing", (Person)character, target));
                }
            }
            if (this.characterAction.get(character) == EscapeRoutes.RUN) {
                this.messageData.addToMessage("\n" + TextUtil.t("rob.noticed.run", (Person)character, target));
            }
        }
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        String message = TextUtil.t("rob.basic", character);
        this.messageData = new MessageData(message, null, this.getBackground());
        this.messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()));
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, SpecializationAttribute.AGILITY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, SpecializationAttribute.PICKPOCKETING));
        return modifications;
    }

    private Customer generateTarget(int skill) {
        SpawnData spawnData = new SpawnData();
        if (skill < 20) {
            return spawnData.createCustomer(CustomerType.BUM);
        }
        if (skill < 40) {
            return spawnData.createCustomer(CustomerType.PEASANT);
        }
        if (skill < 60) {
            return spawnData.createCustomer(CustomerType.MERCHANT);
        }
        if (skill < 80) {
            return spawnData.createCustomer(CustomerType.BUSINESSMAN);
        }
        if (skill < 100) {
            return spawnData.createCustomer(CustomerType.MINORNOBLE);
        }
        if (skill < 150) {
            return spawnData.createCustomer(CustomerType.LORD);
        }
        if (skill < 190) {
            return spawnData.createCustomer(CustomerType.CELEBRITY);
        }
        return spawnData.createCustomer(CustomerType.BUM);
    }

    private static enum EscapeRoutes {
        STEAL,
        CAUGHTANDFIGHT,
        RUN,
        FUCKANDFLEE,
        SEDUCEANDROB,
        FUCKANDROB,
        SEDUCEANDFLEE,
        CALLGUARDS;

    }
}

