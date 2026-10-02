package jasbro.game.housing;

import jasbro.texts.TextUtil;

public enum CleanState {
   SPOTLESS(6),
   CLEAN(3),
   TIDY(0),
   MESSY(-5),
   DIRTY(-10),
   FILTHY(-50);

   private int satisfactionModifier;

   CleanState(int satisfactionModifier) {
      this.satisfactionModifier = satisfactionModifier;
   }

   public String getText() {
      return TextUtil.t(this.toString());
   }

   public int getSatisfactionModifier() {
      return this.satisfactionModifier;
   }

   public static CleanState calcState(House house) {
      int amount = house.getDirt() / house.getRoomAmount();
      if (amount < 5) {
         return SPOTLESS;
      } else if (amount < 15) {
         return CLEAN;
      } else if (amount < 25) {
         return TIDY;
      } else if (amount < 35) {
         return MESSY;
      } else {
         return amount < 45 ? DIRTY : FILTHY;
      }
   }
}
