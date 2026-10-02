package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import javax.swing.JPanel;

public class EventPanel extends JPanel {
   private JPanel eventEditorPanel;

   public EventPanel() {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("120dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("default:grow")}
         )
      );
      EventListPanel eventListPanel = new EventListPanel(this);
      this.add(eventListPanel, "1, 1, fill, fill");
      this.eventEditorPanel = new JPanel();
      this.add(this.eventEditorPanel, "3, 1, fill, fill");
   }

   public void setEvent(WorldEvent event) {
      if (this.eventEditorPanel != null) {
         this.remove(this.eventEditorPanel);
      }

      this.eventEditorPanel = new EventEditorPanel(event);
      this.add(this.eventEditorPanel, "3, 1, fill, fill");
      this.validate();
      this.repaint();
   }
}
