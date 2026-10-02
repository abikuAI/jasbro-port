/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicUse
extends RunningActivity
implements BusinessSecondaryActivity {
    private MessageData messageData;
    private int bonus;
    private Charakter thatOneGirl = null;
    private List<Charakter> girls = new ArrayList<Charakter>();
    private Map<Charakter, Short> groupSize = new HashMap<Charakter, Short>();
    private Map<Charakter, Short> remainingTime = new HashMap<Charakter, Short>();
    private Map<Charakter, Short> totalServed = new HashMap<Charakter, Short>();
    private Map<Charakter, Short> energy = new HashMap<Charakter, Short>();
    private Map<Charakter, Short> energySpent = new HashMap<Charakter, Short>();
    private Map<Charakter, Short> maxEnergy = new HashMap<Charakter, Short>();
    private Map<Charakter, PublicUseEvent> event = new HashMap<Charakter, PublicUseEvent>();
    private Map<Charakter, Short> girlStatus = new HashMap<Charakter, Short>();

    @Override
    public void init() {
        this.girls.addAll(this.getCharacters());
        Collections.shuffle(this.girls);
        short en = 0;
        boolean someoneTookAll = false;
        for (Charakter currentGirl : this.girls) {
            en = (short)(100 + currentGirl.getStamina() / 10);
            if (currentGirl.getTraits().contains(Trait.SEXADDICT)) {
                en = (short)(en + 25);
            }
            if (currentGirl.getTraits().contains(Trait.GANGBANGQUEEN)) {
                en = (short)(en + 25);
            }
            if (currentGirl.getTraits().contains(Trait.KEEPEMCOMING)) {
                en = (short)(en + 25);
            }
            if (currentGirl.getTraits().contains(Trait.PERSEVERING)) {
                en = (short)(en + 10);
            }
            if (currentGirl.getTraits().contains(Trait.NYMPHO)) {
                en = (short)(en + 10);
            }
            if (currentGirl.getTraits().contains(Trait.SEXADDICT)) {
                en = (short)(en + 25);
            }
            if (currentGirl.getTraits().contains(Trait.FRAGILE)) {
                en = (short)(en - 25);
            }
            if (currentGirl.getTraits().contains(Trait.FLABBY)) {
                en = (short)(en - 25);
            }
            if (currentGirl.getTraits().contains(Trait.SINGLEMINDED)) {
                en = (short)(en - 25);
            }
            this.energy.put(currentGirl, en);
            this.energySpent.put(currentGirl, (short)0);
            this.maxEnergy.put(currentGirl, en);
            this.totalServed.put(currentGirl, (short)0);
            this.girlStatus.put(currentGirl, (short)0);
            if (currentGirl.getTraits().contains(Trait.PUBLICUSE)) {
                this.remainingTime.put(currentGirl, (short)400);
            } else {
                this.remainingTime.put(currentGirl, (short)300);
            }
            ArrayList<PublicUseEvent> actions = new ArrayList<PublicUseEvent>();
            actions.add(PublicUseEvent.ROUGHCUSTOMER);
            actions.add(PublicUseEvent.GENTLECUSTOMER);
            if (this.getCustomers().size() > 25) {
                actions.add(PublicUseEvent.LINE);
            }
            if (this.getCustomers().size() > 25) {
                actions.add(PublicUseEvent.SWARM);
            }
            actions.add(PublicUseEvent.NOBREAK);
            actions.add(PublicUseEvent.NICECUSTOMER);
            actions.add(PublicUseEvent.NOBREAK);
            actions.add(PublicUseEvent.ALLANAL);
            actions.add(PublicUseEvent.ALLVAGINAL);
            actions.add(PublicUseEvent.BUKKAKE);
            actions.add(PublicUseEvent.NORMAL);
            actions.add(PublicUseEvent.NORMAL);
            if (this.getCustomers().size() > 25) {
                actions.add(PublicUseEvent.OVERTIME);
            }
            if (currentGirl.getTraits().contains(Trait.SEXADDICT) && this.getCustomers().size() > 25 && Util.getInt(0, 100) < 5) {
                this.event.put(currentGirl, PublicUseEvent.ALL);
                this.remainingTime.put(currentGirl, (short)30000);
            } else if (currentGirl.getTraits().contains(Trait.GANGBANGQUEEN) && this.getCharacters().size() != 1 && !someoneTookAll && this.getCustomers().size() > 25 && Util.getInt(0, 100) < 5) {
                this.event.put(currentGirl, PublicUseEvent.ALONE);
                this.thatOneGirl = currentGirl;
                this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) * 150 / 100));
                someoneTookAll = true;
                for (Charakter otherGirl : this.getCharacters()) {
                    if (otherGirl == currentGirl) continue;
                    this.event.put(otherGirl, PublicUseEvent.NONE);
                }
            }
            if (this.event.get(currentGirl) == PublicUseEvent.ALONE || this.event.get(currentGirl) == PublicUseEvent.NONE || this.event.get(currentGirl) == PublicUseEvent.ALL) continue;
            this.event.put(currentGirl, (PublicUseEvent)((Object)actions.get(Util.getInt(0, actions.size()))));
        }
    }

    @Override
    public void perform() {
        if (this.getCustomers().size() >= 5) {
            short totalTips = 0;
            short timeTaken = 0;
            short spentEnergy = 0;
            int customersServed = 0;
            short servedThisRound = 1;
            int breakTime = 100;
            int energyCostFactor = 100;
            Sextype sex = null;
            short rand = 0;
            while (customersServed <= this.getCustomers().size() && this.isTimeLeft(this.getCharacters())) {
                for (Charakter currentGirl : this.girls) {
                    servedThisRound = 0;
                    this.groupSize.put(currentGirl, (short)(Util.getInt(2, 3) + currentGirl.getFinalValue(Sextype.GROUP) / 20));
                    if (currentGirl.getTraits().contains(Trait.GANGBANGQUEEN)) {
                        this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + Util.getInt(0, 1)));
                    }
                    if (currentGirl.getTraits().contains(Trait.PUBLICUSE)) {
                        this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + Util.getInt(0, 2)));
                    }
                    if (currentGirl.getTraits().contains(Trait.MULTIFACETED)) {
                        this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + Util.getInt(0, 1)));
                    }
                    switch (this.event.get(currentGirl)) {
                        case ROUGHCUSTOMER: {
                            energyCostFactor = 130;
                            break;
                        }
                        case GENTLECUSTOMER: {
                            energyCostFactor = 70;
                            break;
                        }
                        case NICECUSTOMER: {
                            breakTime = 130;
                            break;
                        }
                        case LINE: {
                            this.groupSize.put(currentGirl, (short)1);
                            energyCostFactor = 30;
                            breakTime = 5;
                            break;
                        }
                        case ALLANAL: {
                            if (currentGirl.getFinalValue(Sextype.ANAL) > 90 && Util.getInt(0, 100) > 20) {
                                this.groupSize.put(currentGirl, (short)2);
                            } else if (currentGirl.getFinalValue(Sextype.ANAL) > 90 && Util.getInt(0, 100) > 50) {
                                this.groupSize.put(currentGirl, (short)2);
                            } else {
                                this.groupSize.put(currentGirl, (short)1);
                            }
                            energyCostFactor = 110;
                            breakTime = 10;
                            break;
                        }
                        case ALLVAGINAL: {
                            if (currentGirl.getFinalValue(Sextype.VAGINAL) > 90 && Util.getInt(0, 100) > 20) {
                                this.groupSize.put(currentGirl, (short)2);
                            } else if (currentGirl.getFinalValue(Sextype.VAGINAL) > 90 && Util.getInt(0, 100) > 50) {
                                this.groupSize.put(currentGirl, (short)2);
                            } else {
                                this.groupSize.put(currentGirl, (short)1);
                            }
                            energyCostFactor = 110;
                            breakTime = 5;
                            break;
                        }
                        case BUKKAKE: {
                            this.groupSize.put(currentGirl, (short)(2 + currentGirl.getFinalValue(Sextype.ORAL) / 20));
                            energyCostFactor = 30;
                            breakTime = 80;
                            break;
                        }
                        case SWARM: {
                            this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + 2));
                            energyCostFactor = 110;
                            break;
                        }
                        case NOBREAK: {
                            if (this.energy.get(currentGirl) <= 10) break;
                            breakTime = 0;
                            break;
                        }
                        case OVERTIME: {
                            this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) * 150 / 100));
                            break;
                        }
                        case ALONE: {
                            this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) * 130 / 100));
                            breakTime = 2;
                            this.groupSize.put(currentGirl, (short)(this.groupSize.get(currentGirl) + 2));
                            break;
                        }
                    }
                    if (this.groupSize.get(currentGirl) > this.getCustomers().size()) {
                        this.groupSize.put(currentGirl, (short)this.getCustomers().size());
                    }
                    energyCostFactor = (short)(energyCostFactor + this.totalServed.get(currentGirl));
                    if (this.energy.get(currentGirl) > 5 && this.remainingTime.get(currentGirl) > 10 && this.event.get(currentGirl) != PublicUseEvent.NONE) {
                        for (int i = customersServed; i < this.getCustomers().size(); i = (int)((short)(i + 1))) {
                            rand = (short)Util.getInt(0, 100);
                            sex = rand < 20 || this.event.get(currentGirl) == PublicUseEvent.BUKKAKE ? Sextype.ORAL : (rand < 60 || this.event.get(currentGirl) == PublicUseEvent.ALLVAGINAL ? Sextype.VAGINAL : Sextype.ANAL);
                            if (this.event.get(currentGirl) == PublicUseEvent.BUKKAKE) {
                                sex = Sextype.ORAL;
                            }
                            if (this.event.get(currentGirl) == PublicUseEvent.ALLVAGINAL) {
                                sex = Sextype.VAGINAL;
                            }
                            if (this.event.get(currentGirl) == PublicUseEvent.ALLANAL) {
                                sex = Sextype.ANAL;
                            }
                            short energyBefore = this.energy.get(currentGirl);
                            currentGirl.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                            this.totalServed.put(currentGirl, (short)(this.totalServed.get(currentGirl) + 1));
                            servedThisRound = (short)(servedThisRound + 1);
                            customersServed = (short)(customersServed + 1);
                            timeTaken = (short)(12 - this.groupSize.get(currentGirl) / 2);
                            timeTaken = (short)(timeTaken - timeTaken * (currentGirl.getFinalValue(sex) + currentGirl.getFinalValue(Sextype.GROUP)) / 250);
                            totalTips = (short)(totalTips + this.getCustomers().get(i).payFixed(Util.getInt(1, 5 + currentGirl.getFinalValue(sex))));
                            this.getCustomers().get(i).addToSatisfaction(2 + currentGirl.getFinalValue(sex) / 16, this);
                            this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) - timeTaken));
                            this.getAttributeModifications().add(new AttributeModification(0.015f, BaseAttributeTypes.OBEDIENCE, currentGirl));
                            this.getAttributeModifications().add(new AttributeModification(0.1f, sex, currentGirl));
                            this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.GROUP, currentGirl));
                            this.getAttributeModifications().add(new AttributeModification(0.02f, BaseAttributeTypes.STAMINA, currentGirl));
                            if (Util.getInt(0, 100) < 100) {
                                currentGirl.getFame().modifyFame(1.0);
                            }
                            this.getHouse().modDirt(2);
                            if (servedThisRound != this.groupSize.get(currentGirl)) continue;
                            spentEnergy = (short)(servedThisRound * 10 * (energyCostFactor + Util.getInt(-10, 10)) / 100);
                            this.energy.put(currentGirl, (short)(this.energy.get(currentGirl) - spentEnergy));
                            this.energySpent.put(currentGirl, (short)(this.energySpent.get(currentGirl) + spentEnergy));
                            if (energyBefore <= 0 || this.energy.get(currentGirl) > 0) break;
                            this.girlStatus.put(currentGirl, (short)(this.girlStatus.get(currentGirl) + 1));
                            break;
                        }
                    }
                    if (this.energy.get(currentGirl) < 10) {
                        breakTime = (short)(breakTime + 100);
                    }
                    if (currentGirl.getTraits().contains(Trait.PUBLICUSE)) {
                        this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) - breakTime / 30 - 1));
                    } else {
                        this.remainingTime.put(currentGirl, (short)(this.remainingTime.get(currentGirl) - breakTime / 20 - 1));
                    }
                    this.energy.put(currentGirl, (short)(this.energy.get(currentGirl) + 5 + (currentGirl.getStamina() / 5 + this.getCharacters().size()) * breakTime / 100));
                }
            }
            this.modifyIncome(totalTips);
            for (Charakter character : this.getCharacters()) {
                if (this.totalServed.get(character) < this.groupSize.get(character)) {
                    this.groupSize.put(character, this.totalServed.get(character));
                }
                if (this.totalServed.get(character) == 0 && this.event.get(character) != PublicUseEvent.NONE) {
                    this.event.put(character, PublicUseEvent.NOCUSTOMER);
                }
                Object[] arguments = new Object[]{this.totalServed.get(character), this.groupSize.get(character), this.girlStatus.get(character)};
                this.messageData.addToMessage("\n");
                switch (this.event.get(character)) {
                    case NORMAL: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.normal", (Person)character, arguments));
                        break;
                    }
                    case ROUGHCUSTOMER: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.rough", (Person)character, arguments));
                        break;
                    }
                    case GENTLECUSTOMER: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.gentle", (Person)character, arguments));
                        break;
                    }
                    case NICECUSTOMER: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.nice", (Person)character, arguments));
                        break;
                    }
                    case NOBREAK: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.nobreak", (Person)character, arguments));
                        break;
                    }
                    case ALLANAL: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.anal", (Person)character, arguments));
                        break;
                    }
                    case ALLVAGINAL: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.vaginal", (Person)character, arguments));
                        break;
                    }
                    case BUKKAKE: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.bukkake", (Person)character, arguments));
                        break;
                    }
                    case LINE: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.line", (Person)character, arguments));
                        break;
                    }
                    case SWARM: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.swarm", (Person)character, arguments));
                        break;
                    }
                    case OVERTIME: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.overtime", (Person)character, arguments));
                        break;
                    }
                    case NONE: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.none", (Person)character, (Person)this.thatOneGirl, arguments));
                        break;
                    }
                    case NOCUSTOMER: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.nocustomer", (Person)character, (Person)this.thatOneGirl, arguments));
                        break;
                    }
                    case ALONE: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.alone", (Person)character, arguments));
                        if (this.totalServed.get(character).shortValue() == this.getCustomers().size()) {
                            this.messageData.addToMessage("\n" + TextUtil.t("public.alone.success", (Person)character, arguments));
                            break;
                        }
                        this.messageData.addToMessage("\n" + TextUtil.t("public.alone.failure", (Person)character, arguments));
                        break;
                    }
                    case ALL: {
                        this.messageData.addToMessage("\n" + TextUtil.t("public.all", (Person)character, arguments));
                    }
                }
                if (this.totalServed.get(character) == 0) continue;
                if (this.energySpent.get(character) < this.maxEnergy.get(character)) {
                    this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.easy", (Person)character, arguments));
                    this.getAttributeModifications().add(new AttributeModification(-30.0f - (float)this.girlStatus.get(character).shortValue() * 2.0f, EssentialAttributes.ENERGY, character));
                    continue;
                }
                if (this.energySpent.get(character) < this.maxEnergy.get(character) * 15 / 10) {
                    this.getAttributeModifications().add(new AttributeModification(-45.0f - (float)this.girlStatus.get(character).shortValue() * 3.0f, EssentialAttributes.ENERGY, character));
                    this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.normal", (Person)character, arguments));
                    if (this.girlStatus.get(character) == 1) {
                        this.messageData.addToMessage(" " + TextUtil.t("public.stamina.normal.faint.once", (Person)character, arguments));
                    }
                    if (this.girlStatus.get(character) == 2) {
                        this.messageData.addToMessage(" " + TextUtil.t("public.stamina.normal.faint.twice", (Person)character, arguments));
                    }
                    if (this.girlStatus.get(character) <= 2) continue;
                    this.messageData.addToMessage(" " + TextUtil.t("public.stamina.normal.faint.more", (Person)character, arguments));
                    continue;
                }
                if (this.energySpent.get(character) < this.maxEnergy.get(character) * 2) {
                    this.getAttributeModifications().add(new AttributeModification(-60.0f - (float)this.girlStatus.get(character).shortValue() * 4.0f, EssentialAttributes.ENERGY, character));
                    this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.hard", (Person)character, arguments));
                    if (this.girlStatus.get(character) == 1) {
                        this.messageData.addToMessage(" " + TextUtil.t("public.stamina.hard.faint.once", (Person)character, arguments));
                    }
                    if (this.girlStatus.get(character) == 2) {
                        this.messageData.addToMessage(" " + TextUtil.t("public.stamina.hard.faint.twice", (Person)character, arguments));
                    }
                    if (this.girlStatus.get(character) <= 2) continue;
                    this.messageData.addToMessage(" " + TextUtil.t("public.stamina.hard.faint.more", (Person)character, arguments));
                    continue;
                }
                this.getAttributeModifications().add(new AttributeModification(-75.0f - (float)this.girlStatus.get(character).shortValue() * 5.0f, EssentialAttributes.ENERGY, character));
                this.messageData.addToMessage("\n" + TextUtil.t("public.stamina.exhausted", (Person)character, arguments));
                if (this.girlStatus.get(character) == 1) {
                    this.messageData.addToMessage(" " + TextUtil.t("public.stamina.exhausted.faint.once", (Person)character, arguments));
                }
                if (this.girlStatus.get(character) == 2) {
                    this.messageData.addToMessage(" " + TextUtil.t("public.stamina.exhausted.faint.twice", (Person)character, arguments));
                }
                if (this.girlStatus.get(character) <= 2) continue;
                this.messageData.addToMessage(" " + TextUtil.t("public.stamina.exhausted.faint.more", (Person)character, arguments));
            }
            Object[] arguments = new Object[]{totalTips};
            this.messageData.addToMessage("\n\n" + TextUtil.t("public.result.final", arguments));
        } else {
            this.messageData.addToMessage(TextUtil.t("public.notenoughcustomers"));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        Object[] arguments = new Object[]{TextUtil.listCharacters(this.getCharacters()), this.getCustomers().size()};
        String messageText = TextUtil.t("public.basic", arguments);
        this.messageData = new MessageData(messageText, null, this.getBackground());
        block15: for (Charakter character : this.getCharacters()) {
            if (this.getCustomers().size() >= 5) {
                switch (this.event.get(character)) {
                    case NORMAL: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case GENTLECUSTOMER: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case ALL: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case ALONE: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case ROUGHCUSTOMER: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case NICECUSTOMER: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case OVERTIME: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case NOBREAK: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case SWARM: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
                        continue block15;
                    }
                    case LINE: {
                        if (Util.getInt(0, 100) < 50) {
                            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
                            continue block15;
                        }
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
                        continue block15;
                    }
                    case ALLANAL: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
                        continue block15;
                    }
                    case ALLVAGINAL: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
                        continue block15;
                    }
                    case BUKKAKE: {
                        this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, character));
                        continue block15;
                    }
                }
                this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
                continue;
            }
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
        }
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.getCustomers().size() >= 10) {
            for (Charakter character : this.getCharacters()) {
                if (character.getTraits().contains(Trait.SEXADDICT)) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -0.75f, EssentialAttributes.MOTIVATION));
                } else {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -1.5f, EssentialAttributes.MOTIVATION));
                }
                if (character.getType() != CharacterType.TRAINER || character.getTraits().contains(Trait.LEGACYWHORE)) continue;
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -0.5f, BaseAttributeTypes.COMMAND));
            }
        }
        return modifications;
    }

    @Override
    public int getAppeal() {
        return 1;
    }

    @Override
    public int getMaxAttendees() {
        return 60 + this.getCharacters().size() * 40;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    private boolean isTimeLeft(List<Charakter> list) {
        for (Charakter character : list) {
            if (this.remainingTime.get(character) <= 0) continue;
            return true;
        }
        return false;
    }

    public static enum PublicUseEvent {
        ROUGHCUSTOMER,
        GENTLECUSTOMER,
        LINE,
        SWARM,
        NOBREAK,
        NORMAL,
        NICECUSTOMER,
        ALLANAL,
        ALLVAGINAL,
        BUKKAKE,
        OVERTIME,
        ALL,
        ALONE,
        NONE,
        NOCUSTOMER;

    }
}

