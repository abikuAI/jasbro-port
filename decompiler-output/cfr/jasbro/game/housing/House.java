/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.BuildingAdvertising;
import jasbro.game.events.business.BuildingMercSecurity;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.housing.CleanState;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.Room;
import jasbro.game.housing.RoomSlot;
import jasbro.game.housing.RoomSlotType;
import jasbro.game.housing.SecurityState;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class House
extends GameObject
implements AreaInterface {
    private HouseType houseType;
    private List<RoomSlot> roomSlots = new ArrayList<RoomSlot>();
    private int value;
    private String name;
    private Fame fame = new Fame();
    private int dirt = 0;
    private int security = 100;
    private boolean mercs = false;
    private SpawnData spawnData;
    private Map<Time, ImageData> images;
    private BuildingAdvertising advertising;
    private BuildingMercSecurity mercenaries;

    public House(HouseType houseType, int value) {
        this.houseType = houseType;
        this.value = value;
    }

    public House(HouseType type, int value, String morningImage, String afternoonImage, String nightImage, RoomSlotType[] slots, String[] roomInfoIds) {
        this(type, value);
        this.images = new HashMap<Time, ImageData>();
        this.images.put(Time.MORNING, new ImageData(morningImage));
        this.images.put(Time.AFTERNOON, new ImageData(afternoonImage));
        this.images.put(Time.NIGHT, new ImageData(nightImage));
        for (int i = 0; i < slots.length; ++i) {
            this.roomSlots.add(new RoomSlot(slots[i], roomInfoIds[i], this));
        }
    }

    public int getRoomAmount() {
        return this.roomSlots.size();
    }

    public void initImages() {
    }

    public int getAmountPeople() {
        int amount = 0;
        for (Room room : this.getRooms()) {
            if (room.getCurrentUsage() == null) continue;
            amount += room.getCurrentUsage().getCharacters().size();
        }
        return amount;
    }

    public List<RoomSlot> getRoomSlots() {
        return this.roomSlots;
    }

    public List<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<Room>();
        for (RoomSlot roomSlot : this.getRoomSlots()) {
            rooms.add(roomSlot.getRoom());
        }
        return rooms;
    }

    public String getInternName() {
        return this.name;
    }

    @Override
    public String getName() {
        if (this.name == null || this.name.trim().equals("")) {
            return this.houseType.getText();
        }
        return this.name.trim() + " (" + this.houseType.getText() + ")";
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public ImageData getImage() {
        return this.getImages().get((Object)Jasbro.getInstance().getData().getTime());
    }

    public void setInternName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Map<Time, ImageData> getImages() {
        if (this.images == null) {
            this.images = new HashMap<Time, ImageData>();
            this.initImages();
        }
        return this.images;
    }

    public void setImages(HashMap<Time, ImageData> images) {
        this.images = images;
    }

    public void empty() {
        for (Room room : this.getRooms()) {
            room.empty();
        }
    }

    public String toString() {
        return this.getName();
    }

    public Fame getFame() {
        if (this.fame == null) {
            this.fame = new Fame();
        }
        return this.fame;
    }

    public void setFame(Fame fame) {
        this.fame = fame;
    }

    public int getDirt() {
        return this.dirt;
    }

    public void modDirt(int mod) {
        this.dirt += mod;
    }

    public void applyDirtLimit() {
        if (this.dirt < 0) {
            this.dirt = 0;
        }
    }

    public void applySecurityLimit() {
        if (this.security < 0) {
            this.security = 0;
        }
        if (this.security > 100) {
            this.security = 100;
        }
    }

    public int getSellPrice() {
        return (int)Util.getPercent(this.getValue(), 75);
    }

    public CleanState getCleanState() {
        return CleanState.calcState(this);
    }

    public int getSecurity() {
        return this.security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public void modSecurity(int security) {
        this.security += security;
    }

    public boolean hasMercenaries() {
        return this.mercs;
    }

    public void setMercenaries(boolean mercenaries) {
        this.mercs = mercenaries;
    }

    public SecurityState getSecurityState() {
        return SecurityState.calcState(this);
    }

    @Override
    public int getLocationAmount() {
        return this.roomSlots.size();
    }

    @Override
    public List<? extends CharacterLocation> getLocations() {
        return this.getRoomSlots();
    }

    public HouseType getHouseType() {
        return this.houseType;
    }

    public SpawnData getSpawnData() {
        if (this.spawnData == null) {
            for (House house : Jasbro.getInstance().getData().getHouses()) {
                if (house == this || house.spawnData == null) continue;
                this.spawnData = new SpawnData(house.spawnData);
            }
        }
        if (this.spawnData == null) {
            this.spawnData = new SpawnData();
        }
        return this.spawnData;
    }

    public BuildingAdvertising getAdvertising() {
        if (this.advertising == null) {
            this.advertising = new BuildingAdvertising();
        }
        return this.advertising;
    }

    public BuildingMercSecurity getMercSecurity() {
        if (this.mercenaries == null) {
            this.mercenaries = new BuildingMercSecurity();
        }
        return this.mercenaries;
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY || e.getType() == EventType.NEXTSHIFT) {
            for (RoomSlot roomSlot : this.getRoomSlots()) {
                roomSlot.handleEvent(e);
            }
        }
        if (e.getType() == EventType.NEXTDAY) {
            int pay = 0;
            pay += this.getValue() / 100;
            if (Jasbro.getInstance().getData().getDay() % 30 == 0) {
                for (Room room : this.getRooms()) {
                    if (room == null) continue;
                    pay += room.getRoomInfo().getCost() / 10;
                }
            }
            Jasbro.getInstance().getData().spendMoney(pay, this);
        }
        super.handleEvent(e);
        this.getSpawnData().handleEvent(e);
    }
}

