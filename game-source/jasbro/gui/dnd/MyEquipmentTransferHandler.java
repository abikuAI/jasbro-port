package jasbro.gui.dnd;

import jasbro.Jasbro;
import jasbro.game.items.Equipment;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.gui.objects.div.EquippedItemPanel;
import jasbro.gui.pictures.ImageUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr.Mode;

public class MyEquipmentTransferHandler extends TransferHandler {
   private static final Logger log = LogManager.getLogger(MyEquipmentTransferHandler.class);

   @Override
   public int getSourceActions(JComponent c) {
      return 1073741824;
   }

   @Override
   protected Transferable createTransferable(JComponent c) {
      if (c instanceof JList) {
         Item item = ((Inventory.ItemData)((JList)c).getSelectedValue()).getItem();
         if (item instanceof Equipment) {
            Equipment equipment = (Equipment)item;
            this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Mode.AUTOMATIC));
            return new TransferableEquipment(equipment);
         } else {
            return null;
         }
      } else if (c instanceof EquippedItemPanel) {
         EquippedItemPanel equippedItemPanel = (EquippedItemPanel)c;
         Equipment equipment = equippedItemPanel.getItem();
         if (equipment != null) {
            this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Mode.AUTOMATIC));
            return new TransferableEquipment(equipment);
         } else {
            return null;
         }
      } else {
         return null;
      }
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
   public boolean canImport(TransferSupport support) {
      Component comp = support.getComponent();
      boolean canImport = false;

      do {
         canImport = this.importPossible(comp);
         if (!canImport) {
            comp = comp.getParent();
         }
      } while (!canImport && comp != null);

      return comp instanceof CanReceiveEquipmentDrop
         ? ((CanReceiveEquipmentDrop)comp).getEquipmentSlot().getEquipmentType() == this.getItem(support).getEquipmentType()
         : false;
   }

   @Override
   public boolean importData(TransferSupport support) {
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
         ((CanReceiveEquipmentDrop)cont).receiveEquipmentDrop(equipment);
         return false;
      } catch (Exception e) {
         log.error("Error on importing data", e);
         return false;
      }
   }

   public Equipment getItem(TransferSupport support) {
      try {
         return (Equipment)Jasbro.getInstance()
            .getItems()
            .get((String)support.getTransferable().getTransferData(support.getTransferable().getTransferDataFlavors()[0]));
      } catch (Exception e) {
         return null;
      }
   }
}
