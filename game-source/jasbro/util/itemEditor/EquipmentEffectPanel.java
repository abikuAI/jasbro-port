package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.gui.DelegateMouseListener;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class EquipmentEffectPanel extends JPanel {
   protected boolean selected = false;
   protected EquipmentEffect itemEffect;
   protected JPanel contentPanel;
   protected JPanel dataPanel;

   public EquipmentEffectPanel() {
      this.setBackground(Color.DARK_GRAY);
      this.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_COLSPEC},
            new RowSpec[]{FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("default:none"), RowSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_ROWSPEC}
         )
      );
      this.contentPanel = new JPanel();
      this.contentPanel.addMouseListener(new DelegateMouseListener());
      this.add(this.contentPanel, "2, 3, fill, fill");
      this.contentPanel.setLayout(new BoxLayout(this.contentPanel, 1));
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
      if (selected) {
         this.setBackground(Color.BLUE);
      } else {
         this.setBackground(Color.DARK_GRAY);
      }

      this.repaint();
   }

   public EquipmentEffect getItemEffect() {
      return this.itemEffect;
   }

   public void setItemEffect(EquipmentEffect usableItemEffect) {
      this.itemEffect = usableItemEffect;

      try {
         Class<? extends JPanel> panelClass = this.getItemEffect().getType().getItemEffectPanelClass();
         if (panelClass != null) {
            this.dataPanel = panelClass.getConstructor(EquipmentEffect.class).newInstance(this.getItemEffect());
         } else {
            this.dataPanel = new JPanel();
            this.dataPanel.add(new JLabel(usableItemEffect.getName()));
         }

         this.add(this.dataPanel, "2, 2, fill, fill");
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      this.add(this.dataPanel, "2, 2, fill, fill");
   }

   public void addPanel(EquipmentEffectPanel usableItemEffectPanel) {
      this.contentPanel.add(usableItemEffectPanel);
   }

   public void removePanel(EquipmentEffectPanel usableItemEffectPanel) {
      this.contentPanel.remove(usableItemEffectPanel);
   }

   public boolean hasChildEffects() {
      return this.contentPanel.getComponents().length > 0;
   }

   public JPanel getDataPanel() {
      return this.dataPanel;
   }
}
