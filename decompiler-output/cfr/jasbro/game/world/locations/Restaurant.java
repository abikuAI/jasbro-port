/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.locations;

import jasbro.Util;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.game.world.locations.LocationType;
import jasbro.game.world.locations.OtherLocation;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Restaurant
extends OtherLocation {
    public Restaurant() {
        Map<Time, PlannedActivity> roomUsageMap = this.getUsageMap();
        for (Time time : Time.values()) {
            roomUsageMap.put(time, new PlannedActivity(this, ActivityType.EAT));
        }
    }

    @Override
    public ImageData getImage() {
        return this.getType().getImage();
    }

    @Override
    public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
        ArrayList<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
        possibleActivities.add(new ActivityDetails(ActivityType.EAT));
        return possibleActivities;
    }

    @Override
    public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, Util.TypeAmounts typeAmounts) {
        ArrayList<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
        possibleActivities.add(new ActivityDetails(ActivityType.EAT));
        return possibleActivities;
    }

    @Override
    public String getName() {
        return this.getType().getText();
    }

    @Override
    public String getDescription() {
        return this.getType().getDescription();
    }

    @Override
    public LocationTypeInterface getLocationType() {
        return this.getType();
    }

    public LocationType getType() {
        return LocationType.RESTAURANT;
    }
}

