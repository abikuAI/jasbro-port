/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStage;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EventEffectSetQuestStagePanel
extends JPanel {
    private WorldEventSetQuestStage worldEventEffect;

    public EventEffectSetQuestStagePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.worldEventEffect = (WorldEventSetQuestStage)worldEventEffectTmp;
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel((Number)0, null, null, (Number)1));
        spinner.setValue(this.worldEventEffect.getQuestStage());
        this.add((Component)spinner, "1, 1, fill, top");
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                EventEffectSetQuestStagePanel.this.worldEventEffect.setQuestStage((int)((Integer)spinner.getValue()));
            }
        });
    }
}

