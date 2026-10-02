/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Practice
extends RunningActivity {
    private int actionType = 0;
    private List<String> actions = new ArrayList<String>();

    @Override
    public void init() {
        int choice;
        if (this.getCharacter().getFinalValue(EssentialAttributes.MOTIVATION) < 70) {
            this.actions.add("practice.lazy");
        }
        this.actions.add("practice.stamina");
        this.actions.add("practice.strength");
        if (this.getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION) > 20) {
            this.actions.add("practice.stamina");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION) > 40) {
            this.actions.add("practice.stamina");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION) > 60) {
            this.actions.add("practice.stamina");
        }
        if (this.getCharacter().getFinalValue(Sextype.GROUP) > 50) {
            this.actions.add("practice.stamina");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.STRIP) > 20) {
            this.actions.add("practice.dance");
            this.actions.add("practice.stamina");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.STRIP) > 40) {
            this.actions.add("practice.dance");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.STRIP) > 60) {
            this.actions.add("practice.dance");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.VETERAN) > 20) {
            this.actions.add("practice.fight");
            this.actions.add("practice.strength");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.VETERAN) > 40) {
            this.actions.add("practice.fight");
            this.actions.add("practice.strength");
        }
        if (this.getCharacter().getFinalValue(SpecializationAttribute.VETERAN) > 60) {
            this.actions.add("practice.fight");
            this.actions.add("practice.strength");
        }
        if (this.actions.get(choice = (int)(Math.random() * (double)this.actions.size())) == "practice.lazy") {
            this.setactionType(1);
        }
        if (this.actions.get(choice) == "practice.stamina") {
            this.setactionType(2);
        }
        if (this.actions.get(choice) == "practice.strength") {
            this.setactionType(3);
        }
        if (this.actions.get(choice) == "practice.dance") {
            this.setactionType(4);
        }
        if (this.actions.get(choice) == "practice.fight") {
            this.setactionType(5);
        }
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        String message = "";
        message = TextUtil.t("practice.basic", character);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
        switch (this.getactionType()) {
            case 1: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
                message = message + "\n\n" + TextUtil.t("practice.lazy", character);
                break;
            }
            case 2: {
                message = message + "\n\n" + TextUtil.t("practice.stamina", character);
                break;
            }
            case 3: {
                message = message + "\n\n" + TextUtil.t("practice.strength", character);
                break;
            }
            case 4: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                message = message + "\n\n" + TextUtil.t("practice.dance", character);
                break;
            }
            case 5: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character);
                message = message + "\n\n" + TextUtil.t("practice.fight", character);
            }
        }
        return new MessageData(message, image, this.getCharacter().getBackground());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0f, EssentialAttributes.ENERGY));
        switch (this.getactionType()) {
            case 1: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.5f, EssentialAttributes.MOTIVATION));
                break;
            }
            case 2: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -35.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, BaseAttributeTypes.STAMINA));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
                break;
            }
            case 3: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -35.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, BaseAttributeTypes.STRENGTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
                break;
            }
            case 4: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -35.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, SpecializationAttribute.STRIP));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
                break;
            }
            case 5: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -35.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, SpecializationAttribute.VETERAN));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
            }
        }
        return modifications;
    }

    public int getactionType() {
        return this.actionType;
    }

    public void setactionType(int actionType) {
        this.actionType = actionType;
    }
}

