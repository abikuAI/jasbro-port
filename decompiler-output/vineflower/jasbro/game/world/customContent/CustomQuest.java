package jasbro.game.world.customContent;

import bsh.EvalError;
import bsh.Interpreter;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.Person;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomQuest extends Quest implements TriggerParent {
   private static final Logger log = LogManager.getLogger(CustomQuest.class);
   private String customQuestId;
   private transient Interpreter interpreter;
   private transient Map<String, Object> attributeMap;
   private transient CustomQuestTemplate template;
   private transient Set<WorldEvent> triggeredEvents;
   private transient boolean inProgress = false;

   public CustomQuest(String customQuestId) {
      this.customQuestId = customQuestId;
   }

   @Override
   public List<QuestStage> getInitStages() {
      return new ArrayList<>(this.getTemplate().getQuestStages());
   }

   @Override
   public Interpreter getInterpreter() {
      if (this.interpreter == null) {
         this.interpreter = Jasbro.getInstance().getInterpreter();

         for (Entry<String, Object> entry : this.getAttributeMap().entrySet()) {
            try {
               this.interpreter.set(entry.getKey(), entry.getValue());
            } catch (EvalError e) {
               log.error("Eval error", e);
            }
         }
      }

      return this.interpreter;
   }

   @Override
   public void putAttribute(WorldEvent.WorldEventVariables key, Object object) {
      this.putAttribute(key.toString(), object);
   }

   public void putAttribute(String key, Object object) {
      this.getAttributeMap().put(key, object);
      if (this.interpreter != null) {
         try {
            this.interpreter.set(key, object);
         } catch (EvalError e) {
            log.error("Eval error", e);
         }
      }
   }

   public Map<String, Object> getAttributeMap() {
      if (this.attributeMap == null) {
         this.attributeMap = new HashMap<>();
         this.putAttribute(WorldEvent.WorldEventVariables.questInstance, this);
         this.putAttribute(WorldEvent.WorldEventVariables.data, Jasbro.getInstance().getData());
      }

      return this.attributeMap;
   }

   @Override
   public void reset() {
      this.reset(true);
   }

   @Override
   public void reset(boolean resetInterpreter) {
      if (!this.inProgress) {
         if (this.triggeredEvents != null) {
            for (TriggerParent subEffect : this.getTriggeredEvents()) {
               subEffect.reset(false);
            }
         }

         this.attributeMap = null;
         this.triggeredEvents = null;
         if (this.interpreter != null && resetInterpreter) {
            Jasbro.getInstance().cleanupInterpreter();
         }

         this.interpreter = null;
      }
   }

   public CustomQuestTemplate getTemplate() {
      if (this.template == null) {
         this.template = Jasbro.getInstance().getCustomQuestTemplates().get(this.customQuestId);
      }

      return this.template;
   }

   public void set(String attributeName, Object value) {
      this.getAttributeMap().put(attributeName, value);
   }

   public Object get(String attributeName) {
      return this.getAttributeMap().get(attributeName);
   }

   @Override
   public boolean showInQuestLog() {
      return ((CustomQuestStage)this.getCurrentStage()).isShowInQuestLog();
   }

   @Override
   public boolean isInterpreterInitialized() {
      if (this.interpreter != null) {
         return true;
      }

      for (TriggerParent subEffect : this.getTriggeredEvents()) {
         if (subEffect.isInterpreterInitialized()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public RunningActivity getActivity() throws EvalError {
      return (RunningActivity)this.getAttribute(WorldEvent.WorldEventVariables.activity);
   }

   @Override
   public MyEvent getEvent() throws EvalError {
      return (MyEvent)this.getAttribute(WorldEvent.WorldEventVariables.event);
   }

   @Override
   public List<Charakter> getCharacters() throws EvalError {
      return (List<Charakter>)this.getAttribute(WorldEvent.WorldEventVariables.characters);
   }

   @Override
   public List<Person> getPeople() throws EvalError {
      if (this.interpreter != null && this.interpreter.get(WorldEvent.WorldEventVariables.people.toString()) != null) {
         return (List<Person>)this.interpreter.get(WorldEvent.WorldEventVariables.people.toString());
      }

      if (this.getAttributeMap().containsKey(WorldEvent.WorldEventVariables.people.toString())) {
         return (List<Person>)this.getAttributeMap().get(WorldEvent.WorldEventVariables.people.toString());
      }

      List<Person> people = new ArrayList<>();
      people.addAll(this.getCharacters());
      this.putAttribute(WorldEvent.WorldEventVariables.people, people);
      return people;
   }

   @Override
   public Util.TypeAmounts getTypeAmounts() throws EvalError {
      if (this.interpreter != null && this.interpreter.get(WorldEvent.WorldEventVariables.typeamounts.toString()) != null) {
         return (Util.TypeAmounts)this.interpreter.get(WorldEvent.WorldEventVariables.typeamounts.toString());
      } else if (this.getAttributeMap().containsKey(WorldEvent.WorldEventVariables.typeamounts.toString())) {
         return (Util.TypeAmounts)this.getAttributeMap().get(WorldEvent.WorldEventVariables.typeamounts.toString());
      } else {
         List<Charakter> characters = this.getCharacters();
         if (characters != null) {
            Util.TypeAmounts typeAmounts = Util.getTypeAmounts(characters);
            this.getAttributeMap().put(WorldEvent.WorldEventVariables.typeamounts.toString(), typeAmounts);
            return typeAmounts;
         } else {
            return null;
         }
      }
   }

   @Override
   public ActivityType getActivityType() throws EvalError {
      return (ActivityType)this.getAttribute(WorldEvent.WorldEventVariables.activitytype);
   }

   @Override
   public Charakter getCharacter() throws EvalError {
      return (Charakter)this.getAttribute(WorldEvent.WorldEventVariables.character);
   }

   @Override
   public CustomQuest getQuest() {
      return this;
   }

   public Object getAttribute(WorldEvent.WorldEventVariables key) throws EvalError {
      return this.getAttribute(key.toString());
   }

   @Override
   public Object getAttribute(String key) throws EvalError {
      if (this.interpreter == null) {
         return this.getAttributeMap().containsKey(key) ? this.getAttributeMap().get(key) : null;
      } else {
         return this.interpreter.get(key);
      }
   }

   @Override
   public LocationTypeInterface getLocation() throws EvalError {
      LocationTypeInterface locationType = (LocationTypeInterface)this.getAttribute(WorldEvent.WorldEventVariables.location);
      if (locationType == null) {
         RunningActivity activity = this.getActivity();
         if (activity != null) {
            locationType = activity.getPlannedActivity().getSource().getLocationType();
            this.putAttribute(WorldEvent.WorldEventVariables.location, locationType);
         }
      }

      return locationType;
   }

   @Override
   public void modifyActivities(
      List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, Util.TypeAmounts typeAmounts, CharacterLocation characterLocation
   ) throws EvalError {
      ((CustomQuestStage)this.getCurrentStage()).modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation, this);
   }

   @Override
   public Map<String, Object> generateAttributeMap() throws EvalError {
      if (this.interpreter == null) {
         return this.getAttributeMap();
      }

      Map<String, Object> attributeMap = new HashMap<>();

      for (String variable : (String[])this.getInterpreter().eval("return this.variables;")) {
         attributeMap.put(variable, this.getInterpreter().get(variable));
      }

      return attributeMap;
   }

   public void setInterpreter(Interpreter interpreter) {
      this.interpreter = interpreter;
   }

   @Override
   public Set<WorldEvent> getTriggeredEvents() {
      if (this.triggeredEvents == null) {
         this.triggeredEvents = new HashSet<>();
      }

      return this.triggeredEvents;
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (!this.inProgress && this.getCurrentStage() != null) {
         try {
            this.inProgress = true;
            this.getCurrentStage().handleEvent(e, this);
         } finally {
            this.inProgress = false;
         }
      }
   }
}
