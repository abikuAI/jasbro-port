package jasbro.game.character.attributes;

import jasbro.game.interfaces.AttributeType;

public class AttributeAmount {
   private AttributeType attributeType;
   private float amount;

   public AttributeAmount() {
   }

   public AttributeAmount(AttributeType attributeType, float amount) {
      this.attributeType = attributeType;
      this.amount = amount;
   }

   public AttributeType getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(AttributeType attributeType) {
      this.attributeType = attributeType;
   }

   public float getAmount() {
      return this.amount;
   }

   public void setAmount(float amount) {
      this.amount = amount;
   }
}
