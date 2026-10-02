/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.housing.RoomUnlock;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.items.UnlockItem;
import jasbro.game.world.locations.LocationType;
import jasbro.util.itemEditor.SpawnDataPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UnlockItemEditorPanel
extends JPanel {
    private UnlockItem item;

    public UnlockItemEditorPanel(UnlockItem curItem) {
        this.item = curItem;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow(8)"), FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        JPanel panel = new JPanel();
        this.add((Component)panel, "1, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        JLabel lblUnlocks = new JLabel("Unlocks:");
        panel.add((Component)lblUnlocks, "1, 2, right, default");
        final JComboBox<Object> unlockComboBox = new JComboBox<Object>();
        panel.add(unlockComboBox, "2, 2, fill, default");
        for (HouseType houseType : HouseType.values()) {
            unlockComboBox.addItem(houseType);
        }
        for (RoomUnlock roomUnlock : RoomInfoUtil.getRoomUnlocks()) {
            unlockComboBox.addItem(roomUnlock);
        }
        for (Enum enum_ : LocationType.values()) {
            unlockComboBox.addItem(enum_);
        }
        for (Enum enum_ : SpecializationType.values()) {
            if (enum_ == SpecializationType.TRAINER || enum_ == SpecializationType.SLAVE || enum_ == SpecializationType.UNDERAGE || enum_ == SpecializationType.SEX || ((SpecializationType)enum_).getAssociatedSkillTree() == null) continue;
            unlockComboBox.addItem(enum_);
        }
        unlockComboBox.setSelectedItem(this.item.getUnlockObject());
        unlockComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                UnlockItemEditorPanel.this.item.setUnlockObject((UnlockObject)unlockComboBox.getSelectedItem());
            }
        });
        JLabel lblUnlockDescription = new JLabel("Unlockdescription:");
        panel.add((Component)lblUnlockDescription, "1, 4, right, default");
        final JTextArea textArea = new JTextArea();
        panel.add((Component)textArea, "2, 4, fill, fill");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.item.getUnlockMessage());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                UnlockItemEditorPanel.this.item.setUnlockMessage(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                UnlockItemEditorPanel.this.item.setUnlockMessage(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                UnlockItemEditorPanel.this.item.setUnlockMessage(textArea.getText());
            }
        });
        SpawnDataPanel spawnDataPanel = new SpawnDataPanel(curItem);
        this.add((Component)spawnDataPanel, "3, 1, fill, fill");
        this.validate();
        this.repaint();
    }
}

