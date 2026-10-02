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
import jasbro.game.world.customContent.effects.WorldEventAddToMessage;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectAddToMessagePanel
extends JPanel {
    private WorldEventAddToMessage worldEventEffect;

    public EventEffectAddToMessagePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("80dlu")}));
        this.worldEventEffect = (WorldEventAddToMessage)worldEventEffectTmp;
        final JCheckBox chckbxNewCheckBox = new JCheckBox(TextUtil.t("eventEditor.makePriorityMessage"));
        this.add((Component)chckbxNewCheckBox, "1, 1");
        chckbxNewCheckBox.setSelected(this.worldEventEffect.isChangeToPriorityMessage());
        chckbxNewCheckBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EventEffectAddToMessagePanel.this.worldEventEffect.setChangeToPriorityMessage(chckbxNewCheckBox.isSelected());
            }
        });
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(31);
        this.add((Component)scrollPane, "1, 2, fill, fill");
        final JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.worldEventEffect.getText());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                EventEffectAddToMessagePanel.this.worldEventEffect.setText(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                EventEffectAddToMessagePanel.this.worldEventEffect.setText(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                EventEffectAddToMessagePanel.this.worldEventEffect.setText(textArea.getText());
            }
        });
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }
}

