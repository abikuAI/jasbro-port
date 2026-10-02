/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkForGuild
extends RunningActivity {
    private int actionType = 0;
    private Map<Charakter, teachAction> characterAction = new HashMap<Charakter, teachAction>();

    @Override
    public void init() {
        boolean plop = true;
        ArrayList<teachAction> action = new ArrayList<teachAction>();
        Charakter character = this.getCharacters().get(0);
        if (character.getTraits().contains(Trait.SLUT)) {
            action.add(teachAction.SLUT);
        }
        if (character.getTraits().contains(Trait.NYMPHO)) {
            action.add(teachAction.SLUT);
        }
        if (character.getTraits().contains(Trait.SEXADDICT)) {
            action.add(teachAction.SLUT);
        }
        if (character.getTraits().contains(Trait.LEGACYWHORE)) {
            action.add(teachAction.SLUT);
        }
        if (character.getTraits().contains(Trait.SLUT)) {
            action.add(teachAction.SEX);
        }
        if (Jasbro.getInstance().getData().getDay() < 30) {
            action.add(teachAction.SEX);
        }
        if (character.getTraits().contains(Trait.SEXADDICT)) {
            action.add(teachAction.SEX);
        }
        if (character.getTraits().contains(Trait.NYMPHO)) {
            action.add(teachAction.SEX);
        }
        if (character.getTraits().contains(Trait.LEGACYWHORE)) {
            action.add(teachAction.SEX);
        }
        if (character.getTraits().contains(Trait.LEGACYWHORE)) {
            action.add(teachAction.TEACHSEX);
        }
        if (character.getTraits().contains(Trait.NATURAL)) {
            action.add(teachAction.TEACHSEX);
        }
        if (character.getFinalValue(SpecializationAttribute.SEDUCTION) > 25) {
            action.add(teachAction.TEACHWHORE);
        }
        if (character.getFinalValue(SpecializationAttribute.COOKING) > 25 && character.getFinalValue(SpecializationAttribute.CLEANING) > 25) {
            action.add(teachAction.TEACHMAID);
        }
        if (character.getTraits().contains(Trait.LEGACYMAID)) {
            action.add(teachAction.TEACHMAID);
        }
        if (character.getFinalValue(Sextype.VAGINAL) > 20 && character.getFinalValue(Sextype.ANAL) > 20 && character.getFinalValue(Sextype.ORAL) > 20 && character.getFinalValue(Sextype.TITFUCK) > 20 && character.getFinalValue(Sextype.FOREPLAY) > 20) {
            action.add(teachAction.TEACHSEX);
        }
        if (character.getFinalValue(SpecializationAttribute.BARTENDING) > 25) {
            action.add(teachAction.TEACHBARTEND);
        }
        if (character.getFinalValue(SpecializationAttribute.STRIP) > 25) {
            action.add(teachAction.TEACHSTRIP);
        }
        if (character.getTraits().contains(Trait.LEGACYSTRIPPER)) {
            action.add(teachAction.TEACHSTRIP);
        }
        if (character.getTraits().contains(Trait.LEGACYBARTENDER)) {
            action.add(teachAction.TEACHBARTEND);
        }
        if (character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 25) {
            action.add(teachAction.TEACHNURSE);
        }
        if (character.getFinalValue(SpecializationAttribute.MAGIC) > 25 && character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) > 25) {
            action.add(teachAction.TEACHALCHEMY);
        }
        if (character.getFinalValue(SpecializationAttribute.VETERAN) > 25) {
            action.add(teachAction.TEACHFIGHT);
        }
        if (character.getFinalValue(SpecializationAttribute.ADVERTISING) > 25) {
            action.add(teachAction.TEACHADVERTISING);
        }
        if (character.getFinalValue(BaseAttributeTypes.COMMAND) > 15) {
            action.add(teachAction.TEACHBASIC);
        }
        if (character.getFinalValue(Sextype.GROUP) > 25) {
            action.add(teachAction.TEACHGROUP);
        }
        if (character.getFinalValue(Sextype.MONSTER) > 25) {
            action.add(teachAction.TEACHMONSTER);
        }
        if (action.size() < 4) {
            action.add(teachAction.PAPERWORK);
            action.add(teachAction.CLEANING);
            action.add(teachAction.BRINGTEA);
        }
        this.characterAction.put(character, (teachAction)((Object)action.get(Util.getInt(0, action.size()))));
    }

    @Override
    public MessageData getBaseMessage() {
        int pay = 0;
        Charakter character = this.getCharacters().get(0);
        Charakter student = Jasbro.getInstance().generateBasicSlave();
        String message = "";
        message = TextUtil.t("workguild.basic", character);
        message = message + "\n";
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
        ImageData image2 = null;
        switch (this.characterAction.get(character)) {
            case SLUT: {
                int luck = Util.getInt(0, 100) + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 4;
                message = message + TextUtil.t("workguild.slut", character);
                message = message + "\n";
                if (luck > 75 && character.getFinalValue(SpecializationAttribute.SEDUCTION) > 10) {
                    int amount = Util.getInt(2, 5);
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
                    this.getAttributeModifications().add(new AttributeModification((float)(amount += Util.getInt(0, (character.getFinalValue(SpecializationAttribute.SEDUCTION) + character.getFinalValue(Sextype.ANAL) + character.getFinalValue(Sextype.VAGINAL) + character.getFinalValue(Sextype.ORAL)) / 13)) * 0.3f, SpecializationAttribute.SEDUCTION, character));
                    this.getAttributeModifications().add(new AttributeModification((float)amount * 0.2f, Sextype.GROUP, character));
                    pay = amount * Util.getInt(200, 300 + character.getFinalValue(SpecializationAttribute.SEDUCTION));
                    Object[] ar = new Object[]{amount};
                    message = message + TextUtil.t("workguild.slut.lots", (Person)character, ar);
                    message = message + "\n";
                    character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(amount));
                } else if (luck > 50 && character.getFinalValue(SpecializationAttribute.SEDUCTION) > 25) {
                    int amount = Util.getInt(6, 16);
                    Object[] ar = new Object[]{amount};
                    message = message + TextUtil.t("workguild.slut.group", (Person)character, ar);
                    message = message + "\n";
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                    this.getAttributeModifications().add(new AttributeModification(0.3f, SpecializationAttribute.SEDUCTION, character));
                    this.getAttributeModifications().add(new AttributeModification(0.2f, Sextype.GROUP, character));
                    pay = Util.getInt(100 + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 2, 200 + character.getFinalValue(SpecializationAttribute.SEDUCTION) * 2);
                    pay *= (120 + character.getFinalValue(Sextype.GROUP)) / 100;
                    pay *= amount;
                    character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(amount));
                } else if (luck > 20) {
                    message = message + TextUtil.t("workguild.slut.oneguy", character);
                    message = message + "\n";
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                    this.getAttributeModifications().add(new AttributeModification(0.2f, SpecializationAttribute.SEDUCTION, character));
                    pay = Util.getInt(25 + character.getFinalValue(SpecializationAttribute.SEDUCTION), 50 + character.getFinalValue(SpecializationAttribute.SEDUCTION) * 4);
                    character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                } else {
                    message = message + TextUtil.t("workguild.slut.none", character);
                    message = message + "\n";
                }
                if (Util.getInt(0, 100) >= 30 || pay == 0) break;
                character.getFame().modifyFame(pay);
                pay = 0;
                message = message + TextUtil.t("workguild.slut.gainrep", character);
                break;
            }
            case PAPERWORK: {
                message = message + TextUtil.t("workguild.paperwork", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);
                pay = Util.getInt(20, 50) + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) * 10;
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, character));
                break;
            }
            case CLEANING: {
                message = message + TextUtil.t("workguild.clean", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, character);
                pay = Util.getInt(100, 200);
                this.getAttributeModifications().add(new AttributeModification(0.5f, SpecializationAttribute.CLEANING, character));
                break;
            }
            case BRINGTEA: {
                message = message + TextUtil.t("workguild.tea", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);
                pay = Util.getInt(100, 200);
                this.getAttributeModifications().add(new AttributeModification(0.5f, SpecializationAttribute.COOKING, character));
                break;
            }
            case SEX: {
                Sextype sex = null;
                int rnd = Util.getInt(0, 100);
                int grpSize = 2;
                if (rnd < 30 && (character.getTraits().contains(Trait.SLUT) || character.getTraits().contains(Trait.NYMPHO) || character.getTraits().contains(Trait.LEGACYWHORE) || character.getTraits().contains(Trait.BEDROOMPRINCESS) || character.getTraits().contains(Trait.SEXADDICT))) {
                    sex = Sextype.GROUP;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                    int chance = Util.getInt(0, (character.getFinalValue(Sextype.GROUP) + 12) * 2);
                    if (chance > 75) {
                        grpSize = Util.getInt(20, 36);
                        Object[] a = new Object[]{grpSize};
                        message = message + TextUtil.t("workguild.group.class", (Person)character, a);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(grpSize));
                        this.getAttributeModifications().add(new AttributeModification(-20.0f, EssentialAttributes.ENERGY, character));
                        pay += Util.getInt(150, 200) * grpSize;
                    } else if (chance > 50) {
                        grpSize = Util.getInt(6, 12);
                        Object[] a = new Object[]{grpSize};
                        message = message + TextUtil.t("workguild.group.students", (Person)character, a);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(grpSize));
                        this.getAttributeModifications().add(new AttributeModification(-15.0f, EssentialAttributes.ENERGY, character));
                        pay += Util.getInt(115, 130) * grpSize;
                    } else if (chance > 25) {
                        grpSize = Util.getInt(3, 6);
                        Object[] a = new Object[]{grpSize};
                        message = message + TextUtil.t("workguild.group.threeteachers", (Person)character, a);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(grpSize));
                        this.getAttributeModifications().add(new AttributeModification(-10.0f, EssentialAttributes.ENERGY, character));
                        pay += Util.getInt(100, 110) * grpSize;
                    } else {
                        message = message + TextUtil.t("workguild.group.twoteachers", character);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)2L);
                        this.getAttributeModifications().add(new AttributeModification(-5.0f, EssentialAttributes.ENERGY, character));
                        pay += 200;
                    }
                    this.getAttributeModifications().add(new AttributeModification(0.1f + (float)grpSize * 0.1f, sex, character));
                    break;
                }
                rnd = Util.getInt(0, 100);
                message = rnd < 20 ? message + TextUtil.t("workguild.sex.nice", character) : (rnd < 40 ? message + TextUtil.t("workguild.sex.angry", character) : (rnd < 60 ? message + TextUtil.t("workguild.sex.lazy", character) : (rnd < 80 ? message + TextUtil.t("workguild.sex.scary", character) : message + TextUtil.t("workguild.sex.gentle", character))));
                rnd = Util.getInt(0, 100);
                message = rnd < 20 ? message + "\n" + TextUtil.t("workguild.sex.nowork", character) : (rnd < 40 ? message + "\n" + TextUtil.t("workguild.sex.goodwork", character) : (rnd < 60 ? message + "\n" + TextUtil.t("workguild.sex.lotsofwork", character) : (rnd < 80 ? message + "\n" + TextUtil.t("workguild.sex.incompetent", character) : message + "\n" + TextUtil.t("workguild.sex.intheway", character))));
                rnd = Util.getInt(0, 120);
                if (rnd < 10 && !character.getTraits().contains(Trait.SMALLBOOBS)) {
                    message = message + "\n" + TextUtil.t("workguild.sex.boobjob", character);
                    sex = Sextype.TITFUCK;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                }
                if (rnd < 20) {
                    message = message + "\n" + TextUtil.t("workguild.sex.closet", character);
                    sex = Sextype.VAGINAL;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                } else if (rnd < 40) {
                    message = message + "\n" + TextUtil.t("workguild.sex.desk", character);
                    sex = Sextype.VAGINAL;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                } else if (rnd < 60) {
                    message = message + "\n" + TextUtil.t("workguild.sex.assram", character);
                    sex = Sextype.ANAL;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                } else if (rnd < 80) {
                    message = message + "\n" + TextUtil.t("workguild.sex.sitlap", character);
                    sex = Sextype.ANAL;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                } else if (rnd < 100) {
                    message = message + "\n" + TextUtil.t("workguild.sex.blowjob", character);
                    sex = Sextype.ORAL;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                } else {
                    message = message + "\n" + TextUtil.t("workguild.sex.fondle", character);
                    sex = Sextype.FOREPLAY;
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character);
                }
                character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                pay += Util.getInt(200, 400) + character.getFinalValue(sex) * character.getFinalValue(sex) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.5f, sex, character));
                break;
            }
            case TEACHWHORE: {
                message = message + TextUtil.t("workguild.teach.whore", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.SEDUCTION) * character.getFinalValue(SpecializationAttribute.SEDUCTION) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.SEDUCTION, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHMAID: {
                message = message + TextUtil.t("workguild.teach.maid", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.MAID, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.COOKING) * character.getFinalValue(SpecializationAttribute.CLEANING) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.SEDUCTION, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHBARTEND: {
                message = message + TextUtil.t("workguild.teach.bartend", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.BARTENDING) * character.getFinalValue(SpecializationAttribute.BARTENDING) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.BARTENDING, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHSTRIP: {
                message = message + TextUtil.t("workguild.teach.strip", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.STRIP) * character.getFinalValue(SpecializationAttribute.STRIP) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.STRIP, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHNURSE: {
                message = message + TextUtil.t("workguild.teach.nurse", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.MAGIC) * character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.MAGIC, character));
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.MEDICALKNOWLEDGE, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHSEX: {
                int rnd = Util.getInt(0, 5);
                Sextype sex2 = null;
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                if (rnd == 0 && student.getGender() != Gender.MALE) {
                    sex2 = Sextype.VAGINAL;
                    image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, student);
                    message = message + TextUtil.t("workguild.teach.sex.vaginal", (Person)character, student);
                    if (Util.getInt(0, 100) < character.getFinalValue(Sextype.VAGINAL)) {
                        message = message + "\n" + TextUtil.t("workguild.teach.sex.demonstration", (Person)character, student);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.VAGINAL, character));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                } else if (rnd == 1) {
                    sex2 = Sextype.ORAL;
                    image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, student);
                    message = message + TextUtil.t("workguild.teach.sex.oral", (Person)character, student);
                    if (Util.getInt(0, 100) < character.getFinalValue(Sextype.ORAL)) {
                        message = message + "\n" + TextUtil.t("workguild.teach.sex.demonstration", (Person)character, student);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.ORAL, character));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                } else if (rnd == 2) {
                    sex2 = Sextype.ANAL;
                    image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, student);
                    message = message + TextUtil.t("workguild.teach.sex.anal", (Person)character, student);
                    if (Util.getInt(0, 100) < character.getFinalValue(Sextype.ANAL)) {
                        message = message + "\n" + TextUtil.t("workguild.teach.sex.demonstration", (Person)character, student);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.ANAL, character));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                } else if (rnd == 3) {
                    sex2 = Sextype.TITFUCK;
                    image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, student);
                    message = message + TextUtil.t("workguild.teach.sex.titfuck", (Person)character, student);
                    if (Util.getInt(0, 100) < character.getFinalValue(Sextype.TITFUCK)) {
                        message = message + "\n" + TextUtil.t("workguild.teach.sex.demonstration", (Person)character, student);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.ANAL, character));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                } else {
                    sex2 = Sextype.FOREPLAY;
                    image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, student);
                    message = message + TextUtil.t("workguild.teach.sex.foreplay", (Person)character, student);
                    if (Util.getInt(0, 100) < character.getFinalValue(Sextype.FOREPLAY)) {
                        message = message + "\n" + TextUtil.t("workguild.teach.sex.demonstration", (Person)character, student);
                        this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.FOREPLAY, character));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character);
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (Long)1L);
                    }
                }
                pay = Util.getInt(100, 200) + character.getFinalValue(sex2) * character.getFinalValue(sex2) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHALCHEMY: {
                message = message + TextUtil.t("workguild.teach.alchemy", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) * character.getFinalValue(SpecializationAttribute.MAGIC) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.SEDUCTION, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHFIGHT: {
                message = message + TextUtil.t("workguild.teach.fight", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.VETERAN) * character.getFinalValue(SpecializationAttribute.VETERAN) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.VETERAN, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHADVERTISING: {
                message = message + TextUtil.t("workguild.teach.advertise", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(SpecializationAttribute.ADVERTISING) * character.getFinalValue(SpecializationAttribute.ADVERTISING) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, SpecializationAttribute.ADVERTISING, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHBASIC: {
                message = message + TextUtil.t("workguild.teach.basic", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, student);
                pay = Util.getInt(100, 200) + character.getCharisma() + character.getCommand() + character.getIntelligence() + character.getStamina() + character.getStrength();
                pay *= 3;
                this.getAttributeModifications().add(new AttributeModification(0.04f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHGROUP: {
                message = message + TextUtil.t("workguild.teach.group", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(Sextype.GROUP) * character.getFinalValue(Sextype.GROUP) / 5;
                if (Util.getInt(0, 100) < character.getFinalValue(Sextype.GROUP)) {
                    message = message + "\n" + TextUtil.t("workguild.teach.sex.demonstration", (Person)character, student);
                    this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.GROUP, character));
                    image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                    character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(Util.getInt(3, 7)));
                    pay *= 1;
                }
                this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.GROUP, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
                break;
            }
            case TEACHMONSTER: {
                message = message + TextUtil.t("workguild.teach.monster", (Person)character, student);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTER, student);
                pay = Util.getInt(100, 200) + character.getFinalValue(Sextype.MONSTER) * character.getFinalValue(Sextype.MONSTER) / 5;
                this.getAttributeModifications().add(new AttributeModification(0.8f, Sextype.MONSTER, character));
                this.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.COMMAND, character));
            }
        }
        Object[] argument = new Object[]{pay};
        if (pay != 0) {
            message = message + "\n" + TextUtil.t("workguild.result", (Person)character, argument);
        }
        this.setIncome(pay);
        if (image2 != null) {
            return new MessageData(message, image, image2, this.getCharacter().getBackground());
        }
        return new MessageData(message, image, image2, this.getCharacter().getBackground());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.07f, SpecializationAttribute.EXPERIENCE));
        return modifications;
    }

    public int getactionType() {
        return this.actionType;
    }

    public void setactionType(int actionType) {
        this.actionType = actionType;
    }

    private static enum teachAction {
        PAPERWORK,
        CLEANING,
        BRINGTEA,
        SEX,
        SLUT,
        TEACHWHORE,
        TEACHMAID,
        TEACHSEX,
        TEACHBARTEND,
        TEACHSTRIP,
        TEACHNURSE,
        TEACHALCHEMY,
        TEACHFIGHT,
        TEACHADVERTISING,
        TEACHBASIC,
        TEACHGROUP,
        TEACHMONSTER;

    }
}

