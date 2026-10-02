package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.texts.TextUtil;

public class EquipmentChangeCalculatedAttributeFixed extends EquipmentEffect {
   private CalculatedAttribute attributeType;
   private double amount;

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.CHANGECALCULATEDATTRIBUTEFIXED;
   }

   @Override
   public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
      return attribute != null && attribute == this.attributeType && this.amount != 0.0
         ? value + this.amount
         : super.modifyCalculatedAttribute(attribute, value, character);
   }

   public CalculatedAttribute getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(CalculatedAttribute attribute) {
      this.attributeType = attribute;
   }

   public double getAmount() {
      return this.amount;
   }

   public void setAmount(double amount) {
      this.amount = amount;
   }

   @Override
   public String getDescription() {
      if (this.attributeType == null) {
         return "";
      } else if (this.attributeType != CalculatedAttribute.HIT
         && this.attributeType != CalculatedAttribute.DODGE
         && this.attributeType != CalculatedAttribute.BLOCKAMOUNT
         && this.attributeType != CalculatedAttribute.BLOCKCHANCE
         && this.attributeType != CalculatedAttribute.CRITCHANCE
         && this.attributeType != CalculatedAttribute.CRITDAMAGEAMOUNT
         && this.attributeType != CalculatedAttribute.CHANCEADDITIONALCHILD
         && this.attributeType != CalculatedAttribute.PREGNANCYCHANCE) {
         return this.amount < 0.0
            ? TextUtil.t("equipment.valueMinus", this.attributeType.getText(), this.amount)
            : TextUtil.t("equipment.valuePlus", this.attributeType.getText(), this.amount);
      } else {
         return this.amount < 0.0
            ? TextUtil.t("equipment.percentMinus", this.attributeType.getText(), this.amount)
            : TextUtil.t("equipment.percentPlus", this.attributeType.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      if (this.attributeType == null) {
         return 0.0;
      }

      switch (this.attributeType) {
         case DAMAGE:
            return 150.0;
         case ARMORVALUE:
            return 15.0;
         case ARMORPERCENT:
            return 10000.0;
         case HIT:
            return 75.0;
         case DODGE:
            return 100.0;
         case BLOCKAMOUNT:
         case BLOCKCHANCE:
         case CRITCHANCE:
         case CRITDAMAGEAMOUNT:
            return 50.0;
         case SPEED:
            return 50.0;
         case MINCHILDREN:
            return 1000.0;
         case MAXCHILDREN:
            return 1000.0;
         case AMOUNTCUSTOMERSPERSHIFT:
            return 10000.0;
         case ITEMLOOTCHANCEMODIFIER:
            return 100.0;
         case PREGNANCYCHANCE:
            return 10.0;
         case CHANCEADDITIONALCHILD:
            return 100.0;
         case CONTROL:
            return 250.0;
         default:
            return 10.0;
      }
   }

   @Override
   public int getAmountEffects() {
      if (this.attributeType == null) {
         return 0;
      } else if (this.attributeType == CalculatedAttribute.DAMAGE) {
         return (int)(this.amount * 10.0);
      } else {
         return this.attributeType == CalculatedAttribute.PREGNANCYDURATIONMODIFIER ? (int)(-this.amount) : (int)this.amount;
      }
   }
}
