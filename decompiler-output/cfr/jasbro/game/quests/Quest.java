/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.quests.QuestStage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Quest
implements MyEventListener,
Serializable {
    private Map<String, Object> variables;
    private int currentStage = 0;
    private transient List<QuestStage> stages;

    public abstract List<QuestStage> getInitStages();

    public void init() {
        if (this.getCurrentStage() != null) {
            this.getCurrentStage().init(this);
        }
    }

    public List<QuestStage> getStages() {
        if (this.stages == null) {
            this.stages = this.getInitStages();
        }
        return this.stages;
    }

    public void setStages(List<QuestStage> stages) {
        this.stages = stages;
    }

    public QuestStage getCurrentStage() {
        return this.getStages().get(this.currentStage);
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public void setResolved() {
        Jasbro.getInstance().getData().getQuestManager().setSolved(this);
    }

    public void setActive() {
        Jasbro.getInstance().getData().getQuestManager().activateQuest(this);
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (this.getCurrentStage() != null) {
            this.getCurrentStage().handleEvent(e, this);
        }
    }

    public String getTitle() {
        return this.getCurrentStage().getTitle(this);
    }

    public String getDescription() {
        return this.getCurrentStage().getDescription(this);
    }

    public boolean canFinishEarly() {
        return this.getCurrentStage().canFinishEarly(this);
    }

    public void finish() {
        this.getCurrentStage().finish(this);
    }

    public Map<String, Object> getVariables() {
        if (this.variables == null) {
            this.variables = new HashMap<String, Object>();
        }
        return this.variables;
    }

    public void setVariable(String key, Object value) {
        this.getVariables().put(key, value);
    }

    public Object getVariable(String key) {
        if (this.getVariables().containsKey(key)) {
            return this.getVariables().get(key);
        }
        return null;
    }

    public boolean showInQuestLog() {
        return true;
    }

    public int getCurrentStageNumber() {
        return this.currentStage;
    }
}

