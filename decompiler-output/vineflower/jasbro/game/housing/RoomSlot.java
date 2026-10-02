package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomSlot extends CharacterLocation {
   private RoomSlotType slotType;
   private Room room;
   private int downTime = 0;

   public RoomSlot(RoomSlotType slotType, House house) {
      this(slotType, "EMPTYROOM", house);
   }

   public RoomSlot(RoomSlotType slotType, String roomInfoId, House house) {
      this.slotType = slotType;
      this.room = RoomInfoUtil.newRoom(roomInfoId);
      this.room.setHouse(house);
   }

   public RoomSlotType getSlotType() {
      return this.slotType;
   }

   public void setSlotType(RoomSlotType slotType) {
      this.slotType = slotType;
   }

   public Room getRoom() {
      return this.room;
   }

   public void setRoom(Room room) {
      this.room = room;
   }

   public boolean isAvailable() {
      return this.downTime <= 0;
   }

   public int getDownTime() {
      return this.downTime;
   }

   public void setDownTime(int downTime) {
      this.downTime = downTime;
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY && this.downTime > 0) {
         this.downTime--;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORCARPENTERS) && this.downTime > 0) {
            this.downTime--;
         }
      }

      this.room.handleEvent(e);
   }

   @Override
   public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
      if (this.isAvailable()) {
         return this.room.getPossibleActivities(time, typeAmounts);
      }

      List<ActivityDetails> activityDetails = new ArrayList<>();
      activityDetails.add(new ActivityDetails(ActivityType.IDLE));
      return activityDetails;
   }

   @Override
   public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, Util.TypeAmounts typeAmounts) {
      if (this.isAvailable()) {
         return this.room.getPossibleActivitiesChildCare(time, typeAmounts);
      }

      List<ActivityDetails> activityDetails = new ArrayList<>();
      activityDetails.add(new ActivityDetails(ActivityType.IDLE));
      return activityDetails;
   }

   @Override
   public String getName() {
      return this.slotType.getText() + ": " + this.room.getName();
   }

   @Override
   public String getDescription() {
      return this.room.getDescription();
   }

   @Override
   public LocationTypeInterface getLocationType() {
      return this.room.getLocationType();
   }

   @Override
   public ImageData getImage() {
      return !this.isAvailable() ? new ImageData("images/backgrounds/under-construction.png") : this.room.getImage();
   }

   @Override
   public PlannedActivity getCurrentUsage() {
      return this.room.getCurrentUsage();
   }

   @Override
   public Map<Time, PlannedActivity> getUsageMap() {
      return this.room.getUsageMap();
   }

   @Override
   public int getMaxPeople() {
      return this.isAvailable() ? this.room.getMaxPeople() : 0;
   }

   @Override
   public void empty() {
      this.room.empty();
   }

   @Override
   public void addListener(MyEventListener listener) {
      this.room.addListener(listener);
   }
}
