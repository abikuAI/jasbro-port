/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Unlocks;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;

public class DungeonLocations
implements AreaInterface {
    @Override
    public String getName() {
        return "Dungeons";
    }

    public List<LocationType> getLocationTypes() {
        ArrayList<LocationType> locationTypes = new ArrayList<LocationType>();
        Unlocks unlocks = Jasbro.getInstance().getData().getUnlocks();
        List<LocationType> availableLocations = unlocks.getAvailableLocations();
        if (availableLocations.contains(LocationType.DUNGEON1)) {
            locationTypes.add(LocationType.DUNGEON1);
        }
        if (availableLocations.contains(LocationType.DUNGEON2)) {
            locationTypes.add(LocationType.DUNGEON3);
        }
        if (availableLocations.contains(LocationType.DUNGEON3)) {
            locationTypes.add(LocationType.DUNGEON3);
        }
        if (availableLocations.contains(LocationType.DUNGEON4)) {
            locationTypes.add(LocationType.DUNGEON4);
        }
        return locationTypes;
    }

    public List<CharacterLocation> getLocations() {
        return this.getLocationList();
    }

    @Override
    public ImageData getImage() {
        return new ImageData("images/backgrounds/sky.jpg");
    }

    @Override
    public int getLocationAmount() {
        return this.getLocationTypes().size();
    }

    public List<CharacterLocation> getLocationList() {
        ArrayList<CharacterLocation> characterLocations = new ArrayList<CharacterLocation>();
        GameData gameData = Jasbro.getInstance().getData();
        for (LocationType locationType : this.getLocationTypes()) {
            if (gameData.getOtherLocationMap().containsKey(locationType)) {
                characterLocations.add(gameData.getOtherLocationMap().get(locationType));
                continue;
            }
            CharacterLocation location = locationType.getLocation();
            gameData.getOtherLocationMap().put(locationType, location);
            characterLocations.add(location);
        }
        return characterLocations;
    }
}

