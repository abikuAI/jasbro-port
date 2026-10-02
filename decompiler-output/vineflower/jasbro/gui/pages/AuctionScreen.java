package jasbro.gui.pages;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.world.market.Auction;
import jasbro.game.world.market.Bidder;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class AuctionScreen extends JPanel implements Auction.AuctionGui {
   private Auction auction;
   private JTextArea messageField;
   private Bidder.UiBidder playerBidder;
   private JScrollPane messageScrollPane;
   private JButton leaveEarlyButton;

   public AuctionScreen(Auction auction) {
      this.setBackground(Color.WHITE);
      this.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.auction = auction;
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("1dlu:grow(2)"), ColumnSpec.decode("1dlu:grow")},
            new RowSpec[]{RowSpec.decode("1dlu:grow"), RowSpec.decode("fill:30dlu")}
         )
      );
      MyImage girlImage = new MyImage();
      this.add(girlImage, "2, 1, fill, fill");
      girlImage.setOpaque(false);
      List<ImageTag> imageTags = auction.getSlave().getBaseTags();
      imageTags.add(0, ImageTag.NAKED);
      imageTags.add(1, ImageTag.CLEANED);
      girlImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, auction.getSlave().getImages()));
      girlImage.setBackgroundImage(new ImageData("images/backgrounds/auction.jpg"));
      this.messageScrollPane = new JScrollPane();
      this.add(this.messageScrollPane, "3, 1, fill, fill");
      this.messageField = new JTextArea();
      this.messageField.setLineWrap(true);
      this.messageField.setWrapStyleWord(true);
      this.messageField.setEditable(false);
      this.messageField.setFont(new Font("Tahoma", 0, 20));
      this.messageScrollPane.setViewportView(this.messageField);
      ActionListener al = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               AuctionScreen.this.playerBidder.bid(AuctionScreen.this.auction.getMaxBid() + Integer.parseInt(e.getActionCommand()));
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         }
      };
      this.leaveEarlyButton = new JButton(TextUtil.t("ui.auction.leaveEarly"));
      this.leaveEarlyButton.setEnabled(false);
      this.leaveEarlyButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AuctionScreen.this.auction.setAbort(true);
         }
      });
      this.add(this.leaveEarlyButton, "2, 2, left, center");
      JPanel bidPanel = new JPanel();
      this.add(bidPanel, "3, 2, fill, fill");
      bidPanel.setBorder(GuiUtil.DEFAULTBORDER);
      bidPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      bidPanel.setAutoscrolls(true);
      bidPanel.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("3dlu:grow"), ColumnSpec.decode("15dlu:grow"), ColumnSpec.decode("15dlu:grow")},
            new RowSpec[]{RowSpec.decode("10dlu:grow"), RowSpec.decode("10dlu:grow")}
         )
      );
      JButton btnBidMore = new JButton("Bid 1 more");
      bidPanel.add(btnBidMore, "1, 1");
      btnBidMore.setActionCommand("1");
      btnBidMore.addActionListener(al);
      btnBidMore = new JButton("Bid 10 more");
      bidPanel.add(btnBidMore, "2, 1");
      btnBidMore.setActionCommand("10");
      btnBidMore.addActionListener(al);
      btnBidMore = new JButton("Bid 100 more");
      bidPanel.add(btnBidMore, "3, 1");
      btnBidMore.setActionCommand("100");
      btnBidMore.addActionListener(al);
      btnBidMore = new JButton("Bid 1000 more");
      bidPanel.add(btnBidMore, "1, 2");
      btnBidMore.setActionCommand("1000");
      btnBidMore.addActionListener(al);
      btnBidMore = new JButton("Bid 10000 more");
      bidPanel.add(btnBidMore, "2, 2");
      btnBidMore.setActionCommand("10000");
      btnBidMore.addActionListener(al);
      btnBidMore = new JButton("Bid 100000 more");
      bidPanel.add(btnBidMore, "3, 2");
      btnBidMore.setActionCommand("100000");
      btnBidMore.addActionListener(al);
      this.playerBidder = new Bidder.UiBidder();
      this.playerBidder.setAuction(auction);
      this.validate();
   }

   @Override
   public void update() {
      this.messageField.setText(this.auction.getMessages());
      if (!this.auction.isOwnSlave() && !(this.auction.getMaxBidder() instanceof Bidder.UiBidder)) {
         this.leaveEarlyButton.setEnabled(true);
      } else {
         this.leaveEarlyButton.setEnabled(false);
      }

      JScrollBar vertical = this.messageScrollPane.getVerticalScrollBar();
      vertical.setValue(vertical.getMaximum());
      this.repaint();
   }

   @Override
   public void close() {
      Jasbro.getInstance().getGui().removeLayer(this);
   }

   @Override
   public void setVisible(boolean visibility) {
      if (!visibility) {
         this.auction.setAbort(true);
      }

      super.setVisible(visibility);
   }
}
