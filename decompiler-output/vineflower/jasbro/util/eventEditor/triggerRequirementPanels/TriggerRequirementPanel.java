package jasbro.util.eventEditor.triggerRequirementPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.gui.GuiUtil;
import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TriggerRequirementPanel extends JPanel {
   private static final Logger log = LogManager.getLogger(TriggerRequirementPanel.class);
   private TriggerRequirement triggerRequirement;
   private JPanel contentPanel;
   private JPanel subRequirementsPanel;
   private boolean selected = false;

   public TriggerRequirementPanel(TriggerRequirement triggerRequirementTmp, MouseListener mouseListener) {
      this.triggerRequirement = triggerRequirementTmp;
      this.addMouseListener(mouseListener);
      this.setBackground(Color.GRAY);
      this.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.PREF_COLSPEC, FormFactory.PREF_COLSPEC, FormFactory.PREF_COLSPEC, ColumnSpec.decode("1dlu")},
            new RowSpec[]{RowSpec.decode("1dlu"), RowSpec.decode("pref:grow"), RowSpec.decode("1dlu")}
         )
      );
      JLabel lblNewLabel = new JLabel(triggerRequirementTmp.getType().getText());
      this.add(lblNewLabel, "1, 2");
      if (this.triggerRequirement != null) {
         try {
            Class<? extends JPanel> panelClass = this.triggerRequirement.getType().getEventPanelClass();
            if (panelClass != null) {
               this.contentPanel = panelClass.getConstructor(TriggerRequirement.class).newInstance(this.triggerRequirement);
            } else {
               this.contentPanel = new JPanel();
            }

            this.contentPanel.setOpaque(false);
            this.contentPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
            this.add(this.contentPanel, "2, 2, fill, fill");
         } catch (Exception ex) {
            log.error("Error when creating sub panel", ex);
         }
      }

      this.subRequirementsPanel = new JPanel();
      this.add(this.subRequirementsPanel, "3, 2, fill, fill");
      this.subRequirementsPanel.setLayout(new BoxLayout(this.subRequirementsPanel, 1));

      for (TriggerRequirement requirement : this.triggerRequirement.getSubRequirements()) {
         TriggerRequirementPanel triggerRequirementPanel = new TriggerRequirementPanel(requirement, mouseListener);
         this.subRequirementsPanel.add(triggerRequirementPanel);
         triggerRequirementPanel.addMouseListener(mouseListener);
      }
   }

   public void addPanel(TriggerRequirementPanel triggerRequirementPanel) {
      this.subRequirementsPanel.add(triggerRequirementPanel);
   }

   public void removePanel(TriggerRequirementPanel triggerRequirementPanel) {
      this.subRequirementsPanel.remove(triggerRequirementPanel);
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
      if (selected) {
         this.setBackground(Color.BLUE);
      } else {
         this.setBackground(Color.GRAY);
      }

      this.repaint();
   }

   public boolean isSelected() {
      return this.selected;
   }

   public JPanel getSubRequirementsPanel() {
      return this.subRequirementsPanel;
   }

   public TriggerRequirement getTriggerRequirement() {
      return this.triggerRequirement;
   }
}
