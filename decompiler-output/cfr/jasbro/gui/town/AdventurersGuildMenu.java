/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.town;

import jasbro.Jasbro;
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

public class AdventurersGuildMenu
extends MyImage {
    public AdventurersGuildMenu() {
        this.setBackgroundImage(new ImageData("images/backgrounds/adventurers guild.jpg"));
        this.setOpaque(false);
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
        int shunBtnWidth = 150 * width / 1280;
        int shunBtnHeight = 300 * width / 1280;
        int siegBtnWidth = 65 * width / 1280;
        int siegBtnHeight = 130 * width / 1280;
        int aBtnHeight = 200 * width / 1280;
        int aBtnWidth = 150 * width / 1280;
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
        backButton.setBounds(15 * width / 1280, 620 * height / 720, backHomeBtnWidth, backHomeBtnHeight);
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
        ImageIcon magicIcon1 = new ImageIcon("images/buttons/laboratory.png");
        Image magicImage1 = magicIcon1.getImage().getScaledInstance(iconSize, iconSize, 4);
        magicIcon1 = new ImageIcon(magicImage1);
        ImageIcon magicIcon2 = new ImageIcon("images/buttons/laboratory hover.png");
        Image magicImage2 = magicIcon2.getImage().getScaledInstance(iconSize, iconSize, 4);
        magicIcon2 = new ImageIcon(magicImage2);
        JButton laboratory = new JButton(magicIcon1);
        laboratory.setRolloverIcon(magicIcon2);
        laboratory.setPressedIcon(magicIcon1);
        laboratory.setBounds(105 * width / 1280, 300 * height / 720, iconSize, iconSize);
        laboratory.setBorderPainted(false);
        laboratory.setContentAreaFilled(false);
        laboratory.setFocusPainted(false);
        laboratory.setOpaque(false);
        laboratory.setToolTipText("Laboratory");
        this.add(laboratory);
        laboratory.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showLaboratory();
            }
        });
        ImageIcon shunIcon1 = new ImageIcon("images/people/adventurers/Shun Full.png");
        Image shunImage1 = shunIcon1.getImage().getScaledInstance(shunBtnWidth, shunBtnHeight, 4);
        shunIcon1 = new ImageIcon(shunImage1);
        ImageIcon shunIcon2 = new ImageIcon("images/people/adventurers/Shun Full Hover.png");
        Image shunImage2 = shunIcon2.getImage().getScaledInstance(shunBtnWidth, shunBtnHeight, 4);
        shunIcon2 = new ImageIcon(shunImage2);
        JButton shunButton = new JButton(shunIcon1);
        shunButton.setRolloverIcon(shunIcon2);
        shunButton.setPressedIcon(shunIcon1);
        shunButton.setBounds(840 * width / 1280, 230 * height / 720, shunBtnWidth, shunBtnHeight);
        shunButton.setBorderPainted(false);
        shunButton.setContentAreaFilled(false);
        shunButton.setFocusPainted(false);
        shunButton.setOpaque(false);
        shunButton.setToolTipText("Shun");
        this.add(shunButton);
        shunButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
            }
        });
        ImageIcon siegIcon1 = new ImageIcon("images/people/adventurers/Sieg Full.png");
        Image siegImage1 = siegIcon1.getImage().getScaledInstance(siegBtnWidth, siegBtnHeight, 4);
        siegIcon1 = new ImageIcon(siegImage1);
        ImageIcon siegIcon2 = new ImageIcon("images/people/adventurers/Sieg Full Hover.png");
        Image siegImage2 = siegIcon2.getImage().getScaledInstance(siegBtnWidth, siegBtnHeight, 4);
        siegIcon2 = new ImageIcon(siegImage2);
        JButton siegButton = new JButton(siegIcon1);
        siegButton.setRolloverIcon(siegIcon2);
        siegButton.setPressedIcon(siegIcon1);
        siegButton.setBounds(550 * width / 1280, 259 * height / 720, siegBtnWidth, siegBtnHeight);
        siegButton.setBorderPainted(false);
        siegButton.setContentAreaFilled(false);
        siegButton.setFocusPainted(false);
        siegButton.setOpaque(false);
        siegButton.setToolTipText("Sieg");
        this.add(siegButton);
        siegButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
            }
        });
        ImageIcon aIcon1 = new ImageIcon("images/people/adventurers/Unnamed Full.png");
        Image aImage1 = aIcon1.getImage().getScaledInstance(aBtnWidth, aBtnHeight, 4);
        aIcon1 = new ImageIcon(aImage1);
        ImageIcon aIcon2 = new ImageIcon("images/people/adventurers/Unnamed Full Hover.png");
        Image aImage2 = aIcon2.getImage().getScaledInstance(aBtnWidth, aBtnHeight, 4);
        aIcon2 = new ImageIcon(aImage2);
        JButton aButton = new JButton(aIcon1);
        aButton.setRolloverIcon(aIcon2);
        aButton.setPressedIcon(aIcon1);
        aButton.setBounds(220 * width / 1280, 300 * height / 720, aBtnWidth, aBtnHeight);
        aButton.setBorderPainted(false);
        aButton.setContentAreaFilled(false);
        aButton.setFocusPainted(false);
        aButton.setOpaque(false);
        aButton.setToolTipText("Sieg");
        this.add(aButton);
        aButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
            }
        });
    }
}

