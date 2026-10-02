/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.housing.House;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Massage
extends RunningActivity
implements BusinessMainActivity {
    private MessageData messageData;

    @Override
    public void init() {
        Charakter slave = this.getCharacters().get(0);
        String message = TextUtil.t("massage.service.prepare", (Person)slave, this.getMainCustomer());
        this.messageData = new MessageData(message, null, this.getBackground());
    }

    @Override
    public void perform() {
        for (Charakter character : this.getCharacters()) {
            int skill = character.getCharisma() / 4 + this.getCharacter().getFinalValue(SpecializationAttribute.MAGIC) + this.getCharacter().getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 2;
            skill /= 2;
            int payment = this.getMainCustomer().pay(this.getCharacter().getMoneyModifier());
            this.modifyIncome(payment);
            this.messageData.addToMessage("\n\n" + TextUtil.t("massage.result", (Person)this.getCharacter(), (Person)this.getMainCustomer(), this.getMainCustomer().getSatisfaction().getText(), this.getIncome()));
            if (this.getHouse() == null) continue;
            House house = this.getHouse();
            house.modDirt(1);
        }
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        for (Charakter character : this.getCharacters()) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, -30.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.01f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, character, 0.25f, SpecializationAttribute.MEDICALKNOWLEDGE));
        }
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYMASSEUR)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.1f, BaseAttributeTypes.COMMAND));
        }
        return modifications;
    }

    @Override
    public MessageData getBaseMessage() {
        List tags = this.getCharacter().getBaseTags();
        tags.addAll(ImageTag.getAssociatedImageTags(this.getCharacter(), this.getMainCustomer()));
        if (tags.contains((Object)ImageTag.FUTA)) {
            tags.add(0, ImageTag.FUTA);
        }
        if (tags.contains((Object)ImageTag.LESBIAN)) {
            tags.add(0, ImageTag.LESBIAN);
        }
        ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages());
        this.messageData.setImage(image);
        String messageText = TextUtil.t("massage.service.basic", (Person)this.getCharacter(), (Person)this.getMainCustomer(), (Object[])null);
        this.messageData.addToMessage(messageText);
        return this.messageData;
    }

    @Override
    public int rateCustomer(Customer customer) {
        int rating = customer.getType() == CustomerType.GROUP ? 0 : 1 + this.getCharacter().getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 5;
        return rating;
    }
}

