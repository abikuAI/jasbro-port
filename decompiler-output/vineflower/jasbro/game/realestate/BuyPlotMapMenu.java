package jasbro.game.realestate;

import jasbro.Jasbro;
import jasbro.game.world.Time;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class BuyPlotMapMenu extends MyImage {
   int width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
   int height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
   int widthRat = this.width / 1280;
   int heightRat = this.height / 720;
   int iconSize = 65 * this.width / 1280;
   int backHomeBtnWidth = 150 * this.width / 1280;
   int backHomeBtnHeight = 50 * this.width / 1280;
   int arrowBtnWidth = 150 * this.width / 1280;
   int arrowBtnHeight = 50 * this.width / 1280;
   private ImageIcon plotIcon1 = new ImageIcon("images/buttons/landmarker.png");
   private ImageIcon plotIcon2 = new ImageIcon("images/buttons/landmarker hover.png");
   private ImageIcon leftIcon1 = new ImageIcon("images/buttons/arrowleft.png");
   private ImageIcon leftIcon2 = new ImageIcon("images/buttons/arrowleft hover.png");
   private ImageIcon rightIcon1 = new ImageIcon("images/buttons/arrowright.png");
   private ImageIcon rightIcon2 = new ImageIcon("images/buttons/arrowright hover.png");

   public BuyPlotMapMenu(String map) {
      this.removeAll();
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               Jasbro.getInstance().getGui().showRealEstate();
            }
         }
      });
      ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
      Image backImage1 = backIcon1.getImage().getScaledInstance(this.backHomeBtnWidth, this.backHomeBtnHeight, 4);
      backIcon1 = new ImageIcon(backImage1);
      ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
      Image backImage2 = backIcon2.getImage().getScaledInstance(this.backHomeBtnWidth, this.backHomeBtnHeight, 4);
      backIcon2 = new ImageIcon(backImage2);
      JButton backButton = new JButton(backIcon1);
      backButton.setRolloverIcon(backIcon2);
      backButton.setPressedIcon(backIcon1);
      backButton.setBounds(15 * this.width / 1280, 620 * this.height / 720, this.backHomeBtnWidth, this.backHomeBtnHeight);
      backButton.setBorderPainted(false);
      backButton.setContentAreaFilled(false);
      backButton.setFocusPainted(false);
      backButton.setOpaque(false);
      this.add(backButton);
      backButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showRealEstate();
         }
      });
      this.plotIcon1 = new ImageIcon("images/buttons/landmarker.png");
      Image plotImage1 = this.plotIcon1.getImage().getScaledInstance(this.iconSize, this.iconSize, 4);
      this.plotIcon1 = new ImageIcon(plotImage1);
      this.plotIcon2 = new ImageIcon("images/buttons/landmarker hover.png");
      Image plotImage2 = this.plotIcon2.getImage().getScaledInstance(this.iconSize, this.iconSize, 4);
      this.plotIcon2 = new ImageIcon(plotImage2);
      Image leftImage1 = this.leftIcon1.getImage().getScaledInstance(this.backHomeBtnHeight, this.backHomeBtnWidth, 4);
      this.leftIcon1 = new ImageIcon(leftImage1);
      Image leftImage2 = this.leftIcon2.getImage().getScaledInstance(this.backHomeBtnHeight, this.backHomeBtnWidth, 4);
      this.leftIcon2 = new ImageIcon(leftImage2);
      Image rightImage1 = this.rightIcon1.getImage().getScaledInstance(this.backHomeBtnHeight, this.backHomeBtnWidth, 4);
      this.rightIcon1 = new ImageIcon(rightImage1);
      Image rightImage2 = this.rightIcon2.getImage().getScaledInstance(this.backHomeBtnHeight, this.backHomeBtnWidth, 4);
      this.rightIcon2 = new ImageIcon(rightImage2);
      switch (map) {
         case "map2":
            this.map2();
            break;
         case "map3":
            this.map3();
            break;
         case "map4":
            this.map4();
            break;
         default:
            this.map1();
      }
   }

   public void map1() {
      this.setBackgroundImage(this.getTownImage("map1"));
      this.setBackground(Color.WHITE);
      this.setLayout(null);
      this.setVisible(true);
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         JButton plot1Button = new JButton(this.plotIcon1);
         plot1Button.setRolloverIcon(this.plotIcon2);
         plot1Button.setPressedIcon(this.plotIcon1);
         plot1Button.setBounds(580 * this.width / 1280, 320 * this.height / 720, this.iconSize, this.iconSize);
         plot1Button.setBorderPainted(false);
         plot1Button.setContentAreaFilled(false);
         plot1Button.setFocusPainted(false);
         plot1Button.setOpaque(false);
         plot1Button.setToolTipText("Plot 1");
         this.add(plot1Button);
         plot1Button.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  if (JOptionPane.showConfirmDialog(
                        Jasbro.getInstance().getGui(), TextUtil.t("Do you really want to buy this plot?"), TextUtil.t("ui.confirmResetPerks.title"), 2
                     )
                     == 0) {
                     Jasbro.getInstance().getData().spendMoney(100000L, "");
                  }
               }
            }
         );
         JButton plot2Button = new JButton(this.plotIcon1);
         plot2Button.setRolloverIcon(this.plotIcon2);
         plot2Button.setPressedIcon(this.plotIcon1);
         plot2Button.setBounds(50 * this.width / 1280, 80 * this.height / 720, this.iconSize, this.iconSize);
         plot2Button.setBorderPainted(false);
         plot2Button.setContentAreaFilled(false);
         plot2Button.setFocusPainted(false);
         plot2Button.setOpaque(false);
         plot2Button.setToolTipText("Plot 2");
         this.add(plot2Button);
         plot2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot3Button = new JButton(this.plotIcon1);
         plot3Button.setRolloverIcon(this.plotIcon2);
         plot3Button.setPressedIcon(this.plotIcon1);
         plot3Button.setBounds(425 * this.width / 1280, 280 * this.height / 720, this.iconSize, this.iconSize);
         plot3Button.setBorderPainted(false);
         plot3Button.setContentAreaFilled(false);
         plot3Button.setFocusPainted(false);
         plot3Button.setOpaque(false);
         plot3Button.setToolTipText("Plot 3");
         this.add(plot3Button);
         plot3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot4Button = new JButton(this.plotIcon1);
         plot4Button.setRolloverIcon(this.plotIcon2);
         plot4Button.setPressedIcon(this.plotIcon1);
         plot4Button.setBounds(190 * this.width / 1280, 320 * this.height / 720, this.iconSize, this.iconSize);
         plot4Button.setBorderPainted(false);
         plot4Button.setContentAreaFilled(false);
         plot4Button.setFocusPainted(false);
         plot4Button.setOpaque(false);
         plot4Button.setToolTipText("Plot 4");
         this.add(plot4Button);
         plot4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton leftButton = new JButton(this.leftIcon1);
         leftButton.setRolloverIcon(this.leftIcon2);
         leftButton.setPressedIcon(this.leftIcon1);
         leftButton.setBounds(15 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         leftButton.setBorderPainted(false);
         leftButton.setContentAreaFilled(false);
         leftButton.setFocusPainted(false);
         leftButton.setOpaque(false);
         this.add(leftButton);
         leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map4");
            }
         });
         JButton rightButton = new JButton(this.rightIcon1);
         rightButton.setRolloverIcon(this.rightIcon2);
         rightButton.setPressedIcon(this.rightIcon1);
         rightButton.setBounds(1210 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         rightButton.setBorderPainted(false);
         rightButton.setContentAreaFilled(false);
         rightButton.setFocusPainted(false);
         rightButton.setOpaque(false);
         this.add(rightButton);
         rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map2");
            }
         });
      }

      this.validate();
      this.repaint();
   }

   public void map2() {
      this.setBackgroundImage(this.getTownImage("map2"));
      this.setBackground(Color.WHITE);
      this.setLayout(null);
      this.setVisible(true);
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         JButton plot1Button = new JButton(this.plotIcon1);
         plot1Button.setRolloverIcon(this.plotIcon2);
         plot1Button.setPressedIcon(this.plotIcon1);
         plot1Button.setBounds(860 * this.width / 1280, 505 * this.height / 720, this.iconSize, this.iconSize);
         plot1Button.setBorderPainted(false);
         plot1Button.setContentAreaFilled(false);
         plot1Button.setFocusPainted(false);
         plot1Button.setOpaque(false);
         plot1Button.setToolTipText("Plot 1");
         this.add(plot1Button);
         plot1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot2Button = new JButton(this.plotIcon1);
         plot2Button.setRolloverIcon(this.plotIcon2);
         plot2Button.setPressedIcon(this.plotIcon1);
         plot2Button.setBounds(475 * this.width / 1280, 15 * this.height / 720, this.iconSize, this.iconSize);
         plot2Button.setBorderPainted(false);
         plot2Button.setContentAreaFilled(false);
         plot2Button.setFocusPainted(false);
         plot2Button.setOpaque(false);
         plot2Button.setToolTipText("Plot 2");
         this.add(plot2Button);
         plot2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot3Button = new JButton(this.plotIcon1);
         plot3Button.setRolloverIcon(this.plotIcon2);
         plot3Button.setPressedIcon(this.plotIcon1);
         plot3Button.setBounds(595 * this.width / 1280, 220 * this.height / 720, this.iconSize, this.iconSize);
         plot3Button.setBorderPainted(false);
         plot3Button.setContentAreaFilled(false);
         plot3Button.setFocusPainted(false);
         plot3Button.setOpaque(false);
         plot3Button.setToolTipText("Plot 3");
         this.add(plot3Button);
         plot3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot4Button = new JButton(this.plotIcon1);
         plot4Button.setRolloverIcon(this.plotIcon2);
         plot4Button.setPressedIcon(this.plotIcon1);
         plot4Button.setBounds(800 * this.width / 1280, 320 * this.height / 720, this.iconSize, this.iconSize);
         plot4Button.setBorderPainted(false);
         plot4Button.setContentAreaFilled(false);
         plot4Button.setFocusPainted(false);
         plot4Button.setOpaque(false);
         plot4Button.setToolTipText("Plot 4");
         this.add(plot4Button);
         plot4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot5Button = new JButton(this.plotIcon1);
         plot5Button.setRolloverIcon(this.plotIcon2);
         plot5Button.setPressedIcon(this.plotIcon1);
         plot5Button.setBounds(1150 * this.width / 1280, 460 * this.height / 720, this.iconSize, this.iconSize);
         plot5Button.setBorderPainted(false);
         plot5Button.setContentAreaFilled(false);
         plot5Button.setFocusPainted(false);
         plot5Button.setOpaque(false);
         plot5Button.setToolTipText("Plot 5");
         this.add(plot5Button);
         plot5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton leftButton = new JButton(this.leftIcon1);
         leftButton.setRolloverIcon(this.leftIcon2);
         leftButton.setPressedIcon(this.leftIcon1);
         leftButton.setBounds(15 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         leftButton.setBorderPainted(false);
         leftButton.setContentAreaFilled(false);
         leftButton.setFocusPainted(false);
         leftButton.setOpaque(false);
         this.add(leftButton);
         leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map1");
            }
         });
         JButton rightButton = new JButton(this.rightIcon1);
         rightButton.setRolloverIcon(this.rightIcon2);
         rightButton.setPressedIcon(this.rightIcon1);
         rightButton.setBounds(1210 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         rightButton.setBorderPainted(false);
         rightButton.setContentAreaFilled(false);
         rightButton.setFocusPainted(false);
         rightButton.setOpaque(false);
         this.add(rightButton);
         rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map3");
            }
         });
      }

      this.validate();
      this.repaint();
   }

   public void map3() {
      this.setBackgroundImage(this.getTownImage("map3"));
      this.setBackground(Color.WHITE);
      this.setLayout(null);
      this.setVisible(true);
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         JButton plot1Button = new JButton(this.plotIcon1);
         plot1Button.setRolloverIcon(this.plotIcon2);
         plot1Button.setPressedIcon(this.plotIcon1);
         plot1Button.setBounds(600 * this.width / 1280, 520 * this.height / 720, this.iconSize, this.iconSize);
         plot1Button.setBorderPainted(false);
         plot1Button.setContentAreaFilled(false);
         plot1Button.setFocusPainted(false);
         plot1Button.setOpaque(false);
         plot1Button.setToolTipText("Plot 1");
         this.add(plot1Button);
         plot1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot2Button = new JButton(this.plotIcon1);
         plot2Button.setRolloverIcon(this.plotIcon2);
         plot2Button.setPressedIcon(this.plotIcon1);
         plot2Button.setBounds(630 * this.width / 1280, 35 * this.height / 720, this.iconSize, this.iconSize);
         plot2Button.setBorderPainted(false);
         plot2Button.setContentAreaFilled(false);
         plot2Button.setFocusPainted(false);
         plot2Button.setOpaque(false);
         plot2Button.setToolTipText("Plot 2");
         this.add(plot2Button);
         plot2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot3Button = new JButton(this.plotIcon1);
         plot3Button.setRolloverIcon(this.plotIcon2);
         plot3Button.setPressedIcon(this.plotIcon1);
         plot3Button.setBounds(970 * this.width / 1280, 125 * this.height / 720, this.iconSize, this.iconSize);
         plot3Button.setBorderPainted(false);
         plot3Button.setContentAreaFilled(false);
         plot3Button.setFocusPainted(false);
         plot3Button.setOpaque(false);
         plot3Button.setToolTipText("Plot 3");
         this.add(plot3Button);
         plot3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot4Button = new JButton(this.plotIcon1);
         plot4Button.setRolloverIcon(this.plotIcon2);
         plot4Button.setPressedIcon(this.plotIcon1);
         plot4Button.setBounds(100 * this.width / 1280, 320 * this.height / 720, this.iconSize, this.iconSize);
         plot4Button.setBorderPainted(false);
         plot4Button.setContentAreaFilled(false);
         plot4Button.setFocusPainted(false);
         plot4Button.setOpaque(false);
         plot4Button.setToolTipText("Plot 4");
         this.add(plot4Button);
         plot4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton leftButton = new JButton(this.leftIcon1);
         leftButton.setRolloverIcon(this.leftIcon2);
         leftButton.setPressedIcon(this.leftIcon1);
         leftButton.setBounds(15 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         leftButton.setBorderPainted(false);
         leftButton.setContentAreaFilled(false);
         leftButton.setFocusPainted(false);
         leftButton.setOpaque(false);
         this.add(leftButton);
         leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map2");
            }
         });
         JButton rightButton = new JButton(this.rightIcon1);
         rightButton.setRolloverIcon(this.rightIcon2);
         rightButton.setPressedIcon(this.rightIcon1);
         rightButton.setBounds(1210 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         rightButton.setBorderPainted(false);
         rightButton.setContentAreaFilled(false);
         rightButton.setFocusPainted(false);
         rightButton.setOpaque(false);
         this.add(rightButton);
         rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map4");
            }
         });
      }

      this.validate();
      this.repaint();
   }

   public void map4() {
      this.setBackgroundImage(this.getTownImage("map4"));
      this.setBackground(Color.WHITE);
      this.setLayout(null);
      this.setVisible(true);
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         JButton plot1Button = new JButton(this.plotIcon1);
         plot1Button.setRolloverIcon(this.plotIcon2);
         plot1Button.setPressedIcon(this.plotIcon1);
         plot1Button.setBounds(550 * this.width / 1280, 585 * this.height / 720, this.iconSize, this.iconSize);
         plot1Button.setBorderPainted(false);
         plot1Button.setContentAreaFilled(false);
         plot1Button.setFocusPainted(false);
         plot1Button.setOpaque(false);
         plot1Button.setToolTipText("Plot 1");
         this.add(plot1Button);
         plot1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot2Button = new JButton(this.plotIcon1);
         plot2Button.setRolloverIcon(this.plotIcon2);
         plot2Button.setPressedIcon(this.plotIcon1);
         plot2Button.setBounds(150 * this.width / 1280, 385 * this.height / 720, this.iconSize, this.iconSize);
         plot2Button.setBorderPainted(false);
         plot2Button.setContentAreaFilled(false);
         plot2Button.setFocusPainted(false);
         plot2Button.setOpaque(false);
         plot2Button.setToolTipText("Plot 2");
         this.add(plot2Button);
         plot2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot3Button = new JButton(this.plotIcon1);
         plot3Button.setRolloverIcon(this.plotIcon2);
         plot3Button.setPressedIcon(this.plotIcon1);
         plot3Button.setBounds(670 * this.width / 1280, 75 * this.height / 720, this.iconSize, this.iconSize);
         plot3Button.setBorderPainted(false);
         plot3Button.setContentAreaFilled(false);
         plot3Button.setFocusPainted(false);
         plot3Button.setOpaque(false);
         plot3Button.setToolTipText("Plot 3");
         this.add(plot3Button);
         plot3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot4Button = new JButton(this.plotIcon1);
         plot4Button.setRolloverIcon(this.plotIcon2);
         plot4Button.setPressedIcon(this.plotIcon1);
         plot4Button.setBounds(685 * this.width / 1280, 323 * this.height / 720, this.iconSize, this.iconSize);
         plot4Button.setBorderPainted(false);
         plot4Button.setContentAreaFilled(false);
         plot4Button.setFocusPainted(false);
         plot4Button.setOpaque(false);
         plot4Button.setToolTipText("Plot 4");
         this.add(plot4Button);
         plot4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton plot5Button = new JButton(this.plotIcon1);
         plot5Button.setRolloverIcon(this.plotIcon2);
         plot5Button.setPressedIcon(this.plotIcon1);
         plot5Button.setBounds(190 * this.width / 1280, 170 * this.height / 720, this.iconSize, this.iconSize);
         plot5Button.setBorderPainted(false);
         plot5Button.setContentAreaFilled(false);
         plot5Button.setFocusPainted(false);
         plot5Button.setOpaque(false);
         plot5Button.setToolTipText("Plot 5");
         this.add(plot5Button);
         plot5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
         });
         JButton leftButton = new JButton(this.leftIcon1);
         leftButton.setRolloverIcon(this.leftIcon2);
         leftButton.setPressedIcon(this.leftIcon1);
         leftButton.setBounds(15 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         leftButton.setBorderPainted(false);
         leftButton.setContentAreaFilled(false);
         leftButton.setFocusPainted(false);
         leftButton.setOpaque(false);
         this.add(leftButton);
         leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map3");
            }
         });
         JButton rightButton = new JButton(this.rightIcon1);
         rightButton.setRolloverIcon(this.rightIcon2);
         rightButton.setPressedIcon(this.rightIcon1);
         rightButton.setBounds(1210 * this.width / 1280, 250 * this.height / 720, this.backHomeBtnHeight, this.backHomeBtnWidth);
         rightButton.setBorderPainted(false);
         rightButton.setContentAreaFilled(false);
         rightButton.setFocusPainted(false);
         rightButton.setOpaque(false);
         this.add(rightButton);
         rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showBuyPlotMapScreen("map1");
            }
         });
      }

      this.validate();
      this.repaint();
   }

   public ImageData getTownImage(String map) {
      switch (map) {
         case "map2":
            return new ImageData("images/backgrounds/map2.jpg");
         case "map3":
            return new ImageData("images/backgrounds/map3.jpg");
         case "map4":
            return new ImageData("images/backgrounds/map4.jpg");
         default:
            return new ImageData("images/backgrounds/town_morning.png");
      }
   }
}
