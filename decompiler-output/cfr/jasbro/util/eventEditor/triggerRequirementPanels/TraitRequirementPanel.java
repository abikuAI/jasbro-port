/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.traits.Trait;
import jasbro.game.world.customContent.requirements.TraitRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class TraitRequirementPanel
extends JPanel {
    private TraitRequirement triggerRequirement;

    public TraitRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        this.triggerRequirement = (TraitRequirement)triggerRequirementTmp;
        final JComboBox<Trait> traitCombobox = new JComboBox<Trait>();
        this.add(traitCombobox, "1, 1");
        for (Trait trait : Trait.values()) {
            traitCombobox.addItem(trait);
        }
        traitCombobox.setSelectedItem(this.triggerRequirement.getTrait());
        traitCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TraitRequirementPanel.this.triggerRequirement.setTrait((Trait)traitCombobox.getSelectedItem());
            }
        });
    }
}

