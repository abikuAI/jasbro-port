package jasbro.gui.objects.menus;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.ControlData;
import jasbro.gui.MyPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import javax.swing.JLabel;

public class MyInfoPanel extends MyPanel {
   private JLabel controlLabel;
   private JLabel moneyLabel;
   private MyImage controlIcon;
   private JLabel dayLabel;

   public MyInfoPanel() {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               ColumnSpec.decode("1dlu:grow"),
               FormFactory.DEFAULT_COLSPEC,
               ColumnSpec.decode("10dlu"),
               FormFactory.RELATED_GAP_COLSPEC,
               FormFactory.DEFAULT_COLSPEC,
               ColumnSpec.decode("10dlu"),
               FormFactory.RELATED_GAP_COLSPEC,
               FormFactory.DEFAULT_COLSPEC
            },
            new RowSpec[]{RowSpec.decode("default:grow")}
         )
      );
      this.controlLabel = new JLabel();
      this.add(this.controlLabel, "2, 1");
      this.controlLabel.setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("CONTROL.description")));
      this.controlIcon = new MyImage(new ImageData("images/icons/control.png"));
      this.add(this.controlIcon, "3, 1, fill, fill");
      this.moneyLabel = new JLabel();
      this.add(this.moneyLabel, "5, 1");
      MyImage myImage = new MyImage(new ImageData("images/icons/cent.png"));
      this.add(myImage, "6, 1, fill, fill");
      this.dayLabel = new JLabel();
      this.add(this.dayLabel, "8, 1");
      this.update();
   }

   @Override
   public void update() {
      GameData data = Jasbro.getInstance().getData();
      if (data != null) {
         if (!data.getEventManager().isShiftInProgress()) {
            ControlData controlData = data.calculateControl();
            String controlText = TextUtil.t("ui.control", controlData.getControlUsed(), controlData.getControlGenerated());
            if (!this.controlLabel.getText().equals(controlText)) {
               this.controlLabel.setText(controlText);
            }

            String dayText = TextUtil.t("ui.day", data.getDay());
            if (!this.dayLabel.getText().equals(dayText)) {
               this.dayLabel.setText(dayText);
            }
         }

         String moneyText = TextUtil.t("ui.gold", data.getMoney());
         if (!this.moneyLabel.equals(moneyText)) {
            this.moneyLabel.setText(moneyText);
         }

         if (!this.isVisible()) {
            this.setVisible(true);
            this.getParent().validate();
         }
      } else {
         this.setVisible(false);
      }

      this.repaint();
   }
}
