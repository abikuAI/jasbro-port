/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Cook
extends RunningActivity {
    private static final float BASEMODIFICATION = 1.0f;
    private static final float OBEDIENCEMODIFICATION = 0.01f;

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        float modification = 1.0f;
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, modification, SpecializationAttribute.COOKING));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01f, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYMAID)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
        }
        if (this.getCharacter().getTraits().contains(Trait.RESTAURATEUR)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALLHOUSE, 0.8f + (float)this.getCharacter().getAttribute(SpecializationAttribute.COOKING).getValue() / 12.0f, EssentialAttributes.HEALTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALLHOUSE, 4.0f + (float)this.getCharacter().getAttribute(SpecializationAttribute.COOKING).getValue() / 6.0f, EssentialAttributes.ENERGY));
        return modifications;
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);
        String message = TextUtil.t("cook.basic", character);
        return new MessageData(message, image, null);
    }
}

