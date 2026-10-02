/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Eat
extends RunningActivity {
    private final int foodRank;
    private final long pay = Jasbro.getInstance().getData().getMoney() / 100L;

    public Eat() {
        this.foodRank = this.pay < 100L ? 0 : (this.pay < 400L ? (Jasbro.getInstance().getData().canAfford(20L) ? 10 : 0) : (this.pay < 900L ? 20 : (this.pay < 1600L ? 30 : (this.pay < 2500L ? 40 : 50))));
    }

    @Override
    public MessageData getBaseMessage() {
        StringBuilder builder = new StringBuilder(TextUtil.t("eat.basic", this.getCharacters()));
        builder.append(" ");
        ArrayList<ImageData> images = new ArrayList<ImageData>();
        switch (this.foodRank) {
            case 50: {
                builder.append(TextUtil.t("eat.MOAB"));
                break;
            }
            case 40: {
                builder.append(TextUtil.t("eat.assortment"));
                break;
            }
            case 30: {
                builder.append(TextUtil.t("eat.steak"));
                break;
            }
            case 20: {
                builder.append(TextUtil.t("eat.pasta"));
                break;
            }
            case 10: {
                builder.append(TextUtil.t("eat.rice"));
                break;
            }
            default: {
                builder.append(TextUtil.t("eat.nothing"));
            }
        }
        this.modifyIncome(-1 * this.foodRank * this.foodRank);
        for (Charakter character : this.getCharacters()) {
            images.addAll(character.getImages());
            character.addCondition(new Buff.Satiated(this.foodRank, character));
        }
        return new MessageData(builder.toString(), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, images), this.getCharacterLocation().getImage());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f * (float)this.foodRank, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f * (float)this.foodRank, EssentialAttributes.HEALTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f * (float)this.foodRank, EssentialAttributes.MOTIVATION));
        return modifications;
    }
}

