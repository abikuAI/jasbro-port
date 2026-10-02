package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.Item;
import jasbro.game.items.UsableItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ItemListPanel extends JPanel {
   private JTextField textField;
   private JList<Item> itemList;

   public ItemListPanel(final ItemEditor itemEditor) {
      this.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")})
      );
      JPanel newItemPanel = new JPanel();
      this.add(newItemPanel, "1, 1, fill, fill");
      newItemPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC})
      );
      this.textField = new JTextField();
      newItemPanel.add(this.textField, "1, 1, fill, default");
      this.textField.setColumns(10);
      JButton btnCreateNewItem = new JButton("Create new Item");
      btnCreateNewItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String itemId = ItemListPanel.this.textField.getText().trim();
            if (itemId != null && !itemId.equals("") && !Jasbro.getInstance().getItems().containsKey(itemId)) {
               Item item = new UsableItem(itemId);
               Jasbro.getInstance().getItems().put(itemId, item);
               itemEditor.setItem(item);
               ItemListPanel.this.updateList();
            }
         }
      });
      newItemPanel.add(btnCreateNewItem, "2, 1");
      JScrollPane scrollPane = new JScrollPane();
      this.add(scrollPane, "1, 2, fill, fill");
      this.itemList = new JList<>();
      scrollPane.setViewportView(this.itemList);
      this.itemList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            itemEditor.setItem(ItemListPanel.this.itemList.getSelectedValue());
         }
      });
      this.updateList();
   }

   public void updateList() {
      Item[] itemArray = new Item[Jasbro.getInstance().getItems().entrySet().size()];
      itemArray = Jasbro.getInstance().getItems().values().toArray(itemArray);
      Arrays.sort(itemArray, new Comparator<Item>() {
         public int compare(Item o1, Item o2) {
            if (o1 == null && o2 == null) {
               return 0;
            } else {
               return o1 != null ? o1.getId().compareTo(o2.getId()) : o2.getId().compareTo((String)null);
            }
         }
      });
      this.itemList.setListData(itemArray);
      this.validate();
      this.repaint();
   }
}
