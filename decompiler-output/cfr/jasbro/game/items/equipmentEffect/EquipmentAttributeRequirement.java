/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;

public class EquipmentAttributeRequirement
extends EquipmentEffect {
    private AttributeType attributeType;
    private int amount;

    @Override
    public boolean canEquip(Charakter character) {
        return this.attributeType == null || this.amount <= 0 || !(character.getAttribute(this.attributeType).getInternValue() < (float)this.amount);
    }

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(AttributeType attribute) {
        this.attributeType = attribute;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public EquipmentEffectType getType() {
        return EquipmentEffectType.ATTRIBUTEREQUIREMENT;
    }

    @Override
    public String getDescription() {
        if (this.attributeType == null) {
            return "";
        }
        return TextUtil.t("equipment.attributeRequirement", this.amount, this.attributeType.getText());
    }

    @Override
    public double getValue() {
        return 0.0;
    }

    @Override
    public int getAmountEffects() {
        return 0;
    }
}

