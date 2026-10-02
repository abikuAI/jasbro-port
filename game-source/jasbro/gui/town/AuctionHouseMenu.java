package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class AuctionHouseMenu extends MyImage {
   private static final Map<Trait, String> TRAIT_OPTION_KEYS = new EnumMap<>(Trait.class);
   private MyImage girlImage;
   private MyImage auctioneerImage;
   private AuctionHouse auctionHouse;
   private Color backgroundColor = new Color(1.0F, 1.0F, 0.9F, 0.8F);
   private Charakter selectedSlave = null;
   private JPanel buyList;
   private JButton startAuctionButton;
   private JTextArea hintArea;
   private String behavior2;
   private String slaveJob;
   private String slaveJobText;
   private String furryText;
   private String proficiency;

   public AuctionHouseMenu() {
      this.setOpaque(false);
      this.setBackgroundImage(new ImageData("images/backgrounds/auctionHouse.jpg"));
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
      backButton.setBounds((int)(15.0 * width / 1280.0), (int)(620.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
      backButton.setBorderPainted(false);
      backButton.setContentAreaFilled(false);
      backButton.setFocusPainted(false);
      backButton.setOpaque(false);
      this.add(backButton);
      backButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showSlaveMarketScreen();
         }
      });
      this.auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();
      FormLayout formLayout = new FormLayout(
         new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")},
         new RowSpec[]{
            RowSpec.decode("default:grow"),
            RowSpec.decode("min:grow(20)"),
            RowSpec.decode("min:grow(40)"),
            RowSpec.decode("default:grow"),
            RowSpec.decode("30dlu")
         }
      );
      formLayout.setHonorsVisibility(false);
      this.setLayout(formLayout);
      this.auctioneerImage = new MyImage();
      if (Jasbro.getInstance().getData().getDay() % 2 == 0) {
         this.auctioneerImage.setImage(new ImageData("images/people/Runa and Rune 1.png"));
      } else {
         this.auctioneerImage.setImage(new ImageData("images/people/Runa and Rune 2.png"));
      }

      this.add(this.auctioneerImage, "1, 1, 1, 5, fill, fill");
      UIManager.put("TabbedPane.contentOpaque", false);
      JTabbedPane tabbedPane = new JTabbedPane(1);
      tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.add(tabbedPane, "2, 2, fill, center");
      tabbedPane.setOpaque(false);
      JPanel buyPanel = new TranslucentPanel();
      tabbedPane.addTab("Buy girl", null, buyPanel, null);
      buyPanel.setBackground(this.backgroundColor);
      buyPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
      buyPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("10px:grow")}, new RowSpec[]{RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow")})
      );
      JLabel lblBuyGirl = new JLabel("Buy slave");
      lblBuyGirl.setFont(new Font("Tahoma", 1, 18));
      buyPanel.add(lblBuyGirl, "1, 1");
      this.buyList = new JPanel();
      this.buyList.setOpaque(false);
      buyPanel.add(this.buyList, "1, 2, fill, fill");
      this.hintArea = GuiUtil.getDefaultTextarea();
      this.hintArea.setText(TextUtil.t("ui.buyslave.introduction"));
      this.hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
      TranslucentPanel controlPanel = new TranslucentPanel();
      controlPanel.setBackground(this.backgroundColor);
      this.add(controlPanel, "2, 3, fill, fill");
      controlPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC})
      );
      controlPanel.add(this.hintArea, "1, 1, fill, fill");
      JPanel sellPanel = new TranslucentPanel();
      tabbedPane.addTab("Sell slave", null, sellPanel, null);
      sellPanel.setBackground(this.backgroundColor);
      sellPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
      sellPanel.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("10px:grow")},
            new RowSpec[]{RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow")}
         )
      );
      JLabel lblSellGirl = new JLabel("Sell slave");
      lblSellGirl.setFont(new Font("Tahoma", 1, 18));
      sellPanel.add(lblSellGirl, "1, 1, left, center");
      final JComboBox<Charakter> characterSelect = new JComboBox<>();
      characterSelect.addItem(null);

      for (Charakter character : Jasbro.getInstance().getData().getSlavesForSale()) {
         characterSelect.addItem(character);
      }

      sellPanel.add(characterSelect, "1, 2, left, center");
      final JPanel sellDataPanel = new JPanel();
      sellDataPanel.setOpaque(false);
      sellPanel.add(sellDataPanel, "1, 3, fill, fill");
      sellDataPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("center:pref:grow")}, new RowSpec[]{RowSpec.decode("pref:grow")}));
      characterSelect.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
               Charakter slave = (Charakter)characterSelect.getSelectedItem();
               AuctionHouseMenu.this.selectedSlave = slave;
               sellDataPanel.removeAll();
               sellDataPanel.add(new CharacterShortView(slave), "1,1");
               List<ImageTag> imageTags = AuctionHouseMenu.this.selectedSlave.getBaseTags();
               imageTags.add(0, ImageTag.NAKED);
               imageTags.add(1, ImageTag.CLEANED);
               sellDataPanel.validate();
               AuctionHouseMenu.this.startAuctionButton.setEnabled(true);
               AuctionHouseMenu.this.repaint();
            }
         }
      });
      this.startAuctionButton = new JButton("Start Auction");
      this.add(this.startAuctionButton, "2, 4, center, top");
      this.startAuctionButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (AuctionHouseMenu.this.selectedSlave != null) {
               Charakter character = AuctionHouseMenu.this.selectedSlave;
               AuctionHouseMenu.this.auctionHouse.getSlaves().remove(AuctionHouseMenu.this.selectedSlave);
               sellDataPanel.removeAll();
               characterSelect.removeItem(AuctionHouseMenu.this.selectedSlave);
               AuctionHouseMenu.this.selectedSlave = null;
               Jasbro.getInstance().getGui().startAuction(AuctionHouseMenu.this.auctionHouse, character, false);
               AuctionHouseMenu.this.initBuyList();
            }
         }
      });
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               Jasbro.getInstance().getGui().showSlaveMarketScreen();
            }
         }
      });
      this.initBuyList();
      this.validate();
   }

   public void initBuyList() {
      MouseListener ml = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            this.mouseAction(e);
         }

         public void mouseAction(MouseEvent e) {
            if (!e.isConsumed()) {
               e.consume();
               CharacterShortView shortView = (CharacterShortView)e.getSource();
               AuctionHouseMenu.this.selectedSlave = shortView.getCharacter();
               List<ImageTag> imageTags = AuctionHouseMenu.this.selectedSlave.getBaseTags();
               imageTags.add(0, ImageTag.NAKED);
               imageTags.add(1, ImageTag.CLEANED);
               AuctionHouseMenu.this.getAuctionText();
               AuctionHouseMenu.this.startAuctionButton.setEnabled(true);
               AuctionHouseMenu.this.repaint();
            }
         }
      };
      this.buyList.removeAll();

      for (Charakter character : this.auctionHouse.getSlaves()) {
         CharacterShortView shortView = new CharacterShortView(character, false);
         this.buyList.add(shortView);
         shortView.addMouseListener(ml);
      }
   }

   public void getAuctionText() {
      String auctioneer1 = "Rune";
      String auctioneer2 = "Runa";
      String behavior1;
      if (Jasbro.getInstance().getData().getDay() % 2 == 0) {
         behavior1 = "lewd";
         this.behavior2 = "mean";
      } else {
         behavior1 = "mean";
         this.behavior2 = "lewd";
      }

      this.getFurryText();
      this.getSpecializationText();
      String introductionText;
      if (this.selectedSlave.getSpecializations().contains(SpecializationType.FURRY)) {
         introductionText = TextUtil.t(
            this.slaveJobText + "\n" + this.furryText + "\n\n" + auctioneer1 + ": " + "auction.introduction." + behavior1, this.selectedSlave
         );
      } else {
         introductionText = TextUtil.t(this.slaveJobText + "\n\n" + auctioneer1 + ": " + "auction.introduction." + behavior1, this.selectedSlave);
      }

      int a = this.selectedSlave.getTraits().size();
      if (a > 3) {
         int var12 = 3;
      }

      String[] traitText = new String[3];
      List<String> traitOption = new ArrayList<>();
      this.getTrait(traitOption);
      Vector<String> vector = new Vector<>();
      int check = 3;

      do {
         if (traitOption.size() < 3) {
            check = traitOption.size();
         }

         if (traitOption.size() == 0) {
            break;
         }

         int choice = Util.getInt(0, traitOption.size());
         String temp = traitOption.get(choice);
         if (!vector.contains(temp)) {
            vector.add(temp);
         }
      } while (vector.size() != check);

      if (traitOption.size() != 0) {
         for (int i = 0; i < vector.size(); i++) {
            String choice;
            if (i != 1) {
               choice = this.behavior2;
            } else {
               choice = behavior1;
            }

            traitText[i] = TextUtil.t(vector.get(i) + choice, this.selectedSlave);
         }

         if (vector.size() == 1) {
            this.hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + traitText[0]);
         }

         if (vector.size() == 2) {
            this.hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + traitText[0] + "\n" + auctioneer1 + ": " + traitText[1]);
         }

         if (vector.size() == 3) {
            this.hintArea
               .setText(
                  introductionText
                     + "\n"
                     + auctioneer2
                     + ": "
                     + traitText[0]
                     + "\n"
                     + auctioneer1
                     + ": "
                     + traitText[1]
                     + "\n"
                     + auctioneer2
                     + ": "
                     + traitText[2]
                     + "\n"
               );
         }
      } else {
         this.hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + TextUtil.t("auction.trait.notrait." + this.behavior2, this.selectedSlave));
      }
   }

   void getFurryText() {
   }

   void getSpecializationText() {
      int skill = 0;
      this.slaveJob = this.selectedSlave.getSlaveJob();
      if (this.slaveJob == "ClassyWhore") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.SEDUCTION) + this.selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.classywhore." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "CumBucket") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.SEDUCTION) + this.selectedSlave.getFinalValue(BaseAttributeTypes.STAMINA), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.cumbucket." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "ServesDrunkards") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.BARTENDING) + this.selectedSlave.getFinalValue(BaseAttributeTypes.INTELLIGENCE), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.servesdrunkards." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "ClassyBartender") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.BARTENDING) + this.selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.classybartender." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "GivesExtras") {
         this.getProficiency(
            (int)(this.selectedSlave.getFinalValue(SpecializationAttribute.STRIP) + Util.getAverage(SpecializationType.SEX, this.selectedSlave)), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.givesextras." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "Performer") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.STRIP) + this.selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.performer." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "Brawler") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.VETERAN) + this.selectedSlave.getFinalValue(BaseAttributeTypes.STRENGTH), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.brawler." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "Mage") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.VETERAN) + this.selectedSlave.getFinalValue(BaseAttributeTypes.INTELLIGENCE), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.mage." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "BookWorm") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) + this.selectedSlave.getFinalValue(BaseAttributeTypes.INTELLIGENCE),
            30,
            60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.bookworm." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "DoesWhatShesAskedTo") {
         this.getProficiency(
            this.selectedSlave.getFinalValue(SpecializationAttribute.MAGIC) + this.selectedSlave.getFinalValue(BaseAttributeTypes.OBEDIENCE), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.doeswhatshesaskedto." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "FuckedByMonsters") {
         this.getProficiency(this.selectedSlave.getFinalValue(Sextype.MONSTER) + this.selectedSlave.getFinalValue(BaseAttributeTypes.STRENGTH), 30, 60);
         this.slaveJobText = TextUtil.t("auction.slavejob.fuckedbymonsters." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "FuckedByGroup") {
         this.getProficiency(this.selectedSlave.getFinalValue(Sextype.GROUP) + this.selectedSlave.getFinalValue(BaseAttributeTypes.STAMINA), 30, 60);
         this.slaveJobText = TextUtil.t("auction.slavejob.fuckedbygroup." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "Cosette") {
         this.getProficiency(
            (int)(Util.getAverage(SpecializationType.MAID, this.selectedSlave) + this.selectedSlave.getFinalValue(BaseAttributeTypes.OBEDIENCE)), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.cosette." + this.proficiency, this.selectedSlave);
      } else if (this.slaveJob == "HeadMaid") {
         this.getProficiency(
            (int)(Util.getAverage(SpecializationType.MAID, this.selectedSlave) + this.selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA)), 30, 60
         );
         this.slaveJobText = TextUtil.t("auction.slavejob.headmaid." + this.proficiency, this.selectedSlave);
      } else {
         this.getProficiency(this.selectedSlave.getFinalValue(BaseAttributeTypes.OBEDIENCE), 20, 40);
         this.slaveJobText = TextUtil.t("auction.slavejob.none." + this.proficiency, this.selectedSlave);
      }
   }

   void getProficiency(Integer skill, int a, int b) {
      if (skill <= a) {
         this.proficiency = "low";
      } else if (skill > b) {
         this.proficiency = "high";
      } else {
         this.proficiency = "mid";
      }
   }

   void getTrait(List<String> traitOption) {
      for (Trait t : this.selectedSlave.getTraits()) {
         traitOption.add(TRAIT_OPTION_KEYS.get(t));
      }
   }

   static {
      for (Trait t : Trait.values()) {
         TRAIT_OPTION_KEYS.put(t, String.format("auction.trait.%s.", t.toString().toLowerCase()));
      }
   }
}
