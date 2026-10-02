package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.texts.TextUtil;
import java.util.List;

public class EquipmentAddTrait extends EquipmentEffect {
   private Trait trait;

   @Override
   public void modifyTraits(List<Trait> traits, Charakter character) {
      if (this.trait != null && !traits.contains(this.trait)) {
         Trait opposedTrait = null;

         for (Trait curTrait : traits) {
            if (this.trait.isOpposed(curTrait)) {
               opposedTrait = curTrait;
               break;
            }
         }

         if (opposedTrait == null) {
            traits.add(this.trait);
         } else {
            traits.remove(opposedTrait);
         }
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
      return EquipmentEffectType.ADDTRAIT;
   }

   @Override
   public String getDescription() {
      return this.trait == null ? "" : TextUtil.t("equipment.addTrait", this.trait.getText());
   }

   @Override
   public double getValue() {
      return this.trait != null ? this.trait.getValueModifier() : 0.0;
   }

   @Override
   public int getAmountEffects() {
      return this.trait != null ? 1 : 0;
   }
}
