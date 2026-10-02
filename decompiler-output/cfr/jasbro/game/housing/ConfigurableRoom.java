/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.housing;

import jasbro.Util;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.housing.RoomInfo;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;

public class ConfigurableRoom
extends Room {
    private static final long serialVersionUID = 1L;

    public ConfigurableRoom(RoomInfo roomInfo) {
        super(roomInfo);
    }

    public ConfigurableRoom(RoomInfo roomInfo, House house) {
        this(roomInfo);
        this.setHouse(house);
    }

    @Override
    public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
        ArrayList<ActivityDetails> valid = new ArrayList<ActivityDetails>();
        for (ActivityType activity : this.getRoomInfo().getActivities()) {
            if (!this.getRoomInfo().isActivityValid(activity, this.getUsage(time).getCharacters(), typeAmounts)) continue;
            valid.add(new ActivityDetails(activity));
        }
        return valid;
    }

    @Override
    public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, Util.TypeAmounts typeAmounts) {
        ArrayList<ActivityDetails> valid = new ArrayList<ActivityDetails>();
        for (ActivityType activity : this.getRoomInfo().getChildCareActivities()) {
            if (!this.getRoomInfo().isChildCareActivityValid(activity, this.getCurrentUsage().getCharacters(), typeAmounts)) continue;
            valid.add(new ActivityDetails(activity));
        }
        return valid;
    }

    @Override
    public ImageData getImage() {
        return this.getRoomInfo().getImage();
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (this.getRoomInfo().hasEventHandler()) {
            this.getRoomInfo().getEventHandler().handleEvent(e);
        }
        super.handleEvent(e);
    }

    @Override
    public int getMaxPeople() {
        return this.getRoomInfo().getMaxOccupancy();
    }
}

