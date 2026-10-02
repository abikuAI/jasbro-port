package jasbro.util.enemyEditor;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.NpcFileLoader;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.EditorInterface;
import jasbro.util.ImageListPanel;
import jasbro.util.ImageTagEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnemyEditorPanel extends JPanel implements EditorInterface {
   private static final Logger log = LogManager.getLogger(EnemyEditorPanel.class);
   private ComplexEnemyTemplate enemyTemplate;
   private MyImage imageDisplay;
   private ImageTagEditor imageTagEditor;
   private boolean on = false;
   private ImageListPanel imageListPanel;
   private JButton saveButton;
   private EnemyDataEditorPanel enemyDataEditorPanel;

   public EnemyEditorPanel() {
      this.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("180dlu:none"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")})
      );
      JSplitPane splitPane = new JSplitPane();
      splitPane.setOrientation(0);
      this.add(splitPane, "1, 1, fill, fill");
      EnemyListPanel enemyListPanel = new EnemyListPanel(this);
      splitPane.setLeftComponent(enemyListPanel);
      this.imageListPanel = new ImageListPanel(this);
      splitPane.setRightComponent(this.imageListPanel);
      splitPane.setDividerLocation(300);
      JSplitPane splitPane_1 = new JSplitPane();
      splitPane_1.setOrientation(0);
      this.add(splitPane_1, "2, 1, fill, fill");
      this.imageDisplay = new MyImage();
      splitPane_1.setLeftComponent(this.imageDisplay);
      this.imageDisplay.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
      this.saveButton = new JButton("Save");
      this.imageDisplay.add(this.saveButton, "1, 1, right, bottom");
      this.saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (EnemyEditorPanel.this.enemyTemplate != null) {
               NpcFileLoader.getInstance().save(EnemyEditorPanel.this.enemyTemplate);
            }
         }
      });
      JSplitPane splitPane_2 = new JSplitPane();
      splitPane_1.setRightComponent(splitPane_2);
      this.imageTagEditor = new ImageTagEditor();
      splitPane_2.setLeftComponent(this.imageTagEditor);
      this.enemyDataEditorPanel = new EnemyDataEditorPanel();
      splitPane_2.setRightComponent(this.enemyDataEditorPanel);
      splitPane_2.setDividerLocation(650);
      splitPane_1.setDividerLocation(450);
   }

   public ComplexEnemyTemplate getEnemyTemplate() {
      return this.enemyTemplate;
   }

   public void setEnemyTemplate(ComplexEnemyTemplate enemyTemplate) {
      this.enemyTemplate = enemyTemplate;
      this.enemyDataEditorPanel.setEnemyTemplate(enemyTemplate);
      this.imageListPanel.setImageObject(enemyTemplate);
      if (this.enemyTemplate.getImages().size() > 0) {
         this.changeCurrentImage(enemyTemplate.getImages().get(0));
      } else {
         this.setNoImageSelected(true);
      }
   }

   @Override
   public void setNoImageSelected(boolean selected) {
      this.imageTagEditor.setEnabled(!this.on);
      if (this.on) {
         this.imageDisplay.setImage(null);
      }

      this.repaint();
   }

   @Override
   public void changeCurrentImage(ImageData selectedValue) {
      try {
         this.imageTagEditor.setImage(this.imageListPanel.getSelectedImage(), null);
         this.imageDisplay.setImage(this.imageListPanel.getSelectedImage());
         this.repaint();
      } catch (Exception e) {
         log.error("Error on changing image", e);
      }
   }
}
