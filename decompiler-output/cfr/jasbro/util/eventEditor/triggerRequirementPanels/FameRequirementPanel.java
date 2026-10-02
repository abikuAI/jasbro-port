/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.FameRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FameRequirementPanel
extends JPanel {
    private FameRequirement triggerRequirement;

    public FameRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (FameRequirement)triggerRequirementTmp;
        this.setLayout(new BoxLayout(this, 0));
        final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9.223372036854776E18, 1.0));
        spinner.setValue(this.triggerRequirement.getFameRequired());
        this.add(spinner);
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                double tmp = (Double)spinner.getValue();
                FameRequirementPanel.this.triggerRequirement.setFameRequired((long)tmp);
            }
        });
    }
}

