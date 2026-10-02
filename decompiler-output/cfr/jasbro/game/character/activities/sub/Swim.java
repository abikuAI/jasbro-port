/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Swim
extends RunningActivity {
    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("swim.basic", this.getCharacter());
        return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, this.getCharacter()), this.getCharacterLocation().getImage());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.getCharacters().get(0).getTraits().contains(Trait.FIT)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 8.0f, EssentialAttributes.MOTIVATION));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f, BaseAttributeTypes.STAMINA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, BaseAttributeTypes.STRENGTH));
        return modifications;
    }
}

