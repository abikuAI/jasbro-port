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

public class RealEstatePanel extends JPanel {
   private List<JButton> buyButtons = new ArrayList<>();
   private List<House> houses;

   public RealEstatePanel() {
      this.init();
   }

   public void init() {
      this.removeAll();
      this.houses = Jasbro.getInstance().getData().getHouses();
      this.setOpaque(false);
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               ColumnSpec.decode("pref:grow"),
               ColumnSpec.decode("pref:grow(10)"),
               ColumnSpec.decode("pref:grow"),
               ColumnSpec.decode("pref:grow(10)"),
               ColumnSpec.decode("pref:grow"),
               ColumnSpec.decode("pref:grow(8)"),
               ColumnSpec.decode("pref:grow")
            },
            new RowSpec[]{RowSpec.decode("20dlu"), RowSpec.decode("1dlu:grow"), RowSpec.decode("20dlu")}
         )
      );
      this.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
         @Override
         public void ancestorResized(HierarchyEvent e) {
            RealEstatePanel.this.revalidate();
         }
      });
      JPanel buyHousePanel = new JPanel();
      buyHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      buyHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
      buyHousePanel.setOpaque(true);
      this.add(buyHousePanel, "2, 2, fill, fill");
      buyHousePanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")})
      );
      JLabel lblBuyHouse = new JLabel("Buy Houses");
      lblBuyHouse.setFont(new Font("Tahoma", 1, 15));
      buyHousePanel.add(lblBuyHouse, "1, 1, center, default");
      ActionListener myBuyListener = new ActionListener() {
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

      for (int i = 0; i < houseTypes.size(); i++) {
         HouseType type = houseTypes.get(i);
         JPanel curHousePanel = new JPanel();
         curHousePanel.setOpaque(false);
         curHousePanel.setLayout(new GridLayout(1, 1));
         curHousePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
         formLayout.insertRow(i + 2, RowSpec.decode("pref:none"));
         buyHousePanel.add(curHousePanel, "1," + (i + 2) + ", fill, top");
         House house = HouseUtil.newHouse(type);
         Object[] arguments = new Object[]{type.getText(), house.getValue()};
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

      buyHousePanel = new JPanel();
      buyHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      buyHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
      buyHousePanel.setOpaque(true);
      this.add(buyHousePanel, "4, 2, fill, fill");
      buyHousePanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")})
      );
      lblBuyHouse = new JLabel("Sell Houses");
      lblBuyHouse.setFont(new Font("Tahoma", 1, 15));
      buyHousePanel.add(lblBuyHouse, "1, 1, center, default");
      FormLayout formLayoutx = (FormLayout)buyHousePanel.getLayout();

      for (int i = 0; i < this.houses.size(); i++) {
         House house = this.houses.get(i);
         JPanel curHousePanel = new JPanel();
         curHousePanel.setOpaque(false);
         curHousePanel.setLayout(new GridLayout(1, 1));
         curHousePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
         formLayoutx.insertRow(i + 2, RowSpec.decode("pref:none"));
         buyHousePanel.add(curHousePanel, "1," + (i + 2) + ", fill, top");
         Object[] arguments = new Object[]{house.getName(), house.getSellPrice()};
         JButton sellButton = new JButton(TextUtil.t("ui.realestate.sellhouse", arguments));
         sellButton.addActionListener(new RealEstatePanel.MySellListener(house));
         sellButton.setEnabled(this.houses.size() > 1);
         curHousePanel.add(sellButton);
      }

      buyHousePanel = new JPanel();
      buyHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      buyHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
      buyHousePanel.setOpaque(true);
      this.add(buyHousePanel, "6, 2, fill, fill");
      buyHousePanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")})
      );
      lblBuyHouse = new JLabel("Build Houses");
      lblBuyHouse.setFont(new Font("Tahoma", 1, 15));
      buyHousePanel.add(lblBuyHouse, "1, 1, center, default");
      myBuyListener = new ActionListener() {
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
      formLayout = (FormLayout)buyHousePanel.getLayout();
      houseTypes = Jasbro.getInstance().getData().getUnlocks().getAvailableHouseTypes();

      for (int i = 0; i < houseTypes.size(); i++) {
         HouseType type = houseTypes.get(i);
         JPanel curHousePanel = new JPanel();
         curHousePanel.setOpaque(false);
         curHousePanel.setLayout(new GridLayout(1, 1));
         curHousePanel.setBorder(new EmptyBorder(10, 20, 10, 20));
         formLayout.insertRow(i + 2, RowSpec.decode("pref:none"));
         buyHousePanel.add(curHousePanel, "1," + (i + 2) + ", fill, top");
         House house = HouseUtil.newHouse(type);
         Object[] arguments = new Object[]{house.getHouseType().getText(), house.getValue() * 2};
         JButton builtButton = new JButton(TextUtil.t("ui.realestate.buildhouse", arguments));
         builtButton.addActionListener(myBuyListener);
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
         if (houseType == house.getHouseType()) {
            return true;
         }
      }

      return false;
   }

   public class MySellListener implements ActionListener {
      private House house;

      public MySellListener(House house) {
         this.house = house;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         synchronized (RealEstatePanel.this) {
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
