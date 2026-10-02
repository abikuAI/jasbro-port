package jasbro.gui.objects.div;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.UsableItem;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

public class InventoryPanel extends TranslucentPanel {
   private Inventory inventory;
   private JList<Inventory.ItemData> itemList;
   private boolean imageOnly;
   private boolean resized = false;
   private Item displayedItem;

   private InventoryPanel() {
      this(null, false);
   }

   public InventoryPanel(Inventory inventory) {
      this(inventory, false);
   }

   public InventoryPanel(Inventory inventory, boolean imageOnlyTmp) {
      this.imageOnly = imageOnlyTmp;
      this.inventory = inventory;
      this.setLayout(new GridLayout(0, 1, 0, 0));
      JScrollPane scrollPane = new JScrollPane();
      this.add(scrollPane);
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      this.itemList = new JList<Inventory.ItemData>() {
         @Override
         public Point getToolTipLocation(MouseEvent event) {
            return !InventoryPanel.this.imageOnly
               ? super.getToolTipLocation(event)
               : new Point(event.getComponent().getX() - 600, event.getComponent().getY() - 100);
         }

         @Override
         public JToolTip createToolTip() {
            JToolTip tooltip = super.createToolTip();
            if (InventoryPanel.this.displayedItem != null && InventoryPanel.this.displayedItem instanceof UsableItem) {
               tooltip.setBackground(new Color(24, 219, 92));
            }

            return tooltip;
         }

         @Override
         public int locationToIndex(Point location) {
            int index = super.locationToIndex(location);
            return index != -1 && !this.getCellBounds(index, index).contains(location) ? -1 : index;
         }
      };
      this.itemList.setOpaque(false);
      if (this.imageOnly) {
         this.itemList.setLayoutOrientation(2);
         this.itemList.setVisibleRowCount(-1);
      }

      scrollPane.setViewportView(this.itemList);
      this.itemList.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseMoved(MouseEvent e) {
            ListModel<Inventory.ItemData> m = InventoryPanel.this.itemList.getModel();
            int index = InventoryPanel.this.itemList.locationToIndex(e.getPoint());
            if (index > -1) {
               Item item = m.getElementAt(index).getItem();
               InventoryPanel.this.displayedItem = item;
               if (item.getDescription() != null) {
                  InventoryPanel.this.itemList.setToolTipText(TextUtil.htmlItem(item));
               } else {
                  InventoryPanel.this.itemList.setToolTipText("");
               }
            } else {
               InventoryPanel.this.itemList.setToolTipText("");
            }
         }
      });
      this.itemList
         .setCellRenderer(
            new DefaultListCellRenderer() {
               @Override
               public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                  if (!InventoryPanel.this.imageOnly) {
                     Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                     ((JComponent)component).setOpaque(false);
                     Inventory.ItemData itemData = (Inventory.ItemData)value;
                     JPanel panel = new JPanel();
                     panel.setLayout(
                        new FormLayout(
                           new ColumnSpec[]{ColumnSpec.decode("15dlu:none"), ColumnSpec.decode("pref:grow(4)")}, new RowSpec[]{RowSpec.decode("15dlu:none")}
                        )
                     );
                     panel.setOpaque(false);
                     panel.add(new MyImage(itemData.getItem().getIcon()), "1, 1, fill, fill");
                     panel.add(component, "2, 1, fill, fill");
                     return panel;
                  }

                  Inventory.ItemData itemData = (Inventory.ItemData)value;
                  MyImage image = new MyImage(itemData.getItem().getIcon());
                  if (itemData.getAmount() > 1) {
                     image.setLayout(
                        new FormLayout(
                           new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")},
                           new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow")}
                        )
                     );
                     JLabel amountLabel = new JLabel("" + itemData.getAmount());
                     amountLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
                     amountLabel.setBackground(Color.WHITE);
                     amountLabel.setOpaque(true);
                     image.add(amountLabel, "2, 2, right, bottom");
                  }

                  Border border = null;
                  if (cellHasFocus) {
                     if (isSelected) {
                        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                     }

                     if (border == null) {
                        border = UIManager.getBorder("List.focusCellHighlightBorder");
                     }
                  } else {
                     border = new EmptyBorder(1, 1, 1, 1);
                  }

                  image.setBorder(border);
                  return image;
               }
            }
         );
      this.itemList.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            if (InventoryPanel.this.imageOnly && !InventoryPanel.this.resized) {
               int width = InventoryPanel.this.itemList.getWidth() - 10;
               InventoryPanel.this.itemList.setFixedCellHeight(width / 4);
               InventoryPanel.this.itemList.setFixedCellWidth(width / 4);
               InventoryPanel.this.resized = true;
            }
         }
      });
      this.updateView();
   }

   public void updateView() {
      this.resized = false;
      Item selectedItem = null;
      if (this.itemList.getSelectedValue() != null) {
         selectedItem = this.itemList.getSelectedValue().getItem();
      }

      Inventory.ItemData[] itemArray = new Inventory.ItemData[this.inventory.getItems().size()];
      this.itemList.setListData(this.inventory.getItems().toArray(itemArray));
      if (selectedItem != null) {
         for (Inventory.ItemData itemData : itemArray) {
            if (itemData.getItem().equals(selectedItem)) {
               this.itemList.setSelectedValue(itemData, true);
               break;
            }
         }
      }

      this.repaint();
   }

   public Item getSelectedItem() {
      return this.itemList.getSelectedValue() != null ? this.itemList.getSelectedValue().getItem() : null;
   }

   public JList<Inventory.ItemData> getItemList() {
      return this.itemList;
   }

   public void addListSelectionListener(ListSelectionListener listener) {
      this.itemList.addListSelectionListener(listener);
   }
}
