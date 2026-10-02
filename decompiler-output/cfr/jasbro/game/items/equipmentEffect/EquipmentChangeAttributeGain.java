/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttributeGain
extends EquipmentEffect {
    private AttributeType attributeType;
    private int amountPercent;

    @Override
    public void handleEvent(MyEvent e, Charakter character) {
        AttributeModification attributeModification;
        if (this.attributeType != null && e.getType() == EventType.ATTRIBUTECHANGE && (attributeModification = (AttributeModification)e.getSource()).getAttributeType() == this.attributeType) {
            float modification = attributeModification.getBaseAmount();
            float change = Math.abs(modification) * (float)this.amountPercent / 100.0f;
            attributeModification.addModificator(Float.valueOf(change));
        }
    }

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public int getAmountPercent() {
        return this.amountPercent;
    }

    public void setAmountPercent(int amountPercent) {
        this.amountPercent = amountPercent;
    }

    @Override
    public EquipmentEffectType getType() {
        return EquipmentEffectType.CHANGEATTRIBUTEGAIN;
    }

    @Override
    public String getDescription() {
        if (this.attributeType == null) {
            return "";
        }
        if (this.amountPercent < 0) {
            return TextUtil.t("equipment.attributeGainMinus", this.attributeType.getText(), this.amountPercent);
        }
        return TextUtil.t("equipment.attributeGainPlus", this.attributeType.getText(), this.amountPercent);
    }

    @Override
    public double getValue() {
        if (this.attributeType == null) {
            return 0.0;
        }
        if (this.attributeType instanceof BaseAttributeTypes) {
            return 300.0;
        }
        if (this.attributeType instanceof EssentialAttributes) {
            return 1000.0;
        }
        return 50.0;
    }

    @Override
    public int getAmountEffects() {
        return this.amountPercent;
    }
}

