package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.texts.TextUtil;
import java.util.List;

public class EquipmentRemoveTrait extends EquipmentEffect {
   private Trait trait;

   @Override
   public void modifyTraits(List<Trait> traits, Charakter character) {
      if (this.trait != null) {
         traits.remove(this.trait);
      }
   }

   public Trait getTrait() {
      return this.trait;
   }

   public void setTrait(Trait trait) {
      this.trait = trait;
   }

   @Override
   public EquipmentEffectType getType() {
      return EquipmentEffectType.REMOVETRAIT;
   }

   @Override
   public String getDescription() {
      return this.trait == null ? "" : TextUtil.t("equipment.removeTrait", this.trait.getText());
   }

   @Override
   public double getValue() {
      return this.trait != null ? -this.trait.getValueModifier() : 0.0;
   }

   @Override
   public int getAmountEffects() {
      return this.trait != null ? 1 : 0;
   }
}
