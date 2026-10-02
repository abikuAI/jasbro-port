package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.market.CharacterSchool;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SchoolMenu extends MyImage {
   private CharacterSchool school = new CharacterSchool();
   private Charakter selectedCharacter;
   private List<CharacterSchool.Training> possibleTraining;
   private JPanel trainingPanel;
   private JScrollPane scrollPane;
   private JComboBox<Charakter> characterSelect;
   private JCheckBox chckbxShowUnavailable;

   public SchoolMenu() {
      this.setBackgroundImage(new ImageData("images/backgrounds/school.jpg"));
      this.setOpaque(false);
      double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
      double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
      int backHomeBtnWidth = (int)(150.0 * width / 1280.0);
      int backHomeBtnHeight = (int)(50.0 * width / 1280.0);
      ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
      Image homeImage1 = homeIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      homeIcon1 = new ImageIcon(homeImage1);
      ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
      Image homeImage2 = homeIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      homeIcon2 = new ImageIcon(homeImage2);
      JButton homeButton = new JButton(homeIcon1);
      homeButton.setRolloverIcon(homeIcon2);
      homeButton.setPressedIcon(homeIcon1);
      homeButton.setBounds((int)(15.0 * width / 1280.0), (int)(550.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
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
      Image backImage1 = backIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      backIcon1 = new ImageIcon(backImage1);
      ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
      Image backImage2 = backIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      backIcon2 = new ImageIcon(backImage2);
      JButton backButton = new JButton(backIcon1);
      backButton.setRolloverIcon(backIcon2);
      backButton.setPressedIcon(backIcon1);
      backButton.setBounds((int)(15.0 * width / 1280.0), (int)(620.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
      backButton.setBorderPainted(false);
      backButton.setContentAreaFilled(false);
      backButton.setFocusPainted(false);
      backButton.setOpaque(false);
      this.add(backButton);
      backButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showTownScreen();
         }
      });
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow(23)"), ColumnSpec.decode("default:grow(20)"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow")}
         )
      );
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               Jasbro.getInstance().getGui().showTownScreen();
            }
         }
      });
      MyImage teacherImage = new MyImage();
      teacherImage.setImage(new ImageData("images/people/realtor.png"));
      this.add(teacherImage, "1, 1, 1, 3, fill, fill");
      JPanel schoolPanel = new TranslucentPanel();
      this.add(schoolPanel, "2, 2, fill, fill");
      schoolPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      schoolPanel.setBorder(GuiUtil.DEFAULTBORDER);
      schoolPanel.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("5dlu"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("5dlu")},
            new RowSpec[]{
               RowSpec.decode("5dlu"),
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("5dlu"),
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("5dlu"),
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("5dlu"),
               RowSpec.decode("default:grow"),
               RowSpec.decode("5dlu")
            }
         )
      );
      this.characterSelect = new JComboBox<>();
      this.characterSelect.addKeyListener(new SchoolMenu.MyKeyListener());
      this.characterSelect.updateUI();
      this.characterSelect.addItem(null);

      for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
         this.characterSelect.addItem(character);
      }

      this.characterSelect.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
               Charakter charakter = (Charakter)SchoolMenu.this.characterSelect.getSelectedItem();
               SchoolMenu.this.selectedCharacter = charakter;
               SchoolMenu.this.updateTraining();
               SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     SchoolMenu.this.scrollPane.getVerticalScrollBar().setValue(0);
                  }
               });
            }
         }
      });
      JLabel lblNewLabel = new JLabel(TextUtil.t("school"));
      lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
      schoolPanel.add(lblNewLabel, "2, 2");
      schoolPanel.add(this.characterSelect, "2, 4");
      this.scrollPane = new JScrollPane();
      this.scrollPane.setOpaque(false);
      this.scrollPane.getViewport().setOpaque(false);
      this.chckbxShowUnavailable = new JCheckBox("Show unavailable");
      this.chckbxShowUnavailable.setOpaque(false);
      schoolPanel.add(this.chckbxShowUnavailable, "2, 6");
      this.chckbxShowUnavailable.setSelected(ConfigHandler.isShowUnavailableSchool());
      this.chckbxShowUnavailable.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            ConfigHandler.setShowUnavailableSchool(SchoolMenu.this.chckbxShowUnavailable.isSelected());
            SchoolMenu.this.updateTraining();
         }
      });
      schoolPanel.add(this.scrollPane, "2, 8, fill, fill");
      this.trainingPanel = new JPanel();
      this.trainingPanel.setOpaque(false);
      this.scrollPane.setViewportView(this.trainingPanel);
      FormLayout layout = new FormLayout(
         new ColumnSpec[]{ColumnSpec.decode("center:min:grow"), ColumnSpec.decode("center:min:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}
      );
      layout.setColumnGroups(new int[][]{{1, 2}});
      this.trainingPanel.setLayout(layout);
      this.validate();
   }

   public void updateTraining() {
      if (this.selectedCharacter == null) {
         this.possibleTraining = new ArrayList<>();
      } else if (ConfigHandler.isShowUnavailableSchool()) {
         this.possibleTraining = this.school.getTrainingOpportunities(this.selectedCharacter);
      } else {
         this.possibleTraining = this.school.getTrainingOpportunitiesHideUnavailable(this.selectedCharacter);
      }

      this.trainingPanel.removeAll();
      FormLayout layout = (FormLayout)this.trainingPanel.getLayout();

      for (int i = 0; i < this.possibleTraining.size(); i++) {
         CharacterSchool.Training training = this.possibleTraining.get(i);
         if (i % 2 == 0) {
            layout.insertRow(i / 2 + 1, RowSpec.decode("default:none"));
         }

         this.trainingPanel.add(new SchoolMenu.TrainingInfoPanel(training), i % 2 + 1 + ", " + (i / 2 + 1) + ", fill, fill");
      }

      this.revalidate();
      this.repaint();
   }

   private class MyKeyListener extends KeyAdapter {
      private MyKeyListener() {
      }

      @Override
      public void keyPressed(KeyEvent e) {
         int curIndex = SchoolMenu.this.characterSelect.getSelectedIndex();
         if (e.getKeyCode() == 40) {
            if (curIndex == -1) {
               curIndex = 0;
            }

            if (curIndex + 1 < SchoolMenu.this.characterSelect.getItemCount()) {
               SchoolMenu.this.characterSelect.hidePopup();
               SchoolMenu.this.characterSelect.setSelectedIndex(curIndex + 1);
            }

            e.consume();
         } else if (e.getKeyCode() == 38) {
            if (curIndex - 1 >= 0) {
               SchoolMenu.this.characterSelect.hidePopup();
               SchoolMenu.this.characterSelect.setSelectedIndex(curIndex - 1);
            }

            e.consume();
         }
      }
   }

   private class TrainingInfoPanel extends TranslucentPanel {
      private CharacterSchool.Training training;

      public TrainingInfoPanel(CharacterSchool.Training training) {
         this.training = training;
         this.setPreferredSize(null);
         this.getPreferredSize().width = 0;
         this.setBackground(new Color(1.0F, 1.0F, 0.9F, 0.8F));
         this.setBorder(GuiUtil.DEFAULTEMPTYBORDER);
         this.setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("default:grow")},
               new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), RowSpec.decode("default:none")}
            )
         );
         JTextArea nameField = GuiUtil.getDefaultTextarea();
         nameField.setText(training.getName());
         nameField.setFont(new Font("Tahoma", 1, 18));
         this.add(nameField, "1,1");
         JTextArea messageField = GuiUtil.getDefaultTextarea();
         messageField.setText(training.getDescription());
         this.add(messageField, "1,2");
         Object[] arguments = new Object[]{training.getPrice()};
         JButton buyButton = new JButton(TextUtil.t("school.pay", arguments));
         this.add(buyButton, "1,3, fill, bottom");
         if (!Jasbro.getInstance().getData().canAfford(training.getPrice()) || !training.fulfillsRequirements()) {
            buyButton.setEnabled(false);
         }

         buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               TrainingInfoPanel.this.training.apply();
               SchoolMenu.this.updateTraining();
            }
         });
      }
   }
}
