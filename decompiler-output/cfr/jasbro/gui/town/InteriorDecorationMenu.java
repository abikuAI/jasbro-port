/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.gui.town;

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
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InteriorDecorationMenu
extends JPanel {
    private Logger log = LogManager.getLogger(InteriorDecorationMenu.class);
    private RoomPlanning roomPlanning;
    private JLabel costLabel;

    public InteriorDecorationMenu() {
        this.setOpaque(false);
        double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
        double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
        int widthRat = (int)(width / 1280.0);
        int heightRat = (int)(height / 720.0);
        int iconSize = (int)(65.0 * width / 1280.0);
        int backHomeBtnWidth = (int)(150.0 * width / 1280.0);
        int backHomeBtnHeight = (int)(50.0 * width / 1280.0);
        ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
        Image homeImage1 = homeIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        homeIcon1 = new ImageIcon(homeImage1);
        ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
        Image homeImage2 = homeIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        homeIcon2 = new ImageIcon(homeImage2);
        JButton homeButton = new JButton(homeIcon1);
        homeButton.setRolloverIcon(homeIcon2);
        homeButton.setPressedIcon(homeIcon1);
        homeButton.setBounds((int)(15.0 * width / 1280.0), (int)(550.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.setOpaque(false);
        this.add(homeButton);
        homeButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showHouseManagementScreen();
            }
        });
        ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
        Image backImage1 = backIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        backIcon1 = new ImageIcon(backImage1);
        ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
        Image backImage2 = backIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        backIcon2 = new ImageIcon(backImage2);
        JButton backButton = new JButton(backIcon1);
        backButton.setRolloverIcon(backIcon2);
        backButton.setPressedIcon(backIcon1);
        backButton.setBounds((int)(15.0 * width / 1280.0), (int)(620.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        this.add(backButton);
        backButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showBuildersGuildScreen();
            }
        });
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("135dlu"), ColumnSpec.decode("pref:grow"), ColumnSpec.decode("1dlu:grow(8)"), ColumnSpec.decode("pref:grow"), ColumnSpec.decode("1dlu:grow(8)"), ColumnSpec.decode("pref:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow")}));
        JPanel housePanel = new JPanel();
        this.add((Component)housePanel, "3, 2, fill, fill");
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
                        InteriorDecorationMenu.this.roomPlanning.getNewRooms().remove(id);
                        RoomInfo roomInfo = (RoomInfo)jComboBox.getSelectedItem();
                        InteriorDecorationMenu.this.roomPlanning.getNewRooms().add(id, roomInfo);
                        jComboBox.setToolTipText(roomInfo.getDescription());
                        InteriorDecorationMenu.this.updateCostLabel();
                    }
                }
                catch (Exception ex) {
                    InteriorDecorationMenu.this.log.error("Error", (Throwable)ex);
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
                InteriorDecorationMenu.this.roomPlanning = null;
                if (house != null) {
                    InteriorDecorationMenu.this.roomPlanning = new RoomPlanning(house);
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
        this.add((Component)controlPanel, "5, 2, fill, fill");
        controlPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.PREF_ROWSPEC, RowSpec.decode("20dlu"), FormFactory.PREF_ROWSPEC, RowSpec.decode("20dlu"), FormFactory.PREF_ROWSPEC, RowSpec.decode("default:grow")}));
        this.costLabel = new JLabel("Cost changes: 0");
        controlPanel.add((Component)this.costLabel, "1, 1");
        JButton btnReset = new JButton("Reset");
        controlPanel.add((Component)btnReset, "1, 3");
        btnReset.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (InteriorDecorationMenu.this.roomPlanning != null) {
                    InteriorDecorationMenu.this.roomPlanning.reset();
                    int index = houseSelectBox.getSelectedIndex();
                    houseSelectBox.setSelectedIndex(0);
                    houseSelectBox.setSelectedIndex(index);
                    InteriorDecorationMenu.this.updateCostLabel();
                }
            }
        });
        JButton btnBuyChanges = new JButton("Buy changes");
        controlPanel.add((Component)btnBuyChanges, "1, 5");
        btnBuyChanges.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (InteriorDecorationMenu.this.roomPlanning != null && Jasbro.getInstance().getData().canAfford(InteriorDecorationMenu.this.roomPlanning.getCosts())) {
                    InteriorDecorationMenu.this.roomPlanning.adoptRoomLayout();
                    InteriorDecorationMenu.this.updateCostLabel();
                }
            }
        });
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Jasbro.getInstance().getGui().showBuildersGuildScreen();
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

