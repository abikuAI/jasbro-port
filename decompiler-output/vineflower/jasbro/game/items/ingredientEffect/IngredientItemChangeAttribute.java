package jasbro.game.items.ingredientEffect;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.Item;

public class IngredientItemChangeAttribute extends IngredientItemEffect {
   private AttributeType attribute;
   private int minChange;
   private int maxChange;

   @Override
   public String getName() {
      return "Change attribute value effect";
   }

   @Override
   public void apply(Charakter character, Item item) {
      if (this.attribute != null) {
         if (this.maxChange < this.minChange) {
            int tmp = this.minChange;
            this.minChange = this.maxChange;
            this.maxChange = tmp;
         }

         character.getAttribute(this.attribute).addToValue(Util.getInt(this.minChange, this.maxChange + 1), true);
      }
   }

   public AttributeType getAttribute() {
      return this.attribute;
   }

   public void setAttribute(AttributeType attribute) {
      this.attribute = attribute;
   }

   public int getMinChange() {
      return this.minChange;
   }

   public void setMinChange(int minChange) {
      this.minChange = minChange;
   }

   public int getMaxChange() {
      return this.maxChange;
   }

   public void setMaxChange(int maxChange) {
      this.maxChange = maxChange;
   }

   @Override
   public IngredientItemEffectType getType() {
      return IngredientItemEffectType.CHANGEATTRIBUTE;
   }
}
