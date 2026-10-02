package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shop extends GameObject implements CentralEventlistener {
   private static final Logger log = LogManager.getLogger(Shop.class);
   private HashMap<ItemLocation, Inventory> shopInventories;
   public static final ItemLocation[] shops = new ItemLocation[]{
      ItemLocation.SHOP,
      ItemLocation.CLOTHINGSTORE,
      ItemLocation.ADVENTURERSSHOP,
      ItemLocation.APOTHECARY,
      ItemLocation.ADULTSTORE,
      ItemLocation.BOOKSTORE,
      ItemLocation.TRAVELLINGMERCHANT
   };

   public Shop() {
      Jasbro.getInstance().addCentralListener(this);
   }

   @Override
   public void handleCentralEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY) {
         this.initInventories();
      }
   }

   public Inventory getInventory(ItemLocation shop) {
      if (!this.getShopInventories().containsKey(shop)) {
         Inventory inventory = new Inventory();
         int chanceModifier = 1;
         int amountModifier = 1;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORSHOPS)) {
            chanceModifier = 2;
            amountModifier = 2;
         }

         for (Item item : Jasbro.getInstance().getAvailableItemsByLocation(shop)) {
            for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
               if (itemSpawnData.getItemLocation() == shop
                  && (itemSpawnData.getChance() == 0 || Util.getInt(0, 100) < itemSpawnData.getChance() * chanceModifier)) {
                  try {
                     inventory.addItems(item, amountModifier * Util.getInt(itemSpawnData.getMinAmount(), amountModifier * itemSpawnData.getMaxAmount() + 1));
                  } catch (Exception e) {
                     log.error("Most likely illegal spawn data: {}", new Object[]{item.getId()});
                     log.throwing(e);
                  }
               }
            }
         }

         if (shop == ItemLocation.BLACKMARKET) {
            return inventory;
         }

         this.getShopInventories().put(shop, inventory);
      }

      return this.getShopInventories().get(shop);
   }

   private void initInventories() {
      for (ItemLocation shop : shops) {
         this.getShopInventories().remove(shop);
         this.getInventory(shop);
      }
   }

   public HashMap<ItemLocation, Inventory> getShopInventories() {
      if (this.shopInventories == null) {
         this.shopInventories = new HashMap<>();
      }

      return this.shopInventories;
   }
}
