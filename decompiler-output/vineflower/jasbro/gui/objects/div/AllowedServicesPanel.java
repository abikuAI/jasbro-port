package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.business.AllowedServices;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class AllowedServicesPanel extends TranslucentPanel {
   private Charakter character;
   private FormLayout layout;

   public AllowedServicesPanel(Charakter character) {
      this.setPreferredSize(null);
      this.character = character;
      this.layout = new FormLayout(
         new ColumnSpec[]{
            ColumnSpec.decode("2dlu"),
            ColumnSpec.decode("default:grow"),
            ColumnSpec.decode("2dlu"),
            ColumnSpec.decode("default:grow"),
            ColumnSpec.decode("2dlu")
         },
         new RowSpec[]{
            FormFactory.NARROW_LINE_GAP_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC,
            FormFactory.DEFAULT_ROWSPEC,
            FormFactory.NARROW_LINE_GAP_ROWSPEC
         }
      );
      this.setLayout(this.layout);
      this.init();
   }

   public void init() {
      Border defaultCheckboxBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
      JLabel lblNewLabel = new JLabel(TextUtil.t("ui.allowedservices"));
      lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
      this.add(lblNewLabel, "2, 2, 3, 1");
      final JCheckBox serviceMenCheckbox = new JCheckBox(TextUtil.t("ui.serviceMen"));
      this.add(serviceMenCheckbox, "2, 3, 3, 1");
      serviceMenCheckbox.setSelected(this.character.getAllowedServices().isServiceMales());
      serviceMenCheckbox.setOpaque(false);
      serviceMenCheckbox.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      serviceMenCheckbox.setBorder(defaultCheckboxBorder);
      serviceMenCheckbox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AllowedServicesPanel.this.character.getAllowedServices().setServiceMales(serviceMenCheckbox.isSelected());
         }
      });
      final JCheckBox serviceWomenCheckbox = new JCheckBox(TextUtil.t("ui.serviceWomen"));
      this.add(serviceWomenCheckbox, "2, 4, 3, 1");
      serviceWomenCheckbox.setSelected(this.character.getAllowedServices().isServiceFemales());
      serviceWomenCheckbox.setOpaque(false);
      serviceWomenCheckbox.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      serviceWomenCheckbox.setBorder(defaultCheckboxBorder);
      serviceWomenCheckbox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AllowedServicesPanel.this.character.getAllowedServices().setServiceFemales(serviceWomenCheckbox.isSelected());
         }
      });
      final JCheckBox serviceFutasCheckbox = new JCheckBox(TextUtil.t("ui.serviceFutas"));
      this.add(serviceFutasCheckbox, "2, 5, 3, 1");
      serviceFutasCheckbox.setSelected(this.character.getAllowedServices().isServiceFutas());
      serviceFutasCheckbox.setOpaque(false);
      serviceFutasCheckbox.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      serviceFutasCheckbox.setBorder(defaultCheckboxBorder);
      serviceFutasCheckbox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AllowedServicesPanel.this.character.getAllowedServices().setServiceFutas(serviceFutasCheckbox.isSelected());
         }
      });
      JButton saveButton = new JButton(TextUtil.t("ui.save"));
      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setToolTipText(TextUtil.t("ui.preferences.tooltipsave"));
      saveButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Jasbro.getInstance()
                  .getData()
                  .getDefaultPreferences()
                  .setAllowedServices(AllowedServicesPanel.this.character.getGender(), AllowedServicesPanel.this.character.getAllowedServices());
            }
         }
      );
      this.add(saveButton, "2, 6");
      JButton loadButton = new JButton(TextUtil.t("ui.load"));
      loadButton.setMargin(new Insets(0, 0, 0, 0));
      loadButton.setToolTipText(TextUtil.t("ui.preferences.tooltipsave"));
      loadButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AllowedServicesPanel.this.character
                  .setAllowedServices(
                     new AllowedServices(
                        Jasbro.getInstance().getData().getDefaultPreferences().getAllowedServices(AllowedServicesPanel.this.character.getGender())
                     )
                  );
               AllowedServicesPanel.this.removeAll();
               AllowedServicesPanel.this.init();
               AllowedServicesPanel.this.validate();
               AllowedServicesPanel.this.repaint();
            }
         }
      );
      this.add(loadButton, "4, 6");
      List<Sextype> possibleSextypes = Sextype.getPossibleSextypes(this.character.getGender());

      for (int i = 0; i < possibleSextypes.size(); i++) {
         final Sextype sextype = possibleSextypes.get(i);
         final JCheckBox checkbox = new JCheckBox(sextype.getText());
         this.layout.insertRow(6 + i, FormFactory.DEFAULT_ROWSPEC);
         this.add(checkbox, "2, " + (6 + i) + ", 3, 1");
         checkbox.setSelected(this.character.getAllowedServices().isAllowed(sextype));
         checkbox.setOpaque(false);
         checkbox.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         checkbox.setBorder(defaultCheckboxBorder);
         checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AllowedServicesPanel.this.character.getAllowedServices().setAllowed(sextype, checkbox.isSelected());
            }
         });
      }
   }
}
