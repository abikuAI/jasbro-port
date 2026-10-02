package jasbro.game.world.customContent;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.texts.TextUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomQuestStage extends QuestStage implements Serializable {
   private static final Logger log = LogManager.getLogger(CustomQuestStage.class);
   private String title;
   private String description;
   private boolean showInQuestLog;
   private List<Trigger> triggers;
   private Map<Trigger, String> triggerToWorldEventMap = new HashMap<>();

   @Override
   public void handleEvent(MyEvent e, Quest questTmp) {
      if (this.getTriggers().size() > 0) {
         CustomQuest quest = (CustomQuest)questTmp;
         quest.putAttribute(WorldEvent.WorldEventVariables.event, e);

         try {
            if (e.getSource() instanceof RunningActivity) {
               RunningActivity activity = (RunningActivity)e.getSource();
               quest.putAttribute(WorldEvent.WorldEventVariables.activity, activity);
               quest.putAttribute(WorldEvent.WorldEventVariables.activitytype, activity.getType());
               quest.putAttribute(WorldEvent.WorldEventVariables.character, activity.getCharacters().get(0));
               quest.putAttribute(WorldEvent.WorldEventVariables.characters, activity.getCharacters());
            } else if (e.getSource() instanceof Charakter) {
               quest.putAttribute(WorldEvent.WorldEventVariables.character, (Charakter)e.getSource());
            }

            for (Trigger trigger : this.getTriggers()) {
               if (trigger.isTriggered(quest)) {
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
         } catch (EvalError ex) {
            log.error("Error, while evaluating custom code", ex);
         }
      }
   }

   public void modifyActivities(
      List<ActivityDetails> activityDetails,
      Time time,
      List<Charakter> characters,
      Util.TypeAmounts typeAmounts,
      CharacterLocation characterLocation,
      Quest questTmp
   ) {
      if (this.getTriggers().size() > 0) {
         CustomQuest quest = (CustomQuest)questTmp;
         quest.putAttribute(WorldEvent.WorldEventVariables.activitytype, ActivityType.CUSTOMEVENT);
         quest.putAttribute(WorldEvent.WorldEventVariables.typeamounts, typeAmounts);
         quest.putAttribute(WorldEvent.WorldEventVariables.location, characterLocation.getLocationType());
         if (characters != null) {
            quest.putAttribute(WorldEvent.WorldEventVariables.characters, characters);
            if (characters.size() > 0) {
               quest.putAttribute(WorldEvent.WorldEventVariables.character, characters.get(0));
            }
         }

         for (Trigger trigger : this.getTriggers()) {
            try {
               if (trigger.isTriggered(quest)) {
                  WorldEvent worldEvent = Jasbro.getInstance().getWorldEvents().get(this.triggerToWorldEventMap.get(trigger));
                  ActivityDetails activityDetail = new ActivityDetails(
                     ActivityType.CUSTOMEVENT,
                     quest,
                     worldEvent.getId(),
                     this.customize(trigger.getActivityLabel(), quest),
                     this.customize(trigger.getActivityDescription(), quest)
                  );
                  activityDetails.add(activityDetail);
               }
            } catch (EvalError ex) {
               log.error("Failed to execute trigger, skipping", ex);
            }
         }
      }
   }

   public List<Trigger> getTriggers() {
      if (this.triggers == null) {
         this.triggers = new ArrayList<>();
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
      return this.triggerToWorldEventMap.containsKey(trigger) ? this.getTriggerToWorldEventMap().get(trigger) : null;
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
         List<? extends Person> people = null;
         if (quest.getVariables().containsKey(WorldEvent.WorldEventVariables.characters)) {
            people = (List<? extends Person>)quest.getVariables().get(WorldEvent.WorldEventVariables.characters);
         }

         return TextUtil.getInstance().applyTemplates(text, people, quest.getVariables());
      } catch (Exception e) {
         log.error("Error while customizing quest: ", e);
         return text;
      }
   }
}
