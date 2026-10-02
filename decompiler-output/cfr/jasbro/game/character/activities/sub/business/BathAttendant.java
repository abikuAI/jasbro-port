/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
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
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BathAttendant
extends RunningActivity
implements BusinessSecondaryActivity {
    private Map<Charakter, Action> characterAction = new HashMap<Charakter, Action>();
    private MessageData messageData;

    @Override
    public int getAppeal() {
        int appeal = Util.getInt(0, 8);
        for (Charakter character : this.getCharacters()) {
            appeal += character.getCharisma() / 6;
        }
        return appeal;
    }

    @Override
    public int getMaxAttendees() {
        return 15 + this.getCharacters().size() * 5;
    }

    @Override
    public void init() {
        ArrayList<Charakter> characters = new ArrayList<Charakter>(this.getCharacters());
        for (Charakter character : characters) {
            ArrayList<Action> actions = new ArrayList<Action>();
            actions.add(Action.NORMAL);
            actions.add(Action.LAZY);
            actions.add(Action.RELAX);
            actions.add(Action.SOAP);
            if (character.getFinalValue(SpecializationAttribute.STRIP) > 60 || character.getTraits().contains(Trait.EXHIBITIONIST) || character.getTraits().contains(Trait.NICEBODY) || character.getTraits().contains(Trait.UNINHIBITED)) {
                actions.add(Action.NAKED);
                actions.add(Action.SWIMSUIT);
            }
            if (character.getTraits().contains(Trait.EXHIBITIONIST) || character.getTraits().contains(Trait.SEXADDICT) || character.getTraits().contains(Trait.AFLEURDEPEAU) || character.getTraits().contains(Trait.HORNY) || character.getTraits().contains(Trait.TEASER) || character.getTraits().contains(Trait.UNINHIBITED)) {
                actions.add(Action.MASTURBATION);
            }
            if (character.getFinalValue(SpecializationAttribute.CLEANING) > 60) {
                actions.add(Action.CLEAN);
            }
            if (character.getFinalValue(SpecializationAttribute.BARTENDING) > 60) {
                actions.add(Action.COCKTAILS);
            }
            if (character.getTraits().contains(Trait.TANTRIC)) {
                actions.add(Action.MASSAGE);
            }
            if (character.getTraits().contains(Trait.OILY)) {
                actions.add(Action.OIL);
            }
            if (character.getTraits().contains(Trait.CALMINGINCENCES)) {
                actions.add(Action.INCENCE);
            }
            this.characterAction.put(character, (Action)((Object)actions.get(Util.getInt(0, actions.size()))));
        }
    }

    @Override
    public void perform() {
        int skill = 0;
        int amountEarned = 0;
        int personalPay = 0;
        int amountVaginal = 0;
        int amountOral = 0;
        int amountGroup = 0;
        int amountAnal = 0;
        int amountForeplay = 0;
        for (Charakter character : this.getCharacters()) {
            amountVaginal = 0;
            amountOral = 0;
            amountGroup = 0;
            amountAnal = 0;
            amountForeplay = 0;
            amountEarned = 0;
            personalPay = 0;
            switch (this.characterAction.get(character)) {
                case NORMAL: {
                    skill += character.getCharisma() + character.getObedience();
                    skill /= 15;
                    break;
                }
                case LAZY: {
                    skill += character.getCharisma() + character.getObedience();
                    skill /= 20;
                    break;
                }
                case RELAX: {
                    skill += character.getCharisma() + character.getObedience() + Util.getInt(0, 5);
                    skill /= 10;
                    break;
                }
                case SWIMSUIT: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.STRIP);
                    skill /= 14;
                    break;
                }
                case NAKED: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.SEDUCTION);
                    skill /= 12;
                    break;
                }
                case OIL: {
                    skill += character.getCharisma() + character.getObedience() / 10 + character.getStamina();
                    skill /= 13;
                    break;
                }
                case INCENCE: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE);
                    skill /= 20;
                    break;
                }
                case COCKTAILS: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.BARTENDING);
                    skill /= 16;
                    break;
                }
                case CLEAN: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.CLEANING);
                    skill /= 20;
                    break;
                }
                case SOAP: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
                    skill /= 13;
                    break;
                }
                case MASSAGE: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
                    skill /= 12;
                    break;
                }
                case MASTURBATION: {
                    skill += character.getCharisma() + character.getObedience() + character.getFinalValue(Sextype.FOREPLAY);
                    skill /= 11;
                }
            }
            for (Customer customer : this.getCustomers()) {
                int tips = 0;
                for (Room room : this.getHouse().getRooms()) {
                    tips += room.getRoomInfo().getCost();
                }
                tips /= 2000;
                tips = customer.payFixed(Math.min(tips, customer.getMoney()));
                amountEarned += tips;
                if (Util.getInt(0, 5) + skill + customer.getSatisfactionAmount() > 50) {
                    customer.addToSatisfaction(skill, this);
                    customer.changePayModifier(0.3f);
                } else {
                    customer.addToSatisfaction(skill / 2, this);
                }
                if (Util.getInt(1, 2) == 1 && customer.getStatus() == CustomerStatus.TIRED) {
                    customer.setStatus(CustomerStatus.LIVELY);
                }
                if (Util.getInt(1, 2) == 1 && (customer.getStatus() == CustomerStatus.DRUNK || customer.getStatus() == CustomerStatus.VERYDRUNK)) {
                    customer.setStatus(CustomerStatus.TIRED);
                }
                if ((this.characterAction.get(character) == Action.MASSAGE || this.characterAction.get(character) == Action.SOAP || this.characterAction.get(character) == Action.COCKTAILS || this.characterAction.get(character) == Action.OIL || this.characterAction.get(character) == Action.SWIMSUIT || this.characterAction.get(character) == Action.NAKED) && Util.getInt(0, 5) == 2) {
                    personalPay += customer.pay(skill * Util.getInt(0, 10) / 30);
                }
                int obed = 10;
                obed = character.getType() == CharacterType.SLAVE ? (obed += character.getObedience()) : (obed += character.getCommand());
                if (obed > 50) {
                    obed = 50;
                }
                if (obed / 2 <= Util.getInt(0, 100)) continue;
                switch (customer.getPreferredSextype()) {
                    case VAGINAL: {
                        if (character.getTraits().contains(Trait.ONSENPRINCESS)) {
                            ++amountVaginal;
                            customer.addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 7, this);
                        }
                    }
                    case ANAL: {
                        if (character.getTraits().contains(Trait.ONSENPRINCESS)) {
                            ++amountAnal;
                            customer.addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);
                        }
                    }
                    case ORAL: {
                        if (character.getTraits().contains(Trait.ONSENPRINCESS)) {
                            ++amountOral;
                            customer.addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 7, this);
                        }
                    }
                    case TITFUCK: {
                        if (character.getTraits().contains(Trait.ONSENPRINCESS)) {
                            ++amountForeplay;
                            customer.addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                        }
                    }
                    case FOREPLAY: {
                        if (!character.getTraits().contains(Trait.ONSENPRINCESS)) break;
                        ++amountForeplay;
                        customer.addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                    }
                }
                int rand = Util.getInt(0, 4);
                if (character.getTraits().contains(Trait.ONSENPRINCESS) && Util.getInt(0, 10) > 5) {
                    customer.addToSatisfaction(character.getFinalValue(Sextype.GROUP) / 7, this);
                    if (++amountGroup <= this.getCustomers().size()) continue;
                    amountGroup = this.getCustomers().size() - 1;
                    continue;
                }
                if (character.getTraits().contains(Trait.ONSENPRINCESS) && rand == 2) {
                    ++amountForeplay;
                    continue;
                }
                if (character.getTraits().contains(Trait.ONSENPRINCESS) && rand == 3) {
                    ++amountVaginal;
                    continue;
                }
                if (!character.getTraits().contains(Trait.ONSENPRINCESS) || rand != 1) continue;
                ++amountAnal;
            }
            this.modifyIncome(amountEarned);
            this.modifyIncome(personalPay);
            int rand = 0;
            int shownAct = Util.getInt(0, 100);
            if (amountVaginal > 2 && shownAct < 20) {
                String message = null;
                rand = Util.getInt(0, 90);
                character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(amountVaginal));
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                message = rand < 30 ? TextUtil.t("attendBath.extras.vaginal3", character) : (rand < 60 ? TextUtil.t("attendBath.extras.vaginal2", character) : TextUtil.t("attendBath.extras.vaginal1", character));
                this.getAttributeModifications().add(new AttributeModification((float)amountVaginal * 0.05f, Sextype.VAGINAL, character));
                this.getMessages().add(new MessageData(message, image, character.getBackground()));
            } else if (amountAnal > 2 && shownAct < 40) {
                String message = null;
                rand = Util.getInt(0, 90);
                character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(amountVaginal));
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                message = rand < 30 ? TextUtil.t("attendBath.extras.anal3", character) : (rand < 60 ? TextUtil.t("attendBath.extras.anal2", character) : TextUtil.t("attendBath.extras.anal1", character));
                this.getAttributeModifications().add(new AttributeModification((float)amountVaginal * 0.05f, Sextype.ANAL, character));
                this.getMessages().add(new MessageData(message, image, character.getBackground()));
            } else if (amountOral > 2 && shownAct < 60) {
                String message = null;
                rand = Util.getInt(0, 90);
                character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(amountVaginal));
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                message = rand < 30 ? TextUtil.t("attendBath.result.oral", character) : (rand < 60 ? TextUtil.t("attendBath.extras.oral2", character) : TextUtil.t("attendBath.extras.oral1", character));
                this.getAttributeModifications().add(new AttributeModification((float)amountVaginal * 0.05f, Sextype.ORAL, character));
                this.getMessages().add(new MessageData(message, image, character.getBackground()));
            } else if (amountForeplay > 2 && shownAct < 80) {
                String message = null;
                rand = Util.getInt(0, 100);
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character);
                message = rand < 25 ? TextUtil.t("attendBath.extras.foreplay3", character) : (rand < 50 ? TextUtil.t("attendBath.result.join", character) : (rand < 75 ? TextUtil.t("attendBath.extras.foreplay2", character) : TextUtil.t("attendBath.extras.foreplay1", character)));
                this.getAttributeModifications().add(new AttributeModification((float)amountVaginal * 0.05f, Sextype.FOREPLAY, character));
                this.getMessages().add(new MessageData(message, image, character.getBackground()));
            } else if (amountGroup > 2 && shownAct > 80) {
                String message = null;
                rand = Util.getInt(0, 100);
                character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(amountGroup));
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                message = rand < 25 ? TextUtil.t("attendBath.extras.group1", character) : (rand < 50 ? TextUtil.t("attendBath.extras.group11", character) : (rand < 75 ? TextUtil.t("attendBath.extras.group2", character) : TextUtil.t("attendBath.extras.group3", character)));
                this.getAttributeModifications().add(new AttributeModification((float)amountVaginal * 0.05f, Sextype.GROUP, character));
                this.getMessages().add(new MessageData(message, image, character.getBackground()));
            }
            Object[] argBis = new Object[]{personalPay};
            this.messageData.addToMessage("\n");
            switch (this.characterAction.get(character)) {
                case NORMAL: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.normal", character));
                    break;
                }
                case LAZY: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.lazy", character));
                    break;
                }
                case RELAX: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.relax", character));
                    break;
                }
                case SWIMSUIT: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.swimsuit", character));
                    if (personalPay == 0) break;
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.swimsuit.pay", (Person)character, argBis));
                    break;
                }
                case NAKED: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.naked", character));
                    if (personalPay == 0) break;
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.naked.pay", (Person)character, argBis));
                    break;
                }
                case OIL: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.oil", character));
                    if (personalPay == 0) break;
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.oil.pay", (Person)character, argBis));
                    break;
                }
                case INCENCE: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.incence", character));
                    break;
                }
                case COCKTAILS: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.cocktails", character));
                    if (personalPay == 0) break;
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.cocktails.pay", (Person)character, argBis));
                    break;
                }
                case CLEAN: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.clean", character));
                    break;
                }
                case SOAP: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.soap", character));
                    if (personalPay == 0) break;
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.soap.pay", (Person)character, argBis));
                    break;
                }
                case MASSAGE: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.massage", character));
                    if (personalPay == 0) break;
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.massage.pay", (Person)character, argBis));
                    break;
                }
                case MASTURBATION: {
                    this.messageData.addToMessage(TextUtil.t("attendBath.result.masturbation", character));
                }
            }
        }
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        for (Charakter character : this.getCharacters()) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.7f, SpecializationAttribute.MEDICALKNOWLEDGE));
            if (character.getTraits().contains(Trait.BENEVOLENT)) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, EssentialAttributes.MOTIVATION));
            }
            if (character.getTraits().contains(Trait.MAGICALHEALING)) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.35f, SpecializationAttribute.MAGIC));
            }
            switch (this.characterAction.get(character)) {
                case NORMAL: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -20.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case LAZY: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -6.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case RELAX: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -6.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 5.0f, EssentialAttributes.HEALTH));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case SWIMSUIT: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -15.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, SpecializationAttribute.STRIP));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case NAKED: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -15.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, SpecializationAttribute.STRIP));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case OIL: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -15.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, SpecializationAttribute.STRIP));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case INCENCE: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -10.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, SpecializationAttribute.PLANTKNOWLEDGE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.INTELLIGENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case COCKTAILS: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -15.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, SpecializationAttribute.BARTENDING));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case CLEAN: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -15.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, SpecializationAttribute.CLEANING));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case SOAP: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -20.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 1.1f, SpecializationAttribute.MEDICALKNOWLEDGE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.08f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
                    if (!character.getTraits().contains(Trait.MAGICALHEALING)) break;
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.55f, SpecializationAttribute.MAGIC));
                    break;
                }
                case MASSAGE: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -25.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 1.1f, SpecializationAttribute.MEDICALKNOWLEDGE));
                    if (character.getTraits().contains(Trait.MAGICALHEALING)) {
                        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.55f, SpecializationAttribute.MAGIC));
                    }
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
                    break;
                }
                case MASTURBATION: {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -15.0f, EssentialAttributes.ENERGY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, Sextype.FOREPLAY));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, EssentialAttributes.MOTIVATION));
                }
            }
            if (character != Jasbro.getInstance().getData().getProtagonist() || character.getTraits().contains(Trait.LEGACYMASSEUR)) continue;
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
        }
        return modifications;
    }

    @Override
    public MessageData getBaseMessage() {
        String messageText;
        int numCustomers = this.getCustomers().size();
        if (numCustomers > 0) {
            int pay = 0;
            for (Customer customer : this.getCustomers()) {
                int tips = 0;
                for (Room room : this.getHouse().getRooms()) {
                    tips += room.getRoomInfo().getCost();
                }
                pay += Math.min(tips /= 2000, customer.getMoney());
            }
            messageText = TextUtil.t("attendBath.basic", TextUtil.listCharacters(this.getCharacters()), numCustomers, pay);
        } else {
            messageText = TextUtil.t("attendBath.nobody", TextUtil.listCharacters(this.getCharacters()), numCustomers);
        }
        this.messageData = new MessageData(messageText, null, this.getBackground());
        for (Charakter character : this.getCharacters()) {
            ArrayList<ImageTag> tags = new ArrayList<ImageTag>();
            tags.add(ImageTag.BATHE);
            switch (this.characterAction.get(character)) {
                case NORMAL: {
                    tags.add(ImageTag.BATHE);
                    break;
                }
                case LAZY: {
                    tags.add(ImageTag.SLEEP);
                    break;
                }
                case RELAX: {
                    tags.add(ImageTag.BATHE);
                    break;
                }
                case SWIMSUIT: {
                    tags.add(ImageTag.SWIMSUIT);
                    break;
                }
                case NAKED: {
                    tags.add(ImageTag.NAKED);
                    break;
                }
                case OIL: {
                    tags.add(ImageTag.DANCE);
                    break;
                }
                case INCENCE: {
                    tags.add(ImageTag.BATHE);
                    break;
                }
                case COCKTAILS: {
                    tags.add(ImageTag.BARTEND);
                    break;
                }
                case CLEAN: {
                    tags.add(ImageTag.CLEAN);
                    break;
                }
                case SOAP: {
                    tags.add(ImageTag.BATHE);
                    break;
                }
                case MASSAGE: {
                    tags.add(ImageTag.NURSE);
                    break;
                }
                case MASTURBATION: {
                    tags.add(ImageTag.MASTURBATION);
                    break;
                }
            }
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
        }
        return this.messageData;
    }

    private static enum Action {
        NORMAL,
        SWIMSUIT,
        NAKED,
        OIL,
        LAZY,
        RELAX,
        CLEAN,
        COCKTAILS,
        MASTURBATION,
        MASSAGE,
        SOAP,
        INCENCE;

    }
}

