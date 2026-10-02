package jasbro.game.items;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CharacterInventory {
   private Map<EquipmentSlot, String> itemMap = new HashMap<>();
   private Charakter character;

   public CharacterInventory(Charakter character) {
      this.character = character;
      this.itemMap.put(EquipmentSlot.DRESS, "RegularClothes");
   }

   public Equipment getItem(EquipmentSlot equipmentSlot) {
      return this.itemMap.containsKey(equipmentSlot) ? this.getEquipmentForId(this.itemMap.get(equipmentSlot)) : null;
   }

   public List<Equipment> listEquipment() {
      List<Equipment> items = new ArrayList<>();
      Map<String, Item> globalItemMap = Jasbro.getInstance().getItems();

      for (String itemId : this.itemMap.values()) {
         if (itemId != null && globalItemMap.containsKey(itemId)) {
            items.add((Equipment)globalItemMap.get(itemId));
         }
      }

      return items;
   }

   public List<Equipment> equip(EquipmentSlot equipmentSlot, Equipment equipment) {
      List<Equipment> returnItems = new ArrayList<>();
      if (equipment.getEquipmentType() != equipmentSlot.getEquipmentType()) {
         returnItems.add(equipment);
         return returnItems;
      }

      if (this.itemMap.containsKey(equipmentSlot)) {
         returnItems.add(this.unequip(equipmentSlot));
      }

      boolean success = equipment.equip(equipmentSlot, this.getCharacter());
      if (success) {
         this.itemMap.put(equipmentSlot, equipment.getId());
         if (equipment.getEquipmentType() == EquipmentType.ACCESSORY) {
            returnItems.addAll(this.removeInvalidAccessory(equipmentSlot, equipment));
         }

         MyEvent event = new MyEvent(EventType.ITEMUSED, equipment);
         this.getCharacter().handleEvent(event);
         this.getCharacter().fireEvent(event);
         this.getCharacter().fireEvent(new MyEvent(EventType.STATUSCHANGE, this.getCharacter()));
         return returnItems;
      } else {
         if (returnItems.size() == 1) {
            returnItems.get(0).equip(equipmentSlot, this.character);
            this.itemMap.put(equipmentSlot, returnItems.get(0).getId());
            returnItems.clear();
         }

         returnItems.add(equipment);
         return returnItems;
      }
   }

   public Equipment unequip(EquipmentSlot equipmentSlot) {
      if (this.itemMap.containsKey(equipmentSlot)) {
         Equipment equipment = this.getEquipmentForId(this.itemMap.get(equipmentSlot));
         equipment.unequip(equipmentSlot, this.getCharacter());
         this.itemMap.remove(equipmentSlot);
         MyEvent event = new MyEvent(EventType.ITEMUSED, equipment);
         this.getCharacter().handleEvent(event);
         this.getCharacter().fireEvent(event);
         return equipment;
      } else {
         return null;
      }
   }

   private Equipment getEquipmentForId(String itemId) {
      Map<String, Item> globalItemMap = Jasbro.getInstance().getItems();
      if (globalItemMap.containsKey(itemId)) {
         Item item = globalItemMap.get(itemId);
         return item instanceof Equipment ? (Equipment)item : null;
      } else {
         return null;
      }
   }

   private Charakter getCharacter() {
      if (this.character == null) {
         for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
            if (character.getCharacterInventory() == this) {
               this.character = character;
            }
         }
      }

      return this.character;
   }

   private List<Equipment> removeInvalidAccessory(EquipmentSlot nowEquippedSlot, Equipment nowEquippedItem) {
      List<Equipment> removedItems = new ArrayList<>();
      if (nowEquippedItem.getEquipmentType() == EquipmentType.ACCESSORY && nowEquippedItem.getAccessoryType() != null) {
         int hands = nowEquippedItem.getAccessoryType().getHandsUsed();

         for (EquipmentSlot equipmentSlot : new HashSet<>(this.itemMap.keySet())) {
            if (equipmentSlot.getEquipmentType() == EquipmentType.ACCESSORY && equipmentSlot != nowEquippedSlot) {
               Equipment curEquipment = this.getItem(equipmentSlot);
               if (curEquipment.getAccessoryType() != null) {
                  if (curEquipment.getAccessoryType() == nowEquippedItem.getAccessoryType() && curEquipment.getAccessoryType() != AccessoryType.ONEHANDED) {
                     removedItems.add(this.unequip(equipmentSlot));
                  } else if (hands + curEquipment.getAccessoryType().getHandsUsed() > 2) {
                     removedItems.add(this.unequip(equipmentSlot));
                  } else {
                     hands += curEquipment.getAccessoryType().getHandsUsed();
                  }
               }
            }
         }
      }

      return removedItems;
   }
}
