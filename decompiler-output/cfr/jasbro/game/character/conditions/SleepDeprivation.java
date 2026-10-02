/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Nurse;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.game.world.Time;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class SleepDeprivation
extends Condition {
    private int severity = 1;

    @Override
    public String getName() {
        return TextUtil.t("conditions.sleepDeprivation");
    }

    @Override
    public String getDescription() {
        Object[] arguments = new Object[]{this.getStaminaDebuff()};
        return this.getName() + "\n" + TextUtil.t("conditions.sleepDeprivation.description", (Person)this.getCharacter(), arguments);
    }

    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/night.png");
    }

    @Override
    public void init() {
        super.init();
        if (this.getCharacter().getConditions().contains(this)) {
            new MessageScreen(TextUtil.t("conditions.sleepDeprivation.text", this.getCharacter()), ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter()), this.getCharacter().getBackground(), true);
        }
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.SLEEP) {
                this.severity -= 15;
            } else if (activity.getType() == ActivityType.NURSE) {
                Nurse nurse = (Nurse)activity;
                if (nurse.getNurse() != this.getCharacter()) {
                    this.severity -= 15;
                }
            } else if (activity.getType() == ActivityType.IDLE && Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
                this.severity -= 15;
            }
            if (this.severity <= 0) {
                this.removeThis();
            }
        } else if (e.getType() == EventType.NEXTSHIFT) {
            ++this.severity;
            this.getCharacter().getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(-0.5f);
            if (this.getCharacter().getFinalValue(BaseAttributeTypes.STAMINA) <= 0) {
                new MessageScreen(TextUtil.t("conditions.sleepDeprivation.deathStamina", this.getCharacter()), new ImageData("images/backgrounds/coffin.png"), this.getCharacter().getBackground(), true);
                Jasbro.getInstance().removeCharacter(this.getCharacter());
            } else if (this.getCharacter().getFinalValue(BaseAttributeTypes.INTELLIGENCE) <= 0) {
                Jasbro.getInstance().getData().spendMoney(-200L, this.getCharacter().getName());
                new MessageScreen(TextUtil.t("conditions.sleepDeprivation.deathIntelligence", this.getCharacter()), new ImageData("images/backgrounds/coffin.png"), this.getCharacter().getBackground(), true);
                Jasbro.getInstance().removeCharacter(this.getCharacter());
            }
        }
    }

    @Override
    public float getAttributeModifier(Attribute attribute) {
        if (attribute.getAttributeType() == BaseAttributeTypes.STAMINA) {
            return -this.getStaminaDebuff();
        }
        return 0.0f;
    }

    public int getStaminaDebuff() {
        return this.severity / 2 + 1;
    }
}

