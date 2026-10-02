/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.housing.House;
import jasbro.game.items.ItemLocation;
import jasbro.game.realestate.BuyPlotMapMenu;
import jasbro.game.world.market.Auction;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.CharacterFilterListModel;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.menus.MainMenuBar;
import jasbro.gui.pages.AuctionScreen;
import jasbro.gui.pages.CharacterScreen;
import jasbro.gui.pages.GameOverScreen;
import jasbro.gui.pages.HouseScreen;
import jasbro.gui.pages.MainMenu;
import jasbro.gui.pages.ManagementScreen;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.NewGameScreen;
import jasbro.gui.pages.QuestScreen;
import jasbro.gui.pages.TownMenu;
import jasbro.gui.pages.UnlockScreen;
import jasbro.gui.pages.subView.CheatScreen;
import jasbro.gui.town.AdventurerBarMenu;
import jasbro.gui.town.AdventurersGuildMenu;
import jasbro.gui.town.AlchemistEntranceMenu;
import jasbro.gui.town.AuctionHouseMenu;
import jasbro.gui.town.BuildersGuildMenu;
import jasbro.gui.town.GeneralMarketMenu;
import jasbro.gui.town.GeneralMarketMenuTwo;
import jasbro.gui.town.InteriorDecorationMenu;
import jasbro.gui.town.LaboratoryMenu;
import jasbro.gui.town.QuestMenu;
import jasbro.gui.town.RealEstateMenu;
import jasbro.gui.town.SchoolMenu;
import jasbro.gui.town.ShopMenu;
import jasbro.gui.town.SlaveMarketMenu;
import jasbro.gui.town.SlavePensMenu;
import jasbro.gui.town.SlaverGuildMenu;
import jasbro.gui.town.TownMenuNew;
import jasbro.gui.town.TrainerBarMenu;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;

