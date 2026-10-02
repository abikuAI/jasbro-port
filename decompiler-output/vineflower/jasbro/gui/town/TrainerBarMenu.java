package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class TrainerBarMenu extends MyImage {
   private JPanel hirePanel;
   private MyImage trainerImage;
   private Charakter selectedTrainer = null;
   private JTextArea hintArea;

   public TrainerBarMenu() {
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
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               Jasbro.getInstance().getGui().showSlaverGuildScreen();
            }
         }
      });
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow(21)"), ColumnSpec.decode("default:grow(20)"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(16)"), RowSpec.decode("default:grow(3)")}
         )
      );
      MyImage teacherImage = new MyImage();
      teacherImage.setImage(new ImageData("images/people/secretary.png"));
      this.add(teacherImage, "1, 1, 1, 3, fill, fill");
      JLabel backgroundLabel = new JLabel();
      backgroundLabel.setPreferredSize(new Dimension(0, 0));
      backgroundLabel.setOpaque(true);
      backgroundLabel.setBackground(new Color(0, 0, 0, 0));
      backgroundLabel.setBorder(LineBorder.createBlackLineBorder());
      this.add(backgroundLabel, "2, 2, fill, fill");
      TranslucentPanel translucentPanel = new TranslucentPanel();
      translucentPanel.setPreferredSize(new Dimension(0, 0));
      translucentPanel.setOpaque(true);
      translucentPanel.setBackground(new Color(0, 0, 0, 0));
      this.add(translucentPanel, "2, 2, fill, fill");
      translucentPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow")})
      );
      this.hirePanel = new JPanel();
      this.hirePanel.setOpaque(false);
      translucentPanel.add(this.hirePanel, "1, 2, fill, fill");
      this.hintArea = GuiUtil.getDefaultTextarea();
      this.hintArea.setText(TextUtil.t("ui.hiretrainer.hint"));
      this.hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
      TranslucentPanel controlPanel = new TranslucentPanel();
      this.add(controlPanel, "2, 3, fill, fill");
      controlPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC})
      );
      controlPanel.add(this.hintArea, "1, 1, fill, fill");
      JButton btnHire = new JButton("Hire");
      btnHire.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (TrainerBarMenu.this.selectedTrainer != null
                  && Jasbro.getInstance().getData().getTrainers().size()
                     < 1 + Jasbro.getInstance().getData().getProtagonist().getFinalValue(SpecializationAttribute.EXPERIENCE) / 10) {
                  TrainerBarMenu.this.selectedTrainer.setOwnership(Ownership.CONTRACT);
                  Jasbro.getInstance().getData().getCharacters().add(TrainerBarMenu.this.selectedTrainer);
                  Jasbro.getInstance().getData().getAuctionHouse().getTrainers().remove(TrainerBarMenu.this.selectedTrainer);
                  new MessageScreen(
                     TextUtil.t("ui.hiretrainer.hired", TrainerBarMenu.this.selectedTrainer),
                     ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, TrainerBarMenu.this.selectedTrainer),
                     TrainerBarMenu.this.selectedTrainer.getBackground()
                  );
                  Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, TrainerBarMenu.this.selectedTrainer));
                  TrainerBarMenu.this.selectedTrainer = null;
                  TrainerBarMenu.this.initHireList();
               }
            }
         }
      );
      btnHire.setFont(GuiUtil.DEFAULTBOLDFONT);
      controlPanel.add(btnHire, "1, 2, center, bottom");
      this.initHireList();
   }

   public void initHireList() {
      MouseListener ml = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            this.mouseAction(e);
         }

         public void mouseAction(MouseEvent e) {
            if (!e.isConsumed()) {
               e.consume();
               CharacterShortView shortView = (CharacterShortView)e.getSource();
               TrainerBarMenu.this.selectedTrainer = shortView.getCharacter();
               List<ImageTag> imageTags = TrainerBarMenu.this.selectedTrainer.getBaseTags();
               imageTags.add(0, ImageTag.NAKED);
               imageTags.add(1, ImageTag.CLEANED);
               if (TrainerBarMenu.this.selectedTrainer != null) {
                  String name = TrainerBarMenu.this.selectedTrainer.getName();
                  int price = 500 + (int)TrainerBarMenu.this.selectedTrainer.calculateValue();
                  if (price < 500) {
                     price = 500;
                  }

                  TrainerBarMenu.this.hintArea.setText(TextUtil.t("ui.hiretrainer.info", name, price));
               }

               TrainerBarMenu.this.repaint();
            }
         }
      };
      this.hirePanel.removeAll();
      AuctionHouse auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();

      for (Charakter character : auctionHouse.getTrainers()) {
         CharacterShortView shortView = new CharacterShortView(character, false);
         this.hirePanel.add(shortView);
         shortView.addMouseListener(ml);
      }
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
