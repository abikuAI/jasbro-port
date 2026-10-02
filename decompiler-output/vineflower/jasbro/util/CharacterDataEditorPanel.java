package jasbro.util;

import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CharacterDataEditorPanel extends JPanel implements PropertyChangeListener {
   private CharacterBase character;
   private int pointsToSpend = 0;
   private boolean noCheckFlag = false;
   private Map<BaseAttributeTypes, JSpinner> attributeSpinnerMap = new HashMap<>();
   private Map<Trait, JCheckBox> traitCheckboxMap = new HashMap<>();
   private JLabel availablePointsLabel;
   private JLabel jLabel1;
   private JLabel jLabel2;
   private JTextField nameField;
   private JLabel lblNewLabel;
   private JComboBox<Gender> genderSelection;
   private JLabel lblType;
   private JComboBox<CharacterType> typeSelection;
   private JComboBox<SpecializationType> initialSpecializationSelection;
   private JTabbedPane tabbedPane;
   private JPanel characterDataPanel;
   private JPanel traitPanel;
   private JTextArea traitDescriptionField;
   private JTextArea descriptionField;

   public CharacterDataEditorPanel() {
      this.initComponents();
      this.addAttributeComponents();
      this.addTraitComponents();
   }

   public void nameChanged() {
      if (!this.noCheckFlag) {
         try {
            this.character.setName(this.nameField.getText());
            this.setCharacterChanged();
         } catch (Exception e) {
         }

         this.repaint();
      }
   }

   private void descriptionChanged() {
      if (!this.noCheckFlag) {
         try {
            this.character.setDescription(this.descriptionField.getText());
            this.setCharacterChanged();
         } catch (Exception e) {
         }

         this.repaint();
      }
   }

   public void setCharacter(CharacterBase character) {
      this.character = character;
      this.noCheckFlag = true;
      this.pointsToSpend = 0;
      this.availablePointsLabel.setText("0");
      this.nameField.setText(character.getName());

      for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
         if (attribute != BaseAttributeTypes.COMMAND) {
            JSpinner spinner = this.attributeSpinnerMap.get(attribute);
            spinner.setValue(character.getAttribute(attribute));
         }
      }

      this.genderSelection.setSelectedItem(character.getGender());
      this.typeSelection.setSelectedItem(character.getType());

      for (Trait trait : Trait.getBasicTraits()) {
         JCheckBox checkBox = this.traitCheckboxMap.get(trait);
         checkBox.setSelected(character.getTraits().contains(trait));
      }

      this.noCheckFlag = false;
      this.descriptionField.setText(character.getDescription());
      this.repaint();
   }

   private synchronized void initComponents() {
      this.noCheckFlag = true;
      this.setLayout(new GridLayout(1, 1));
      this.tabbedPane = new JTabbedPane(1);
      this.add(this.tabbedPane);
      JScrollPane scrollPane = new JScrollPane();
      this.tabbedPane.addTab("Attributes", null, scrollPane, null);
      this.characterDataPanel = new JPanel();
      scrollPane.setViewportView(this.characterDataPanel);
      this.characterDataPanel.setLayout(new GridLayout(0, 2));
      this.jLabel2 = new JLabel();
      this.characterDataPanel.add(this.jLabel2);
      this.jLabel2.setText("Name");
      this.nameField = new JTextField();
      this.characterDataPanel.add(this.nameField);
      this.lblNewLabel = new JLabel("Gender");
      this.characterDataPanel.add(this.lblNewLabel);
      this.genderSelection = new JComboBox<>();
      this.characterDataPanel.add(this.genderSelection);
      this.lblType = new JLabel("Type");
      this.characterDataPanel.add(this.lblType);
      this.typeSelection = new JComboBox<>();
      this.characterDataPanel.add(this.typeSelection);
      this.typeSelection.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!CharacterDataEditorPanel.this.noCheckFlag) {
               CharacterDataEditorPanel.this.character.setType((CharacterType)CharacterDataEditorPanel.this.typeSelection.getSelectedItem());
               CharacterDataEditorPanel.this.setCharacterChanged();
            }
         }
      });
      this.typeSelection.setRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
            CharacterType type = (CharacterType)value;
            if (type == null) {
               label.setText(TextUtil.t("ui.trainerOrSlave"));
            } else if (type == CharacterType.SLAVE) {
               label.setText(TextUtil.t("ui.slaveonly"));
            } else if (type == CharacterType.TRAINER) {
               label.setText(TextUtil.t("ui.traineronly"));
            } else {
               label.setText(type.getText());
            }

            return label;
         }
      });
      this.typeSelection.addItem(null);

      for (CharacterType type : CharacterType.values()) {
         this.typeSelection.addItem(type);
      }

      this.lblType = new JLabel("Initial specialization");
      this.characterDataPanel.add(this.lblType);
      this.initialSpecializationSelection = new JComboBox<>();
      this.characterDataPanel.add(this.initialSpecializationSelection);
      this.initialSpecializationSelection
         .addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  if (!CharacterDataEditorPanel.this.noCheckFlag) {
                     CharacterDataEditorPanel.this.character
                        .setInitialSpecialization((SpecializationType)CharacterDataEditorPanel.this.initialSpecializationSelection.getSelectedItem());
                     CharacterDataEditorPanel.this.setCharacterChanged();
                  }
               }
            }
         );
      this.initialSpecializationSelection.addItem(null);

      for (SpecializationType type : SpecializationType.values()) {
         if (type != SpecializationType.SLAVE && type != SpecializationType.TRAINER && type != SpecializationType.SEX) {
            this.initialSpecializationSelection.addItem(type);
         }
      }

      this.jLabel1 = new JLabel();
      this.characterDataPanel.add(this.jLabel1);
      this.jLabel1.setText("Available Points");
      this.availablePointsLabel = new JLabel();
      this.characterDataPanel.add(this.availablePointsLabel);
      this.availablePointsLabel.setText("0");
      this.genderSelection.addItem(Gender.FEMALE);
      this.genderSelection.addItem(Gender.MALE);
      this.genderSelection.addItem(Gender.FUTA);
      this.genderSelection.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!CharacterDataEditorPanel.this.noCheckFlag) {
               CharacterDataEditorPanel.this.character.setGender((Gender)CharacterDataEditorPanel.this.genderSelection.getSelectedItem());
               CharacterDataEditorPanel.this.setCharacterChanged();
            }
         }
      });
      this.traitPanel = new JPanel();
      this.traitPanel.setLayout(new GridLayout(0, 2));
      this.tabbedPane.addTab("Traits", null, this.traitPanel, null);
      scrollPane = new JScrollPane();
      this.tabbedPane.addTab("Description Author", null, scrollPane, null);
      this.descriptionField = new JTextArea();
      this.descriptionField.setLineWrap(true);
      this.descriptionField.setWrapStyleWord(true);
      this.descriptionField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            CharacterDataEditorPanel.this.descriptionChanged();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            CharacterDataEditorPanel.this.descriptionChanged();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            CharacterDataEditorPanel.this.descriptionChanged();
         }
      });
      scrollPane.setViewportView(this.descriptionField);
      this.noCheckFlag = false;
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      if (!this.noCheckFlag) {
         Integer oldInt = (Integer)evt.getOldValue();
         Integer newInt = (Integer)evt.getNewValue();
         JSpinner spinner = (JSpinner)((JComponent)evt.getSource()).getParent().getParent();
         if (newInt < oldInt) {
            this.pointsToSpend++;
         } else if (this.pointsToSpend <= 0) {
            this.noCheckFlag = true;
            spinner.setValue(oldInt);
            this.noCheckFlag = false;
         } else {
            this.pointsToSpend--;
            if (this.pointsToSpend == 0) {
               for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
                  spinner = this.attributeSpinnerMap.get(attribute);
                  if (spinner != null) {
                     this.character.setAttribute(attribute, (Integer)spinner.getValue());
                  }
               }

               this.setCharacterChanged();
            }
         }

         this.availablePointsLabel.setText(this.pointsToSpend + "");
      }

      this.repaint();
   }

   private void addAttributeComponents() {
      this.nameField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            CharacterDataEditorPanel.this.nameChanged();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            CharacterDataEditorPanel.this.nameChanged();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            CharacterDataEditorPanel.this.nameChanged();
         }
      });

      for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
         if (attribute != BaseAttributeTypes.COMMAND) {
            SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 30, 1);
            if (attribute != BaseAttributeTypes.OBEDIENCE) {
               this.characterDataPanel.add(new JLabel(attribute.getText()));
            } else {
               this.characterDataPanel.add(new JLabel("Obedience / Command"));
            }

            JSpinner spinner = new JSpinner(spinnerModel);
            DefaultEditor editor = (DefaultEditor)spinner.getEditor();
            editor.getTextField().addPropertyChangeListener("value", this);
            this.attributeSpinnerMap.put(attribute, spinner);
            this.characterDataPanel.add(spinner);
         }
      }
   }

   public void addTraitComponents() {
      JPanel checkboxPanel = new JPanel();
      checkboxPanel.setLayout(new GridLayout(0, 1));
      JScrollPane scrollpane = new JScrollPane();
      scrollpane.setViewportView(checkboxPanel);
      this.traitPanel.add(scrollpane);
      ActionListener al = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!CharacterDataEditorPanel.this.noCheckFlag) {
               Trait trait = Trait.valueOf(e.getActionCommand());
               JCheckBox checkBox = (JCheckBox)e.getSource();
               if (checkBox.isSelected()) {
                  CharacterDataEditorPanel.this.character.addTrait(trait);
               } else {
                  CharacterDataEditorPanel.this.character.removeTrait(trait);
               }

               CharacterDataEditorPanel.this.character.setChanged(true);
            }
         }
      };
      MouseListener ml = new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            JCheckBox checkBox = (JCheckBox)e.getSource();
            Trait trait = Trait.valueOf(checkBox.getActionCommand());
            CharacterDataEditorPanel.this.traitDescriptionField.setText(trait.getText() + "\n" + trait.getDescription());
         }
      };

      for (Trait trait : Trait.getBasicTraits()) {
         JCheckBox checkBox = new JCheckBox(trait.getText());
         checkBox.setToolTipText(trait.getDescription());
         checkBox.setActionCommand(trait.toString());
         checkBox.addActionListener(al);
         checkBox.addMouseListener(ml);
         this.traitCheckboxMap.put(trait, checkBox);
         checkboxPanel.add(checkBox);
      }

      this.traitDescriptionField = GuiUtil.getDefaultTextarea();
      this.traitPanel.add(this.traitDescriptionField);
   }

   private void setCharacterChanged() {
      this.character.setChanged(true);
   }

   public JPanel getCharacterDataPanel() {
      return this.characterDataPanel;
   }
}
