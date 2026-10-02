package jasbro.game.interfaces;

import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;
import java.util.List;

public interface AreaInterface {
   String getName();

   List<? extends CharacterLocation> getLocations();

   ImageData getImage();

   int getLocationAmount();
}
