/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSimpleMessage;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.EventEditor;
import jasbro.util.eventEditor.ImageSelectionPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectSimpleMessagePanel
extends JPanel {
    private WorldEventSimpleMessage worldEventEffect;
    private JPanel imagesPanel;
    private WorldEvent worldEvent;

    public EventEffectSimpleMessagePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("80dlu"), FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow"), RowSpec.decode("default:grow")}));
        this.worldEventEffect = (WorldEventSimpleMessage)worldEventEffectTmp;
        this.worldEvent = worldEventTmp;
        final JTextArea textArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(31);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.worldEventEffect.getMessage());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                EventEffectSimpleMessagePanel.this.worldEventEffect.setMessage(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                EventEffectSimpleMessagePanel.this.worldEventEffect.setMessage(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                EventEffectSimpleMessagePanel.this.worldEventEffect.setMessage(textArea.getText());
            }
        });
        this.add((Component)scrollPane, "1, 1, 2, 1, fill, fill");
        final JCheckBox importanceCheckbox = new JCheckBox(TextUtil.t("eventEditor.importantMessage"));
        this.add((Component)importanceCheckbox, "1, 2");
        importanceCheckbox.setToolTipText(TextUtil.t("eventEditor.importantMessage.title"));
        importanceCheckbox.setSelected(this.worldEventEffect.isImportantMessage());
        importanceCheckbox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EventEffectSimpleMessagePanel.this.worldEventEffect.setImportantMessage(importanceCheckbox.isSelected());
            }
        });
        JButton btnNewButton = new JButton(TextUtil.t("eventEditor.addImage"));
        this.add((Component)btnNewButton, "2, 2");
        btnNewButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EventEffectSimpleMessagePanel.this.worldEventEffect.getImages().add(new ImageSelection());
                EventEffectSimpleMessagePanel.this.updateImagesPanel();
            }
        });
        this.imagesPanel = new JPanel();
        this.add((Component)this.imagesPanel, "1, 3, 2, 1, fill, fill");
        this.imagesPanel.setLayout(new BoxLayout(this.imagesPanel, 1));
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.background"));
        this.add((Component)lblNewLabel, "1, 4");
        ImageSelectionPanel imageSelectionPanel = new ImageSelectionPanel(this.worldEvent, this.worldEventEffect.getBackground(), null);
        this.add((Component)imageSelectionPanel, "2, 4, fill, fill");
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
        this.updateImagesPanel();
    }

    public void updateImagesPanel() {
        this.imagesPanel.removeAll();
        for (final ImageSelection imageSelection : this.worldEventEffect.getImages()) {
            ImageSelectionPanel selectionPanel = new ImageSelectionPanel(this.worldEvent, imageSelection, new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    EventEffectSimpleMessagePanel.this.worldEventEffect.getImages().remove(imageSelection);
                    EventEffectSimpleMessagePanel.this.updateImagesPanel();
                }
            });
            this.imagesPanel.add(selectionPanel);
        }
        EventEditor.getInstance().validate();
        EventEditor.getInstance().repaint();
    }
}

