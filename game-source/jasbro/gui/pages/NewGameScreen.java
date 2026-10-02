package jasbro.gui.pages;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.gui.character.CharacterStartDescriptionPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NewGameScreen extends MyImage {
   private MyImage trainerImage;
   private MyImage slaveImage;
   private ImageData background = new ImageData("images/backgrounds/sky.jpg");
   private CharacterBase selectedTrainer;
   private CharacterBase selectedSlave;
   private JPanel descriptionPanel1;
   private JPanel descriptionPanel2;

   public NewGameScreen() {
      this.setBackgroundImage(this.background);
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow(3)"), ColumnSpec.decode("1dlu:grow(3)"), ColumnSpec.decode("1dlu:grow")
            },
            new RowSpec[]{RowSpec.decode("1dlu:grow(3)"), RowSpec.decode("1dlu:grow")}
         )
      );
      JPanel panel_1 = new JPanel();
      panel_1.setOpaque(false);
      this.add(panel_1, "1, 1, 1, 2, fill, fill");
      panel_1.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("20dlu"), RowSpec.decode("1dlu:grow")}));
      JLabel lblNewLabel_1 = new JLabel("Select your Trainer");
      lblNewLabel_1.setBackground(new Color(255, 255, 204));
      lblNewLabel_1.setOpaque(true);
      lblNewLabel_1.setHorizontalAlignment(0);
      panel_1.add(lblNewLabel_1, "1, 1, fill, center");
      JScrollPane scrollPane_1 = new JScrollPane();
      panel_1.add(scrollPane_1, "1, 2, fill, fill");
      final JList<CharacterBase> trainerList = new JList<>();
      trainerList.setModel(new AbstractListModel<CharacterBase>() {
         @Override
         public int getSize() {
            return Jasbro.getInstance().getTrainerBases().size();
         }

         public CharacterBase getElementAt(int index) {
            return Jasbro.getInstance().getTrainerBases().get(index);
         }
      });
      trainerList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (trainerList.getSelectedValue() != NewGameScreen.this.selectedTrainer) {
               NewGameScreen.this.selectedTrainer = trainerList.getSelectedValue();
               ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, NewGameScreen.this.selectedTrainer.getImages());
               NewGameScreen.this.trainerImage.setImage(image);
               NewGameScreen.this.remove(NewGameScreen.this.descriptionPanel1);
               NewGameScreen.this.descriptionPanel1 = new CharacterStartDescriptionPanel(NewGameScreen.this.selectedTrainer);
               NewGameScreen.this.add(NewGameScreen.this.descriptionPanel1, "2, 2, fill, fill");
               NewGameScreen.this.validate();
               NewGameScreen.this.repaint();
            }
         }
      });
      scrollPane_1.setViewportView(trainerList);
      this.trainerImage = new MyImage();
      this.trainerImage.setMaximumSize(new Dimension(9999999, 99999999));
      this.add(this.trainerImage, "2, 1, fill, fill");
      this.slaveImage = new MyImage();
      this.slaveImage.setMaximumSize(new Dimension(999999999, 999999999));
      this.add(this.slaveImage, "3, 1, fill, fill");
      JPanel panel = new JPanel();
      panel.setOpaque(false);
      this.add(panel, "4, 1, 1, 2, fill, fill");
      panel.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("20dlu"), RowSpec.decode("1dlu:grow"), RowSpec.decode("20dlu")}
         )
      );
      JLabel lblNewLabel = new JLabel("Select your first girl");
      lblNewLabel.setBackground(new Color(255, 255, 204));
      lblNewLabel.setOpaque(true);
      lblNewLabel.setHorizontalAlignment(0);
      panel.add(lblNewLabel, "1, 1");
      JScrollPane scrollPane = new JScrollPane();
      panel.add(scrollPane, "1, 2, fill, fill");
      final JList<CharacterBase> slaveList = new JList<>();
      scrollPane.setViewportView(slaveList);
      slaveList.setModel(
         new AbstractListModel<CharacterBase>() {
            private List<CharacterBase> characters = NewGameScreen.this.filterByTrait(
               Jasbro.getInstance().getSlaveBases(), Trait.FORMERNOBLE, Trait.RARESLAVE, Trait.EXTREMELYRARESLAVE, Trait.EXTREMELYRARESLAVE2, Trait.UNSELLABLE
            );

            @Override
            public int getSize() {
               return this.characters.size();
            }

            public CharacterBase getElementAt(int index) {
               return this.characters.get(index);
            }
         }
      );
      slaveList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (slaveList.getSelectedValue() != NewGameScreen.this.selectedSlave) {
               NewGameScreen.this.selectedSlave = slaveList.getSelectedValue();
               ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, NewGameScreen.this.selectedSlave.getImages());
               NewGameScreen.this.slaveImage.setImage(image);
               NewGameScreen.this.remove(NewGameScreen.this.descriptionPanel2);
               NewGameScreen.this.descriptionPanel2 = new CharacterStartDescriptionPanel(NewGameScreen.this.selectedSlave);
               NewGameScreen.this.add(NewGameScreen.this.descriptionPanel2, "3, 2, fill, fill");
               NewGameScreen.this.validate();
               NewGameScreen.this.repaint();
            }
         }
      });
      JButton startbutton = new JButton("Start");
      panel.add(startbutton, "1, 3, fill, center");
      startbutton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().startNewGame(trainerList.getSelectedValue(), slaveList.getSelectedValue());
         }
      });
      this.descriptionPanel1 = new TranslucentPanel();
      this.add(this.descriptionPanel1, "2, 2, fill, fill");
      this.descriptionPanel2 = new TranslucentPanel();
      this.add(this.descriptionPanel2, "3, 2, fill, fill");
      slaveList.setSelectedIndex(0);
      trainerList.setSelectedIndex(0);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, slaveList.getSelectedValue().getImages());
      this.slaveImage.setImage(image);
      image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, trainerList.getSelectedValue().getImages());
      this.trainerImage.setImage(image);
   }

   private List<CharacterBase> filterByTrait(List<CharacterBase> characters, Trait... traits) {
      List<CharacterBase> filteredCharacters = new ArrayList<>(characters.size());

      for (CharacterBase c : characters) {
         boolean hasTrait = false;

         for (Trait t : traits) {
            if (c.getTraits().contains(t)) {
               hasTrait = true;
               break;
            }
         }

         if (!hasTrait) {
            filteredCharacters.add(c);
         }
      }

      return filteredCharacters;
   }

   private List<CharacterBase> filterBySpecialization(List<CharacterBase> characters, SpecializationType... specializations) {
      List<CharacterBase> filteredCharacters = new ArrayList<>(characters.size());

      for (CharacterBase c : characters) {
         boolean hasSpecialization = false;

         for (SpecializationType s : specializations) {
            if (s.equals(c.getInitialSpecialization())) {
               hasSpecialization = true;
               break;
            }
         }

         if (!hasSpecialization) {
            filteredCharacters.add(c);
         }
      }

      return filteredCharacters;
   }
}
