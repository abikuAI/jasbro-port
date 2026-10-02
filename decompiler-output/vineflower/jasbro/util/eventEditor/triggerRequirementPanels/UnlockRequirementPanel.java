package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.housing.RoomUnlock;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.UnlockRequirement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class UnlockRequirementPanel extends JPanel {
   private UnlockRequirement triggerRequirement;
   private JComboBox<UnlockObject> unlockComboBox;

   public UnlockRequirementPanel(TriggerRequirement triggerRequirementTmp) {
      this.triggerRequirement = (UnlockRequirement)triggerRequirementTmp;
      this.unlockComboBox = new JComboBox<>();
      this.add(this.unlockComboBox);
      this.unlockComboBox.addItem(null);

      for (HouseType houseType : HouseType.values()) {
         this.unlockComboBox.addItem(houseType);
      }

      for (RoomUnlock roomUnlock : RoomInfoUtil.getRoomUnlocks()) {
         this.unlockComboBox.addItem(roomUnlock);
      }

      for (SpecializationType specializationType : SpecializationType.values()) {
         if (specializationType != SpecializationType.TRAINER
            && specializationType != SpecializationType.SLAVE
            && specializationType != SpecializationType.UNDERAGE
            && specializationType != SpecializationType.SEX
            && specializationType.getAssociatedSkillTree() != null) {
            this.unlockComboBox.addItem(specializationType);
         }
      }

      if (this.triggerRequirement.getUnlockObject() != null) {
         this.unlockComboBox.setSelectedItem(this.triggerRequirement.getUnlockObject());
      }

      this.unlockComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            UnlockRequirementPanel.this.triggerRequirement.setUnlockObject((UnlockObject)UnlockRequirementPanel.this.unlockComboBox.getSelectedItem());
         }
      });
   }
}
