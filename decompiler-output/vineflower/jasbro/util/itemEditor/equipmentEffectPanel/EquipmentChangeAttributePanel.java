package jasbro.util.itemEditor.equipmentEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentChangeAttribute;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EquipmentChangeAttributePanel extends JPanel {
   private EquipmentChangeAttribute itemEffect;

   public EquipmentChangeAttributePanel(EquipmentEffect equipmentEffect) {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      this.add(new JLabel(equipmentEffect.getName()), "1, 1, left, center");
      this.itemEffect = (EquipmentChangeAttribute)equipmentEffect;
      this.add(new JLabel(TextUtil.t("attribute")), "1, 2, left, center");
      final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<>();
      this.add(attributeTypeCombobox, "2, 2, fill, top");

      for (AttributeType attributeType : BaseAttributeTypes.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      for (AttributeType attributeType : Sextype.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      for (AttributeType attributeType : SpecializationAttribute.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      attributeTypeCombobox.setSelectedItem(this.itemEffect.getAttributeType());
      attributeTypeCombobox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EquipmentChangeAttribute effect = EquipmentChangeAttributePanel.this.itemEffect;
            effect.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
         }
      });
      this.add(new JLabel(TextUtil.t("ui.change")), "1, 3, left, center");
      final JSpinner spinner = new JSpinner();
      spinner.setValue(this.itemEffect.getAmount());
      this.add(spinner, "2, 3, fill, top");
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EquipmentChangeAttribute effect = EquipmentChangeAttributePanel.this.itemEffect;
            effect.setAmount((Integer)spinner.getValue());
         }
      });
   }
}
