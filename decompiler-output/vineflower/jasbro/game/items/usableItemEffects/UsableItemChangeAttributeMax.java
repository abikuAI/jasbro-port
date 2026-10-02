package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.Item;

public class UsableItemChangeAttributeMax extends UsableItemEffect {
   private AttributeType attribute;
   private int change;
   private int max;

   @Override
   public String getName() {
      return "Change attribute max";
   }

   @Override
   public void apply(Charakter character, Item item) {
      Attribute attribute = character.getAttribute(this.attribute);
      if (this.max == 0 || attribute.getMaxValue() < this.max) {
         attribute.setMaxValue(attribute.getMaxValue() + this.change);
         if (this.max != 0 && attribute.getMaxValue() > this.max) {
            attribute.setMaxValue(this.max);
         }
      }
   }

   public AttributeType getAttribute() {
      return this.attribute;
   }

   public void setAttribute(AttributeType attribute) {
      this.attribute = attribute;
   }

   public int getChange() {
      return this.change;
   }

   public void setChange(int change) {
      this.change = change;
   }

   @Override
   public UsableItemEffectType getType() {
      return UsableItemEffectType.CHANGEATTRIBUTEMAX;
   }

   public int getMax() {
      return this.max;
   }

   public void setMax(int max) {
      this.max = max;
   }
}
