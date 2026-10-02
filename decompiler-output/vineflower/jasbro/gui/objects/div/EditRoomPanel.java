package jasbro.gui.objects.div;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EditRoomPanel extends TranslucentPanel implements MyEventListener {
   private Room room;

   public EditRoomPanel(Room room) {
      this.room = room;
      room.addListener(this);
      this.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      this.setBorder(GuiUtil.DEFAULTBORDER);
      this.setPreferredSize(null);
      this.getPreferredSize().width = 0;
      this.getMinimumSize().width = 0;
      this.init();
   }

   public void init() {
      this.removeAll();
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow(3)")},
            new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow")}
         )
      );
      JLabel label = new JLabel(TextUtil.t("type"));
      label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 4.0F));
      label.setOpaque(false);
      label.setFont(GuiUtil.DEFAULTBOLDFONT);
      this.add(label, "1, 1, fill, fill");
      label = new JLabel(this.room.getRoomInfo().getText());
      label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 4.0F));
      label.setOpaque(false);
      label.setFont(GuiUtil.DEFAULTBOLDFONT);
      this.add(label, "2, 1, fill, fill");
      label = new JLabel(TextUtil.t("ui.name"));
      label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 4.0F));
      label.setOpaque(false);
      label.setFont(GuiUtil.DEFAULTBOLDFONT);
      this.add(label, "1, 2, fill, fill");
      final JTextField textField = new JTextField(this.room.getInternName());
      textField.setMinimumSize(new Dimension(0, 0));
      this.add(textField, "2, 2, fill, fill");
      textField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            EditRoomPanel.this.room.setInternName((textField.getText() + e.getKeyChar()).trim());
         }
      });
   }

   @Override
   public void handleEvent(MyEvent e) {
   }
}
