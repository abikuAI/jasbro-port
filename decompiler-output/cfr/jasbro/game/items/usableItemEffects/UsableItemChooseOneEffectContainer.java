/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items.usableItemEffects;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectChance;
import jasbro.game.items.usableItemEffects.UsableItemEffectContainerImpl;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;
import java.util.List;

public class UsableItemChooseOneEffectContainer
extends UsableItemEffectContainerImpl {
    @Override
    public void apply(Charakter character, Item item) {
        List<UsableItemEffect> effects = this.getSubEffects();
        int amountChance = 0;
        if (effects.size() > 0) {
            for (UsableItemEffect itemEffect : effects) {
                if (!(itemEffect instanceof UsableItemEffectChance)) continue;
                ++amountChance;
            }
            if (amountChance != effects.size()) {
                effects.get(Util.getInt(0, effects.size())).apply(character, item);
            } else {
                int sumChances = 0;
                for (UsableItemEffect itemEffect : effects) {
                    sumChances += ((UsableItemEffectChance)itemEffect).getChance();
                }
                int selected = Util.getInt(0, sumChances);
                sumChances = 0;
                for (UsableItemEffect itemEffect : effects) {
                    if ((sumChances += ((UsableItemEffectChance)itemEffect).getChance()) <= selected) continue;
                    ((UsableItemEffectChance)itemEffect).applyOverride(character, item);
                    break;
                }
            }
        }
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.CHOOSEONEFFECT;
    }

    @Override
    public String getName() {
        return "Choose one subeffect container";
    }
}

