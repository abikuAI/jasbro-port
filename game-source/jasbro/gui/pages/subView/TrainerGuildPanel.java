package jasbro.gui.pages.subView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.quests.Quest;
import jasbro.game.world.market.QuestManager;
import jasbro.gui.objects.div.QuestPanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class TrainerGuildPanel extends JPanel {
   private JTabbedPane tabbedPane;
   private JPanel questPanel;
   private JPanel activeQuestsPanel;

   public TrainerGuildPanel() {
      this.setOpaque(false);
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(8)"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("pref:grow"), RowSpec.decode("pref:grow(8)"), RowSpec.decode("pref:grow")}
         )
      );
      UIManager.put("TabbedPane.contentOpaque", false);
      this.tabbedPane = new JTabbedPane(1);
      this.add(this.tabbedPane, "2, 2, fill, fill");
      JPanel panel = new TranslucentPanel();
      this.tabbedPane.addTab("Take Quest", null, panel, null);
      panel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("20dlu"), RowSpec.decode("default:grow")})
      );
      JLabel lblTakeQuest = new JLabel("Take quest");
      lblTakeQuest.setFont(lblTakeQuest.getFont().deriveFont(15.0F));
      panel.add(lblTakeQuest, "1, 1");
      this.questPanel = new JPanel();
      this.questPanel.setOpaque(false);
      panel.add(this.questPanel, "1, 2, fill, fill");
      FormLayout layout = new FormLayout(
         new ColumnSpec[]{
            ColumnSpec.decode("center:min:grow"),
            ColumnSpec.decode("center:min:grow"),
            ColumnSpec.decode("center:min:grow"),
            ColumnSpec.decode("center:min:grow")
         },
         new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}
      );
      layout.setColumnGroups(new int[][]{{1, 2, 3, 4}});
      this.questPanel.setLayout(layout);
      panel = new TranslucentPanel();
      this.tabbedPane.addTab("Active quests", null, panel, null);
      panel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("20dlu"), RowSpec.decode("default:grow")})
      );
      lblTakeQuest = new JLabel("Active quests");
      lblTakeQuest.setFont(lblTakeQuest.getFont().deriveFont(15.0F));
      panel.add(lblTakeQuest, "1, 1");
      JScrollPane scrollPane = new JScrollPane();
      panel.add(scrollPane, "1, 2, fill, fill");
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      this.activeQuestsPanel = new JPanel();
      this.activeQuestsPanel.setOpaque(false);
      scrollPane.setViewportView(this.activeQuestsPanel);
      FormLayout layoutx = new FormLayout(
         new ColumnSpec[]{
            ColumnSpec.decode("center:min:grow"),
            ColumnSpec.decode("center:min:grow"),
            ColumnSpec.decode("center:min:grow"),
            ColumnSpec.decode("center:min:grow")
         },
         new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}
      );
      layoutx.setColumnGroups(new int[][]{{1, 2, 3, 4}});
      this.activeQuestsPanel.setLayout(layoutx);
      this.initQuestPanel();
      this.initActiveQuestsPanel();
   }

   public void initQuestPanel() {
      this.questPanel.removeAll();
      final QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
      FormLayout layout = (FormLayout)this.questPanel.getLayout();
      List<Quest> quests = questManager.getPossibleQuests();

      for (int i = 0; i < quests.size(); i++) {
         final Quest quest = quests.get(i);
         QuestPanel panel = new QuestPanel();
         panel.init(quest);
         if (i % 4 == 0) {
            layout.appendRow(FormFactory.DEFAULT_ROWSPEC);
         }

         this.questPanel.add(panel, i % 4 + 1 + ", " + (i / 4 + 1) + ", fill, fill");
         JButton questButton = new JButton(TextUtil.t("quest.accept"));
         panel.add(questButton, "1, 3, fill, fill");
         questButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               questManager.activateQuest(quest);
               TrainerGuildPanel.this.initQuestPanel();
               TrainerGuildPanel.this.initActiveQuestsPanel();
            }
         });
      }

      this.validate();
      this.repaint();
   }

   public void initActiveQuestsPanel() {
      this.activeQuestsPanel.removeAll();
      QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
      FormLayout layout = (FormLayout)this.activeQuestsPanel.getLayout();
      List<Quest> quests = questManager.getActiveQuests();

      for (Quest quest : questManager.getInactiveQuests()) {
         if (quest.showInQuestLog()) {
            quests.add(quest);
         }
      }

      for (int i = 0; i < quests.size(); i++) {
         final Quest quest = quests.get(i);
         QuestPanel panel = new QuestPanel();
         panel.init(quest);
         if (i % 4 == 0) {
            layout.appendRow(FormFactory.DEFAULT_ROWSPEC);
         }

         this.activeQuestsPanel.add(panel, i % 4 + 1 + ", " + (i / 4 + 1) + ", fill, fill");
         if (quest.canFinishEarly()) {
            JButton finishQuestButton = new JButton(TextUtil.t("quest.finish"));
            panel.add(finishQuestButton, "1, 3, fill, fill");
            finishQuestButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  quest.finish();
                  TrainerGuildPanel.this.initQuestPanel();
                  TrainerGuildPanel.this.initActiveQuestsPanel();
               }
            });
         }
      }

      this.validate();
      this.repaint();
   }
}
