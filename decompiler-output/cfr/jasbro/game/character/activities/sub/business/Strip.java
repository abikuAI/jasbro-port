/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Strip
extends RunningActivity
implements BusinessSecondaryActivity {
    private MessageData messageData;
    private int bonus;
    private Map<Charakter, StripAction> characterAction = new HashMap<Charakter, StripAction>();

    /*
     * Unable to fully structure code
     */
    @Override
    public void perform() {
        block167: {
            character = this.getCharacter();
            skill = character.getCharisma() / 5 + character.getFinalValue(SpecializationAttribute.STRIP) / 5 + 1;
            amountEarned = 0;
            amountHappy = 0;
            overalltips = 0;
            tip = 0;
            chanceOfTip = 25 + character.getCharisma() + character.getFinalValue(SpecializationAttribute.STRIP);
            chanceModifier = 1.0f;
            for (Customer customer : this.getCustomers()) {
                switch (1.$SwitchMap$jasbro$game$events$business$CustomerType[customer.getType().ordinal()]) {
                    case 1: {
                        chanceModifier = 1.7f;
                        break;
                    }
                    case 2: {
                        chanceModifier = 1.2f;
                        break;
                    }
                    case 3: {
                        chanceModifier = 1.0f;
                        break;
                    }
                    case 4: {
                        chanceModifier = 0.8f;
                        break;
                    }
                    case 5: {
                        chanceModifier = 0.6f;
                        break;
                    }
                    case 6: {
                        chanceModifier = 0.4f;
                        break;
                    }
                    case 7: {
                        chanceModifier = 0.2f;
                        break;
                    }
                    default: {
                        chanceModifier = 2.0f;
                    }
                }
                if ((float)Util.getInt(0, 100) < chanceModifier * (float)chanceOfTip) {
                    tip = customer.getMoney() * Util.getInt(6, 12) / 100;
                    tip = customer.pay(tip, this.getCharacter().getMoneyModifier());
                    ++amountHappy;
                    customer.addToSatisfaction(skill, this);
                    overalltips += tip;
                    amountEarned += tip;
                    continue;
                }
                customer.addToSatisfaction(skill / 4, this);
            }
            this.modifyIncome(amountEarned);
            if (amountEarned > 0) {
                this.messageData.addToMessage("\n\n" + TextUtil.t("strip.result.owned", (Person)this.getCharacter(), new Object[]{this.getCustomers().size(), amountHappy, this.getIncome(), overalltips}));
            } else {
                this.messageData.addToMessage("\n\n" + TextUtil.t("strip.result.basic", (Person)this.getCharacter(), new Object[]{this.getCustomers().size(), amountHappy, this.getIncome(), overalltips}));
            }
            actions = new ArrayList<StripAction>();
            if (character.getTraits().contains(Trait.EXTRAS)) {
                actions.add(StripAction.EXTRAS);
                actions.add(StripAction.EXTRAS);
                actions.add(StripAction.EXTRAS);
                actions.add(StripAction.EXTRAS);
            }
            if (character.getTraits().contains(Trait.SHY)) {
                actions.add(StripAction.SHY);
            }
            if (character.getTraits().contains(Trait.UNINHIBITED)) {
                actions.add(StripAction.UNHINIBITED);
                actions.add(StripAction.LICKPOLE);
            }
            if (character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 7L) {
                actions.add(StripAction.SEXSMELL);
            }
            if (character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
                actions.add(StripAction.SEXSMELL);
            }
            if (character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
                actions.add(StripAction.SEXSMELL);
            }
            if (character.getTraits().contains(Trait.STIFF)) {
                actions.add(StripAction.STIFF);
            }
            if (character.getTraits().contains(Trait.HORNY)) {
                actions.add(StripAction.RUBCLIT);
            }
            if (character.getTraits().contains(Trait.FIT)) {
                actions.add(StripAction.FIT);
            }
            if (character.getTraits().contains(Trait.FRAGILE)) {
                actions.add(StripAction.FRAGILE);
            }
            if (character.getTraits().contains(Trait.OILY)) {
                actions.add(StripAction.OILY);
            }
            if (character.getTraits().contains(Trait.SUBMISSIVE)) {
                actions.add(StripAction.SUBMISSIVE);
            }
            if (character.getTraits().contains(Trait.CUMSLUT)) {
                actions.add(StripAction.CUMDRINK);
            }
            if (character.getTraits().contains(Trait.AFLEURDEPEAU)) {
                actions.add(StripAction.FONDLE);
            }
            if (character.getTraits().contains(Trait.FELINEHEAT) && Jasbro.getInstance().getData().getDay() % 15 == 0) {
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
                actions.add(StripAction.FELINEORGY);
            }
            if (character.getFinalValue(SpecializationAttribute.COOKING) > 255) {
                actions.add(StripAction.CREAM);
            }
            if (Util.getInt(0, 100) >= 10 + actions.size() * 6 || this.getCustomers().size() <= 10 || actions.size() <= 0) break block167;
            this.characterAction.put(character, (StripAction)actions.get(Util.getInt(0, actions.size())));
            message = null;
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
            a = Util.getInt(0, this.getCustomers().size() - 1);
            arg = new Object[]{this.getCustomers().get(a).getName()};
            rnd = 0;
            block9 : switch (1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[this.characterAction.get(character).ordinal()]) {
                case 1: {
                    if (this.getCustomers().get(a).getType() == CustomerType.BUM) break;
                    character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    extra = this.getCustomers().get(a).getMoney() * character.getFinalValue(SpecializationAttribute.STRIP) / 300;
                    this.modifyIncome(extra);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                    message = TextUtil.t("STRIP.extras", (Person)character, new Object[]{this.getCustomers().get(a).getStatusName(), this.getCustomers().get(a).getName(), extra});
                    if (this.getCustomers().get(a).getPreferredSextype() == Sextype.VAGINAL && character.getAllowedServices().isAllowed(Sextype.VAGINAL)) {
                        if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                            if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.NYMPHO) || character.getTraits().contains(Trait.SLUT) || character.getTraits().contains(Trait.HORNY))) {
                                this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.VAGINAL, character));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.two", character);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 5, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                character.getFame().modifyFame(50.0);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 10, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.VAGINAL, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.one", character);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 7, this);
                            }
                        }
                        if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) break;
                        if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.NYMPHO) || character.getTraits().contains(Trait.KINKY) || character.getTraits().contains(Trait.SENSITIVE))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.VAGINAL, character));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                            message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.two", character);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
                            this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                            character.getFame().modifyFame(50.0);
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                            break;
                        }
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.VAGINAL, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.one", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                        if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                            this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }
                        if (Util.getInt(1, 2) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.VERYHORNY) break;
                        this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        break;
                    }
                    if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ANAL && character.getAllowedServices().isAllowed(Sextype.ANAL)) {
                        if (this.getCustomers().get(a).getGender() == Gender.MALE && Util.getInt(0, 10) > 5) {
                            if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.ROWDYRUMP) || character.getTraits().contains(Trait.AMBITOUSLOVER) || character.getTraits().contains(Trait.HORNY))) {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ANAL, character));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.two", character);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 5, this);
                                character.getFame().modifyFame(50.0);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 10, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ANAL, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.one", character);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);
                                }
                            }
                        } else {
                            analBead = 2 + character.getFinalValue(Sextype.ANAL) / 10;
                            if (character.getTraits().contains(Trait.DEEPLOVE)) {
                                analBead = (int)((double)analBead * 1.5);
                            }
                            arg2 = new Object[]{analBead, this.getCustomers().get(Util.getInt(0, this.getCustomers().size())).getName()};
                            if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.KINKY) || character.getTraits().contains(Trait.SUBMISSIVE) || character.getTraits().contains(Trait.UNINHIBITED))) {
                                this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.ANAL, character));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.two", (Person)character, arg2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                                character.getFame().modifyFame(50.0);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(analBead + character.getFinalValue(Sextype.ANAL) / 12, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ANAL, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.one", (Person)character, arg2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 12, this);
                                }
                            }
                        }
                        break;
                    }
                    if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ORAL && character.getAllowedServices().isAllowed(Sextype.ORAL)) {
                        if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                            if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.CUMSLUT) || character.getTraits().contains(Trait.SENSUALTONGUE) || character.getTraits().contains(Trait.ABSORPTION))) {
                                this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.ORAL, character));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.two", character);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 5, this);
                                character.getFame().modifyFame(50.0);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 10, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ORAL, character));
                                message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.one", character);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                                this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 7, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                            }
                        }
                        if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) break;
                        if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.SENSUALTONGUE) || character.getTraits().contains(Trait.OPENMINDED) || character.getTraits().contains(Trait.HORNY))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.ORAL, character));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                            message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.two", character);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
                            this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                            character.getFame().modifyFame(50.0);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                            break;
                        }
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ORAL, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.one", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 7, this);
                        if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                            this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }
                        if (Util.getInt(1, 2) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.VERYHORNY) break;
                        this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        break;
                    }
                    if (this.getCustomers().get(a).getPreferredSextype() != Sextype.TITFUCK || !character.getAllowedServices().isAllowed(Sextype.TITFUCK) || character.getGender() != Gender.FEMALE || !character.getTraits().contains(Trait.SMALLBOOBS) && !character.getTraits().contains(Trait.BIGBOOBS)) ** GOTO lbl416
                    if (this.getCustomers().get(a).getGender() != Gender.MALE) ** GOTO lbl389
                    if (!character.getTraits().contains(Trait.SMALLBOOBS)) ** GOTO lbl355
                    if (Util.getInt(0, 10) <= 5 || !character.getTraits().contains(Trait.CUMSLUT) && !character.getTraits().contains(Trait.WILD) && !character.getTraits().contains(Trait.OILY)) ** GOTO lbl345
                    this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.TITFUCK, character));
                    this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                    message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", character);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                    this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 5, this);
                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                    }
                    if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                    }
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 10, this);
                        if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                            this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                        if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                        this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                    }
                    character.getFame().modifyFame(50.0);
                    ** GOTO lbl389
