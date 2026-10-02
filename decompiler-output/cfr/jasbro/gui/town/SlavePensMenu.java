/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.SlaveMarket;
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SlavePensMenu
extends MyImage {
    private JPanel hirePanel;
    private MyImage slaveImage;
    private Charakter selectedSlave = null;
    private JTextArea hintArea;

    public SlavePensMenu() {
        this.setOpaque(false);
        this.setBackgroundImage(new ImageData("images/backgrounds/slavePens.png"));
        double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
        double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
        int widthRat = (int)(width / 1280.0);
        int heightRat = (int)(height / 720.0);
        int iconSize = (int)(65.0 * width / 1280.0);
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
                Jasbro.getInstance().getGui().showSlaveMarketScreen();
            }
        });
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("140dlu"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(20)"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(18)"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(16)"), RowSpec.decode("default:grow(3)")}));
        TranslucentPanel translucentPanel = new TranslucentPanel();
        translucentPanel.setPreferredSize(new Dimension(0, 0));
        this.add((Component)translucentPanel, "3, 2, fill, fill");
        translucentPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow")}));
        JLabel lblBuyGirl = new JLabel(TextUtil.t("ui.buyslave.title"));
        lblBuyGirl.setFont(new Font("Tahoma", 1, 18));
        translucentPanel.add((Component)lblBuyGirl, "1, 1");
        this.hirePanel = new JPanel();
        this.hirePanel.setOpaque(false);
        translucentPanel.add((Component)this.hirePanel, "1, 2, fill, fill");
        this.slaveImage = new MyImage();
        this.add((Component)this.slaveImage, "5, 1, 1, 2");
        this.hintArea = GuiUtil.getDefaultTextarea();
        this.hintArea.setText(TextUtil.t("ui.buyslave.introduction"));
        this.hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
        TranslucentPanel controlPanel = new TranslucentPanel();
        this.add((Component)controlPanel, "5, 3, fill, fill");
        controlPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC}));
        controlPanel.add((Component)this.hintArea, "1, 1, fill, fill");
        JButton btnHire = new JButton("Buy");
        btnHire.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] arguments = new Object[]{SlavePensMenu.this.selectedSlave.getName()};
                int price = 500 + (int)SlavePensMenu.this.selectedSlave.calculateValue();
                if (price < 500) {
                    price = 500;
                }
                if (SlavePensMenu.this.selectedSlave != null && Jasbro.getInstance().getData().canAfford(price)) {
                    SlavePensMenu.this.selectedSlave.setOwnership(Ownership.OWNED);
                    Jasbro.getInstance().getData().getCharacters().add(SlavePensMenu.this.selectedSlave);
                    Jasbro.getInstance().getData().spendMoney(price, TextUtil.t("slavemarket.bought", arguments));
                    Jasbro.getInstance().getData().getSlaveMarket().getSlaves().remove(SlavePensMenu.this.selectedSlave);
                    new MessageScreen(TextUtil.t("ui.buyslave.bought", SlavePensMenu.this.selectedSlave), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, SlavePensMenu.this.selectedSlave), SlavePensMenu.this.selectedSlave.getBackground());
                    Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, SlavePensMenu.this.selectedSlave));
                    SlavePensMenu.this.selectedSlave = null;
                    SlavePensMenu.this.slaveImage.setImage(null);
                    SlavePensMenu.this.initHireList();
                } else {
                    new MessageScreen(TextUtil.t("ui.buyslave.cantafford", SlavePensMenu.this.selectedSlave), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, SlavePensMenu.this.selectedSlave), SlavePensMenu.this.selectedSlave.getBackground());
                }
            }
        });
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Jasbro.getInstance().getGui().showSlaveMarketScreen();
                }
            }
        });
        btnHire.setFont(GuiUtil.DEFAULTBOLDFONT);
        controlPanel.add((Component)btnHire, "1, 2, center, bottom");
        this.initHireList();
    }

    public void initHireList() {
        MouseAdapter ml = new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {
                this.mouseAction(e);
            }

            public void mouseAction(MouseEvent e) {
                if (!e.isConsumed()) {
                    e.consume();
                    CharacterShortView shortView = (CharacterShortView)e.getSource();
                    SlavePensMenu.this.selectedSlave = shortView.getCharacter();
                    List imageTags = SlavePensMenu.this.selectedSlave.getBaseTags();
                    imageTags.add(0, ImageTag.NAKED);
                    imageTags.add(1, ImageTag.CLEANED);
                    SlavePensMenu.this.slaveImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, SlavePensMenu.this.selectedSlave.getImages()));
                    if (SlavePensMenu.this.selectedSlave != null) {
                        String name = SlavePensMenu.this.selectedSlave.getName();
                        int price = 500 + (int)SlavePensMenu.this.selectedSlave.calculateValue();
                        if (price < 500) {
                            price = 500;
                        }
                        SlavePensMenu.this.hintArea.setText(TextUtil.t("ui.buyslave.costText", name, price));
                    }
                    SlavePensMenu.this.repaint();
                }
            }
        };
        this.hirePanel.removeAll();
        SlaveMarket slaveMarket = Jasbro.getInstance().getData().getSlaveMarket();
        for (Charakter character : slaveMarket.getSlaves()) {
            CharacterShortView shortView = new CharacterShortView(character, false);
            this.hirePanel.add(shortView);
            shortView.addMouseListener(ml);
        }
    }
}

