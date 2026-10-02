/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomLocationType;
import jasbro.game.housing.RoomSlot;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import java.util.EnumMap;
import java.util.Map;

public abstract class Room
extends CharacterLocation {
    private Map<Time, PlannedActivity> usageMap = new EnumMap<Time, PlannedActivity>(Time.class);
    private RoomInfo roomInfo;
    private House house;
    private String name;

    private Room() {
        Map<Time, PlannedActivity> roomUsageMap = this.getUsageMap();
        for (Time time : Time.values()) {
            roomUsageMap.put(time, new PlannedActivity(this, ActivityType.SLEEP));
        }
    }

    public Room(RoomInfo roomInfo) {
        this();
        this.roomInfo = roomInfo;
    }

    @Override
    public int getMaxPeople() {
        for (RoomSlot roomSlot : this.house.getRoomSlots()) {
            if (roomSlot.getRoom() != this) continue;
            if (roomSlot.isAvailable()) break;
            return 0;
        }
        return this.roomInfo.getMaxOccupancy();
    }

    public House getHouse() {
        return this.house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public String getName() {
        if (this.name == null || this.name.trim().equals("")) {
            return this.roomInfo.getText();
        }
        return this.name.trim() + " (" + this.roomInfo.getText() + ")";
    }

    @Override
    public final String getDescription() {
        return this.roomInfo.getDescription();
    }

    public RoomInfo getRoomInfo() {
        return this.roomInfo;
    }

    @Override
    public String toString() {
        return this.roomInfo.getText();
    }

    public String getInternName() {
        return this.name;
    }

    public void setInternName(String name) {
        this.name = name;
    }

    @Override
    public LocationTypeInterface getLocationType() {
        return new RoomLocationType(this.roomInfo.getId());
    }

    @Override
    public PlannedActivity getCurrentUsage() {
        return this.getUsageMap().get((Object)Jasbro.getInstance().getData().getTime());
    }

    @Override
    public Map<Time, PlannedActivity> getUsageMap() {
        return this.usageMap;
    }
}

