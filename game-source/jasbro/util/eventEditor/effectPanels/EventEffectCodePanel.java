package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCode;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.pictures.ImageData;
import jasbro.util.eventEditor.EventEditorPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EventEffectCodePanel extends JPanel {
   private WorldEventCode worldEventEffect;
   private FormLayout layout;
   private JScrollPane scrollPane;
   private JPanel buttonPanel;

   public EventEffectCodePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
      this.worldEventEffect = (WorldEventCode)worldEventEffectTmp;
      this.layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("5dlu")}, new RowSpec[]{RowSpec.decode("0dlu")});
      if (this.worldEventEffect != null) {
         this.updateLayout(false);
      }

      this.setLayout(this.layout);
      final JTextArea textArea = new JTextArea();
      this.scrollPane = new JScrollPane(textArea);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setText(this.worldEventEffect.getCode());
      textArea.setEditable(true);
      textArea.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EventEffectCodePanel.this.worldEventEffect.setCode(textArea.getText());
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EventEffectCodePanel.this.worldEventEffect.setCode(textArea.getText());
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EventEffectCodePanel.this.worldEventEffect.setCode(textArea.getText());
         }
      });
      this.add(this.scrollPane, "1, 1, fill, fill");
      this.buttonPanel = new JPanel();
      this.add(this.buttonPanel, "2, 1, fill, fill");
      this.buttonPanel
         .setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("default:grow")},
               new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("10dlu"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("10dlu")}
            )
         );
      MyButton smallerButton = new MyButton((String)null, new ImageData("images/icons/arrow_up.png"), new ImageData("images/icons/arrow_up.png"));
      this.buttonPanel.add(smallerButton, "1, 2, fill, fill");
      smallerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EventEffectCodePanel.this.worldEventEffect.setDisplayHeight(EventEffectCodePanel.this.worldEventEffect.getDisplayHeight() - 1);
            EventEffectCodePanel.this.updateLayout(true);
         }
      });
      MyButton biggerButton = new MyButton((String)null, new ImageData("images/icons/arrow_down.png"), new ImageData("images/icons/arrow_down.png"));
      this.buttonPanel.add(biggerButton, "1, 4, fill, fill");
      biggerButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EventEffectCodePanel.this.worldEventEffect.setDisplayHeight(EventEffectCodePanel.this.worldEventEffect.getDisplayHeight() + 1);
            EventEffectCodePanel.this.updateLayout(true);
         }
      });
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            EventEffectCodePanel.this.scrollPane.getVerticalScrollBar().setValue(0);
         }
      });
   }

   public void updateLayout(boolean repaint) {
      this.layout = new FormLayout(
         new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("5dlu")},
         new RowSpec[]{RowSpec.decode(this.worldEventEffect.getDisplayHeight() * 80 + "dlu:grow")}
      );
      this.setLayout(this.layout);
      if (repaint) {
         this.removeAll();
         this.setLayout(this.layout);
         this.add(this.scrollPane, "1, 1, fill, fill");
         this.add(this.buttonPanel, "2, 1, fill, fill");
         EventEditorPanel eventEditorPanel = (EventEditorPanel)SwingUtilities.getAncestorOfClass(EventEditorPanel.class, this);
         eventEditorPanel.validate();
         eventEditorPanel.repaint();
      } else {
         this.setLayout(this.layout);
      }
   }
}
