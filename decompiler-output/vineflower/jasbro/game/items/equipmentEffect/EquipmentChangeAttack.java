package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttack extends EquipmentEffect {
   private float amount;

   @Override
   public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
      return attribute != null && attribute == CalculatedAttribute.DAMAGE && this.amount != 0.0F
         ? value + this.amount
         : super.modifyCalculatedAttribute(attribute, value, character);
   }

   public float getAmount() {
      return this.amount;
   }

   public void setAmount(float amount) {
      this.amount = amount;
   }

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.CHANGEATTACK;
   }

   @Override
   public String getDescription() {
      if (this.amount == 0.0F) {
         return "";
      } else {
         return this.amount < 0.0F
            ? TextUtil.t("equipment.valueMinus", CalculatedAttribute.DAMAGE.getText(), this.amount)
            : TextUtil.t("equipment.valuePlus", CalculatedAttribute.DAMAGE.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      return 150.0;
   }

   @Override
   public int getAmountEffects() {
      return (int)(this.amount * 10.0F);
   }
}
