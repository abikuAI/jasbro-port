/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.usableItemEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.conditions.ConditionType;
import jasbro.game.items.usableItemEffects.UsableItemAddCondition;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UsableItemAddConditionPanel
extends JPanel {
    private UsableItemAddCondition itemEffect;

    public UsableItemAddConditionPanel(UsableItemEffect usableItemEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(usableItemEffect.getName()), "1, 1, left, center");
        this.itemEffect = (UsableItemAddCondition)usableItemEffect;
        this.add((Component)new JLabel(TextUtil.t("condition")), "1, 2, left, center");
        final JComboBox<ConditionType> conditionTypeCombobox = new JComboBox<ConditionType>();
        this.add(conditionTypeCombobox, "2, 2, fill, top");
        for (ConditionType conditionType : ConditionType.values()) {
            conditionTypeCombobox.addItem(conditionType);
        }
        conditionTypeCombobox.setSelectedItem((Object)this.itemEffect.getConditionType());
        conditionTypeCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                UsableItemAddConditionPanel.this.itemEffect.setConditionType((ConditionType)((Object)conditionTypeCombobox.getSelectedItem()));
            }
        });
    }
}

