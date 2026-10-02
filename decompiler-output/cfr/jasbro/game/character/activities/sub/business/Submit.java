/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;
import java.util.List;

public class Submit
extends Whore {
    @Override
    public int rateCustomer(Customer customer) {
        int rating = super.rateCustomer(customer);
        if (customer.getPreferredSextype() == Sextype.BONDAGE) {
            return rating * 4;
        }
        if ((rating /= 8) == 0) {
            rating = 1;
        }
        return rating;
    }

    @Override
    public MessageData getBaseMessage() {
        MessageData message = super.getBaseMessage();
        if (this.getSexType() == Sextype.BONDAGE) {
            message.addToMessage(TextUtil.t("submit.basic", (Person)this.getCharacter(), this.getMainCustomer()));
        } else {
            message.addToMessage(TextUtil.t("submit.displeased", (Person)this.getCharacter(), this.getMainCustomer()));
        }
        return message;
    }

    @Override
    public void perform() {
        if (this.getSexType() != Sextype.BONDAGE) {
            this.getMainCustomer().addToSatisfaction(-15, this);
        } else {
            this.getMainCustomer().changePayModifier(0.2f);
        }
        super.perform();
    }

    @Override
    public List<Sextype> getPossibleSextypes(Customer customer) {
        List<Sextype> sextypes = super.getPossibleSextypes(customer);
        if (!sextypes.contains(Sextype.BONDAGE)) {
            sextypes.add(Sextype.BONDAGE);
        }
        return sextypes;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        List<RunningActivity.ModificationData> modifications = super.getStatModifications();
        if (this.getSexType() == Sextype.BONDAGE) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0f, EssentialAttributes.HEALTH));
            if (!this.getCharacter().getTraits().contains(Trait.LEGACYWHORE)) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.1f, BaseAttributeTypes.COMMAND));
            }
        }
        return modifications;
    }
}

