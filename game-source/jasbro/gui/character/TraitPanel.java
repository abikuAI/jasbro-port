package jasbro.gui.character;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

public class TraitPanel extends TranslucentPanel {
   private Charakter character;

   public TraitPanel(Charakter character) {
      this.character = character;
      this.update();
      this.setPreferredSize(null);
   }

   @Override
   public void update() {
      this.removeAll();
      List<Trait> traits = new ArrayList<>();

      for (Trait trait : this.character.getTraits()) {
         if (!trait.isPerk()) {
            traits.add(trait);
         }
      }

      if (traits.size() > 0) {
         FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("default:none")});
         this.setLayout(layout);
         JLabel label = new JLabel(TextUtil.t("ui.traits"));
         label.setFont(GuiUtil.DEFAULTBOLDFONT.deriveFont(GuiUtil.DEFAULTBOLDFONT.getSize() + 1.0F));
         this.add(label, "1, 1, left, top");
         int i = 0;

         for (Trait trait : traits) {
            layout.appendRow(RowSpec.decode("min:none"));
            JLabel traitLabel = new JLabel(trait.getText());
            traitLabel.setToolTipText(trait.getDescription(this.character));
            traitLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
            this.add(traitLabel, "1," + (i + 2) + ", left, top");
            i++;
         }

         this.setVisible(true);
      } else {
         this.setVisible(false);
      }
   }
}
