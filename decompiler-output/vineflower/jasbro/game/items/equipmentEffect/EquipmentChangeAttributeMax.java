package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttributeMax extends EquipmentEffect {
   private AttributeType attributeType;
   private int amount;

   @Override
   public void doAtEquip(Charakter character) {
      if (this.attributeType != null && this.amount != 0) {
         Attribute attribute = character.getAttribute(this.attributeType);
         attribute.setMaxValue(attribute.getMaxValue() + this.amount);
      }
   }

   @Override
   public void doAtUnEquip(Charakter character) {
      if (this.attributeType != null && this.amount != 0) {
         Attribute attribute = character.getAttribute(this.attributeType);
         attribute.setMaxValue(attribute.getMaxValue() - this.amount);
      }
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
      return EquipmentEffectType.CHANGEATTRIBUTEMAX;
   }

   @Override
   public String getDescription() {
      if (this.attributeType == null) {
         return "";
      } else {
         return this.amount < 0
            ? TextUtil.t("equipment.attributeMaxMinus", this.attributeType.getText(), this.amount)
            : TextUtil.t("equipment.attributeMaxPlus", this.attributeType.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      if (this.attributeType == null) {
         return 0.0;
      } else {
         return this.attributeType instanceof BaseAttributeTypes ? 300.0 : 50.0;
      }
   }

   @Override
   public int getAmountEffects() {
      return this.amount;
   }
}