public class RPGView
extends JFrame {
    private JPanel layerPane;
    private List<JComponent> layers = new ArrayList<JComponent>();
    private MainMenuBar menuBar;
    private boolean repaintScheduled = false;
    private List<MessageScreen> previousMessages = new ArrayList<MessageScreen>();
    private int resolutionHeight = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
    private int resolutionWidth = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
    private Screen actualScreen = Screen.NONE;
    private CharacterFilterListModel filteredModel = new CharacterFilterListModel();

    public RPGView() {
        super(TextUtil.t("version"));
        this.setExtendedState(this.getExtendedState() | 6);
        ToolTipManager.sharedInstance().setDismissDelay(20000);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        this.setDefaultCloseOperation(3);
        this.menuBar = new MainMenuBar();
        this.setJMenuBar(this.menuBar);
        this.setResizable(false);
        this.initComponents();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showStartScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.addLayer(new NewGameScreen());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showMainMenu() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.addLayer(new MainMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showHouseManagementScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.addLayer(new ManagementScreen());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addLayer(JComponent layer) {
        Object object = this.getTreeLock();
        synchronized (object) {
            if (layer instanceof MessageInterface) {
                ((MessageInterface)((Object)layer)).init();
            }
            if (this.layerPane.getComponents().length != 0) {
                this.layerPane.getComponents()[0].setVisible(false);
                this.layers.add((JComponent)this.layerPane.getComponents()[0]);
            }
            this.layerPane.removeAll();
            this.layerPane.add((Component)layer, "1, 1, fill, fill");
            layer.setVisible(true);
            this.layerPane.validate();
            this.repaint();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addLayerBottom(JPanel layer) {
        Object object = this.getTreeLock();
        synchronized (object) {
            if (this.layerPane.getComponents().length != 0) {
                this.layers.add(0, layer);
                layer.setVisible(false);
            } else if (layer != null) {
                this.addLayer(layer);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeLayer(JComponent component) {
        Object object = this.getTreeLock();
        synchronized (object) {
            if ((this.layers.contains(component) || this.layerPane.getComponents().length > 0 && this.layerPane.getComponent(0) == component) && component instanceof MessageScreen && !this.previousMessages.contains(component)) {
                this.previousMessages.add((MessageScreen)component);
            }
            this.layers.remove(component);
            this.layerPane.remove(component);
            if (this.layerPane.getComponents().length == 0 && this.layers.size() > 0) {
                JComponent component2 = this.layers.remove(this.layers.size() - 1);
                this.addLayer(component2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeAllLayers() {
        Object object = this.getTreeLock();
        synchronized (object) {
            if (this.layerPane.getComponents().length > 0) {
                this.layerPane.getComponent(0).setVisible(false);
            }
            this.layerPane.removeAll();
            this.layers.clear();
            this.previousMessages.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addMessage(JPanel message) {
        Thread.yield();
        Object object = this.getTreeLock();
        synchronized (object) {
            if (this.layerPane.getComponents().length == 0 || !(this.layerPane.getComponent(0) instanceof MessageInterface)) {
                this.addLayer(message);
            } else {
                boolean alreadyAdded = false;
                for (int i = 0; i < this.layers.size(); ++i) {
                    if (!(this.layers.get(i) instanceof MessageInterface)) continue;
                    this.layers.add(i, message);
                    alreadyAdded = true;
                    break;
                }
                if (!alreadyAdded) {
                    this.layers.add(message);
                }
                message.setVisible(false);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void closeAllMessages() {
        Object object = this.getTreeLock();
        synchronized (object) {
            if (this.layerPane.getComponents()[0] instanceof MessageScreen) {
                this.previousMessages.add((MessageScreen)this.layerPane.getComponents()[0]);
            }
            for (int i = this.layers.size() - 1; i >= 0 && this.layers.get(i) != null && this.layers.get(i) instanceof MessageInterface && !((MessageInterface)((Object)this.layers.get(i))).isPriorityMessage(); --i) {
                if (this.layers.get(i) instanceof MessageScreen) {
                    this.previousMessages.add((MessageScreen)this.layers.get(i));
                }
                this.layers.remove(i);
            }
            if (this.layerPane.getComponents()[0] instanceof MessageInterface) {
                this.removeLayer((JComponent)this.layerPane.getComponents()[0]);
            }
            this.repaint();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void closeAllMessagesByLocation(Object groupObject) {
        Object object = this.getTreeLock();
        synchronized (object) {
            if (this.layerPane.getComponents()[0] instanceof MessageScreen) {
                this.previousMessages.add((MessageScreen)this.layerPane.getComponents()[0]);
            }
            for (int i = this.layers.size() - 1; i >= 0 && this.layers.get(i) != null && this.layers.get(i) instanceof MessageInterface && !((MessageInterface)((Object)this.layers.get(i))).isPriorityMessage() && ((MessageInterface)((Object)this.layers.get(i))).getMessageGroupObject() == groupObject; --i) {
                if (this.layers.get(i) instanceof MessageScreen) {
                    this.previousMessages.add((MessageScreen)this.layers.get(i));
                }
                this.layers.remove(i);
            }
            if (this.layerPane.getComponents()[0] instanceof MessageInterface) {
                this.removeLayer((JComponent)this.layerPane.getComponents()[0]);
            }
            this.repaint();
        }
    }

    public void restoreLastMessage() {
        if (this.previousMessages.size() > 0) {
            MessageScreen messageScreen = this.previousMessages.remove(this.previousMessages.size() - 1);
            messageScreen.setPriorityMessage(false);
            this.addLayer(messageScreen);
        }
    }

    public void save() {
        JFileChooser fileChooser = this.getFileChooser();
        fileChooser.showSaveDialog(this);
        Jasbro.getInstance().save(fileChooser.getSelectedFile());
    }

    private JFileChooser getFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter(){

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "";
            }
        });
        fileChooser.setMultiSelectionEnabled(false);
        return fileChooser;
    }

    private void initComponents() {
        this.layerPane = new JPanel();
        this.setPreferredSize(new Dimension(this.resolutionWidth, this.resolutionHeight));
        this.getContentPane().setLayout(new GridLayout(1, 1));
        this.layerPane.setDoubleBuffered(true);
        this.layerPane.addHierarchyBoundsListener(new HierarchyBoundsListener(){

            @Override
            public void ancestorMoved(HierarchyEvent evt) {
            }

            @Override
            public void ancestorResized(HierarchyEvent evt) {
                RPGView.this.layerPaneAncestorResized(evt);
            }
        });
        this.getContentPane().add(this.layerPane);
        this.layerPane.setBackground(Color.WHITE);
        this.layerPane.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("fill:1dlu:grow")}));
        this.pack();
    }

    private void layerPaneAncestorResized(HierarchyEvent evt) {
        for (Component layer : this.layerPane.getComponents()) {
            layer.setSize(this.getContentPane().getWidth(), this.getContentPane().getHeight());
        }
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showTownScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            if (ConfigHandler.getSetting(Settings.TOWNSCREENNEW, true)) {
                this.addLayer(new TownMenuNew());
            } else {
                this.addLayer(new TownMenu());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showSlaveMarketScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.SLAVE;
            this.addLayer(new SlaveMarketMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showSlaverGuildScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.GUILD;
            this.addLayer(new SlaverGuildMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showAdventurersGuildScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.ADVENTURERSGUILD;
            this.addLayer(new AdventurersGuildMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showGeneralMarketScreen(int marketwindow) {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            if (marketwindow == 1) {
                this.actualScreen = Screen.MARKET;
                this.addLayer(new GeneralMarketMenu());
            }
            if (marketwindow == 2) {
                this.actualScreen = Screen.MARKET2;
                this.addLayer(new GeneralMarketMenuTwo());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showSlavePens() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.SLAVEPENS;
            this.addLayer(new SlavePensMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showBuildersGuildScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.BUILDER;
            this.addLayer(new BuildersGuildMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showCheatScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.addLayer(new CheatScreen());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showBuyPlotMapScreen(String map) {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.addLayer(new BuyPlotMapMenu(map));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showSchoolScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.SCHOOL;
            this.addLayer(new SchoolMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showShopScreen(String type) {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            switch (type) {
                case "general": {
                    this.addLayer(new ShopMenu(ItemLocation.SHOP));
                    this.actualScreen = Screen.GENERALSTORE;
                    break;
                }
                case "clothing": {
                    this.addLayer(new ShopMenu(ItemLocation.CLOTHINGSTORE));
                    this.actualScreen = Screen.TAILOR;
                    break;
                }
                case "adventurer": {
                    this.addLayer(new ShopMenu(ItemLocation.ADVENTURERSSHOP));
                    this.actualScreen = Screen.ADVENTURERSTORE;
                    break;
                }
                case "alchemist": {
                    this.addLayer(new ShopMenu(ItemLocation.APOTHECARY));
                    this.actualScreen = Screen.MAGICSHOP;
                    break;
                }
                case "adult": {
                    this.addLayer(new ShopMenu(ItemLocation.ADULTSTORE));
                    this.actualScreen = Screen.ADULTSTORE;
                    break;
                }
                case "traveling": {
                    this.addLayer(new ShopMenu(ItemLocation.TRAVELLINGMERCHANT));
                    this.actualScreen = Screen.TRAVELING;
                    break;
                }
                case "blackmarket": {
                    this.addLayer(new ShopMenu(ItemLocation.BLACKMARKET));
                    this.actualScreen = Screen.BLACKMARKET;
                    break;
                }
                case "architect": {
                    this.addLayer(new ShopMenu(ItemLocation.ARCHITECT));
                    this.actualScreen = Screen.ARCHITECT;
                    break;
                }
                case "book": {
                    this.addLayer(new ShopMenu(ItemLocation.BOOKSTORE));
                    this.actualScreen = Screen.BOOKSTORE;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showAlchemist() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.ALCHEMIST;
            this.addLayer(new AlchemistEntranceMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showTrainerBar() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.TRAINERBAR;
            this.addLayer(new TrainerBarMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showQuestScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.TRAINERBAR;
            this.addLayer(new QuestMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showAdventurerScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.TRAINERBAR;
            this.addLayer(new AdventurerBarMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showUnlockScreen() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.addLayer(new UnlockScreen());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showRealEstate() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.REALESTATE;
            this.addLayer(new RealEstateMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showConversationWindow() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.REALESTATE;
            this.addLayer(new RealEstateMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showLaboratory() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.LABORATORY;
            this.addLayer(new LaboratoryMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showInteriorDecoration() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.CARPENTRY;
            this.addLayer(new InteriorDecorationMenu());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void showAuctionHouse() {
        Object object = this.getTreeLock();
        synchronized (object) {
            this.removeAllLayers();
            this.actualScreen = Screen.AUCTIONHOUSE;
            this.addLayer(new AuctionHouseMenu());
        }
    }

    public JPanel getLayerPane() {
        return this.layerPane;
    }

    public MainMenuBar getMainMenuBar() {
        return this.menuBar;
    }

    public void showCharacterView(Charakter character) {
        this.addMessage(new CharacterScreen(character));
    }

    public void showHouseScreen(House house) {
        this.removeAllLayers();
        this.addLayer(new HouseScreen(house));
    }

    public void showQuestsScreen() {
        this.removeAllLayers();
        this.addLayer(new QuestScreen());
    }

    public void showGameOverScreen() {
        this.addMessage(new GameOverScreen());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void startAuction(AuctionHouse auctionHouse, Charakter selectedSlave, boolean sell) {
        Object object = this.getTreeLock();
        synchronized (object) {
            Auction auction = new Auction();
            auction.setSlave(selectedSlave);
            AuctionScreen auctionScreen = new AuctionScreen(auction);
            auction.setGui(auctionScreen);
            this.addLayer(auctionScreen);
            auction.startAuction();
        }
    }

    public void updateStatus() {
        if (!this.repaintScheduled) {
            AccessController.doPrivileged(new PrivilegedAction<Void>(){

                @Override
                public Void run() {
                    Jasbro.getThreadpool().execute(new Runnable(){

                        @Override
                        public void run() {
                            RPGView.this.repaintScheduled = true;
                            try {
                                Thread.sleep(100L);
                            }
                            catch (InterruptedException interruptedException) {
                                // empty catch block
                            }
                            RPGView.this.getMainMenuBar().updateStatusInfo();
                            RPGView.this.repaintScheduled = false;
                        }
                    });
                    return null;
                }
            });
        }
    }

    public boolean hasPreviousMessages() {
        return this.previousMessages.size() > 0;
    }

    public CharacterFilterListModel getFilteredModel() {
        return this.filteredModel;
    }

    public void setFilteredModel(CharacterFilterListModel filteredModel) {
        this.filteredModel = filteredModel;
    }

    public void changeResolution(int width, int height) {
        Dimension size = new Dimension(width, height);
        this.setSize(size);
        this.repaint();
        switch (this.actualScreen) {
            case ADULTSTORE: {
                this.showShopScreen("adult");
                break;
            }
            case ADVENTURERSTORE: {
                this.showShopScreen("adventurer");
                break;
            }
            case ALCHEMIST: {
                this.showAlchemist();
                break;
            }
            case ARCHITECT: {
                this.showShopScreen("architect");
                break;
            }
            case AUCTIONHOUSE: {
                this.showAuctionHouse();
                break;
            }
            case BLACKMARKET: {
                this.showShopScreen("blackmarket");
                break;
            }
            case BOOKSTORE: {
                this.showShopScreen("book");
                break;
            }
            case BUILDER: {
                this.showBuildersGuildScreen();
                break;
            }
            case CARPENTRY: {
                this.showInteriorDecoration();
                break;
            }
            case GENERALSTORE: {
                this.showShopScreen("general");
                break;
            }
            case GUILD: {
                this.showSlaverGuildScreen();
                break;
            }
            case LABORATORY: {
                this.showLaboratory();
                break;
            }
            case MAGICSHOP: {
                this.showShopScreen("alchemist");
                break;
            }
            case MARKET: {
                this.showGeneralMarketScreen(1);
                break;
            }
            case MARKET2: {
                this.showGeneralMarketScreen(2);
                break;
            }
            case REALESTATE: {
                this.showRealEstate();
                break;
            }
            case SCHOOL: {
                this.showSchoolScreen();
                break;
            }
            case SLAVE: {
                this.showSlaveMarketScreen();
                break;
            }
            case SLAVEPENS: {
                this.showSlavePens();
                break;
            }
            case TAILOR: {
                this.showShopScreen("clothing");
                break;
            }
            case TOWN: {
                this.showTownScreen();
                break;
            }
            case TRAINERBAR: {
                this.showTrainerBar();
                break;
            }
            case TRAVELING: {
                this.showShopScreen("traveling");
                break;
            }
            case ADVENTURERSGUILD: {
                this.showAdventurersGuildScreen();
                break;
            }
        }
        this.actualScreen = Screen.NONE;
    }

    private static enum Screen {
        TOWN,
        BUILDER,
        SLAVE,
        SCHOOL,
        MARKET,
        MARKET2,
        GUILD,
        ARCHITECT,
        CARPENTRY,
        REALESTATE,
        BOOKSTORE,
        MAGICSHOP,
        ADULTSTORE,
        GENERALSTORE,
        TAILOR,
        ADVENTURERSTORE,
        SLAVEPENS,
        AUCTIONHOUSE,
        LABORATORY,
        ALCHEMIST,
        TRAVELING,
        BLACKMARKET,
        TRAINERBAR,
        ADVENTURERSGUILD,
        NONE;

    }
}

