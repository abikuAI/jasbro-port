package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MainMenu extends MyImage {
   private ImageData imageData = new ImageData("images/backgrounds/New Game Screen.jpg");
   private JButton continueButton;
   private JButton newGameButton;
   private JButton quitButton;

   public MainMenu() {
      this.setVisible(false);
      this.initComponents();
      this.setVisible(true);
      this.repaint();
      this.setBackgroundImage(this.imageData);
   }

   private void initComponents() {
      this.setLayout(null);
      double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
      double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
      int widthRat = (int)(width / 1280.0);
      int heightRat = (int)(height / 720.0);
      int iconSize = (int)(65.0 * width / 1280.0);
      int backHomeBtnWidth = (int)(340.0 * width / 1280.0);
      int backHomeBtnHeight = (int)(85.0 * width / 1280.0);
      ImageIcon newgameIcon1 = new ImageIcon("images/buttons/newgame.png");
      Image newgameImage1 = newgameIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      newgameIcon1 = new ImageIcon(newgameImage1);
      ImageIcon newgameIcon2 = new ImageIcon("images/buttons/newgame hover.png");
      Image newgameImage2 = newgameIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      newgameIcon2 = new ImageIcon(newgameImage2);
      this.newGameButton = new JButton(newgameIcon1);
      this.newGameButton.setRolloverIcon(newgameIcon2);
      this.newGameButton.setPressedIcon(newgameIcon1);
      this.newGameButton.setBounds((int)(470.0 * width / 1280.0), (int)(280.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
      this.newGameButton.setBorderPainted(false);
      this.newGameButton.setContentAreaFilled(false);
      this.newGameButton.setFocusPainted(false);
      this.newGameButton.setOpaque(false);
      this.add(this.newGameButton);
      ImageIcon continueIcon1 = new ImageIcon("images/buttons/continue.png");
      Image continueImage1 = continueIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      continueIcon1 = new ImageIcon(continueImage1);
      ImageIcon continueIcon2 = new ImageIcon("images/buttons/continue hover.png");
      Image continueImage2 = continueIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      continueIcon2 = new ImageIcon(continueImage2);
      this.continueButton = new JButton(continueIcon1);
      this.continueButton.setRolloverIcon(continueIcon2);
      this.continueButton.setPressedIcon(continueIcon1);
      this.continueButton.setBounds((int)(470.0 * width / 1280.0), (int)(400.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
      this.continueButton.setBorderPainted(false);
      this.continueButton.setContentAreaFilled(false);
      this.continueButton.setFocusPainted(false);
      this.continueButton.setOpaque(false);
      this.add(this.continueButton);
      ImageIcon quitIcon1 = new ImageIcon("images/buttons/quit.png");
      Image quitImage1 = quitIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      quitIcon1 = new ImageIcon(quitImage1);
      ImageIcon quitIcon2 = new ImageIcon("images/buttons/quit hover.png");
      Image quitImage2 = quitIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
      quitIcon2 = new ImageIcon(quitImage2);
      this.quitButton = new JButton(quitIcon1);
      this.quitButton.setRolloverIcon(quitIcon2);
      this.quitButton.setPressedIcon(quitIcon1);
      this.quitButton.setBounds((int)(470.0 * width / 1280.0), (int)(520.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
      this.quitButton.setBorderPainted(false);
      this.quitButton.setContentAreaFilled(false);
      this.quitButton.setFocusPainted(false);
      this.quitButton.setOpaque(false);
      this.add(this.quitButton);
      this.quitButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().dispose();
         }
      });
      this.continueButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (Jasbro.getInstance().getData() == null) {
               Jasbro.getInstance().continueLastGame();
            } else {
               Jasbro.getInstance().getGui().showHouseManagementScreen();
            }
         }
      });
      this.newGameButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            Jasbro.getInstance().getGui().showStartScreen();
         }
      });
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
   }
}
