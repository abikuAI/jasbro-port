/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.MinimumCharactersMatchRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MinimumCharactersMatchRequirementPanel
extends JPanel {
    private MinimumCharactersMatchRequirement triggerRequirement;

    public MinimumCharactersMatchRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (MinimumCharactersMatchRequirement)triggerRequirementTmp;
        this.setLayout(new BoxLayout(this, 0));
        final JSpinner spinner = new JSpinner();
        spinner.setValue(this.triggerRequirement.getMinimum());
        this.add(spinner);
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                MinimumCharactersMatchRequirementPanel.this.triggerRequirement.setMinimum((Integer)spinner.getValue());
            }
        });
    }
}

