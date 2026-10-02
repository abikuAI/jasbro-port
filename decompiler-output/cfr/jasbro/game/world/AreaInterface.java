/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world;

import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;
import java.util.List;

public interface AreaInterface {
    public String getName();

    public List<CharacterLocation> getLocations();

    public ImageData getImage();

    public int getLocationAmount();
}

