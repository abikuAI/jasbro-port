package jasbro.util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterFileLoader;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageUtil;
import jasbro.util.enemyEditor.EnemyEditorPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CharacterEditor extends JFrame implements EditorInterface {
   private static final Logger log = LogManager.getLogger(CharacterEditor.class);
   private CharacterDataEditorPanel attributeEditorPanel;
   private JList<CharacterBase> characterList;
   private JTextField characterNameField;
   private ImageTagEditor imageTagEditor;
   private ImageListPanel imageListPanel;
   private List<CharacterBase> characters;
   private CharacterBase currentCharacter;
   private ImageData currentImage;
   private MyImage imageDisplay;
   private JFileChooser fileChooser;
   private boolean warningDisplayed = false;
   private JTabbedPane tabbedPane;
   private JPanel characterEditorPanel;

   public CharacterEditor() {
      Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
         @Override
         public void uncaughtException(Thread t, Throwable e) {
            CharacterEditor.log.error("Uncaught Exception", e);
         }
      });
      ToolTipManager.sharedInstance().setDismissDelay(10000);
      ToolTipManager.sharedInstance().setInitialDelay(100);
      this.setExtendedState(this.getExtendedState() | 6);
      CharacterFileLoader loader = CharacterFileLoader.getInstance();
      this.characters = loader.loadAllCharacters(true);
      Collections.sort(this.characters, new Comparators.CharacterBaseFolderComparator());
      Jasbro.getInstance().setCharacterBases(this.characters);
      this.initComponents();
      this.characterList
         .setCellRenderer(
            new ListCellRenderer<CharacterBase>() {
               private BasicComboBoxRenderer renderer = new BasicComboBoxRenderer();

               public Component getListCellRendererComponent(
                  JList<? extends CharacterBase> list, CharacterBase value, int index, boolean isSelected, boolean cellHasFocus
               ) {
                  BasicComboBoxRenderer component = (BasicComboBoxRenderer)this.renderer
                     .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                  component.setText(value.getId());
                  return component;
               }
            }
         );
      this.characterList.setSelectedValue(this.characters.get(0), true);
      this.repaint();
   }

   @Override
   public void changeCurrentImage(ImageData image) {
      try {
         this.currentImage = image;
         this.imageTagEditor.setImage(image, this.currentCharacter);
         this.imageDisplay.setImage(image);
         this.repaint();
      } catch (Exception e) {
         log.error("Error on changing image", e);
      }
   }

   private void initComponents() {
      this.getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
      this.tabbedPane = new JTabbedPane(1);
      this.getContentPane().add(this.tabbedPane);
      this.characterEditorPanel = new JPanel();
      this.tabbedPane.addTab("Characters", null, this.characterEditorPanel, null);
      this.tabbedPane.addTab("Enemies", null, new EnemyEditorPanel(), null);
      JSplitPane jSplitPane1 = new JSplitPane();
      JPanel jPanel3 = new JPanel();
      this.characterNameField = new JTextField();
      JButton createCharacterButton = new JButton();
      JScrollPane jScrollPane1 = new JScrollPane();
      this.characterList = new JList<>();
      this.imageListPanel = new ImageListPanel(this);
      JButton addImageButton = new JButton();
      JButton deleteImageButton = new JButton();
      JSplitPane jSplitPane2 = new JSplitPane();
      JPanel jPanel1 = new JPanel();
      JSplitPane jSplitPane3 = new JSplitPane();
      jSplitPane3.setDividerLocation(500);
      this.imageTagEditor = new ImageTagEditor(this);
      this.attributeEditorPanel = new CharacterDataEditorPanel();
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            try {
               super.windowClosing(e);
               CharacterEditor.this.saveCharacter();
               CharacterEditor.this.dispose();
               Thread.sleep(1000L);
            } catch (InterruptedException e1) {
            } catch (IOException e1) {
               CharacterEditor.log.error("Failed to save character on window exit", e1);
            } finally {
               System.exit(0);
            }
         }
      });
      this.setTitle("Character Editor");
      this.characterEditorPanel.setLayout(new GridBagLayout());
      jPanel3.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow(2)"),
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow(3)"),
               FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      jPanel3.add(this.characterNameField, "1, 1, fill, center");
      createCharacterButton.setText("Create Character");
      createCharacterButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            CharacterEditor.this.createCharacterButtonActionPerformed(evt);
         }
      });
      jPanel3.add(createCharacterButton, "2, 1, center, center");
      jScrollPane1.setDoubleBuffered(true);
      jScrollPane1.setMinimumSize(new Dimension(1, 1));
      jScrollPane1.setPreferredSize(new Dimension(1, 1));
      this.characterList.setModel(new MyAbstractListModel() {
         @Override
         public int getSize() {
            return CharacterEditor.this.characters.size();
         }

         public CharacterBase getElementAt(int i) {
            return CharacterEditor.this.characters.get(i);
         }
      });
      this.characterList.setSelectionMode(0);
      this.characterList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent evt) {
            CharacterEditor.this.characterListValueChanged(evt);
         }
      });
      jScrollPane1.setViewportView(this.characterList);
      jPanel3.add(jScrollPane1, "1, 2, 2, 1, fill, fill");
      jPanel3.add(this.imageListPanel, "1, 4, 2, 1, fill, fill");
      addImageButton.setText("Add Images");
      addImageButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            CharacterEditor.this.addImageButtonActionPerformed(evt);
         }
      });
      jPanel3.add(addImageButton, "1, 5, left, top");
      deleteImageButton.setText("Delete Image");
      deleteImageButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            CharacterEditor.this.deleteImageButtonActionPerformed(evt);
         }
      });
      jPanel3.add(deleteImageButton, "2, 5, left, top");
      jSplitPane1.setLeftComponent(jPanel3);
      jSplitPane2.setDividerLocation(500);
      jSplitPane2.setOrientation(0);
      jSplitPane2.setResizeWeight(0.7);
      jSplitPane2.setDoubleBuffered(true);
      jPanel1.setPreferredSize(new Dimension(1, 1));
      jPanel1.setLayout(new GridBagLayout());
      this.imageDisplay = new MyImage();
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      jPanel1.add(this.imageDisplay, gridBagConstraints);
      jSplitPane2.setTopComponent(jPanel1);
      jSplitPane3.setMinimumSize(new Dimension(1, 1));
      jSplitPane3.setPreferredSize(new Dimension(1, 1));
      jSplitPane3.setLeftComponent(this.imageTagEditor);
      this.imageTagEditor.setImageList(this.imageListPanel.getImageList());
      jSplitPane3.setRightComponent(this.attributeEditorPanel);
      jSplitPane2.setRightComponent(jSplitPane3);
      jSplitPane1.setRightComponent(jSplitPane2);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      this.characterEditorPanel.add(jSplitPane1, gridBagConstraints);
      this.pack();
   }

   public JList<ImageData> getImageList() {
      return this.imageListPanel.getImageList();
   }

   private void characterListValueChanged(ListSelectionEvent evt) {
      this.setCurrentCharacter(this.characterList.getSelectedValue());
   }

   private void setCurrentCharacter(CharacterBase character) {
      if (this.currentCharacter != null) {
         try {
            this.saveCharacter();
         } catch (IOException e) {
            log.error("Faield to save character", e);
         }
      }

      try {
         this.currentCharacter = CharacterFileLoader.getInstance().loadCharacter(character.getFolder());
      } catch (IOException e) {
         log.error("Failed to load character from folder '{}'", new Object[]{character.getFolder().toString()});
         log.throwing(e);
      }

      this.attributeEditorPanel.setCharacter(this.currentCharacter);
      this.imageListPanel.setImageObject(this.currentCharacter);
      this.changeCurrentImage(this.currentCharacter.getImages().get(0));
      if (!this.warningDisplayed && this.currentCharacter.getFolder().isArchive()) {
         JOptionPane.showMessageDialog(
            this,
            "Attention! Editing characters as zip-files is discouraged, since file access from another source (e.g. a running Jasbro game, dropbox, etc.), may lead to the loss of the zip file."
         );
      }
   }

   private void deleteImageButtonActionPerformed(ActionEvent evt) {
      if (this.imageListPanel.getImageList().getSelectedValue() != null && this.currentCharacter.getImages().size() > 1) {
         CharacterFileLoader.getInstance().deletePicture(this.currentImage);
         this.currentCharacter.getImages().remove(this.currentImage);
         int newIndex = this.imageListPanel.getImageList().getSelectedIndex();
         if (newIndex >= this.imageListPanel.getImageList().getModel().getSize()) {
            this.changeCurrentImage(this.imageListPanel.getImageList().getModel().getElementAt(this.imageListPanel.getImageList().getModel().getSize() - 1));
         } else {
            this.changeCurrentImage(this.imageListPanel.getImageList().getModel().getElementAt(newIndex));
         }

         this.imageListPanel.filter();
      }
   }

   private void addImageButtonActionPerformed(ActionEvent evt) {
      if (this.fileChooser == null) {
         this.fileChooser = new JFileChooser();
         this.fileChooser.setMultiSelectionEnabled(true);
         this.fileChooser.setAcceptAllFileFilterUsed(false);
         this.fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
               return "Images";
            }

            @Override
            public boolean accept(File f) {
               return f.isDirectory() || ImageUtil.getInstance().isImage(f.toPath());
            }
         });
      }

      int returnval = this.fileChooser.showOpenDialog(this);
      if (returnval == 0) {
         CharacterFileLoader.getInstance().addImages(this.currentCharacter, this.fileChooser.getSelectedFiles());
         this.imageListPanel.filter();
      }
   }

   private void createCharacterButtonActionPerformed(ActionEvent evt) {
      CharacterBase characterBase = CharacterFileLoader.getInstance().createCharacter(this.characterNameField.getText(), this.characters);
      if (characterBase != null) {
         try {
            CharacterFileLoader.getInstance().saveCharacter(this.currentCharacter);
         } catch (IOException e) {
            log.error("Failed to create character", e);
         }

         this.characterList.setSelectedIndex(this.characters.size() - 1);
         ((MyAbstractListModel)this.characterList.getModel()).update(this, 0, this.currentCharacter.getImages().size() - 1);
      }
   }

   public void saveCharacter() throws IOException {
      if (this.currentCharacter != null && this.currentCharacter.isChanged()) {
         CharacterFileLoader.getInstance().saveCharacter(this.currentCharacter);
      }
   }

   @Override
   public void setNoImageSelected(boolean on) {
      this.imageTagEditor.setEnabled(!on);
      if (on) {
         this.imageDisplay.setImage(null);
      }

      this.repaint();
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               new CharacterEditor().setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
}
