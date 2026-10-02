package jasbro.game.character;

import jasbro.texts.TextUtil;

public enum Gender {
   MALE,
   FEMALE,
   FUTA;

   private String text = null;

   public String getText() {
      if (this.text == null) {
         this.text = TextUtil.t(this.toString());
      }

      return this.text;
   }

   public boolean isFemale() {
      return this != MALE;
   }

   public static Gender getRemaining(Gender gender1, Gender gender2) {
      for (Gender gender : values()) {
         if (gender != gender1 && gender != gender2) {
            return gender;
         }
      }

      return null;
   }
}
