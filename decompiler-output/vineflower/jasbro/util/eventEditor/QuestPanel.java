package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.CustomQuestTemplate;
import javax.swing.JPanel;

public class QuestPanel extends JPanel {
   private JPanel questEditorPanel;

   public QuestPanel() {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("120dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("default:grow")}
         )
      );
      QuestListPanel questListPanel = new QuestListPanel(this);
      this.add(questListPanel, "1, 1, fill, fill");
      this.questEditorPanel = new JPanel();
      this.add(this.questEditorPanel, "3, 1, fill, fill");
   }

   public void setQuest(CustomQuestTemplate quest) {
      if (this.questEditorPanel != null) {
         this.remove(this.questEditorPanel);
      }

      this.questEditorPanel = new QuestEditorPanel(quest);
      this.add(this.questEditorPanel, "3, 1, fill, fill");
      this.validate();
      this.repaint();
   }
}
