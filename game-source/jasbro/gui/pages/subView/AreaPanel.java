package jasbro.gui.pages.subView;

import jasbro.game.housing.House;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.objects.div.MyImage;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AreaPanel extends MyImage {
   private AreaInterface area;
   private JPanel locationPanel;

   public AreaPanel() {
      this.setLayout(new GridLayout(0, 1, 5, 5));
      this.locationPanel = new JPanel();
      this.locationPanel.setOpaque(false);
      this.add(this.locationPanel);
   }

   private void init() {
      this.locationPanel.removeAll();
      this.setBackgroundImage(this.area.getImage());
      if (this.area != null) {
         if (this.area.getLocationAmount() < 7) {
            this.locationPanel.setLayout(new GridLayout(2, 3));
         } else if (this.area.getLocationAmount() < 9) {
            this.locationPanel.setLayout(new GridLayout(3, 3));
         } else {
            this.locationPanel.setLayout(new GridLayout(0, 4));
         }

         GridLayout layout = (GridLayout)this.locationPanel.getLayout();
         if (!(this.area instanceof House)) {
            layout.setHgap(0);
            layout.setVgap(0);
            this.setBorder(null);
         } else {
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            layout.setHgap(10);
            layout.setVgap(10);
         }

         for (CharacterLocation location : this.area.getLocations()) {
            this.locationPanel.add(new LocationPanel(location));
         }

         if (this.area.getLocationAmount() < 3) {
            this.locationPanel.add(Box.createGlue());
            this.locationPanel.add(Box.createGlue());
         }
      }
   }

   public AreaInterface getArea() {
      return this.area;
   }

   public void setArea(AreaInterface area) {
      this.area = area;
      this.init();
   }
}
