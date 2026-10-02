package jasbro.gui.objects.menus;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Gender;
import jasbro.game.housing.House;
import jasbro.gui.CharacterFilterListModel;
import jasbro.texts.TextUtil;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FilterMenu extends JPanel {
   private JTextField searchString;
   private JComboBox<House> houseSelectBox;
   private JComboBox<Gender> genderSelectBox;
   private JComboBox<CharacterType> typeSelectBox;

   public FilterMenu() {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.RELATED_GAP_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.RELATED_GAP_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.RELATED_GAP_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      CharacterFilterListModel.Filter filter = Jasbro.getInstance().getGui().getFilteredModel().getFilter();
      this.searchString = new JTextField();
      this.add(this.searchString, "1, 1, 3, 1, fill, default");
      this.searchString.setColumns(10);
      this.searchString.setText(filter.getSearchString());
      JLabel lblNewLabel = new JLabel(TextUtil.t("ui.gender"));
      this.add(lblNewLabel, "1, 3, right, default");
      this.genderSelectBox = new JComboBox<>();
      this.add(this.genderSelectBox, "3, 3, fill, default");
      this.genderSelectBox.addItem(null);

      for (Gender gender : Gender.values()) {
         this.genderSelectBox.addItem(gender);
      }

      this.genderSelectBox.setSelectedItem(filter.getGender());
      lblNewLabel = new JLabel(TextUtil.t("ui.type"));
      this.add(lblNewLabel, "1, 5, right, default");
      this.typeSelectBox = new JComboBox<>();
      this.add(this.typeSelectBox, "3, 5, fill, default");
      this.typeSelectBox.addItem(null);

      for (CharacterType type : CharacterType.values()) {
         this.typeSelectBox.addItem(type);
      }

      this.typeSelectBox.setSelectedItem(filter.getType());
      lblNewLabel = new JLabel(TextUtil.t("ui.house"));
      this.add(lblNewLabel, "1, 7, right, default");
      this.houseSelectBox = new JComboBox<>();
      this.add(this.houseSelectBox, "3, 7, fill, default");
      this.houseSelectBox.addItem(null);

      for (House house : Jasbro.getInstance().getData().getHouses()) {
         this.houseSelectBox.addItem(house);
      }

      this.houseSelectBox.setSelectedItem(filter.getHouse());
   }

   public CharacterFilterListModel.Filter getFilter() {
      CharacterFilterListModel.Filter filter = new CharacterFilterListModel.Filter();
      filter.setSearchString(this.searchString.getText());
      filter.setHouse((House)this.houseSelectBox.getSelectedItem());
      filter.setGender((Gender)this.genderSelectBox.getSelectedItem());
      filter.setType((CharacterType)this.typeSelectBox.getSelectedItem());
      return filter;
   }
}
