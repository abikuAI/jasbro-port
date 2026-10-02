package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentDailyAttributeChange extends EquipmentEffect {
   private AttributeType attributeType;
   private int amount;

   @Override
   public void handleEvent(MyEvent e, Charakter character) {
      if (this.attributeType != null && e.getType() == EventType.NEXTDAY) {
         character.getAttribute(this.attributeType).addToValue(this.amount);
      }
   }

   public AttributeType getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(AttributeType attributeType) {
      this.attributeType = attributeType;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.DAILYATTRIBUTECHANGE;
   }

   @Override
   public String getDescription() {
      if (this.attributeType == null) {
         return "";
      } else {
         return this.amount < 0
            ? TextUtil.t("equipment.dailyMinus", this.attributeType.getText(), this.amount)
            : TextUtil.t("equipment.dailyPlus", this.attributeType.getText(), this.amount);
      }
   }

   @Override
   public double getValue() {
      if (this.attributeType == null) {
         return 0.0;
      } else if (this.attributeType instanceof BaseAttributeTypes) {
         return 10000.0;
      } else {
         return this.attributeType instanceof EssentialAttributes ? 1000.0 : 2500.0;
      }
   }

   @Override
   public int getAmountEffects() {
      return this.amount;
   }
}
