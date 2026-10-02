package jasbro.game.character.attributes;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;

public class AttributeModification {
   private float baseAmount;
   private AttributeType attributeType;
   private Charakter targetCharacter;
   private float fluctuation = 0.01F;
   private float realModification;
   private float modificators;
   private boolean cancelled = false;

   public AttributeModification(float baseAmount, AttributeType attributeType, Charakter targetCharakter) {
      this.baseAmount = baseAmount;
      this.attributeType = attributeType;
      this.targetCharacter = targetCharakter;
   }

   public void addModificator(Float value) {
      this.modificators = this.modificators + value;
   }

   public void applyModification() {
      this.applyModification(null);
   }

   public void applyModification(RunningActivity activity) {
      if (this.realModification == 0.0F) {
         MyEvent event = new MyEvent(EventType.ATTRIBUTECHANGE, this);
         this.targetCharacter.handleEvent(event);
         if (!this.cancelled) {
            Attribute attribute = this.targetCharacter.getAttribute(this.attributeType);
            this.realModification = attribute.addToValue(this.getFinalModification(), activity);
         }
      }
   }

   public Float getFinalModification() {
      float finalValue = this.baseAmount + this.modificators;
      float flucValue = Util.getRnd().nextFloat() * this.fluctuation;
      boolean plusMinus = Util.getRnd().nextBoolean();
      if (plusMinus) {
         finalValue += finalValue * flucValue;
      } else {
         finalValue -= finalValue * flucValue;
      }

      return finalValue;
   }

   public float getBaseAmount() {
      return this.baseAmount;
   }

   public void setBaseAmount(float baseAmount) {
      this.baseAmount = baseAmount;
   }

   public AttributeType getAttributeType() {
      return this.attributeType;
   }

   public void setAttributeType(AttributeType attributeType) {
      this.attributeType = attributeType;
   }

   public Charakter getTargetCharacter() {
      return this.targetCharacter;
   }

   public float getRealModification() {
      return this.realModification;
   }

   public void setRealModification(float realModification) {
      this.realModification = realModification;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }
}
