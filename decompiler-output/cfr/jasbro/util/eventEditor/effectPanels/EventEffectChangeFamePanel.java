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
import jasbro.game.world.customContent.effects.WorldEventChangeFame;
import jasbro.texts.TextUtil;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectChangeFamePanel
extends JPanel {
    private WorldEventChangeFame worldEventEffect;
    private JTextField textField;

    public EventEffectChangeFamePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.worldEventEffect = (WorldEventChangeFame)worldEventEffectTmp;
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.target"));
        this.add((Component)lblNewLabel, "1, 1, right, fill");
        this.textField = new JTextField();
        this.add((Component)this.textField, "2, 1, fill, default");
        this.textField.setText(this.worldEventEffect.getTarget());
        this.textField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                EventEffectChangeFamePanel.this.worldEventEffect.setTarget(EventEffectChangeFamePanel.this.textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                EventEffectChangeFamePanel.this.worldEventEffect.setTarget(EventEffectChangeFamePanel.this.textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                EventEffectChangeFamePanel.this.worldEventEffect.setTarget(EventEffectChangeFamePanel.this.textField.getText());
            }
        });
        final JSpinner spinner = new JSpinner(new SpinnerNumberModel((Number)0, null, null, (Number)1));
        spinner.setValue(this.worldEventEffect.getValue());
        this.add((Component)spinner, "4, 1");
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                EventEffectChangeFamePanel.this.worldEventEffect.setValue((Integer)spinner.getValue());
            }
        });
    }
}

