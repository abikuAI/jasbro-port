/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.housing.RoomLocationType;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.customContent.requirements.LocationTypeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.locations.LocationType;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LocationTypeRequirementPanel
extends JPanel {
    private LocationTypeRequirement triggerRequirement;
    private JComboBox<LocationTypeInterface> activityTypeComboBox;

    public LocationTypeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (LocationTypeRequirement)triggerRequirementTmp;
        JLabel label_1 = new JLabel(TextUtil.t("eventEditor.activityType"));
        this.add((Component)label_1, "1, 2, left, center");
        this.activityTypeComboBox = new JComboBox();
        this.add(this.activityTypeComboBox, "2, 2, fill, top");
        this.activityTypeComboBox.addItem(null);
        for (LocationType locationType : LocationType.values()) {
            this.activityTypeComboBox.addItem(locationType);
        }
        for (RoomInfo roomInfo : RoomInfoUtil.getRoomInfos()) {
            this.activityTypeComboBox.addItem(new RoomLocationType(roomInfo.getId()));
        }
        this.activityTypeComboBox.setSelectedItem(this.triggerRequirement.getLocationType());
        this.activityTypeComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                LocationTypeRequirementPanel.this.triggerRequirement.setLocationType((LocationTypeInterface)LocationTypeRequirementPanel.this.activityTypeComboBox.getSelectedItem());
            }
        });
    }
}

