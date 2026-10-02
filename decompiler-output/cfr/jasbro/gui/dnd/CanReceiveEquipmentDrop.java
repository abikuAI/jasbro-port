/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.dnd;

import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;

public interface CanReceiveEquipmentDrop {
    public void receiveEquipmentDrop(Equipment var1);

    public EquipmentSlot getEquipmentSlot();
}

