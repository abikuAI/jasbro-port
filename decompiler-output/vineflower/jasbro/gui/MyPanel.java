package jasbro.gui;

import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
   public void update() {
   }

   public void addSingle(JComponent component) {
      this.setLayout(new GridLayout(1, 1));
      this.add(component);
      this.getPreferredSize().height = component.getPreferredSize().height;
   }
}
