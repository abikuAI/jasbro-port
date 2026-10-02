/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publicize
extends RunningActivity {
    private MessageData message;
    private Charakter character;
    private int effectiveness = 8;
    private Map<Charakter, AdvAction> characterAction = new HashMap<Charakter, AdvAction>();

    @Override
    public void init() {
        this.character = this.getCharacter();
    }

    @Override
    public MessageData getBaseMessage() {
        if (this.message == null) {
            List tags = this.character.getBaseTags();
            tags.add(0, ImageTag.ADVERTISE);
            tags.add(1, ImageTag.CLEANED);
            Object[] arguments = new Object[]{this.getCharacterLocation().getName()};
            this.message = new MessageData(TextUtil.t("advertise.basic", (Person)this.character, arguments), ImageUtil.getInstance().getImageDataByTags(tags, this.character.getImages()), this.getCharacterLocation().getImage());
            if (!this.character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
                this.message.addToMessage(TextUtil.t("advertise.hintTraining", this.character));
            }
        }
        return this.message;
    }

    @Override
    public void perform() {
        ArrayList<AdvAction> actions = new ArrayList<AdvAction>();
        actions.add(AdvAction.NORMAL);
        actions.add(AdvAction.NORMAL);
        if (this.character.getTraits().contains(Trait.BIGBOOBS) && (this.character.getObedience() > 10 || this.character.getType() == CharacterType.TRAINER)) {
            actions.add(AdvAction.BIGBREASTS);
        }
        if (this.character.getTraits().contains(Trait.LOLI)) {
            actions.add(AdvAction.LOLI);
        }
        if (this.character.getTraits().contains(Trait.CLUMSY)) {
            actions.add(AdvAction.CLUMSY);
        }
        if (this.character.getTraits().contains(Trait.SHY) && this.character.getCharisma() > 20 && this.character.getFinalValue(SpecializationAttribute.ADVERTISING) > 30) {
            actions.add(AdvAction.SHY);
        }
        if (this.character.getTraits().contains(Trait.OUTGOING)) {
            actions.add(AdvAction.BRIGHT);
        }
        if (this.character.getTraits().contains(Trait.UNINHIBITED)) {
            actions.add(AdvAction.BRIGHT);
        }
        if (this.character.getTraits().contains(Trait.NICEBODY)) {
            actions.add(AdvAction.NICEBODY);
        }
        if (this.character.getTraits().contains(Trait.EXHIBITIONIST)) {
            actions.add(AdvAction.NAKED);
        }
        if (this.character.getTraits().contains(Trait.EXHIBITIONIST) && this.character.getTraits().contains(Trait.SEXFREAK)) {
            actions.add(AdvAction.BODYPAINT);
        }
        if (this.character.getTraits().contains(Trait.SHOWINGTHEGOODS) && this.character.getTraits().contains(Trait.EXHIBITIONIST)) {
            actions.add(AdvAction.DANCER);
        }
        if (this.character.getTraits().contains(Trait.SHOWINGTHEGOODS)) {
            actions.add(AdvAction.DANCERNAKED);
        }
        if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 15 && (this.character.getObedience() > 10 || this.character.getTraits().contains(Trait.SENSITIVE))) {
            actions.add(AdvAction.WHOREFONDLE);
        }
        if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 30 && (this.character.getObedience() > 12 || this.character.getTraits().contains(Trait.SENSUALTONGUE))) {
            actions.add(AdvAction.WHOREBLOWJOB);
        }
        if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 35 && (this.character.getObedience() > 15 || this.character.getTraits().contains(Trait.BIGBOOBS))) {
            actions.add(AdvAction.WHORETITFUCK);
        }
        if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 45 && (this.character.getObedience() > 20 || this.character.getTraits().contains(Trait.NATURAL))) {
            actions.add(AdvAction.WHOREFUCK);
        }
        if (this.character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && this.character.getFinalValue(SpecializationAttribute.SEDUCTION) > 60 && (this.character.getObedience() > 25 || this.character.getTraits().contains(Trait.SEXADDICT))) {
            actions.add(AdvAction.WHOREORGY);
        }
        if (this.character.getTraits().contains(Trait.HEYGUYSBOOSE)) {
            actions.add(AdvAction.BARTENDER);
        }
        if (this.character.getTraits().contains(Trait.AVIANFLIGHT)) {
            actions.add(AdvAction.AVIANFLY);
        }
        if (this.character.getTraits().contains(Trait.AVIANDRAG)) {
            actions.add(AdvAction.AVIANDRAG);
        }
        if (this.character.getTraits().contains(Trait.APHRODISIACS)) {
            actions.add(AdvAction.ALCHEMISTDRUGS);
        }
        this.characterAction.put(this.character, (AdvAction)((Object)actions.get(Util.getInt(0, actions.size()))));
        ArrayList<House> listHouses = new ArrayList<House>();
        ArrayList bonusCustomers = new ArrayList();
        block20: for (House house : Jasbro.getInstance().getData().getHouses()) {
            for (Room room : house.getRooms()) {
                if (room.getAmountPeople() <= 0 || !room.getSelectedActivity().isCustomerDependent()) continue;
                listHouses.add(house);
                continue block20;
            }
        }
        int amountHouses = listHouses.size();
        long skill = this.effectiveness;
        int bonus = Util.getInt(80, 120);
        skill += (long)(this.character.getCharisma() / 4);
        skill += (long)(this.character.getFinalValue(SpecializationAttribute.ADVERTISING) / 4);
        switch (this.characterAction.get(this.character)) {
            case NORMAL: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.normal", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += Util.getInt(-20, 20);
                break;
            }
            case BIGBREASTS: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.bigbreasts", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += this.character.getCharisma() * 4 / 5;
                break;
            }
            case LOLI: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.loli", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += this.character.getCharisma() * 3 / 5;
                break;
            }
            case CLUMSY: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.clumsy", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += this.character.getCharisma() * Util.getInt(50, 100) / 100;
                break;
            }
            case SHY: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.shy", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += 10 + this.character.getCharisma() * 2 / 5;
                break;
            }
            case BRIGHT: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.bright", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += 15 + this.character.getCharisma() * 2 / 5;
                break;
            }
            case NICEBODY: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.nicebody", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += 22 + this.character.getCharisma() * 4 / 5;
                break;
            }
            case NAKED: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.naked", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.character));
                bonus += 40 + this.character.getCharisma() / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, this.character));
                break;
            }
            case BODYPAINT: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.bodypaint", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.character));
                bonus += 60 + this.character.getCharisma();
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, this.character));
                break;
            }
            case DANCER: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.dancer", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, this.character));
                bonus += 10 + this.character.getFinalValue(SpecializationAttribute.STRIP) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.STRIP, this.character));
                break;
            }
            case DANCERNAKED: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.dancernaked", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, this.character));
                bonus += this.character.getCharisma() / 2;
                bonus += 10 + this.character.getFinalValue(SpecializationAttribute.STRIP) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.STRIP, this.character));
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.OBEDIENCE, this.character));
                break;
            }
            case WHOREFONDLE: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.whorefondle", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, this.character));
                bonus += 20 + this.character.getCharisma() / 4;
                bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
                bonus += this.character.getFinalValue(Sextype.FOREPLAY) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.SEDUCTION, this.character));
                this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.FOREPLAY, this.character));
                break;
            }
            case WHORETITFUCK: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.whorefondle", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, this.character));
                bonus += 30 + this.character.getCharisma() / 4;
                bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
                bonus += this.character.getFinalValue(Sextype.TITFUCK) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.SEDUCTION, this.character));
                this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.TITFUCK, this.character));
                break;
            }
            case WHOREBLOWJOB: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.whoreblowjob", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, this.character));
                bonus += 40 + this.character.getCharisma() / 4;
                bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
                bonus += this.character.getFinalValue(Sextype.ORAL) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.SEDUCTION, this.character));
                this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.ORAL, this.character));
                break;
            }
            case WHOREFUCK: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.whorefuck", this.character));
                if (Util.getInt(0, 100) < 50) {
                    bonus += this.character.getFinalValue(Sextype.VAGINAL) / 2;
                    this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.character));
                    this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.VAGINAL, this.character));
                } else {
                    bonus += this.character.getFinalValue(Sextype.ANAL) / 2;
                    this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, this.character));
                    this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.ANAL, this.character));
                }
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.SEDUCTION, this.character));
                bonus += 50 + this.character.getCharisma() / 4;
                bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
                break;
            }
            case WHOREORGY: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.whoreorgy", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.character));
                bonus += 70 + this.character.getCharisma() / 4;
                bonus += this.character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2;
                bonus += this.character.getFinalValue(Sextype.GROUP) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, Sextype.GROUP, this.character));
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.SEDUCTION, this.character));
                break;
            }
            case BARTENDER: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.bartending", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += this.character.getCharisma() / 3;
                bonus += 10 + this.character.getFinalValue(SpecializationAttribute.BARTENDING) / 2;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.BARTENDING, this.character));
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, this.character));
                break;
            }
            case ALCHEMISTDRUGS: {
                this.message.addToMessage("\n\n" + TextUtil.t("advertise.alchemist", this.character));
                this.message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.character));
                bonus += this.character.getCharisma();
                bonus += 10 + this.character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) * Util.getInt(50, 220) / 100;
                this.getAttributeModifications().add(new AttributeModification(0.1f, SpecializationAttribute.PLANTKNOWLEDGE, this.character));
            }
        }
        skill *= (long)(bonus / 100);
        long fame = this.character.getFame().getFame();
        if (fame > 100L) {
            long fameBonus = 0L;
            int divider = 500;
            do {
                ++fameBonus;
            } while ((fame /= (long)(divider *= 4)) > 0L);
            skill += fameBonus;
        }
        List<House> houses = Jasbro.getInstance().getData().getHouses();
        long increaseFameBy = skill * 50L / (long)houses.size();
        for (House house : houses) {
            house.getFame().modifyFame(increaseFameBy);
        }
        Object[] arg = new Object[]{(int)increaseFameBy};
        this.message.addToMessage("\n\n" + TextUtil.t("advertise.increaseFame", (Person)this.character, arg));
        this.character.getFame().modifyFame(skill);
    }

    public int getStartingEffectiveness() {
        return this.effectiveness;
    }

    public void setStartingEffectiveness(int effectiveness) {
        this.effectiveness = effectiveness;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.51f, SpecializationAttribute.ADVERTISING));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, SpecializationAttribute.ADVERTISING));
        }
        if (this.character.getTraits().contains(Trait.SPIRITED)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
        return modifications;
    }

    public static enum AdvAction {
        NORMAL,
        BIGBREASTS,
        LOLI,
        CLUMSY,
        SHY,
        BRIGHT,
        NICEBODY,
        NAKED,
        BODYPAINT,
        DANCER,
        DANCERNAKED,
        BARTENDER,
        WHOREBLOWJOB,
        WHOREFONDLE,
        WHOREFUCK,
        WHORETITFUCK,
        WHOREORGY,
        AVIANFLY,
        AVIANDRAG,
        ALCHEMISTDRUGS;

    }
}

