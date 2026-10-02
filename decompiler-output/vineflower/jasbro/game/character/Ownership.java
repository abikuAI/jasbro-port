package jasbro.game.character;

public enum Ownership {
   OWNED(true),
   CONTRACT(false),
   NOTOWNED(false),
   NOTOWNEDCANSELL(true),
   NONE(false);

   private boolean sellable;

   Ownership(boolean sellable) {
      this.sellable = sellable;
   }

   public boolean isSellable() {
      return this.sellable;
   }
}
