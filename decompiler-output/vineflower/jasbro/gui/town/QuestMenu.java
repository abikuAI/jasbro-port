package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.quests.Quest;
import jasbro.game.world.market.QuestManager;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.QuestPanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class QuestMenu extends MyImage {
   private JTabbedPane tabbedPane;
   private JPanel questPanel;
   private JPanel activeQuestsPanel;

   public QuestMenu() {
      this.setBackgroundImage(this.getTownImage());
      this.setOpaque(false);
      double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
      double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
      int widthRat = (int)(width / 1280.0);
      int heightRat = (int)(height / 720.0);
      int iconSize = (int)(65.0 * width / 1280.0);
      ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
      Image homeImage1 = homeIcon1.getImage().getScaledInstance(230, 75, 4);
      homeIcon1 = new ImageIcon(homeImage1);
      ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
      Image homeImage2 = homeIcon2.getImage().getScaledInstance(230, 75, 4);
      homeIcon2 = new ImageIcon(homeImage2);
      JButton homeButton = new JButton(homeIcon1);
      homeButton.setRolloverIcon(homeIcon2);
      homeButton.setPressedIcon(homeIcon1);
      homeButton.setBounds((int)(15.0 * width / 1280.0), (int)(550.0 * height / 720.0), 230, 75);
      homeButton.setBorderPainted(false);
      homeButton.setContentAreaFilled(false);
      homeButton.setFocusPainted(false);
      homeButton.setOpaque(false);
      this.add(homeButton);
      homeButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showHouseManagementScreen();
         }
      });
      ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
      Image backImage1 = backIcon1.getImage().getScaledInstance(230, 75, 4);
      backIcon1 = new ImageIcon(backImage1);
      ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
      Image backImage2 = backIcon2.getImage().getScaledInstance(230, 75, 4);
      backIcon2 = new ImageIcon(backImage2);
      JButton backButton = new JButton(backIcon1);
      backButton.setRolloverIcon(backIcon2);
      backButton.setPressedIcon(backIcon1);
      backButton.setBounds((int)(15.0 * width / 1280.0), (int)(620.0 * height / 720.0), 230, 75);
      backButton.setBorderPainted(false);
      backButton.setContentAreaFilled(false);
      backButton.setFocusPainted(false);
      backButton.setOpaque(false);
      this.add(backButton);
      backButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showSlaverGuildScreen();
         }
      });
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow(23)"), ColumnSpec.decode("default:grow(20)"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("pref:grow"), RowSpec.decode("pref:grow(8)"), RowSpec.decode("pref:grow")}
         )
      );
      MyImage teacherImage = new MyImage();
      teacherImage.setImage(new ImageData("images/people/secretary.png"));
      this.add(teacherImage, "1, 1, 1, 3, fill, fill");
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               Jasbro.getInstance().getGui().showSlaverGuildScreen();
            }
         }
      });
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
               QuestMenu.this.initQuestPanel();
               QuestMenu.this.initActiveQuestsPanel();
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
                  QuestMenu.this.initQuestPanel();
                  QuestMenu.this.initActiveQuestsPanel();
               }
            });
         }
      }

      this.validate();
      this.repaint();
   }

   public ImageData getTownImage() {
      switch (Jasbro.getInstance().getData().getTime()) {
         case AFTERNOON:
            return new ImageData("images/backgrounds/slaverguild afternoon.jpg");
         case NIGHT:
            return new ImageData("images/backgrounds/hiretrainer morning.jpg");
         default:
            return new ImageData("images/backgrounds/hiretrainer morning.jpg");
      }
   }
}
