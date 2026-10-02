/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CharacterLocation
extends GameObject {
    public abstract List<ActivityDetails> getPossibleActivities(Time var1, Util.TypeAmounts var2);

    public abstract String getName();

    public abstract String getDescription();

    public abstract LocationTypeInterface getLocationType();

    public abstract PlannedActivity getCurrentUsage();

    public abstract Map<Time, PlannedActivity> getUsageMap();

    public abstract ImageData getImage();

    public final List<ActivityDetails> getPossibleActivities() {
        return this.getPossibleActivities(Jasbro.getInstance().getData().getTime());
    }

    public final List<ActivityDetails> getPossibleActivities(Time time) {
        Util.TypeAmounts typeAmounts = Util.getTypeAmounts(this.getCurrentUsage().getCharacters());
        List<ActivityDetails> activities = !typeAmounts.isChildPresent() ? this.getPossibleActivities(time, typeAmounts) : this.getPossibleActivitiesChildCare(time, typeAmounts);
        Jasbro.getInstance().getData().getQuestManager().modifyActivities(activities, time, this.getCurrentUsage().getCharacters(), typeAmounts, this);
        if (activities.size() == 0) {
            activities.add(new ActivityDetails(ActivityType.IDLE));
        }
        return activities;
    }

    public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, Util.TypeAmounts typeAmounts) {
        return new ArrayList<ActivityDetails>();
    }

    public PlannedActivity getUsage(Time time) {
        return this.getUsageMap().get((Object)time);
    }

    public boolean isFull() {
        return this.getCurrentUsage().getCharacters().size() >= this.getMaxPeople();
    }

    public int getMaxPeople() {
        return 6;
    }

    public void empty() {
        for (Time time : Time.values()) {
            PlannedActivity activity = this.getUsageMap().get((Object)time);
            activity.removeAllCharacters();
        }
    }

    public int getAmountPeople() {
        return this.getCurrentUsage().getCharacters().size();
    }

    public ActivityType getSelectedActivity() {
        return this.getCurrentUsage().getType();
    }

    public ActivityDetails getSelectedActivityDetails() {
        return this.getCurrentUsage().getActivityDetails();
    }

    public void setSelectedActivityDetails(ActivityDetails selectedActivity) {
        this.getCurrentUsage().setActivityDetails(selectedActivity);
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.ACTIVITYCHANGE) {
            this.fireEvent(e);
        }
    }

    public float getMoneyModifier(float currentModifier, Charakter character) {
        return currentModifier;
    }

    public String toString() {
        return this.getName();
    }
}

