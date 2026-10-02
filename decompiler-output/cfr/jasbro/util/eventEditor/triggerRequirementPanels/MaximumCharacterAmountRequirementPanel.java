/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.MaximumCharacterAmountRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MaximumCharacterAmountRequirementPanel
extends JPanel {
    private MaximumCharacterAmountRequirement triggerRequirement;

    public MaximumCharacterAmountRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (MaximumCharacterAmountRequirement)triggerRequirementTmp;
        this.setLayout(new BoxLayout(this, 0));
        final JSpinner spinner = new JSpinner();
        spinner.setValue(this.triggerRequirement.getMaximum());
        this.add(spinner);
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                MaximumCharacterAmountRequirementPanel.this.triggerRequirement.setMaximum((Integer)spinner.getValue());
            }
        });
    }
}

