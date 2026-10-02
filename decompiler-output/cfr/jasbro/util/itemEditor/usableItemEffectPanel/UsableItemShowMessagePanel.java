/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.usableItemEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemShowMessage;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UsableItemShowMessagePanel
extends JPanel {
    private UsableItemShowMessage itemEffect;

    public UsableItemShowMessagePanel(UsableItemEffect usableItemEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(usableItemEffect.getName()), "1, 1, left, center");
        this.itemEffect = (UsableItemShowMessage)usableItemEffect;
        this.add((Component)new JLabel(TextUtil.t("imagetag")), "1, 2, left, center");
        final JComboBox<ImageTag> imageTagComboBox = new JComboBox<ImageTag>();
        this.add(imageTagComboBox, "2, 2, fill, top");
        for (ImageTag imageTag : ImageTag.values()) {
            imageTagComboBox.addItem(imageTag);
        }
        imageTagComboBox.setSelectedItem((Object)this.itemEffect.getImageTag());
        imageTagComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                UsableItemShowMessagePanel.this.itemEffect.setImageTag((ImageTag)((Object)imageTagComboBox.getSelectedItem()));
            }
        });
        this.add((Component)new JLabel(TextUtil.t("ui.message")), "1, 3, left, center");
        final JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.itemEffect.getMessage());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                UsableItemShowMessagePanel.this.itemEffect.setMessage(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                UsableItemShowMessagePanel.this.itemEffect.setMessage(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                UsableItemShowMessagePanel.this.itemEffect.setMessage(textArea.getText());
            }
        });
        this.add((Component)scrollPane, "2, 3, fill, top");
    }
}

