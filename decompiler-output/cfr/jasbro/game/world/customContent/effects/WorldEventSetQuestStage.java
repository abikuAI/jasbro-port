/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventSetQuestStage
extends WorldEventEffect {
    private int questStage = 0;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        CustomQuest quest = worldEvent.getQuest();
        quest.setCurrentStage(this.questStage);
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.SETQUESTSTAGE;
    }

    public Integer getQuestStage() {
        return this.questStage;
    }

    public void setQuestStage(Integer questStage) {
        this.questStage = questStage;
    }
}

