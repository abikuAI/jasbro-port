/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Sunbathe
extends RunningActivity {
    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("sunbathe.beach.basic", this.getCharacter());
        return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SUNBATHE, this.getCharacter()), this.getCharacterLocation().getImage());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.3f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f, BaseAttributeTypes.CHARISMA));
        return modifications;
    }

    @Override
    public void perform() {
        if (!this.getCharacter().getTraits().contains(Trait.TANLINES)) {
            boolean isTanned = false;
            for (Condition condition : this.getCharacter().getConditions()) {
                if (condition instanceof Buff.Tan && !this.getCharacter().getTraits().contains(Trait.SKINCARE)) {
                    this.getCharacter().removeCondition(condition);
                    this.getCharacter().addCondition(new Buff.Sunburn());
                    isTanned = true;
                    continue;
                }
                if (!(condition instanceof Buff.LightTan)) continue;
                this.getCharacter().removeCondition(condition);
                this.getCharacter().addCondition(new Buff.Tan());
                isTanned = true;
            }
            if (!isTanned) {
                this.getCharacter().addCondition(new Buff.LightTan());
            }
        } else {
            this.getCharacter().addCondition(new Buff.Tanlines());
        }
    }
}

