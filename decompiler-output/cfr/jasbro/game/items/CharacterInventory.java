/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.items.AccessoryType;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.game.items.EquipmentType;
import jasbro.game.items.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CharacterInventory {
    private Map<EquipmentSlot, String> itemMap = new HashMap<EquipmentSlot, String>();
    private Charakter character;

    public CharacterInventory(Charakter character) {
        this.character = character;
        this.itemMap.put(EquipmentSlot.DRESS, "RegularClothes");
    }

    public Equipment getItem(EquipmentSlot equipmentSlot) {
        if (this.itemMap.containsKey((Object)equipmentSlot)) {
            return this.getEquipmentForId(this.itemMap.get((Object)equipmentSlot));
        }
        return null;
    }

    public List<Equipment> listEquipment() {
        ArrayList<Equipment> items = new ArrayList<Equipment>();
        Map<String, Item> globalItemMap = Jasbro.getInstance().getItems();
        for (String itemId : this.itemMap.values()) {
            if (itemId == null || !globalItemMap.containsKey(itemId)) continue;
            items.add((Equipment)globalItemMap.get(itemId));
        }
        return items;
    }

    public List<Equipment> equip(EquipmentSlot equipmentSlot, Equipment equipment) {
        boolean success;
        ArrayList<Equipment> returnItems = new ArrayList<Equipment>();
        if (equipment.getEquipmentType() != equipmentSlot.getEquipmentType()) {
            returnItems.add(equipment);
            return returnItems;
        }
        if (this.itemMap.containsKey((Object)equipmentSlot)) {
            returnItems.add(this.unequip(equipmentSlot));
        }
        if (success = equipment.equip(equipmentSlot, this.getCharacter())) {
            this.itemMap.put(equipmentSlot, equipment.getId());
            if (equipment.getEquipmentType() == EquipmentType.ACCESSORY) {
                returnItems.addAll(this.removeInvalidAccessory(equipmentSlot, equipment));
            }
            MyEvent event = new MyEvent(EventType.ITEMUSED, equipment);
            this.getCharacter().handleEvent(event);
            this.getCharacter().fireEvent(event);
            this.getCharacter().fireEvent(new MyEvent(EventType.STATUSCHANGE, this.getCharacter()));
            return returnItems;
        }
        if (returnItems.size() == 1) {
            ((Equipment)returnItems.get(0)).equip(equipmentSlot, this.character);
            this.itemMap.put(equipmentSlot, ((Equipment)returnItems.get(0)).getId());
            returnItems.clear();
        }
        returnItems.add(equipment);
        return returnItems;
    }

    public Equipment unequip(EquipmentSlot equipmentSlot) {
        if (this.itemMap.containsKey((Object)equipmentSlot)) {
            Equipment equipment = this.getEquipmentForId(this.itemMap.get((Object)equipmentSlot));
            equipment.unequip(equipmentSlot, this.getCharacter());
            this.itemMap.remove((Object)equipmentSlot);
            MyEvent event = new MyEvent(EventType.ITEMUSED, equipment);
            this.getCharacter().handleEvent(event);
            this.getCharacter().fireEvent(event);
            return equipment;
        }
        return null;
    }

    private Equipment getEquipmentForId(String itemId) {
        Map<String, Item> globalItemMap = Jasbro.getInstance().getItems();
        if (globalItemMap.containsKey(itemId)) {
            Item item = globalItemMap.get(itemId);
            if (item instanceof Equipment) {
                return (Equipment)item;
            }
            return null;
        }
        return null;
    }

    private Charakter getCharacter() {
        if (this.character == null) {
            for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                if (character.getCharacterInventory() != this) continue;
                this.character = character;
            }
        }
        return this.character;
    }

    private List<Equipment> removeInvalidAccessory(EquipmentSlot nowEquippedSlot, Equipment nowEquippedItem) {
        ArrayList<Equipment> removedItems = new ArrayList<Equipment>();
        if (nowEquippedItem.getEquipmentType() == EquipmentType.ACCESSORY && nowEquippedItem.getAccessoryType() != null) {
            int hands = nowEquippedItem.getAccessoryType().getHandsUsed();
            HashSet<EquipmentSlot> equipmentSlots = new HashSet<EquipmentSlot>(this.itemMap.keySet());
            for (EquipmentSlot equipmentSlot : equipmentSlots) {
                Equipment curEquipment;
                if (equipmentSlot.getEquipmentType() != EquipmentType.ACCESSORY || equipmentSlot == nowEquippedSlot || (curEquipment = this.getItem(equipmentSlot)).getAccessoryType() == null) continue;
                if (curEquipment.getAccessoryType() == nowEquippedItem.getAccessoryType() && curEquipment.getAccessoryType() != AccessoryType.ONEHANDED) {
                    removedItems.add(this.unequip(equipmentSlot));
                    continue;
                }
                if (hands + curEquipment.getAccessoryType().getHandsUsed() > 2) {
                    removedItems.add(this.unequip(equipmentSlot));
                    continue;
                }
                hands += curEquipment.getAccessoryType().getHandsUsed();
            }
        }
        return removedItems;
    }
}

