package jasbro.game.housing;

import jasbro.texts.TextUtil;

public enum RoomSlotType {
   SMALLROOM(1),
   LARGEROOM(3),
   OUTDOOR(8),
   UNDERGROUND(5);

   private int downTime;

   RoomSlotType(int downTime) {
      this.downTime = downTime;
   }

   public String getText() {
      return TextUtil.t(this.toString());
   }

   public int getDownTime() {
      return this.downTime;
   }
}
