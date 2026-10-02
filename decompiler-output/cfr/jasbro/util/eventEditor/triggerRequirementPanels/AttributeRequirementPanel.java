/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.requirements.AttributeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AttributeRequirementPanel
extends JPanel {
    private AttributeRequirement triggerRequirement;

    /*
     * WARNING - void declaration
     */
    public AttributeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        void var6_17;
        this.triggerRequirement = (AttributeRequirement)triggerRequirementTmp;
        this.setLayout(new BoxLayout(this, 0));
        final JComboBox<Enum> attributeTypeCombobox = new JComboBox<Enum>();
        this.add(attributeTypeCombobox, "1, 1");
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
        for (Enum enum_ : CalculatedAttribute.values()) {
            attributeTypeCombobox.addItem(enum_);
        }
        attributeTypeCombobox.setSelectedItem(this.triggerRequirement.getAttributeType());
        attributeTypeCombobox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AttributeRequirementPanel.this.triggerRequirement.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
            }
        });
        final JComboBox<TriggerRequirement.Comparison> comboBox = new JComboBox<TriggerRequirement.Comparison>();
        this.add(comboBox);
        TriggerRequirement.Comparison[] arr$ = TriggerRequirement.Comparison.values();
        int len$ = arr$.length;
        boolean bl = false;
        while (var6_17 < len$) {
            TriggerRequirement.Comparison dayComparison = arr$[var6_17];
            comboBox.addItem(dayComparison);
            ++var6_17;
        }
        comboBox.setSelectedItem((Object)this.triggerRequirement.getComparison());
        comboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AttributeRequirementPanel.this.triggerRequirement.setComparison((TriggerRequirement.Comparison)((Object)comboBox.getSelectedItem()));
            }
        });
        final JSpinner spinner = new JSpinner();
        spinner.setValue(this.triggerRequirement.getAmount());
        this.add(spinner);
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                AttributeRequirementPanel.this.triggerRequirement.setAmount((Integer)spinner.getValue());
            }
        });
    }
}

