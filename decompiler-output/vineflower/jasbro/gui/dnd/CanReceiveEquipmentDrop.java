package jasbro.gui.dnd;

import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;

public interface CanReceiveEquipmentDrop {
   void receiveEquipmentDrop(Equipment var1);

   EquipmentSlot getEquipmentSlot();
}
