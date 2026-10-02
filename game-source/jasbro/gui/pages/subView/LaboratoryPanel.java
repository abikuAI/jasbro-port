package jasbro.gui.pages.subView;

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
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class LaboratoryPanel extends JPanel {
   private JButton cloneButton;
   private JButton futanariButton;
   private JButton undoFutaButton;
   private JComboBox<Charakter> characterSelect;
   private JSpinner cloneAmountSpinner;
   private static final int clonePrice = 10000;

   public LaboratoryPanel() {
      this.init();
   }

   public void init() {
      this.removeAll();
      this.setOpaque(false);
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
                  if (LaboratoryPanel.this.getSelectedCharacter() != null) {
                     GameData data = Jasbro.getInstance().getData();
                     int clones = (Integer)LaboratoryPanel.this.cloneAmountSpinner.getValue();
                     int i = 0;
                     if (data.canAfford(clones * 10000)) {
                        data.spendMoney(clones * 10000, "Clone");

                        Charakter clone;
                        do {
                           clone = CharacterSpawner.create(LaboratoryPanel.this.getSelectedCharacter().getBase(), CharacterType.SLAVE);
                           clone.setName(clone.getName() + TextUtil.t("laboratory.clonenameaddition", LaboratoryPanel.this.getSelectedCharacter()));
                           data.getCharacters().add(clone);
                        } while (++i < clones);

                        if (clones == 1) {
                           new MessageScreen(
                              TextUtil.t("laboratory.clone.message", LaboratoryPanel.this.getSelectedCharacter()),
                              ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, clone),
                              clone.getBackground()
                           );
                        } else {
                           Object[] arguments = new Object[]{clones};
                           new MessageScreen(
                              TextUtil.t("laboratory.clone.message.multiple", LaboratoryPanel.this.getSelectedCharacter(), arguments),
                              ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, clone),
                              clone.getBackground()
                           );
                        }

                        Charakter selected = LaboratoryPanel.this.getSelectedCharacter();
                        LaboratoryPanel.this.init();
                        LaboratoryPanel.this.characterSelect.setSelectedItem(selected);
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
                  if (LaboratoryPanel.this.getSelectedCharacter() != null) {
                     GameData data = Jasbro.getInstance().getData();
                     if (data.canAfford(100L)) {
                        data.spendMoney(100L, "Futarization");
                        LaboratoryPanel.this.getSelectedCharacter().setGender(Gender.FUTA);
                        new MessageScreen(
                           TextUtil.t("laboratory.turnintofuta.message", LaboratoryPanel.this.getSelectedCharacter()),
                           ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, LaboratoryPanel.this.getSelectedCharacter()),
                           LaboratoryPanel.this.getSelectedCharacter().getBackground()
                        );
                        LaboratoryPanel.this.init();
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
                  if (LaboratoryPanel.this.getSelectedCharacter() != null) {
                     GameData data = Jasbro.getInstance().getData();
                     if (data.canAfford(100L)) {
                        data.spendMoney(100L, "Defutarization");
                        LaboratoryPanel.this.getSelectedCharacter().setGender(Gender.FEMALE);
                        new MessageScreen(
                           TextUtil.t("laboratory.reversefuta.message", LaboratoryPanel.this.getSelectedCharacter()),
                           ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, LaboratoryPanel.this.getSelectedCharacter()),
                           LaboratoryPanel.this.getSelectedCharacter().getBackground()
                        );
                        LaboratoryPanel.this.init();
                     }
                  }
               }
            }
         );
      this.characterSelect.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
               if (LaboratoryPanel.this.getSelectedCharacter() != null && LaboratoryPanel.this.getSelectedCharacter().getGender() == Gender.FEMALE) {
                  LaboratoryPanel.this.futanariButton.setEnabled(true);
               } else {
                  LaboratoryPanel.this.futanariButton.setEnabled(false);
               }

               if (LaboratoryPanel.this.getSelectedCharacter() != null && LaboratoryPanel.this.getSelectedCharacter().getType() != CharacterType.TRAINER) {
                  LaboratoryPanel.this.cloneButton.setEnabled(true);
                  LaboratoryPanel.this.cloneAmountSpinner.setEnabled(true);
               } else {
                  LaboratoryPanel.this.cloneButton.setEnabled(false);
                  LaboratoryPanel.this.cloneAmountSpinner.setEnabled(false);
               }

               if (LaboratoryPanel.this.getSelectedCharacter() != null && LaboratoryPanel.this.getSelectedCharacter().getGender() == Gender.FUTA) {
                  LaboratoryPanel.this.undoFutaButton.setEnabled(true);
               } else {
                  LaboratoryPanel.this.undoFutaButton.setEnabled(false);
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
