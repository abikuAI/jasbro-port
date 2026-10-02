/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Talk
extends RunningActivity {
    private Charakter character1;
    private Charakter character2;
    private TalkType talkType;

    @Override
    public void init() {
        Charakter tmpCharacter1 = this.getCharacters().get(0);
        Charakter tmpCharacter2 = this.getCharacters().get(1);
        if (tmpCharacter1.getType() == CharacterType.TRAINER && tmpCharacter2.getType() != CharacterType.TRAINER || tmpCharacter1.calculateValue() > tmpCharacter2.calculateValue() && (tmpCharacter1.getType() == CharacterType.TRAINER || tmpCharacter2.getType() != CharacterType.TRAINER)) {
            this.character1 = tmpCharacter1;
            this.character2 = tmpCharacter2;
        } else {
            this.character1 = tmpCharacter2;
            this.character2 = tmpCharacter1;
        }
        if (this.getPlannedActivity().getSelectedOption() == null) {
            List options = this.getTalkOptions(this.character1, this.character2);
            SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character1), null, this.character1.getBackground(), TextUtil.t("talk.decision", (Person)this.character1, this.character2));
            this.talkType = (TalkType)((Object)selectedOption.getSelectionObject());
        } else {
            this.talkType = (TalkType)((Object)this.getPlannedActivity().getSelectedOption().getSelectionObject());
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String message = this.talkType.getTalkDescription(this.character1, this.character2);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character2);
        image = this.talkType != TalkType.PET ? ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character2) : ImageUtil.getInstance().getImageDataByTag(ImageTag.CATGIRL, this.character2);
        return new MessageData(message, image, this.getCharacter().getBackground());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.character1.getType() == CharacterType.TRAINER && this.character2.getType() == CharacterType.SLAVE) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.4f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.4f, BaseAttributeTypes.COMMAND));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, BaseAttributeTypes.INTELLIGENCE));
        if (this.talkType == TalkType.PET) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character2, 15.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character2, 1.5f, SpecializationAttribute.CATGIRL));
        }
        return modifications;
    }

    @Override
    public void perform() {
        this.talkType.applyModifier(this.character1, this.character2);
    }

    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        if (plannedActivity.getCharacters().size() < 2) {
            return null;
        }
        return new ArrayList(this.getTalkOptions(plannedActivity.getCharacters().get(0), plannedActivity.getCharacters().get(1)));
    }

    public List<SelectionData<TalkType>> getTalkOptions(Charakter character1, Charakter character2) {
        if ((character1.getType() != CharacterType.TRAINER || character2.getType() == CharacterType.TRAINER) && (character1.calculateValue() <= character2.calculateValue() || character1.getType() != CharacterType.TRAINER && character2.getType() == CharacterType.TRAINER)) {
            Charakter tmpCharacter = character1;
            character1 = character2;
            character2 = tmpCharacter;
        }
        ArrayList<SelectionData<TalkType>> options = new ArrayList<SelectionData<TalkType>>();
        for (TalkType talk : TalkType.values()) {
            if (talk == TalkType.PET && !character2.getSpecializations().contains(SpecializationType.CATGIRL)) continue;
            SelectionData<TalkType> selectionData = new SelectionData<TalkType>();
            selectionData.setButtonText(talk.getText());
            selectionData.setSelectionObject(talk);
            selectionData.setTooltipText(talk.getDescription(character1, character2));
            selectionData.setShortText(talk.getText());
            options.add(selectionData);
        }
        return options;
    }

    public static enum TalkType {
        INTIMIDATE,
        MOTIVATE,
        PET;


        public String getText() {
            return TextUtil.t("talk." + this.toString());
        }

        public String getDescription(Charakter character1, Charakter character2) {
            return TextUtil.t("talk." + this.toString() + ".description", (Person)character1, character2);
        }

        public void applyModifier(Charakter character1, Charakter character2) {
            if (this == INTIMIDATE) {
                character2.addCondition(new Buff.Intimidated(character1));
            } else if (this == MOTIVATE) {
                character2.addCondition(new Buff.Motivated(character1));
            }
        }

        public String getTalkDescription(Charakter character1, Charakter character2) {
            return TextUtil.t("talk." + this.toString() + ".basic", (Person)character1, character2);
        }
    }
}

