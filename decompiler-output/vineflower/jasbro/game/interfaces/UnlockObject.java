package jasbro.game.interfaces;

import jasbro.gui.pictures.ImageData;

public interface UnlockObject {
   String getText();

   String getDescription();

   boolean isLocked();

   ImageData getImage();

   void setLocked(boolean var1);
}
