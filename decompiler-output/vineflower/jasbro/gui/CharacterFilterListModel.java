package jasbro.gui;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

public class CharacterFilterListModel extends AbstractListModel<Charakter> {
   private CharacterFilterListModel.Filter filter = new CharacterFilterListModel.Filter();
   private final ArrayList<Integer> displayedElements = new ArrayList<>();
   private List<Charakter> sourceList;

   public List<Charakter> getSourceList() {
      if (this.sourceList == null) {
         this.sourceList = Jasbro.getInstance().getData().getCharacters();
      }

      return this.sourceList;
   }

   public void filter() {
      this.displayedElements.clear();
      this.sourceList = null;

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

   public Charakter getElementAt(int index) {
      return this.getSourceList().get(this.displayedElements.get(index));
   }

   public void setFilter(CharacterFilterListModel.Filter filter) {
      this.filter = filter;
      this.filter();
   }

   public CharacterFilterListModel.Filter getFilter() {
      return this.filter;
   }

   public void reset() {
      this.filter = new CharacterFilterListModel.Filter();
      this.filter();
   }

   public static class Filter {
      private String searchString;
      private House house;
      private Gender gender;
      private CharacterType type;

      public Filter() {
      }

      public Filter(String searchString) {
         this.searchString = searchString;
      }

      public boolean accept(Charakter character) {
         if (this.searchString != null && !character.getName().toLowerCase().contains(this.searchString.toLowerCase())) {
            return false;
         }

         if (this.house != null) {
            if (character.getActivity() == null || !(character.getActivity().getSource() instanceof Room)) {
               return false;
            }

            if (((Room)character.getActivity().getSource()).getHouse() != this.house) {
               return false;
            }
         }

         return this.gender != null && character.getGender() != this.gender ? false : this.type == null || character.getType() == this.type;
      }

      public String getSearchString() {
         return this.searchString;
      }

      public House getHouse() {
         return this.house;
      }

      public void setHouse(House house) {
         this.house = house;
      }

      public void setSearchString(String searchString) {
         this.searchString = searchString;
      }

      public Gender getGender() {
         return this.gender;
      }

      public void setGender(Gender gender) {
         this.gender = gender;
      }

      public CharacterType getType() {
         return this.type;
      }

      public void setType(CharacterType type) {
         this.type = type;
      }
   }
}
