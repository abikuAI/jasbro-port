/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 *  bsh.Interpreter
 *  net.java.truevfs.access.TFile
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
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
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.Trigger;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.java.truevfs.access.TFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEvent
implements TriggerParent,
Serializable {
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
            this.putAttribute(WorldEventVariables.worldEventInstance, (Object)this);
            this.putAttribute(WorldEventVariables.event, (Object)e);
            if (e.getSource() instanceof RunningActivity) {
                RunningActivity activity = (RunningActivity)e.getSource();
                this.putAttribute(WorldEventVariables.activity, (Object)activity);
                this.putAttribute(WorldEventVariables.activitytype, (Object)activity.getType());
                this.putAttribute(WorldEventVariables.character, (Object)activity.getCharacters().get(0));
                this.putAttribute(WorldEventVariables.characters, activity.getCharacters());
            } else if (e.getSource() instanceof Charakter) {
                this.putAttribute(WorldEventVariables.character, (Object)((Charakter)e.getSource()));
            }
            for (Trigger trigger : this.getTriggers()) {
                try {
                    if (!trigger.isTriggered(this)) continue;
                    this.execute();
                    break;
                }
                catch (EvalError ex) {
                    log.error("Failed to execute trigger, skipping", (Throwable)ex);
                }
            }
        }
        if (e.getType() == EventType.HEALTHZERO && this.getProtectedCharacters().contains(((AttributeChangedEvent)e).getAttribute().getCharacter())) {
            e.setCancelled(true);
        }
    }

    @Override
    public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, Util.TypeAmounts typeAmounts, CharacterLocation characterLocation) {
        if (this.getTriggers().size() > 0) {
            this.putAttribute(WorldEventVariables.worldEventInstance, (Object)this);
            this.putAttribute(WorldEventVariables.activitytype, (Object)ActivityType.CUSTOMEVENT);
            this.putAttribute(WorldEventVariables.typeamounts, (Object)typeAmounts);
            this.putAttribute(WorldEventVariables.location, (Object)characterLocation.getLocationType());
            if (characters != null) {
                this.putAttribute(WorldEventVariables.characters, characters);
                if (characters.size() > 0) {
                    this.putAttribute(WorldEventVariables.character, (Object)characters.get(0));
                }
            }
            for (Trigger trigger : this.getTriggers()) {
                try {
                    if (!trigger.isTriggered(this)) continue;
                    ActivityDetails activityDetail = new ActivityDetails(ActivityType.CUSTOMEVENT, this.getId(), trigger.getActivityLabel(), trigger.getActivityDescription());
                    activityDetails.add(activityDetail);
                }
                catch (EvalError ex) {
                    log.error("Failed to execute trigger, skipping", (Throwable)ex);
                }
            }
        }
    }

    public void execute(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.execute();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void execute() {
        if (!this.inProgress) {
            try {
                this.inProgress = true;
                this.putAttribute(WorldEventVariables.worldEventInstance, (Object)this);
                for (WorldEventEffect effect : this.effects) {
                    effect.perform(this);
                }
            }
            catch (EvalError ex) {
                log.error("Error executing world event effect", (Throwable)ex);
            }
            finally {
                this.inProgress = false;
            }
        }
    }

    public List<Trigger> getTriggers() {
        if (this.triggers == null) {
            this.triggers = new ArrayList<Trigger>();
        }
        return this.triggers;
    }

    public List<WorldEventEffect> getEffects() {
        if (this.effects == null) {
            this.effects = new ArrayList<WorldEventEffect>();
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
            for (Map.Entry<String, Object> entry : this.getAttributeMap().entrySet()) {
                try {
                    this.interpreter.set(entry.getKey(), entry.getValue());
                }
                catch (EvalError e) {
                    log.error("Eval error", (Throwable)e);
                }
            }
            if (this.getQuest() != null) {
                this.getQuest().setInterpreter(this.interpreter);
            }
        }
        return this.interpreter;
    }

    @Override
    public void putAttribute(WorldEventVariables key, Object object) {
        this.putAttribute(key.toString(), object);
    }

    public void putAttribute(String key, Object object) {
        this.getAttributeMap().put(key, object);
        if (this.interpreter != null) {
            try {
                this.interpreter.set(key, object);
            }
            catch (EvalError e) {
                log.error("Eval error", (Throwable)e);
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
                for (TriggerParent triggerParent : this.getTriggeredEvents()) {
                    triggerParent.reset(false);
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

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

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
        return !(this.id == null ? other.id != null : !this.id.equals(other.id));
    }

    public String toString() {
        return this.getId();
    }

    public Map<String, Object> getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new HashMap<String, Object>();
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

    public Object getAttribute(WorldEventVariables key) throws EvalError {
        return this.getAttribute(key.toString());
    }

    @Override
    public Object getAttribute(String key) throws EvalError {
        if (this.interpreter == null) {
            if (this.getAttributeMap().containsKey(key)) {
                return this.getAttributeMap().get(key);
            }
            return null;
        }
        return this.interpreter.get(key);
    }

    @Override
    public boolean isInterpreterInitialized() {
        return this.interpreter != null;
    }

    @Override
    public RunningActivity getActivity() throws EvalError {
        return (RunningActivity)this.getAttribute(WorldEventVariables.activity);
    }

    @Override
    public MyEvent getEvent() throws EvalError {
        return (MyEvent)this.getAttribute(WorldEventVariables.event);
    }

    @Override
    public CustomQuest getQuest() throws EvalError {
        return (CustomQuest)this.getAttribute(WorldEventVariables.questInstance);
    }

    @Override
    public List<Charakter> getCharacters() throws EvalError {
        return (List)this.getAttribute(WorldEventVariables.characters);
    }

    @Override
    public List<Person> getPeople() throws EvalError {
        if (this.interpreter == null || this.interpreter.get(WorldEventVariables.people.toString()) == null) {
            if (this.getAttributeMap().containsKey(WorldEventVariables.people.toString())) {
                return (List)this.getAttributeMap().get(WorldEventVariables.people.toString());
            }
            ArrayList<Person> people = new ArrayList<Person>();
            List<Charakter> characters = this.getCharacters();
            if (characters != null) {
                people.addAll(this.getCharacters());
            }
            this.putAttribute(WorldEventVariables.people, people);
            return people;
        }
        return (List)this.interpreter.get(WorldEventVariables.people.toString());
    }

    @Override
    public Util.TypeAmounts getTypeAmounts() throws EvalError {
        if (this.interpreter == null || this.interpreter.get(WorldEventVariables.typeamounts.toString()) == null) {
            if (this.getAttributeMap().containsKey(WorldEventVariables.typeamounts.toString())) {
                return (Util.TypeAmounts)this.getAttributeMap().get(WorldEventVariables.typeamounts.toString());
            }
            List<Charakter> characters = this.getCharacters();
            if (characters != null) {
                Util.TypeAmounts typeAmounts = Util.getTypeAmounts(characters);
                this.getAttributeMap().put(WorldEventVariables.typeamounts.toString(), typeAmounts);
                return typeAmounts;
            }
            return null;
        }
        return (Util.TypeAmounts)this.interpreter.get(WorldEventVariables.typeamounts.toString());
    }

    @Override
    public ActivityType getActivityType() throws EvalError {
        return (ActivityType)((Object)this.getAttribute(WorldEventVariables.activitytype));
    }

    @Override
    public Charakter getCharacter() throws EvalError {
        return (Charakter)this.getAttribute(WorldEventVariables.character);
    }

    @Override
    public LocationTypeInterface getLocation() throws EvalError {
        RunningActivity activity;
        LocationTypeInterface locationType = (LocationTypeInterface)this.getAttribute(WorldEventVariables.location);
        if (locationType == null && (activity = this.getActivity()) != null) {
            locationType = activity.getPlannedActivity().getSource().getLocationType();
            this.putAttribute(WorldEventVariables.location, (Object)locationType);
        }
        return locationType;
    }

    @Override
    public Map<String, Object> generateAttributeMap() throws EvalError {
        if (this.interpreter == null) {
            return this.getAttributeMap();
        }
        HashMap<String, Object> attributeMap = new HashMap<String, Object>();
        for (String variable : (String[])this.getInterpreter().eval("return this.variables;")) {
            attributeMap.put(variable, this.getInterpreter().get(variable));
        }
        return attributeMap;
    }

    @Override
    public Set<WorldEvent> getTriggeredEvents() {
        if (this.triggeredEvents == null) {
            this.triggeredEvents = new HashSet<WorldEvent>();
        }
        return this.triggeredEvents;
    }

    public Set<Charakter> getProtectedCharacters() {
        if (this.protectedCharacters == null) {
            this.protectedCharacters = new HashSet<Charakter>();
        }
        return this.protectedCharacters;
    }

    public void setTriggeredEvents(Set<WorldEvent> effects) {
        this.triggeredEvents = effects;
    }

    public void setProtectedCharacters(Set<Charakter> protectedCharacters) {
        this.protectedCharacters = protectedCharacters;
    }

    public static enum WorldEventVariables {
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

