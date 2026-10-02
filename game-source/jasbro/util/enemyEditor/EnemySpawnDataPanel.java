package jasbro.util.enemyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.EnemySpawnData;
import jasbro.game.world.customContent.npc.EnemySpawnLocation;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EnemySpawnDataPanel extends JPanel {
   private ComplexEnemyTemplate complexEnemyTemplate;
   private EnemySpawnData enemySpawnData;

   public EnemySpawnDataPanel(ComplexEnemyTemplate complexEnemyTemplateTmp, EnemySpawnData enemySpawnDataTmp) {
      this.complexEnemyTemplate = complexEnemyTemplateTmp;
      this.enemySpawnData = enemySpawnDataTmp;
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.UNRELATED_GAP_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.UNRELATED_GAP_ROWSPEC
            }
         )
      );
      JLabel lblLocation = new JLabel("Spawn-Location");
      this.add(lblLocation, "1, 2, right, default");
      final JComboBox<EnemySpawnLocation> comboBox = new JComboBox<>();
      this.add(comboBox, "2, 2, fill, default");

      for (EnemySpawnLocation enemySpawnLocation : EnemySpawnLocation.values()) {
         comboBox.addItem(enemySpawnLocation);
      }

      comboBox.setSelectedItem(this.enemySpawnData.getEnemySpawnLocation());
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EnemySpawnDataPanel.this.enemySpawnData.setEnemySpawnLocation((EnemySpawnLocation)comboBox.getSelectedItem());
         }
      });
      JLabel lblChance = new JLabel("Encounter Chance Modifier");
      this.add(lblChance, "1, 3");
      final JSpinner spinner = new JSpinner();
      spinner.setModel(new SpinnerNumberModel(1, 1, 999999, 1));
      spinner.setValue(this.enemySpawnData.getEncounterChanceModifier());
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EnemySpawnDataPanel.this.enemySpawnData.setEncounterChanceModifier((Integer)spinner.getValue());
         }
      });
      this.add(spinner, "2, 3");
      JButton btnDelete = new JButton("Delete");
      btnDelete.setForeground(Color.RED);
      this.add(btnDelete, "2, 6");
      btnDelete.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            EnemySpawnDataPanel.this.complexEnemyTemplate.getSpawnDataList().remove(EnemySpawnDataPanel.this.enemySpawnData);
            Container parent = EnemySpawnDataPanel.this.getParent();
            parent.remove(EnemySpawnDataPanel.this);
            parent.validate();
            parent.repaint();
         }
      });
   }
}
