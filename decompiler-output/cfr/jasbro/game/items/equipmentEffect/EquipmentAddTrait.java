/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;
import java.util.List;

public class EquipmentAddTrait
extends EquipmentEffect {
    private Trait trait;

    @Override
    public void modifyTraits(List<Trait> traits, Charakter character) {
        if (this.trait != null && !traits.contains(this.trait)) {
            Trait opposedTrait = null;
            for (Trait curTrait : traits) {
                if (!this.trait.isOpposed(curTrait)) continue;
                opposedTrait = curTrait;
                break;
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
        if (this.trait == null) {
            return "";
        }
        return TextUtil.t("equipment.addTrait", this.trait.getText());
    }

    @Override
    public double getValue() {
        if (this.trait != null) {
            return this.trait.getValueModifier();
        }
        return 0.0;
    }

    @Override
    public int getAmountEffects() {
        if (this.trait != null) {
            return 1;
        }
        return 0;
    }
}

