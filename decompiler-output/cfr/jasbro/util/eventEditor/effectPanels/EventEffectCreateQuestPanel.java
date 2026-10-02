/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCreateQuest;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EventEffectCreateQuestPanel
extends JPanel {
    private WorldEventCreateQuest worldEventEffect;

    public EventEffectCreateQuestPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.worldEventEffect = (WorldEventCreateQuest)worldEventEffectTmp;
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.quest"));
        this.add((Component)lblNewLabel, "1, 1, right, fill");
        final JComboBox<String> questIdComboBox = new JComboBox<String>();
        this.add(questIdComboBox, "2, 1, fill, default");
        questIdComboBox.setEditable(true);
        questIdComboBox.setSelectedItem(this.worldEventEffect.getQuestId());
        questIdComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EventEffectCreateQuestPanel.this.worldEventEffect.setQuestId((String)questIdComboBox.getSelectedItem());
            }
        });
        ArrayList<String> questIdList = new ArrayList<String>(Jasbro.getInstance().getCustomQuestTemplates().keySet());
        Collections.sort(questIdList);
        for (String questId : questIdList) {
            questIdComboBox.addItem(questId);
        }
    }
}

