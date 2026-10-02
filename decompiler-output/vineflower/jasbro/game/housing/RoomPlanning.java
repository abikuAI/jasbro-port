package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.events.rooms.Garden;
import java.util.ArrayList;
import java.util.List;

public class RoomPlanning {
   private House house;
   private List<RoomInfo> newRooms;

   public RoomPlanning(House house) {
      this.house = house;
      this.newRooms = this.getRoomInfoList(house);
   }

   public int getCosts() {
      int costs = 0;
      if (this.newRooms.size() < this.house.getRoomAmount()) {
         return 0;
      }

      List<RoomInfo> roomTypeList = this.getRoomInfoList(this.house);

      for (int i = 0; i < this.newRooms.size(); i++) {
         RoomInfo newRoomType = this.newRooms.get(i);
         if (i >= roomTypeList.size()) {
            costs += 500;
            costs += newRoomType.getCost();
         } else if (newRoomType != roomTypeList.get(i)) {
            costs += newRoomType.getCost();
         }
      }

      return costs;
   }

   public void adoptRoomLayout() {
      if (this.newRooms.size() >= this.house.getRoomAmount()) {
         int cost = this.getCosts();
         Jasbro.getInstance().getData().spendMoney(cost, "Rooms");

         for (int i = 0; i < this.newRooms.size(); i++) {
            RoomInfo newType = this.newRooms.get(i);
            Room newRoom = null;
            if ("GARDEN".equals(newType.getId()) || "BIGGARDEN".equals(newType.getId())) {
               newRoom = new Garden(newType);
            } else if ("CRYPT".equals(newType.getId())) {
               newRoom = new Crypt(newType);
            } else {
               newRoom = new ConfigurableRoom(newType);
            }

            if (i >= this.house.getRooms().size()) {
               this.house.getRooms().add(newRoom);
            } else if (!newType.getId().equals(this.house.getRooms().get(i).getRoomInfo().getId())) {
               RoomSlot roomSlot = this.house.getRoomSlots().get(i);
               roomSlot.getRoom().empty();
               newRoom.setHouse(this.house);
               roomSlot.setRoom(newRoom);
               roomSlot.setDownTime(roomSlot.getSlotType().getDownTime());
            }
         }
      }
   }

   public List<RoomInfo> getNewRooms() {
      return this.newRooms;
   }

   public void reset() {
      this.newRooms = this.getRoomInfoList(this.house);
   }

   public List<RoomInfo> getRoomInfoList(House house) {
      List<RoomInfo> roomInfoList = new ArrayList<>();

      for (Room room : house.getRooms()) {
         roomInfoList.add(room.getRoomInfo());
      }

      return roomInfoList;
   }
}
