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
import javax.swing.SwingUtilities;

public class BuildersGuildMenu
extends MyImage {
    public BuildersGuildMenu() {
        this.setBackgroundImage(this.getTownImage());
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
        if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
            ImageIcon architectIcon1 = new ImageIcon("images/buttons/architect.png");
            Image architectImage1 = architectIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            architectIcon1 = new ImageIcon(architectImage1);
            ImageIcon architectIcon2 = new ImageIcon("images/buttons/architect hover.png");
            Image architectImage2 = architectIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            architectIcon2 = new ImageIcon(architectImage2);
            JButton architect = new JButton(architectIcon1);
            architect.setRolloverIcon(architectIcon2);
            architect.setPressedIcon(architectIcon1);
            architect.setBounds((int)(495.0 * width / 1280.0), (int)(365.0 * height / 720.0), iconSize, iconSize);
            architect.setBorderPainted(false);
            architect.setContentAreaFilled(false);
            architect.setFocusPainted(false);
            architect.setOpaque(false);
            architect.setToolTipText("Architect");
            this.add(architect);
            architect.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showShopScreen("architect");
                }
            });
            ImageIcon realEstateIcon1 = new ImageIcon("images/buttons/real estate.png");
            Image realEstateImage1 = realEstateIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            realEstateIcon1 = new ImageIcon(realEstateImage1);
            ImageIcon realEstateIcon2 = new ImageIcon("images/buttons/real estate hover.png");
            Image realEstateImage2 = realEstateIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            realEstateIcon2 = new ImageIcon(realEstateImage2);
            JButton realEstate = new JButton(realEstateIcon1);
            realEstate.setRolloverIcon(realEstateIcon2);
            realEstate.setPressedIcon(realEstateIcon1);
            realEstate.setBounds((int)(820.0 * width / 1280.0), (int)(410.0 * height / 720.0), iconSize, iconSize);
            realEstate.setBorderPainted(false);
            realEstate.setContentAreaFilled(false);
            realEstate.setFocusPainted(false);
            realEstate.setOpaque(false);
            realEstate.setToolTipText("Realtor");
            this.add(realEstate);
            realEstate.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showRealEstate();
                }
            });
            ImageIcon interiorDecorationIcon1 = new ImageIcon("images/buttons/carpentry.png");
            Image interiorDecorationImage1 = interiorDecorationIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
            interiorDecorationIcon1 = new ImageIcon(interiorDecorationImage1);
            ImageIcon interiorDecorationIcon2 = new ImageIcon("images/buttons/carpentry hover.png");
            Image interiorDecorationImage2 = interiorDecorationIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
            interiorDecorationIcon2 = new ImageIcon(interiorDecorationImage2);
            JButton interiorDecoration = new JButton(interiorDecorationIcon1);
            interiorDecoration.setRolloverIcon(interiorDecorationIcon2);
            interiorDecoration.setPressedIcon(interiorDecorationIcon1);
            interiorDecoration.setBounds((int)(1140.0 * width / 1280.0), (int)(450.0 * height / 720.0), iconSize, iconSize);
            interiorDecoration.setBorderPainted(false);
            interiorDecoration.setContentAreaFilled(false);
            interiorDecoration.setFocusPainted(false);
            interiorDecoration.setOpaque(false);
            interiorDecoration.setToolTipText("Carpentry");
            this.add(interiorDecoration);
            interiorDecoration.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    Jasbro.getInstance().getGui().showInteriorDecoration();
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

    public ImageData getTownImage() {
        switch (Jasbro.getInstance().getData().getTime()) {
            case AFTERNOON: {
                return new ImageData("images/backgrounds/buildersGuild_afternoon.jpg");
            }
            case NIGHT: {
                return new ImageData("images/backgrounds/buildersGuild_night.jpg");
            }
        }
        return new ImageData("images/backgrounds/buildersGuild_morning.jpg");
    }
}

