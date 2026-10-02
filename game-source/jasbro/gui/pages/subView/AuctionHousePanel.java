package jasbro.gui.pages.subView;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class AuctionHousePanel extends JPanel {
   private MyImage girlImage;
   private AuctionHouse auctionHouse;
   private Color backgroundColor = new Color(1.0F, 1.0F, 0.9F, 0.8F);
   private Charakter selectedSlave = null;
   private JPanel buyList;
   private JButton startAuctionButton;

   public AuctionHousePanel() {
      this.setOpaque(false);
      this.auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();
      FormLayout formLayout = new FormLayout(
         new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")},
         new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("min:grow(10)"), RowSpec.decode("default:grow"), RowSpec.decode("30dlu")}
      );
      formLayout.setHonorsVisibility(false);
      this.setLayout(formLayout);
      UIManager.put("TabbedPane.contentOpaque", false);
      JTabbedPane tabbedPane = new JTabbedPane(1);
      tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.add(tabbedPane, "1, 2, fill, fill");
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
      JLabel lblSellGirl = new JLabel("Sell girl");
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
      characterSelect.addItemListener(
         new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == 1) {
                  Charakter slave = (Charakter)characterSelect.getSelectedItem();
                  AuctionHousePanel.this.selectedSlave = slave;
                  sellDataPanel.removeAll();
                  sellDataPanel.add(new CharacterShortView(slave), "1,1");
                  List<ImageTag> imageTags = AuctionHousePanel.this.selectedSlave.getBaseTags();
                  imageTags.add(0, ImageTag.NAKED);
                  imageTags.add(1, ImageTag.CLEANED);
                  AuctionHousePanel.this.girlImage
                     .setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, AuctionHousePanel.this.selectedSlave.getImages()));
                  sellDataPanel.validate();
                  AuctionHousePanel.this.startAuctionButton.setEnabled(true);
                  AuctionHousePanel.this.repaint();
               }
            }
         }
      );
      this.startAuctionButton = new JButton("Start Auction");
      this.add(this.startAuctionButton, "1, 4, right, default");
      this.startAuctionButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (AuctionHousePanel.this.selectedSlave != null) {
               Charakter character = AuctionHousePanel.this.selectedSlave;
               AuctionHousePanel.this.auctionHouse.getSlaves().remove(AuctionHousePanel.this.selectedSlave);
               AuctionHousePanel.this.girlImage.setImage(null);
               sellDataPanel.removeAll();
               characterSelect.removeItem(AuctionHousePanel.this.selectedSlave);
               AuctionHousePanel.this.selectedSlave = null;
               Jasbro.getInstance().getGui().startAuction(AuctionHousePanel.this.auctionHouse, character, false);
               AuctionHousePanel.this.initBuyList();
            }
         }
      });
      this.initBuyList();
      this.validate();
      this.girlImage = new MyImage();
      this.add(this.girlImage, "2, 1, 1, 4");
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
               AuctionHousePanel.this.selectedSlave = shortView.getCharacter();
               List<ImageTag> imageTags = AuctionHousePanel.this.selectedSlave.getBaseTags();
               imageTags.add(0, ImageTag.NAKED);
               imageTags.add(1, ImageTag.CLEANED);
               AuctionHousePanel.this.girlImage
                  .setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, AuctionHousePanel.this.selectedSlave.getImages()));
               AuctionHousePanel.this.repaint();
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
}
