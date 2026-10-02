package jasbro.game.housing;

import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;

public class RoomUnlock implements UnlockObject {
   private final String roomInfoId;
   private boolean locked = false;

   public RoomUnlock(String roomInfoId) {
      this.roomInfoId = roomInfoId;
   }

   public String getRoomInfoId() {
      return this.roomInfoId;
   }

   @Override
   public String getText() {
      return RoomInfoUtil.getRoomInfo(this.roomInfoId).getText();
   }

   @Override
   public String getDescription() {
      return RoomInfoUtil.getRoomInfo(this.roomInfoId).getDescription();
   }

   @Override
   public boolean isLocked() {
      return this.locked;
   }

   @Override
   public ImageData getImage() {
      return RoomInfoUtil.getRoomInfo(this.roomInfoId).getImage();
   }

   @Override
   public void setLocked(boolean locked) {
      this.locked = locked;
   }
}
