/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class BodyWrap
extends RunningActivity {
    private Charakter nurse;

    @Override
    public void init() {
        for (Charakter character : this.getCharacters()) {
            if (!character.getSpecializations().contains(SpecializationType.NURSE) || !character.getTraits().contains(Trait.COSMETICS) || this.nurse != null && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) <= this.nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)) continue;
            this.nurse = character;
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("bodywrap.basic", this.nurse);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, this.nurse);
        return new MessageData(message, image, this.getCharacter().getBackground());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        int skill = this.nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
        this.modifyIncome(-30 * this.getCharacters().size());
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 1.5f, SpecializationAttribute.MEDICALKNOWLEDGE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, -25.0f, EssentialAttributes.ENERGY));
        if (this.nurse.getTraits().contains(Trait.ALTRUISTIC)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, 0.5f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.nurse, -0.5f, EssentialAttributes.MOTIVATION));
        }
        for (Charakter character : this.getCharacters()) {
            if (character == this.nurse) continue;
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.1f + (float)(skill / 100), BaseAttributeTypes.CHARISMA));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -10.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.6f, EssentialAttributes.MOTIVATION));
        }
        return modifications;
    }
}

