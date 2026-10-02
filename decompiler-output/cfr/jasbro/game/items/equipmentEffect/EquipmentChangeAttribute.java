/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.equipmentEffect;

import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttribute
extends EquipmentEffect {
    private AttributeType attributeType;
    private int amount;

    @Override
    public float getAttributeModifier(Attribute attribute) {
        if (attribute.getAttributeType() == this.attributeType) {
            return this.amount;
        }
        return 0.0f;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public EquipmentEffectType getType() {
        return EquipmentEffectType.CHANGEATTRIBUTE;
    }

    @Override
    public String getDescription() {
        if (this.attributeType == null) {
            return "";
        }
        if (this.amount < 0) {
            return TextUtil.t("equipment.valueMinus", this.attributeType.getText(), this.amount);
        }
        return TextUtil.t("equipment.valuePlus", this.attributeType.getText(), this.amount);
    }

    @Override
    public double getValue() {
        if (this.attributeType == null) {
            return 0.0;
        }
        if (this.attributeType instanceof BaseAttributeTypes) {
            return 200.0;
        }
        return 25.0;
    }

    @Override
    public int getAmountEffects() {
        return this.amount;
    }
}

