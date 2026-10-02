/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.housing;

import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.EventType;
import jasbro.game.events.rooms.ClassRoomEventHandler;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.events.rooms.EmptyRoomEventHandler;
import jasbro.game.events.rooms.Garden;
import jasbro.game.events.rooms.MasterBedroomEventHandler;
import jasbro.game.events.rooms.SexSatisfactionEventHandler;
import jasbro.game.events.rooms.SickroomEventHandler;
import jasbro.game.housing.ConfigurableRoom;
import jasbro.game.housing.Room;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomUnlock;
import jasbro.game.world.RoomLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RoomInfoUtil {
    private static final Map<String, RoomInfo> roomInfos = RoomLoader.loadRooms(null);
    private static final Map<String, RoomUnlock> roomUnlocks = new HashMap<String, RoomUnlock>();

    public static RoomInfo getRoomInfo(String id) {
        return roomInfos.get(id);
    }

    public static Collection<RoomInfo> getRoomInfos() {
        return roomInfos.values();
    }

    public static RoomUnlock getRoomUnlock(String id) {
        return roomUnlocks.get(id);
    }

    public static Collection<RoomUnlock> getRoomUnlocks() {
        return roomUnlocks.values();
    }

    public static Room newRoom(String id) {
        if ("GARDEN".equals(id) || "BIGGARDEN".equals(id)) {
            return new Garden(roomInfos.get(id));
        }
        if ("CRYPT".equals(id)) {
            return new Crypt(roomInfos.get(id));
        }
        return new ConfigurableRoom(roomInfos.get(id));
    }

    static {
        roomInfos.get("EMPTYROOM").setEventHandler(new EmptyRoomEventHandler());
        roomInfos.get("MASTERBEDROOM").setEventHandler(new MasterBedroomEventHandler());
        roomInfos.get("SICKROOM").setEventHandler(new SickroomEventHandler());
        roomInfos.get("DUNGEON").setEventHandler(new SexSatisfactionEventHandler(EventType.ACTIVITY, Sextype.BONDAGE, 30));
        roomInfos.get("CLASSROOM").setEventHandler(new ClassRoomEventHandler());
        for (String key : roomInfos.keySet()) {
            roomUnlocks.put(key, new RoomUnlock(key));
        }
    }
}

