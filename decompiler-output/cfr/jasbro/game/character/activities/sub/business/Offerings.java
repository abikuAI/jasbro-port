/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Offerings
extends RunningActivity
implements BusinessSecondaryActivity {
    private MessageData messageData;
    private int bonus;

    @Override
    public void perform() {
        Charakter character = this.getCharacter();
        int skill = Util.getInt(0, 5) * character.getCharisma() / 8;
        if (skill > 20) {
            skill = 20;
        }
        int amountEarned = 0;
        int amountHappy = 0;
        int overalltips = 0;
        for (Customer customer : this.getCustomers()) {
            Inventory.ItemData itemStolen;
            ArrayList<Inventory.ItemData> loot = new ArrayList<Inventory.ItemData>();
            if (Util.getInt(0, 100) < skill + customer.getInitialSatisfaction() / 5 && (itemStolen = customer.getItem()) != null) {
                loot.add(itemStolen);
            }
            if (loot.size() > 0) {
                Jasbro.getInstance().getData().getInventory().addItems(loot);
                Object[] arguments = new Object[]{TextUtil.listItems(loot)};
                this.getMessages().get(0).addToMessage("\n" + TextUtil.t("offerings.loot", (Person)character, arguments));
            }
            if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
                ++amountHappy;
                customer.addToSatisfaction(skill, this);
                int tips = 0;
                switch (customer.getType()) {
                    case PEASANT: {
                        tips = 10;
                        break;
                    }
                    case SOLDIER: {
                        tips = 20;
                        break;
                    }
                    case MERCHANT: {
                        tips = 40;
                        break;
                    }
                    case BUSINESSMAN: {
                        tips = 80;
                        break;
                    }
                    case MINORNOBLE: {
                        tips = 160;
                        break;
                    }
                    case LORD: {
                        tips = 320;
                        break;
                    }
                    case CELEBRITY: {
                        tips = 640;
                        break;
                    }
                    default: {
                        tips = 5;
                    }
                }
                tips = customer.pay(tips, this.getCharacter().getMoneyModifier());
                overalltips += tips;
                amountEarned += tips;
                continue;
            }
            customer.addToSatisfaction(skill / 4, this);
        }
        this.modifyIncome(amountEarned);
        if (amountEarned > 0) {
            this.messageData.addToMessage("\n\n" + TextUtil.t("offerings.result", (Person)this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String messageText = TextUtil.t("offerings.basic", this.getCharacter());
        this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), this.getBackground());
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.02f, BaseAttributeTypes.COMMAND));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, EssentialAttributes.MOTIVATION));
        return modifications;
    }

    @Override
    public int getAppeal() {
        return Util.getInt(1, 30);
    }

    @Override
    public int getMaxAttendees() {
        return 15;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}

