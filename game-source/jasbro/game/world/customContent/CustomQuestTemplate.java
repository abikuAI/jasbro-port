package jasbro.game.world.customContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.java.truevfs.access.TFile;

public class CustomQuestTemplate implements Serializable {
   private String id;
   private List<CustomQuestStage> questStages;
   private transient TFile file;

   public CustomQuestTemplate(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public List<CustomQuestStage> getQuestStages() {
      if (this.questStages == null) {
         this.questStages = new ArrayList<>();
      }

      return this.questStages;
   }

   public void setQuestStages(List<CustomQuestStage> questStages) {
      this.questStages = questStages;
   }

   public TFile getFile() {
      return this.file;
   }

   public void setFile(TFile file) {
      this.file = file;
   }

   @Override
   public String toString() {
      return this.id;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      return 31 * result + (this.id == null ? 0 : this.id.hashCode());
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

      CustomQuestTemplate other = (CustomQuestTemplate)obj;
      if (this.id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!this.id.equals(other.id)) {
         return false;
      }

      return true;
   }
}
