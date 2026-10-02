/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.requirements.CharacterIdRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CharacterIdRequirementPanel
extends JPanel {
    private CharacterIdRequirement triggerRequirement;

    public CharacterIdRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (CharacterIdRequirement)triggerRequirementTmp;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("max(150dlu;default):grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        final JTextField textField = new JTextField();
        this.add((Component)textField, "1, 1, fill, fill");
        textField.setText(this.triggerRequirement.getCharacterId());
        textField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                CharacterIdRequirementPanel.this.triggerRequirement.setCharacterId(textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                CharacterIdRequirementPanel.this.triggerRequirement.setCharacterId(textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                CharacterIdRequirementPanel.this.triggerRequirement.setCharacterId(textField.getText());
            }
        });
    }
}

