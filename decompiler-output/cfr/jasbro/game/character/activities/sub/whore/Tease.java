/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.whore;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Tease
extends Whore {
    @Override
    public int rateCustomer(Customer customer) {
        int rating = super.rateCustomer(customer);
        if (customer.getPreferredSextype() == Sextype.FOREPLAY) {
            return rating;
        }
        if ((rating /= 10) == 0) {
            rating = 1;
        }
        return rating;
    }

    @Override
    public String checkPossible(RunningActivity activity, Charakter whore) {
        return "";
    }

    @Override
    public void init() {
        super.init();
        if (this.getSexType() != Sextype.FOREPLAY) {
            this.setSexType(Sextype.FOREPLAY);
            this.getMainCustomer().addToSatisfaction(-40, this);
        }
        this.getMainCustomer().addToSatisfaction(this.getCharacter().getFinalValue(SpecializationAttribute.STRIP) / 4, this);
    }

    @Override
    public MessageData getBaseMessage() {
        MessageData message = super.getBaseMessage();
        if (this.getMainCustomer().getPreferredSextype() != Sextype.FOREPLAY) {
            message.addToMessage(TextUtil.t("tease.basic2", (Person)this.getCharacter(), this.getMainCustomer()));
        } else {
            message.addToMessage(TextUtil.t("tease.basic", (Person)this.getCharacter(), this.getMainCustomer()));
        }
        List tags = this.getCharacter().getBaseTags();
        if (Util.getRnd().nextBoolean()) {
            tags.add(0, ImageTag.MASTURBATION);
        } else {
            tags.add(0, ImageTag.DANCE);
        }
        ImageData imageData = ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages());
        message.setImage(imageData);
        return message;
    }

    @Override
    public List<Sextype> getPossibleSextypes(Customer customer) {
        ArrayList<Sextype> sextypes = new ArrayList<Sextype>();
        sextypes.add(Sextype.FOREPLAY);
        return sextypes;
    }

    @Override
    public void perform() {
        this.getMainCustomer().changePayModifier(-0.33f);
        super.perform();
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modificationData = new ArrayList<RunningActivity.ModificationData>();
        modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, Sextype.FOREPLAY));
        modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
        modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.1f, BaseAttributeTypes.OBEDIENCE));
        modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4f, SpecializationAttribute.SEDUCTION));
        modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, SpecializationAttribute.STRIP));
        modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -4.0f, EssentialAttributes.ENERGY));
        return modificationData;
    }

    @Override
    public Float getAmountActions() {
        return Float.valueOf(0.7f);
    }

    @Override
    public float getExecutionModifier() {
        return -0.5f;
    }

    @Override
    public float getCooldownModifier() {
        return -100.0f;
    }
}

