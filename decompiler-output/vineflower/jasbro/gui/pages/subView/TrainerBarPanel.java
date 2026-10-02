package jasbro.gui.pages.subView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.AuctionHouse;
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

public class TrainerBarPanel extends JPanel {
   private JPanel hirePanel;
   private MyImage trainerImage;
   private Charakter selectedTrainer = null;

   public TrainerBarPanel() {
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
      JLabel lblBuyGirl = new JLabel(TextUtil.t("ui.hiretrainer.title"));
      lblBuyGirl.setFont(new Font("Tahoma", 1, 18));
      translucentPanel.add(lblBuyGirl, "1, 1");
      this.hirePanel = new JPanel();
      this.hirePanel.setOpaque(false);
      translucentPanel.add(this.hirePanel, "1, 2, fill, fill");
      this.trainerImage = new MyImage();
      this.add(this.trainerImage, "4, 1, 1, 2");
      JTextArea hintArea = GuiUtil.getDefaultTextarea();
      hintArea.setText(TextUtil.t("ui.hiretrainer.hint"));
      hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
      TranslucentPanel controlPanel = new TranslucentPanel();
      this.add(controlPanel, "4, 3, fill, fill");
      controlPanel.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC})
      );
      controlPanel.add(hintArea, "1, 1, fill, fill");
      JButton btnHire = new JButton("Hire");
      btnHire.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (TrainerBarPanel.this.selectedTrainer != null) {
                  TrainerBarPanel.this.selectedTrainer.setOwnership(Ownership.CONTRACT);
                  Jasbro.getInstance().getData().getCharacters().add(TrainerBarPanel.this.selectedTrainer);
                  Jasbro.getInstance().getData().getAuctionHouse().getTrainers().remove(TrainerBarPanel.this.selectedTrainer);
                  new MessageScreen(
                     TextUtil.t("ui.hiretrainer.hired", TrainerBarPanel.this.selectedTrainer),
                     ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, TrainerBarPanel.this.selectedTrainer),
                     TrainerBarPanel.this.selectedTrainer.getBackground()
                  );
                  Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, TrainerBarPanel.this.selectedTrainer));
                  TrainerBarPanel.this.selectedTrainer = null;
                  TrainerBarPanel.this.trainerImage.setImage(null);
                  TrainerBarPanel.this.initHireList();
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
               TrainerBarPanel.this.selectedTrainer = shortView.getCharacter();
               List<ImageTag> imageTags = TrainerBarPanel.this.selectedTrainer.getBaseTags();
               imageTags.add(0, ImageTag.STANDARD);
               imageTags.add(1, ImageTag.CLEANED);
               TrainerBarPanel.this.trainerImage
                  .setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, TrainerBarPanel.this.selectedTrainer.getImages()));
               TrainerBarPanel.this.repaint();
            }
         }
      };
      this.hirePanel.removeAll();
      AuctionHouse auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();

      for (Charakter character : auctionHouse.getTrainers()) {
         CharacterShortView shortView = new CharacterShortView(character, false);
         this.hirePanel.add(shortView);
         shortView.addMouseListener(ml);
      }
   }
}
