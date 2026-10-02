package jasbro.game.items.ingredientEffect;

import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemAddGoldPanel;
import javax.swing.JPanel;

public enum IngredientItemEffectType {
   CHANGEATTRIBUTE(IngredientItemChangeAttribute.class, UsableItemAddGoldPanel.class);

   private Class<? extends IngredientItemEffect> itemEffectClass;
   private Class<? extends JPanel> itemEffectPanelClass;

   IngredientItemEffectType(Class<? extends IngredientItemEffect> itemEffectClass, Class<? extends JPanel> itemEffectPanelClass) {
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
