package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventEffectChance;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EventEffectChancePanel extends JPanel {
   private WorldEventEffectChance worldEventEffect;

   public EventEffectChancePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
      this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
      this.worldEventEffect = (WorldEventEffectChance)worldEventEffectTmp;
      final JSpinner spinner = new JSpinner();
      spinner.setModel(new SpinnerNumberModel(0, null, null, 1));
      spinner.setValue(this.worldEventEffect.getChance());
      this.add(spinner, "1, 1, fill, top");
      spinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EventEffectChancePanel.this.worldEventEffect.setChance((Integer)spinner.getValue());
         }
      });
   }
}
