package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.MinimumCharacterAmountRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MinimumCharacterAmountRequirementPanel extends JPanel {
   private MinimumCharacterAmountRequirement triggerRequirement;

   public MinimumCharacterAmountRequirementPanel(TriggerRequirement triggerRequirementTmp) {
      this.triggerRequirement = (MinimumCharacterAmountRequirement)triggerRequirementTmp;
      this.setLayout(new BoxLayout(this, 0));
      final JSpinner spinner = new JSpinner();
      spinner.setValue(this.triggerRequirement.getMinimum());
      this.add(spinner);
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            MinimumCharacterAmountRequirementPanel.this.triggerRequirement.setMinimum((Integer)spinner.getValue());
         }
      });
   }
}
