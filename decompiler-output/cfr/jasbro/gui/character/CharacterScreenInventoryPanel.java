/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.character;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.items.CharacterInventory;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.UsableItem;
import jasbro.gui.dnd.MyEquipmentTransferHandler;
import jasbro.gui.objects.div.InventoryPanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.CharacterScreen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CharacterScreenInventoryPanel
extends TranslucentPanel {
    private InventoryPanel inventoryPanel;
    private JButton useButton;
    private JButton equipButton;
    private JComboBox<EquipmentSlot> equipmentSlotComboBox;
    private CharacterInventory characterInventory;

    public CharacterScreenInventoryPanel(final Charakter character, final CharacterScreen characterScreen) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("1dlu")}, new RowSpec[]{RowSpec.decode("1dlu"), RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("1dlu")}));
        this.characterInventory = character.getCharacterInventory();
        this.inventoryPanel = new InventoryPanel(Jasbro.getInstance().getData().getInventory(), true);
        this.add((Component)this.inventoryPanel, "2, 2, 2, 1, fill, fill");
        this.setMinimumSize(new Dimension(200, -1));
        this.setPreferredSize(new Dimension(-1, 1600));
        this.useButton = new JButton("Use");
        this.useButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (CharacterScreenInventoryPanel.this.inventoryPanel.getSelectedItem() != null) {
                    Jasbro.getThreadpool().execute(new Runnable(){

                        @Override
                        public void run() {
                            UsableItem usableItem;
                            boolean used;
                            Item item = CharacterScreenInventoryPanel.this.inventoryPanel.getSelectedItem();
                            if (item instanceof UsableItem && (used = (usableItem = (UsableItem)item).use(character))) {
                                Jasbro.getInstance().getData().getInventory().removeItem(usableItem);
                                characterScreen.update();
                            }
                        }
                    });
                }
            }
        });
        this.add((Component)this.useButton, "2, 3");
        this.equipButton = new JButton("Equip");
        this.equipButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Item item = CharacterScreenInventoryPanel.this.inventoryPanel.getSelectedItem();
                if (item != null && item instanceof Equipment && CharacterScreenInventoryPanel.this.equipmentSlotComboBox.getSelectedItem() != null) {
                    Equipment equipment = (Equipment)item;
                    Jasbro.getInstance().getData().getInventory().removeItem(item);
                    List<Equipment> oldEquipmentList = character.getCharacterInventory().equip((EquipmentSlot)((Object)CharacterScreenInventoryPanel.this.equipmentSlotComboBox.getSelectedItem()), equipment);
                    if (oldEquipmentList != null) {
                        for (Equipment oldEquipment : oldEquipmentList) {
                            Jasbro.getInstance().getData().getInventory().addItem(oldEquipment);
                        }
                    }
                    characterScreen.update();
                }
            }
        });
        this.add((Component)this.equipButton, "2, 4");
        this.equipButton.setEnabled(false);
        this.equipmentSlotComboBox = new JComboBox();
        this.add(this.equipmentSlotComboBox, "3, 4, fill, default");
        this.equipmentSlotComboBox.setEnabled(false);
        this.inventoryPanel.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                Inventory.ItemData item = (Inventory.ItemData)((JList)e.getSource()).getSelectedValue();
                if (item != null) {
                    if (item.getItem() instanceof Equipment) {
                        CharacterScreenInventoryPanel.this.useButton.setEnabled(false);
                        CharacterScreenInventoryPanel.this.useButton.setVisible(false);
                        if (!character.getType().isChildType()) {
                            CharacterScreenInventoryPanel.this.equipmentSlotComboBox.setVisible(true);
                            CharacterScreenInventoryPanel.this.equipmentSlotComboBox.setEnabled(true);
                            CharacterScreenInventoryPanel.this.equipButton.setEnabled(true);
                            CharacterScreenInventoryPanel.this.equipButton.setVisible(true);
                        }
                        CharacterScreenInventoryPanel.this.initEquipmentComboBox();
                    } else {
                        CharacterScreenInventoryPanel.this.useButton.setEnabled(true);
                        CharacterScreenInventoryPanel.this.equipButton.setEnabled(false);
                        CharacterScreenInventoryPanel.this.equipmentSlotComboBox.setEnabled(false);
                        CharacterScreenInventoryPanel.this.equipButton.setVisible(false);
                        CharacterScreenInventoryPanel.this.equipmentSlotComboBox.setVisible(false);
                        CharacterScreenInventoryPanel.this.useButton.setVisible(true);
                    }
                }
                CharacterScreenInventoryPanel.this.repaint();
            }
        });
        this.equipButton.setVisible(false);
        this.equipmentSlotComboBox.setVisible(false);
        this.inventoryPanel.getItemList().setTransferHandler(new MyEquipmentTransferHandler());
        MouseAdapter tml = new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    JList<Inventory.ItemData> itemList = CharacterScreenInventoryPanel.this.inventoryPanel.getItemList();
                    int index = itemList.locationToIndex(e.getPoint());
                    itemList.setSelectedIndex(index);
                    if (CharacterScreenInventoryPanel.this.inventoryPanel.getSelectedItem() instanceof Equipment) {
                        CharacterScreenInventoryPanel.this.equipButton.doClick();
                    } else {
                        CharacterScreenInventoryPanel.this.useButton.doClick();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                CharacterScreenInventoryPanel.this.inventoryPanel.getItemList().setAutoscrolls(false);
                TransferHandler handle = CharacterScreenInventoryPanel.this.inventoryPanel.getItemList().getTransferHandler();
                handle.exportAsDrag(CharacterScreenInventoryPanel.this.inventoryPanel.getItemList(), e, 0x40000000);
                CharacterScreenInventoryPanel.this.inventoryPanel.getItemList().setAutoscrolls(true);
            }
        };
        this.inventoryPanel.getItemList().addMouseListener(tml);
        this.inventoryPanel.getItemList().addMouseMotionListener(tml);
    }

    public void initEquipmentComboBox() {
        Item item = this.inventoryPanel.getSelectedItem();
        if (item != null && item instanceof Equipment) {
            Equipment equipment = (Equipment)item;
            this.equipmentSlotComboBox.removeAllItems();
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.getEquipmentType() != equipment.getEquipmentType()) continue;
                this.equipmentSlotComboBox.addItem(equipmentSlot);
            }
            this.equipmentSlotComboBox.setSelectedIndex(0);
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.getEquipmentType() != equipment.getEquipmentType() || this.characterInventory.getItem(equipmentSlot) != null) continue;
                this.equipmentSlotComboBox.setSelectedItem((Object)equipmentSlot);
                break;
            }
        }
    }

    @Override
    public void update() {
        this.inventoryPanel.updateView();
    }
}

