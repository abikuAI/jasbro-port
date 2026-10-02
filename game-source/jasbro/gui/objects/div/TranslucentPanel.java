package jasbro.gui.objects.div;

import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

public class TranslucentPanel extends MyPanel {
   public TranslucentPanel() {
      this.setBorder(GuiUtil.DEFAULTBORDER);
      this.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      this.setPreferredSize(new Dimension(1, 1));
   }

   @Override
   public boolean isOpaque() {
      return false;
   }

   @Override
   protected void paintComponent(Graphics g) {
      g.setColor(this.getBackground());
      Insets insets = this.getInsets();
      g.fillRect(insets.left, insets.top, this.getWidth() - insets.right, this.getHeight() - insets.bottom);
      super.paintComponent(g);
   }
}
