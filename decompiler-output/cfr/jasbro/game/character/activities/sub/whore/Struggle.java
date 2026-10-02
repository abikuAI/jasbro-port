/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.whore;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Struggle
extends Whore {
    private MessageData messageData;
    private float actions = 1.5f;
    private boolean submit = false;

    @Override
    public void init() {
        super.init();
        this.setMinimumObedience(10);
        this.determineSubmit(this.getMainCustomer());
    }

    @Override
    public MessageData getBaseMessage() {
        String house = this.getHouse().getName();
        String customer = this.getMainCustomer().getName();
        String mood = this.getMainCustomer().getStatusName();
        String message = this.getHouse().getInternName() == null || this.getHouse().getInternName().trim().equals("") ? TextUtil.t("whore.basic1", mood, customer, house) + " " : TextUtil.t("whore.basic2", mood, customer, house) + " ";
        message = message + TextUtil.t("whore.service", (Person)this.getCharacter(), this.getMainCustomer());
        message = message + "\n" + TextUtil.t("struggle.basic", (Person)this.getCharacter(), this.getMainCustomer());
        if (this.submit) {
            message = message + "\n" + TextUtil.t("struggle.result.lost", this.getCharacter());
            message = message + "\n" + TextUtil.t("struggle.bondage", (Person)this.getMainCustomer(), this.getCharacter());
        } else {
            message = message + "\n" + TextUtil.t("struggle.result.won", (Person)this.getCharacter(), this.getMainCustomer());
            message = message + "\n" + TextUtil.t("struggle.dominate", this.getCharacter());
        }
        message = message + "\n" + TextUtil.t("struggle.finish") + "\n";
        List tags = this.getCharacter().getBaseTags();
        tags.add(0, ImageTag.DOMINATRIX);
        if (this.getMainCustomer().getType() == CustomerType.GROUP) {
            tags.add(ImageTag.GROUP);
        }
        this.messageData = new MessageData(message, ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages()), this.getBackground());
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.getMainCustomer() != null) {
            if (this.submit) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.5f, BaseAttributeTypes.OBEDIENCE));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, SpecializationAttribute.DOMINATE));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, BaseAttributeTypes.STAMINA));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -3.0f, EssentialAttributes.HEALTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 4.0f, Sextype.BONDAGE));
            } else {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.5f, BaseAttributeTypes.COMMAND));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 4.0f, SpecializationAttribute.DOMINATE));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, BaseAttributeTypes.STRENGTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -1.0f, EssentialAttributes.HEALTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, Sextype.BONDAGE));
            }
        } else {
            return new Idle().getStatModifications();
        }
        return modifications;
    }

    @Override
    public void perform() {
        if (this.getMainCustomer() != null) {
            int skill = this.submit ? this.getCharacter().getCommand() + this.getCharacter().getFinalValue(Sextype.BONDAGE) * 2 : this.getCharacter().getCommand() + this.getCharacter().getFinalValue(SpecializationAttribute.DOMINATE) * 2;
            this.getMainCustomer().addToSatisfaction(skill, this);
            int pay = this.getMainCustomer().pay(this.getCharacter().getMoneyModifier() + 0.5f);
            this.modifyIncome(pay);
            this.getMessages().get(0).addToMessage("\n\n" + TextUtil.t("whore.end", (Person)this.getCharacter(), (Person)this.getMainCustomer(), this.getMainCustomer().getSatisfaction().getText(), pay));
        }
    }

    private void determineSubmit(Customer customer) {
        int bonus = 0;
        int bumBase = 2;
        int businessBase = 2;
        int celebrityBase = 4;
        int lordBase = 4;
        int merchantBase = 3;
        int nobleBase = 3;
        int peasantBase = 2;
        int soldierBase = 7;
        switch (customer.getStatus()) {
            case DRUNK: {
                bonus = -1;
                break;
            }
            case HYPED: {
                bonus = 1;
                break;
            }
            case PISSED: {
                bonus = 2;
                break;
            }
            case SAD: {
                bonus = -2;
                break;
            }
            case SHYSTATUS: {
                bonus = -1;
                break;
            }
            case STRONGSTATUS: {
                bonus = 3;
                break;
            }
            case TIRED: {
                bonus = -2;
                break;
            }
            case VERYDRUNK: {
                bonus = -3;
                break;
            }
            default: {
                bonus = 0;
            }
        }
        switch (customer.getType()) {
            case BUM: {
                if (Util.getInt(0, 10) < bumBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case BUSINESSMAN: {
                if (Util.getInt(0, 10) < businessBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case CELEBRITY: {
                if (Util.getInt(0, 10) < celebrityBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case LORD: {
                if (Util.getInt(0, 10) < lordBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case MERCHANT: {
                if (Util.getInt(0, 10) < merchantBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case MINORNOBLE: {
                if (Util.getInt(0, 10) < nobleBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case PEASANT: {
                if (Util.getInt(0, 10) < peasantBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            case SOLDIER: {
                if (Util.getInt(0, 10) < soldierBase + bonus) {
                    this.submit = true;
                    break;
                }
                this.submit = false;
                break;
            }
            default: {
                this.submit = true;
            }
        }
    }

    @Override
    public List<Sextype> getPossibleSextypes(Customer customer) {
        ArrayList<Sextype> sextypes = new ArrayList<Sextype>();
        sextypes.add(Sextype.BONDAGE);
        return sextypes;
    }

    @Override
    public int rateCustomer(Customer customer) {
        int rating = super.rateCustomer(customer);
        if (customer.getPreferredSextype() == Sextype.BONDAGE) {
            return rating * 4;
        }
        return 0;
    }

    public void setActionCost(float cost) {
        this.actions = cost;
    }

    @Override
    public Float getAmountActions() {
        return Float.valueOf(this.actions);
    }
}

