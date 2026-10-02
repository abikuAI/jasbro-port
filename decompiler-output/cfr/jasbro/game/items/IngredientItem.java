/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items;

import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.ingredientEffect.IngredientItemEffect;
import jasbro.texts.TextUtil;

public class IngredientItem
extends Item {
    private IngredientItemEffect itemEffect;

    public IngredientItem(String id) {
        super(id, ItemType.INGREDIENT);
    }

    public IngredientItem(Item item) {
        super(item);
        this.setType(ItemType.INGREDIENT);
    }

    @Override
    public String getText() {
        return "<b>" + this.getName() + "</b>\n" + TextUtil.t("typeConsumable") + "\n" + TextUtil.t("valueItem", this.getValue()) + "\n" + this.getDescription();
    }

    public IngredientItemEffect getItemEffect() {
        return this.itemEffect;
    }

    public void setItemEffect(IngredientItemEffect itemEffect) {
        this.itemEffect = itemEffect;
    }
}

