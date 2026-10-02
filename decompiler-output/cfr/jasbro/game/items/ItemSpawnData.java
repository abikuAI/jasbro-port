/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items;

import jasbro.game.items.ItemLocation;

public class ItemSpawnData {
    private ItemLocation itemLocation = ItemLocation.SHOP;
    private int chance;
    private int minAmount;
    private int maxAmount;

    public ItemLocation getItemLocation() {
        return this.itemLocation;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public int getChance() {
        return this.chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getMinAmount() {
        return this.minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
}

