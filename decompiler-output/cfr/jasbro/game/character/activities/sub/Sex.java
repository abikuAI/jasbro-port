/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Sex
extends RunningActivity {
    private Sextype sexType;
    private Charakter character1;
    private Charakter character2;

    @Override
    public void init() {
        if (this.getCharacters().get(0).getGender() == Gender.MALE || this.getCharacters().get(0).getGender() == this.getCharacters().get(1).getGender() || this.getCharacters().get(0).getGender() == Gender.FUTA && this.getCharacters().get(1).getGender() != Gender.MALE) {
            this.character1 = this.getCharacters().get(0);
            this.character2 = this.getCharacters().get(1);
        } else {
            this.character1 = this.getCharacters().get(1);
            this.character2 = this.getCharacters().get(0);
        }
        if (this.getPlannedActivity().getSelectedOption() != null) {
            this.sexType = (Sextype)this.getPlannedActivity().getSelectedOption().getSelectionObject();
        } else {
            List options = this.getSextypeOptions(this.character1, this.character2);
            List tags1 = this.character1.getBaseTags();
            List tags2 = this.character2.getBaseTags();
            tags1.add(0, ImageTag.NAKED);
            tags1.add(1, ImageTag.CLEANED);
            tags2.add(0, ImageTag.NAKED);
            tags2.add(1, ImageTag.CLEANED);
            SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTags(tags1, this.character1.getImages()), ImageUtil.getInstance().getImageDataByTags(tags2, this.character2.getImages()), this.character1.getBackground(), TextUtil.t("sex.option.description", (Person)this.character1, this.character2));
            this.sexType = (Sextype)selectedOption.getSelectionObject();
        }
    }

    @Override
    public MessageData getBaseMessage() {
        this.setMinimumObedience(this.sexType.getObedienceRequired());
        MessageData messageData = new MessageData();
        Future<MessageData> future = Jasbro.getThreadpool().submit(new ImageUtil.BestImageSelection(this.sexType, this.character1, this.character2));
        messageData.addFuture(future);
        return messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, this.sexType));
        if (this.sexType != Sextype.BONDAGE) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.03f, BaseAttributeTypes.STAMINA));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.03f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01f, BaseAttributeTypes.STAMINA));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        if (this.character1.getTraits().contains(Trait.NATURAL)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character1, 0.8f, EssentialAttributes.MOTIVATION));
        }
        if (this.character2.getTraits().contains(Trait.NATURAL)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character2, 0.8f, EssentialAttributes.MOTIVATION));
        }
        return modifications;
    }

    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        if (plannedActivity.getCharacters().size() < 2) {
            return null;
        }
        return new ArrayList(this.getSextypeOptions(plannedActivity.getCharacters().get(0), plannedActivity.getCharacters().get(1)));
    }

    public List<SelectionData<Sextype>> getSextypeOptions(Charakter character1, Charakter character2) {
        List<Sextype> possibleSextypes = Sextype.getPossibleSextypes(character1.getGender(), character2.getGender());
        ArrayList<SelectionData<Sextype>> options = new ArrayList<SelectionData<Sextype>>();
        for (Sextype curSexType : possibleSextypes) {
            SelectionData<Sextype> option = new SelectionData<Sextype>();
            option.setSelectionObject(curSexType);
            option.setButtonText(TextUtil.t("sex.option." + curSexType.toString(), (Person)character1, character2));
            option.setShortText(curSexType.getText());
            options.add(option);
        }
        return options;
    }

    @Override
    public Sextype getSextype() {
        return this.sexType;
    }
}

