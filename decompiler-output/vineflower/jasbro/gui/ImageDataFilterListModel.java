package jasbro.gui;

import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

public class ImageDataFilterListModel extends AbstractListModel<ImageData> {
   private ImageDataFilterListModel.Filter filter = new ImageDataFilterListModel.Filter();
   private final ArrayList<Integer> displayedElements = new ArrayList<>();
   private HasImagesInterface imageContainerObject;

   public List<ImageData> getSourceList() {
      return this.imageContainerObject.getImages();
   }

   public void filter() {
      this.displayedElements.clear();

      for (int i = 0; i < this.getSourceList().size(); i++) {
         if (this.filter.accept(this.getSourceList().get(i))) {
            this.displayedElements.add(i);
         }
      }

      this.fireContentsChanged(this, 0, this.getSize() - 1);
   }

   @Override
   public int getSize() {
      return this.displayedElements.size();
   }

   public ImageData getElementAt(int index) {
      return this.getSourceList().get(this.displayedElements.get(index));
   }

   public void setFilter(ImageDataFilterListModel.Filter filter) {
      this.filter = filter;
      this.filter();
   }

   public ImageDataFilterListModel.Filter getFilter() {
      return this.filter;
   }

   public void reset() {
      this.filter = new ImageDataFilterListModel.Filter();
      this.filter();
   }

   public void setBase(HasImagesInterface characterBase) {
      this.imageContainerObject = characterBase;
      this.reset();
   }

   public static class Filter {
      private String searchString = "";
      private ImageTag imageTag;
      private boolean noTagsOnly;

      public Filter() {
      }

      public Filter(String searchString) {
         this.searchString = searchString;
      }

      public boolean accept(ImageData imageData) {
         if (this.searchString != null && !imageData.getFilename().toLowerCase().contains(this.searchString.toLowerCase())) {
            return false;
         } else {
            return this.noTagsOnly && imageData.getTags().size() > 0 ? false : this.imageTag == null || imageData.getTags().contains(this.imageTag);
         }
      }

      public String getSearchString() {
         return this.searchString;
      }

      public void setSearchString(String searchString) {
         this.searchString = searchString;
      }

      public ImageTag getImageTag() {
         return this.imageTag;
      }

      public void setImageTag(ImageTag imageTag) {
         this.imageTag = imageTag;
      }

      public boolean isNoTagsOnly() {
         return this.noTagsOnly;
      }

      public void setNoTagsOnly(boolean noTagsOnly) {
         this.noTagsOnly = noTagsOnly;
      }
   }
}
