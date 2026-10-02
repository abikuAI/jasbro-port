/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Fish
extends RunningActivity {
    private static final String smallFish = "FISH_Small_Fish";
    private static final String mediumFish = "FISH_Medium_Fish";
    private static final String bigFish = "FISH_Big_Fish";

    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("fish.basic", this.getCharacter());
        message = message + "\n";
        int fishCatch = Util.getInt(0, 8);
        int amount = Util.getInt(2, 3);
        Object[] arguments = new Object[]{amount, this.getCharacter()};
        switch (fishCatch) {
            case 1: {
                message = message + TextUtil.t("fish.none", (Person)this.getCharacter(), arguments);
                break;
            }
            case 2: {
                message = message + TextUtil.t("fish.small", (Person)this.getCharacter(), arguments);
                break;
            }
            case 3: {
                message = message + TextUtil.t("fish.medium", (Person)this.getCharacter(), arguments);
                break;
            }
            case 4: {
                message = message + TextUtil.t("fish.big", (Person)this.getCharacter(), arguments);
                break;
            }
            case 5: {
                message = message + TextUtil.t("fish.asleep", (Person)this.getCharacter(), arguments);
                break;
            }
            default: {
                message = message + TextUtil.t("fish.none", (Person)this.getCharacter(), arguments);
            }
        }
        message = message + "\n";
        if (fishCatch > 1 && fishCatch < 5) {
            if (Util.getInt(0, 10) < 5) {
                message = message + TextUtil.t("fish.eat", this.getCharacter());
                this.getCharacter().addCondition(new Buff.Satiated(fishCatch, this.getCharacter()));
            } else {
                message = message + TextUtil.t("fish.keep", this.getCharacter());
                if (Jasbro.getInstance().getItems().containsKey(smallFish) && Jasbro.getInstance().getItems().containsKey(bigFish) && Jasbro.getInstance().getItems().containsKey(mediumFish)) {
                    Item item = Jasbro.getInstance().getItems().get(smallFish);
                    switch (fishCatch) {
                        case 2: {
                            Jasbro.getInstance().getData().getInventory().addItems(item, amount);
                            break;
                        }
                        case 3: {
                            item = Jasbro.getInstance().getItems().get(mediumFish);
                            Jasbro.getInstance().getData().getInventory().addItems(item, amount);
                            break;
                        }
                        case 4: {
                            item = Jasbro.getInstance().getItems().get(bigFish);
                            Jasbro.getInstance().getData().getInventory().addItems(item, amount);
                        }
                    }
                }
            }
        }
        return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, this.getCharacter()), this.getCharacterLocation().getImage());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.6f, EssentialAttributes.MOTIVATION));
        return modifications;
    }
}

