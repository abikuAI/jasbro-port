/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.town;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SlaverGuildMenu
extends MyImage {
    public SlaverGuildMenu() {
        this.setBackgroundImage(this.getTownImage());
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
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow(23)"), ColumnSpec.decode("default:grow(20)"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow")}));
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Jasbro.getInstance().getGui().showTownScreen();
                }
            }
        });
        MyImage teacherImage = new MyImage();
        teacherImage.setImage(new ImageData("images/people/secretary.png"));
        this.add((Component)teacherImage, "1, 1, 1, 3, fill, fill");
        JLabel schoolPanel = new JLabel();
        schoolPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
        schoolPanel.setBorder(GuiUtil.DEFAULTBORDER);
        this.add((Component)schoolPanel, "2, 2, fill, fill");
        JButton hireTrainerButton = new JButton("Hire Trainer");
        this.add((Component)hireTrainerButton, "2,3, left, center");
        hireTrainerButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showTrainerBar();
            }
        });
        JButton questButton = new JButton("Quest");
        this.add((Component)questButton, "2,3, right, center");
        questButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showQuestScreen();
            }
        });
    }

    public ImageData getTownImage() {
        switch (Jasbro.getInstance().getData().getTime()) {
            case AFTERNOON: {
                return new ImageData("images/backgrounds/slaverguild afternoon.jpg");
            }
            case NIGHT: {
                return new ImageData("images/backgrounds/slaverguild morning.jpg");
            }
        }
        return new ImageData("images/backgrounds/slaverguild morning.jpg");
    }
}

