package jasbro.game.events.rooms;

import jasbro.game.events.MyEvent;
import jasbro.game.housing.ConfigurableRoom;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomInfo;

public class Crypt extends ConfigurableRoom implements RoomEventHandler {
   private String demonName = "nobody";
   private int ritualAdvancment = 0;

   public Crypt(RoomInfo roomInfo) {
      super(roomInfo);
   }

   public Crypt(RoomInfo roomInfo, House house) {
      super(roomInfo, house);
   }

   @Override
   public String getName() {
      return super.getName() + " Worshipping " + this.demonName + " (Advancment:" + this.ritualAdvancment + "/13)";
   }

   @Override
   public void handleEvent(MyEvent e) {
      super.handleEvent(e);
   }

   public String getDemonName() {
      return this.demonName;
   }

   public void setDemonName(String demonName) {
      this.demonName = demonName;
   }

   public int getRitualAdvancment() {
      return this.ritualAdvancment;
   }

   public void setRitualAdvancment(int ritualAdvancment) {
      this.ritualAdvancment = ritualAdvancment;
   }
}
