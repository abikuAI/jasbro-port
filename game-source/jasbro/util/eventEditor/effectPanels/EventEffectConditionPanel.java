package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCondition;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.triggerRequirementPanels.TriggerRequirementPanel;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventEffectConditionPanel extends JPanel {
   private static final Logger log = LogManager.getLogger(EventEffectConditionPanel.class);
   private WorldEventCondition worldEventEffect;
   private JPanel triggerRequirementPanel;
   private TriggerRequirementPanel selectedRequirementPanel;

   public EventEffectConditionPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               ColumnSpec.decode("left:default:none"), ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC
            },
            new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC}
         )
      );
      this.worldEventEffect = (WorldEventCondition)worldEventEffectTmp;
      JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.requirements"));
      this.add(lblNewLabel_1, "1, 1, left, default");
      final JComboBox<TriggerRequirementType> requirementComboBox = new JComboBox<>();
      this.add(requirementComboBox, "2, 1, fill, default");

      for (TriggerRequirementType triggerRequirementType : TriggerRequirementType.values()) {
         requirementComboBox.addItem(triggerRequirementType);
      }

      JButton addRequirementButton = new JButton(TextUtil.t("eventEditor.addRequirement"));
      this.add(addRequirementButton, "3, 1");
      addRequirementButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                  TriggerRequirement triggerRequirement = ((TriggerRequirementType)requirementComboBox.getSelectedItem()).getRequirementClass().newInstance();
                  TriggerRequirementPanel triggerPanel = new TriggerRequirementPanel(triggerRequirement, EventEffectConditionPanel.this.new MyMouseListener());
                  if (EventEffectConditionPanel.this.triggerRequirementPanel.getComponents().length == 0) {
                     EventEffectConditionPanel.this.triggerRequirementPanel.add(triggerPanel);
                     EventEffectConditionPanel.this.worldEventEffect.setRequirement(triggerRequirement);
                     EventEffectConditionPanel.this.setSelected(triggerPanel);
                  } else if (EventEffectConditionPanel.this.selectedRequirementPanel != null
                     && EventEffectConditionPanel.this.selectedRequirementPanel.getTriggerRequirement().canAddRequirement(triggerRequirement)) {
                     EventEffectConditionPanel.this.selectedRequirementPanel.getTriggerRequirement().getSubRequirements().add(triggerRequirement);
                     EventEffectConditionPanel.this.selectedRequirementPanel.addPanel(triggerPanel);
                     EventEffectConditionPanel.this.setSelected(triggerPanel);
                  }

                  EventEffectConditionPanel.this.validate();
                  EventEffectConditionPanel.this.repaint();
               } catch (Exception ex) {
                  EventEffectConditionPanel.log.error("Error when creating trigger requirement panel", ex);
               }
            }
         }
      );
      JButton deleteRequirementButton = new JButton(TextUtil.t("eventEditor.deleteRequirement"));
      this.add(deleteRequirementButton, "4, 1");
      deleteRequirementButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (EventEffectConditionPanel.this.selectedRequirementPanel != null) {
                  Container parent = EventEffectConditionPanel.this.selectedRequirementPanel.getParent();
                  if (parent == EventEffectConditionPanel.this.triggerRequirementPanel) {
                     EventEffectConditionPanel.this.worldEventEffect.setRequirement(null);
                     EventEffectConditionPanel.this.triggerRequirementPanel.remove(EventEffectConditionPanel.this.selectedRequirementPanel);
                  } else {
                     parent.remove(EventEffectConditionPanel.this.selectedRequirementPanel);

                     while (!(parent instanceof TriggerRequirementPanel)) {
                        parent = parent.getParent();
                     }

                     ((TriggerRequirementPanel)parent)
                        .getTriggerRequirement()
                        .getSubRequirements()
                        .remove(EventEffectConditionPanel.this.selectedRequirementPanel.getTriggerRequirement());
                  }

                  EventEffectConditionPanel.this.selectedRequirementPanel = null;
                  EventEffectConditionPanel.this.validate();
                  EventEffectConditionPanel.this.repaint();
               }
            }
         }
      );
      this.triggerRequirementPanel = new JPanel();
      this.add(this.triggerRequirementPanel, "1, 2, 4, 1, fill, fill");
      this.triggerRequirementPanel.setLayout(new GridLayout(0, 1, 0, 0));
      if (this.worldEventEffect.getRequirement() != null) {
         this.addTriggerRequirementPanel(this.worldEventEffect.getRequirement());
      }
   }

   public void addTriggerRequirementPanel(TriggerRequirement triggerRequirement) {
      TriggerRequirementPanel triggerRequirementPanel = new TriggerRequirementPanel(triggerRequirement, new EventEffectConditionPanel.MyMouseListener());
      this.triggerRequirementPanel.add(triggerRequirementPanel);
   }

   public void setSelected(TriggerRequirementPanel triggerRequirementPanel) {
      if (this.selectedRequirementPanel != null) {
         this.selectedRequirementPanel.setSelected(false);
      }

      this.selectedRequirementPanel = triggerRequirementPanel;
      triggerRequirementPanel.setSelected(true);
      this.validate();
      this.repaint();
   }

   private class MyMouseListener extends MouseAdapter {
      private MyMouseListener() {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         if (e.getSource() instanceof TriggerRequirementPanel) {
            TriggerRequirementPanel newPanel = (TriggerRequirementPanel)e.getSource();
            EventEffectConditionPanel.this.setSelected(newPanel);
         }
      }
   }
}
