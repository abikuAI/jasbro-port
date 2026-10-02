package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.texts.TextUtil;

public class EquipmentChangeArmor extends EquipmentEffect {
   private int amount;

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.CHANGEARMOR;
   }

   @Override
   public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
      return attribute == CalculatedAttribute.ARMORVALUE && this.amount != 0
         ? value + this.amount
         : super.modifyCalculatedAttribute(attribute, value, character);
   }

   @Override
   public String getDescription() {
      if (this.amount == 0) {
         return "";
      } else {
         return this.amount < 0
            ? TextUtil.t("equipment.valueMinus", CalculatedAttribute.ARMORVALUE.getText(), this.amount)
            : TextUtil.t("equipment.valuePlus", CalculatedAttribute.ARMORVALUE.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      return 15.0;
   }

   @Override
   public int getAmountEffects() {
      return this.amount;
   }
}
