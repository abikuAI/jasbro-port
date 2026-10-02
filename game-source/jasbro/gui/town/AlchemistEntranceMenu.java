package jasbro.gui.town;

import jasbro.Jasbro;
import jasbro.game.world.Time;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
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
import javax.swing.SwingUtilities;

public class AlchemistEntranceMenu extends MyImage {
   public AlchemistEntranceMenu() {
      this.setBackgroundImage(new ImageData("images/backgrounds/alchemistentrance.jpg"));
      this.setBackground(Color.WHITE);
      this.setLayout(null);
      this.setVisible(true);
      int width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
      int height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
      int widthRat = width / 1280;
      int heightRat = height / 720;
      int iconSize = 80 * width / 1280;
      int backHomeBtnWidth = 150 * width / 1280;
      int backHomeBtnHeight = 50 * width / 1280;
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         ImageIcon alchemistIcon1 = new ImageIcon("images/buttons/alchemist.png");
         Image alchemistImage1 = alchemistIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
         alchemistIcon1 = new ImageIcon(alchemistImage1);
         ImageIcon alchemistIcon2 = new ImageIcon("images/buttons/alchemist hover.png");
         Image alchemistImage2 = alchemistIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
         alchemistIcon2 = new ImageIcon(alchemistImage2);
         JButton alchemist = new JButton(alchemistIcon1);
         alchemist.setRolloverIcon(alchemistIcon2);
         alchemist.setPressedIcon(alchemistIcon1);
         alchemist.setBounds(800 * width / 1280, 310 * height / 720, iconSize, iconSize);
         alchemist.setBorderPainted(false);
         alchemist.setContentAreaFilled(false);
         alchemist.setFocusPainted(false);
         alchemist.setOpaque(false);
         alchemist.setToolTipText("Alchemist");
         this.add(alchemist);
         alchemist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showShopScreen("alchemist");
            }
         });
         ImageIcon laboratoryIcon1 = new ImageIcon("images/buttons/laboratory.png");
         Image laboratoryImage1 = laboratoryIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
         laboratoryIcon1 = new ImageIcon(laboratoryImage1);
         ImageIcon laboratoryIcon2 = new ImageIcon("images/buttons/laboratory hover.png");
         Image laboratoryImage2 = laboratoryIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
         laboratoryIcon2 = new ImageIcon(laboratoryImage2);
         JButton laboratory = new JButton(laboratoryIcon1);
         laboratory.setRolloverIcon(laboratoryIcon2);
         laboratory.setPressedIcon(laboratoryIcon1);
         laboratory.setBounds(185 * width / 1280, 310 * height / 720, iconSize, iconSize);
         laboratory.setBorderPainted(false);
         laboratory.setContentAreaFilled(false);
         laboratory.setFocusPainted(false);
         laboratory.setOpaque(false);
         laboratory.setToolTipText("Laboratory");
         this.add(laboratory);
         laboratory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showLaboratory();
            }
         });
         ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
         Image homeImage1 = homeIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
         homeIcon1 = new ImageIcon(homeImage1);
         ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
         Image homeImage2 = homeIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
         homeIcon2 = new ImageIcon(homeImage2);
         JButton homeButton = new JButton(homeIcon1);
         homeButton.setRolloverIcon(homeIcon2);
         homeButton.setPressedIcon(homeIcon1);
         homeButton.setBounds(15 * width / 1280, 550 * height / 720, backHomeBtnWidth, backHomeBtnHeight);
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
         backButton.setBounds(15 * width / 1280, 620 * height / 720, backHomeBtnWidth, backHomeBtnHeight);
         backButton.setBorderPainted(false);
         backButton.setContentAreaFilled(false);
         backButton.setFocusPainted(false);
         backButton.setOpaque(false);
         this.add(backButton);
         backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
            }
         });
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (SwingUtilities.isRightMouseButton(e)) {
                  Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
               }
            }
         });
      }

      this.validate();
      this.repaint();
   }

   public ImageData getTownImage() {
      switch (Jasbro.getInstance().getData().getTime()) {
         case AFTERNOON:
            return new ImageData("images/backgrounds/town_afternoon.jpg");
         case NIGHT:
            return new ImageData("images/backgrounds/town_night.jpg");
         default:
            return new ImageData("images/backgrounds/town_morning.jpg");
      }
   }
}
