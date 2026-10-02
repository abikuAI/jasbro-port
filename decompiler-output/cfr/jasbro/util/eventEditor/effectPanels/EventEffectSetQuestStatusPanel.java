/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStatus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class EventEffectSetQuestStatusPanel
extends JPanel {
    private WorldEventSetQuestStatus worldEventEffect;

    public EventEffectSetQuestStatusPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.worldEventEffect = (WorldEventSetQuestStatus)worldEventEffectTmp;
        final JComboBox<WorldEventSetQuestStatus.QuestStatus> questStatusComboBox = new JComboBox<WorldEventSetQuestStatus.QuestStatus>();
        this.add(questStatusComboBox, "1, 1, fill, top");
        for (WorldEventSetQuestStatus.QuestStatus questStatus : WorldEventSetQuestStatus.QuestStatus.values()) {
            questStatusComboBox.addItem(questStatus);
        }
        questStatusComboBox.setSelectedItem((Object)this.worldEventEffect.getQuestStatus());
        questStatusComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EventEffectSetQuestStatusPanel.this.worldEventEffect.setQuestStatus((WorldEventSetQuestStatus.QuestStatus)((Object)questStatusComboBox.getSelectedItem()));
            }
        });
    }
}

