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
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
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

public class Attend
extends RunningActivity
implements BusinessSecondaryActivity {
    private MessageData messageData;
    private List<Charakter> dancers = new ArrayList<Charakter>();
    private List<Charakter> bartenders = new ArrayList<Charakter>();
    private Map<Charakter, Strip.StripAction> stripperAction = new HashMap<Charakter, Strip.StripAction>();
    private Map<Charakter, Bartend.BarAction> bartenderAction = new HashMap<Charakter, Bartend.BarAction>();

    @Override
    public void init() {
        for (Charakter character : this.getCharacters()) {
            if (character.getSpecializations().contains(SpecializationType.DANCER) && this.dancers.size() < 2) {
                this.dancers.add(character);
                continue;
            }
            this.bartenders.add(character);
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void perform() {
        amountEarned = 0;
        stripBonus = false;
        stripSkill = 0;
        if (!this.dancers.isEmpty()) {
            for (Charakter stripper : this.dancers) {
                stripSkill += stripper.getCharisma() / 5 + stripper.getFinalValue(SpecializationAttribute.STRIP) / 5 + 1;
            }
        }
        pay = 0;
        portionPay = 0.0f;
        for (Customer customer : this.getCustomers()) {
            bartender = this.bartenders.get(Util.getInt(0, this.bartenders.size()));
            skill = 5 + bartender.getFinalValue(BaseAttributeTypes.CHARISMA) / 10 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 10 + bartender.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 10;
            switch (SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[customer.getType().ordinal()]) {
                case 1: {
                    portionPay = 0.03f;
                    skill = (int)((float)skill + (float)stripSkill * 0.8f);
                    break;
                }
                case 2: {
                    portionPay = 0.02f;
                    skill = (int)((float)skill + (float)stripSkill * 0.7f);
                    break;
                }
                case 3: {
                    portionPay = 0.015f;
                    skill = (int)((float)skill + (float)stripSkill * 0.5f);
                    break;
                }
                case 4: {
                    portionPay = 0.01f;
                    skill = (int)((float)skill + (float)stripSkill * 0.3f);
                    break;
                }
                case 5: {
                    portionPay = 0.005f;
                    skill = (int)((float)skill + (float)stripSkill * 0.2f);
                    break;
                }
                case 6: {
                    portionPay = 0.001f;
                    skill = (int)((float)skill + (float)stripSkill * 0.1f);
                    break;
                }
                case 7: {
                    portionPay = 5.0E-4f;
                    skill = (int)((float)skill + (float)stripSkill * 0.07f);
                    break;
                }
                default: {
                    portionPay = 0.09f;
                }
            }
            customer.addToSatisfaction((int)((float)skill * portionPay * 10.0f), this);
            pay = (int)((float)customer.getMoney() * portionPay * (float)Util.getInt(50, 125) / 100.0f);
            customer.payFixed(pay += 5 + customer.getMoney() * bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 1200);
            amountEarned += pay;
            customer.changePayModifier(0.2f);
        }
        this.modifyIncome(amountEarned);
        arguments = new Object[]{TextUtil.listCharacters(this.dancers), TextUtil.listCharacters(this.bartenders), this.getCustomers().size(), amountEarned};
        this.messageData.addToMessage("\n" + TextUtil.t("cabaret.result", arguments));
        for (Charakter stripper2 : this.dancers) {
            actions = new ArrayList<Enum>();
            if (stripper2.getTraits().contains(Trait.EXTRAS)) {
                actions.add(Strip.StripAction.EXTRAS);
                actions.add(Strip.StripAction.EXTRAS);
                actions.add(Strip.StripAction.EXTRAS);
                actions.add(Strip.StripAction.EXTRAS);
            }
            if (stripper2.getTraits().contains(Trait.SHY)) {
                actions.add(Strip.StripAction.SHY);
            }
            if (stripper2.getTraits().contains(Trait.UNINHIBITED)) {
                actions.add(Strip.StripAction.UNHINIBITED);
                actions.add(Strip.StripAction.LICKPOLE);
            }
            if (stripper2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 7L) {
                actions.add(Strip.StripAction.SEXSMELL);
            }
            if (stripper2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
                actions.add(Strip.StripAction.SEXSMELL);
            }
            if (stripper2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
                actions.add(Strip.StripAction.SEXSMELL);
            }
            if (stripper2.getTraits().contains(Trait.STIFF)) {
                actions.add(Strip.StripAction.STIFF);
            }
            if (stripper2.getTraits().contains(Trait.HORNY)) {
                actions.add(Strip.StripAction.RUBCLIT);
            }
            if (stripper2.getTraits().contains(Trait.FIT)) {
                actions.add(Strip.StripAction.FIT);
            }
            if (stripper2.getTraits().contains(Trait.FRAGILE)) {
                actions.add(Strip.StripAction.FRAGILE);
            }
            if (stripper2.getTraits().contains(Trait.OILY)) {
                actions.add(Strip.StripAction.OILY);
            }
            if (stripper2.getTraits().contains(Trait.SUBMISSIVE)) {
                actions.add(Strip.StripAction.SUBMISSIVE);
            }
            if (stripper2.getTraits().contains(Trait.CUMSLUT)) {
                actions.add(Strip.StripAction.CUMDRINK);
            }
            if (stripper2.getTraits().contains(Trait.AFLEURDEPEAU)) {
                actions.add(Strip.StripAction.FONDLE);
            }
            if (stripper2.getTraits().contains(Trait.FELINEHEAT) && Jasbro.getInstance().getData().getDay() % 15 == 0) {
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
                actions.add(Strip.StripAction.FELINEORGY);
            }
            if (stripper2.getFinalValue(SpecializationAttribute.COOKING) > 255) {
                actions.add(Strip.StripAction.CREAM);
            }
            if (Util.getInt(0, 100) >= 10 + actions.size() * 6 || this.getCustomers().size() <= 10 || actions.size() <= 0) continue;
            this.stripperAction.put(stripper2, (Strip.StripAction)actions.get(Util.getInt(0, actions.size())));
            message = null;
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
            a = Util.getInt(0, this.getCustomers().size() - 1);
            arg = new Object[]{this.getCustomers().get(a).getName()};
            rnd = 0;
            block9 : switch (SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[this.stripperAction.get(stripper2).ordinal()]) {
                case 1: {
                    if (this.getCustomers().get(a).getType() == CustomerType.BUM) break;
                    stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    extra = this.getCustomers().get(a).getMoney() * stripper2.getFinalValue(SpecializationAttribute.STRIP) / 300;
                    this.modifyIncome(extra);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                    message = TextUtil.t("STRIP.extras", (Person)stripper2, new Object[]{this.getCustomers().get(a).getStatusName(), this.getCustomers().get(a).getName(), extra});
                    if (this.getCustomers().get(a).getPreferredSextype() == Sextype.VAGINAL && stripper2.getAllowedServices().isAllowed(Sextype.VAGINAL)) {
                        if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                            if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.NYMPHO) || stripper2.getTraits().contains(Trait.SLUT) || stripper2.getTraits().contains(Trait.HORNY))) {
                                this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.VAGINAL, stripper2));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.two", stripper2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, stripper2);
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 5, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                stripper2.getFame().modifyFame(50.0);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 10, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.VAGINAL, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.one", stripper2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, stripper2);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 7, this);
                            }
                        }
                        if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) break;
                        if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.NYMPHO) || stripper2.getTraits().contains(Trait.KINKY) || stripper2.getTraits().contains(Trait.SENSITIVE))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.VAGINAL, stripper2));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                            message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.two", stripper2);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, stripper2);
                            this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                            stripper2.getFame().modifyFame(50.0);
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                            break;
                        }
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.VAGINAL, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.one", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                        if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                            this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }
                        if (Util.getInt(1, 2) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.VERYHORNY) break;
                        this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        break;
                    }
                    if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ANAL && stripper2.getAllowedServices().isAllowed(Sextype.ANAL)) {
                        if (this.getCustomers().get(a).getGender() == Gender.MALE && Util.getInt(0, 10) > 5) {
                            if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.ROWDYRUMP) || stripper2.getTraits().contains(Trait.AMBITOUSLOVER) || stripper2.getTraits().contains(Trait.HORNY))) {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ANAL, stripper2));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.two", stripper2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 5, this);
                                stripper2.getFame().modifyFame(50.0);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 10, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ANAL, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.one", stripper2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 7, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 7, this);
                                }
                            }
                        } else {
                            analBead = 2 + stripper2.getFinalValue(Sextype.ANAL) / 10;
                            if (stripper2.getTraits().contains(Trait.DEEPLOVE)) {
                                analBead = (int)((double)analBead * 1.5);
                            }
                            arg2 = new Object[]{analBead, this.getCustomers().get(Util.getInt(0, this.getCustomers().size())).getName()};
                            if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.KINKY) || stripper2.getTraits().contains(Trait.SUBMISSIVE) || stripper2.getTraits().contains(Trait.UNINHIBITED))) {
                                this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.ANAL, stripper2));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.two", (Person)stripper2, arg2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                                stripper2.getFame().modifyFame(50.0);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(analBead + stripper2.getFinalValue(Sextype.ANAL) / 12, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ANAL, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.one", (Person)stripper2, arg2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, stripper2);
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 7, this);
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 12, this);
                                }
                            }
                        }
                        break;
                    }
                    if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ORAL && stripper2.getAllowedServices().isAllowed(Sextype.ORAL)) {
                        if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                            if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.CUMSLUT) || stripper2.getTraits().contains(Trait.SENSUALTONGUE) || stripper2.getTraits().contains(Trait.ABSORPTION))) {
                                this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.ORAL, stripper2));
                                this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.two", stripper2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, stripper2);
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 5, this);
                                stripper2.getFame().modifyFame(50.0);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                    this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 10, this);
                                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                        this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                    }
                                    if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                }
                            } else {
                                this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ORAL, stripper2));
                                message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.one", stripper2);
                                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, stripper2);
                                this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 7, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                                }
                                if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                            }
                        }
                        if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) break;
                        if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.SENSUALTONGUE) || stripper2.getTraits().contains(Trait.OPENMINDED) || stripper2.getTraits().contains(Trait.HORNY))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.ORAL, stripper2));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                            message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.two", stripper2);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, stripper2);
                            this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                            stripper2.getFame().modifyFame(50.0);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                            break;
                        }
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.ORAL, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.one", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 7, this);
                        if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                            this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }
                        if (Util.getInt(1, 2) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.VERYHORNY) break;
                        this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        break;
                    }
                    if (this.getCustomers().get(a).getPreferredSextype() != Sextype.TITFUCK || !stripper2.getAllowedServices().isAllowed(Sextype.TITFUCK) || stripper2.getGender() != Gender.FEMALE || !stripper2.getTraits().contains(Trait.SMALLBOOBS) && !stripper2.getTraits().contains(Trait.BIGBOOBS)) ** GOTO lbl422
                    if (this.getCustomers().get(a).getGender() != Gender.MALE) ** GOTO lbl395
                    if (!stripper2.getTraits().contains(Trait.SMALLBOOBS)) ** GOTO lbl361
                    if (Util.getInt(0, 10) <= 5 || !stripper2.getTraits().contains(Trait.CUMSLUT) && !stripper2.getTraits().contains(Trait.WILD) && !stripper2.getTraits().contains(Trait.OILY)) ** GOTO lbl351
                    this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.TITFUCK, stripper2));
                    this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                    message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", stripper2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                    this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 5, this);
                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                    }
                    if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                    }
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 10, this);
                        if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                            this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                        if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                        this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                    }
                    stripper2.getFame().modifyFame(50.0);
                    ** GOTO lbl395
