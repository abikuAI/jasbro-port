package jasbro.game.character;

import jasbro.texts.TextUtil;

public enum CharacterType {
   SLAVE,
   TRAINER,
   INFANT(true),
   CHILD(true),
   TEENAGER(true);

   private boolean childType = false;

   CharacterType() {
   }

   CharacterType(boolean childType) {
      this.childType = childType;
   }

   public String getText() {
      return TextUtil.t(this.toString());
   }

   public boolean isChildType() {
      return this.childType;
   }
}
