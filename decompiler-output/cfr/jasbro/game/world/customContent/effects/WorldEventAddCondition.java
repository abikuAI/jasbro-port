/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.character.Condition;
import jasbro.game.character.conditions.ConditionType;
import jasbro.game.character.conditions.Illness;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventAddCondition
extends WorldEventEffect {
    private static final Logger LOGGER = LogManager.getLogger(WorldEventAddCondition.class);
    private ConditionType conditionType;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        if (this.conditionType != null) {
            try {
                Condition condition = this.conditionType.getConditionClass().newInstance();
                if (this.conditionType == ConditionType.SMALLPOX) {
                    ((Illness.Smallpox)condition).setShowMessage(false);
                }
                worldEvent.getCharacters().get(0).addCondition(condition);
            }
            catch (ReflectiveOperationException e) {
                LOGGER.error("Failed to creat an instance of '{}'", new Object[]{Condition.class.getName()});
                LOGGER.throwing((Throwable)e);
            }
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.ADDCONDITION;
    }

    public ConditionType getConditionType() {
        return this.conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
}

