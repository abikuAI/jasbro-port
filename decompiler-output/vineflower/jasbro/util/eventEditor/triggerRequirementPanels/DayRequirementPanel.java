package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.DayRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayRequirementPanel extends JPanel {
   private DayRequirement triggerRequirement;

   public DayRequirementPanel(TriggerRequirement triggerRequirementTmp) {
      this.triggerRequirement = (DayRequirement)triggerRequirementTmp;
      this.setLayout(new BoxLayout(this, 0));
      final JComboBox<TriggerRequirement.Comparison> comboBox = new JComboBox<>();
      this.add(comboBox);

      for (TriggerRequirement.Comparison dayComparison : TriggerRequirement.Comparison.values()) {
         comboBox.addItem(dayComparison);
      }

      comboBox.setSelectedItem(this.triggerRequirement.getDayComparison());
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            DayRequirementPanel.this.triggerRequirement.setDayComparison((TriggerRequirement.Comparison)comboBox.getSelectedItem());
         }
      });
      final JSpinner spinner = new JSpinner();
      spinner.setValue(this.triggerRequirement.getDay());
      this.add(spinner);
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            DayRequirementPanel.this.triggerRequirement.setDay((Integer)spinner.getValue());
         }
      });
   }
}
