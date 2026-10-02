package jasbro.game.character.attributes;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.io.Serializable;

public class Attribute implements Serializable {
   private AttributeType attributeType;
   private float internValue = 0.0F;
   private transient int minValue = 0;
   private int maxValue = 100;
   private Charakter character;

   public Attribute(Charakter character, AttributeType attributeType) {
      this.character = character;
      this.attributeType = attributeType;
      this.maxValue = attributeType.getDefaultMax();
      this.minValue = attributeType.getDefaultMin();
      this.internValue = attributeType.getStartValue();
   }

   public Attribute(AttributeType attributeType, float internValue, Charakter character) {
      this(character, attributeType);
      this.internValue = internValue;
      this.maxValue = attributeType.getDefaultMax();
      this.minValue = attributeType.getDefaultMin();
   }

   public int getValue() {
      return this.getCharacter().getFinalValue(this.getAttributeType());
   }

   public int getBonus() {
      return this.getCharacter().getBonus(this.getAttributeType());
   }

   public float addToValue(float modValue) {
      return this.addToValue(modValue, null);
   }

   public float addToValue(float modValue, boolean fixed) {
      return this.addToValue(modValue, null, fixed);
   }

   public float addToValue(float modValue, RunningActivity activity) {
      return this.addToValue(modValue, activity, false);
   }

   public float addToValue(float modValue, RunningActivity activity, boolean fixed) {
      if (!(this.attributeType instanceof EssentialAttributes)) {
         modValue /= 10.0F;

         for (SpecializationType specializationType : this.character.getSpecializations()) {
            if (specializationType.getAssociatedAttributes().contains(this.attributeType)) {
               modValue *= 10.0F;
               break;
            }
         }

         if (!fixed) {
            float factor = 1.0F;
            if (this.getInternValue() < 10.0F) {
               factor = 1.0F;
            } else if (this.getInternValue() < 20.0F) {
               factor = 0.8F;
            } else if (this.getInternValue() < 30.0F) {
               factor = 0.6F;
            } else if (this.getInternValue() < 40.0F) {
               factor = 0.4F;
            } else if (this.getInternValue() < 60.0F) {
               factor = 0.2F;
            } else if (this.getInternValue() < 80.0F) {
               factor = 0.1F;
            } else {
               factor = 0.0F;
            }

            if (this.attributeType instanceof SpecializationAttribute) {
               modValue *= factor;
               modValue += Math.abs(modValue) * (this.character.getIntelligence() - 5) / 2.0F / 100.0F;
               if (this.internValue > this.attributeType.getDefaultMax()) {
                  int level = (int)(1.0F + (this.internValue - this.attributeType.getDefaultMax()) / this.attributeType.getRaiseMaxBy());
                  int modPercent = level * 5;
                  if (level > 10) {
                     modPercent -= level - 10;
                  }

                  if (level > 15) {
                     modPercent -= level - 15;
                  }

                  modValue -= modPercent * modValue / 100.0F;
               }
            }

            if (this.attributeType instanceof BaseAttributeTypes || this.attributeType instanceof Sextype) {
               modValue *= factor;
            }
         }
      } else if (this.attributeType == EssentialAttributes.ENERGY && !fixed) {
         if (modValue >= 0.0F) {
            modValue += Math.abs(modValue) * ((this.character.getStamina() - 5) * 0.5F) / 100.0F * 0.9F;
         } else {
            modValue += Math.abs(modValue) * ((this.character.getStamina() - 5) * 0.5F) / 100.0F * 1.1F;
         }
      } else if (this.attributeType == EssentialAttributes.HEALTH && !fixed) {
         modValue += Math.abs(modValue) * (this.character.getStrength() - 5) * 0.5F / 100.0F;
      }

      this.internValue += modValue;
      if (this.internValue < this.minValue) {
         modValue -= this.internValue - this.minValue;
         this.internValue = this.minValue;
      } else if (this.internValue > this.maxValue) {
         modValue -= this.internValue - this.maxValue;
         this.internValue = this.maxValue;
      }

      if (modValue != 0.0F || this.internValue == this.minValue) {
         MyEvent event = new AttributeChangedEvent(EventType.ATTRIBUTECHANGED, this, modValue, activity);
         this.getCharacter().handleEvent(event);
      }

      return modValue;
   }

   public String getNameResolved() {
      return TextUtil.t(this.attributeType.toString());
   }

   public float getInternValue() {
      return this.internValue;
   }

   public void setInternValue(float internValue) {
      this.internValue = internValue;
   }

   public int getMaxValue() {
      return this.maxValue;
   }

   public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
   }

   public String getAttributeName() {
      return this.attributeType.toString();
   }

   public ImageData getIcon() {
      return new ImageData("images/icons/" + this.attributeType.toString().toLowerCase() + ".png");
   }

   public int getMinValue() {
      return this.minValue;
   }

   public void setMinValue(int minValue) {
      this.minValue = minValue;
   }

   public Charakter getCharacter() {
      return this.character;
   }

   public AttributeType getAttributeType() {
      return this.attributeType;
   }

   public boolean isMaxed() {
      return this.getMaxValue() == this.internValue;
   }
}
