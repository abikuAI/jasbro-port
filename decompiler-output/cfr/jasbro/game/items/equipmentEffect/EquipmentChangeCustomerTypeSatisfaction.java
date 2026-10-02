/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;

public class EquipmentChangeCustomerTypeSatisfaction
extends EquipmentEffect {
    private CustomerType customerType;
    private int amount;

    @Override
    public EquipmentEffectType getType() {
        return EquipmentEffectType.CHANGECUSTOMERTYPESATISFACTION;
    }

    @Override
    public void handleEvent(MyEvent e, Charakter character) {
        Customer customer;
        BusinessMainActivity businessMainActivity;
        RunningActivity activity;
        if (e.getType() == EventType.ACTIVITY && this.customerType != null && (activity = (RunningActivity)e.getSource()) instanceof BusinessMainActivity && (businessMainActivity = (BusinessMainActivity)((Object)activity)).getMainCustomers().size() == 1 && (customer = activity.getMainCustomer()).getType() == this.customerType) {
            customer.addToSatisfaction(this.amount, "Equipment");
        }
    }

    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String getDescription() {
        if (this.customerType == null) {
            return "";
        }
        if (this.amount < 0) {
            return TextUtil.t("equipment.customerSatisfactionMinus", this.customerType.getText(), this.amount);
        }
        return TextUtil.t("equipment.customerSatisfactionPlus", this.customerType.getText(), this.amount);
    }

    @Override
    public double getValue() {
        if (this.customerType == null) {
            return 0.0;
        }
        if (this.customerType != CustomerType.GROUP) {
            return CustomerType.generateCustomer(this.customerType).getImportance() * 10.0f;
        }
        return 200.0;
    }

    @Override
    public int getAmountEffects() {
        return this.amount;
    }
}

