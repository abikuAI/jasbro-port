/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Util;
import jasbro.game.character.Charakter;
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

public class Read
extends RunningActivity {
    private MessageData message;
    private Charakter character;

    @Override
    public void init() {
        this.character = this.getCharacter();
    }

    @Override
    public MessageData getBaseMessage() {
        if (this.message == null) {
            List tags = this.character.getBaseTags();
            tags.add(0, ImageTag.STUDY);
            tags.add(1, ImageTag.CLOTHED);
            this.message = new MessageData(TextUtil.t("read.basic", this.character), ImageUtil.getInstance().getImageDataByTags(tags, this.character.getImages()), this.getCharacterLocation().getImage());
            this.message.addToMessage("\n\n");
            if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 5) {
                this.message.addToMessage(TextUtil.t("read.toolow" + Util.getInt(1, 3), this.character));
            } else if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 10) {
                this.message.addToMessage(TextUtil.t("read.easy" + Util.getInt(1, 3), this.character));
            } else if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 25) {
                this.message.addToMessage(TextUtil.t("read.normal" + Util.getInt(1, 3), this.character));
            } else if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 50) {
                this.message.addToMessage(TextUtil.t("read.hard" + Util.getInt(1, 3), this.character));
            } else {
                this.message.addToMessage(TextUtil.t("read.lunatic" + Util.getInt(0, 2), this.character));
            }
        }
        return this.message;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 10.0f, EssentialAttributes.ENERGY));
        if (this.character.getTraits().contains(Trait.CLEVER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, EssentialAttributes.MOTIVATION));
        } else if (this.character.getTraits().contains(Trait.STUPID)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, EssentialAttributes.MOTIVATION));
        }
        if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 5) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05f, BaseAttributeTypes.INTELLIGENCE));
        } else if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 10) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
        } else if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 25) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, BaseAttributeTypes.INTELLIGENCE));
        } else if (this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) < 50) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f, BaseAttributeTypes.INTELLIGENCE));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.6f, BaseAttributeTypes.INTELLIGENCE));
        }
        return modifications;
    }
}