lbl351:
                    // 1 sources

                    this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, stripper2));
                    message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", stripper2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                    this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 7, this);
                    if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                    }
                    if (Util.getInt(1, 2) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.VERYHORNY) ** GOTO lbl395
                    this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                    ** GOTO lbl395
lbl361:
                    // 1 sources

                    if (stripper2.getTraits().contains(Trait.BIGBOOBS)) {
                        if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.WENCH) || stripper2.getTraits().contains(Trait.WENCH) || stripper2.getTraits().contains(Trait.COMETOMOMMY))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.TITFUCK, stripper2));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                            message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", stripper2);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                            this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 5, this);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                            stripper2.getFame().modifyFame(50.0);
                        } else {
                            this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, stripper2));
                            message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", stripper2);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                            this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 7, this);
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
lbl395:
                    // 8 sources

                    if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) break;
                    if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.EXHIBITIONIST) || stripper2.getTraits().contains(Trait.UNINHIBITED) || stripper2.getTraits().contains(Trait.TOUCHYFEELY))) {
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.two", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                            this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                        }
                        stripper2.getFame().modifyFame(50.0);
                        break;
                    }
                    this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.TITFUCK, stripper2));
                    message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.one", stripper2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, stripper2);
                    this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(10 + stripper2.getFinalValue(Sextype.FOREPLAY), this);
                    }
                    break;
