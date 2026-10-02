package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventProtectCharacter;
import jasbro.texts.TextUtil;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectProtectCharacterPanel extends JPanel {
   private WorldEventProtectCharacter worldEventEffect;
   private JTextField textField;

   public EventEffectProtectCharacterPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
      this.setLayout(
         new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")})
      );
      this.worldEventEffect = (WorldEventProtectCharacter)worldEventEffectTmp;
      JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.target"));
      this.add(lblNewLabel, "1, 1, right, fill");
      this.textField = new JTextField();
      this.add(this.textField, "2, 1, fill, default");
      this.textField.setText(this.worldEventEffect.getTarget());
      this.textField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EventEffectProtectCharacterPanel.this.worldEventEffect.setTarget(EventEffectProtectCharacterPanel.this.textField.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EventEffectProtectCharacterPanel.this.worldEventEffect.setTarget(EventEffectProtectCharacterPanel.this.textField.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EventEffectProtectCharacterPanel.this.worldEventEffect.setTarget(EventEffectProtectCharacterPanel.this.textField.getText());
         }
      });
   }
}
