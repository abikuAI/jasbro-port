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
import jasbro.game.housing.CleanState;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Sleep
extends RunningActivity {
    private MessageData messageData;

    @Override
    public MessageData getBaseMessage() {
        String message = "";
        List<Charakter> characters = this.getCharacters();
        this.messageData = new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, this.getCharacter()), this.getBackground());
        for (int i = 1; i < characters.size(); ++i) {
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, this.getCharacters().get(i)));
        }
        if (this.getCharacters().size() == 1) {
            if (Jasbro.getInstance().getData().getTime() == Time.MORNING) {
                message = TextUtil.t("sleepin.solo", this.getCharacters().get(0));
            }
            if (Jasbro.getInstance().getData().getTime() == Time.AFTERNOON) {
                message = TextUtil.t("nap.solo", this.getCharacters().get(0));
            }
            if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
                message = TextUtil.t("sleep.solo", this.getCharacters().get(0));
            }
        } else {
            if (Jasbro.getInstance().getData().getTime() == Time.MORNING) {
                message = TextUtil.t("sleepin.group", this.getCharacters());
            }
            if (Jasbro.getInstance().getData().getTime() == Time.AFTERNOON) {
                message = TextUtil.t("nap.group", this.getCharacters());
            }
            if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
                message = TextUtil.t("sleep.group", this.getCharacters());
            }
        }
        this.messageData.addToMessage(message);
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 40.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.HEALTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.15f, EssentialAttributes.MOTIVATION));
        return modifications;
    }

    @Override
    public void perform() {
        CleanState state;
        if (this.getHouse() != null && (state = CleanState.calcState(this.getHouse())) == CleanState.FILTHY && Util.getRnd().nextBoolean()) {
            Illness.Flu flu = new Illness.Flu(false);
            this.getCharacter().addCondition(flu);
            this.getMessages().get(0).setPriorityMessage(true);
            this.getMessages().get(0).addToMessage("\n\n" + TextUtil.t("flu.startDirt", this.getCharacter()));
        }
    }
}

