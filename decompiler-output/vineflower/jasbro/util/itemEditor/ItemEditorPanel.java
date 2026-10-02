package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.Equipment;
import jasbro.game.items.Item;
import jasbro.game.items.ItemFileLoader;
import jasbro.game.items.ItemType;
import jasbro.game.items.LootItem;
import jasbro.game.items.SummoningItem;
import jasbro.game.items.UnlockItem;
import jasbro.game.items.UsableItem;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.gui.objects.div.MyImage;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.ImageSelectionPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ItemEditorPanel extends JPanel {
   private Item item;
   private JTextField textField;
   private MyImage imagePreview;
   private ImageSelectionPanel imageSelection;

   public ItemEditorPanel(Item curItem, final ItemEditor itemEditor) {
      this.item = curItem;
      this.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.MIN_ROWSPEC, RowSpec.decode("default:grow(3)")})
      );
      JPanel baseDataPanel = new JPanel();
      this.add(baseDataPanel, "1, 1, fill, fill");
      FormLayout layout = new FormLayout(
         new ColumnSpec[]{
            ColumnSpec.decode("default:grow"),
            FormFactory.UNRELATED_GAP_COLSPEC,
            ColumnSpec.decode("default:grow"),
            FormFactory.UNRELATED_GAP_COLSPEC,
            ColumnSpec.decode("default:grow")
         },
         new RowSpec[]{RowSpec.decode("default:grow")}
      );
      baseDataPanel.setLayout(layout);
      layout.setColumnGroups(new int[][]{{1, 3, 5}});
      JPanel panel = new JPanel();
      baseDataPanel.add(panel, "1, 1, fill, fill");
      panel.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow")}
         )
      );
      JLabel lblItemName = new JLabel("Item name:");
      panel.add(lblItemName, "1, 1, right, default");
      this.textField = new JTextField();
      this.textField.setText(this.item.getName());
      this.textField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setName(ItemEditorPanel.this.textField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setName(ItemEditorPanel.this.textField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setName(ItemEditorPanel.this.textField.getText());
         }
      });
      panel.add(this.textField, "2, 1, fill, default");
      JLabel lblValue = new JLabel("Value");
      panel.add(lblValue, "1, 2");
      final JSpinner spinner = new JSpinner();
      spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
      spinner.setValue(this.item.getValue());
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            ItemEditorPanel.this.item.setValue((Integer)spinner.getValue());
         }
      });
      panel.add(spinner, "2, 2");
      JButton btnSave = new JButton("Save");
      btnSave.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ItemFileLoader.getInstance().save(ItemEditorPanel.this.item);
         }
      });
      JLabel lblType = new JLabel("Type");
      panel.add(lblType, "1, 3, left, default");
      final JComboBox<ItemType> comboBox = new JComboBox<>();

      for (ItemType itemType : ItemType.values()) {
         comboBox.addItem(itemType);
      }

      comboBox.setSelectedItem(this.item.getType());
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Item newItem;
            if (comboBox.getSelectedItem() == ItemType.EQUIPMENT) {
               newItem = new Equipment(ItemEditorPanel.this.item);
            } else if (comboBox.getSelectedItem() == ItemType.UNLOCK) {
               newItem = new UnlockItem(ItemEditorPanel.this.item);
            } else if (comboBox.getSelectedItem() == ItemType.SUMMONING) {
               newItem = new SummoningItem(ItemEditorPanel.this.item);
            } else if (comboBox.getSelectedItem() == ItemType.LOOT) {
               newItem = new LootItem(ItemEditorPanel.this.item);
            } else {
               newItem = new UsableItem(ItemEditorPanel.this.item);
            }

            Jasbro.getInstance().getItems().remove(ItemEditorPanel.this.item.getId());
            Jasbro.getInstance().getItems().put(newItem.getId(), newItem);
            itemEditor.setItem(newItem);
         }
      });
      panel.add(comboBox, "2, 3, fill, default");
      panel.add(btnSave, "1, 4");
      JButton btnDelete = new JButton("Delete");
      panel.add(btnDelete, "2, 4, center, default");
      btnDelete.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(ItemEditorPanel.this, "Delete Item?", "Delete", 0);
            if (confirm == 0) {
               ItemFileLoader.getInstance().delete(ItemEditorPanel.this.item);
               itemEditor.setItem(null);
            }
         }
      });
      JTabbedPane descriptionTabbedPane = new JTabbedPane(1);
      baseDataPanel.add(descriptionTabbedPane, "3, 1, fill, fill");
      JPanel panel_1 = new JPanel();
      descriptionTabbedPane.addTab("Ingame description", null, panel_1, null);
      panel_1.setLayout(
         new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")})
      );
      JLabel lblDescription = new JLabel(TextUtil.htmlPreformatted("Description\n(ingame)"));
      panel_1.add(lblDescription, "1, 1");
      final JTextArea textArea = new JTextArea();
      JScrollPane scrollPane = new JScrollPane(textArea);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setText(this.item.getDescription());
      textArea.setEditable(true);
      textArea.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setDescription(textArea.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setDescription(textArea.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setDescription(textArea.getText());
         }
      });
      panel_1.add(scrollPane, "2, 1, fill, fill");
      panel_1 = new JPanel();
      descriptionTabbedPane.addTab("Author description", null, panel_1, null);
      panel_1.setLayout(
         new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")})
      );
      lblDescription = new JLabel(TextUtil.htmlPreformatted("Author\ndescription"));
      panel_1.add(lblDescription, "1, 1");
      textArea = new JTextArea();
      scrollPane = new JScrollPane(textArea);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setText(this.item.getAuthorDescription());
      textArea.setEditable(true);
      textArea.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setAuthorDescription(textArea.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setAuthorDescription(textArea.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            ItemEditorPanel.this.item.setAuthorDescription(textArea.getText());
         }
      });
      panel_1.add(scrollPane, "2, 1, fill, fill");
      panel_1 = new JPanel();
      baseDataPanel.add(panel_1, "5, 1, fill, fill");
      panel_1.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("left:default:grow")},
            new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow"), RowSpec.decode("default:grow")}
         )
      );
      final JCheckBox chckbxDefaultImage = new JCheckBox("Default Image");
      panel_1.add(chckbxDefaultImage, "1, 1");
      if (this.item.getImageSelection() == null) {
         chckbxDefaultImage.setSelected(true);
      }

      chckbxDefaultImage.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (chckbxDefaultImage.isSelected()) {
               ItemEditorPanel.this.imagePreview.setVisible(true);
               ItemEditorPanel.this.imageSelection.setVisible(false);
               ItemEditorPanel.this.item.setImageSelection(null);
            } else {
               ItemEditorPanel.this.imagePreview.setVisible(false);
               ItemEditorPanel.this.item.setImageSelection(new ImageSelection());
               ItemEditorPanel.this.item.getImageSelection().setImageLocation(ImageSelection.ImageLocation.GLOBAL);
               ItemEditorPanel.this.imageSelection.init(ItemEditorPanel.this.item.getImageSelection());
               ItemEditorPanel.this.imageSelection.setVisible(true);
            }

            ItemEditorPanel.this.validate();
            ItemEditorPanel.this.repaint();
         }
      });
      this.imagePreview = new MyImage(this.item.getIcon());
      panel_1.add(this.imagePreview, "1, 2, fill, fill");
      if (this.item.getImageSelection() != null) {
         this.imagePreview.setVisible(false);
      }

      this.imageSelection = new ImageSelectionPanel(this.item);
      panel_1.add(this.imageSelection, "1, 3, fill, fill");
      if (this.item.getImageSelection() == null) {
         this.imageSelection.setVisible(false);
      }

      if (this.item instanceof Equipment) {
         EquipmentEditorPanel usableItemEditorPanel = new EquipmentEditorPanel((Equipment)this.item);
         this.add(usableItemEditorPanel, "1, 2, fill, fill");
      } else if (this.item instanceof UnlockItem) {
         UnlockItemEditorPanel usableItemEditorPanel = new UnlockItemEditorPanel((UnlockItem)this.item);
         this.add(usableItemEditorPanel, "1, 2, fill, fill");
      } else if (this.item instanceof SummoningItem) {
         SummoningItemEditorPanel summoningItemEditorPanel = new SummoningItemEditorPanel((SummoningItem)this.item);
         this.add(summoningItemEditorPanel, "1, 2, fill, fill");
      } else if (this.item instanceof LootItem) {
         LootItemEditorPanel lootItemEditorPanel = new LootItemEditorPanel((LootItem)this.item);
         this.add(lootItemEditorPanel, "1, 2, fill, fill");
      } else {
         UsableItemEditorPanel usableItemEditorPanel = new UsableItemEditorPanel((UsableItem)this.item);
         this.add(usableItemEditorPanel, "1, 2, fill, fill");
      }
   }
}
