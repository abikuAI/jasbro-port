/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items;

import jasbro.Jasbro;
import jasbro.game.events.MessageData;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.UnlockItem;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> items = new HashMap<String, Integer>();

    public void addItem(Item item) {
        String itemId = item.getId();
        if (this.checkTriggerUnlock(item)) {
            this.unlock((UnlockItem)item);
        } else if (!this.items.containsKey(itemId)) {
            this.items.put(itemId, 1);
        } else {
            this.items.put(itemId, this.items.get(itemId) + 1);
        }
    }

    public void addItems(Item item, int amount) {
        if (amount > 0) {
            if (this.checkTriggerUnlock(item)) {
                this.unlock((UnlockItem)item);
            } else {
                String itemId = item.getId();
                if (!this.items.containsKey(itemId)) {
                    this.items.put(itemId, amount);
                } else {
                    this.items.put(itemId, this.items.get(itemId) + amount);
                }
            }
        }
    }

    public void addItems(List<ItemData> items) {
        for (ItemData itemData : items) {
            this.addItems(itemData.getItem(), itemData.getAmount());
        }
    }

    public void removeItem(Item item) {
        String itemId = item.getId();
        if (this.items.containsKey(itemId)) {
            this.items.put(itemId, this.items.get(itemId) - 1);
            if (this.items.get(itemId) < 1) {
                this.items.remove(itemId);
            }
        }
    }

    public void removeItems(Item item, int amount) {
        String itemId = item.getId();
        if (this.items.containsKey(itemId)) {
            this.items.put(itemId, this.items.get(itemId) - amount);
            if (this.items.get(itemId) < 1) {
                this.items.remove(itemId);
            }
        }
    }

    public int getAmount(Item item) {
        if (item == null) {
            return 0;
        }
        if (!this.items.containsKey(item.getId())) {
            return 0;
        }
        return this.items.get(item.getId());
    }

    public List<ItemData> getItems() {
        ArrayList<ItemData> itemList = new ArrayList<ItemData>();
        Map<String, Item> itemMap = Jasbro.getInstance().getItems();
        ArrayList<String> removeList = new ArrayList<String>();
        for (String itemId : this.items.keySet()) {
            if (itemMap.containsKey(itemId)) {
                itemList.add(new ItemData(itemMap.get(itemId), this.items.get(itemId)));
                continue;
            }
            removeList.add(itemId);
        }
        itemList.removeAll(removeList);
        Collections.sort(itemList);
        return itemList;
    }

    public List<Item> getExistingItems() {
        ArrayList<Item> itemList = new ArrayList<Item>();
        Map<String, Item> itemMap = Jasbro.getInstance().getItems();
        ArrayList<String> removeList = new ArrayList<String>();
        for (String itemId : this.items.keySet()) {
            if (itemMap.containsKey(itemId)) {
                itemList.add(itemMap.get(itemId));
                continue;
            }
            removeList.add(itemId);
        }
        itemList.removeAll(removeList);
        return itemList;
    }

    public boolean checkTriggerUnlock(Item item) {
        return item.getType() == ItemType.UNLOCK && this == Jasbro.getInstance().getData().getInventory();
    }

    public void unlock(UnlockItem item) {
        Jasbro.getInstance().getData().getUnlocks().addUnlock(item.getUnlockObject());
        MessageData messageData = new MessageData(item.getUnlockMessage(), item.getUnlockObject().getImage(), new ImageData("images/backgrounds/sky.jpg"), true);
        messageData.createMessageScreen();
    }

    public static class ItemData
    implements Comparable<ItemData> {
        private Item item;
        private int amount;

        public ItemData() {
        }

        public ItemData(Item item, int amount) {
            this.item = item;
            this.amount = amount;
        }

        public Item getItem() {
            return this.item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String toString() {
            return this.item.getName() + " (" + this.getAmount() + ")";
        }

        @Override
        public int compareTo(ItemData o) {
            return this.item.getName().compareTo(o.getItem().getName());
        }
    }
}

