package jasbro.game.items.equipmentEffect;

import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttribute extends EquipmentEffect {
   private AttributeType attributeType;
   private int amount;

   @Override
   public float getAttributeModifier(Attribute attribute) {
      return attribute.getAttributeType() == this.attributeType ? this.amount : 0.0F;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public AttributeType getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(AttributeType attributeType) {
      this.attributeType = attributeType;
   }

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.CHANGEATTRIBUTE;
   }

   @Override
   public String getDescription() {
      if (this.attributeType == null) {
         return "";
      } else {
         return this.amount < 0
            ? TextUtil.t("equipment.valueMinus", this.attributeType.getText(), this.amount)
            : TextUtil.t("equipment.valuePlus", this.attributeType.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      if (this.attributeType == null) {
         return 0.0;
      } else {
         return this.attributeType instanceof BaseAttributeTypes ? 200.0 : 25.0;
      }
   }

   @Override
   public int getAmountEffects() {
      return this.amount;
   }
}
