/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.equipmentEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.equipmentEffect.EquipmentAddImageTag;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EquipmentAddImageTagPanel
extends JPanel {
    private EquipmentAddImageTag itemEffect;

    public EquipmentAddImageTagPanel(EquipmentEffect equipmentEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(equipmentEffect.getName()), "1, 1, left, center");
        this.itemEffect = (EquipmentAddImageTag)equipmentEffect;
        this.add((Component)new JLabel(TextUtil.t("imagetag")), "1, 2, left, center");
        final JComboBox<ImageTag> traitCombobox = new JComboBox<ImageTag>();
        this.add(traitCombobox, "2, 2, fill, top");
        for (ImageTag trait : ImageTag.values()) {
            traitCombobox.addItem(trait);
        }
        traitCombobox.setSelectedItem((Object)this.itemEffect.getImageTag());
        traitCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EquipmentAddImageTagPanel.this.itemEffect.setImageTag((ImageTag)((Object)traitCombobox.getSelectedItem()));
            }
        });
    }
}

