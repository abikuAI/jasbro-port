/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import jasbro.game.world.customContent.requirements.AndRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

public class WorldEventCondition
extends WorldEventEffectContainer {
    private TriggerRequirement requirement = new AndRequirement();

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        if (this.requirement == null) {
            return;
        }
        if (this.requirement.isValid(worldEvent)) {
            if (this.getSubEffects().size() > 0) {
                this.getSubEffects().get(0).perform(worldEvent);
            }
        } else if (this.getSubEffects().size() > 1) {
            this.getSubEffects().get(1).perform(worldEvent);
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.CONDITION;
    }

    @Override
    public void addEffect(WorldEventEffect worldEventEffect) {
        if (this.getSubEffects().size() < 2) {
            super.addEffect(worldEventEffect);
        }
    }

    @Override
    public boolean canAddSubEffect() {
        return this.getSubEffects().size() < 2;
    }

    public TriggerRequirement getRequirement() {
        return this.requirement;
    }

    public void setRequirement(TriggerRequirement requirement) {
        this.requirement = requirement;
    }
}

