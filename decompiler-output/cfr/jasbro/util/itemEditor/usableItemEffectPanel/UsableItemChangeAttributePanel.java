/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.usableItemEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.usableItemEffects.UsableItemChangeAttribute;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class UsableItemChangeAttributePanel
extends JPanel {
    private UsableItemChangeAttribute itemEffect;

    public UsableItemChangeAttributePanel(UsableItemEffect usableItemEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(usableItemEffect.getName()), "1, 1, left, center");
        this.itemEffect = (UsableItemChangeAttribute)usableItemEffect;
        this.add((Component)new JLabel(TextUtil.t("attribute")), "1, 2, left, center");
        final JComboBox<Enum> attributeTypeCombobox = new JComboBox<Enum>();
        this.add(attributeTypeCombobox, "2, 2, fill, top");
        for (EssentialAttributes essentialAttributes : EssentialAttributes.values()) {
            attributeTypeCombobox.addItem(essentialAttributes);
        }
        for (Enum enum_ : BaseAttributeTypes.values()) {
            attributeTypeCombobox.addItem(enum_);
        }
        for (Enum enum_ : Sextype.values()) {
            attributeTypeCombobox.addItem(enum_);
        }
        for (Enum enum_ : SpecializationAttribute.values()) {
            attributeTypeCombobox.addItem(enum_);
        }
        attributeTypeCombobox.setSelectedItem(this.itemEffect.getAttribute());
        attributeTypeCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                UsableItemChangeAttribute effect = UsableItemChangeAttributePanel.this.itemEffect;
                effect.setAttribute((AttributeType)attributeTypeCombobox.getSelectedItem());
            }
        });
        this.add((Component)new JLabel(TextUtil.t("ui.minChange")), "1, 3, left, center");
        final JSpinner spinner = new JSpinner();
        spinner.setValue(this.itemEffect.getMinChange());
        this.add((Component)spinner, "2, 3, fill, top");
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                UsableItemChangeAttribute effect = UsableItemChangeAttributePanel.this.itemEffect;
                effect.setMinChange((Integer)spinner.getValue());
            }
        });
        this.add((Component)new JLabel(TextUtil.t("ui.maxChange")), "1, 4, left, center");
        final JSpinner spinner2 = new JSpinner();
        spinner2.setValue(this.itemEffect.getMaxChange());
        this.add((Component)spinner2, "2, 4, fill, top");
        spinner2.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                UsableItemChangeAttribute effect = UsableItemChangeAttributePanel.this.itemEffect;
                effect.setMaxChange((Integer)spinner2.getValue());
            }
        });
    }
}

