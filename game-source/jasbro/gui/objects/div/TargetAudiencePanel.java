package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Gender;
import jasbro.game.housing.House;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TargetAudiencePanel extends TranslucentPanel {
   private House house;
   private JSlider maleAudienceSlider;
   private JSlider femaleAudienceSlider;
   private JSlider futaAudienceSlider;
   private boolean valueUpdating;

   public TargetAudiencePanel(House house) {
      this.setPreferredSize(null);
      this.house = house;
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC},
            new RowSpec[]{
               FormFactory.RELATED_GAP_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow"),
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow"),
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow"),
               FormFactory.RELATED_GAP_ROWSPEC
            }
         )
      );
      JLabel lblNewLabel = new JLabel(TextUtil.t("ui.targetAudience"));
      lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
      this.add(lblNewLabel, "2, 2");
      JLabel lblNewLabel_1 = new JLabel(TextUtil.t("ui.percentMale"));
      this.add(lblNewLabel_1, "2, 3");
      this.maleAudienceSlider = new JSlider();
      this.maleAudienceSlider.setOpaque(false);
      this.maleAudienceSlider.setMinimumSize(new Dimension(1, 1));
      this.maleAudienceSlider.setMinimum(0);
      this.maleAudienceSlider.setMaximum(100);
      this.maleAudienceSlider.setMajorTickSpacing(10);
      this.maleAudienceSlider.setPaintTicks(true);
      this.maleAudienceSlider.setPaintLabels(true);
      this.maleAudienceSlider.setPaintTrack(true);
      this.maleAudienceSlider.setSnapToTicks(true);
      this.maleAudienceSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            if (!TargetAudiencePanel.this.valueUpdating) {
               if (TargetAudiencePanel.this.maleAudienceSlider.getValue() + 7 <= TargetAudiencePanel.this.house.getSpawnData().getPercentMale()) {
                  TargetAudiencePanel.this.house.getSpawnData().decreaseChance(Gender.MALE);
               } else if (TargetAudiencePanel.this.maleAudienceSlider.getValue() - 7 >= TargetAudiencePanel.this.house.getSpawnData().getPercentMale()) {
                  TargetAudiencePanel.this.house.getSpawnData().increaseChance(Gender.MALE);
               }

               TargetAudiencePanel.this.update();
            }
         }
      });
      this.add(this.maleAudienceSlider, "2, 4, fill, fill");
      JLabel lblNewLabel_2 = new JLabel(TextUtil.t("ui.percentFemale"));
      this.add(lblNewLabel_2, "2, 5");
      this.femaleAudienceSlider = new JSlider();
      this.femaleAudienceSlider.setOpaque(false);
      this.femaleAudienceSlider.setMinimumSize(new Dimension(1, 1));
      this.femaleAudienceSlider.setMinimum(0);
      this.femaleAudienceSlider.setMaximum(100);
      this.femaleAudienceSlider.setMajorTickSpacing(10);
      this.femaleAudienceSlider.setPaintTicks(true);
      this.femaleAudienceSlider.setPaintLabels(true);
      this.femaleAudienceSlider.setPaintTrack(true);
      this.femaleAudienceSlider.setSnapToTicks(true);
      this.femaleAudienceSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            if (!TargetAudiencePanel.this.valueUpdating) {
               if (TargetAudiencePanel.this.femaleAudienceSlider.getValue() + 7 <= TargetAudiencePanel.this.house.getSpawnData().getPercentFemale()) {
                  TargetAudiencePanel.this.house.getSpawnData().decreaseChance(Gender.FEMALE);
               } else if (TargetAudiencePanel.this.femaleAudienceSlider.getValue() - 7 >= TargetAudiencePanel.this.house.getSpawnData().getPercentFemale()) {
                  TargetAudiencePanel.this.house.getSpawnData().increaseChance(Gender.FEMALE);
               }

               TargetAudiencePanel.this.update();
            }
         }
      });
      this.add(this.femaleAudienceSlider, "2, 6");
      JLabel lblNewLabel_3 = new JLabel(TextUtil.t("ui.percentFuta"));
      this.add(lblNewLabel_3, "2, 7");
      this.futaAudienceSlider = new JSlider();
      this.futaAudienceSlider.setOpaque(false);
      this.futaAudienceSlider.setMinimumSize(new Dimension(1, 1));
      this.futaAudienceSlider.setMinimum(0);
      this.futaAudienceSlider.setMaximum(100);
      this.futaAudienceSlider.setMajorTickSpacing(10);
      this.futaAudienceSlider.setPaintTicks(true);
      this.futaAudienceSlider.setPaintLabels(true);
      this.futaAudienceSlider.setPaintTrack(true);
      this.futaAudienceSlider.setSnapToTicks(true);
      this.futaAudienceSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            if (!TargetAudiencePanel.this.valueUpdating) {
               if (TargetAudiencePanel.this.futaAudienceSlider.getValue() + 7 <= TargetAudiencePanel.this.house.getSpawnData().getPercentFuta()) {
                  TargetAudiencePanel.this.house.getSpawnData().decreaseChance(Gender.FUTA);
               } else if (TargetAudiencePanel.this.futaAudienceSlider.getValue() - 7 >= TargetAudiencePanel.this.house.getSpawnData().getPercentFuta()) {
                  TargetAudiencePanel.this.house.getSpawnData().increaseChance(Gender.FUTA);
               }

               TargetAudiencePanel.this.update();
            }
         }
      });
      this.add(this.futaAudienceSlider, "2, 8");
      this.update();
   }

   @Override
   public void update() {
      this.valueUpdating = true;
      this.maleAudienceSlider.setValue(this.house.getSpawnData().getPercentMale());
      this.femaleAudienceSlider.setValue(this.house.getSpawnData().getPercentFemale());
      this.futaAudienceSlider.setValue(this.house.getSpawnData().getPercentFuta());
      this.valueUpdating = false;
      this.repaint();
   }
}
