package jasbro.game.events.rooms;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;

public class SexSatisfactionEventHandler extends AbstractRoomEventHandler {
   private final Sextype bonusType;
   private final int bonusAmount;

   public SexSatisfactionEventHandler(EventType handledType, Sextype bonusType, int bonusAmount) {
      this.bonusType = bonusType;
      this.bonusAmount = bonusAmount;
      this.setHandledType(handledType);
   }

   @Override
   protected void handleEventInternal(MyEvent event) {
      RunningActivity a = (RunningActivity)event.getSource();

      for (Customer c : a.getMainCustomers()) {
         if (c.getPreferredSextype() == this.bonusType) {
            c.addToSatisfaction(this.bonusAmount, this);
         }
      }
   }
}
