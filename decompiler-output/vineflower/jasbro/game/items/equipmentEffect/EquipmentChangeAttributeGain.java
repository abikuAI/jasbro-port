package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttributeGain extends EquipmentEffect {
   private AttributeType attributeType;
   private int amountPercent;

   @Override
   public void handleEvent(MyEvent e, Charakter character) {
      if (this.attributeType != null && e.getType() == EventType.ATTRIBUTECHANGE) {
         AttributeModification attributeModification = (AttributeModification)e.getSource();
         if (attributeModification.getAttributeType() == this.attributeType) {
            float modification = attributeModification.getBaseAmount();
            float change = Math.abs(modification) * this.amountPercent / 100.0F;
            attributeModification.addModificator(change);
         }
      }
   }

   public AttributeType getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(AttributeType attributeType) {
      this.attributeType = attributeType;
   }

   public int getAmountPercent() {
      return this.amountPercent;
   }

   public void setAmountPercent(int amountPercent) {
      this.amountPercent = amountPercent;
   }

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.CHANGEATTRIBUTEGAIN;
   }

   @Override
   public String getDescription() {
      if (this.attributeType == null) {
         return "";
      } else {
         return this.amountPercent < 0
            ? TextUtil.t("equipment.attributeGainMinus", this.attributeType.getText(), this.amountPercent)
            : TextUtil.t("equipment.attributeGainPlus", this.attributeType.getText(), this.amountPercent);
      }
   }

   @Override
   public double getValue() {
      if (this.attributeType == null) {
         return 0.0;
      } else if (this.attributeType instanceof BaseAttributeTypes) {
         return 300.0;
      } else {
         return this.attributeType instanceof EssentialAttributes ? 1000.0 : 50.0;
      }
   }

   @Override
   public int getAmountEffects() {
      return this.amountPercent;
   }
}
