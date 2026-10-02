package jasbro.game.world.customContent.npc;

import jasbro.game.character.battle.SimpleEnemy;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import java.util.ArrayList;
import java.util.List;

public class ComplexEnemy extends SimpleEnemy implements HasImagesInterface {
   private List<ImageData> images = new ArrayList<>();
   private String description;
   private String characterBaseId;
   private String itemBaseId;
   private List<String> encounterTexts;
   private List<String> rapeTexts;

   @Override
   public List<ImageData> getImages() {
      return this.images;
   }

   public void setImages(List<ImageData> images) {
      this.images = images;
   }

   @Override
   public List<ImageTag> getBaseTags() {
      return new ArrayList<>();
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getCharacterBaseId() {
      return this.characterBaseId;
   }

   public void setCharacterBaseId(String characterBaseId) {
      this.characterBaseId = characterBaseId;
   }

   public String getItemBaseId() {
      return this.itemBaseId;
   }

   public void setItemBaseId(String itemBaseId) {
      this.itemBaseId = itemBaseId;
   }

   public List<String> getEncounterTexts() {
      if (this.encounterTexts == null) {
         this.encounterTexts = new ArrayList<>();
      }

      return this.encounterTexts;
   }

   public void setEncounterTexts(List<String> encounterTexts) {
      this.encounterTexts = encounterTexts;
   }

   public List<String> getRapeTexts() {
      if (this.rapeTexts == null) {
         this.rapeTexts = new ArrayList<>();
      }

      return this.rapeTexts;
   }

   public void setRapeTexts(List<String> rapeTexts) {
      this.rapeTexts = rapeTexts;
   }
}
