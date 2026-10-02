package jasbro.gui.character;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.AttributePanel;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.perks.PerksPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CharacterScreenInfoPanel extends TranslucentPanel {
   private Charakter character;
   private MyButton perkButton;
   private JLabel typeLabel;
   private JLabel genderLabel;
   private JLabel fameLabel;

   public CharacterScreenInfoPanel(Charakter characterTmp) {
      this.character = characterTmp;
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               RowSpec.decode("default:grow"),
               RowSpec.decode("default:grow"),
               RowSpec.decode("default:grow"),
               FormFactory.PREF_ROWSPEC,
               FormFactory.PREF_ROWSPEC,
               FormFactory.PREF_ROWSPEC,
               RowSpec.decode("default:grow"),
               RowSpec.decode("default:grow"),
               RowSpec.decode("default:grow"),
               RowSpec.decode("default:grow")
            }
         )
      );
      this.setPreferredSize(null);
      JLabel label = new JLabel(TextUtil.t("type"));
      label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.add(label, "1, 1");
      this.typeLabel = new JLabel();
      this.typeLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.add(this.typeLabel, "2, 1, right, default");
      label = new JLabel(TextUtil.t("gender"));
      label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.add(label, "1, 2");
      this.genderLabel = new JLabel();
      this.genderLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.add(this.genderLabel, "2, 2, right, default");
      label = new JLabel(TextUtil.t("fame"));
      label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.add(label, "1, 3");
      this.fameLabel = new JLabel();
      this.fameLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.add(this.fameLabel, "2, 3, right, default");
      if (this.character.getCounter().get(CharacterStuffCounter.CounterNames.CHILDREN) > 0L) {
         label = new JLabel(TextUtil.t("ui.children"));
         label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.add(label, "1, 4");
         JLabel childLabel = new JLabel("" + this.character.getCounter().get(CharacterStuffCounter.CounterNames.CHILDREN));
         childLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.add(childLabel, "2, 4, right, default");
      }

      if (this.character.getAgeProgressionData().getNameMother() != null) {
         label = new JLabel(TextUtil.t("ui.mother"));
         label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.add(label, "1, 5");
         JLabel childLabel = new JLabel(this.character.getAgeProgressionData().getNameMother());
         childLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.add(childLabel, "2, 5, right, default");
      }

      if (this.character.getAgeProgressionData().getNameFather() != null) {
         label = new JLabel(TextUtil.t("ui.father"));
         label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.add(label, "1, 6");
         JLabel childLabel = new JLabel(this.character.getAgeProgressionData().getNameFather());
         childLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.add(childLabel, "2, 6, right, default");
      }

      AttributePanel attributePanel = new AttributePanel(this.character.getAttribute(EssentialAttributes.HEALTH));
      this.add(attributePanel, "1, 7, 2, 1, fill, top");
      attributePanel.setNormalSize();
      attributePanel.update();
      attributePanel = new AttributePanel(this.character.getAttribute(EssentialAttributes.ENERGY));
      this.add(attributePanel, "1, 8, 2, 1, fill, top");
      attributePanel.setNormalSize();
      attributePanel.update();
      attributePanel = new AttributePanel(this.character.getAttribute(EssentialAttributes.MOTIVATION));
      this.add(attributePanel, "1, 9, 2, 1, fill, top");
      attributePanel.setNormalSize();
      attributePanel.update();
      if (Jasbro.getInstance().getData().getCharacters().contains(this.character)) {
         this.perkButton = new MyButton("", new ImageData("images/icons/perks/button75892304.png"), new ImageData("images/icons/perks/button76812194.png"));
         this.perkButton.setMinimumSize(new Dimension(-1, 30));
         this.add(this.perkButton, "1, 10, 2, 1, fill, fill");
         this.perkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JFrame frame = Jasbro.getInstance().getGui();
               JDialog dialog = new JDialog(Jasbro.getInstance().getGui(), "Perks", true);
               dialog.getContentPane().add(new PerksPanel(CharacterScreenInfoPanel.this.character));
               dialog.setResizable(false);
               dialog.pack();
               dialog.setLocationRelativeTo(frame);
               dialog.validate();
               dialog.setVisible(true);
            }
         });
      }

      for (Component component : this.getComponents()) {
         component.setFont(component.getFont().deriveFont(component.getFont().getSize() + 4.0F));
      }

      this.update();
   }

   @Override
   public void update() {
      this.typeLabel.setText(this.character.getType().getText());
      this.genderLabel.setText(this.character.getGender().getText());
      this.fameLabel.setText(TextUtil.t("formatted", this.character.getFame().getFameCharacter().getText()));
      this.fameLabel.setToolTipText(this.character.getFame().getFame() + "");
      if (this.perkButton != null) {
         int amountUnspent = this.character.getUnspentPerkPoints();
         if (amountUnspent > 0) {
            Object[] arguments = new Object[]{amountUnspent};
            this.perkButton.setText(TextUtil.t("ui.perkButtonUnspent", arguments));
         } else {
            this.perkButton.setText(TextUtil.t("ui.perkButton"));
         }

         if (this.character.getSkillTrees().size() == 0) {
            this.perkButton.setEnabled(false);
         } else {
            this.perkButton.setEnabled(true);
         }
      }
   }
}
