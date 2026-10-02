package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.quests.Quest;
import jasbro.gui.GuiUtil;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class QuestPanel extends TranslucentPanel {
   public QuestPanel() {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}
         )
      );
      this.setBorder(GuiUtil.DEFAULTEMPTYBORDER);
      this.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      this.setOpaque(false);
      this.setPreferredSize(null);
      this.getPreferredSize().width = 0;
   }

   public void init(Quest quest) {
      this.removeAll();
      JLabel lblNewLabel = new JLabel(quest.getTitle());
      lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
      this.add(lblNewLabel, "1, 1, fill, fill");
      JTextArea textArea = GuiUtil.getDefaultTextarea();
      textArea.setText(quest.getDescription());
      this.add(textArea, "1, 2, fill, fill");
   }
}
