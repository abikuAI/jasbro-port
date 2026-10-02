package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.game.world.customContent.EventAndQuestFileLoader;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class QuestEditorPanel extends JPanel {
   private CustomQuestTemplate customQuestTemplate;
   private QuestStagePanel selectedQuestStagePanel;
   private JPanel questStageMasterPanel;
   private JButton saveButton;

   public QuestEditorPanel(CustomQuestTemplate customQuestTemplateTmp) {
      this.customQuestTemplate = customQuestTemplateTmp;
      this.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("90dlu")}, new RowSpec[]{RowSpec.decode("default:grow")})
      );
      JScrollPane scrollPane = new JScrollPane();
      this.add(scrollPane, "1, 1, fill, fill");
      this.questStageMasterPanel = new JPanel();
      scrollPane.setViewportView(this.questStageMasterPanel);
      this.questStageMasterPanel.setLayout(new BoxLayout(this.questStageMasterPanel, 1));
      JPanel panel = new JPanel();
      this.add(panel, "2, 1, fill, fill");
      panel.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.UNRELATED_GAP_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow"),
               FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      this.saveButton = new JButton(TextUtil.t("eventEditor.save"));
      panel.add(this.saveButton, "1, 1");
      this.saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EventAndQuestFileLoader.getInstance().save(QuestEditorPanel.this.customQuestTemplate);
         }
      });
      JButton btnAddQuestStage = new JButton(TextUtil.t("eventEditor.addQuestStage"));
      panel.add(btnAddQuestStage, "1, 3");
      btnAddQuestStage.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            CustomQuestStage newQuestStage = new CustomQuestStage();
            QuestEditorPanel.this.customQuestTemplate.getQuestStages().add(newQuestStage);
            QuestEditorPanel.this.addQuestStagePanel(newQuestStage);
            QuestEditorPanel.this.validate();
            QuestEditorPanel.this.repaint();
         }
      });
      JButton btnDeleteQuestStage = new JButton(TextUtil.t("eventEditor.deleteQuestStage"));
      btnDeleteQuestStage.setForeground(Color.RED);
      panel.add(btnDeleteQuestStage, "1, 5, right, default");
      btnDeleteQuestStage.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (QuestEditorPanel.this.selectedQuestStagePanel != null) {
               QuestEditorPanel.this.questStageMasterPanel.remove(QuestEditorPanel.this.selectedQuestStagePanel);
               QuestEditorPanel.this.customQuestTemplate.getQuestStages().remove(QuestEditorPanel.this.selectedQuestStagePanel.getQuestStage());
               QuestEditorPanel.this.selectedQuestStagePanel = null;
               QuestEditorPanel.this.validate();
               QuestEditorPanel.this.repaint();
            }
         }
      });

      for (CustomQuestStage customQuestStage : this.customQuestTemplate.getQuestStages()) {
         this.addQuestStagePanel(customQuestStage);
      }

      this.questStageMasterPanel.addMouseListener(new QuestEditorPanel.MyMouseListener());
   }

   public void addQuestStagePanel(CustomQuestStage customQuestStage) {
      QuestStagePanel questStagePanel = new QuestStagePanel(customQuestStage, this.customQuestTemplate);
      this.questStageMasterPanel.add(questStagePanel);
   }

   public void setSelected(QuestStagePanel questStagePanel) {
      if (this.selectedQuestStagePanel != null) {
         this.selectedQuestStagePanel.setSelected(false);
      }

      this.selectedQuestStagePanel = questStagePanel;
      questStagePanel.setSelected(true);
      this.validate();
      this.repaint();
   }

   private class MyMouseListener extends MouseAdapter {
      private MyMouseListener() {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         if (e.getSource() instanceof QuestStagePanel) {
            QuestStagePanel newPanel = (QuestStagePanel)e.getSource();
            QuestEditorPanel.this.setSelected(newPanel);
         }
      }
   }
}
