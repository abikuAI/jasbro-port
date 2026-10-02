/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.Item;
import jasbro.game.items.UsableItem;
import jasbro.util.itemEditor.ItemEditor;
import java.awt.Component;
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

public class ItemListPanel
extends JPanel {
    private JTextField textField;
    private JList<Item> itemList;

    public ItemListPanel(final ItemEditor itemEditor) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        JPanel newItemPanel = new JPanel();
        this.add((Component)newItemPanel, "1, 1, fill, fill");
        newItemPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        this.textField = new JTextField();
        newItemPanel.add((Component)this.textField, "1, 1, fill, default");
        this.textField.setColumns(10);
        JButton btnCreateNewItem = new JButton("Create new Item");
        btnCreateNewItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String itemId = ItemListPanel.this.textField.getText().trim();
                if (itemId != null && !itemId.equals("") && !Jasbro.getInstance().getItems().containsKey(itemId)) {
                    UsableItem item = new UsableItem(itemId);
                    Jasbro.getInstance().getItems().put(itemId, item);
                    itemEditor.setItem(item);
                    ItemListPanel.this.updateList();
                }
            }
        });
        newItemPanel.add((Component)btnCreateNewItem, "2, 1");
        JScrollPane scrollPane = new JScrollPane();
        this.add((Component)scrollPane, "1, 2, fill, fill");
        this.itemList = new JList();
        scrollPane.setViewportView(this.itemList);
        this.itemList.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                itemEditor.setItem((Item)ItemListPanel.this.itemList.getSelectedValue());
            }
        });
        this.updateList();
    }

    public void updateList() {
        Item[] itemArray = new Item[Jasbro.getInstance().getItems().entrySet().size()];
        itemArray = Jasbro.getInstance().getItems().values().toArray(itemArray);
        Arrays.sort(itemArray, new Comparator<Item>(){

            @Override
            public int compare(Item o1, Item o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 != null) {
                    return o1.getId().compareTo(o2.getId());
                }
                return o2.getId().compareTo(null);
            }
        });
        this.itemList.setListData((Item[])itemArray);
        this.validate();
        this.repaint();
    }
}

