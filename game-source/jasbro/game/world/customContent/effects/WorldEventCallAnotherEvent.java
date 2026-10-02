package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventCallAnotherEvent extends WorldEventEffect {
   private String eventId;

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      WorldEvent worldEvent2 = Jasbro.getInstance().getWorldEvents().get(this.eventId);
      worldEvent2.setAttributeMap(worldEvent.getAttributeMap());
      worldEvent2.setInterpreter(worldEvent.getInterpreter());
      worldEvent2.setProtectedCharacters(worldEvent.getProtectedCharacters());
      worldEvent.getTriggeredEvents().add(worldEvent2);
      worldEvent2.execute();
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.CALLANOTHEREVENT;
   }

   public String getEventId() {
      return this.eventId;
   }

   public void setEventId(String eventId) {
      this.eventId = eventId;
   }
}
