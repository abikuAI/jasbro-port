/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Govern
extends RunningActivity {
    private SpecializationType specialization;
    private List<Charakter> students = new ArrayList<Charakter>();
    private BaseAttributeTypes statBoost;
    private SpecializationAttribute skillBoost1 = null;
    private SpecializationAttribute skillBoost2 = null;
    private Sextype sexBoost1 = null;
    private Sextype sexBoost2 = null;

    @Override
    public void init() {
        List options = this.getSuperviseOptions(this.getCharacter());
        if (this.getPlannedActivity().getSelectedOption() == null) {
            SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacters().get(0)), null, this.getCharacters().get(0).getBackground(), TextUtil.t("train.option.description", (Person)this.getCharacters().get(0), TextUtil.listCharacters(this.students)));
            this.specialization = (SpecializationType)selectedOption.getSelectionObject();
        } else {
            this.specialization = (SpecializationType)this.getPlannedActivity().getSelectedOption().getSelectionObject();
        }
        for (Room room : this.getHouse().getRooms()) {
            block15: for (Charakter character : room.getCurrentUsage().getCharacters()) {
                if (character.getActivity().getType() == ActivityType.WHORE && this.specialization == SpecializationType.WHORE) {
                    this.students.add(character);
                    this.statBoost = Util.getInt(0, 10) > 5 ? BaseAttributeTypes.CHARISMA : BaseAttributeTypes.STAMINA;
                    this.skillBoost1 = SpecializationAttribute.SEDUCTION;
                    switch (Util.getInt(0, 7)) {
                        case 1: {
                            this.sexBoost1 = Sextype.TITFUCK;
                            break;
                        }
                        case 2: {
                            this.sexBoost1 = Sextype.ANAL;
                            break;
                        }
                        case 3: {
                            this.sexBoost1 = Sextype.ORAL;
                            break;
                        }
                        case 4: {
                            this.sexBoost1 = Sextype.ANAL;
                            break;
                        }
                        case 5: {
                            this.sexBoost2 = Sextype.FOREPLAY;
                            break;
                        }
                        default: {
                            this.sexBoost1 = Sextype.VAGINAL;
                        }
                    }
                    switch (Util.getInt(0, 7)) {
                        case 1: {
                            this.sexBoost2 = Sextype.TITFUCK;
                            continue block15;
                        }
                        case 2: {
                            this.sexBoost2 = Sextype.ANAL;
                            continue block15;
                        }
                        case 3: {
                            this.sexBoost2 = Sextype.ORAL;
                            continue block15;
                        }
                        case 4: {
                            this.sexBoost2 = Sextype.ANAL;
                            continue block15;
                        }
                        case 5: {
                            this.sexBoost2 = Sextype.FOREPLAY;
                            continue block15;
                        }
                    }
                    this.sexBoost2 = Sextype.VAGINAL;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.PUBLICUSE && this.specialization == SpecializationType.KINKYSEX) {
                    this.students.add(character);
                    this.statBoost = BaseAttributeTypes.STAMINA;
                    this.sexBoost1 = Sextype.GROUP;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.CLEAN && this.specialization == SpecializationType.KINKYSEX) {
                    this.students.add(character);
                    this.statBoost = BaseAttributeTypes.OBEDIENCE;
                    this.skillBoost1 = SpecializationAttribute.CLEANING;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.COOK && this.specialization == SpecializationType.KINKYSEX) {
                    this.students.add(character);
                    this.statBoost = BaseAttributeTypes.OBEDIENCE;
                    this.skillBoost1 = SpecializationAttribute.COOKING;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.SELLFOOD && this.specialization == SpecializationType.KINKYSEX) {
                    this.students.add(character);
                    this.statBoost = BaseAttributeTypes.OBEDIENCE;
                    this.skillBoost1 = SpecializationAttribute.COOKING;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.SUCK && this.specialization == SpecializationType.WHORE) {
                    this.students.add(character);
                    this.statBoost = BaseAttributeTypes.STAMINA;
                    this.skillBoost1 = SpecializationAttribute.SEDUCTION;
                    this.sexBoost1 = Sextype.ORAL;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.TEASE && this.specialization == SpecializationType.WHORE) {
                    this.students.add(character);
                    this.statBoost = BaseAttributeTypes.STAMINA;
                    this.skillBoost1 = SpecializationAttribute.SEDUCTION;
                    this.sexBoost1 = Sextype.FOREPLAY;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.BARTEND && this.specialization == SpecializationType.BARTENDER) {
                    this.students.add(character);
                    this.statBoost = Util.getInt(0, 10) > 5 ? BaseAttributeTypes.CHARISMA : BaseAttributeTypes.INTELLIGENCE;
                    this.skillBoost1 = SpecializationAttribute.BARTENDING;
                    continue;
                }
                if (character.getActivity().getType() == ActivityType.STRIP && this.specialization == SpecializationType.DANCER) {
                    this.students.add(character);
                    this.statBoost = Util.getInt(0, 10) > 5 ? BaseAttributeTypes.CHARISMA : BaseAttributeTypes.STAMINA;
                    this.skillBoost1 = SpecializationAttribute.STRIP;
                    continue;
                }
                if (this.specialization != SpecializationType.TRAINER) continue;
                this.students.add(character);
                this.statBoost = BaseAttributeTypes.OBEDIENCE;
            }
        }
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.08f, BaseAttributeTypes.COMMAND));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.08f, SpecializationAttribute.EXPERIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        if (this.students != null) {
            for (Charakter character : this.students) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f, EssentialAttributes.MOTIVATION));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.02f, this.statBoost));
                if (this.skillBoost1 != null) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.5f, this.skillBoost1));
                }
                if (this.skillBoost2 != null) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.5f, this.skillBoost2));
                }
                if (this.sexBoost1 != null) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.8f, this.sexBoost1));
                }
                if (this.sexBoost2 == null) continue;
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.8f, this.sexBoost2));
            }
        }
        return modifications;
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);
        String message = TextUtil.t("govern.basic1", (Person)character, character.getBackground());
        message = this.students.size() != 0 && this.specialization != SpecializationType.TRAINER ? message + " " + TextUtil.t("govern.specific", this.students) : (this.specialization == SpecializationType.TRAINER ? TextUtil.t("govern.basic2", (Person)character, character.getBackground()) : TextUtil.t("govern.none", (Person)character, character.getBackground()));
        return new MessageData(message, image, null);
    }

    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        return new ArrayList(this.getSuperviseOptions(plannedActivity.getCharacters().get(0)));
    }

    public List<SelectionData<SpecializationType>> getSuperviseOptions(Charakter character) {
        ArrayList<SelectionData<SpecializationType>> options = new ArrayList<SelectionData<SpecializationType>>();
        for (SpecializationType specialization : character.getSpecializations()) {
            if (!specialization.isTeachable() || !Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations().contains(specialization) || specialization != SpecializationType.WHORE && specialization != SpecializationType.BARTENDER && specialization != SpecializationType.TRAINER && specialization != SpecializationType.DANCER && specialization != SpecializationType.KINKYSEX && specialization != SpecializationType.NURSE) continue;
            SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
            option.setSelectionObject(specialization);
            switch (specialization) {
                case TRAINER: {
                    option.setButtonText(TextUtil.t("supervise.option.all"));
                    option.setShortText(specialization.getText());
                    break;
                }
                case WHORE: {
                    option.setButtonText(TextUtil.t("supervise.option.whore"));
                    option.setShortText(specialization.getText());
                    break;
                }
                case BARTENDER: {
                    option.setButtonText(TextUtil.t("supervise.option.bartender"));
                    option.setShortText(specialization.getText());
                    break;
                }
                case DANCER: {
                    option.setButtonText(TextUtil.t("supervise.option.dancer"));
                    option.setShortText(specialization.getText());
                    break;
                }
                case KINKYSEX: {
                    option.setButtonText(TextUtil.t("supervise.option.kinky"));
                    option.setShortText(specialization.getText());
                    break;
                }
                case NURSE: {
                    option.setButtonText(TextUtil.t("supervise.option.nurse"));
                    option.setShortText(specialization.getText());
                    break;
                }
            }
            option.setShortText(specialization.getText());
            options.add(option);
        }
        return options;
    }
}

