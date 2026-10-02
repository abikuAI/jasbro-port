/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages.subView;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.HouseUtil;
import jasbro.gui.GuiUtil;
import jasbro.gui.pages.MessageScreen;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class RealEstatePanel
extends JPanel {
    private List<JButton> buyButtons = new ArrayList<JButton>();
    private List<House> houses;

    public RealEstatePanel() {
        this.init();
    }

    public void init() {
        Object[] arguments;
        House house;
        JPanel curHousePanel;
        HouseType type;
        int i;
        this.removeAll();
        this.houses = Jasbro.getInstance().getData().getHouses();
        this.setOpaque(false);
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("pref:grow"), ColumnSpec.decode("pref:grow(10)"), ColumnSpec.decode("pref:grow"), ColumnSpec.decode("pref:grow(10)"), ColumnSpec.decode("pref:grow"), ColumnSpec.decode("pref:grow(8)"), ColumnSpec.decode("pref:grow")}, new RowSpec[]{RowSpec.decode("20dlu"), RowSpec.decode("1dlu:grow"), RowSpec.decode("20dlu")}));
        this.addHierarchyBoundsListener(new HierarchyBoundsAdapter(){

            @Override
            public void ancestorResized(HierarchyEvent e) {
                RealEstatePanel.this.revalidate();
            }
        });
        JPanel buyHousePanel = new JPanel();
        buyHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
        buyHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
        buyHousePanel.setOpaque(true);
        this.add((Component)buyHousePanel, "2, 2, fill, fill");
        buyHousePanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")}));
        JLabel lblBuyHouse = new JLabel("Buy Houses");
        lblBuyHouse.setFont(new Font("Tahoma", 1, 15));
        buyHousePanel.add((Component)lblBuyHouse, "1, 1, center, default");
        ActionListener myBuyListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                HouseType type = HouseType.valueOf(button.getActionCommand());
                if (type != null) {
                    House house = HouseUtil.newHouse(type);
                    if (!RealEstatePanel.this.playerOwnsHouseType(type) && Jasbro.getInstance().getData().canAfford(house.getValue())) {
                        RealEstatePanel.this.houses.add(house);
                        Jasbro.getInstance().getData().spendMoney(house.getValue(), house.getName());
                        button.setEnabled(false);
                        new MessageScreen(house.getName() + " bought!", house.getImage(), null);
                        RealEstatePanel.this.init();
                    }
                }
            }
        };
        FormLayout formLayout = (FormLayout)buyHousePanel.getLayout();
        List<HouseType> houseTypes = Jasbro.getInstance().getData().getUnlocks().getAvailableHouseTypes();
        for (i = 0; i < houseTypes.size(); ++i) {
            type = houseTypes.get(i);
            curHousePanel = new JPanel();
            curHousePanel.setOpaque(false);
            curHousePanel.setLayout(new GridLayout(1, 1));
            curHousePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
            formLayout.insertRow(i + 2, RowSpec.decode("pref:none"));
            buyHousePanel.add((Component)curHousePanel, "1," + (i + 2) + ", fill, top");
            house = HouseUtil.newHouse(type);
            arguments = new Object[]{type.getText(), house.getValue()};
            JButton buyButton = new JButton();
            buyButton.setText(TextUtil.t("ui.realestate.buyhouse", arguments));
            buyButton.addActionListener(myBuyListener);
            buyButton.setActionCommand(type.toString());
            buyButton.setEnabled(!this.playerOwnsHouseType(type));
            if (!Jasbro.getInstance().getData().canAfford(house.getValue())) {
                buyButton.setEnabled(false);
            }
            this.buyButtons.add(buyButton);
            curHousePanel.add(buyButton);
        }
        JPanel sellHousePanel = new JPanel();
        sellHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
        sellHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
        sellHousePanel.setOpaque(true);
        this.add((Component)sellHousePanel, "4, 2, fill, fill");
        sellHousePanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")}));
        JLabel lblSellHouse = new JLabel("Sell Houses");
        lblSellHouse.setFont(new Font("Tahoma", 1, 15));
        sellHousePanel.add((Component)lblSellHouse, "1, 1, center, default");
        FormLayout formLayout2 = (FormLayout)sellHousePanel.getLayout();
        for (int i2 = 0; i2 < this.houses.size(); ++i2) {
            House house2 = this.houses.get(i2);
            JPanel curHousePanel2 = new JPanel();
            curHousePanel2.setOpaque(false);
            curHousePanel2.setLayout(new GridLayout(1, 1));
            curHousePanel2.setBorder(new EmptyBorder(10, 20, 10, 20));
            formLayout2.insertRow(i2 + 2, RowSpec.decode("pref:none"));
            sellHousePanel.add((Component)curHousePanel2, "1," + (i2 + 2) + ", fill, top");
            Object[] arguments2 = new Object[]{house2.getName(), house2.getSellPrice()};
            JButton sellButton = new JButton(TextUtil.t("ui.realestate.sellhouse", arguments2));
            sellButton.addActionListener(new MySellListener(house2));
            sellButton.setEnabled(this.houses.size() > 1);
            curHousePanel2.add(sellButton);
        }
        JPanel buildHousePanel = new JPanel();
        buildHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
        buildHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
        buildHousePanel.setOpaque(true);
        this.add((Component)buildHousePanel, "6, 2, fill, fill");
        buildHousePanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")}));
        lblBuyHouse = new JLabel("Build Houses");
        lblBuyHouse.setFont(new Font("Tahoma", 1, 15));
        buildHousePanel.add((Component)lblBuyHouse, "1, 1, center, default");
        ActionListener myBuildListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                HouseType type = HouseType.valueOf(button.getActionCommand());
                if (type != null) {
                    House house = HouseUtil.newHouse(type);
                    if (Jasbro.getInstance().getData().canAfford(house.getValue() * 2)) {
                        RealEstatePanel.this.houses.add(house);
                        Jasbro.getInstance().getData().spendMoney(house.getValue() * 2, house.getName());
                        new MessageScreen(house.getName() + " built!", house.getImage(), null);
                        RealEstatePanel.this.init();
                    }
                }
            }
        };
        formLayout = (FormLayout)buildHousePanel.getLayout();
        houseTypes = Jasbro.getInstance().getData().getUnlocks().getAvailableHouseTypes();
        for (i = 0; i < houseTypes.size(); ++i) {
            type = houseTypes.get(i);
            curHousePanel = new JPanel();
            curHousePanel.setOpaque(false);
            curHousePanel.setLayout(new GridLayout(1, 1));
            curHousePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
            formLayout.insertRow(i + 2, RowSpec.decode("pref:none"));
            buildHousePanel.add((Component)curHousePanel, "1," + (i + 2) + ", fill, top");
            house = HouseUtil.newHouse(type);
            arguments = new Object[]{house.getHouseType().getText(), house.getValue() * 2};
            JButton builtButton = new JButton(TextUtil.t("ui.realestate.buildhouse", arguments));
            builtButton.addActionListener(myBuildListener);
            builtButton.setActionCommand(type.toString());
            if (!Jasbro.getInstance().getData().canAfford(house.getValue() * 2)) {
                builtButton.setEnabled(false);
            }
            curHousePanel.add(builtButton);
        }
        this.validate();
    }

    public boolean playerOwnsHouseType(HouseType houseType) {
        for (House house : Jasbro.getInstance().getData().getHouses()) {
            if (houseType != house.getHouseType()) continue;
            return true;
        }
        return false;
    }

    public class MySellListener
    implements ActionListener {
        private House house;

        public MySellListener(House house) {
            this.house = house;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            RealEstatePanel realEstatePanel = RealEstatePanel.this;
            synchronized (realEstatePanel) {
                if (RealEstatePanel.this.houses.size() > 0) {
                    this.house.empty();
                    RealEstatePanel.this.houses.remove(this.house);
                    Jasbro.getInstance().getData().earnMoney(this.house.getSellPrice(), this.house.getName());
                    new MessageScreen(this.house.getName() + " sold!", this.house.getImage(), null);
                    RealEstatePanel.this.init();
                }
            }
        }
    }
}

