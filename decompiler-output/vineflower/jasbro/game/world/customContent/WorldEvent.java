package jasbro.game.world.customContent;

import bsh.EvalError;
import bsh.Interpreter;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.Person;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.java.truevfs.access.TFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEvent implements TriggerParent, Serializable {
   private static final Logger log = LogManager.getLogger(WorldEvent.class);
   private String id;
   private List<Trigger> triggers;
   private List<WorldEventEffect> effects;
   private transient Interpreter interpreter;
   private transient Map<String, Object> attributeMap;
   private transient TFile file;
   private transient Set<WorldEvent> triggeredEvents;
   private transient Set<Charakter> protectedCharacters;
   private transient boolean inProgress = false;

   public WorldEvent(String id) {
      this.id = id;
      this.getEffects().add(new WorldEventEffectContainer());
   }

   public void handleEvent(MyEvent e) {
      if (!this.inProgress && this.getTriggers().size() > 0) {
         this.putAttribute(WorldEvent.WorldEventVariables.worldEventInstance, this);
         this.putAttribute(WorldEvent.WorldEventVariables.event, e);
         if (e.getSource() instanceof RunningActivity) {
            RunningActivity activity = (RunningActivity)e.getSource();
            this.putAttribute(WorldEvent.WorldEventVariables.activity, activity);
            this.putAttribute(WorldEvent.WorldEventVariables.activitytype, activity.getType());
            this.putAttribute(WorldEvent.WorldEventVariables.character, activity.getCharacters().get(0));
            this.putAttribute(WorldEvent.WorldEventVariables.characters, activity.getCharacters());
         } else if (e.getSource() instanceof Charakter) {
            this.putAttribute(WorldEvent.WorldEventVariables.character, (Charakter)e.getSource());
         }

         for (Trigger trigger : this.getTriggers()) {
            try {
               if (trigger.isTriggered(this)) {
                  this.execute();
                  break;
               }
            } catch (EvalError ex) {
               log.error("Failed to execute trigger, skipping", ex);
            }
         }
      }

      if (e.getType() == EventType.HEALTHZERO && this.getProtectedCharacters().contains(((AttributeChangedEvent)e).getAttribute().getCharacter())) {
         e.setCancelled(true);
      }
   }

   @Override
   public void modifyActivities(
      List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, Util.TypeAmounts typeAmounts, CharacterLocation characterLocation
   ) {
      if (this.getTriggers().size() > 0) {
         this.putAttribute(WorldEvent.WorldEventVariables.worldEventInstance, this);
         this.putAttribute(WorldEvent.WorldEventVariables.activitytype, ActivityType.CUSTOMEVENT);
         this.putAttribute(WorldEvent.WorldEventVariables.typeamounts, typeAmounts);
         this.putAttribute(WorldEvent.WorldEventVariables.location, characterLocation.getLocationType());
         if (characters != null) {
            this.putAttribute(WorldEvent.WorldEventVariables.characters, characters);
            if (characters.size() > 0) {
               this.putAttribute(WorldEvent.WorldEventVariables.character, characters.get(0));
            }
         }

         for (Trigger trigger : this.getTriggers()) {
            try {
               if (trigger.isTriggered(this)) {
                  ActivityDetails activityDetail = new ActivityDetails(
                     ActivityType.CUSTOMEVENT, this.getId(), trigger.getActivityLabel(), trigger.getActivityDescription()
                  );
                  activityDetails.add(activityDetail);
               }
            } catch (EvalError ex) {
               log.error("Failed to execute trigger, skipping", ex);
            }
         }
      }
   }

   public void execute(Interpreter interpreter) {
      this.interpreter = interpreter;
      this.execute();
   }

   public void execute() {
      if (!this.inProgress) {
         try {
            this.inProgress = true;
            this.putAttribute(WorldEvent.WorldEventVariables.worldEventInstance, this);

            for (WorldEventEffect effect : this.effects) {
               effect.perform(this);
            }
         } catch (EvalError ex) {
            log.error("Error executing world event effect", ex);
         } finally {
            this.inProgress = false;
         }
      }
   }

   public List<Trigger> getTriggers() {
      if (this.triggers == null) {
         this.triggers = new ArrayList<>();
      }

      return this.triggers;
   }

   public List<WorldEventEffect> getEffects() {
      if (this.effects == null) {
         this.effects = new ArrayList<>();
      }

      return this.effects;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   @Override
   public Interpreter getInterpreter() throws EvalError {
      if (this.interpreter == null) {
         this.interpreter = Jasbro.getInstance().getInterpreter();

         for (Entry<String, Object> entry : this.getAttributeMap().entrySet()) {
            try {
               this.interpreter.set(entry.getKey(), entry.getValue());
            } catch (EvalError e) {
               log.error("Eval error", e);
            }
         }

         if (this.getQuest() != null) {
            this.getQuest().setInterpreter(this.interpreter);
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
         this.protectedCharacters = null;
         this.triggeredEvents = null;
         if (this.interpreter != null && resetInterpreter) {
            Jasbro.getInstance().cleanupInterpreter();
         }

         this.interpreter = null;
      }
   }

   public TFile getFile() {
      return this.file;
   }

   public void setFile(TFile file) {
      this.file = file;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      return 31 * result + (this.id == null ? 0 : this.id.hashCode());
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj == null) {
         return false;
      }

      if (this.getClass() != obj.getClass()) {
         return false;
      }

      WorldEvent other = (WorldEvent)obj;
      if (this.id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!this.id.equals(other.id)) {
         return false;
      }

      return true;
   }

   @Override
   public String toString() {
      return this.getId();
   }

   public Map<String, Object> getAttributeMap() {
      if (this.attributeMap == null) {
         this.attributeMap = new HashMap<>();
         this.attributeMap.put("data", Jasbro.getInstance().getData());
      }

      return this.attributeMap;
   }

   public void setAttributeMap(Map<String, Object> attributeMap) {
      this.attributeMap = attributeMap;
   }

   public void setInterpreter(Interpreter interpreter) {
      this.interpreter = interpreter;
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
   public boolean isInterpreterInitialized() {
      return this.interpreter != null;
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
   public CustomQuest getQuest() throws EvalError {
      return (CustomQuest)this.getAttribute(WorldEvent.WorldEventVariables.questInstance);
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
      List<Charakter> characters = this.getCharacters();
      if (characters != null) {
         people.addAll(this.getCharacters());
      }

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

   @Override
   public Set<WorldEvent> getTriggeredEvents() {
      if (this.triggeredEvents == null) {
         this.triggeredEvents = new HashSet<>();
      }

      return this.triggeredEvents;
   }

   public Set<Charakter> getProtectedCharacters() {
      if (this.protectedCharacters == null) {
         this.protectedCharacters = new HashSet<>();
      }

      return this.protectedCharacters;
   }

   public void setTriggeredEvents(Set<WorldEvent> effects) {
      this.triggeredEvents = effects;
   }

   public void setProtectedCharacters(Set<Charakter> protectedCharacters) {
      this.protectedCharacters = protectedCharacters;
   }

   public enum WorldEventVariables {
      data,
      activity,
      questInstance,
      event,
      character,
      characters,
      people,
      typeamounts,
      activitytype,
      attributemodifications,
      location,
      worldEventInstance;
   }
}
