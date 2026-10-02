package jasbro.game.items;

public class LootItem extends Item {
   private String monsterID;

   public LootItem(String id) {
      super(id, ItemType.LOOT);
   }

   public LootItem(Item item) {
      super(item);
      this.setType(ItemType.LOOT);
   }

   @Override
   public String getText() {
      return this.getDescription();
   }
}
