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

public class RPGView extends JFrame {
   private JPanel layerPane;
   private List<JComponent> layers = new ArrayList<>();
   private MainMenuBar menuBar;
   private boolean repaintScheduled = false;
   private List<MessageScreen> previousMessages = new ArrayList<>();
   private int resolutionHeight = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
   private int resolutionWidth = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
   private RPGView.Screen actualScreen = RPGView.Screen.NONE;
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

   public void showStartScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.addLayer(new NewGameScreen());
      }
   }

   public void showMainMenu() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.addLayer(new MainMenu());
      }
   }

   public void showHouseManagementScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.addLayer(new ManagementScreen());
      }
   }

   public void addLayer(JComponent layer) {
      synchronized (this.getTreeLock()) {
         if (layer instanceof MessageInterface) {
            ((MessageInterface)layer).init();
         }

         if (this.layerPane.getComponents().length != 0) {
            this.layerPane.getComponents()[0].setVisible(false);
            this.layers.add((JComponent)this.layerPane.getComponents()[0]);
         }

         this.layerPane.removeAll();
         this.layerPane.add(layer, "1, 1, fill, fill");
         layer.setVisible(true);
         this.layerPane.validate();
         this.repaint();
      }
   }

   public void addLayerBottom(JPanel layer) {
      synchronized (this.getTreeLock()) {
         if (this.layerPane.getComponents().length != 0) {
            this.layers.add(0, layer);
            layer.setVisible(false);
         } else if (layer != null) {
            this.addLayer(layer);
         }
      }
   }

   public void removeLayer(JComponent component) {
      synchronized (this.getTreeLock()) {
         if ((this.layers.contains(component) || this.layerPane.getComponents().length > 0 && this.layerPane.getComponent(0) == component)
            && component instanceof MessageScreen
            && !this.previousMessages.contains(component)) {
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

   public void removeAllLayers() {
      synchronized (this.getTreeLock()) {
         if (this.layerPane.getComponents().length > 0) {
            this.layerPane.getComponent(0).setVisible(false);
         }

         this.layerPane.removeAll();
         this.layers.clear();
         this.previousMessages.clear();
      }
   }

   public void addMessage(JPanel message) {
      Thread.yield();
      synchronized (this.getTreeLock()) {
         if (this.layerPane.getComponents().length != 0 && this.layerPane.getComponent(0) instanceof MessageInterface) {
            boolean alreadyAdded = false;

            for (int i = 0; i < this.layers.size(); i++) {
               if (this.layers.get(i) instanceof MessageInterface) {
                  this.layers.add(i, message);
                  alreadyAdded = true;
                  break;
               }
            }

            if (!alreadyAdded) {
               this.layers.add(message);
            }

            message.setVisible(false);
         } else {
            this.addLayer(message);
         }
      }
   }

   public void closeAllMessages() {
      synchronized (this.getTreeLock()) {
         if (this.layerPane.getComponents()[0] instanceof MessageScreen) {
            this.previousMessages.add((MessageScreen)this.layerPane.getComponents()[0]);
         }

         for (int i = this.layers.size() - 1;
            i >= 0
               && this.layers.get(i) != null
               && this.layers.get(i) instanceof MessageInterface
               && !((MessageInterface)this.layers.get(i)).isPriorityMessage();
            i--
         ) {
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

   public void closeAllMessagesByLocation(Object groupObject) {
      synchronized (this.getTreeLock()) {
         if (this.layerPane.getComponents()[0] instanceof MessageScreen) {
            this.previousMessages.add((MessageScreen)this.layerPane.getComponents()[0]);
         }

         for (int i = this.layers.size() - 1;
            i >= 0
               && this.layers.get(i) != null
               && this.layers.get(i) instanceof MessageInterface
               && !((MessageInterface)this.layers.get(i)).isPriorityMessage()
               && ((MessageInterface)this.layers.get(i)).getMessageGroupObject() == groupObject;
            i--
         ) {
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
      fileChooser.setFileFilter(new FileFilter() {
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
      this.layerPane.addHierarchyBoundsListener(new HierarchyBoundsListener() {
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

   public void showTownScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         if (ConfigHandler.getSetting(Settings.TOWNSCREENNEW, true)) {
            this.addLayer(new TownMenuNew());
         } else {
            this.addLayer(new TownMenu());
         }
      }
   }

   public void showSlaveMarketScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.SLAVE;
         this.addLayer(new SlaveMarketMenu());
      }
   }

   public void showSlaverGuildScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.GUILD;
         this.addLayer(new SlaverGuildMenu());
      }
   }

   public void showAdventurersGuildScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.ADVENTURERSGUILD;
         this.addLayer(new AdventurersGuildMenu());
      }
   }

   public void showGeneralMarketScreen(int marketwindow) {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         if (marketwindow == 1) {
            this.actualScreen = RPGView.Screen.MARKET;
            this.addLayer(new GeneralMarketMenu());
         }

         if (marketwindow == 2) {
            this.actualScreen = RPGView.Screen.MARKET2;
            this.addLayer(new GeneralMarketMenuTwo());
         }
      }
   }

   public void showSlavePens() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.SLAVEPENS;
         this.addLayer(new SlavePensMenu());
      }
   }

   public void showBuildersGuildScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.BUILDER;
         this.addLayer(new BuildersGuildMenu());
      }
   }

   public void showCheatScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.addLayer(new CheatScreen());
      }
   }

   public void showBuyPlotMapScreen(String map) {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.addLayer(new BuyPlotMapMenu(map));
      }
   }

   public void showSchoolScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.SCHOOL;
         this.addLayer(new SchoolMenu());
      }
   }

   public void showShopScreen(String type) {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         switch (type) {
            case "general":
               this.addLayer(new ShopMenu(ItemLocation.SHOP));
               this.actualScreen = RPGView.Screen.GENERALSTORE;
               break;
            case "clothing":
               this.addLayer(new ShopMenu(ItemLocation.CLOTHINGSTORE));
               this.actualScreen = RPGView.Screen.TAILOR;
               break;
            case "adventurer":
               this.addLayer(new ShopMenu(ItemLocation.ADVENTURERSSHOP));
               this.actualScreen = RPGView.Screen.ADVENTURERSTORE;
               break;
            case "alchemist":
               this.addLayer(new ShopMenu(ItemLocation.APOTHECARY));
               this.actualScreen = RPGView.Screen.MAGICSHOP;
               break;
            case "adult":
               this.addLayer(new ShopMenu(ItemLocation.ADULTSTORE));
               this.actualScreen = RPGView.Screen.ADULTSTORE;
               break;
            case "traveling":
               this.addLayer(new ShopMenu(ItemLocation.TRAVELLINGMERCHANT));
               this.actualScreen = RPGView.Screen.TRAVELING;
               break;
            case "blackmarket":
               this.addLayer(new ShopMenu(ItemLocation.BLACKMARKET));
               this.actualScreen = RPGView.Screen.BLACKMARKET;
               break;
            case "architect":
               this.addLayer(new ShopMenu(ItemLocation.ARCHITECT));
               this.actualScreen = RPGView.Screen.ARCHITECT;
               break;
            case "book":
               this.addLayer(new ShopMenu(ItemLocation.BOOKSTORE));
               this.actualScreen = RPGView.Screen.BOOKSTORE;
         }
      }
   }

   public void showAlchemist() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.ALCHEMIST;
         this.addLayer(new AlchemistEntranceMenu());
      }
   }

   public void showTrainerBar() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.TRAINERBAR;
         this.addLayer(new TrainerBarMenu());
      }
   }

   public void showQuestScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.TRAINERBAR;
         this.addLayer(new QuestMenu());
      }
   }

   public void showAdventurerScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.TRAINERBAR;
         this.addLayer(new AdventurerBarMenu());
      }
   }

   public void showUnlockScreen() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.addLayer(new UnlockScreen());
      }
   }

   public void showRealEstate() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.REALESTATE;
         this.addLayer(new RealEstateMenu());
      }
   }

   public void showConversationWindow() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.REALESTATE;
         this.addLayer(new RealEstateMenu());
      }
   }

   public void showLaboratory() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.LABORATORY;
         this.addLayer(new LaboratoryMenu());
      }
   }

   public void showInteriorDecoration() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.CARPENTRY;
         this.addLayer(new InteriorDecorationMenu());
      }
   }

   public void showAuctionHouse() {
      synchronized (this.getTreeLock()) {
         this.removeAllLayers();
         this.actualScreen = RPGView.Screen.AUCTIONHOUSE;
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

   public void startAuction(AuctionHouse auctionHouse, Charakter selectedSlave, boolean sell) {
      synchronized (this.getTreeLock()) {
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
         AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
               Jasbro.getThreadpool().execute(new Runnable() {
                  @Override
                  public void run() {
                     RPGView.this.repaintScheduled = true;

                     try {
                        Thread.sleep(100L);
                     } catch (InterruptedException e) {
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
         case ADULTSTORE:
            this.showShopScreen("adult");
            break;
         case ADVENTURERSTORE:
            this.showShopScreen("adventurer");
            break;
         case ALCHEMIST:
            this.showAlchemist();
            break;
         case ARCHITECT:
            this.showShopScreen("architect");
            break;
         case AUCTIONHOUSE:
            this.showAuctionHouse();
            break;
         case BLACKMARKET:
            this.showShopScreen("blackmarket");
            break;
         case BOOKSTORE:
            this.showShopScreen("book");
            break;
         case BUILDER:
            this.showBuildersGuildScreen();
            break;
         case CARPENTRY:
            this.showInteriorDecoration();
            break;
         case GENERALSTORE:
            this.showShopScreen("general");
            break;
         case GUILD:
            this.showSlaverGuildScreen();
            break;
         case LABORATORY:
            this.showLaboratory();
            break;
         case MAGICSHOP:
            this.showShopScreen("alchemist");
            break;
         case MARKET:
            this.showGeneralMarketScreen(1);
            break;
         case MARKET2:
            this.showGeneralMarketScreen(2);
            break;
         case REALESTATE:
            this.showRealEstate();
            break;
         case SCHOOL:
            this.showSchoolScreen();
            break;
         case SLAVE:
            this.showSlaveMarketScreen();
            break;
         case SLAVEPENS:
            this.showSlavePens();
            break;
         case TAILOR:
            this.showShopScreen("clothing");
            break;
         case TOWN:
            this.showTownScreen();
            break;
         case TRAINERBAR:
            this.showTrainerBar();
            break;
         case TRAVELING:
            this.showShopScreen("traveling");
            break;
         case ADVENTURERSGUILD:
            this.showAdventurersGuildScreen();
      }

      this.actualScreen = RPGView.Screen.NONE;
   }

   private enum Screen {
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
