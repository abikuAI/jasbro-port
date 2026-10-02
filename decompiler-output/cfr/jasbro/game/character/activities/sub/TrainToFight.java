/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class TrainToFight
extends RunningActivity {
    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("traintofight.basic", this.getCharacter());
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.getCharacter());
        return new MessageData(message, image, this.getCharacter().getBackground());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (Jasbro.getInstance().getData().getDay() % 3 == 0) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -3.0f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, EssentialAttributes.MOTIVATION));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -30.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3f, BaseAttributeTypes.STRENGTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, BaseAttributeTypes.STAMINA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.7f, SpecializationAttribute.VETERAN));
        return modifications;
    }
}

