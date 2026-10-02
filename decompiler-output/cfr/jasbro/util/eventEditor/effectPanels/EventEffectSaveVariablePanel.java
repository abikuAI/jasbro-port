/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSaveVariable;
import jasbro.texts.TextUtil;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectSaveVariablePanel
extends JPanel {
    private WorldEventSaveVariable worldEventEffect;
    private JTextField textField;
    private JTextField textField_1;

    public EventEffectSaveVariablePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.worldEventEffect = (WorldEventSaveVariable)worldEventEffectTmp;
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.source"));
        this.add((Component)lblNewLabel, "1, 1, right, fill");
        this.textField = new JTextField();
        this.add((Component)this.textField, "2, 1, fill, default");
        this.textField.setText(this.worldEventEffect.getSource());
        this.textField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                EventEffectSaveVariablePanel.this.worldEventEffect.setSource(EventEffectSaveVariablePanel.this.textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                EventEffectSaveVariablePanel.this.worldEventEffect.setSource(EventEffectSaveVariablePanel.this.textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                EventEffectSaveVariablePanel.this.worldEventEffect.setSource(EventEffectSaveVariablePanel.this.textField.getText());
            }
        });
        JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.target"));
        this.add((Component)lblNewLabel_1, "3, 1, right, default");
        this.textField_1 = new JTextField();
        this.add((Component)this.textField_1, "4, 1, fill, default");
        this.textField_1.setText(this.worldEventEffect.getTarget());
        this.textField_1.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                EventEffectSaveVariablePanel.this.worldEventEffect.setTarget(EventEffectSaveVariablePanel.this.textField_1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                EventEffectSaveVariablePanel.this.worldEventEffect.setTarget(EventEffectSaveVariablePanel.this.textField_1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                EventEffectSaveVariablePanel.this.worldEventEffect.setTarget(EventEffectSaveVariablePanel.this.textField_1.getText());
            }
        });
    }
}

