/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.quests.Quest;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public class Questtimer
extends Condition {
    private int remainingTime;
    private Quest quest;

    public Questtimer(int remainingTime, Quest quest) {
        this.remainingTime = remainingTime;
        this.quest = quest;
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY) {
            --this.remainingTime;
            if (this.remainingTime <= 0) {
                this.getCharacter().getConditions().remove(this);
            } else if (!Jasbro.getInstance().getData().getQuestManager().getActiveQuests().contains(this.quest)) {
                this.getCharacter().getConditions().remove(this);
            }
        }
        super.handleEvent(e);
    }

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return TextUtil.t("conditions.questtimer");
    }

    @Override
    public String getDescription() {
        Object[] arguments = new Object[]{this.remainingTime, this.quest.getTitle()};
        return TextUtil.t("conditions.questtimer.description", arguments) + "\n" + this.quest.getDescription();
    }

    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/clock.png");
    }
}

