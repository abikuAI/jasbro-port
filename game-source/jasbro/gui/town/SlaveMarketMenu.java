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

public class SlaveMarketMenu extends MyImage {
   private JButton btnAuctionHouse;

   public SlaveMarketMenu() {
      this.setBackgroundImage(this.getTownImage());
      this.setBackground(Color.WHITE);
      this.setLayout(null);
      this.setVisible(true);
      int width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
      int height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
      int widthRat = width / 1280;
      int heightRat = height / 720;
      int iconSize = 65 * width / 1280;
      int backHomeBtnWidth = 150 * width / 1280;
      int backHomeBtnHeight = 50 * width / 1280;
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         ImageIcon auctionHouseIcon1 = new ImageIcon("images/buttons/auctionhouse.png");
         Image auctionHouseImage1 = auctionHouseIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
         auctionHouseIcon1 = new ImageIcon(auctionHouseImage1);
         ImageIcon auctionHouseIcon2 = new ImageIcon("images/buttons/auctionhouse hover.png");
         Image auctionHouseImage2 = auctionHouseIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
         auctionHouseIcon2 = new ImageIcon(auctionHouseImage2);
         JButton auctionHouseButton = new JButton(auctionHouseIcon1);
         auctionHouseButton.setRolloverIcon(auctionHouseIcon2);
         auctionHouseButton.setPressedIcon(auctionHouseIcon1);
         auctionHouseButton.setBounds(987 * width / 1280, 315 * height / 720, iconSize, iconSize);
         auctionHouseButton.setBorderPainted(false);
         auctionHouseButton.setContentAreaFilled(false);
         auctionHouseButton.setFocusPainted(false);
         auctionHouseButton.setOpaque(false);
         auctionHouseButton.setToolTipText("Auction House");
         this.add(auctionHouseButton);
         auctionHouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showAuctionHouse();
            }
         });
         ImageIcon slavePensIcon1 = new ImageIcon("images/buttons/slavepens.png");
         Image slavePensImage1 = slavePensIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
         slavePensIcon1 = new ImageIcon(slavePensImage1);
         ImageIcon slavePensIcon2 = new ImageIcon("images/buttons/slavepens hover.png");
         Image slavePensImage2 = slavePensIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
         slavePensIcon2 = new ImageIcon(slavePensImage2);
         JButton slavePensButton = new JButton(slavePensIcon1);
         slavePensButton.setRolloverIcon(slavePensIcon2);
         slavePensButton.setPressedIcon(slavePensIcon1);
         slavePensButton.setBounds(107 * width / 1280, 390 * height / 720, iconSize, iconSize);
         slavePensButton.setBorderPainted(false);
         slavePensButton.setContentAreaFilled(false);
         slavePensButton.setFocusPainted(false);
         slavePensButton.setOpaque(false);
         slavePensButton.setToolTipText("Slave Pens");
         this.add(slavePensButton);
         slavePensButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance().getGui().showSlavePens();
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
               Jasbro.getInstance().getGui().showTownScreen();
            }
         });
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (SwingUtilities.isRightMouseButton(e)) {
                  Jasbro.getInstance().getGui().showTownScreen();
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
            return new ImageData("images/backgrounds/slavemarket afternoon.jpg");
         case NIGHT:
            return new ImageData("images/backgrounds/slavemarket night.jpg");
         default:
            return new ImageData("images/backgrounds/slavemarket morning.jpg");
      }
   }
}
