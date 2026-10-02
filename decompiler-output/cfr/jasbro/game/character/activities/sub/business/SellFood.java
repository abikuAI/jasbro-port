/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class SellFood
extends RunningActivity
implements BusinessSecondaryActivity {
    private static final int COSTMEAL = 20;
    private static final int PROFITMEAL = 12;
    private MessageData messageData;

    @Override
    public void perform() {
        int amountEarned = 0;
        for (Customer customer : this.getCustomers()) {
            customer.addToSatisfaction(this.getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 5, this);
            amountEarned += 12;
            int tips = customer.getMoney() / 200 + Util.getInt(1, 4) + this.getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 10;
            customer.payFixed(20);
            tips = customer.pay(tips, this.getCharacter().getMoneyModifier());
            amountEarned += tips;
        }
        this.modifyIncome(amountEarned);
        this.messageData.addToMessage("\n\n" + TextUtil.t("sellfood.result", (Person)this.getCharacter(), this.getCustomers().size(), this.getIncome()));
    }

    @Override
    public MessageData getBaseMessage() {
        String messageText = TextUtil.t("sellfood.basic", this.getCharacter());
        this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, this.getCharacter()), this.getBackground());
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, SpecializationAttribute.COOKING));
        if (this.getCharacter().getTraits().contains(Trait.RESTAURATEUR)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
        }
        return modifications;
    }

    @Override
    public int getAppeal() {
        return 1 + this.getCharacter().getFinalValue(SpecializationAttribute.COOKING);
    }

    @Override
    public int getMaxAttendees() {
        return 5 + this.getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 2;
    }
}

