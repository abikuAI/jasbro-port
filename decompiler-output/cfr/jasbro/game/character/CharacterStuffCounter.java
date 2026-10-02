/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Nurse;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.conditions.SleepDeprivation;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerType;
import jasbro.game.world.Time;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.util.HashMap;
import java.util.Map;

public class CharacterStuffCounter {
    private Map<String, Long> counterMap;
    private transient Map<String, Integer> map;

    private Map<String, Long> getCounterMap() {
        if (this.counterMap == null) {
            this.counterMap = new HashMap<String, Long>();
        }
        return this.counterMap;
    }

    public Long get(String key) {
        if (!this.getCounterMap().containsKey(key)) {
            return 0L;
        }
        return this.getCounterMap().get(key);
    }

    public Long get(CounterNames key) {
        return this.get(key.toString());
    }

    public void add(CounterNames key) {
        this.add(key.toString(), (Long)1L);
    }

    public void add(String key) {
        this.add(key, (Long)1L);
    }

    public void add(CounterNames key, Long value) {
        this.add(key.toString(), value);
    }

    public void add(String key, Long value) {
        if (!this.getCounterMap().containsKey(key)) {
            this.getCounterMap().put(key, value);
        } else {
            this.getCounterMap().put(key, this.getCounterMap().get(key) + value);
        }
    }

    public void reset(String key) {
        if (this.getCounterMap().containsKey(key)) {
            this.counterMap.remove(key);
        }
    }

    public void handleEvent(MyEvent e, Charakter character) {
        if (e.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity = (RunningActivity)e.getSource();
            String activityString = activity.getType().toString();
            this.add(activityString);
            if (activity.getType() == ActivityType.BREAK && this.get(activityString) % 10L == 0L) {
                if (character.getType() == CharacterType.SLAVE) {
                    if (!character.getTraits().contains(Trait.OBEDIENT)) {
                        character.addTrait(Trait.OBEDIENT);
                    } else {
                        character.getAttribute(BaseAttributeTypes.OBEDIENCE).addToValue(2.0f);
                    }
                    character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(-2.0f);
                    GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.break", character), ImageTag.HURT, character, true), e);
                }
            } else if (activity.getType() == ActivityType.SWIM && !character.getTraits().contains(Trait.FIT) && this.get(activityString) % 30L == 0L) {
                character.addTrait(Trait.FIT);
                GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.swim", character), ImageTag.SWIM, character, true), e);
            } else if (activity.getType() == ActivityType.ADVERTISE && character.getTraits().contains(Trait.SHY) && this.get(activityString) % 50L == 0L) {
                character.removeTrait(Trait.SHY);
                GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.advertise", character), ImageTag.CLOTHED, character, true), e);
            } else if (activity.getType() == ActivityType.BARTEND || activity.getType() == ActivityType.COOK || activity.getType() == ActivityType.CLEAN || activity.getType() == ActivityType.SELLFOOD) {
                long cookAmount;
                if (this.get(activityString) % 300L == 0L && !character.getTraits().contains(Trait.HELPFUL) && ((cookAmount = this.get(ActivityType.COOK.toString()) + this.get(ActivityType.SELLFOOD.toString())) > 50L && this.get(ActivityType.CLEAN.toString()) > 50L || cookAmount > 50L && this.get(ActivityType.BARTEND.toString()) > 50L || this.get(ActivityType.BARTEND.toString()) > 50L && this.get(ActivityType.CLEAN.toString()) > 50L)) {
                    character.addTrait(Trait.HELPFUL);
                    GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.housework", character), ImageTag.MAID, character, true), e);
                }
            } else if (activity.getType() == ActivityType.SLEEP || activity.getType() == ActivityType.CAMP) {
                this.reset(CounterNames.NOSLEEP.toString());
                this.add(CounterNames.NOSLEEP.toString(), (Long)-1L);
            } else if (activity.getType() == ActivityType.NURSE) {
                Nurse nurse = (Nurse)activity;
                if (nurse.getNurse() != character) {
                    this.reset(CounterNames.NOSLEEP.toString());
                    this.add(CounterNames.NOSLEEP.toString(), (Long)-1L);
                }
            } else if (activity.getType() == ActivityType.IDLE && Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
                this.reset(CounterNames.NOSLEEP.toString());
                this.add(CounterNames.NOSLEEP.toString(), (Long)-1L);
            }
            if (activity.getType() == ActivityType.WHORE) {
                if (activity.getMainCustomer().getType() == CustomerType.GROUP) {
                    CustomerGroup group = (CustomerGroup)activity.getMainCustomer();
                    this.add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(group.getCustomers().size()));
                } else {
                    this.add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), Long.valueOf(activity.getMainCustomers().size()));
                }
            }
            this.add(CounterNames.MAINCUSTOMERS.toString(), Long.valueOf(activity.getMainCustomers().size()));
            this.add(CounterNames.SECONDARYCUSTOMERS.toString(), Long.valueOf(activity.getCustomers().size()));
        } else if (e.getType() == EventType.ACTIVITYFINISHED) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getCharacters().size() > 0) {
                this.add(CounterNames.MONEYEARNED.toString(), Long.valueOf(activity.getIncome() / activity.getCharacters().size()));
            }
        } else if (e.getType() == EventType.NEXTDAY) {
            this.reset(CounterNames.CUSTOMERSSERVEDTODAY.toString());
            this.add(CounterNames.DAYS.toString());
            if (this.get(CounterNames.DAYS.toString()) % 1000L == 0L && character.getType() == CharacterType.TRAINER && character != Jasbro.getInstance().getData().getProtagonist() && !character.getTraits().contains(Trait.LOYAL)) {
                character.addTrait(Trait.LOYAL);
                GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.loyal", character), ImageTag.CLOTHED, character, true), e);
            }
        } else if (e.getType() == EventType.NEXTSHIFT) {
            this.add(CounterNames.NOSLEEP.toString(), (Long)1L);
            if (this.get(CounterNames.NOSLEEP.toString()) >= 9L) {
                character.addCondition(new SleepDeprivation());
            }
        }
    }

    public static enum CounterNames {
        DAYS,
        SICK,
        MONEYEARNED,
        NOSLEEP,
        CUSTOMERSSERVEDTODAY,
        MAINCUSTOMERS,
        SECONDARYCUSTOMERS,
        CHILDREN;

    }
}

