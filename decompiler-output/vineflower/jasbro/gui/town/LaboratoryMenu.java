package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.traits.Trait;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LaboratoryMenu extends MyImage {
   private JButton cloneButton;
   private JButton futanariButton;
   private JButton undoFutaButton;
   private JComboBox<Charakter> characterSelect;
   private JSpinner cloneAmountSpinner;
   private static final int clonePrice = 10000;

   public LaboratoryMenu() {
      this.removeAll();
      this.init();
   }

   public void init() {
      this.removeAll();
      this.setBackgroundImage(new ImageData("images/backgrounds/laboratory.jpg"));
      this.setOpaque(false);
      this.setVisible(true);
      double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
      double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
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

      try {
         this.add(homeButton);
      } catch (NullPointerException e) {
      }

      homeButton.addActionListener(new ActionListener() {
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

      try {
         this.add(backButton);
      } catch (NullPointerException e) {
      }

      backButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showAlchemist();
         }
      });
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               Jasbro.getInstance().getGui().showAlchemist();
            }
         }
      });
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow(3)"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(3)")},
            new RowSpec[]{RowSpec.decode("default:grow")}
         )
      );
      JPanel contentPanel = new JPanel();
      contentPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
      contentPanel.setOpaque(false);
      contentPanel.setAutoscrolls(true);
      this.add(contentPanel, "2, 1, fill, fill");
      this.characterSelect = new JComboBox<>();
      this.characterSelect.addItem(null);

      for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
         this.characterSelect.addItem(character);
      }

      contentPanel.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("right:default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:default:grow")},
            new RowSpec[]{
               RowSpec.decode("top:pref:grow"),
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("top:40dlu"),
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("top:20dlu"),
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("top:20dlu"),
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("default:grow(2)")
            }
         )
      );
      contentPanel.add(this.characterSelect, "1, 2, 3, 1, fill, fill");
      this.cloneAmountSpinner = new JSpinner();
      this.cloneAmountSpinner.setModel(new SpinnerNumberModel(1, 1, 10000, 1));
      this.cloneAmountSpinner.setEnabled(false);
      contentPanel.add(this.cloneAmountSpinner, "1, 4, right, fill");
      this.cloneButton = new JButton(TextUtil.t("laboratory.clone", this.getSelectedCharacter(), 10000));
      contentPanel.add(this.cloneButton, "3, 4, left, fill");
      this.cloneButton.setEnabled(false);
      this.cloneButton
         .addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  if (LaboratoryMenu.this.getSelectedCharacter() != null) {
                     GameData data = Jasbro.getInstance().getData();
                     int clones = (Integer)LaboratoryMenu.this.cloneAmountSpinner.getValue();
                     int i = 0;
                     if (data.canAfford(clones * 10000)) {
                        data.spendMoney(clones * 10000, "Clone");

                        Charakter clone;
                        do {
                           clone = CharacterSpawner.create(LaboratoryMenu.this.getSelectedCharacter().getBase(), CharacterType.SLAVE);
                           clone.setName(clone.getName() + TextUtil.t("laboratory.clonenameaddition", LaboratoryMenu.this.getSelectedCharacter()));
                           clone.removeTrait(Trait.RARESLAVE);
                           clone.removeTrait(Trait.FORMERNOBLE);
                           clone.removeTrait(Trait.EXTREMELYRARESLAVE);
                           clone.removeTrait(Trait.EXTREMELYRARESLAVE2);
                           clone.addTrait(Trait.CLONE);
                           data.getCharacters().add(clone);
                        } while (++i < clones);

                        if (clones == 1) {
                           new MessageScreen(
                              TextUtil.t("laboratory.clone.message", LaboratoryMenu.this.getSelectedCharacter()),
                              ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, clone),
                              clone.getBackground()
                           );
                        } else {
                           Object[] arguments = new Object[]{clones};
                           new MessageScreen(
                              TextUtil.t("laboratory.clone.message.multiple", LaboratoryMenu.this.getSelectedCharacter(), arguments),
                              ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, clone),
                              clone.getBackground()
                           );
                        }

                        Charakter selected = LaboratoryMenu.this.getSelectedCharacter();
                        LaboratoryMenu.this.init();
                        LaboratoryMenu.this.characterSelect.setSelectedItem(selected);
                     }
                  }
               }
            }
         );
      this.futanariButton = new JButton(TextUtil.t("laboratory.turnintofuta", this.getSelectedCharacter()));
      contentPanel.add(this.futanariButton, "1, 6, 3, 1");
      this.futanariButton.setEnabled(false);
      this.futanariButton
         .addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  if (LaboratoryMenu.this.getSelectedCharacter() != null) {
                     GameData data = Jasbro.getInstance().getData();
                     if (data.canAfford(100L)) {
                        data.spendMoney(100L, "Futarization");
                        LaboratoryMenu.this.getSelectedCharacter().setGender(Gender.FUTA);
                        new MessageScreen(
                           TextUtil.t("laboratory.turnintofuta.message", LaboratoryMenu.this.getSelectedCharacter()),
                           ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, LaboratoryMenu.this.getSelectedCharacter()),
                           LaboratoryMenu.this.getSelectedCharacter().getBackground()
                        );
                        LaboratoryMenu.this.init();
                     }
                  }
               }
            }
         );
      this.undoFutaButton = new JButton(TextUtil.t("laboratory.reversefuta", this.getSelectedCharacter()));
      this.undoFutaButton.setEnabled(false);
      contentPanel.add(this.undoFutaButton, "1, 8, 3, 1");
      this.undoFutaButton
         .addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  if (LaboratoryMenu.this.getSelectedCharacter() != null) {
                     GameData data = Jasbro.getInstance().getData();
                     if (data.canAfford(100L)) {
                        data.spendMoney(100L, "Defutarization");
                        LaboratoryMenu.this.getSelectedCharacter().setGender(Gender.FEMALE);
                        new MessageScreen(
                           TextUtil.t("laboratory.reversefuta.message", LaboratoryMenu.this.getSelectedCharacter()),
                           ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, LaboratoryMenu.this.getSelectedCharacter()),
                           LaboratoryMenu.this.getSelectedCharacter().getBackground()
                        );
                        LaboratoryMenu.this.init();
                     }
                  }
               }
            }
         );
      this.characterSelect.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
               if (LaboratoryMenu.this.getSelectedCharacter() != null && LaboratoryMenu.this.getSelectedCharacter().getGender() == Gender.FEMALE) {
                  LaboratoryMenu.this.futanariButton.setEnabled(true);
               } else {
                  LaboratoryMenu.this.futanariButton.setEnabled(false);
               }

               if (LaboratoryMenu.this.getSelectedCharacter() != null && LaboratoryMenu.this.getSelectedCharacter().getType() != CharacterType.TRAINER) {
                  LaboratoryMenu.this.cloneButton.setEnabled(true);
                  LaboratoryMenu.this.cloneAmountSpinner.setEnabled(true);
               } else {
                  LaboratoryMenu.this.cloneButton.setEnabled(false);
                  LaboratoryMenu.this.cloneAmountSpinner.setEnabled(false);
               }

               if (LaboratoryMenu.this.getSelectedCharacter() != null && LaboratoryMenu.this.getSelectedCharacter().getGender() == Gender.FUTA) {
                  LaboratoryMenu.this.undoFutaButton.setEnabled(true);
               } else {
                  LaboratoryMenu.this.undoFutaButton.setEnabled(false);
               }
            }
         }
      });
      this.validate();
   }

   public Charakter getSelectedCharacter() {
      return (Charakter)this.characterSelect.getSelectedItem();
   }
}
