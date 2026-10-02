package jasbro.gui.perks;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Dimension;
import javax.swing.JPanel;

public class SkillTreeColumnPanel extends JPanel {
   private FormLayout layout;

   public SkillTreeColumnPanel() {
      this.setMinimumSize(new Dimension(120, -1));
      this.layout = new FormLayout(
         new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")},
         new RowSpec[]{FormFactory.UNRELATED_GAP_ROWSPEC}
      );
      this.setLayout(this.layout);
      this.setOpaque(false);
   }

   public void addPerkItem(PerkItemPanel perkItemPanel) {
      this.layout.appendRow(RowSpec.decode("default:grow"));
      this.add(perkItemPanel, "2," + this.layout.getRowCount() + ", center, center");
      perkItemPanel.setPreferredSize(new Dimension(100, 100));
      this.validate();
   }
}
