/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public class CoolDown
extends Condition {
    private int daysRemaining;
    private String itemId;
    private ImageData icon = new ImageData("images/icons/cooldown.png");
    private String name;
    private String description;

    public CoolDown(int daysRemaining, String itemId, String name, String description) {
        this.daysRemaining = daysRemaining;
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY) {
            --this.daysRemaining;
            if (this.daysRemaining <= 0) {
                this.getCharacter().getConditions().remove(this);
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description + "\n" + TextUtil.t("conditions.cooldown.remainingDays", this.daysRemaining);
    }

    @Override
    public ImageData getIcon() {
        return this.icon;
    }

    public void setIcon(ImageData icon) {
        this.icon = icon;
    }

    public int getDaysRemaining() {
        return this.daysRemaining;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

