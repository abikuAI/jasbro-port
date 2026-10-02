/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
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

public class Catshow
extends RunningActivity
implements BusinessSecondaryActivity {
    private MessageData messageData;
    private int bonus;

    @Override
    public void perform() {
        Charakter character = this.getCharacter();
        int skill = character.getCharisma() + character.getFinalValue(SpecializationAttribute.CATGIRL) / 4 + 1;
        int amountEarned = 0;
        int amountHappy = 0;
        int overalltips = 0;
        for (Customer customer : this.getCustomers()) {
            if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
                ++amountHappy;
                customer.addToSatisfaction(skill, this);
                int tips = (int)((double)customer.getMoney() / (1500.0 / (double)skill) + (double)Util.getInt(10, 20));
                tips = customer.pay(tips, this.getCharacter().getMoneyModifier());
                overalltips += tips;
                amountEarned += tips;
                continue;
            }
            customer.addToSatisfaction(skill / 4, this);
        }
        this.modifyIncome(amountEarned);
        if (character.getFinalValue(SpecializationAttribute.CATGIRL) > 75 && amountEarned > 5000) {
            this.messageData.addToMessage("\n\n" + TextUtil.t("catshow.result.skill", (Person)this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips));
        } else if (amountEarned > 2000) {
            this.messageData.addToMessage("\n\n" + TextUtil.t("catshow.result.owned", (Person)this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips));
        } else {
            this.messageData.addToMessage("\n\n" + TextUtil.t("catshow.result.basic", (Person)this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String messageText = TextUtil.t("catshow.basic", this.getCharacter());
        this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.CATGIRL, this.getCharacter()), this.getBackground());
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2f, SpecializationAttribute.CATGIRL));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.02f, BaseAttributeTypes.STRENGTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.02f, BaseAttributeTypes.STAMINA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
        }
        return modifications;
    }

    @Override
    public int getAppeal() {
        return (this.getCharacter().getCharisma() + this.getCharacter().getFinalValue(SpecializationAttribute.CATGIRL) / 4) / 4;
    }

    @Override
    public int getMaxAttendees() {
        return 25 + this.bonus;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}

