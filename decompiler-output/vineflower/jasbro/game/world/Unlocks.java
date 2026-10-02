package jasbro.game.world;

import jasbro.Jasbro;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.housing.RoomUnlock;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.UnlockItem;
import jasbro.game.world.locations.LocationType;
import java.util.ArrayList;
import java.util.List;

public class Unlocks {
   private List<UnlockObject> unlockedObjects;
   private transient List<Unlocks.FameUnlock> fameUnlockObjects;

   public List<HouseType> getAvailableHouseTypes() {
      if (this.fameUnlockObjects == null) {
         this.init();
      }

      List<HouseType> houseTypes = new ArrayList<>();

      for (HouseType houseType : HouseType.values()) {
         if (!houseType.isLocked()) {
            houseTypes.add(houseType);
         } else if (this.getUnlockedObjects().contains(houseType)) {
            houseTypes.add(houseType);
         }
      }

      return houseTypes;
   }

   public List<RoomInfo> getAvailableRoomTypes() {
      if (this.fameUnlockObjects == null) {
         this.init();
      }

      List<RoomInfo> roomInfos = new ArrayList<>();

      for (RoomUnlock roomUnlock : RoomInfoUtil.getRoomUnlocks()) {
         if (!roomUnlock.isLocked() || this.getUnlockedObjects().contains(roomUnlock)) {
            roomInfos.add(RoomInfoUtil.getRoomInfo(roomUnlock.getRoomInfoId()));
         }
      }

      return roomInfos;
   }

   public List<LocationType> getAvailableLocations() {
      if (this.fameUnlockObjects == null) {
         this.init();
      }

      List<LocationType> locationTypes = new ArrayList<>();

      for (LocationType locationType : LocationType.values()) {
         if (!locationType.isLocked()) {
            locationTypes.add(locationType);
         } else if (this.getUnlockedObjects().contains(locationType)) {
            locationTypes.add(locationType);
         }
      }

      return locationTypes;
   }

   public List<SpecializationType> getAvailableSpecializations() {
      if (this.fameUnlockObjects == null) {
         this.init();
      }

      List<SpecializationType> specializationTypes = new ArrayList<>();

      for (SpecializationType specializationType : SpecializationType.values()) {
         if (!specializationType.isLocked()) {
            specializationTypes.add(specializationType);
         } else if (this.getUnlockedObjects().contains(specializationType)) {
            specializationTypes.add(specializationType);
         }
      }

      return specializationTypes;
   }

   public void init() {
      for (HouseType houseType : HouseType.values()) {
         houseType.setLocked(false);
      }

      for (RoomUnlock roomUnlock : RoomInfoUtil.getRoomUnlocks()) {
         roomUnlock.setLocked(false);
      }

      for (SpecializationType specializationType : SpecializationType.values()) {
         specializationType.setLocked(false);
      }

      for (Item item : Jasbro.getInstance().getItems().values()) {
         if (item.getType() == ItemType.UNLOCK) {
            UnlockObject unlockObject = ((UnlockItem)item).getUnlockObject();
            if (unlockObject != null) {
               unlockObject.setLocked(true);
            }
         }
      }

      this.fameUnlockObjects = new ArrayList<>();
      new FameUnlockLoader().loadUnlocks(this);
   }

   public List<Unlocks.FameUnlock> getFameUnlockObjects() {
      if (this.fameUnlockObjects == null) {
         this.init();
      }

      return this.fameUnlockObjects;
   }

   public List<UnlockObject> getUnlockedObjects() {
      ArrayList<UnlockObject> unlocks = new ArrayList<>(this.getUnlockedObjectsInternal());

      for (Unlocks.FameUnlock fameUnlock : this.getFameUnlockObjects()) {
         if (fameUnlock.isUnlocked()) {
            unlocks.add(fameUnlock.getUnlockObject());
         }
      }

      return unlocks;
   }

   public List<UnlockObject> getUnlockedObjectsInternal() {
      if (this.unlockedObjects == null) {
         this.unlockedObjects = new ArrayList<>();
      }

      return this.unlockedObjects;
   }

   private long getFame() {
      return Jasbro.getInstance().getData().getProtagonist().getFame().getFame();
   }

   public void addUnlock(UnlockObject unlockObject) {
      if (!this.getUnlockedObjectsInternal().contains(unlockObject)) {
         this.getUnlockedObjectsInternal().add(unlockObject);
      }
   }

   protected void addFameUnlock(UnlockObject unlockObject, long fame) {
      this.getFameUnlockObjects().add(new Unlocks.FameUnlock(unlockObject, fame));
      unlockObject.setLocked(true);
   }

   public boolean isUnlocked(UnlockObject unlockObject) {
      return !unlockObject.isLocked() || this.getUnlockedObjects().contains(unlockObject);
   }

   public boolean isLocked(UnlockObject unlockObject) {
      return unlockObject.isLocked() && !this.getUnlockedObjects().contains(unlockObject);
   }

   public class FameUnlock {
      private long requiredFame;
      private UnlockObject unlockObject;

      public FameUnlock() {
      }

      public FameUnlock(UnlockObject unlockObject, long requiredFame) {
         this.unlockObject = unlockObject;
         this.requiredFame = requiredFame;
      }

      public long getRequiredFame() {
         return this.requiredFame;
      }

      public void setRequiredFame(long requiredFame) {
         this.requiredFame = requiredFame;
      }

      public boolean isUnlocked() {
         return Unlocks.this.getFame() >= this.requiredFame;
      }

      public UnlockObject getUnlockObject() {
         return this.unlockObject;
      }

      public void setUnlockObject(UnlockObject unlockObject) {
         this.unlockObject = unlockObject;
      }
   }
}
