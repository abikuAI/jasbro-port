/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Train
extends RunningActivity {
    private Charakter teacher;
    private List<Charakter> students = new ArrayList<Charakter>();
    private SpecializationType specialization;

    @Override
    public void init() {
        List options = this.getTrainOptions(this.getCharacters());
        if (this.getPlannedActivity().getSelectedOption() == null) {
            SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.teacher), null, this.teacher.getBackground(), TextUtil.t("train.option.description", (Person)this.teacher, TextUtil.listCharacters(this.students)));
            this.specialization = (SpecializationType)selectedOption.getSelectionObject();
        } else {
            this.specialization = (SpecializationType)this.getPlannedActivity().getSelectedOption().getSelectionObject();
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("train.basic", (Person)this.teacher, TextUtil.listCharacters(this.students));
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, this.teacher);
        MessageData messageData = new MessageData(message, image, this.getCharacter().getBackground());
        for (Charakter character : this.students) {
            List tags = character.getBaseTags();
            tags.add(0, ImageTag.STUDY);
            image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
            messageData.addImage(image);
        }
        return messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.specialization != null) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.2f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.2f, BaseAttributeTypes.COMMAND));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05f, BaseAttributeTypes.INTELLIGENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
            for (AttributeType attributeType : this.specialization.getAssociatedAttributes()) {
                float modTeacher = attributeType instanceof BaseAttributeTypes ? 0.005f : 0.05f;
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.teacher, modTeacher, attributeType));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.teacher, 0.3f, SpecializationAttribute.EXPERIENCE));
            }
            float modifier = 1.0f - 0.1f * (float)(this.students.size() - 1);
            for (Charakter student : this.students) {
                for (AttributeType attributeType : this.specialization.getAssociatedAttributes()) {
                    int diff;
                    float baseMod = attributeType instanceof BaseAttributeTypes ? 0.08f : 0.8f;
                    AttributeType attributeType2 = attributeType;
                    if (attributeType == BaseAttributeTypes.COMMAND && student.getType() == CharacterType.SLAVE) {
                        attributeType2 = BaseAttributeTypes.OBEDIENCE;
                    }
                    if ((diff = this.teacher.getFinalValue(attributeType) - (int)student.getAttribute(attributeType2).getInternValue()) > 0) {
                        baseMod += (float)diff / 5.0f;
                    }
                    float expMod = 0.0f;
                    expMod += (baseMod += (float)this.teacher.getFinalValue(attributeType) / 20.0f) * (float)((this.teacher.getIntelligence() + student.getIntelligence() - 12) / 2) / 100.0f;
                    if (this.teacher.getType() == CharacterType.TRAINER && student.getType() == CharacterType.SLAVE) {
                        expMod += baseMod * (float)((this.teacher.getCommand() + student.getObedience() - 12) / 2) / 100.0f;
                    }
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, student, (baseMod + expMod) * modifier, attributeType2));
                }
            }
        }
        return modifications;
    }

    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        if (plannedActivity.getCharacters().size() < 2) {
            return null;
        }
        return new ArrayList(this.getTrainOptions(plannedActivity.getCharacters()));
    }

    public List<SelectionData<SpecializationType>> getTrainOptions(List<Charakter> characters) {
        for (Charakter character : characters) {
            if (this.teacher == null) {
                this.teacher = character;
                continue;
            }
            if (character.getType() == CharacterType.TRAINER && this.teacher.getType() != CharacterType.TRAINER || character.getType() == this.teacher.getType() && (character.getFinalValue(BaseAttributeTypes.COMMAND) > this.teacher.getFinalValue(BaseAttributeTypes.COMMAND) || character.getFinalValue(BaseAttributeTypes.COMMAND) == this.teacher.getFinalValue(BaseAttributeTypes.COMMAND) && character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) > this.teacher.getFinalValue(BaseAttributeTypes.INTELLIGENCE))) {
                this.students.add(this.teacher);
                this.teacher = character;
                continue;
            }
            this.students.add(character);
        }
        ArrayList<SelectionData<SpecializationType>> options = new ArrayList<SelectionData<SpecializationType>>();
        for (SpecializationType specialization : this.teacher.getSpecializations()) {
            if (!specialization.isTeachable() || !Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations().contains(specialization)) continue;
            SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
            option.setSelectionObject(specialization);
            if (specialization == SpecializationType.TRAINER || specialization == SpecializationType.SLAVE) {
                option.setButtonText(TextUtil.t("train.option." + specialization.toString(), this.students));
            } else {
                option.setButtonText(TextUtil.t("train.option", TextUtil.listCharacters(this.students), specialization.getText()));
            }
            option.setShortText(specialization.getText());
            options.add(option);
        }
        return options;
    }
}

