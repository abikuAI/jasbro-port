package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.Trigger;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TriggerListPanel extends JPanel {
   private CustomQuestStage customQuestStage;
   private WorldEvent worldEvent;
   private List<Trigger> triggers;
   private JPanel triggerListPanel;

   private TriggerListPanel() {
      this.init(null);
   }

   public TriggerListPanel(CustomQuestStage customQuestStage) {
      this.customQuestStage = customQuestStage;
      this.init(customQuestStage.getTriggers());
   }

   public TriggerListPanel(WorldEvent worldEvent) {
      this.worldEvent = worldEvent;
      this.init(worldEvent.getTriggers());
   }

   private void init(List<Trigger> triggersTmp) {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("pref:grow"), FormFactory.RELATED_GAP_ROWSPEC}
         )
      );
      JButton addTriggerButton = new JButton(TextUtil.t("eventEditor.addTrigger"));
      this.add(addTriggerButton, "1, 2, right, default");
      addTriggerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Trigger trigger = new Trigger();
            TriggerListPanel.this.triggers.add(trigger);
            TriggerListPanel.this.triggerListPanel.add(new TriggerPanel(trigger, TriggerListPanel.this.customQuestStage));
            Component parent = TriggerListPanel.this.getParent();

            while (parent.getParent() != null && !(parent instanceof QuestEditorPanel)) {
               parent = parent.getParent();
            }

            parent.validate();
            parent.repaint();
         }
      });
      this.triggerListPanel = new JPanel();
      this.add(this.triggerListPanel, "1, 3, 3, 1, fill, fill");
      this.triggerListPanel.setLayout(new BoxLayout(this.triggerListPanel, 1));
      if (triggersTmp != null) {
         this.triggers = triggersTmp;

         for (Trigger trigger : this.triggers) {
            if (this.customQuestStage != null) {
               this.triggerListPanel.add(new TriggerPanel(trigger, this.customQuestStage));
            } else {
               this.triggerListPanel.add(new TriggerPanel(trigger, this.worldEvent));
            }
         }
      }
   }

   public List<Trigger> getTriggers() {
      return this.triggers;
   }
}
