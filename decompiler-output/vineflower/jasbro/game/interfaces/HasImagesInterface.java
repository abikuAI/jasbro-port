package jasbro.game.interfaces;

import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import java.util.List;

public interface HasImagesInterface {
   List<ImageData> getImages();

   List<ImageTag> getBaseTags();
}
