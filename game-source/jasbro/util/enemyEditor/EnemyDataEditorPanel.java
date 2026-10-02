package jasbro.util.enemyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.Gender;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.MonsterDickType;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EnemyDataEditorPanel extends JTabbedPane {
   private JTextField nameField;
   private JComboBox<Gender> genderSelection;
   private JComboBox<MonsterDickType> dickSelection;
   private JComboBox<DamageType> atunementSelection;
   private ComplexEnemyTemplate complexEnemyTemplate;
   private CombatEditorPanel combatEditorPanel;
   private EnemySpawnDataListPanel spawnDataListPanel;
   private EnemySexPanel enemySexPanel;
   private JLabel lblCanBeCaptured;
   private JLabel lblSummoningStone;
   private JComboBox<String> characterBaseComboBox;
   private JComboBox<String> itemComboBox;
   private JCheckBox customerMonsterCheckbox;

   public EnemyDataEditorPanel() {
      JPanel mainEnemyDataPanel = new JPanel();
      this.addTab("Data", null, mainEnemyDataPanel, null);
      mainEnemyDataPanel.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      JLabel jLabel2 = new JLabel();
      mainEnemyDataPanel.add(jLabel2, "1,1");
      jLabel2.setText("Name");
      this.nameField = new JTextField();
      mainEnemyDataPanel.add(this.nameField, "2,1");
      this.nameField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setName(EnemyDataEditorPanel.this.nameField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setName(EnemyDataEditorPanel.this.nameField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setName(EnemyDataEditorPanel.this.nameField.getText());
         }
      });
      JLabel lblNewLabel = new JLabel("Gender");
      mainEnemyDataPanel.add(lblNewLabel, "1,2");
      this.genderSelection = new JComboBox<>();
      mainEnemyDataPanel.add(this.genderSelection, "2, 2");

      for (Gender gender : Gender.values()) {
         this.genderSelection.addItem(gender);
      }

      this.genderSelection.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setGender((Gender)EnemyDataEditorPanel.this.genderSelection.getSelectedItem());
         }
      });
      JLabel lblNewLabel2 = new JLabel("Dick Type");
      mainEnemyDataPanel.add(lblNewLabel2, "1,3");
      this.dickSelection = new JComboBox<>();
      mainEnemyDataPanel.add(this.dickSelection, "2, 3");

      for (MonsterDickType dick : MonsterDickType.values()) {
         this.dickSelection.addItem(dick);
      }

      this.dickSelection.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setDick((MonsterDickType)EnemyDataEditorPanel.this.dickSelection.getSelectedItem());
         }
      });
      JLabel atunementLabel = new JLabel("Element");
      mainEnemyDataPanel.add(atunementLabel, "1,4");
      this.atunementSelection = new JComboBox<>();
      mainEnemyDataPanel.add(this.atunementSelection, "2, 4");

      for (DamageType type : DamageType.values()) {
         if (type != DamageType.REGULAR) {
            this.atunementSelection.addItem(type);
         }
      }

      this.atunementSelection.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setElement((DamageType)EnemyDataEditorPanel.this.atunementSelection.getSelectedItem());
         }
      });
      this.lblCanBeCaptured = new JLabel("Can be captured as:");
      mainEnemyDataPanel.add(this.lblCanBeCaptured, "1, 5, right, default");
      this.characterBaseComboBox = new JComboBox<>();
      mainEnemyDataPanel.add(this.characterBaseComboBox, "2, 5, fill, default");
      this.characterBaseComboBox.setEditable(true);
      this.characterBaseComboBox.addItem(null);

      for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
         this.characterBaseComboBox.addItem(base.getId());
      }

      this.characterBaseComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setCharacterBaseId((String)EnemyDataEditorPanel.this.characterBaseComboBox.getSelectedItem());
         }
      });
      this.lblSummoningStone = new JLabel("Summoning Stone:");
      mainEnemyDataPanel.add(this.lblSummoningStone, "1, 6");
      this.itemComboBox = new JComboBox<>();
      mainEnemyDataPanel.add(this.itemComboBox, "2, 6, fill, default");
      this.itemComboBox.setEditable(true);
      this.itemComboBox.addItem(null);

      for (Item item : Jasbro.getInstance().getAvailableItemsByType(ItemType.SUMMONING)) {
         this.itemComboBox.addItem(item.getId());
      }

      this.itemComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setItemBaseId((String)EnemyDataEditorPanel.this.itemComboBox.getSelectedItem());
         }
      });
      JLabel customerMonsterLabel = new JLabel(TextUtil.t("ui.enemyEditor.customerMonster"));
      customerMonsterLabel.setToolTipText(TextUtil.t("ui.enemyEditor.customerMonster.tooltip"));
      mainEnemyDataPanel.add(customerMonsterLabel, "1, 7, left, center");
      this.customerMonsterCheckbox = new JCheckBox("");
      this.customerMonsterCheckbox.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            EnemyDataEditorPanel.this.complexEnemyTemplate.setCustomerMonster(e.getStateChange() == 1);
         }
      });
      mainEnemyDataPanel.add(this.customerMonsterCheckbox, "2, 7, left, top");
      this.combatEditorPanel = new CombatEditorPanel();
      this.addTab("Combat stats", null, this.combatEditorPanel, null);
      this.spawnDataListPanel = new EnemySpawnDataListPanel();
      this.addTab("Spawn", null, this.spawnDataListPanel, null);
      this.enemySexPanel = new EnemySexPanel();
      this.addTab("Text", null, this.enemySexPanel, null);
   }

   public void setEnemyTemplate(ComplexEnemyTemplate enemy) {
      this.complexEnemyTemplate = enemy;
      this.nameField.setText(enemy.getName());
      this.genderSelection.setSelectedItem(enemy.getGender());
      this.dickSelection.setSelectedItem(enemy.getDick());
      this.atunementSelection.setSelectedItem(enemy.getElement());
      this.combatEditorPanel.setComplexEnemyTemplate(this.complexEnemyTemplate);
      this.spawnDataListPanel.setComplexEnemyTemplate(enemy);
      this.enemySexPanel.setComplexEnemyTemplate(enemy);
      this.characterBaseComboBox.setSelectedItem(this.complexEnemyTemplate.getCharacterBaseId());
      this.itemComboBox.setSelectedItem(this.complexEnemyTemplate.getItemBaseId());
      this.customerMonsterCheckbox.setSelected(this.complexEnemyTemplate.isCustomerMonster());
      this.repaint();
   }
}
