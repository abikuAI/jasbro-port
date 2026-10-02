package jasbro.game.character.battle;

import jasbro.texts.TextUtil;

public enum MonsterDickType {
   NONE,
   TENTACLE,
   NORMAL,
   FLUID;

   private String text = null;

   public String getText() {
      if (this.text == null) {
         this.text = TextUtil.t(this.toString());
      }

      return this.text;
   }
}
