/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
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

public class Study
extends RunningActivity {
    private SpecializationType specialization;

    @Override
    public void init() {
        ArrayList options = new ArrayList();
        for (SpecializationType specialization : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
            if (specialization == SpecializationType.TRAINER || specialization == SpecializationType.SLAVE || specialization == SpecializationType.UNDERAGE || !specialization.isTeachable()) continue;
            SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
            option.setSelectionObject(specialization);
            String type = specialization == SpecializationType.SEX || specialization == SpecializationType.KINKYSEX ? "sex" : "specialization";
            if (this.isBasicTraining(specialization, this.getCharacter())) {
                Object[] arguments = new Object[]{specialization.getText()};
                option.setButtonText(TextUtil.t("study.option." + type + ".free", (Person)this.getCharacter(), arguments));
            } else {
                int price = 1000;
                if (this.isHiddenTraining(specialization, this.getCharacter()) && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY)) {
                    price = 20000;
                } else if (this.isAdvancedTraining(specialization, this.getCharacter())) {
                    price = 10000;
                }
                if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTLIBRARY)) {
                    price = (int)((double)price * 0.8);
                }
                Object[] arguments = new Object[]{specialization.getText(), price};
                option.setButtonText(TextUtil.t("study.option." + type + ".costs", (Person)this.getCharacter(), arguments));
                if (!Jasbro.getInstance().getData().canAfford(price)) {
                    option.setEnabled(false);
                }
            }
            options.add(option);
        }
        SelectionData option = new SelectionData();
        option.setButtonText(TextUtil.t("ui.cancel"));
        options.add(option);
        SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), null, this.getCharacter().getBackground(), TextUtil.t("study.option.description", this.getCharacter()));
        this.specialization = (SpecializationType)selectedOption.getSelectionObject();
        if (this.specialization != null) {
            int price = 0;
            price = this.isHiddenTraining(this.specialization, this.getCharacter()) && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY) ? 20000 : (this.isAdvancedTraining(this.specialization, this.getCharacter()) ? 10000 : (!this.isBasicTraining(this.specialization, this.getCharacter()) ? 1000 : 0));
            if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTLIBRARY)) {
                price *= 0;
            }
            Jasbro.getInstance().getData().spendMoney(price, this);
        }
    }

    @Override
    public MessageData getBaseMessage() {
        if (this.specialization != null) {
            String message = TextUtil.t("study.basic", (Person)this.getCharacter(), this.specialization.getText());
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, this.getCharacter());
            return new MessageData(message, image, this.getCharacter().getBackground());
        }
        return new MessageData(TextUtil.t("idle.basic", this.getCharacter()), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), this.getCharacter().getBackground(), true);
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        if (this.specialization != null) {
            ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
            if (this.specialization != null) {
                if (this.getCharacters().get(0).getTraits().contains(Trait.CLEVER)) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
                } else if (this.getCharacters().get(0).getTraits().contains(Trait.STUPID)) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
                }
                float change = (5.0f + (float)this.specialization.getAssociatedAttributes().size() - 1.0f) / (float)this.specialization.getAssociatedAttributes().size();
                if (this.isHiddenTraining(this.specialization, this.getCharacter()) && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY)) {
                    change *= 4.0f;
                } else if (this.isAdvancedTraining(this.specialization, this.getCharacter())) {
                    change *= 1.0f;
                }
                for (AttributeType attributeType : this.specialization.getAssociatedAttributes()) {
                    modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, change, attributeType));
                }
            }
            return modifications;
        }
        return new Idle().getStatModifications();
    }

    private boolean isBasicTraining(SpecializationType specialization, Charakter character) {
        if (character.getSpecializations().contains(specialization)) {
            return false;
        }
        if (specialization == SpecializationType.DOMINATRIX) {
            return character.getAttribute(SpecializationAttribute.DOMINATE).getInternValue() > 1.0f;
        }
        for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
            if (!(character.getAttribute(attributeType).getInternValue() > 1.0f)) continue;
            return false;
        }
        return true;
    }

    private boolean isAdvancedTraining(SpecializationType specialization, Charakter character) {
        for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
            if (!(character.getAttribute(attributeType).getInternValue() >= 20.0f)) continue;
            return true;
        }
        return false;
    }

    private boolean isHiddenTraining(SpecializationType specialization, Charakter character) {
        for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
            if (!(character.getAttribute(attributeType).getInternValue() >= 50.0f)) continue;
            return true;
        }
        return false;
    }
}

