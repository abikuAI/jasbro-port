/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.usableItemEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.usableItemEffects.UsableItemAddTrait;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UsableItemAddTraitPanel
extends JPanel {
    private UsableItemAddTrait itemEffect;

    public UsableItemAddTraitPanel(UsableItemEffect usableItemEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(usableItemEffect.getName()), "1, 1, left, center");
        this.itemEffect = (UsableItemAddTrait)usableItemEffect;
        this.add((Component)new JLabel(TextUtil.t("trait")), "1, 2, left, center");
        final JComboBox<Trait> traitCombobox = new JComboBox<Trait>();
        this.add(traitCombobox, "2, 2, fill, top");
        for (Trait trait : Trait.getBasicTraits()) {
            traitCombobox.addItem(trait);
        }
        traitCombobox.setSelectedItem(this.itemEffect.getTrait());
        traitCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                UsableItemAddTraitPanel.this.itemEffect.setTrait((Trait)traitCombobox.getSelectedItem());
            }
        });
    }
}

