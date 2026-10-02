/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.world.customContent.requirements.ActivityRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ActivityTypeRequirementPanel
extends JPanel {
    private ActivityRequirement triggerRequirement;
    private JComboBox<ActivityType> activityTypeComboBox;

    public ActivityTypeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (ActivityRequirement)triggerRequirementTmp;
        JLabel label_1 = new JLabel(TextUtil.t("eventEditor.activityType"));
        this.add((Component)label_1, "1, 2, left, center");
        this.activityTypeComboBox = new JComboBox();
        this.add(this.activityTypeComboBox, "2, 2, fill, top");
        this.activityTypeComboBox.addItem(null);
        for (ActivityType activityType : ActivityType.values()) {
            this.activityTypeComboBox.addItem(activityType);
        }
        this.activityTypeComboBox.setSelectedItem((Object)this.triggerRequirement.getActivityType());
        this.activityTypeComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ActivityTypeRequirementPanel.this.triggerRequirement.setActivityType((ActivityType)((Object)ActivityTypeRequirementPanel.this.activityTypeComboBox.getSelectedItem()));
            }
        });
    }
}

