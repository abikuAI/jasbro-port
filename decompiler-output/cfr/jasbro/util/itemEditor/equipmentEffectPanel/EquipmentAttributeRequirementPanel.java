/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.equipmentEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentAttributeRequirement;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
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

public class EquipmentAttributeRequirementPanel
extends JPanel {
    private EquipmentAttributeRequirement itemEffect;

    public EquipmentAttributeRequirementPanel(EquipmentEffect equipmentEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(equipmentEffect.getName()), "1, 1, left, center");
        this.itemEffect = (EquipmentAttributeRequirement)equipmentEffect;
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
        attributeTypeCombobox.setSelectedItem(this.itemEffect.getAttributeType());
        attributeTypeCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EquipmentAttributeRequirement effect = EquipmentAttributeRequirementPanel.this.itemEffect;
                effect.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
            }
        });
        this.add((Component)new JLabel(TextUtil.t("ui.percent")), "1, 3, left, center");
        final JSpinner spinner = new JSpinner();
        spinner.setValue(this.itemEffect.getAmount());
        this.add((Component)spinner, "2, 3, fill, top");
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                EquipmentAttributeRequirement effect = EquipmentAttributeRequirementPanel.this.itemEffect;
                effect.setAmount((Integer)spinner.getValue());
            }
        });
    }
}

