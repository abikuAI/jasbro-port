/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.conditions;

import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.warnings.Severity;
import jasbro.game.character.warnings.Warning;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.List;

public abstract class Illness
extends Condition {
    protected int remainingTime = 10;

    @Override
    public void init() {
        super.init();
        this.getCharacter().getCounter().add(CharacterStuffCounter.CounterNames.SICK.toString());
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public static class Smallpox
    extends Illness {
        private boolean showMessage = true;

        public Smallpox() {
        }

        public Smallpox(boolean showMessage) {
            this.showMessage = showMessage;
        }

        @Override
        public void init() {
            super.init();
            this.setRemainingTime(20);
            if (this.getCharacter().getConditions().contains(this) && this.showMessage) {
                new MessageScreen(this.getInfectMessage());
            }
        }

        public MessageData getInfectMessage() {
            return new MessageData(this.getTextStart(), ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, this.getCharacter()), this.getCharacter().getBackground(), true);
        }

        public String getTextStart() {
            return TextUtil.t("smallpox.start", this.getCharacter());
        }

        @Override
        public String getName() {
            return TextUtil.t("smallpox");
        }

        @Override
        public ImageData getIcon() {
            return new ImageData("images/icons/biohazard.png");
        }

        @Override
        public String getDescription() {
            return TextUtil.t("smallpox.description", this.getCharacter());
        }

        @Override
        public void handleEvent(MyEvent e) {
            if (e.getType() == EventType.ACTIVITYPERFORMED) {
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity.getPlannedActivity().getType() == ActivityType.NURSE) {
                    this.remainingTime -= 5;
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage(TextUtil.t("smallpox.medicalAttention", this.getCharacter()));
                    activity.getAttributeModifications().add(new AttributeModification(15.0f, EssentialAttributes.HEALTH, this.getCharacter()));
                } else if (activity.getPlannedActivity().getType() == ActivityType.SLEEP) {
                    --this.remainingTime;
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage(TextUtil.t("smallpox.sleep", this.getCharacter()));
                    activity.getAttributeModifications().add(new AttributeModification(5.0f, EssentialAttributes.HEALTH, this.getCharacter()));
                    this.infect(activity);
                } else {
                    this.infect(activity);
                }
                if (!(activity instanceof Whore)) {
                    activity.getAttributeModifications().add(new AttributeModification(-40.0f, EssentialAttributes.HEALTH, this.getCharacter()));
                } else {
                    activity.getAttributeModifications().add(new AttributeModification(-10.0f, EssentialAttributes.HEALTH, this.getCharacter()));
                }
            } else if (e.getType() == EventType.NEXTSHIFT) {
                --this.remainingTime;
                if (this.remainingTime <= 0) {
                    this.getCharacter().getConditions().remove(this);
                    new MessageScreen(TextUtil.t("smallpox.cured", this.getCharacter()), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), this.getCharacter().getBackground());
                }
            }
        }

        private void infect(RunningActivity activity) {
            for (Charakter character : activity.getCharacterLocation().getCurrentUsage().getCharacters()) {
                if (Util.getInt(0, 100) >= 80) continue;
                Smallpox condition = new Smallpox(false);
                character.addCondition(condition);
                if (!character.getConditions().contains(condition)) continue;
                activity.getMessages().add(condition.getInfectMessage());
            }
        }

        @Override
        public float getAttributeModifier(Attribute attribute) {
            if (attribute.getAttributeType() == BaseAttributeTypes.CHARISMA) {
                return -2.0f;
            }
            return 0.0f;
        }

        @Override
        public void modifyWarnings(List<Warning> warnings) {
            warnings.add(new Warning(Severity.DANGER, this.getDescription()));
        }

        public void setShowMessage(boolean showMessage) {
            this.showMessage = showMessage;
        }
    }

    public static class Flu
    extends Illness {
        private boolean showMessage = true;

        public Flu() {
        }

        public Flu(boolean showMessage) {
            this.showMessage = showMessage;
        }

        @Override
        public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
            if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
                return -255.0;
            }
            return currentValue;
        }

        @Override
        public void init() {
            super.init();
            this.setRemainingTime(Util.getInt(6, 12));
            AttributeModification mod = new AttributeModification(-5.0f, EssentialAttributes.HEALTH, this.getCharacter());
            mod.applyModification();
            if (this.showMessage && this.getCharacter().getConditions().contains(this)) {
                new MessageScreen(this.getInfectMessage());
            }
        }

        public MessageData getInfectMessage() {
            return new MessageData(this.getTextStart(), ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, this.getCharacter()), this.getCharacter().getBackground(), true);
        }

        public String getTextStart() {
            return TextUtil.t("flu.start", this.getCharacter());
        }

        @Override
        public String getName() {
            return "Flu";
        }

        @Override
        public ImageData getIcon() {
            return new ImageData("images/icons/flu.png");
        }

        @Override
        public String getDescription() {
            return this.getCharacter().getName() + " has the Flu and can not work.";
        }

        @Override
        public void handleEvent(MyEvent e) {
            if (e.getType() == EventType.ACTIVITY) {
                RunningActivity activity = (RunningActivity)e.getSource();
                if (activity.getPlannedActivity().getType() != ActivityType.SLEEP && activity.getPlannedActivity().getType() != ActivityType.SOAK && activity.getPlannedActivity().getType() != ActivityType.NURSE) {
                    new MessageScreen(TextUtil.t("illness.basic", this.getCharacter()), ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, this.getCharacter()), this.getCharacter().getBackground());
                    activity.setAbort(true);
                } else if (activity.getPlannedActivity().getType() == ActivityType.NURSE) {
                    --this.remainingTime;
                    --this.remainingTime;
                } else if (activity.getPlannedActivity().getType() == ActivityType.SOAK) {
                    --this.remainingTime;
                    --this.remainingTime;
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage(TextUtil.t("flu.soak", this.getCharacter()));
                } else {
                    MessageData message = activity.getMessages().get(0);
                    message.addToMessage(TextUtil.t("flu.sleep", this.getCharacter()));
                    --this.remainingTime;
                }
            } else if (e.getType() == EventType.NEXTSHIFT) {
                --this.remainingTime;
                if (this.remainingTime <= 0) {
                    this.getCharacter().getConditions().remove(this);
                    new MessageScreen(TextUtil.t("illness.cured", this.getCharacter()), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), this.getCharacter().getBackground());
                }
            }
        }

        @Override
        public void modifyWarnings(List<Warning> warnings) {
            warnings.add(new Warning(Severity.WARN, this.getDescription()));
        }
    }
}

