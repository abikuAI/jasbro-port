/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import java.util.ArrayList;

public class WorldEventChangeAttribute
extends WorldEventEffect {
    private AttributeType attributeType = EssentialAttributes.ENERGY;
    private float value = 0.0f;
    private String target = WorldEvent.WorldEventVariables.character.toString();
    private boolean addToActivity = false;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        Charakter character = (Charakter)worldEvent.getAttribute(this.target);
        AttributeModification modification = new AttributeModification(this.value, this.attributeType, character);
        if (worldEvent.getActivity() != null && !worldEvent.getActivity().isAbort() && this.addToActivity) {
            worldEvent.getActivity().getAttributeModifications().add(modification);
        } else {
            modification.applyModification(worldEvent.getActivity());
            ArrayList<AttributeModification> modifications = (ArrayList<AttributeModification>)worldEvent.getAttribute(WorldEvent.WorldEventVariables.attributemodifications);
            if (modifications == null) {
                modifications = new ArrayList<AttributeModification>();
                worldEvent.putAttribute(WorldEvent.WorldEventVariables.attributemodifications, modifications);
            }
            modifications.add(modification);
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.CHANGEATTRIBUTE;
    }

    public AttributeType getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