lbl345:
                    // 1 sources

                    this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, character));
                    message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", character);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                    this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 7, this);
                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                    }
                    if (Util.getInt(1, 2) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.VERYHORNY) ** GOTO lbl389
                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                    ** GOTO lbl389
lbl355:
                    // 1 sources

                    if (character.getTraits().contains(Trait.BIGBOOBS)) {
                        if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.WENCH) || character.getTraits().contains(Trait.WENCH) || character.getTraits().contains(Trait.COMETOMOMMY))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.TITFUCK, character));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                            message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", character);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                            this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 5, this);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                            character.getFame().modifyFame(50.0);
                        } else {
                            this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, character));
                            message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", character);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                            this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 7, this);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-15, 15), this);
                            }
                        }
                    }
lbl389:
                    // 8 sources

                    if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) break;
                    if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.EXHIBITIONIST) || character.getTraits().contains(Trait.UNINHIBITED) || character.getTraits().contains(Trait.TOUCHYFEELY))) {
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, character));
                        this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.two", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 10, this);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                            this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                        }
                        character.getFame().modifyFame(50.0);
                        break;
                    }
                    this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, character));
                    message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.one", character);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
                    this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(10 + character.getFinalValue(Sextype.FOREPLAY), this);
                    }
                    break;
lbl416:
                    // 1 sources

                    if (Util.getInt(0, 10) > 4 || this.getCustomers().size() < 8) {
                        if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.SENSITIVE) || character.getTraits().contains(Trait.NICEBODY) || character.getTraits().contains(Trait.TOUCHYFEELY))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.FOREPLAY, character));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                            message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.two", (Person)character, this.getCustomers().get(a));
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
                            character.getFame().modifyFame(50.0);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction((character.getFinalValue(Sextype.FOREPLAY) + character.getFinalValue(SpecializationAttribute.STRIP)) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                        } else {
                            this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.FOREPLAY, character));
                            message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.one", (Person)character, this.getCustomers().get(a));
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
                            character.getFame().modifyFame(20.0);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction((character.getFinalValue(Sextype.FOREPLAY) + character.getFinalValue(SpecializationAttribute.STRIP)) / 15, this);
                            }
                        }
                    } else if (Util.getInt(0, 10) > 5 && (character.getTraits().contains(Trait.GANGBANGQUEEN) || character.getTraits().contains(Trait.INSATIABLE) || character.getTraits().contains(Trait.MULTIFACETED))) {
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)((long)this.getCustomers().size() - 1L));
                        this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.GROUP, character));
                        this.getAttributeModifications().add(new AttributeModification(2.0f, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.group.two", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                        this.getHouse().modDirt(70);
                        character.getFame().modifyFame(450.0);
                        character.addCondition(new Buff.RoughenedUp());
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10 + character.getFinalValue(Sextype.GROUP) / 5, this);
                            if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
                        }
                    } else {
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(Util.getInt(1, 2 + this.getCustomers().size() / 4)));
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.GROUP, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.group.one", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                        character.getFame().modifyFame(100.0);
                        for (cust = 0; cust < 8; ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.GROUP) / 7, this);
                        }
                        for (cust = 8; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 2: {
                    if (character.getTraits().contains(Trait.PURE)) {
                        this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.shy.win", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10, this);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.shy.lose", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-10, this);
                        }
                    }
                    break;
                }
                case 3: {
                    if (character.getTraits().contains(Trait.LEWD) || Util.getInt(0, 3) == 2) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.lewd.win", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10, this);
                        }
                    } else {
                        message = TextUtil.t("STRIP.event.lewd.lose", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 5), this);
                        }
                    }
                    break;
                }
                case 4: {
                    if (character.getTraits().contains(Trait.LASCIVIOUS)) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.fragile.win", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(7, this);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.fragile.lose", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 5: {
                    if (character.getTraits().contains(Trait.PERFECTCONDITION) && Util.getInt(0, 3) == 1) {
                        this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.fit.win", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(skill / 5, this);
                        }
                    } else {
                        message = TextUtil.t("STRIP.event.fit.winwin", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10, this);
                            if (this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS && Util.getInt(1, 4) == 2) {
                                this.getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
                            }
                            if (this.getCustomers().get(cust).getStatus() != CustomerStatus.TIRED || Util.getInt(1, 4) != 2) continue;
                            this.getCustomers().get(cust).setStatus(CustomerStatus.LIVELY);
                        }
                    }
                    break;
                }
                case 6: {
                    message = TextUtil.t("STRIP.event.stiff", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(-skill / 2, this);
                    }
                    break;
                }
                case 7: {
                    message = TextUtil.t("STRIP.event.bigtits", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 7, this);
                    }
                    break;
                }
                case 8: {
                    if (Util.getInt(0, 3) == 2) {
                        this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.clumsy.win", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 7), this);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.3f, EssentialAttributes.MOTIVATION, character));
                        message = TextUtil.t("STRIP.event.clumsy.lose", (Person)character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 9: {
                    message = TextUtil.t("STRIP.event.rubclit", (Person)character, this.getCustomers().get(a));
                    this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(5 + character.getFinalValue(Sextype.FOREPLAY) / 10, this);
                    }
                    break;
                }
                case 10: {
                    message = TextUtil.t("STRIP.event.lickpole", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(5 + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 10, this);
                    }
                    break;
                }
                case 11: {
                    message = TextUtil.t("STRIP.event.oily", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(5 + character.getCharisma() / 5, this);
                    }
                    break;
                }
                case 12: {
                    message = TextUtil.t("STRIP.event.felineorgy", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(character.getCharisma() / 5, this);
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 5, this);
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 5, this);
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 5, this);
                        this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.ENERGY, character));
                        this.getAttributeModifications().add(new AttributeModification(0.5f, Sextype.GROUP, character));
                        if (Util.getInt(0, 100) < this.getCustomers().size()) {
                            character.addCondition(new Buff.Exhausted());
                        }
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                    break;
                }
                case 13: {
                    message = TextUtil.t("STRIP.event.cumdrink", (Person)character, this.getCustomers().get(a));
                    this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, character));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, character);
                    character.getFame().modifyFame(250.0);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(10 + (character.getFinalValue(Sextype.ORAL) + character.getFinalValue(Sextype.FOREPLAY)) / 12, this);
                    }
                    break;
                }
                case 14: {
                    message = Util.getInt(0, 2) == 0 ? TextUtil.t("STRIP.event.sexsmell.smell", (Person)character, this.getCustomers().get(a)) : TextUtil.t("STRIP.event.sexsmell.move", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
                    character.getFame().modifyFame(150.0);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(15, this);
                    }
                    break;
                }
                case 15: {
                    rnd = Util.getInt(0, 3);
                    if (character.getTraits().contains(Trait.BIGBOOBS) && rnd == 0) {
                        message = TextUtil.t("STRIP.event.fondle.bigbreasts", (Person)character, this.getCustomers().get(a));
                    }
                    message = character.getTraits().contains(Trait.SMALLBOOBS) != false && rnd == 1 ? TextUtil.t("STRIP.event.fondle.smallbreasts", (Person)character, this.getCustomers().get(a)) : TextUtil.t("STRIP.event.fondle.butt", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        if (this.getCustomers().get(cust).getType() != CustomerType.MINORNOBLE && this.getCustomers().get(cust).getType() != CustomerType.BUSINESSMAN && this.getCustomers().get(cust).getType() != CustomerType.LORD && this.getCustomers().get(cust).getType() != CustomerType.CELEBRITY) continue;
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                    }
                    break;
                }
                case 16: {
                    message = TextUtil.t("STRIP.event.cream", (Person)character, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                    }
                    break;
                }
                case 17: {
                    message = TextUtil.t("STRIP.event.submissive", (Person)character, arg) + "\n";
                    character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    rnd = Util.getInt(0, 3);
                    switch (rnd) {
                        case 0: {
                            if (character.getGender() == Gender.MALE) break block9;
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                            message = message + TextUtil.t("STRIP.event.submissive.vaginal", (Person)character, arg);
                            this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                            break block9;
                        }
                        case 1: {
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                            message = message + TextUtil.t("STRIP.event.submissive.anal", (Person)character, arg);
                            this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                            break block9;
                        }
                    }
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                    message = message + TextUtil.t("STRIP.event.submissive.oral", (Person)character, arg);
                    this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                }
            }
            if (message != null) {
                this.getMessages().add(new MessageData(message, image, character.getBackground()));
            }
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String messageText = TextUtil.t("strip.basic", this.getCharacter());
        this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, this.getCharacter()), this.getBackground());
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -30.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.7f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, SpecializationAttribute.STRIP));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.02f, BaseAttributeTypes.STRENGTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.STAMINA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
        }
        return modifications;
    }

    @Override
    public int getAppeal() {
        return this.getCharacter().getCharisma() + this.getCharacter().getFinalValue(SpecializationAttribute.STRIP);
    }

    @Override
    public int getMaxAttendees() {
        return 20 + this.bonus;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    static class 1 {
        static final /* synthetic */ int[] $SwitchMap$jasbro$game$events$business$CustomerType;
        static final /* synthetic */ int[] $SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction;

        static {
            $SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction = new int[StripAction.values().length];
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.EXTRAS.ordinal()] = 1;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.SHY.ordinal()] = 2;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.UNHINIBITED.ordinal()] = 3;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.FRAGILE.ordinal()] = 4;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.FIT.ordinal()] = 5;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.STIFF.ordinal()] = 6;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.BIGTITS.ordinal()] = 7;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.CLUMSY.ordinal()] = 8;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.RUBCLIT.ordinal()] = 9;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.LICKPOLE.ordinal()] = 10;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.OILY.ordinal()] = 11;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.FELINEORGY.ordinal()] = 12;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.CUMDRINK.ordinal()] = 13;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.SEXSMELL.ordinal()] = 14;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.FONDLE.ordinal()] = 15;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.CREAM.ordinal()] = 16;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[StripAction.SUBMISSIVE.ordinal()] = 17;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            $SwitchMap$jasbro$game$events$business$CustomerType = new int[CustomerType.values().length];
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.PEASANT.ordinal()] = 1;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.SOLDIER.ordinal()] = 2;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.MERCHANT.ordinal()] = 3;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.BUSINESSMAN.ordinal()] = 4;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.MINORNOBLE.ordinal()] = 5;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.LORD.ordinal()] = 6;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                1.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.CELEBRITY.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }
    }

    public static enum StripAction {
        EXTRAS,
        SHY,
        FRAGILE,
        STIFF,
        CLUMSY,
        SUBMISSIVE,
        UNHINIBITED,
        FIT,
        WILD,
        OILY,
        BIGTITS,
        RUBCLIT,
        LICKPOLE,
        CUMDRINK,
        FONDLE,
        CREAM,
        SEXSMELL,
        FELINEORGY;

    }
}

