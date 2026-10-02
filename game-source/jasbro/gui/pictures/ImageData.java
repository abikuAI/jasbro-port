package jasbro.gui.pictures;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageData implements Serializable {
   private static final Logger log = LogManager.getLogger(ImageData.class);
   private Set<ImageTag> tags = new HashSet<>();
   private String key;
   private String filename;
   private String customText;
   private transient Future<Boolean> future;
   private transient int curAccuracy;

   public ImageData() {
   }

   public ImageData(String key) {
      this();
      this.key = key;
      this.filename = key;
   }

   public ImageData(Future<Boolean> future) {
      this();
      this.future = future;
   }

   public void init(ImageData imageData) {
      this.tags = imageData.tags;
      this.key = imageData.key;
      this.filename = imageData.filename;
      this.customText = imageData.customText;
      this.curAccuracy = imageData.curAccuracy;
   }

   public Set<ImageTag> getTags() {
      try {
         if (this.future != null && this.future.get()) {
         }
      } catch (Exception e) {
         log.error("Error during task", e);
      }

      return this.tags;
   }

   public boolean hasTag(ImageTag tag) {
      return this.getTags().contains(tag);
   }

   public String getKey() {
      try {
         if (this.future != null && this.future.get()) {
         }
      } catch (Exception e) {
         log.error("Error during task", e);
      }

      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public void addTag(ImageTag tag) {
      if (!this.getTags().contains(tag)) {
         this.getTags().add(tag);
      }
   }

   public void removeTag(ImageTag tag) {
      this.getTags().remove(tag);
   }

   @Override
   public String toString() {
      return this.getKey();
   }

   public String getTagString() {
      if (this.getTags().size() == 0) {
         return null;
      }

      String sTag = "";

      for (ImageTag tag : this.tags) {
         sTag = sTag + tag.toString() + ", ";
      }

      return sTag;
   }

   public String getFilename() {
      try {
         if (this.future != null && this.future.get()) {
         }
      } catch (Exception e) {
         log.error("Error during task", e);
      }

      return this.filename;
   }

   public void setFilename(String filename) {
      this.filename = filename;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      return 31 * result + (this.getKey() == null ? 0 : this.getKey().hashCode());
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj == null) {
         return false;
      }

      if (this.getClass() != obj.getClass()) {
         return false;
      }

      ImageData other = (ImageData)obj;
      if (this.getKey() == null) {
         if (other.getKey() != null) {
            return false;
         }
      } else if (!this.getKey().equals(other.getKey())) {
         return false;
      }

      return true;
   }

   public String getCustomText() {
      return this.customText;
   }

   public void setCustomText(String customText) {
      this.customText = customText;
   }

   public int getCurAccuracy() {
      return this.curAccuracy;
   }

   public void setCurAccuracy(int curAccuracy) {
      this.curAccuracy = curAccuracy;
   }

   public void setFuture(Future<Boolean> future) {
      this.future = future;
   }
}
