package jasbro.gui.perks;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.SkillTreeItem;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class SkillTreePanel extends JPanel {
   private FormLayout layout;
   private List<PerkItemPanel> itemPanels = new ArrayList<>();
   private Map<SkillTreeItem, PerkItemPanel> itemPanelMap = new HashMap<>();
   private Charakter character;

   public SkillTreePanel() {
      this.setOpaque(false);
   }

   public void initSkillTree(SkillTree skillTree, Charakter character) {
      this.removeAll();
      this.itemPanelMap.clear();
      this.itemPanels.clear();
      this.character = character;
      this.layout = new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{RowSpec.decode("default:grow")});
      this.setLayout(this.layout);
      List<SkillTreeItem> perksPerTreeLevel = new ArrayList<>();
      perksPerTreeLevel.add(skillTree.getFirstItem());
      int i = 0;

      do {
         if (i > 0) {
            this.layout.appendColumn(ColumnSpec.decode("default:grow"));
         }

         SkillTreeColumnPanel columnPanel = new SkillTreeColumnPanel();
         List<SkillTreeItem> nextPerkTreeLevel = new ArrayList<>();
         this.add(columnPanel, i + 1 + ", 1, fill, fill");

         for (SkillTreeItem item : perksPerTreeLevel) {
            PerkItemPanel perkItemPanel = new PerkItemPanel(item, character, skillTree);
            columnPanel.addPerkItem(perkItemPanel);
            this.itemPanels.add(perkItemPanel);
            this.itemPanelMap.put(item, perkItemPanel);

            for (SkillTreeItem nextItem : item.getNextItems()) {
               if (!nextPerkTreeLevel.contains(nextItem)) {
                  nextPerkTreeLevel.add(nextItem);
               }
            }
         }

         perksPerTreeLevel = nextPerkTreeLevel;
         i++;
      } while (perksPerTreeLevel.size() > 0);

      this.validate();
      this.repaint();
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (this.isShowing()) {
         Point location = this.getLocationOnScreen();

         for (PerkItemPanel itemPanel : this.itemPanels) {
            if (itemPanel.isShowing()) {
               SkillTreeItem skillTreeItem = itemPanel.getPerk();

               for (SkillTreeItem nextItem : skillTreeItem.getNextItems()) {
                  if (this.itemPanelMap.containsKey(nextItem)) {
                     PerkItemPanel targetItem = this.itemPanelMap.get(nextItem);
                     Point start = itemPanel.getLocationOnScreen();
                     start.translate(-location.x + itemPanel.getWidth() - 20, -location.y + itemPanel.getHeight() / 2);
                     Point end = targetItem.getLocationOnScreen();
                     end.translate(-location.x + 20, -location.y + targetItem.getHeight() / 2);
                     Graphics2D g2d = (Graphics2D)g.create();
                     if (!this.character.getTraits().contains(skillTreeItem.getPerk()) || this.character.getTraits().contains(targetItem.getPerk().getPerk())) {
                        g2d.setColor(Color.BLACK);
                     } else if (targetItem.canLearn()) {
                        g2d.setColor(Color.BLUE);
                     } else {
                        g2d.setColor(Color.RED);
                     }

                     if (start != null && end != null) {
                        double rotation = 0.0;
                        if (end != null) {
                           int x = start.x;
                           int y = start.y;
                           int deltaX = end.x - x;
                           int deltaY = end.y - y;
                           rotation = -Math.atan2(deltaX, deltaY);
                           rotation = Math.toDegrees(rotation) + 180.0;
                        }

                        g2d.setStroke(new BasicStroke(3.0F));
                        g2d.draw(new Float(start, end));
                     }

                     g2d.dispose();
                  }
               }
            }
         }
      }
   }
}
