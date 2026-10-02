/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventComment;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectCommentPanel
extends JPanel {
    private WorldEventComment worldEventEffect;

    public EventEffectCommentPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("80dlu")}));
        this.worldEventEffect = (WorldEventComment)worldEventEffectTmp;
        final JTextArea textArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(31);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.worldEventEffect.getComment());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                EventEffectCommentPanel.this.worldEventEffect.setComment(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                EventEffectCommentPanel.this.worldEventEffect.setComment(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                EventEffectCommentPanel.this.worldEventEffect.setComment(textArea.getText());
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

