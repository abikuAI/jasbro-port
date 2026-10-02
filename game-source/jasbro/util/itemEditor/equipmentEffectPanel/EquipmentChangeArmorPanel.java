package jasbro.util.itemEditor.equipmentEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.equipmentEffect.EquipmentChangeArmor;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.texts.TextUtil;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EquipmentChangeArmorPanel extends JPanel {
   private EquipmentChangeArmor itemEffect;

   public EquipmentChangeArmorPanel(EquipmentEffect equipmentEffect) {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      this.add(new JLabel(equipmentEffect.getName()), "1, 1, left, center");
      this.itemEffect = (EquipmentChangeArmor)equipmentEffect;
      this.add(new JLabel(TextUtil.t("ui.amount")), "1, 3, left, center");
      final JSpinner spinner = new JSpinner();
      spinner.setValue(this.itemEffect.getAmount());
      this.add(spinner, "2, 3, fill, top");
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EquipmentChangeArmor effect = EquipmentChangeArmorPanel.this.itemEffect;
            effect.setAmount((Integer)spinner.getValue());
         }
      });
   }
}
