package jasbro.game.world;

import jasbro.gui.pictures.ImageData;
import java.util.List;

public interface AreaInterface {
   String getName();

   List<CharacterLocation> getLocations();

   ImageData getImage();

   int getLocationAmount();
}
