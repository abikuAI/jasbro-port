package jasbro.gui.pages.subView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.gui.objects.div.InventoryPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatter;

public class ShopPanel extends JPanel {
   private InventoryPanel playerInventoryPanel;
   private InventoryPanel shopInventoryPanel;
   private JButton sellButton;
   private JButton buyButton;
   private JPanel sellPanel;
   private JPanel buyPanel;
   private JSpinner sellSpinner;
   private JSpinner buySpinner;
   private Inventory playerInventory;
   private Inventory shopInventory;
   private ItemLocation shop;

   public ShopPanel(ItemLocation shop) {
      this.shop = shop;
      this.setOpaque(false);
      this.playerInventory = Jasbro.getInstance().getData().getInventory();
      this.shopInventory = Jasbro.getInstance().getData().getShop().getInventory(shop);
      FormLayout layout = new FormLayout(
         new ColumnSpec[]{
            ColumnSpec.decode("default:grow"),
            ColumnSpec.decode("150dlu"),
            ColumnSpec.decode("default:grow"),
            ColumnSpec.decode("150dlu"),
            ColumnSpec.decode("default:grow")
         },
         new RowSpec[]{RowSpec.decode("50dlu"), RowSpec.decode("default:grow(3)"), RowSpec.decode("50dlu")}
      );
      this.setLayout(layout);
      this.playerInventoryPanel = new InventoryPanel(this.playerInventory);
      this.add(this.playerInventoryPanel, "2, 2, fill, fill");
      this.playerInventoryPanel.getItemList().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (ShopPanel.this.playerInventoryPanel.getSelectedItem() != null) {
               int value = (Integer)ShopPanel.this.sellSpinner.getValue();
               Integer itemAmount = new Integer(ShopPanel.this.playerInventory.getAmount(ShopPanel.this.playerInventoryPanel.getSelectedItem()));
               ShopPanel.this.sellSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), itemAmount, new Integer(1)));
               ShopPanel.this.sellSpinner.setValue(value);
               ShopPanel.this.sellSpinner.setValue(Math.min(value, itemAmount));
               JComponent comp = ShopPanel.this.sellSpinner.getEditor();
               JFormattedTextField field = (JFormattedTextField)comp.getComponent(0);
               DefaultFormatter formatter = (DefaultFormatter)field.getFormatter();
               formatter.setCommitsOnValidEdit(true);
               ShopPanel.this.updateSellButton();
            }
         }
      });
      this.shopInventoryPanel = new InventoryPanel(this.shopInventory);
      this.add(this.shopInventoryPanel, "4, 2, fill, fill");
      this.shopInventoryPanel.getItemList().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (ShopPanel.this.shopInventoryPanel.getSelectedItem() != null) {
               int value = (Integer)ShopPanel.this.buySpinner.getValue();
               Integer itemAmount = ShopPanel.this.shopInventory.getAmount(ShopPanel.this.shopInventoryPanel.getSelectedItem());
               ShopPanel.this.buySpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), itemAmount, new Integer(1)));
               ShopPanel.this.buySpinner.setValue(Math.min(value, itemAmount));
               JComponent comp = ShopPanel.this.buySpinner.getEditor();
               JFormattedTextField field = (JFormattedTextField)comp.getComponent(0);
               DefaultFormatter formatter = (DefaultFormatter)field.getFormatter();
               formatter.setCommitsOnValidEdit(true);
               ShopPanel.this.updateBuyButton();
            }
         }
      });
      this.sellPanel = new JPanel();
      this.sellPanel.setOpaque(false);
      this.add(this.sellPanel, "2, 3, fill, fill");
      this.sellPanel
         .setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("right:default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:default:grow")},
               new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:default"), RowSpec.decode("default:grow")}
            )
         );
      this.sellSpinner = new JSpinner();
      this.sellSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
      this.sellSpinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            ShopPanel.this.updateSellButton();
         }
      });
      this.sellPanel.add(this.sellSpinner, "1, 2");
      this.sellButton = new JButton("Sell");
      this.sellPanel.add(this.sellButton, "3, 2, left, top");
      this.sellButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Item item = ShopPanel.this.playerInventoryPanel.getSelectedItem();
            if (item != null) {
               int amount = (Integer)ShopPanel.this.sellSpinner.getValue();
               if (ShopPanel.this.playerInventory.getAmount(item) >= amount) {
                  Jasbro.getInstance().getData().earnMoney(item.getValue() / 2 * amount, item.getName());
                  ShopPanel.this.playerInventory.removeItems(item, amount);
                  ShopPanel.this.shopInventory.addItems(item, amount);
                  ShopPanel.this.updateLists();
               }
            }
         }
      });
      this.sellButton.setEnabled(false);
      this.buyPanel = new JPanel();
      this.buyPanel.setOpaque(false);
      this.add(this.buyPanel, "4, 3, fill, fill");
      this.buyPanel
         .setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("right:default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:default:grow")},
               new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:default"), RowSpec.decode("default:grow")}
            )
         );
      this.buySpinner = new JSpinner();
      this.buySpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
      this.buySpinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            ShopPanel.this.updateBuyButton();
         }
      });
      this.buyPanel.add(this.buySpinner, "1, 2");
      this.buyButton = new JButton("Buy");
      this.buyPanel.add(this.buyButton, "3, 2, left, top");
      this.buyButton
         .addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  Item item = ShopPanel.this.shopInventoryPanel.getSelectedItem();
                  int amount = (Integer)ShopPanel.this.buySpinner.getValue();
                  int discount = 100;
                  if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSHOPS)) {
                     discount = 75;
                  }

                  if (ShopPanel.this.shopInventory.getAmount(item) >= amount
                     && Jasbro.getInstance().getData().getMoney() >= amount * item.getValue() * discount / 100) {
                     Jasbro.getInstance().getData().spendMoney(item.getValue() * amount * discount / 100, item.getName());
                     ShopPanel.this.shopInventory.removeItems(item, amount);
                     ShopPanel.this.playerInventory.addItems(item, amount);
                     ShopPanel.this.updateLists();
                  }
               }
            }
         );
      this.buyButton.setEnabled(false);
      MouseAdapter mouseAdapter = new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (SwingUtilities.isRightMouseButton(e)) {
               JList<Inventory.ItemData> itemList = ShopPanel.this.playerInventoryPanel.getItemList();
               int index = itemList.locationToIndex(e.getPoint());
               itemList.setSelectedIndex(index);
               ShopPanel.this.sellButton.doClick();
            }
         }
      };
      this.playerInventoryPanel.getItemList().addMouseListener(mouseAdapter);
      MouseAdapter mouseAdapter2 = new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (SwingUtilities.isRightMouseButton(e)) {
               JList<Inventory.ItemData> itemList = ShopPanel.this.shopInventoryPanel.getItemList();
               int index = itemList.locationToIndex(e.getPoint());
               itemList.setSelectedIndex(index);
               ShopPanel.this.buyButton.doClick();
            }
         }
      };
      this.shopInventoryPanel.getItemList().addMouseListener(mouseAdapter2);
   }

   public void updateLists() {
      this.playerInventoryPanel.updateView();
      this.shopInventoryPanel.updateView();
   }

   public void updateBuyButton() {
      Item item = this.shopInventoryPanel.getSelectedItem();
      int amount = (Integer)this.buySpinner.getValue();
      int discount = 100;
      if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSHOPS)) {
         discount = 75;
      }

      if (item != null) {
         this.buyButton.setText("Buy (" + this.shopInventoryPanel.getSelectedItem().getValue() * amount * discount / 100 + ")");
         if (item.getValue() * amount <= Jasbro.getInstance().getData().getMoney()) {
            this.buyButton.setEnabled(true);
         } else {
            this.buyButton.setEnabled(false);
         }
      } else {
         this.buyButton.setText("Buy");
         this.buyButton.setEnabled(false);
      }

      this.repaint();
   }

   public void updateSellButton() {
      Item item = this.playerInventoryPanel.getSelectedItem();
      if (item != null) {
         if (item.getValue() != 0) {
            int amount = (Integer)this.sellSpinner.getValue();
            this.sellButton.setText("Sell (" + item.getValue() / 2 * amount + ")");
            this.sellButton.setEnabled(true);
         }
      } else {
         this.sellButton.setText("Sell");
         this.sellButton.setEnabled(false);
      }

      this.repaint();
   }

   public ItemLocation getShop() {
      return this.shop;
   }
}
