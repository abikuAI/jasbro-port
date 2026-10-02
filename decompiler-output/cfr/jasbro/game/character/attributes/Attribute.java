/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.attributes;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.io.Serializable;

public class Attribute
implements Serializable {
    private AttributeType attributeType;
    private float internValue = 0.0f;
    private transient int minValue = 0;
    private int maxValue = 100;
    private Charakter character;

    public Attribute(Charakter character, AttributeType attributeType) {
        this.character = character;
        this.attributeType = attributeType;
        this.maxValue = attributeType.getDefaultMax();
        this.minValue = attributeType.getDefaultMin();
        this.internValue = attributeType.getStartValue();
    }

    public Attribute(AttributeType attributeType, float internValue, Charakter character) {
        this(character, attributeType);
        this.internValue = internValue;
        this.maxValue = attributeType.getDefaultMax();
        this.minValue = attributeType.getDefaultMin();
    }

    public int getValue() {
        return this.getCharacter().getFinalValue(this.getAttributeType());
    }

    public int getBonus() {
        return this.getCharacter().getBonus(this.getAttributeType());
    }

    public float addToValue(float modValue) {
        return this.addToValue(modValue, null);
    }

    public float addToValue(float modValue, boolean fixed) {
        return this.addToValue(modValue, null, fixed);
    }

    public float addToValue(float modValue, RunningActivity activity) {
        return this.addToValue(modValue, activity, false);
    }

    public float addToValue(float modValue, RunningActivity activity, boolean fixed) {
        if (!(this.attributeType instanceof EssentialAttributes)) {
            modValue /= 10.0f;
            for (SpecializationType specializationType : this.character.getSpecializations()) {
                if (!specializationType.getAssociatedAttributes().contains(this.attributeType)) continue;
                modValue *= 10.0f;
                break;
            }
            if (!fixed) {
                float factor = 1.0f;
                factor = this.getInternValue() < 10.0f ? 1.0f : (this.getInternValue() < 20.0f ? 0.8f : (this.getInternValue() < 30.0f ? 0.6f : (this.getInternValue() < 40.0f ? 0.4f : (this.getInternValue() < 60.0f ? 0.2f : (this.getInternValue() < 80.0f ? 0.1f : 0.0f)))));
                if (this.attributeType instanceof SpecializationAttribute) {
                    modValue *= factor;
                    modValue += Math.abs(modValue) * (float)(this.character.getIntelligence() - 5) / 2.0f / 100.0f;
                    if (this.internValue > (float)this.attributeType.getDefaultMax()) {
                        int level = (int)(1.0f + (this.internValue - (float)this.attributeType.getDefaultMax()) / (float)this.attributeType.getRaiseMaxBy());
                        int modPercent = level * 5;
                        if (level > 10) {
                            modPercent -= level - 10;
                        }
                        if (level > 15) {
                            modPercent -= level - 15;
                        }
                        modValue -= (float)modPercent * modValue / 100.0f;
                    }
                }
                if (this.attributeType instanceof BaseAttributeTypes || this.attributeType instanceof Sextype) {
                    modValue *= factor;
                }
            }
        } else if (this.attributeType == EssentialAttributes.ENERGY && !fixed) {
            modValue = modValue >= 0.0f ? (modValue += Math.abs(modValue) * ((float)(this.character.getStamina() - 5) * 0.5f) / 100.0f * 0.9f) : (modValue += Math.abs(modValue) * ((float)(this.character.getStamina() - 5) * 0.5f) / 100.0f * 1.1f);
        } else if (this.attributeType == EssentialAttributes.HEALTH && !fixed) {
            modValue += Math.abs(modValue) * (float)(this.character.getStrength() - 5) * 0.5f / 100.0f;
        }
        this.internValue += modValue;
        if (this.internValue < (float)this.minValue) {
            modValue -= this.internValue - (float)this.minValue;
            this.internValue = this.minValue;
        } else if (this.internValue > (float)this.maxValue) {
            modValue -= this.internValue - (float)this.maxValue;
            this.internValue = this.maxValue;
        }
        if (modValue != 0.0f || this.internValue == (float)this.minValue) {
            AttributeChangedEvent event = new AttributeChangedEvent(EventType.ATTRIBUTECHANGED, this, modValue, activity);
            this.getCharacter().handleEvent(event);
        }
        return modValue;
    }

    public String getNameResolved() {
        return TextUtil.t(this.attributeType.toString());
    }

    public float getInternValue() {
        return this.internValue;
    }

    public void setInternValue(float internValue) {
        this.internValue = internValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public String getAttributeName() {
        return this.attributeType.toString();
    }

    public ImageData getIcon() {
        return new ImageData("images/icons/" + this.attributeType.toString().toLowerCase() + ".png");
    }

    public int getMinValue() {
        return this.minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public Charakter getCharacter() {
        return this.character;
    }

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public boolean isMaxed() {
        return (float)this.getMaxValue() == this.internValue;
    }
}

