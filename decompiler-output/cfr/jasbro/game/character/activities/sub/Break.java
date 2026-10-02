/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Break
extends RunningActivity {
    private Charakter trainer;
    private Charakter slave;

    @Override
    public void init() {
        if (this.getCharacters().get(0).getType() == CharacterType.TRAINER) {
            this.trainer = this.getCharacters().get(0);
            this.slave = this.getCharacters().get(1);
        } else {
            this.trainer = this.getCharacters().get(1);
            this.slave = this.getCharacters().get(0);
        }
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        float obedienceMod = 0.05f;
        float modObLow = (12.0f - (float)this.slave.getObedience() * 1.5f) / 15.0f;
        if (modObLow > 0.0f) {
            obedienceMod += modObLow;
        }
        obedienceMod = (float)((double)obedienceMod + (double)this.trainer.getCommand() / 50.0);
        obedienceMod = (float)((double)obedienceMod + (double)this.trainer.getFinalValue(SpecializationAttribute.DOMINATE) / 50.0);
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, obedienceMod, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.2f, BaseAttributeTypes.COMMAND));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, -1.3f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 1.3f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, -30.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, -15.0f, EssentialAttributes.HEALTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -20.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 1.0f, SpecializationAttribute.DOMINATE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, Sextype.BONDAGE));
        return modifications;
    }

    @Override
    public MessageData getBaseMessage() {
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BONDAGE, this.slave);
        String message = TextUtil.t("break.basic", (Person)this.trainer, this.slave);
        return new MessageData(message, image, null);
    }
}

