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
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TriggerParent {
   void putAttribute(WorldEvent.WorldEventVariables var1, Object var2);

   Interpreter getInterpreter() throws EvalError;

   boolean isInterpreterInitialized();

   Map<String, Object> generateAttributeMap() throws EvalError;

   Object getAttribute(String var1) throws EvalError;

   RunningActivity getActivity() throws EvalError;

   Charakter getCharacter() throws EvalError;

   List<Charakter> getCharacters() throws EvalError;

   Util.TypeAmounts getTypeAmounts() throws EvalError;

   ActivityType getActivityType() throws EvalError;

   CustomQuest getQuest() throws EvalError;

   MyEvent getEvent() throws EvalError;

   List<Person> getPeople() throws EvalError;

   LocationTypeInterface getLocation() throws EvalError;

   void reset(boolean var1);

   void reset();

   void modifyActivities(List<ActivityDetails> var1, Time var2, List<Charakter> var3, Util.TypeAmounts var4, CharacterLocation var5) throws EvalError;

   Set<WorldEvent> getTriggeredEvents();
}
