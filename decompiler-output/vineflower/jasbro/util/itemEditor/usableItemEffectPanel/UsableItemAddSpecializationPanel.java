package jasbro.util.itemEditor.usableItemEffectPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.items.usableItemEffects.UsableItemAddSpecialization;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UsableItemAddSpecializationPanel extends JPanel {
   private UsableItemAddSpecialization itemEffect;

   public UsableItemAddSpecializationPanel(UsableItemEffect usableItemEffect) {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      this.add(new JLabel(usableItemEffect.getName()), "1, 1, left, center");
      this.itemEffect = (UsableItemAddSpecialization)usableItemEffect;
      this.add(new JLabel(TextUtil.t("condition")), "1, 2, left, center");
      final JComboBox<SpecializationType> SpecializationTypeCombobox = new JComboBox<>();
      this.add(SpecializationTypeCombobox, "2, 2, fill, top");

      for (SpecializationType specializationType : SpecializationType.values()) {
         SpecializationTypeCombobox.addItem(specializationType);
      }

      SpecializationTypeCombobox.setSelectedItem(this.itemEffect.getSpecializationType());
      SpecializationTypeCombobox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            UsableItemAddSpecializationPanel.this.itemEffect.setSpecializationType((SpecializationType)SpecializationTypeCombobox.getSelectedItem());
         }
      });
   }
}
