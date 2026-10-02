/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.character;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Charakter;
import jasbro.gui.objects.div.TranslucentPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

public class CharacterScreenOptionsPanel
extends TranslucentPanel {
    private Charakter character;
    private JCheckBox contraceptivesCheckbox;

    public CharacterScreenOptionsPanel(Charakter characterTmp) {
        this.setPreferredSize(null);
        this.character = characterTmp;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.contraceptivesCheckbox = new JCheckBox("Use contraceptives");
        this.add((Component)this.contraceptivesCheckbox, "1, 1");
        this.contraceptivesCheckbox.setSelected(this.character.isUsesContraceptives());
        this.contraceptivesCheckbox.setOpaque(false);
        this.contraceptivesCheckbox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                CharacterScreenOptionsPanel.this.character.setUsesContraceptives(CharacterScreenOptionsPanel.this.contraceptivesCheckbox.isSelected());
            }
        });
    }
}

