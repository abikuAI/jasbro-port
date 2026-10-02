/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.usableItemEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.usableItemEffects.UsableItemCooldown;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class UsableItemCooldownPanel
extends JPanel {
    private UsableItemCooldown itemEffect;

    public UsableItemCooldownPanel(UsableItemEffect usableItemEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(usableItemEffect.getName()), "1, 1, left, center");
        this.itemEffect = (UsableItemCooldown)usableItemEffect;
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel((Number)0, null, null, (Number)1));
        spinner.setValue(this.itemEffect.getTime());
        this.add((Component)spinner, "2, 1, fill, top");
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                UsableItemCooldownPanel.this.itemEffect.setTime((Integer)spinner.getValue());
            }
        });
    }
}

