package jasbro.util.itemEditor.equipmentEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentChangeAttributeMax;
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

public class EquipmentChangeAttributeMaxPanel extends JPanel {
   private EquipmentChangeAttributeMax itemEffect;

   public EquipmentChangeAttributeMaxPanel(EquipmentEffect equipmentEffect) {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      this.add(new JLabel(equipmentEffect.getName()), "1, 1, left, center");
      this.itemEffect = (EquipmentChangeAttributeMax)equipmentEffect;
      this.add(new JLabel(TextUtil.t("attribute")), "1, 2, left, center");
      final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<>();
      this.add(attributeTypeCombobox, "2, 2, fill, top");

      for (AttributeType attributeType : EssentialAttributes.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      attributeTypeCombobox.setSelectedItem(this.itemEffect.getAttributeType());
      attributeTypeCombobox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EquipmentChangeAttributeMaxPanel.this.itemEffect.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
         }
      });
      this.add(new JLabel(TextUtil.t("ui.change")), "1, 3, left, center");
      final JSpinner spinner = new JSpinner();
      spinner.setValue(this.itemEffect.getAmount());
      this.add(spinner, "2, 3, fill, top");
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EquipmentChangeAttributeMaxPanel.this.itemEffect.setAmount((Integer)spinner.getValue());
         }
      });
   }
}
