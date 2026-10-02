package jasbro.util;

import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterType;
import jasbro.gui.pictures.ImageData;
import java.util.Comparator;

public class Comparators {
   public static class CharacterBaseFolderComparator implements Comparator<CharacterBase> {
      public int compare(CharacterBase o1, CharacterBase o2) {
         if (o1 == o2) {
            return 0;
         } else {
            return o1 == null ? -1 : o1.getId().compareTo(o2.getId());
         }
      }
   }

   public static class CharacterBaseNameComparator implements Comparator<CharacterBase> {
      public int compare(CharacterBase o1, CharacterBase o2) {
         if (o1 == o2) {
            return 0;
         } else {
            return o1 == null ? -1 : o1.getName().compareTo(o2.getName());
         }
      }
   }

   public static class CharacterTypeComparator implements Comparator<CharacterBase> {
      public int compare(CharacterBase o1, CharacterBase o2) {
         if (o1.getType() == o2.getType()) {
            return 0;
         } else if (o1.getType() == CharacterType.TRAINER) {
            return -1;
         } else {
            return o2.getType() == CharacterType.TRAINER ? 1 : 0;
         }
      }
   }

   public static class ImageDataComparator implements Comparator<ImageData> {
      public int compare(ImageData o1, ImageData o2) {
         if (o1 == o2) {
            return 0;
         } else {
            return o1 == null ? -1 : o1.getFilename().compareTo(o2.getFilename());
         }
      }
   }
}
