/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.requirements.CharacterRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;

public class AttributeRequirement
extends TriggerRequirement
implements CharacterRequirement {
    private AttributeType attributeType;
    private int amount;
    private TriggerRequirement.Comparison comparison = TriggerRequirement.Comparison.GREATERTHAN;

    @Override
    public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
        if (this.attributeType != null) {
            double attributeValue = character.getAnyAttributeValue(this.attributeType);
            switch (this.comparison) {
                case GREATERTHAN: {
                    return attributeValue > (double)this.amount;
                }
                case LESSTHAN: {
                    return attributeValue < (double)this.amount;
                }
                case EQUAL: {
                    return attributeValue == (double)this.amount;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
            return false;
        }
        return this.isValid(triggerParent.getCharacters().get(0), triggerParent);
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.ATTRIBUTEREQUIREMENT;
    }

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TriggerRequirement.Comparison getComparison() {
        return this.comparison;
    }

    public void setComparison(TriggerRequirement.Comparison comparison) {
        this.comparison = comparison;
    }
}

