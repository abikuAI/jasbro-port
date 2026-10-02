package jasbro.gui.objects.div;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.gui.GuiUtil;
import jasbro.gui.dnd.CanReceiveEquipmentDrop;
import jasbro.gui.dnd.MyEquipmentTransferHandler;
import jasbro.gui.pages.CharacterScreen;
import jasbro.texts.TextUtil;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EquippedItemPanel extends TranslucentPanel implements CanReceiveEquipmentDrop {
   private static final Logger log = LogManager.getLogger(EquippedItemPanel.class);
   private Charakter character;
   private EquipmentSlot equipmentSlot;
   private MyImage equipmentIcon;
   private CharacterScreen characterScreen;

   public EquippedItemPanel(Charakter characterTmp, EquipmentSlot equipmentSlotTmp, CharacterScreen characterScreenTmp) {
      this.character = characterTmp;
      this.equipmentSlot = equipmentSlotTmp;
      this.characterScreen = characterScreenTmp;
      this.setLayout(new GridLayout(1, 1));
      this.equipmentIcon = new MyImage();
      this.add(this.equipmentIcon);
      JLabel label = new JLabel(this.equipmentSlot.getText());
      label.setFont(GuiUtil.DEFAULTTINYFONT);
      this.equipmentIcon.add(label);
      this.equipmentIcon.addMouseMotionListener(GuiUtil.DELEGATEMOUSELISTENER);
      this.setTransferHandler(new MyEquipmentTransferHandler());
      MouseAdapter tml = new MouseAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            Equipment item = EquippedItemPanel.this.character.getCharacterInventory().getItem(EquippedItemPanel.this.equipmentSlot);
            if (item != null) {
               TransferHandler handle = EquippedItemPanel.this.getTransferHandler();
               handle.exportAsDrag(EquippedItemPanel.this, e, 1073741824);
               EquippedItemPanel.this.character.getCharacterInventory().unequip(EquippedItemPanel.this.equipmentSlot);
               Jasbro.getInstance().getData().getInventory().addItem(item);
            }

            super.mouseDragged(e);
         }
      };
      this.addMouseListener(tml);
      this.addMouseMotionListener(tml);
      this.updateEquipmentIcon();
   }

   public void updateEquipmentIcon() {
      Equipment equipment = this.character.getCharacterInventory().getItem(this.equipmentSlot);
      if (equipment != null) {
         this.equipmentIcon.setImage(equipment.getIcon());
         this.setToolTipText(TextUtil.htmlItem(equipment));
      } else {
         this.equipmentIcon.setImage(null);
         this.setToolTipText("");
      }

      this.repaint();
   }

   @Override
   public void receiveEquipmentDrop(Equipment equipment) {
      Jasbro.getInstance().getData().getInventory().removeItem(equipment);

      for (Equipment curEquipment : this.character.getCharacterInventory().equip(this.equipmentSlot, equipment)) {
         Jasbro.getInstance().getData().getInventory().addItem(curEquipment);
      }

      this.characterScreen.update();
   }

   public Equipment getItem() {
      return this.character.getCharacterInventory().getItem(this.equipmentSlot);
   }

   @Override
   public Point getToolTipLocation(MouseEvent event) {
      Container parent = this.getParent();
      return new Point(parent.getX() - 600, 0);
   }

   @Override
   public EquipmentSlot getEquipmentSlot() {
      return this.equipmentSlot;
   }
}