lbl422:
                    // 1 sources

                    if (Util.getInt(0, 10) > 4 || this.getCustomers().size() < 8) {
                        if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.SENSITIVE) || stripper2.getTraits().contains(Trait.NICEBODY) || stripper2.getTraits().contains(Trait.TOUCHYFEELY))) {
                            this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.FOREPLAY, stripper2));
                            this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                            message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.two", (Person)stripper2, this.getCustomers().get(a));
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, stripper2);
                            stripper2.getFame().modifyFame(50.0);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction((stripper2.getFinalValue(Sextype.FOREPLAY) + stripper2.getFinalValue(SpecializationAttribute.STRIP)) / 10, this);
                                if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS)) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                }
                                if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(cust).getStatus() != CustomerStatus.HYPED) continue;
                                this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                            }
                        } else {
                            this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.FOREPLAY, stripper2));
                            message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.one", (Person)stripper2, this.getCustomers().get(a));
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, stripper2);
                            stripper2.getFame().modifyFame(20.0);
                            if (Util.getInt(1, 3) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                            }
                            if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                                this.getCustomers().get(cust).addToSatisfaction((stripper2.getFinalValue(Sextype.FOREPLAY) + stripper2.getFinalValue(SpecializationAttribute.STRIP)) / 15, this);
                            }
                        }
                    } else if (Util.getInt(0, 10) > 5 && (stripper2.getTraits().contains(Trait.GANGBANGQUEEN) || stripper2.getTraits().contains(Trait.INSATIABLE) || stripper2.getTraits().contains(Trait.MULTIFACETED))) {
                        stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)((long)this.getCustomers().size() - 1L));
                        this.getAttributeModifications().add(new AttributeModification(2.5f, Sextype.GROUP, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(2.0f, EssentialAttributes.MOTIVATION, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.group.two", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, stripper2);
                        this.getHouse().modDirt(70);
                        stripper2.getFame().modifyFame(450.0);
                        stripper2.addCondition(new Buff.RoughenedUp());
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10 + stripper2.getFinalValue(Sextype.GROUP) / 5, this);
                            if (Util.getInt(1, 3) != 1 || this.getCustomers().get(cust).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
                        }
                    } else {
                        stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(Util.getInt(1, 2 + this.getCustomers().size() / 4)));
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.GROUP, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.group.one", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, stripper2);
                        stripper2.getFame().modifyFame(100.0);
                        for (cust = 0; cust < 8; ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.GROUP) / 7, this);
                        }
                        for (cust = 8; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 2: {
                    if (stripper2.getTraits().contains(Trait.PURE)) {
                        this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.shy.win", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10, this);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.shy.lose", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-10, this);
                        }
                    }
                    break;
                }
                case 3: {
                    if (stripper2.getTraits().contains(Trait.LEWD) || Util.getInt(0, 3) == 2) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.lewd.win", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(10, this);
                        }
                    } else {
                        message = TextUtil.t("STRIP.event.lewd.lose", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 5), this);
                        }
                    }
                    break;
                }
                case 4: {
                    if (stripper2.getTraits().contains(Trait.LASCIVIOUS)) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.fragile.win", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(7, this);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.fragile.lose", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 5: {
                    if (stripper2.getTraits().contains(Trait.PERFECTCONDITION) && Util.getInt(0, 3) == 1) {
                        this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.fit.win", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(stripSkill / 5, this);
                        }
                    } else {
                        message = TextUtil.t("STRIP.event.fit.winwin", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
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
                    message = TextUtil.t("STRIP.event.stiff", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(-stripSkill / 2, this);
                    }
                    break;
                }
                case 7: {
                    message = TextUtil.t("STRIP.event.bigtits", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 7, this);
                    }
                    break;
                }
                case 8: {
                    if (Util.getInt(0, 3) == 2) {
                        this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.clumsy.win", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 7), this);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.3f, EssentialAttributes.MOTIVATION, stripper2));
                        message = TextUtil.t("STRIP.event.clumsy.lose", (Person)stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                        for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                            this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 9: {
                    message = TextUtil.t("STRIP.event.rubclit", (Person)stripper2, this.getCustomers().get(a));
                    this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(5 + stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                    }
                    break;
                }
                case 10: {
                    message = TextUtil.t("STRIP.event.lickpole", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(5 + stripper2.getFinalValue(SpecializationAttribute.SEDUCTION) / 10, this);
                    }
                    break;
                }
                case 11: {
                    message = TextUtil.t("STRIP.event.oily", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(5 + stripper2.getCharisma() / 5, this);
                    }
                    break;
                }
                case 12: {
                    message = TextUtil.t("STRIP.event.felineorgy", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getCharisma() / 5, this);
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 5, this);
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 5, this);
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 5, this);
                        this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.ENERGY, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(0.5f, Sextype.GROUP, stripper2));
                        if (Util.getInt(0, 100) < this.getCustomers().size()) {
                            stripper2.addCondition(new Buff.Exhausted());
                        }
                        stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                    break;
                }
                case 13: {
                    message = TextUtil.t("STRIP.event.cumdrink", (Person)stripper2, this.getCustomers().get(a));
                    this.getAttributeModifications().add(new AttributeModification(1.0f, EssentialAttributes.MOTIVATION, stripper2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, stripper2);
                    stripper2.getFame().modifyFame(250.0);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(10 + (stripper2.getFinalValue(Sextype.ORAL) + stripper2.getFinalValue(Sextype.FOREPLAY)) / 12, this);
                    }
                    break;
                }
                case 14: {
                    message = Util.getInt(0, 2) == 0 ? TextUtil.t("STRIP.event.sexsmell.smell", (Person)stripper2, this.getCustomers().get(a)) : TextUtil.t("STRIP.event.sexsmell.move", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, stripper2);
                    stripper2.getFame().modifyFame(150.0);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(15, this);
                    }
                    break;
                }
                case 15: {
                    rnd = Util.getInt(0, 3);
                    if (stripper2.getTraits().contains(Trait.BIGBOOBS) && rnd == 0) {
                        message = TextUtil.t("STRIP.event.fondle.bigbreasts", (Person)stripper2, this.getCustomers().get(a));
                    }
                    message = stripper2.getTraits().contains(Trait.SMALLBOOBS) != false && rnd == 1 ? TextUtil.t("STRIP.event.fondle.smallbreasts", (Person)stripper2, this.getCustomers().get(a)) : TextUtil.t("STRIP.event.fondle.butt", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        if (this.getCustomers().get(cust).getType() != CustomerType.MINORNOBLE && this.getCustomers().get(cust).getType() != CustomerType.BUSINESSMAN && this.getCustomers().get(cust).getType() != CustomerType.LORD && this.getCustomers().get(cust).getType() != CustomerType.CELEBRITY) continue;
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                    }
                    break;
                }
                case 16: {
                    message = TextUtil.t("STRIP.event.cream", (Person)stripper2, this.getCustomers().get(a));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, stripper2);
                    for (cust = 0; cust < this.getCustomers().size(); ++cust) {
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                    }
                    break;
                }
                case 17: {
                    message = TextUtil.t("STRIP.event.submissive", (Person)stripper2, arg) + "\n";
                    stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    rnd = Util.getInt(0, 3);
                    switch (rnd) {
                        case 0: {
                            if (stripper2.getGender() == Gender.MALE) break block9;
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, stripper2);
                            message = message + TextUtil.t("STRIP.event.submissive.vaginal", (Person)stripper2, arg);
                            this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                            break block9;
                        }
                        case 1: {
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                            message = message + TextUtil.t("STRIP.event.submissive.anal", (Person)stripper2, arg);
                            this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                            break block9;
                        }
                    }
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, stripper2);
                    message = message + TextUtil.t("STRIP.event.submissive.oral", (Person)stripper2, arg);
                    this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                }
            }
            if (message == null) continue;
            this.getMessages().add(new MessageData(message, image, stripper2.getBackground()));
        }
        for (Charakter bartender2 : this.bartenders) {
            actions = new ArrayList<E>();
            if (bartender2.getTraits().contains(Trait.DATASS)) {
                actions.add(Bartend.BarAction.GROPE);
                actions.add(Bartend.BarAction.FLIP);
                actions.add(Bartend.BarAction.LOOK);
                actions.add(Bartend.BarAction.CROWD);
                actions.add(Bartend.BarAction.SLAP);
            }
            if (bartender2.getTraits().contains(Trait.FLIRTY)) {
                actions.add(Bartend.BarAction.SITLAP);
                actions.add(Bartend.BarAction.SITGROUP);
                actions.add(Bartend.BarAction.CHAT);
                actions.add(Bartend.BarAction.TEASELOT);
            }
            if (bartender2.getTraits().contains(Trait.UNDERTHETABLE)) {
                actions.add(Bartend.BarAction.FUCK);
                actions.add(Bartend.BarAction.BLOWJOB);
                actions.add(Bartend.BarAction.GROUP);
            }
            if (bartender2.getTraits().contains(Trait.UNDERTHETABLE) && bartender2.getTraits().contains(Trait.WENCH)) {
                actions.add(Bartend.BarAction.FUCK);
                actions.add(Bartend.BarAction.BLOWJOB);
                actions.add(Bartend.BarAction.GROUP);
            }
            if (bartender2.getTraits().contains(Trait.OUTGOING)) {
                actions.add(Bartend.BarAction.LOUDBUNCH);
                actions.add(Bartend.BarAction.SMALLTALK);
            }
            if (bartender2.getTraits().contains(Trait.STUPID)) {
                actions.add(Bartend.BarAction.DUMBBJ);
                actions.add(Bartend.BarAction.DUMBFUCK);
            }
            if (bartender2.getIntelligence() < 5) {
                actions.add(Bartend.BarAction.DUMBBJ);
                actions.add(Bartend.BarAction.DUMBFUCK);
            }
            if (bartender2.getTraits().contains(Trait.OUTGOING) && bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > 300) {
                actions.add(Bartend.BarAction.LOUDBUNCH);
                actions.add(Bartend.BarAction.SMALLTALK);
            }
            if (bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 5L) {
                actions.add(Bartend.BarAction.SEXSMELL);
            }
            if (bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 10L) {
                actions.add(Bartend.BarAction.SEXSMELL);
            }
            if (bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 15L) {
                actions.add(Bartend.BarAction.SEXSMELL);
            }
            if (bartender2.getTraits().contains(Trait.BIGBOOBS)) {
                actions.add(Bartend.BarAction.BIGBOOB);
            }
            if (bartender2.getTraits().contains(Trait.SMALLBOOBS)) {
                actions.add(Bartend.BarAction.SMALLBOOB);
            }
            if (bartender2.getTraits().contains(Trait.LOLI)) {
                actions.add(Bartend.BarAction.LOLI);
            }
            if (bartender2.getTraits().contains(Trait.CLUMSY)) {
                actions.add(Bartend.BarAction.BREAKSTUFF);
            }
            if (bartender2.getTraits().contains(Trait.FRAGILE)) {
                actions.add(Bartend.BarAction.TIRED);
            }
            if (bartender2.getTraits().contains(Trait.SHY)) {
                actions.add(Bartend.BarAction.SHY);
            }
            if (bartender2.getTraits().contains(Trait.LAGOMORPHQUICKY)) {
                actions.add(Bartend.BarAction.LAGOMORPHQUICKIE);
            }
            if (bartender2.getTraits().contains(Trait.LAGOMORPHORGY) && Util.getInt(0, 100) < 70) {
                actions.add(Bartend.BarAction.LAGOMORPHORGY);
            }
            if (bartender2.getTraits().contains(Trait.RESTAURATEUR) || bartender2.getFinalValue(SpecializationAttribute.COOKING) > 150) {
                actions.add(Bartend.BarAction.SNACKS);
            }
            if (Util.getInt(0, 100) >= 10 + actions.size() * 6 || this.getCustomers().size() <= 10 || actions.size() <= 0) continue;
            this.bartenderAction.put(bartender2, (Bartend.BarAction)actions.get(Util.getInt(0, actions.size())));
            message = null;
            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
            a = Util.getInt(0, this.getCustomers().size() - 1);
            arg = new Object[]{this.getCustomers().get(a).getName()};
            rnd = 0;
            switch (SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[this.bartenderAction.get(bartender2).ordinal()]) {
                case 1: {
                    message = TextUtil.t("barevent.flip", (Person)bartender2, arg);
                    if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 7 || bartender2.getTraits().contains(Trait.OPENMINDED) || bartender2.getTraits().contains(Trait.OBEDIENT)) {
                        if (Util.getInt(0, 100) > 50) {
                            this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.OBEDIENCE, bartender2));
                            this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                            message = message + "\n" + TextUtil.t("barevent.flip.win", bartender2);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                            this.getCustomers().get(Util.getInt(1, this.getCustomers().size() - 1)).addToSatisfaction(5, this);
                            break;
                        }
                        this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.flip.okay", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                        for (Customer cust : this.getCustomers()) {
                            if (cust.getStatus() != CustomerStatus.LIVELY && cust.getStatus() != CustomerStatus.HAPPY && cust.getStatus() != CustomerStatus.DRUNK || Util.getInt(0, 100) <= 50) continue;
                            cust.addToSatisfaction(5, this);
                        }
                        break;
                    }
                    this.getAttributeModifications().add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.MOTIVATION, bartender2));
                    message = message + "\n" + TextUtil.t("barevent.flip.lose", bartender2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                    rnd = Util.getInt(1, this.getCustomers().size() - 1);
                    this.getCustomers().get(rnd).setStatus(CustomerStatus.PISSED);
                    this.getCustomers().get(rnd).addToSatisfaction(-20, this);
                    if (Util.getInt(1, 4) != 1 || this.getCustomers().get(rnd).getStatus() != CustomerStatus.DRUNK && this.getCustomers().get(rnd).getStatus() != CustomerStatus.VERYDRUNK) break;
                    this.getCustomers().get(rnd).setStatus(CustomerStatus.TIRED);
                    break;
                }
                case 2: {
                    message = TextUtil.t("barevent.grope", (Person)bartender2, arg);
                    if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 7 || bartender2.getTraits().contains(Trait.WENCH) || bartender2.getTraits().contains(Trait.TOUCHYFEELY) && Util.getInt(0, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.07f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
                        bartender2.getFame().modifyFame(15.0);
                        income = bartender2.getCharisma() / 2;
                        for (Customer customer : this.getCustomers()) {
                            if ((Util.getInt(1, 10) <= 4 || customer.getStatus() != CustomerStatus.HORNYSTATUS) && customer.getStatus() != CustomerStatus.VERYHORNY) continue;
                            customer.payFixed(bartender2.getCharisma() / 2);
                            this.modifyIncome(bartender2.getCharisma() / 2);
                            customer.addToSatisfaction(5, this);
                            income += bartender2.getCharisma() / 2;
                        }
                        gropeincome = new Object[]{income};
                        message = message + "\n" + TextUtil.t("barevent.grope.winwin", (Person)bartender2, gropeincome);
                        break;
                    }
                    if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 5 || bartender2.getTraits().contains(Trait.WENCH) || bartender2.getTraits().contains(Trait.TOUCHYFEELY)) {
                        this.getAttributeModifications().add(new AttributeModification(0.07f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.grope.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
                        bartender2.getFame().modifyFame(15.0);
                        for (Customer customer : this.getCustomers()) {
                            if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                                customer.setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                                customer.setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) != 1 || customer.getStatus() != CustomerStatus.LIVELY && customer.getStatus() != CustomerStatus.DRUNK) continue;
                            customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(-0.07f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.grope.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(-10, this);
                        }
                    }
                    break;
                }
                case 3: {
                    message = TextUtil.t("barevent.look", bartender2);
                    if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > Util.getInt(10, 25) || bartender2.getTraits().contains(Trait.OUTGOING) || bartender2.getTraits().contains(Trait.CROWDLOVER)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.look.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 5, this);
                            if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                                customer.setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                                customer.setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) != 1 || customer.getStatus() != CustomerStatus.LIVELY && customer.getStatus() != CustomerStatus.DRUNK) continue;
                            customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    } else {
                        message = message + "\n" + TextUtil.t("barevent.look.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.3f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(-10, this);
                        }
                    }
                    break;
                }
                case 4: {
                    message = TextUtil.t("barevent.crowd", bartender2);
                    if (bartender2.getFinalValue(SpecializationAttribute.STRIP) > 30 || bartender2.getTraits().contains(Trait.WILD)) {
                        this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.STAMINA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.05f, SpecializationAttribute.BARTENDING, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.55f, SpecializationAttribute.STRIP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.7f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.crowd.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(bartender2.getFinalValue(SpecializationAttribute.STRIP) / 9, this);
                            if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                                customer.setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                                customer.setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) != 1 || customer.getStatus() != CustomerStatus.LIVELY && customer.getStatus() != CustomerStatus.DRUNK) continue;
                            customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.05f, Sextype.FOREPLAY, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.crowd.lose", (Person)bartender2, arg);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(Util.getInt(-10, 15), this);
                            if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.SHYSTATUS) {
                                customer.setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SHYSTATUS || customer.getStatus() == CustomerStatus.LIVELY)) {
                                customer.setStatus(CustomerStatus.PISSED);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.HYPED || customer.getStatus() == CustomerStatus.DRUNK)) {
                                customer.setStatus(CustomerStatus.PISSED);
                            }
                            if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                                customer.setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                                customer.setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) != 1 || customer.getStatus() != CustomerStatus.LIVELY && customer.getStatus() != CustomerStatus.DRUNK) continue;
                            customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    }
                    break;
                }
                case 5: {
                    message = TextUtil.t("barevent.sitlap", bartender2);
                    if (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) > 15 && Util.getInt(1, 4) == 2) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.65f, SpecializationAttribute.BARTENDING, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.sitlap.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        rnd = Util.getInt(0, this.getCustomers().size());
                        this.getCustomers().get(rnd).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 2 + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 7) / 2, this);
                        this.getCustomers().get(rnd).payFixed(200);
                        if (Util.getInt(1, 6) == 1 && this.getCustomers().get(rnd).getStatus() == CustomerStatus.HORNYSTATUS) {
                            this.getCustomers().get(rnd).setStatus(CustomerStatus.VERYHORNY);
                        }
                        if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(rnd).getStatus() == CustomerStatus.SAD || this.getCustomers().get(rnd).getStatus() == CustomerStatus.PISSED)) {
                            this.getCustomers().get(rnd).setStatus(CustomerStatus.HAPPY);
                        }
                        if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(rnd).getStatus() == CustomerStatus.LIVELY || this.getCustomers().get(rnd).getStatus() == CustomerStatus.DRUNK)) {
                            this.getCustomers().get(rnd).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                        this.modifyIncome(200);
                        break;
                    }
                    message = message + "\n" + TextUtil.t("barevent.sitlap.lose", bartender2);
                    this.getAttributeModifications().add(new AttributeModification(-0.2f, EssentialAttributes.MOTIVATION, bartender2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                    break;
                }
                case 6: {
                    message = TextUtil.t("barevent.chat", (Person)bartender2, arg);
                    this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(0.85f, SpecializationAttribute.BARTENDING, bartender2));
                    if (Util.getInt(0, 20) > 10) {
                        message = message + "\n" + TextUtil.t("barevent.chat.win", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        this.getCustomers().get(a).addToSatisfaction(5, this);
                        if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.SAD || this.getCustomers().get(a).getStatus() == CustomerStatus.PISSED)) {
                            this.getCustomers().get(a).setStatus(CustomerStatus.HAPPY);
                        }
                        if (Util.getInt(1, 4) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.HAPPY && this.getCustomers().get(a).getStatus() != CustomerStatus.SHYSTATUS) break;
                        this.getCustomers().get(a).setStatus(CustomerStatus.LIVELY);
                        break;
                    }
                    message = message + "\n" + TextUtil.t("barevent.chat.lose", bartender2);
                    this.getAttributeModifications().add(new AttributeModification(-0.1f, EssentialAttributes.MOTIVATION, bartender2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                    break;
                }
                case 7: {
                    message = TextUtil.t("barevent.sitgroup", bartender2);
                    if (Util.getInt(1, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85f, SpecializationAttribute.BARTENDING, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        arg2 = new Object[]{this.getCustomers().size() * 10 - 10};
                        message = message + "\n" + TextUtil.t("barevent.sitgroup.win", (Person)bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (b = 0; b < this.getCustomers().size() / 10; ++b) {
                            this.getCustomers().get(b).payFixed(100);
                            this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE)) / 3, this);
                            this.modifyIncome(100);
                            if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(b).getStatus() == CustomerStatus.SAD || this.getCustomers().get(b).getStatus() == CustomerStatus.PISSED)) {
                                this.getCustomers().get(b).setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(b).getStatus() == CustomerStatus.SHYSTATUS || this.getCustomers().get(b).getStatus() == CustomerStatus.HAPPY)) {
                                this.getCustomers().get(b).setStatus(CustomerStatus.DRUNK);
                            }
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() != CustomerStatus.DRUNK) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.VERYDRUNK);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.75f, Sextype.FOREPLAY, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        arg2 = new Object[]{this.getCustomers().size() * 40 - 40};
                        message = message + "\n" + TextUtil.t("barevent.sitgroup.lose", (Person)bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (b = 0; b < this.getCustomers().size(); ++b) {
                            if (a < this.getCustomers().size() / 10) {
                                this.getCustomers().get(b).payFixed(400);
                                this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE)) / 3, this);
                                this.modifyIncome(400);
                                if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(b).getStatus() == CustomerStatus.SAD || this.getCustomers().get(b).getStatus() == CustomerStatus.PISSED)) {
                                    this.getCustomers().get(b).setStatus(CustomerStatus.HAPPY);
                                }
                                if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(b).getStatus() == CustomerStatus.SHYSTATUS || this.getCustomers().get(b).getStatus() == CustomerStatus.HAPPY)) {
                                    this.getCustomers().get(b).setStatus(CustomerStatus.DRUNK);
                                }
                                if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS || this.getCustomers().get(b).getStatus() == CustomerStatus.VERYHORNY) continue;
                                this.getCustomers().get(b).setStatus(CustomerStatus.HORNYSTATUS);
                                continue;
                            }
                            this.getCustomers().get(a).addToSatisfaction(-10, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.HYPED && this.getCustomers().get(a).getStatus() != CustomerStatus.LIVELY) continue;
                            this.getCustomers().get(a).setStatus(CustomerStatus.PISSED);
                        }
                    }
                    break;
                }
                case 8: {
                    message = TextUtil.t("barevent.teaselot", bartender2);
                    this.getAttributeModifications().add(new AttributeModification(0.3f, EssentialAttributes.MOTIVATION, bartender2));
                    if (Util.getInt(1, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.55f, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.teaselot.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (b = 0; b < this.getCustomers().size(); ++b) {
                            this.getCustomers().get(b).addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 10 + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 10, this);
                            if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(b).getStatus() == CustomerStatus.HORNYSTATUS || this.getCustomers().get(b).getStatus() == CustomerStatus.HYPED)) {
                                this.getCustomers().get(b).setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() != CustomerStatus.SAD && this.getCustomers().get(b).getStatus() != CustomerStatus.PISSED) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.75f, SpecializationAttribute.BARTENDING, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.teaselot.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (b = 0; b < this.getCustomers().size(); ++b) {
                            this.getCustomers().get(b).addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 10 + bartender2.getFinalValue(SpecializationAttribute.BARTENDING) / 10, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() != CustomerStatus.SAD && this.getCustomers().get(b).getStatus() != CustomerStatus.PISSED) continue;
                            this.getCustomers().get(a).setStatus(CustomerStatus.HAPPY);
                        }
                    }
                    break;
                }
                case 9: {
                    message = TextUtil.t("barevent.ninjablowjob", (Person)bartender2, arg);
                    cust = this.getCustomers().get(a);
                    blowjobPay = 25 + bartender2.getFinalValue(Sextype.ORAL) * bartender2.getFinalValue(Sextype.ORAL) / 10;
                    arg3 = new Object[]{this.getCustomers().get(a).getName(), blowjobPay};
                    if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 6 || bartender2.getTraits().contains(Trait.WENCH)) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.6f, Sextype.ORAL, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.65f, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.ninjablowjob.win", (Person)bartender2, (Person)cust, arg3);
                        message = message + "\n" + TextUtil.t("barevent.ninjablowjob.gold", (Person)bartender2, arg3);
                        image = cust.getGender() == Gender.MALE ? ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, bartender2) : ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, bartender2);
                        cust.addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL) / 6, this);
                        cust.payFixed(blowjobPay);
                        this.modifyIncome(blowjobPay);
                        if (bartender2.getTraits().contains(Trait.SLURPYSLURP) && bartender2.getFinalValue(Sextype.ORAL) > 25 && Util.getInt(0, 100) > 50) {
                            i = Util.getInt(1, 5);
                            total = 0;
                            for (z = 0; z < i + bartender2.getFinalValue(Sextype.ORAL) / 20; ++z) {
                                this.getCustomers().get(z).payFixed(blowjobPay * (100 - i) / 100);
                                this.modifyIncome(blowjobPay * (100 - i) / 100);
                                total += blowjobPay * (100 - i) / 100;
                                this.getCustomers().get(z).addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL) / 6, this);
                                if (cust.getStatus() == CustomerStatus.HORNYSTATUS) {
                                    cust.setStatus(CustomerStatus.LIVELY);
                                }
                                if (cust.getStatus() != CustomerStatus.SAD && cust.getStatus() != CustomerStatus.PISSED) continue;
                                cust.setStatus(CustomerStatus.HAPPY);
                            }
                            multiBJincome = new Object[]{total};
                            message = message + "\n" + TextUtil.t("barevent.ninjablowjob.winmore", (Person)bartender2, (Person)cust, multiBJincome);
                            message = Util.getInt(0, 100) > 50 ? message + "\n" + TextUtil.t("barevent.ninjablowjob.drink", bartender2) : message + "\n" + TextUtil.t("barevent.ninjablowjob.bukkake", bartender2);
                        }
                        if (cust.getStatus() == CustomerStatus.HORNYSTATUS) {
                            cust.setStatus(CustomerStatus.LIVELY);
                        }
                        if (cust.getStatus() != CustomerStatus.SAD && cust.getStatus() != CustomerStatus.PISSED) break;
                        cust.setStatus(CustomerStatus.HAPPY);
                        break;
                    }
                    message = message + "\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                    cust.addToSatisfaction(-100, this);
                    cust.payFixed(100);
                    this.modifyIncome(100);
                    cust.setStatus(CustomerStatus.PISSED);
                    break;
                }
                case 10: {
                    quickieAmount = 0;
                    sex2 = null;
                    tips = 0;
                    tip = 0;
                    image = Util.getInt(1, 100) > 50 ? ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2) : ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                    for (Customer cust2 : this.getCustomers()) {
                        if (cust2.getType() != CustomerType.SOLDIER && cust2.getStatus() != CustomerStatus.STRONGSTATUS || Util.getInt(0, 100) >= 30 || quickieAmount >= 3 + (bartender2.getFinalValue(Sextype.ANAL) + bartender2.getFinalValue(Sextype.VAGINAL)) / 5) continue;
                        sex2 = Util.getInt(1, 100) > 50 ? Sextype.VAGINAL : Sextype.ANAL;
                        this.getAttributeModifications().add(new AttributeModification(0.2f, sex2, bartender2));
                        cust2.addToSatisfaction(5, this);
                        ++quickieAmount;
                        if (Util.getInt(0, 100) >= 45) continue;
                        tip = Util.getInt(5, 10) + bartender2.getFinalValue(sex2) / 3;
                        tip = cust2.payFixed(tip);
                        tips += tip;
                    }
                    quickieArgs = new Object[]{quickieAmount, tips};
                    message = TextUtil.t("barevent.lagomorphquickie", (Person)bartender2, quickieArgs);
                    break;
                }
                case 11: {
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                    this.getAttributeModifications().add(new AttributeModification(1.1f, Sextype.VAGINAL, bartender2));
                    this.getCustomers().get(Util.getInt(0, this.getCustomers().size() - 1)).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                    message = TextUtil.t("barevent.dumbfuck", bartender2);
                    break;
                }
                case 12: {
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                    this.getAttributeModifications().add(new AttributeModification(1.1f, Sextype.ORAL, bartender2));
                    this.getCustomers().get(Util.getInt(0, this.getCustomers().size() - 1)).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                    message = TextUtil.t("barevent.bj", bartender2);
                    break;
                }
                case 13: {
                    rand2 = Util.getInt(10, 20);
                    fraction = Util.getInt(7, 10) - bartender2.getFinalValue(SpecializationAttribute.TRANSFORMATION) / 10;
                    arg5 = new Object[]{1 + this.getCustomers().size() / fraction, this.getCustomers().size() * rand2 / fraction};
                    message = TextUtil.t("barevent.lagomorphorgy", (Person)bartender2, arg5);
                    bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)(1L + (long)(this.getCustomers().size() / 10)));
                    this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(1.1f, Sextype.GROUP, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(0.85f, SpecializationAttribute.SEDUCTION, bartender2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                    for (b = 0; b < this.getCustomers().size() / fraction; ++b) {
                        this.getCustomers().get(b).payFixed(rand2);
                        this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                        if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                        this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                    }
                    break;
                }
                case 14: {
                    cust2 = this.getCustomers().get(a);
                    sex = null;
                    sex = Util.getInt(1, 100) > 50 ? Sextype.VAGINAL : Sextype.ANAL;
                    fuckPay = bartender2.getFinalValue(sex) * bartender2.getFinalValue(sex) / 8;
                    fuckPay += 100;
                    fuckPay = Math.min(fuckPay, cust2.getMoney());
                    arg4 = new Object[]{this.getCustomers().get(a).getName(), fuckPay};
                    message = TextUtil.t("barevent.ninjablowjob", (Person)bartender2, arg4);
                    if (bartender2.getTraits().contains(Trait.WENCH) && Util.getInt(1, 100) > 50) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1f, sex, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.05f, SpecializationAttribute.SEDUCTION, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.8f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.fuck.wench", (Person)bartender2, arg4);
                        image = sex == Sextype.VAGINAL ? ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2) : ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
                        cust2.addToSatisfaction(bartender2.getFinalValue(sex) / 4, this);
                        cust2.payFixed(fuckPay);
                        this.modifyIncome(fuckPay);
                        if (Util.getInt(0, 100) < 20 + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 2) {
                            linefuck = 0;
                            total = 0;
                            for (z = 0; z < this.getCustomers().size(); ++z) {
                                if ((linefuck > (bartender2.getFinalValue(Sextype.VAGINAL) + bartender2.getFinalValue(Sextype.ANAL)) / 5 || this.getCustomers().get(z).getStatus() != CustomerStatus.HORNYSTATUS && this.getCustomers().get(z).getStatus() != CustomerStatus.VERYHORNY) && linefuck >= 3) continue;
                                this.getCustomers().get(z).addToSatisfaction(bartender2.getFinalValue(sex) - bartender2.getFinalValue(sex) * ++linefuck / 50, this);
                                this.getCustomers().get(z).payFixed(fuckPay - fuckPay * linefuck / 50);
                                total += fuckPay - fuckPay * linefuck / 50;
                                bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                            }
                            if (linefuck <= 4) break;
                            this.getAttributeModifications().add(new AttributeModification((float)linefuck * 0.4f, sex, bartender2));
                            this.getAttributeModifications().add(new AttributeModification((float)linefuck * -1.8f, EssentialAttributes.ENERGY, bartender2));
                            LinefuckTotal = new Object[]{linefuck};
                            message = message + "\n" + TextUtil.t("barevent.fuck.line", (Person)bartender2, LinefuckTotal);
                            LinefuckPay = new Object[]{total};
                            message = message + "\n" + TextUtil.t("barevent.fuck.line.pay", (Person)bartender2, LinefuckPay);
                            this.modifyIncome(total);
                            break;
                        }
                        for (z = 0; z < this.getCustomers().size(); ++z) {
                            this.getCustomers().get(z).payFixed(100);
                            this.getCustomers().get(z).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(sex) / 8) / 4, this);
                            if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS || this.getCustomers().get(a).getStatus() == CustomerStatus.HYPED)) {
                                this.getCustomers().get(a).setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(a).getStatus() != CustomerStatus.HAPPY && this.getCustomers().get(a).getStatus() != CustomerStatus.LIVELY) continue;
                            this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                        break;
                    }
                    if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 9 || bartender2.getTraits().contains(Trait.WENCH)) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.5f, sex, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.5f, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.fuck.win", (Person)bartender2, arg4);
                        image = sex == Sextype.VAGINAL ? ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2) : ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
                        cust2.addToSatisfaction(bartender2.getFinalValue(sex) / 5, this);
                        cust2.payFixed(fuckPay);
                        this.modifyIncome(fuckPay);
                        if (cust2.getStatus() == CustomerStatus.HORNYSTATUS) {
                            cust2.setStatus(CustomerStatus.LIVELY);
                        }
                        if (cust2.getStatus() != CustomerStatus.SAD && cust2.getStatus() != CustomerStatus.PISSED) break;
                        cust2.setStatus(CustomerStatus.HAPPY);
                        break;
                    }
                    message = message + "\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
                    this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.MOTIVATION, bartender2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                    cust2.addToSatisfaction(-50, this);
                    cust2.payFixed(Math.min(100, cust2.getMoney()));
                    this.modifyIncome(Math.min(100, cust2.getMoney()));
                    cust2.setStatus(CustomerStatus.PISSED);
                    break;
                }
                case 15: {
                    servedToday2 = bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
                    rand = Util.getInt(100, 101 + bartender2.getFinalValue(Sextype.GROUP) + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION));
                    size = Util.getInt(8, 10);
                    arg2 = new Object[]{1 + this.getCustomers().size() / size, this.getCustomers().get(Util.getInt(0, this.getCustomers().size() - 1)).getName(), this.getCustomers().size() * rand / size};
                    message = TextUtil.t("barevent.group", (Person)bartender2, arg2);
                    if (bartender2.getTraits().contains(Trait.SEXADDICT) && Util.getInt(1, 100) > 50) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)(1L + (long)(this.getCustomers().size() / 10)));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.5f, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.55f, SpecializationAttribute.SEDUCTION, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(2.0f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.queen", (Person)bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                        for (b = 0; b < this.getCustomers().size() / size; ++b) {
                            this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                            this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                        }
                        for (z = this.getCustomers().size() / size; z < this.getCustomers().size(); ++z) {
                            this.getCustomers().get(z).addToSatisfaction(15 + (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 4, this);
                            if (Util.getInt(1, 4) == 1 && (this.getCustomers().get(z).getStatus() == CustomerStatus.HORNYSTATUS || this.getCustomers().get(z).getStatus() == CustomerStatus.HYPED)) {
                                this.getCustomers().get(z).setStatus(CustomerStatus.VERYHORNY);
                            }
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(z).getStatus() != CustomerStatus.HAPPY && this.getCustomers().get(z).getStatus() != CustomerStatus.LIVELY) continue;
                            this.getCustomers().get(z).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    } else if (servedToday2 > 7L) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)(1L + (long)(this.getCustomers().size() / 10)));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1f, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85f, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.lotalready", (Person)bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                        for (b = 0; b < this.getCustomers().size() / size; ++b) {
                            this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                            this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                        }
                    } else if (bartender2.getFinalValue(Sextype.GROUP) > 35 || bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) > 35) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)(1L + (long)(this.getCustomers().size() / 10)));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1f, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85f, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.win.usedtoit", (Person)bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                        for (b = 0; b < this.getCustomers().size() / size; ++b) {
                            this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                            this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                        }
                    } else if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 15 || bartender2.getTraits().contains(Trait.NYMPHO)) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)(1L + (long)(this.getCustomers().size() / 10)));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1f, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85f, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.win", (Person)bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                        for (b = 0; b < this.getCustomers().size() / size; ++b) {
                            this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                            this.getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                        }
                    } else {
                        message = message + "\n" + TextUtil.t("barevent.group.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (b = 0; b < this.getCustomers().size() / 15; ++b) {
                            this.getCustomers().get(b).addToSatisfaction(-10, this);
                            if (Util.getInt(1, 4) != 1 || this.getCustomers().get(b).getStatus() == CustomerStatus.STRONGSTATUS) continue;
                            this.getCustomers().get(b).setStatus(CustomerStatus.PISSED);
                        }
                    }
                    break;
                }
                case 16: {
                    message = TextUtil.t("barevent.bigboob", bartender2);
                    if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > Util.getInt(20, 120) || bartender2.getTraits().contains(Trait.MEATBUNS) || bartender2.getTraits().contains(Trait.LEWD)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        boobPay = 0;
                        boobPayTotal = 0;
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 8, this);
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                                customer.setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                                customer.setStatus(CustomerStatus.HORNYSTATUS);
                            }
                            boobPay = Util.getInt(1, 10);
                            customer.payFixed(boobPay);
                            boobPayTotal += boobPay;
                        }
                        this.modifyIncome(boobPayTotal);
                        arg6 = new Object[]{boobPayTotal};
                        message = message + "\n" + TextUtil.t("barevent.bigboob.win", (Person)bartender2, arg6);
                        break;
                    }
                    message = message + "\n" + TextUtil.t("barevent.bigboob.lose", bartender2);
                    this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.MOTIVATION, bartender2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                    for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(-10, this);
                    }
                    break;
                }
                case 17: {
                    message = TextUtil.t("barevent.smallboob", bartender2);
                    if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > Util.getInt(20, 70) || bartender2.getTraits().contains(Trait.OUTGOING) || bartender2.getTraits().contains(Trait.CROWDLOVER)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08f, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.smallboob.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 8, this);
                            if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                                customer.setStatus(CustomerStatus.HAPPY);
                            }
                            if (Util.getInt(1, 4) != 1 || customer.getStatus() != CustomerStatus.LIVELY && customer.getStatus() != CustomerStatus.DRUNK) continue;
                            customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                    } else {
                        message = message + "\n" + TextUtil.t("barevent.smallboob.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.7f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(-10, this);
                        }
                    }
                    break;
                }
                case 18: {
                    message = TextUtil.t("barevent.breakstuff", bartender2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, bartender2);
                    this.modifyIncome(-200);
                    break;
                }
                case 19: {
                    message = TextUtil.t("barevent.loli", bartender2);
                    if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > Util.getInt(20, 120)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08f, BaseAttributeTypes.CHARISMA, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.loli.win", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(5, this);
                        }
                    } else {
                        message = message + "\n" + TextUtil.t("barevent.loli.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.7f, EssentialAttributes.MOTIVATION, bartender2));
                        for (Customer customer : this.getCustomers()) {
                            if (Util.getInt(0, 12) == 2) {
                                customer.addToSatisfaction(-5, this);
                                continue;
                            }
                            customer.addToSatisfaction(-15, this);
                        }
                    }
                    break;
                }
                case 20: {
                    this.getAttributeModifications().add(new AttributeModification(-0.5f, EssentialAttributes.MOTIVATION, bartender2));
                    if (this.getCustomers().size() > 90) {
                        message = TextUtil.t("barevent.tired.one", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-10.0f, EssentialAttributes.ENERGY, bartender2));
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(-5, this);
                        }
                    } else {
                        if (this.getCustomers().size() <= 50) break;
                        this.getAttributeModifications().add(new AttributeModification(-10.5f, EssentialAttributes.ENERGY, bartender2));
                        message = TextUtil.t("barevent.tired.two", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(-10, this);
                        }
                    }
                    break;
                }
                case 21: {
                    message = TextUtil.t("barevent.shy", bartender2);
                    if (Util.getInt(0, 10) > 5 && !bartender2.getTraits().contains(Trait.OUTGOING)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08f, BaseAttributeTypes.CHARISMA, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.shy.winwin", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(10, this);
                        }
                    } else if (Util.getInt(0, 10) > 5) {
                        message = message + "\n" + TextUtil.t("barevent.shy.win", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.5f, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(5, this);
                        }
                    } else {
                        message = message + "\n" + TextUtil.t("barevent.shy.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        for (Customer customer : this.getCustomers()) {
                            customer.addToSatisfaction(-5, this);
                        }
                    }
                    break;
                }
                case 22: {
                    this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, bartender2));
                    message = TextUtil.t("barevent.smalltalk", bartender2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
                    for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(5, this);
                    }
                    break;
                }
                case 23: {
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
                    this.getAttributeModifications().add(new AttributeModification(0.2f, EssentialAttributes.MOTIVATION, bartender2));
                    servedToday = bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
                    ar = new Object[]{servedToday, Util.getInt(3, 7)};
                    message = TextUtil.t("barevent.sexsmell", bartender2) + "\n";
                    if (servedToday > (long)(20 + bartender2.getStamina() / 3) && (bartender2.getTraits().contains(Trait.SEXADDICT) || bartender2.getTraits().contains(Trait.SLUT) || bartender2.getTraits().contains(Trait.KEEPEMCOMING) || bartender2.getTraits().contains(Trait.NYMPHO) || bartender2.getTraits().contains(Trait.FLIRTY) && Util.getInt(0, 100) < 50)) {
                        message = message + TextUtil.t("barevent.sexsmell.satisfied", (Person)bartender2, ar);
                    } else if (servedToday > (long)(15 + bartender2.getStamina() / 5) && (bartender2.getTraits().contains(Trait.SEXADDICT) || bartender2.getTraits().contains(Trait.SLUT) || bartender2.getTraits().contains(Trait.KEEPEMCOMING) || bartender2.getTraits().contains(Trait.NYMPHO) || bartender2.getTraits().contains(Trait.FLIRTY) && Util.getInt(0, 100) < 50)) {
                        message = message + TextUtil.t("barevent.sexsmell.coulddomore", (Person)bartender2, ar);
                        if (Util.getInt(0, 100) < 50) {
                            message = message + "\n" + TextUtil.t("barevent.sexsmell.coulddomore.okay", (Person)bartender2, ar);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                            this.getAttributeModifications().add(new AttributeModification(0.28f, Sextype.VAGINAL, bartender2));
                        }
                    } else if (servedToday < (long)(15 + bartender2.getStamina() / 5) && (bartender2.getTraits().contains(Trait.SEXADDICT) || bartender2.getTraits().contains(Trait.SLUT) || bartender2.getTraits().contains(Trait.NYMPHO))) {
                        message = message + TextUtil.t("barevent.sexsmell.wantedmore", (Person)bartender2, ar);
                        if (Util.getInt(0, 100) < 50) {
                            message = message + "\n" + TextUtil.t("barevent.sexsmell.wantedmore.okay", (Person)bartender2, ar);
                            image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                            this.getAttributeModifications().add(new AttributeModification(0.18f, Sextype.GROUP, bartender2));
                        }
                    } else {
                        message = servedToday > (long)(7 + bartender2.getStamina()) ? message + TextUtil.t("barevent.sexsmell.waytoomuch", (Person)bartender2, ar) : message + TextUtil.t("barevent.sexsmell.notthatmuch", (Person)bartender2, ar);
                    }
                    for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(13, this);
                    }
                    break;
                }
                case 24: {
                    message = TextUtil.t("barevent.snacks", bartender2);
                    this.getAttributeModifications().add(new AttributeModification(1.1f, SpecializationAttribute.COOKING, bartender2));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, bartender2);
                    for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(bartender2.getFinalValue(SpecializationAttribute.COOKING) / 8, this);
                    }
                    break;
                }
                case 25: {
                    randomChat = Util.getInt(0, 9);
                    chatBonus = 0;
                    this.getAttributeModifications().add(new AttributeModification(1.0f, SpecializationAttribute.BARTENDING, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(0.08f, BaseAttributeTypes.CHARISMA, bartender2));
                    this.getAttributeModifications().add(new AttributeModification(0.8f, EssentialAttributes.MOTIVATION, bartender2));
                    message = TextUtil.t("barevent.loudbunch", bartender2);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
                    if (bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) > 100 && randomChat == 1) {
                        chatBonus = bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.whore", bartender2);
                    } else if (bartender2.getFinalValue(Sextype.GROUP) > 20 && randomChat == 1) {
                        chatBonus = bartender2.getFinalValue(Sextype.GROUP) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.group", bartender2);
                    } else if (bartender2.getFinalValue(Sextype.MONSTER) > 35 && randomChat == 2) {
                        chatBonus = bartender2.getFinalValue(Sextype.MONSTER) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.monster", bartender2);
                    } else if (bartender2.getFinalValue(SpecializationAttribute.STRIP) > 20 && randomChat == 3) {
                        chatBonus = bartender2.getFinalValue(SpecializationAttribute.STRIP) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.dance", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                    } else if (bartender2.getFinalValue(SpecializationAttribute.VETERAN) > 20 && randomChat == 4) {
                        chatBonus = bartender2.getFinalValue(SpecializationAttribute.VETERAN) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.fight", bartender2);
                    } else if (bartender2.getFinalValue(BaseAttributeTypes.STRENGTH) > 25 && randomChat == 5) {
                        chatBonus = bartender2.getFinalValue(BaseAttributeTypes.STRENGTH) / 5;
                        message = message + TextUtil.t("barevent.loudbunch.strength", bartender2);
                    } else if (randomChat == 6) {
                        chatBonus = 10;
                        message = message + TextUtil.t("barevent.loudbunch.drink", bartender2);
                        this.modifyIncome(800);
                    } else {
                        chatBonus = 5;
                        message = message + TextUtil.t("barevent.loudbunch.fun", bartender2);
                    }
                    for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(1 + chatBonus, this);
                    }
                    break;
                }
            }
            if (message == null) continue;
            this.getMessages().add(new MessageData(message, image, bartender2.getBackground()));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String messageText = TextUtil.t("cabaret.basic", this.getCharacters());
        messageText = messageText + "\n";
        this.messageData = new MessageData(messageText, null, this.getBackground());
        for (Charakter character : this.getCharacters()) {
            if (this.dancers.contains(character)) {
                this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character));
                continue;
            }
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, character));
        }
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        for (Charakter dancer : this.dancers) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, -30.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, -0.7f, EssentialAttributes.MOTIVATION));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, 1.0f, SpecializationAttribute.STRIP));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, 0.02f, BaseAttributeTypes.STAMINA));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, 0.02f, BaseAttributeTypes.CHARISMA));
        }
        for (Charakter bartender : this.bartenders) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, -15.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, -0.5f, EssentialAttributes.MOTIVATION));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, 1.0f, SpecializationAttribute.BARTENDING));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, 0.02f, BaseAttributeTypes.INTELLIGENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, 0.02f, BaseAttributeTypes.CHARISMA));
        }
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYBARTENDER) && !this.getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
        }
        return modifications;
    }

    @Override
    public int getAppeal() {
        int appeal = 0;
        for (Charakter dancer : this.dancers) {
            appeal += (dancer.getCharisma() + dancer.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
        }
        for (Charakter bartender : this.bartenders) {
            appeal += (bartender.getCharisma() + bartender.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
        }
        return appeal;
    }

    @Override
    public int getMaxAttendees() {
        int amount = 0;
        for (Charakter bartender : this.getCharacters()) {
            amount += 10 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 10;
        }
        if (amount >= 40 * this.getCharacters().size()) {
            amount = 40 * this.getCharacters().size();
        }
        return amount;
    }

    static class SwitchMapHolder {
        static final /* synthetic */ int[] $SwitchMap$jasbro$game$events$business$CustomerType;
        static final /* synthetic */ int[] $SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction;
        static final /* synthetic */ int[] $SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction;

        static {
            $SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction = new int[Bartend.BarAction.values().length];
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.FLIP.ordinal()] = 1;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.GROPE.ordinal()] = 2;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.LOOK.ordinal()] = 3;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.CROWD.ordinal()] = 4;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SITLAP.ordinal()] = 5;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.CHAT.ordinal()] = 6;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SITGROUP.ordinal()] = 7;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.TEASELOT.ordinal()] = 8;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.BLOWJOB.ordinal()] = 9;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.LAGOMORPHQUICKIE.ordinal()] = 10;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.DUMBFUCK.ordinal()] = 11;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.DUMBBJ.ordinal()] = 12;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.LAGOMORPHORGY.ordinal()] = 13;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.FUCK.ordinal()] = 14;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.GROUP.ordinal()] = 15;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.BIGBOOB.ordinal()] = 16;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SMALLBOOB.ordinal()] = 17;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.BREAKSTUFF.ordinal()] = 18;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.LOLI.ordinal()] = 19;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.TIRED.ordinal()] = 20;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SHY.ordinal()] = 21;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SMALLTALK.ordinal()] = 22;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SEXSMELL.ordinal()] = 23;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.SNACKS.ordinal()] = 24;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Bartend$BarAction[Bartend.BarAction.LOUDBUNCH.ordinal()] = 25;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            $SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction = new int[Strip.StripAction.values().length];
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.EXTRAS.ordinal()] = 1;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.SHY.ordinal()] = 2;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.UNHINIBITED.ordinal()] = 3;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.FRAGILE.ordinal()] = 4;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.FIT.ordinal()] = 5;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.STIFF.ordinal()] = 6;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.BIGTITS.ordinal()] = 7;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.CLUMSY.ordinal()] = 8;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.RUBCLIT.ordinal()] = 9;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.LICKPOLE.ordinal()] = 10;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.OILY.ordinal()] = 11;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.FELINEORGY.ordinal()] = 12;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.CUMDRINK.ordinal()] = 13;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.SEXSMELL.ordinal()] = 14;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.FONDLE.ordinal()] = 15;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.CREAM.ordinal()] = 16;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$character$activities$sub$business$Strip$StripAction[Strip.StripAction.SUBMISSIVE.ordinal()] = 17;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            $SwitchMap$jasbro$game$events$business$CustomerType = new int[CustomerType.values().length];
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.PEASANT.ordinal()] = 1;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.SOLDIER.ordinal()] = 2;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.MERCHANT.ordinal()] = 3;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.BUSINESSMAN.ordinal()] = 4;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.MINORNOBLE.ordinal()] = 5;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.LORD.ordinal()] = 6;
            }
            catch (NoSuchFieldError ex) {
                // empty catch block
            }
            try {
                SwitchMapHolder.$SwitchMap$jasbro$game$events$business$CustomerType[CustomerType.CELEBRITY.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }
    }
}

