/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 *  bsh.Interpreter
 */
package jasbro.game.world.customContent;

import bsh.EvalError;
import bsh.Interpreter;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.Person;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.WorldEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TriggerParent {
    public void putAttribute(WorldEvent.WorldEventVariables var1, Object var2);

    public Interpreter getInterpreter() throws EvalError;

    public boolean isInterpreterInitialized();

    public Map<String, Object> generateAttributeMap() throws EvalError;

    public Object getAttribute(String var1) throws EvalError;

    public RunningActivity getActivity() throws EvalError;

    public Charakter getCharacter() throws EvalError;

    public List<Charakter> getCharacters() throws EvalError;

    public Util.TypeAmounts getTypeAmounts() throws EvalError;

    public ActivityType getActivityType() throws EvalError;

    public CustomQuest getQuest() throws EvalError;

    public MyEvent getEvent() throws EvalError;

    public List<Person> getPeople() throws EvalError;

    public LocationTypeInterface getLocation() throws EvalError;

    public void reset(boolean var1);

    public void reset();

    public void modifyActivities(List<ActivityDetails> var1, Time var2, List<Charakter> var3, Util.TypeAmounts var4, CharacterLocation var5) throws EvalError;

    public Set<WorldEvent> getTriggeredEvents();
}

