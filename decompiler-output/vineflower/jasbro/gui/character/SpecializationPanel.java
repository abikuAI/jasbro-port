package jasbro.gui.character;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.market.CharacterSchool;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.AttributePanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SpecializationPanel extends TranslucentPanel {
   private Charakter character;
   private SpecializationType specialisationType;
   private FormLayout layout;
   private Map<AttributeType, AttributePanel> panelMap;
   private JPanel warnPanel;
   private MyImage warning;

   public SpecializationPanel(SpecializationType specializationType, Charakter character) {
      this.specialisationType = specializationType;
      this.character = character;
      this.panelMap = new HashMap<>();
      this.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
      this.setBorder(GuiUtil.DEFAULTBORDER);
      this.layout = new FormLayout(
         new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(3)")},
         new RowSpec[]{RowSpec.decode("default:grow")}
      );
      this.setLayout(this.layout);
      this.setPreferredSize(null);
      this.getPreferredSize().width = 1;
      this.getMinimumSize().width = 200;
      this.init();
   }

   public void init() {
      this.removeAll();
      JLabel label = new JLabel(this.specialisationType.getText());
      this.add(label, "1, 1, fill, top");
      label.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
      label.setOpaque(false);
      this.warnPanel = new JPanel();
      this.warnPanel.setOpaque(false);
      this.add(this.warnPanel, "2, 1, fill, fill");
      this.warnPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));

      for (int i = 0; i < this.specialisationType.getAssociatedAttributes().size(); i++) {
         AttributeType attributeType = this.specialisationType.getAssociatedAttributes().get(i);
         Attribute attribute = this.character.getAttribute(attributeType);
         AttributePanel attributePanel = new AttributePanel(attribute);
         this.layout.appendRow(RowSpec.decode("default:grow"));
         this.add(attributePanel, "1," + (i + 2) + ", 3, 1, fill, top");
         this.panelMap.put(attributeType, attributePanel);
      }

      this.update();
   }

   @Override
   public void update() {
      this.warnPanel.removeAll();
      boolean maxed = false;

      for (int i = 0; i < this.specialisationType.getAssociatedAttributes().size(); i++) {
         AttributeType attributeType = this.specialisationType.getAssociatedAttributes().get(i);
         this.panelMap.get(attributeType).update();
         if (this.character.getAttribute(attributeType).isMaxed()) {
            maxed = true;
         }
      }

      if (this.specialisationType == SpecializationType.FIGHTER) {
         float damage = (int)(this.character.getDamage() * 100.0F) / 100.0F;
         Object[] arguments = new Object[]{
            damage,
            this.character.getArmorValue(),
            this.character.getArmor(),
            this.character.getCritChance(),
            this.character.getCritDamageBonus(),
            this.character.getBlockChance(),
            this.character.getBlockAmount(),
            this.character.getDodge()
         };
         this.setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("combatDataTooltip", arguments)));
      } else if (this.specialisationType == SpecializationType.THIEF) {
         Object[] arguments = new Object[]{this.character.getStealChance(), this.character.getStealAmountModifier(), this.character.getStealItemChance()};
         this.setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("thiefDataTooltip", arguments)));
      } else if (this.specialisationType == SpecializationType.BARTENDER) {
         float f = 10 + this.character.getFinalValue(SpecializationAttribute.BARTENDING) / 3;
         if (this.character.getTraits().contains(Trait.MULTITASKING)) {
            f += this.character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 4;
         }

         int i = (int)f;
         Object[] arguments = new Object[]{i};
         this.setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("bartenderDataTooltip", arguments)));
      }

      if (maxed) {
         this.warning = new MyImage(new ImageData("images/icons/Exclamation_Mark.png"));
         this.warning.setPreferredSize(null);
         if (!this.specialisationType.isTeachable()) {
            this.warning.setToolTipText(TextUtil.t("school.attributeMaxed.cantTeach"));
         } else {
            Jasbro.getThreadpool().execute(new SpecializationPanel.SchoolShortcutRunnable());
         }

         this.warnPanel.add(this.warning, "1,1, fill, fill");
      } else {
         this.warning = null;
      }
   }

   private class SchoolShortcutRunnable implements Runnable {
      private SchoolShortcutRunnable() {
      }

      @Override
      public void run() {
         MyImage warning = SpecializationPanel.this.warning;
         final CharacterSchool characterSchool = new CharacterSchool();

         for (CharacterSchool.Training training : characterSchool.getTrainingOpportunities(SpecializationPanel.this.character)) {
            if (training instanceof CharacterSchool.SpecializationTraining) {
               final CharacterSchool.SpecializationTraining specializationTraining = (CharacterSchool.SpecializationTraining)training;
               if (specializationTraining.getSpecializationType() == SpecializationPanel.this.specialisationType) {
                  warning.setToolTipText(
                     TextUtil.htmlPreformatted(
                        TextUtil.t("school.attributeMaxed")
                           + "\n\n"
                           + specializationTraining.getName()
                           + "\n"
                           + specializationTraining.getDescription()
                           + "\n"
                           + TextUtil.t("stats.money", specializationTraining.getPrice())
                     )
                  );
                  if (specializationTraining.fulfillsRequirements() && Jasbro.getInstance().getData().getMoney() >= specializationTraining.getPrice()) {
                     warning.addMouseListener(
                        new MouseAdapter() {
                           @Override
                           public void mouseClicked(MouseEvent e) {
                              characterSchool.getTrainingOpportunitiesHideUnavailable(SpecializationPanel.this.character);
                              String text = TextUtil.t("school.attributeMaxed.shortCut", SpecializationPanel.this.character)
                                 + "\n\n"
                                 + specializationTraining.getName()
                                 + "\n"
                                 + specializationTraining.getDescription()
                                 + "\n"
                                 + TextUtil.t("stats.money", specializationTraining.getPrice());
                              int confirm = JOptionPane.showConfirmDialog(SpecializationPanel.this, text, "fire", 0);
                              if (confirm == 0) {
                                 specializationTraining.apply();
                                 SpecializationPanel.this.update();
                                 SpecializationPanel.this.repaint();
                              }
                           }
                        }
                     );
                  }
                  break;
               }
            }
         }
      }
   }
}
