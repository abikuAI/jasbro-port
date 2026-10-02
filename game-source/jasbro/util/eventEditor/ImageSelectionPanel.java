package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.Item;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TPath;

public class ImageSelectionPanel extends JPanel {
   private ImageSelection imageSelection;
   private JTextField textField;
   private JTextField textField_1;
   private JPanel backgroundPanel;
   private JTabbedPane tabbedPane;
   private MyImage previewImage;
   private JComboBox<ImageTag> imageTagComboBox;
   private JPanel imageByNamePanel;
   private WorldEvent worldEvent;
   private Item item;
   private ActionListener deleteActionListener;

   public ImageSelectionPanel(Item item) {
      this.item = item;
      if (item.getImageSelection() != null) {
         this.init(item.getImageSelection());
      }
   }

   public ImageSelectionPanel(WorldEvent worldEvent, ImageSelection imageSelectionTmp, ActionListener deleteActionListener) {
      this.worldEvent = worldEvent;
      this.deleteActionListener = deleteActionListener;
      this.init(imageSelectionTmp);
   }

   public void init(ImageSelection imageSelectionTmp) {
      this.imageSelection = imageSelectionTmp;
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("50dlu:grow"), FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC}
         )
      );
      this.imageByNamePanel = new JPanel();
      if (this.item != null) {
         this.add(this.imageByNamePanel, "1, 1, fill, fill");
      } else {
         this.tabbedPane = new JTabbedPane(1);
         this.add(this.tabbedPane, "1, 1, fill, fill");
         this.tabbedPane.addTab("Image by name", null, this.imageByNamePanel, null);
      }

      this.imageByNamePanel
         .setLayout(
            new FormLayout(
               new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")},
               new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}
            )
         );
      JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.image"));
      this.imageByNamePanel.add(lblNewLabel_1, "1, 1, right, default");
      final JComboBox<String> targetImageComboBox = new JComboBox<>();
      this.imageByNamePanel.add(targetImageComboBox, "2, 1, fill, default");
      targetImageComboBox.setPreferredSize(new Dimension(650, 25));
      targetImageComboBox.setPrototypeDisplayValue("a");
      targetImageComboBox.setEditable(true);
      targetImageComboBox.setSelectedItem(this.imageSelection.getImage());
      targetImageComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ImageSelectionPanel.this.imageSelection.setImage((String)targetImageComboBox.getSelectedItem());
            if (ImageSelectionPanel.this.previewImage != null) {
               try {
                  if (ImageSelectionPanel.this.item != null) {
                     ImageSelectionPanel.this.previewImage.setImage(ImageSelectionPanel.this.imageSelection.getImageData(ImageSelectionPanel.this.item));
                  } else {
                     ImageSelectionPanel.this.previewImage.setImage(ImageSelectionPanel.this.imageSelection.getImageData(ImageSelectionPanel.this.worldEvent));
                  }

                  ImageSelectionPanel.this.validate();
                  ImageSelectionPanel.this.repaint();
               } catch (Exception ex) {
               }
            }
         }
      });
      JLabel lblNewLabel_2 = new JLabel(TextUtil.t("eventEditor.imageLocation"));
      this.imageByNamePanel.add(lblNewLabel_2, "1, 2, right, default");
      final JComboBox<ImageSelection.ImageLocation> comboBox = new JComboBox<>();
      this.imageByNamePanel.add(comboBox, "2, 2, fill, default");

      for (ImageSelection.ImageLocation imageLocation : ImageSelection.ImageLocation.values()) {
         comboBox.addItem(imageLocation);
      }

      comboBox.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ImageSelectionPanel.this.imageSelection.setImageLocation((ImageSelection.ImageLocation)comboBox.getSelectedItem());
               Jasbro.getThreadpool()
                  .execute(
                     new Runnable() {
                        @Override
                        public void run() {
                           String selectedItem = (String)targetImageComboBox.getSelectedItem();
                           targetImageComboBox.removeAllItems();
                           targetImageComboBox.addItem(selectedItem);
                           TFile parentFolder;
                           if (ImageSelectionPanel.this.imageSelection.getImageLocation() == ImageSelection.ImageLocation.LOCAL) {
                              if (ImageSelectionPanel.this.worldEvent != null) {
                                 parentFolder = ImageSelectionPanel.this.worldEvent.getFile().getParentFile();
                              } else {
                                 parentFolder = ImageSelectionPanel.this.item.getFile().getParentFile();
                              }
                           } else {
                              parentFolder = new TFile("images");
                           }

                           List<String> images;
                           if (ImageSelectionPanel.this.item != null
                              && ImageSelectionPanel.this.imageSelection.getImageLocation() == ImageSelection.ImageLocation.GLOBAL) {
                              images = ImageSelectionPanel.this.listImages(parentFolder, new TFile("images/icons/items"), 10);
                           } else {
                              images = ImageSelectionPanel.this.listImages(parentFolder, parentFolder, 10);
                           }

                           for (String image : images) {
                              targetImageComboBox.addItem(image);
                           }

                           ImageSelectionPanel.this.validate();
                           ImageSelectionPanel.this.repaint();
                        }
                     }
                  );
            }
         }
      );
      comboBox.setSelectedItem(this.imageSelection.getImageLocation());
      this.previewImage = new MyImage();
      this.validate();
      if (this.item == null) {
         this.imageByNamePanel.add(this.previewImage, "3, 1, 1, 2 fill, fill");
      } else {
         this.imageByNamePanel.add(this.previewImage, "1, 3, 2, 1, fill, fill");
      }

      if (this.item == null) {
         JPanel imageByCharacterPanel = new JPanel();
         this.tabbedPane.addTab("Image from character", null, imageByCharacterPanel, null);
         imageByCharacterPanel.setLayout(
            new FormLayout(
               new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")},
               new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}
            )
         );
         JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.targetCharacter"));
         imageByCharacterPanel.add(lblNewLabel, "1, 1, right, default");
         this.textField = new JTextField();
         imageByCharacterPanel.add(this.textField, "2, 1, fill, default");
         this.textField.setText(this.imageSelection.getTarget());
         this.textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               ImageSelectionPanel.this.imageSelection.setTarget(ImageSelectionPanel.this.textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               ImageSelectionPanel.this.imageSelection.setTarget(ImageSelectionPanel.this.textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               ImageSelectionPanel.this.imageSelection.setTarget(ImageSelectionPanel.this.textField.getText());
            }
         });
         JLabel lblNewLabel_4 = new JLabel(TextUtil.t("imagetag"));
         imageByCharacterPanel.add(lblNewLabel_4, "1, 2, right, default");
         this.imageTagComboBox = new JComboBox<>();
         imageByCharacterPanel.add(this.imageTagComboBox, "2, 2, fill, default");

         for (ImageTag imageTag : ImageTag.values()) {
            this.imageTagComboBox.addItem(imageTag);
         }

         this.imageTagComboBox.setSelectedItem(this.imageSelection.getImageTags().get(0));
         this.imageTagComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ImageSelectionPanel.this.imageSelection.getImageTags().remove(0);
               ImageSelectionPanel.this.imageSelection.getImageTags().add(0, (ImageTag)ImageSelectionPanel.this.imageTagComboBox.getSelectedItem());
            }
         });
         this.backgroundPanel = new JPanel();
         this.tabbedPane.addTab(TextUtil.t("eventEditor.characterBackground"), null, this.backgroundPanel, null);
         this.backgroundPanel
            .setLayout(
               new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC})
            );
         this.tabbedPane.addChangeListener(new ImageSelectionPanel.MyChangeListener(this.backgroundPanel));
         JLabel lblNewLabel_3 = new JLabel(TextUtil.t("eventEditor.targetCharacter"));
         this.backgroundPanel.add(lblNewLabel_3, "1, 1, right, default");
         this.textField_1 = new JTextField();
         this.backgroundPanel.add(this.textField_1, "2, 1, fill, default");
         this.textField_1.setText(this.imageSelection.getTarget());
         this.textField_1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               ImageSelectionPanel.this.imageSelection.setTarget(ImageSelectionPanel.this.textField_1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               ImageSelectionPanel.this.imageSelection.setTarget(ImageSelectionPanel.this.textField_1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               ImageSelectionPanel.this.imageSelection.setTarget(ImageSelectionPanel.this.textField_1.getText());
            }
         });
         if (this.imageSelection.isBackground()) {
            this.tabbedPane.setSelectedComponent(this.backgroundPanel);
         } else if (this.imageSelection.getImage() == null) {
            this.tabbedPane.setSelectedIndex(1);
         }
      }

      if (this.deleteActionListener != null) {
         JButton deleteButton = new JButton(TextUtil.t("eventEditor.delete"));
         deleteButton.setForeground(Color.RED);
         this.add(deleteButton, "1, 2, left, default");
         deleteButton.addActionListener(this.deleteActionListener);
      }
   }

   private List<String> listImages(TFile origFolder, TFile curFolder, int depth) {
      TFile[] files = curFolder.listFiles();
      List<String> images = new ArrayList<>();

      for (TFile curFile : files) {
         if (ImageUtil.getInstance().isImage(curFile)) {
            String relativePath = new TPath(origFolder).relativize(new TPath(curFile)).toString().replace('\\', '/');
            images.add(relativePath);
         } else if (curFile.isDirectory() && depth > 0) {
            images.addAll(this.listImages(origFolder, curFile, depth - 1));
         }
      }

      return images;
   }

   private class MyChangeListener implements ChangeListener {
      private JPanel backgroundPanel;

      public MyChangeListener(JPanel backgroundPanel) {
         this.backgroundPanel = backgroundPanel;
      }

      @Override
      public void stateChanged(ChangeEvent e) {
         if (ImageSelectionPanel.this.tabbedPane.getSelectedComponent() != ImageSelectionPanel.this.imageByNamePanel) {
            ImageSelectionPanel.this.imageSelection.setImage(null);
         }

         if (ImageSelectionPanel.this.tabbedPane.getSelectedComponent() == this.backgroundPanel) {
            ImageSelectionPanel.this.imageSelection.setBackground(true);
         } else {
            ImageSelectionPanel.this.imageSelection.setBackground(false);
         }
      }
   }
}
