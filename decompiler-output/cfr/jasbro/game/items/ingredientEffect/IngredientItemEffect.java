/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.ingredientEffect;

import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.game.items.ingredientEffect.IngredientItemEffectType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class IngredientItemEffect
implements Serializable {
    public abstract String getName();

    public abstract void apply(Charakter var1, Item var2);

    public abstract IngredientItemEffectType getType();

    public List<IngredientItemEffect> getSubEffects() {
        return new ArrayList<IngredientItemEffect>();
    }
}

