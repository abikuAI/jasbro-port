package jasbro.game.world.customContent;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TPath;

public class ImageSelection {
   private String image;
   private ImageSelection.ImageLocation imageLocation = ImageSelection.ImageLocation.LOCAL;
   private List<ImageTag> imageTags;
   private boolean background = false;
   private String target = "character";

   public ImageData getImageData(WorldEvent worldEvent) throws EvalError {
      if (this.background) {
         return ((Charakter)worldEvent.getAttribute(this.target)).getBackground();
      }

      if (this.image != null) {
         String relativePath;
         if (this.imageLocation == ImageSelection.ImageLocation.LOCAL) {
            TFile folder = worldEvent.getFile().getParentFile();
            relativePath = this.getRelativePathFor(folder);
         } else {
            relativePath = "images";
         }

         return new ImageData(relativePath + "/" + this.image);
      } else if (this.imageTags.size() > 0) {
         HasImagesInterface character = (HasImagesInterface)worldEvent.getAttribute(this.target);
         return ImageUtil.getInstance().getImageDataByTag(this.imageTags.get(0), character);
      } else {
         return null;
      }
   }

   private String getRelativePathFor(final TFile folder) {
      return AccessController.doPrivileged(new PrivilegedAction<String>() {
         public String run() {
            return new TPath(new TFile("")).relativize(new TPath(folder)).toString();
         }
      });
   }

   public ImageData getImageData(Item item) {
      if (this.image != null) {
         String relativePath;
         if (this.imageLocation == ImageSelection.ImageLocation.LOCAL) {
            TFile folder = item.getFile().getParentFile();
            relativePath = this.getRelativePathFor(folder);
         } else {
            relativePath = "images";
         }

         return new ImageData(relativePath + "/" + this.image);
      } else {
         return null;
      }
   }

   public String getImage() {
      return this.image;
   }

   public void setImage(String image) {
      this.image = image;
   }

   public String getTarget() {
      return this.target;
   }

   public void setTarget(String target) {
      this.target = target;
   }

   public boolean isBackground() {
      return this.background;
   }

   public void setBackground(boolean background) {
      this.background = background;
   }

   public ImageSelection.ImageLocation getImageLocation() {
      return this.imageLocation;
   }

   public void setImageLocation(ImageSelection.ImageLocation imageLocation) {
      this.imageLocation = imageLocation;
   }

   public List<ImageTag> getImageTags() {
      if (this.imageTags == null) {
         this.imageTags = new ArrayList<>();
         this.imageTags.add(ImageTag.STANDARD);
      }

      return this.imageTags;
   }

   public void setImageTags(List<ImageTag> imageTags) {
      this.imageTags = imageTags;
   }

   public enum ImageLocation {
      LOCAL,
      GLOBAL;
   }
}
