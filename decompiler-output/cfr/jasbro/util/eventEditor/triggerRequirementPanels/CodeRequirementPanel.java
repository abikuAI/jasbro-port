/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.requirements.CodeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CodeRequirementPanel
extends JPanel {
    private CodeRequirement triggerRequirement;

    public CodeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (CodeRequirement)triggerRequirementTmp;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("max(350dlu;default):grow")}, new RowSpec[]{RowSpec.decode("30dlu")}));
        final JTextArea textArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.triggerRequirement.getCode());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                CodeRequirementPanel.this.triggerRequirement.setCode(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                CodeRequirementPanel.this.triggerRequirement.setCode(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                CodeRequirementPanel.this.triggerRequirement.setCode(textArea.getText());
            }
        });
        this.add((Component)scrollPane, "1, 1, fill, fill");
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }
}

