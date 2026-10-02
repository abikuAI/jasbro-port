package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.quests.BetTrainAmountQuest;
import jasbro.game.quests.Quest;
import jasbro.game.quests.SellSlaveQuest;
import jasbro.game.quests.StandardSlaveQuest;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.game.world.customContent.WorldEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestManager implements CentralEventlistener {
   private static final Logger log = LogManager.getLogger(QuestManager.class);
   private List<Quest> activeQuests = new ArrayList<>();
   private List<Quest> possibleQuests = null;
   private transient List<Quest> inactiveQuests;
   private Map<String, Integer> solvedQuests;
   private int difficultyModifier = 0;

   public QuestManager() {
      Jasbro.getInstance().getData().getEventManager().addListener(this);
   }

   public List<Quest> getPossibleQuests() {
      if (this.possibleQuests == null) {
         this.possibleQuests = new ArrayList<>();

         for (int i = 0; i < Math.min(10, Math.max(4, (int)Math.sqrt(Jasbro.getInstance().getData().getProtagonist().getFame().getFame()) / 40)); i++) {
            this.possibleQuests.add(this.generateQuest());
         }
      }

      return this.possibleQuests;
   }

   public void activateQuest(Quest quest) {
      this.getActiveQuests().add(quest);
      this.difficultyModifier += 2;
      quest.init();
      this.getPossibleQuests().remove(quest);
      this.getInactiveQuests().remove(quest);
   }

   public Quest generateQuest() {
      return Util.getRnd().nextInt(100) < 5 ? SellSlaveQuest.generate(this.difficultyModifier) : StandardSlaveQuest.generate(this.difficultyModifier);
   }

   public List<Quest> getActiveQuests() {
      return this.activeQuests;
   }

   public int getDifficultyModifier() {
      return this.difficultyModifier;
   }

   @Override
   public void handleCentralEvent(MyEvent e) {
      if (e.getType().isCustomContentRelevant()) {
         try {
            if (e.getType() == EventType.NEXTDAY) {
               this.possibleQuests = null;
               this.difficultyModifier++;
               this.getPossibleQuests();
            }

            List<Quest> inactiveQuests = new ArrayList<>();
            inactiveQuests.addAll(this.getInactiveQuests());
            List<Quest> activeQuests = new ArrayList<>();
            activeQuests.addAll(this.getActiveQuests());

            for (int i = 0; i < inactiveQuests.size(); i++) {
               Quest quest = null;

               try {
                  quest = inactiveQuests.get(i);
                  quest.handleEvent(e);
               } catch (Exception ex) {
                  log.error("Error in quest {}", new Object[]{quest.getTitle()});
                  log.throwing(ex);
                  this.getInactiveQuests().remove(quest);
               } finally {
                  if (quest != null && quest instanceof CustomQuest) {
                     ((CustomQuest)quest).reset();
                  }
               }
            }

            for (int i = 0; i < activeQuests.size(); i++) {
               Quest quest = null;

               try {
                  quest = activeQuests.get(i);
                  quest.handleEvent(e);
               } catch (Exception ex) {
                  log.error("Error in quest {}", new Object[]{quest.getTitle()});
                  log.throwing(ex);
                  this.getActiveQuests().remove(quest);
               } finally {
                  if (quest != null && quest instanceof CustomQuest) {
                     ((CustomQuest)quest).reset();
                  }
               }
            }

            for (WorldEvent event : Jasbro.getInstance().getWorldEvents().values()) {
               try {
                  event.handleEvent(e);
               } catch (Exception ex) {
                  log.error("Error in event {}", new Object[]{event.getId()});
                  log.throwing(ex);
                  Jasbro.getInstance().getWorldEvents().remove(event.getId());
               } finally {
                  event.reset();
               }
            }
         } catch (Exception ex) {
            log.error("Error while handling quests", ex);
         }
      }
   }

   public void setSolved(Quest quest) {
      this.activeQuests.remove(quest);
   }

   public void setSolved(Quest quest, int difficultyModifier) {
      this.activeQuests.remove(quest);
      this.addToModifier(difficultyModifier);
   }

   public void addToModifier(int mod) {
      this.difficultyModifier += mod;
   }

   public List<Quest> getInactiveQuests() {
      if (this.inactiveQuests == null) {
         this.inactiveQuests = new ArrayList<>();
         this.inactiveQuests.add(new BetTrainAmountQuest());

         for (CustomQuestTemplate questTemplate : Jasbro.getInstance().getCustomQuestTemplates().values()) {
            if (questTemplate.getQuestStages().size() > 0 && questTemplate.getQuestStages().get(0).getTriggers().size() > 0) {
               boolean questExists = false;

               for (String solvedQuest : this.getSolvedQuests().keySet()) {
                  if (solvedQuest.equals(questTemplate.getId())) {
                     questExists = true;
                     break;
                  }
               }

               if (!questExists) {
                  for (Quest quest : this.getActiveQuests()) {
                     if (quest instanceof CustomQuest && ((CustomQuest)quest).getTemplate().getId().equals(questTemplate.getId())) {
                        questExists = true;
                        break;
                     }
                  }
               }

               if (!questExists) {
                  this.inactiveQuests.add(new CustomQuest(questTemplate.getId()));
               }
            }
         }
      }

      return this.inactiveQuests;
   }

   public void setInactiveQuests(List<Quest> inactiveQuests) {
      this.inactiveQuests = inactiveQuests;
   }

   public Map<String, Integer> getSolvedQuests() {
      if (this.solvedQuests == null) {
         this.solvedQuests = new HashMap<>();
      }

      return this.solvedQuests;
   }

   public void modifyActivities(
      List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, Util.TypeAmounts typeAmounts, CharacterLocation characterLocation
   ) {
      try {
         List<Quest> inactiveQuests = new ArrayList<>();
         inactiveQuests.addAll(this.getInactiveQuests());
         List<Quest> activeQuests = new ArrayList<>();
         activeQuests.addAll(this.getActiveQuests());

         for (int i = 0; i < inactiveQuests.size(); i++) {
            Quest quest = null;

            try {
               quest = inactiveQuests.get(i);
               if (quest instanceof CustomQuest) {
                  ((CustomQuest)quest).modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation);
               }
            } catch (Exception ex) {
               log.error("Error in quest", ex);
               this.getInactiveQuests().remove(quest);
            } finally {
               if (quest != null && quest instanceof CustomQuest) {
                  ((CustomQuest)quest).reset();
               }
            }
         }

         for (int i = 0; i < activeQuests.size(); i++) {
            Quest quest = null;

            try {
               quest = activeQuests.get(i);
               if (quest instanceof CustomQuest) {
                  ((CustomQuest)quest).modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation);
               }
            } catch (Exception ex) {
               log.error("Error in quest", ex);
               this.getActiveQuests().remove(quest);
            } finally {
               if (quest != null && quest instanceof CustomQuest) {
                  ((CustomQuest)quest).reset();
               }
            }
         }

         for (WorldEvent event : Jasbro.getInstance().getWorldEvents().values()) {
            try {
               event.modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation);
            } catch (Exception ex) {
               log.error("Error in event", ex);
               Jasbro.getInstance().getWorldEvents().remove(event.getId());
            } finally {
               event.reset();
            }
         }
      } catch (Exception ex) {
         log.error("Error while handling quests", ex);
      }
   }
}
