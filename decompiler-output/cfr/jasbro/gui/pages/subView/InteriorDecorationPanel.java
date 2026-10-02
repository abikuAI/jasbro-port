/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.gui.pages.subView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomPlanning;
import jasbro.game.housing.RoomSlot;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InteriorDecorationPanel
extends JPanel {
    private Logger log = LogManager.getLogger(InteriorDecorationPanel.class);
    private RoomPlanning roomPlanning;
    private JLabel costLabel;

    public InteriorDecorationPanel() {
        this.setOpaque(false);
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("pref:grow"), ColumnSpec.decode("1dlu:grow(8)"), ColumnSpec.decode("pref:grow"), ColumnSpec.decode("1dlu:grow(8)"), ColumnSpec.decode("pref:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow")}));
        JPanel housePanel = new JPanel();
        this.add((Component)housePanel, "2, 2, fill, fill");
        housePanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow(40)")}));
        housePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
        housePanel.setBorder(GuiUtil.DEFAULTBORDER);
        JLabel lblNewLabel = new JLabel(TextUtil.t("ui.interiordeco"));
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        housePanel.add((Component)lblNewLabel, "1, 4");
        final JComboBox<House> houseSelectBox = new JComboBox<House>();
        housePanel.add(houseSelectBox, "1, 6, fill, default");
        houseSelectBox.addItem(null);
        houseSelectBox.setSelectedIndex(0);
        for (House house : Jasbro.getInstance().getData().getHouses()) {
            houseSelectBox.addItem(house);
        }
        final JPanel roomPanel = new JPanel();
        roomPanel.setOpaque(false);
        housePanel.add((Component)roomPanel, "1, 8, fill, default");
        final ItemListener roomListener = new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    if (e.getStateChange() == 1) {
                        JComboBox jComboBox = (JComboBox)e.getSource();
                        int id = Integer.parseInt(jComboBox.getActionCommand());
                        InteriorDecorationPanel.this.roomPlanning.getNewRooms().remove(id);
                        RoomInfo roomInfo = (RoomInfo)jComboBox.getSelectedItem();
                        InteriorDecorationPanel.this.roomPlanning.getNewRooms().add(id, roomInfo);
                        jComboBox.setToolTipText(roomInfo.getDescription());
                        InteriorDecorationPanel.this.updateCostLabel();
                    }
                }
                catch (Exception ex) {
                    InteriorDecorationPanel.this.log.error("Error", (Throwable)ex);
                }
            }
        };
        houseSelectBox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                FormLayout fl = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")});
                House house = (House)houseSelectBox.getSelectedItem();
                roomPanel.removeAll();
                roomPanel.setLayout(fl);
                InteriorDecorationPanel.this.roomPlanning = null;
                if (house != null) {
                    InteriorDecorationPanel.this.roomPlanning = new RoomPlanning(house);
                    int i = -1;
                    for (RoomSlot roomSlot : house.getRoomSlots()) {
                        JComboBox<Object> roomSelect = new JComboBox<Object>();
                        roomSelect.setRenderer(new DefaultListCellRenderer(){

                            @Override
                            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                                JLabel label = (JLabel)super.getListCellRendererComponent((JList<?>)list, value, index, isSelected, hasFocus);
                                RoomInfo roomInfo = (RoomInfo)value;
                                label.setText(roomInfo.getText());
                                label.setToolTipText(roomInfo.getDescription());
                                label.setForeground(Color.BLACK);
                                label.setOpaque(false);
                                return label;
                            }
                        });
                        fl.insertRow(++i + 1, RowSpec.decode("default:none"));
                        roomPanel.add(roomSelect, "1," + (i + 1) + ", fill, top");
                        roomSelect.setBorder(new EmptyBorder(2, 2, 2, 2));
                        roomSelect.setActionCommand(i + "");
                        roomSelect.setOpaque(false);
                        boolean actualRoomTypeAdded = false;
                        for (RoomInfo roomInfo : Jasbro.getInstance().getData().getUnlocks().getAvailableRoomTypes()) {
                            if (!roomInfo.fitsInSlot(roomSlot.getSlotType())) continue;
                            roomSelect.addItem(roomInfo);
                            if (!roomInfo.getId().equals(roomSlot.getRoom().getRoomInfo().getId())) continue;
                            roomSelect.setSelectedItem(roomInfo);
                            roomSelect.setToolTipText(roomInfo.getDescription());
                            actualRoomTypeAdded = true;
                        }
                        if (!actualRoomTypeAdded) {
                            roomSelect.addItem(roomSlot.getRoom().getRoomInfo());
                            roomSelect.setSelectedItem(roomSlot.getRoom().getRoomInfo());
                            roomSelect.setToolTipText(roomSlot.getRoom().getRoomInfo().getDescription());
                        }
                        roomSelect.addItemListener(roomListener);
                    }
                }
                roomPanel.validate();
                roomPanel.repaint();
            }
        });
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
        controlPanel.setBorder(GuiUtil.DEFAULTBORDER);
        this.add((Component)controlPanel, "4, 2, fill, fill");
        controlPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.PREF_ROWSPEC, RowSpec.decode("20dlu"), FormFactory.PREF_ROWSPEC, RowSpec.decode("20dlu"), FormFactory.PREF_ROWSPEC, RowSpec.decode("default:grow")}));
        this.costLabel = new JLabel("Cost changes: 0");
        controlPanel.add((Component)this.costLabel, "1, 1");
        JButton btnReset = new JButton("Reset");
        controlPanel.add((Component)btnReset, "1, 3");
        btnReset.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (InteriorDecorationPanel.this.roomPlanning != null) {
                    InteriorDecorationPanel.this.roomPlanning.reset();
                    int index = houseSelectBox.getSelectedIndex();
                    houseSelectBox.setSelectedIndex(0);
                    houseSelectBox.setSelectedIndex(index);
                    InteriorDecorationPanel.this.updateCostLabel();
                }
            }
        });
        JButton btnBuyChanges = new JButton("Buy changes");
        controlPanel.add((Component)btnBuyChanges, "1, 5");
        btnBuyChanges.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (InteriorDecorationPanel.this.roomPlanning != null && Jasbro.getInstance().getData().canAfford(InteriorDecorationPanel.this.roomPlanning.getCosts())) {
                    InteriorDecorationPanel.this.roomPlanning.adoptRoomLayout();
                    InteriorDecorationPanel.this.updateCostLabel();
                }
            }
        });
    }

    public void updateCostLabel() {
        if (this.roomPlanning != null) {
            this.costLabel.setText("Cost changes: " + this.roomPlanning.getCosts());
        } else {
            this.costLabel.setText("Cost changes: 0");
        }
        this.repaint();
    }
}

