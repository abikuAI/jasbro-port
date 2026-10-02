/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Clean
extends RunningActivity {
    private static final float BASEMODIFICATION = 1.0f;
    private static final float OBEDIENCEMODIFICATION = 0.01f;
    private float dirtModification = -1.0f;

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        float modification = 1.0f;
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, modification, SpecializationAttribute.CLEANING));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYMAID)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        if (this.getCharacter().getTraits().contains(Trait.PERSONALMAID)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.25f, EssentialAttributes.MOTIVATION));
        }
        return modifications;
    }

    @Override
    public void init() {
        this.dirtModification = -20 - 1 * this.getCharacters().get(0).getFinalValue(SpecializationAttribute.CLEANING);
    }

    @Override
    public void perform() {
        House house = ((Room)this.getCharacterLocation()).getHouse();
        house.modDirt((int)this.dirtModification);
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, character);
        String message = TextUtil.t("clean.basic", character);
        return new MessageData(message, image, null);
    }

    public float getDirtModification() {
        return this.dirtModification;
    }

    public void setDirtModification(float dirtModification) {
        this.dirtModification = dirtModification;
    }
}

