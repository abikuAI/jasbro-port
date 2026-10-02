package jasbro.game.items;

import jasbro.game.character.AttributeModifier;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MyEvent;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Equipment extends Item implements AttributeModifier {
   private List<EquipmentEffect> equipmentEffects = new ArrayList<>();
   private EquipmentType equipmentType = EquipmentType.ACCESSORY;
   private AccessoryType accessoryType;

   public Equipment(String id) {
      super(id, ItemType.EQUIPMENT);
   }

   public Equipment(Item item) {
      super(item);
      this.setType(ItemType.EQUIPMENT);
   }

   @Override
   public String getText() {
      String text = "<b>" + this.getName() + "</b>\n" + TextUtil.t("slotItem", this.equipmentType.getText()) + "\n";
      if (this.equipmentType == EquipmentType.ACCESSORY && this.accessoryType != null) {
         text = text + TextUtil.t("accessoryType", this.accessoryType.getText()) + "\n";
      }

      text = text + TextUtil.t("valueItem", this.getValue()) + "\n" + this.getDescription() + "\n\n";

      for (EquipmentEffect effect : this.equipmentEffects) {
         text = text + effect.getDescription() + "\n";
      }

      return text;
   }

   @Override
   public float getAttributeModifier(Attribute attribute) {
      float modifier = 0.0F;

      for (EquipmentEffect effect : this.equipmentEffects) {
         modifier += effect.getAttributeModifier(attribute);
      }

      return modifier;
   }

   public void handleEvent(MyEvent e, Charakter character) {
      for (EquipmentEffect effect : this.equipmentEffects) {
         effect.handleEvent(e, character);
      }
   }

   public boolean equip(EquipmentSlot equipmentSlot, Charakter character) {
      boolean equipSuccess = true;

      for (EquipmentEffect effect : this.equipmentEffects) {
         if (!effect.canEquip(character)) {
            equipSuccess = false;
            break;
         }
      }

      if (equipSuccess) {
         for (EquipmentEffect effect : this.equipmentEffects) {
            effect.doAtEquip(character);
         }
      }

      return equipSuccess;
   }

   public void unequip(EquipmentSlot equipmentSlot, Charakter character) {
      if (character.getCharacterInventory().getItem(equipmentSlot) == this) {
         for (EquipmentEffect effect : this.equipmentEffects) {
            effect.doAtUnEquip(character);
         }
      }
   }

   public void modifyImageTags(List<ImageTag> imageTags) {
      for (EquipmentEffect effect : this.equipmentEffects) {
         effect.modifyImageTags(imageTags);
      }
   }

   public void modifyTraits(List<Trait> traits, Charakter character) {
      for (EquipmentEffect effect : this.equipmentEffects) {
         effect.modifyTraits(traits, character);
      }
   }

   public List<EquipmentEffect> getEquipmentEffects() {
      return this.equipmentEffects;
   }

   public void setEquipmentEffects(List<EquipmentEffect> equipmentEffects) {
      this.equipmentEffects = equipmentEffects;
   }

   public EquipmentType getEquipmentType() {
      return this.equipmentType;
   }

   public void setEquipmentType(EquipmentType equipmentType) {
      this.equipmentType = equipmentType;
   }

   public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
      for (EquipmentEffect effect : this.equipmentEffects) {
         value = effect.modifyCalculatedAttribute(attribute, value, character);
      }

      return value;
   }

   public AccessoryType getAccessoryType() {
      return this.accessoryType;
   }

   public void setAccessoryType(AccessoryType accessoryType) {
      this.accessoryType = accessoryType;
   }

   public long calculateValue() {
      double valueSum = 0.0;
      double valueExp = 1.0;

      for (EquipmentEffect effect : this.equipmentEffects) {
         valueSum += effect.getValue() * effect.getAmountEffects();

         for (int i = 0; i < effect.getAmountEffects(); i++) {
            if (effect.getValue() >= 0.0) {
               valueExp *= 1.0 + effect.getValueExponential();
            } else {
               valueExp /= -(1.0 + effect.getValueExponential());
            }
         }
      }

      double valueFinal = (long)(valueSum * valueExp);
      if (this.equipmentType == EquipmentType.ACCESSORY && this.accessoryType == AccessoryType.ONEHANDED) {
         valueFinal = valueFinal * 4.0 / 5.0;
      } else if (this.equipmentType == EquipmentType.ACCESSORY && this.accessoryType == AccessoryType.TWOHANDED) {
         valueFinal = valueFinal * 2.0 / 3.0;
      }

      return (long)valueFinal;
   }
}
