/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Illness;
import jasbro.game.events.MessageData;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Camp
extends RunningActivity {
    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("camp", this.getCharacters());
        MessageData messageData = new MessageData(message, null, this.getCharacters().get(0).getBackground());
        for (Charakter character : this.getCharacters()) {
            List tags = character.getBaseTags();
            tags.add(0, ImageTag.SLEEP);
            tags.add(1, ImageTag.CLEANED);
            ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
            messageData.addImage(image);
        }
        return messageData;
    }

    @Override
    public void perform() {
        this.modifyIncome(-25 * this.getCharacters().size());
        for (Charakter character : this.getCharacters()) {
            if (Jasbro.getInstance().getData().getTime() != Time.NIGHT || Util.getInt(0, 100) >= 5) continue;
            Illness.Flu flu = new Illness.Flu(false);
            character.addCondition(flu);
            this.getMessages().get(0).addToMessage("\n\n" + flu.getTextStart());
        }
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 40.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.HEALTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, (float)Util.getInt(-2, 3) * 0.2f, EssentialAttributes.MOTIVATION));
        return modifications;
    }
}

