package jasbro.util.enemyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EnemySexPanel extends JPanel {
   private ComplexEnemyTemplate complexEnemyTemplate;
   private JTextField rapeFemaleField = new JTextField();
   private JTextField reverseRapeFemaleField = new JTextField();
   private JTextField reverseRapeFemaleCaptureField = new JTextField();
   private JTextField submitFemaleField = new JTextField();
   private JTextField rapeMaleField = new JTextField();
   private JTextField reverseRapeMaleField = new JTextField();
   private JTextField reverseRapeMaleCaptureField = new JTextField();
   private JTextField submitMaleField = new JTextField();
   private JTextField encounterField = new JTextField();
   private JTextField summoningField = new JTextField();
   private JTextField captureField = new JTextField();

   public EnemySexPanel() {
      this.setLayout(
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
      this.rapeFemaleField = new JTextField();
      this.reverseRapeFemaleField = new JTextField();
      this.reverseRapeFemaleCaptureField = new JTextField();
      this.submitFemaleField = new JTextField();
      this.rapeMaleField = new JTextField();
      this.reverseRapeMaleField = new JTextField();
      this.reverseRapeMaleCaptureField = new JTextField();
      this.submitMaleField = new JTextField();
      this.encounterField = new JTextField();
      this.summoningField = new JTextField();
      this.captureField = new JTextField();
      JLabel jLabel1 = new JLabel("Those fields are for monster on female scenes.");
      this.add(jLabel1, "1,1");
      jLabel1.setFont(new Font("Tahoma", 1, 12));
      JLabel jLabel2 = new JLabel("Submit:");
      this.add(jLabel2, "1,2");
      JLabel jLabel3 = new JLabel("Rape:");
      this.add(jLabel3, "1,3");
      JLabel jLabel4 = new JLabel("Reverse-Rape:");
      this.add(jLabel4, "1,4");
      JLabel jLabel5 = new JLabel("Reverse-Rape + Capture:");
      this.add(jLabel5, "1,5");
      JLabel jLabel6 = new JLabel("Those fields are for monster on male scenes.");
      this.add(jLabel6, "1,6");
      jLabel6.setFont(new Font("Tahoma", 1, 12));
      JLabel jLabel7 = new JLabel("Submit:");
      this.add(jLabel7, "1,7");
      JLabel jLabel8 = new JLabel("Rape:");
      this.add(jLabel8, "1,8");
      JLabel jLabel9 = new JLabel("Reverse-Rape:");
      this.add(jLabel9, "1,9");
      JLabel jLabel10 = new JLabel("Reverse-Rape + Capture:");
      this.add(jLabel10, "1,10");
      JLabel jLabel11 = new JLabel("Those fields are for generic/non-sex scenes.");
      this.add(jLabel11, "1,11");
      jLabel11.setFont(new Font("Tahoma", 1, 12));
      JLabel jLabel12 = new JLabel("Encounter:");
      this.add(jLabel12, "1,12");
      JLabel jLabel13 = new JLabel("Summoning:");
      this.add(jLabel13, "1,13");
      JLabel jLabel14 = new JLabel("Capture:");
      this.add(jLabel14, "1,14");
      this.add(this.submitFemaleField, "2,2");
      this.submitFemaleField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setFemaleSubmit(EnemySexPanel.this.submitFemaleField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setFemaleSubmit(EnemySexPanel.this.submitFemaleField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setFemaleSubmit(EnemySexPanel.this.submitFemaleField.getText());
         }
      });
      this.add(this.rapeFemaleField, "2,3");
      this.rapeFemaleField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setFemaleRape(EnemySexPanel.this.rapeFemaleField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setFemaleRape(EnemySexPanel.this.rapeFemaleField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setFemaleRape(EnemySexPanel.this.rapeFemaleField.getText());
         }
      });
      this.add(this.reverseRapeFemaleField, "2,4");
      this.reverseRapeFemaleField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseFemaleRape(EnemySexPanel.this.reverseRapeFemaleField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseFemaleRape(EnemySexPanel.this.reverseRapeFemaleField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseFemaleRape(EnemySexPanel.this.reverseRapeFemaleField.getText());
         }
      });
      this.add(this.reverseRapeFemaleCaptureField, "2,5");
      this.reverseRapeFemaleCaptureField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseFemaleRapeCapture(EnemySexPanel.this.reverseRapeFemaleCaptureField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseFemaleRapeCapture(EnemySexPanel.this.reverseRapeFemaleCaptureField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseFemaleRapeCapture(EnemySexPanel.this.reverseRapeFemaleCaptureField.getText());
         }
      });
      this.add(this.submitMaleField, "2,7");
      this.submitMaleField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setMaleSubmit(EnemySexPanel.this.submitMaleField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setMaleSubmit(EnemySexPanel.this.submitMaleField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setMaleSubmit(EnemySexPanel.this.submitMaleField.getText());
         }
      });
      this.add(this.rapeMaleField, "2,8");
      this.rapeMaleField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setMaleRape(EnemySexPanel.this.rapeMaleField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setMaleRape(EnemySexPanel.this.rapeMaleField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setMaleRape(EnemySexPanel.this.rapeMaleField.getText());
         }
      });
      this.add(this.reverseRapeMaleField, "2,9");
      this.reverseRapeMaleField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseMaleRape(EnemySexPanel.this.reverseRapeMaleField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseMaleRape(EnemySexPanel.this.reverseRapeMaleField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseMaleRape(EnemySexPanel.this.reverseRapeMaleField.getText());
         }
      });
      this.add(this.reverseRapeMaleCaptureField, "2,10");
      this.reverseRapeMaleCaptureField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseMaleRapeCapture(EnemySexPanel.this.reverseRapeMaleCaptureField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseMaleRapeCapture(EnemySexPanel.this.reverseRapeMaleCaptureField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setReverseMaleRapeCapture(EnemySexPanel.this.reverseRapeMaleCaptureField.getText());
         }
      });
      this.add(this.encounterField, "2,12");
      this.encounterField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextEncounter(EnemySexPanel.this.encounterField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextEncounter(EnemySexPanel.this.encounterField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextEncounter(EnemySexPanel.this.encounterField.getText());
         }
      });
      this.add(this.summoningField, "2,13");
      this.summoningField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextSummoning(EnemySexPanel.this.summoningField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextSummoning(EnemySexPanel.this.summoningField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextSummoning(EnemySexPanel.this.summoningField.getText());
         }
      });
      this.add(this.captureField, "2,14");
      this.captureField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextCapture(EnemySexPanel.this.captureField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextCapture(EnemySexPanel.this.captureField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EnemySexPanel.this.complexEnemyTemplate.setTextCapture(EnemySexPanel.this.captureField.getText());
         }
      });
   }

   public void setComplexEnemyTemplate(ComplexEnemyTemplate enemy) {
      this.complexEnemyTemplate = enemy;
      this.submitFemaleField.setText(enemy.getFemaleSubmit());
      this.submitMaleField.setText(enemy.getMaleSubmit());
      this.rapeFemaleField.setText(enemy.getFemaleRape());
      this.rapeMaleField.setText(enemy.getMaleRape());
      this.reverseRapeFemaleField.setText(enemy.getReverseFemaleRape());
      this.reverseRapeMaleField.setText(enemy.getReverseMaleRape());
      this.reverseRapeMaleCaptureField.setText(enemy.getReverseMaleRapeCapture());
      this.reverseRapeFemaleCaptureField.setText(enemy.getReverseFemaleRapeCapture());
      this.encounterField.setText(enemy.getTextEncounter());
      this.summoningField.setText(enemy.getTextSummoning());
      this.captureField.setText(enemy.getTextCapture());
      this.repaint();
   }
}
