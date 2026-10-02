/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;

public class EquipmentChangeArmor
extends EquipmentEffect {
    private int amount;

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public EquipmentEffectType getType() {
        return EquipmentEffectType.CHANGEARMOR;
    }

    @Override
    public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
        if (attribute == CalculatedAttribute.ARMORVALUE && this.amount != 0) {
            return value + (double)this.amount;
        }
        return super.modifyCalculatedAttribute(attribute, value, character);
    }

    @Override
    public String getDescription() {
        if (this.amount == 0) {
            return "";
        }
        if (this.amount < 0) {
            return TextUtil.t("equipment.valueMinus", CalculatedAttribute.ARMORVALUE.getText(), this.amount);
        }
        return TextUtil.t("equipment.valuePlus", CalculatedAttribute.ARMORVALUE.getText(), this.amount);
    }

    @Override
    public double getValue() {
        return 15.0;
    }

    @Override
    public int getAmountEffects() {
        return this.amount;
    }
}

