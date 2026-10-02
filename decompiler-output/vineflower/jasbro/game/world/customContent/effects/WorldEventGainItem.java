package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.items.Item;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventGainItem extends WorldEventEffect {
   private String itemId;

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      Item item = Jasbro.getInstance().getItems().get(this.itemId);
      Jasbro.getInstance().getData().getInventory().addItem(item);
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.GAINITEM;
   }

   public String getItemId() {
      return this.itemId;
   }

   public void setItemId(String itemId) {
      this.itemId = itemId;
   }
}
