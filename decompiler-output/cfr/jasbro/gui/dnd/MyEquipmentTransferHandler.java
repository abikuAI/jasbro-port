/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.imgscalr.Scalr$Mode
 */
package jasbro.gui.dnd;

import jasbro.Jasbro;
import jasbro.game.items.Equipment;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.gui.dnd.CanReceiveEquipmentDrop;
import jasbro.gui.dnd.TransferableEquipment;
import jasbro.gui.objects.div.EquippedItemPanel;
import jasbro.gui.pictures.ImageUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

public class MyEquipmentTransferHandler
extends TransferHandler {
    private static final Logger log = LogManager.getLogger(MyEquipmentTransferHandler.class);

    @Override
    public int getSourceActions(JComponent c) {
        return 0x40000000;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JList) {
            Item item = ((Inventory.ItemData)((JList)c).getSelectedValue()).getItem();
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment)item;
                this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Scalr.Mode.AUTOMATIC));
                return new TransferableEquipment(equipment);
            }
            return null;
        }
        if (c instanceof EquippedItemPanel) {
            EquippedItemPanel equippedItemPanel = (EquippedItemPanel)c;
            Equipment equipment = equippedItemPanel.getItem();
            if (equipment != null) {
                this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Scalr.Mode.AUTOMATIC));
                return new TransferableEquipment(equipment);
            }
            return null;
        }
        return null;
    }

    private boolean importPossible(Component comp) {
        return comp instanceof CanReceiveEquipmentDrop;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        log.error("Method not supported");
        return false;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        Component comp = support.getComponent();
        boolean canImport = false;
        do {
            if (canImport = this.importPossible(comp)) continue;
            comp = comp.getParent();
        } while (!canImport && comp != null);
        if (comp instanceof CanReceiveEquipmentDrop) {
            return ((CanReceiveEquipmentDrop)((Object)comp)).getEquipmentSlot().getEquipmentType() == this.getItem(support).getEquipmentType();
        }
        return false;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        return this.importData((JComponent)support.getComponent(), support.getTransferable());
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        try {
            Container cont = comp;
            while (!(cont instanceof CanReceiveEquipmentDrop)) {
                cont = cont.getParent();
            }
            Equipment equipment = (Equipment)Jasbro.getInstance().getItems().get((String)t.getTransferData(t.getTransferDataFlavors()[0]));
            ((CanReceiveEquipmentDrop)((Object)cont)).receiveEquipmentDrop(equipment);
            return false;
        }
        catch (Exception e) {
            log.error("Error on importing data", (Throwable)e);
            return false;
        }
    }

    public Equipment getItem(TransferHandler.TransferSupport support) {
        try {
            return (Equipment)Jasbro.getInstance().getItems().get((String)support.getTransferable().getTransferData(support.getTransferable().getTransferDataFlavors()[0]));
        }
        catch (Exception e) {
            return null;
        }
    }
}

