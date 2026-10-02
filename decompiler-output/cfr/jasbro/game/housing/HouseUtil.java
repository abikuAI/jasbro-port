/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.housing;

import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomSlotType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HouseUtil {
    private static final transient Logger LOG = LogManager.getLogger(HouseUtil.class);

    public static House newHouse(HouseType type) {
        return HouseUtil.loadHouseFromProperties(type.toString().toLowerCase());
    }

    private static House loadHouseFromProperties(String id) {
        Properties houseProps = new Properties();
        try (InputStream is = HouseUtil.class.getResourceAsStream("/houses/" + id + ".properties");){
            houseProps.load(is);
        }
        catch (IOException e) {
            LOG.error("Failed to load house with id '" + id + "' from properties", (Throwable)e);
            return null;
        }
        HouseType type = HouseType.valueOf(houseProps.getProperty("type"));
        int value = Integer.parseInt(houseProps.getProperty("value"));
        String morningImage = houseProps.getProperty("image.morning");
        String afternoonImage = houseProps.getProperty("image.afternoon");
        String nightImage = houseProps.getProperty("image.night");
        String[] rawSlotTypes = houseProps.getProperty("rooms.slots").split(",");
        String[] roomInfoIds = houseProps.getProperty("rooms.default").split(",");
        if (rawSlotTypes.length != roomInfoIds.length) {
            LOG.error("'rooms.slots' and 'rooms.default' do not have the same number if items in properties for '{}'", new Object[]{id});
            return null;
        }
        RoomSlotType[] slotTypes = new RoomSlotType[rawSlotTypes.length];
        for (int i = 0; i < rawSlotTypes.length; ++i) {
            rawSlotTypes[i] = rawSlotTypes[i].trim();
            roomInfoIds[i] = roomInfoIds[i].trim();
            slotTypes[i] = RoomSlotType.valueOf(rawSlotTypes[i]);
        }
        return new House(type, value, morningImage, afternoonImage, nightImage, slotTypes, roomInfoIds);
    }
}

