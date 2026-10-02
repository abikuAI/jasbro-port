package jasbro.game.items.usableItemEffects;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.items.Item;

public class UsableItemAddGold extends UsableItemEffect {
   public int amount;

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   @Override
   public void apply(Charakter character, Item item) {
      if (this.amount > 0) {
         Jasbro.getInstance().getData().earnMoney(this.amount, item.getName());
      } else if (this.amount < 0) {
         Jasbro.getInstance().getData().spendMoney(-this.amount, item.getName());
      }
   }

   @Override
   public String getName() {
      return "Add gold effect";
   }

   @Override
   public UsableItemEffectType getType() {
      return UsableItemEffectType.ADDGOLD;
   }
}
