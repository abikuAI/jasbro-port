/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.conditions.ItemCooldown;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;

public class UsableItem
extends Item {
    private UsableItemEffect itemEffect;

    public UsableItem(String id) {
        super(id, ItemType.USABLE);
    }

    public UsableItem(Item item) {
        super(item);
        this.setType(ItemType.USABLE);
    }

    public boolean use(Charakter character) {
        for (Condition condition : character.getConditions()) {
            ItemCooldown cooldown;
            if (!(condition instanceof ItemCooldown) || !(cooldown = (ItemCooldown)condition).getItemId().equals(this.getId())) continue;
            return false;
        }
        this.itemEffect.apply(character, this);
        MyEvent event = new MyEvent(EventType.ITEMUSED, character);
        character.handleEvent(event);
        character.fireEvent(event);
        return true;
    }

    @Override
    public String getText() {
        return "<b>" + this.getName() + "</b>\n" + TextUtil.t("typeConsumable") + "\n" + TextUtil.t("valueItem", this.getValue()) + "\n" + this.getDescription();
    }

    public UsableItemEffect getItemEffect() {
        return this.itemEffect;
    }

    public void setItemEffect(UsableItemEffect itemEffect) {
        this.itemEffect = itemEffect;
    }
}

