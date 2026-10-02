package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.texts.TextUtil;

public class EquipmentTraitRequirement extends EquipmentEffect {
   private Trait trait;

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.TRAITREQUIREMENT;
   }

   @Override
   public boolean canEquip(Charakter character) {
      return this.trait == null || character.getTraits().contains(this.trait);
   }

   public Trait getTrait() {
      return this.trait;
   }

   public void setTrait(Trait trait) {
      this.trait = trait;
   }

   @Override
   public String getDescription() {
      return this.trait == null ? "" : TextUtil.t("equipment.traitRequirement", this.trait.getText());
   }

   @Override
   public double getValue() {
      return 0.0;
   }

   @Override
   public int getAmountEffects() {
      return 0;
   }
}
