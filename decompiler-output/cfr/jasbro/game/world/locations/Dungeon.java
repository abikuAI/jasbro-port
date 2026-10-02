/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.locations;

import jasbro.Jasbro;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Dungeon
extends OtherLocation {
    private transient Map<Time, ImageData> images;
    private LocationType type;

    public Dungeon() {
        Map<Time, PlannedActivity> roomUsageMap = this.getUsageMap();
        for (Time time : Time.values()) {
            roomUsageMap.put(time, new PlannedActivity(this, ActivityType.EXPLORE));
        }
    }

    @Override
    public ImageData getImage() {
        return this.getImages().get((Object)Jasbro.getInstance().getData().getTime());
    }

    @Override
    public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
        ArrayList<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
        possibleActivities.add(new ActivityDetails(ActivityType.EXPLORE));
        return possibleActivities;
    }

    private Map<Time, ImageData> getImages() {
        if (this.images == null) {
            this.images = new EnumMap<Time, ImageData>(Time.class);
            this.images.put(Time.MORNING, new ImageData("images/backgrounds/dungeon/dungeon1.png"));
            this.images.put(Time.AFTERNOON, new ImageData("images/backgrounds/dungeon/dungeon1.png"));
            this.images.put(Time.NIGHT, new ImageData("images/backgrounds/dungeon/dungeon1.png"));
        }
        return this.images;
    }

    @Override
    public LocationTypeInterface getLocationType() {
        return this.type;
    }

    public LocationType getType() {
        return this.type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return this.type.getText();
    }

    @Override
    public String getDescription() {
        return this.type.getDescription();
    }

    @Override
    public int getMaxPeople() {
        return 4;
    }
}

