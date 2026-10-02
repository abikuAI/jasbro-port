/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.world.customContent;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.Trigger;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.texts.TextUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomQuestStage
extends QuestStage
implements Serializable {
    private static final Logger log = LogManager.getLogger(CustomQuestStage.class);
    private String title;
    private String description;
    private boolean showInQuestLog;
    private List<Trigger> triggers;
    private Map<Trigger, String> triggerToWorldEventMap = new HashMap<Trigger, String>();

    @Override
    public void handleEvent(MyEvent e, Quest questTmp) {
        if (this.getTriggers().size() > 0) {
            CustomQuest quest = (CustomQuest)questTmp;
            quest.putAttribute(WorldEvent.WorldEventVariables.event, (Object)e);
            try {
                if (e.getSource() instanceof RunningActivity) {
                    RunningActivity activity = (RunningActivity)e.getSource();
                    quest.putAttribute(WorldEvent.WorldEventVariables.activity, (Object)activity);
                    quest.putAttribute(WorldEvent.WorldEventVariables.activitytype, (Object)activity.getType());
                    quest.putAttribute(WorldEvent.WorldEventVariables.character, (Object)activity.getCharacters().get(0));
                    quest.putAttribute(WorldEvent.WorldEventVariables.characters, activity.getCharacters());
                } else if (e.getSource() instanceof Charakter) {
                    quest.putAttribute(WorldEvent.WorldEventVariables.character, (Object)((Charakter)e.getSource()));
                }
                for (Trigger trigger : this.getTriggers()) {
                    if (!trigger.isTriggered(quest)) continue;
                    WorldEvent worldEvent = Jasbro.getInstance().getWorldEvents().get(this.triggerToWorldEventMap.get(trigger));
                    worldEvent.setAttributeMap(quest.getAttributeMap());
                    if (quest.isInterpreterInitialized()) {
                        worldEvent.setInterpreter(quest.getInterpreter());
                    }
                    quest.getTriggeredEvents().add(worldEvent);
                    worldEvent.execute();
                    break;
                }
            }
            catch (EvalError ex) {
                log.error("Error, while evaluating custom code", (Throwable)ex);
            }
        }
    }

    public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, Util.TypeAmounts typeAmounts, CharacterLocation characterLocation, Quest questTmp) {
        if (this.getTriggers().size() > 0) {
            CustomQuest quest = (CustomQuest)questTmp;
            quest.putAttribute(WorldEvent.WorldEventVariables.activitytype, (Object)ActivityType.CUSTOMEVENT);
            quest.putAttribute(WorldEvent.WorldEventVariables.typeamounts, (Object)typeAmounts);
            quest.putAttribute(WorldEvent.WorldEventVariables.location, (Object)characterLocation.getLocationType());
            if (characters != null) {
                quest.putAttribute(WorldEvent.WorldEventVariables.characters, characters);
                if (characters.size() > 0) {
                    quest.putAttribute(WorldEvent.WorldEventVariables.character, (Object)characters.get(0));
                }
            }
            for (Trigger trigger : this.getTriggers()) {
                try {
                    if (!trigger.isTriggered(quest)) continue;
                    WorldEvent worldEvent = Jasbro.getInstance().getWorldEvents().get(this.triggerToWorldEventMap.get(trigger));
                    ActivityDetails activityDetail = new ActivityDetails(ActivityType.CUSTOMEVENT, quest, worldEvent.getId(), this.customize(trigger.getActivityLabel(), quest), this.customize(trigger.getActivityDescription(), quest));
                    activityDetails.add(activityDetail);
                }
                catch (EvalError ex) {
                    log.error("Failed to execute trigger, skipping", (Throwable)ex);
                }
            }
        }
    }

    public List<Trigger> getTriggers() {
        if (this.triggers == null) {
            this.triggers = new ArrayList<Trigger>();
        }
        return this.triggers;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowInQuestLog() {
        return this.showInQuestLog;
    }

    public void setShowInQuestLog(boolean showInQuestLog) {
        this.showInQuestLog = showInQuestLog;
    }

    public Map<Trigger, String> getTriggerToWorldEventMap() {
        return this.triggerToWorldEventMap;
    }

    public String getEventName(Trigger trigger) {
        if (this.triggerToWorldEventMap.containsKey(trigger)) {
            return this.getTriggerToWorldEventMap().get(trigger);
        }
        return null;
    }

    public void removeTrigger(Trigger trigger) {
        if (this.triggerToWorldEventMap.containsKey(trigger)) {
            this.triggerToWorldEventMap.remove(trigger);
        }
        this.triggers.remove(trigger);
    }

    @Override
    public String getTitle(Quest quest) {
        return this.customize(this.getTitle(), quest);
    }

    @Override
    public String getDescription(Quest quest) {
        return this.customize(this.getDescription(), quest);
    }

    public String customize(String text, Quest quest) {
        try {
            List people = null;
            if (quest.getVariables().containsKey((Object)WorldEvent.WorldEventVariables.characters)) {
                people = (List)quest.getVariables().get((Object)WorldEvent.WorldEventVariables.characters);
            }
            return TextUtil.getInstance().applyTemplates(text, people, quest.getVariables());
        }
        catch (Exception e) {
            log.error("Error while customizing quest: ", (Throwable)e);
            return text;
        }
    }
}

