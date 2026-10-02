/*
 * Decompiled with CFR 0.152.
 */
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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GeneralMarketMenu
extends MyImage {
    private JPanel menuPanel;
    private JPanel contenPanel;
    private JButton btnAuctionHouse;

    public GeneralMarketMenu() {
        this.setBackgroundImage(this.getMarket());
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.setVisible(true);
        double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
        double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
        int widthRat = (int)(width / 1280.0);
        int heightRat = (int)(height / 720.0);
        int iconSize = (int)(65.0 * width / 1280.0);
        int backHomeBtnWidth = (int)(150.0 * width / 1280.0);
        int backHomeBtnHeight = (int)(50.0 * width / 1280.0);
        int downBtnWidth = (int)(50.0 * width / 1280.0);
        int downBtnHeight = (int)(150.0 * width / 1280.0);
        if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
            ImageIcon generalStoreIcon1 = new ImageIcon("images/buttons/generalstore.png");
            Image generalStoreImage1 = generalStoreIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            generalStoreIcon1 = new ImageIcon(generalStoreImage1);
            ImageIcon generalStoreIcon2 = new ImageIcon("images/buttons/generalstore hover.png");
            Image generalStoreImage2 = generalStoreIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            generalStoreIcon2 = new ImageIcon(generalStoreImage2);
            JButton generalStore = new JButton(generalStoreIcon1);
            generalStore.setRolloverIcon(generalStoreIcon2);
            generalStore.setPressedIcon(generalStoreIcon1);
            generalStore.setBounds((int)(1025.0 * width / 1280.0), (int)(463.0 * height / 720.0), iconSize, iconSize);
            generalStore.setBorderPainted(false);
            generalStore.setContentAreaFilled(false);
            generalStore.setFocusPainted(false);
            generalStore.setOpaque(false);
            generalStore.setToolTipText("General Store");
            this.add(generalStore);
            generalStore.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showShopScreen("general");
                }
            });
            ImageIcon adultStoreIcon1 = new ImageIcon("images/buttons/adultstore.png");
            Image adultStoreImage1 = adultStoreIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            adultStoreIcon1 = new ImageIcon(adultStoreImage1);
            ImageIcon adultStoreIcon2 = new ImageIcon("images/buttons/adultstore hover.png");
            Image adultStoreImage2 = adultStoreIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            adultStoreIcon2 = new ImageIcon(adultStoreImage2);
            JButton adultStore = new JButton(adultStoreIcon1);
            adultStore.setRolloverIcon(adultStoreIcon2);
            adultStore.setPressedIcon(adultStoreIcon1);
            adultStore.setBounds((int)(810.0 * width / 1280.0), (int)(440.0 * height / 720.0), iconSize, iconSize);
            adultStore.setBorderPainted(false);
            adultStore.setContentAreaFilled(false);
            adultStore.setFocusPainted(false);
            adultStore.setOpaque(false);
            adultStore.setToolTipText("Adult Store");
            this.add(adultStore);
            adultStore.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showShopScreen("adult");
                }
            });
            ImageIcon magicStoreIcon1 = new ImageIcon("images/buttons/magicstore.png");
            Image magicStoreImage1 = magicStoreIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            magicStoreIcon1 = new ImageIcon(magicStoreImage1);
            ImageIcon magicStoreIcon2 = new ImageIcon("images/buttons/magicstore hover.png");
            Image magicStoreImage2 = magicStoreIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            magicStoreIcon2 = new ImageIcon(magicStoreImage2);
            JButton magicStore = new JButton(magicStoreIcon1);
            magicStore.setRolloverIcon(magicStoreIcon2);
            magicStore.setPressedIcon(magicStoreIcon1);
            magicStore.setBounds((int)(510.0 * width / 1280.0), (int)(450.0 * height / 720.0), iconSize, iconSize);
            magicStore.setBorderPainted(false);
            magicStore.setContentAreaFilled(false);
            magicStore.setFocusPainted(false);
            magicStore.setOpaque(false);
            magicStore.setToolTipText("Magic Shop");
            this.add(magicStore);
            magicStore.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showAlchemist();
                }
            });
            ImageIcon bookStoreIcon1 = new ImageIcon("images/buttons/bookstore.png");
            Image bookStoreImage1 = bookStoreIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            bookStoreIcon1 = new ImageIcon(bookStoreImage1);
            ImageIcon bookStoreIcon2 = new ImageIcon("images/buttons/bookstore hover.png");
            Image bookStoreImage2 = bookStoreIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            bookStoreIcon2 = new ImageIcon(bookStoreImage2);
            JButton bookStore = new JButton(bookStoreIcon1);
            bookStore.setRolloverIcon(bookStoreIcon2);
            bookStore.setPressedIcon(bookStoreIcon1);
            bookStore.setBounds((int)(206.0 * width / 1280.0), (int)(470.0 * height / 720.0), iconSize, iconSize);
            bookStore.setBorderPainted(false);
            bookStore.setContentAreaFilled(false);
            bookStore.setFocusPainted(false);
            bookStore.setOpaque(false);
            bookStore.setToolTipText("Book Store");
            this.add(bookStore);
            bookStore.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showShopScreen("book");
                }
            });
            ImageIcon turnIcon1 = new ImageIcon("images/buttons/arrowdown.png");
            Image turnImage1 = turnIcon1.getImage().getScaledInstance(downBtnHeight, downBtnWidth, 4);
            turnIcon1 = new ImageIcon(turnImage1);
            ImageIcon turnIcon2 = new ImageIcon("images/buttons/arrowdown hover.png");
            Image turnImage2 = turnIcon2.getImage().getScaledInstance(downBtnHeight, downBtnWidth, 4);
            turnIcon2 = new ImageIcon(turnImage2);
            JButton turnButton = new JButton(turnIcon1);
            turnButton.setRolloverIcon(turnIcon2);
            turnButton.setPressedIcon(turnIcon1);
            turnButton.setBounds((int)(550.0 * width / 1280.0), (int)(620.0 * height / 720.0), downBtnHeight, downBtnWidth);
            turnButton.setBorderPainted(false);
            turnButton.setContentAreaFilled(false);
            turnButton.setFocusPainted(false);
            turnButton.setOpaque(false);
            this.add(turnButton);
            turnButton.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showGeneralMarketScreen(2);
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
            homeButton.setBounds((int)(15.0 * width / 1280.0), (int)(550.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
            homeButton.setBorderPainted(false);
            homeButton.setContentAreaFilled(false);
            homeButton.setFocusPainted(false);
            homeButton.setOpaque(false);
            this.add(homeButton);
            homeButton.addActionListener(new ActionListener(){

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
            backButton.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showTownScreen();
                }
            });
            this.addMouseListener(new MouseAdapter(){

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

    public ImageData getMarket() {
        switch (Jasbro.getInstance().getData().getTime()) {
            case AFTERNOON: {
                return new ImageData("images/backgrounds/marketdistrict afternoon.jpg");
            }
            case NIGHT: {
                return new ImageData("images/backgrounds/marketdistrict night.jpg");
            }
        }
        return new ImageData("images/backgrounds/marketdistrict morning.jpg");
    }
}

