/*
 * Decompiled with CFR 0.152.
 */
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

public class UnlockRequirementPanel
extends JPanel {
    private UnlockRequirement triggerRequirement;
    private JComboBox<UnlockObject> unlockComboBox;

    public UnlockRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (UnlockRequirement)triggerRequirementTmp;
        this.unlockComboBox = new JComboBox();
        this.add(this.unlockComboBox);
        this.unlockComboBox.addItem(null);
        for (HouseType houseType : HouseType.values()) {
            this.unlockComboBox.addItem(houseType);
        }
        for (RoomUnlock roomUnlock : RoomInfoUtil.getRoomUnlocks()) {
            this.unlockComboBox.addItem(roomUnlock);
        }
        for (Enum enum_ : SpecializationType.values()) {
            if (enum_ == SpecializationType.TRAINER || enum_ == SpecializationType.SLAVE || enum_ == SpecializationType.UNDERAGE || enum_ == SpecializationType.SEX || ((SpecializationType)enum_).getAssociatedSkillTree() == null) continue;
            this.unlockComboBox.addItem((UnlockObject)((Object)enum_));
        }
        if (this.triggerRequirement.getUnlockObject() != null) {
            this.unlockComboBox.setSelectedItem(this.triggerRequirement.getUnlockObject());
        }
        this.unlockComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                UnlockRequirementPanel.this.triggerRequirement.setUnlockObject((UnlockObject)UnlockRequirementPanel.this.unlockComboBox.getSelectedItem());
            }
        });
    }
}

