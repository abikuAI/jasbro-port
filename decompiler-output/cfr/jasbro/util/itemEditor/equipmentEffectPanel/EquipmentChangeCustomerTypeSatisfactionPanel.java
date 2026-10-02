/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor.equipmentEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.events.business.CustomerType;
import jasbro.game.items.equipmentEffect.EquipmentChangeCustomerTypeSatisfaction;
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

public class EquipmentChangeCustomerTypeSatisfactionPanel
extends JPanel {
    private EquipmentChangeCustomerTypeSatisfaction itemEffect;

    public EquipmentChangeCustomerTypeSatisfactionPanel(EquipmentEffect equipmentEffect) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.add((Component)new JLabel(equipmentEffect.getName()), "1, 1, left, center");
        this.itemEffect = (EquipmentChangeCustomerTypeSatisfaction)equipmentEffect;
        this.add((Component)new JLabel(TextUtil.t("customertype")), "1, 2, left, center");
        final JComboBox<CustomerType> customerTypeComboBox = new JComboBox<CustomerType>();
        this.add(customerTypeComboBox, "2, 2, fill, top");
        for (CustomerType customerType : CustomerType.values()) {
            customerTypeComboBox.addItem(customerType);
        }
        customerTypeComboBox.setSelectedItem((Object)this.itemEffect.getCustomerType());
        customerTypeComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EquipmentChangeCustomerTypeSatisfactionPanel.this.itemEffect.setCustomerType((CustomerType)((Object)customerTypeComboBox.getSelectedItem()));
            }
        });
        this.add((Component)new JLabel(TextUtil.t("ui.change")), "1, 3, left, center");
        final JSpinner spinner = new JSpinner();
        spinner.setValue(this.itemEffect.getAmount());
        this.add((Component)spinner, "2, 3, fill, top");
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                EquipmentChangeCustomerTypeSatisfactionPanel.this.itemEffect.setAmount((Integer)spinner.getValue());
            }
        });
    }
}

