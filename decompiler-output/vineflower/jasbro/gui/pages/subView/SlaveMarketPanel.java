package jasbro.gui.pages.subView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.SlaveMarket;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SlaveMarketPanel extends JPanel {
   private JPanel hirePanel;
   private MyImage slaveImage;
   private Charakter selectedSlave = null;
   private JTextArea hintArea;

   public SlaveMarketPanel() {
      this.setOpaque(false);
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               ColumnSpec.decode("default:grow"),
               ColumnSpec.decode("default:grow(20)"),
               ColumnSpec.decode("default:grow"),
               ColumnSpec.decode("default:grow(18)"),
               ColumnSpec.decode("default:grow")
            },
            new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("default:grow(16)"), RowSpec.decode("default:grow(3)")}
         )
      );
      TranslucentPanel translucentPanel = new TranslucentPanel();
      translucentPanel.setPreferredSize(new Dimension(0, 0));
      this.add(translucentPanel, "2, 2, fill, fill");
      translucentPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow")})
      );
      JLabel lblBuyGirl = new JLabel(TextUtil.t("ui.buyslave.title"));
      lblBuyGirl.setFont(new Font("Tahoma", 1, 18));
      translucentPanel.add(lblBuyGirl, "1, 1");
      this.hirePanel = new JPanel();
      this.hirePanel.setOpaque(false);
      translucentPanel.add(this.hirePanel, "1, 2, fill, fill");
      this.slaveImage = new MyImage();
      this.add(this.slaveImage, "4, 1, 1, 2");
      this.hintArea = GuiUtil.getDefaultTextarea();
      this.hintArea.setText(TextUtil.t("ui.buyslave.introduction"));
      this.hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
      TranslucentPanel controlPanel = new TranslucentPanel();
      this.add(controlPanel, "4, 3, fill, fill");
      controlPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC})
      );
      controlPanel.add(this.hintArea, "1, 1, fill, fill");
      JButton btnHire = new JButton("Buy");
      btnHire.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Object[] arguments = new Object[]{SlaveMarketPanel.this.selectedSlave.getName()};
               int price = 500 + (int)SlaveMarketPanel.this.selectedSlave.calculateValue();
               if (price < 500) {
                  price = 500;
               }

               if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSLAVES)) {
                  price /= 2;
               }

               if (SlaveMarketPanel.this.selectedSlave != null && Jasbro.getInstance().getData().canAfford(price)) {
                  SlaveMarketPanel.this.selectedSlave.setOwnership(Ownership.OWNED);
                  Jasbro.getInstance().getData().getCharacters().add(SlaveMarketPanel.this.selectedSlave);
                  Jasbro.getInstance().getData().spendMoney(price, TextUtil.t("slavemarket.bought", arguments));
                  Jasbro.getInstance().getData().getSlaveMarket().getSlaves().remove(SlaveMarketPanel.this.selectedSlave);
                  new MessageScreen(
                     TextUtil.t("ui.buyslave.bought", SlaveMarketPanel.this.selectedSlave),
                     ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, SlaveMarketPanel.this.selectedSlave),
                     SlaveMarketPanel.this.selectedSlave.getBackground()
                  );
                  Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, SlaveMarketPanel.this.selectedSlave));
                  SlaveMarketPanel.this.selectedSlave = null;
                  SlaveMarketPanel.this.slaveImage.setImage(null);
                  SlaveMarketPanel.this.initHireList();
               } else {
                  new MessageScreen(
                     TextUtil.t("ui.buyslave.cantafford", SlaveMarketPanel.this.selectedSlave),
                     ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, SlaveMarketPanel.this.selectedSlave),
                     SlaveMarketPanel.this.selectedSlave.getBackground()
                  );
               }
            }
         }
      );
      btnHire.setFont(GuiUtil.DEFAULTBOLDFONT);
      controlPanel.add(btnHire, "1, 2, center, bottom");
      this.initHireList();
   }

   public void initHireList() {
      MouseListener ml = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            this.mouseAction(e);
         }

         public void mouseAction(MouseEvent e) {
            if (!e.isConsumed()) {
               e.consume();
               CharacterShortView shortView = (CharacterShortView)e.getSource();
               SlaveMarketPanel.this.selectedSlave = shortView.getCharacter();
               List<ImageTag> imageTags = SlaveMarketPanel.this.selectedSlave.getBaseTags();
               imageTags.add(0, ImageTag.NAKED);
               imageTags.add(1, ImageTag.CLEANED);
               SlaveMarketPanel.this.slaveImage
                  .setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, SlaveMarketPanel.this.selectedSlave.getImages()));
               if (SlaveMarketPanel.this.selectedSlave != null) {
                  String name = SlaveMarketPanel.this.selectedSlave.getName();
                  int price = 500 + (int)SlaveMarketPanel.this.selectedSlave.calculateValue();
                  if (price < 500) {
                     price = 500;
                  }

                  if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSLAVES)) {
                     price /= 2;
                  }

                  SlaveMarketPanel.this.hintArea.setText(TextUtil.t("ui.buyslave.costText", name, price));
               }

               SlaveMarketPanel.this.repaint();
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
