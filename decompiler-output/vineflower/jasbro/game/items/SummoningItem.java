package jasbro.game.items;

public class SummoningItem extends Item {
   private String monsterID;

   public SummoningItem(String id) {
      super(id, ItemType.SUMMONING);
   }

   public SummoningItem(Item item) {
      super(item);
      this.setType(ItemType.SUMMONING);
   }

   public String getSummonedMonster() {
      return this.monsterID;
   }

   public void setSummonedMonster(String monsterID) {
      this.monsterID = monsterID;
   }

   @Override
   public String getText() {
      return this.getDescription();
   }
}
