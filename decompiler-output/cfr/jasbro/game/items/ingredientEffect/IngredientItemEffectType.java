/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.ingredientEffect;

import jasbro.game.items.ingredientEffect.IngredientItemChangeAttribute;
import jasbro.game.items.ingredientEffect.IngredientItemEffect;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemAddGoldPanel;
import javax.swing.JPanel;

public enum IngredientItemEffectType {
    CHANGEATTRIBUTE(IngredientItemChangeAttribute.class, UsableItemAddGoldPanel.class);

    private Class<? extends IngredientItemEffect> itemEffectClass;
    private Class<? extends JPanel> itemEffectPanelClass;

    private IngredientItemEffectType(Class<? extends IngredientItemEffect> itemEffectClass, Class<? extends JPanel> itemEffectPanelClass) {
        this.itemEffectClass = itemEffectClass;
        this.itemEffectPanelClass = itemEffectPanelClass;
    }

    public Class<? extends IngredientItemEffect> getItemEffectClass() {
        return this.itemEffectClass;
    }

    public Class<? extends JPanel> getItemEffectPanelClass() {
        return this.itemEffectPanelClass;
    }
}

