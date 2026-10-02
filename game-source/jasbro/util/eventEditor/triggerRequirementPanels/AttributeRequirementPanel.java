package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.requirements.AttributeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AttributeRequirementPanel extends JPanel {
   private AttributeRequirement triggerRequirement;

   public AttributeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
      this.triggerRequirement = (AttributeRequirement)triggerRequirementTmp;
      this.setLayout(new BoxLayout(this, 0));
      final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<>();
      this.add(attributeTypeCombobox, "1, 1");

      for (AttributeType attributeType : EssentialAttributes.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      for (AttributeType attributeType : BaseAttributeTypes.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      for (AttributeType attributeType : Sextype.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      for (AttributeType attributeType : SpecializationAttribute.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      for (AttributeType attributeType : CalculatedAttribute.values()) {
         attributeTypeCombobox.addItem(attributeType);
      }

      attributeTypeCombobox.setSelectedItem(this.triggerRequirement.getAttributeType());
      attributeTypeCombobox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AttributeRequirementPanel.this.triggerRequirement.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
         }
      });
      final JComboBox<TriggerRequirement.Comparison> comboBox = new JComboBox<>();
      this.add(comboBox);

      for (TriggerRequirement.Comparison dayComparison : TriggerRequirement.Comparison.values()) {
         comboBox.addItem(dayComparison);
      }

      comboBox.setSelectedItem(this.triggerRequirement.getComparison());
      comboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AttributeRequirementPanel.this.triggerRequirement.setComparison((TriggerRequirement.Comparison)comboBox.getSelectedItem());
         }
      });
      final JSpinner spinner = new JSpinner();
      spinner.setValue(this.triggerRequirement.getAmount());
      this.add(spinner);
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            AttributeRequirementPanel.this.triggerRequirement.setAmount((Integer)spinner.getValue());
         }
      });
   }
}
