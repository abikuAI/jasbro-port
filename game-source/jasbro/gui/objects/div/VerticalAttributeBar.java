package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.gui.DelegateMouseListener;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class VerticalAttributeBar extends JPanel {
   private Attribute attribute;
   private JProgressBar attributeBar;
   private MyImage attributeIcon;

   public VerticalAttributeBar() {
      this.initComponents();
   }

   public VerticalAttributeBar(Attribute attribute) {
      this();
      this.setAttribute(attribute);
   }

   public Attribute getAttribute() {
      return this.attribute;
   }

   public void setAttribute(Attribute attribute) {
      this.attribute = attribute;
      if (attribute != null) {
         this.attributeBar.setMaximum(attribute.getMaxValue());
         this.attributeBar.setValue(attribute.getValue());
         this.setToolTipText(attribute.getNameResolved() + ": " + attribute.getValue() + " of " + attribute.getMaxValue());
         if (attribute.getAttributeName().equals(EssentialAttributes.HEALTH.toString())) {
            this.attributeBar.setStringPainted(true);
            this.attributeBar.setForeground(Color.RED);
         } else if (attribute.getAttributeName().equals(EssentialAttributes.MOTIVATION.toString())) {
            this.attributeBar.setStringPainted(true);
            this.attributeBar.setForeground(new Color(0, 128, 128));
         } else {
            this.attributeBar.setStringPainted(true);
            this.attributeBar.setForeground(new Color(128, 0, 128));
         }

         if (ConfigHandler.isShowNumbersOnBars()) {
            this.attributeBar.setString("" + attribute.getValue());
         } else {
            this.attributeBar.setString("");
         }

         this.attributeIcon.setImage(attribute.getIcon());
         if (attribute.getAttributeType() instanceof EssentialAttributes) {
            this.attributeIcon.setToolTipText(TextUtil.htmlPreformatted(((EssentialAttributes)attribute.getAttributeType()).getDescription()));
         }
      }
   }

   private void initComponents() {
      this.setOpaque(false);
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")},
            new RowSpec[]{
               FormFactory.NARROW_LINE_GAP_ROWSPEC,
               RowSpec.decode("1dlu:grow(11)"),
               FormFactory.NARROW_LINE_GAP_ROWSPEC,
               RowSpec.decode("1dlu:grow"),
               FormFactory.NARROW_LINE_GAP_ROWSPEC
            }
         )
      );
      this.attributeBar = new JProgressBar(1);
      this.add(this.attributeBar, "1, 2, center, fill");
      this.attributeIcon = new MyImage();
      this.add(this.attributeIcon, "1, 4, fill, fill");
      DelegateMouseListener listener = new DelegateMouseListener();
      this.addMouseMotionListener(listener);
      this.addMouseListener(listener);
      this.setOpaque(false);
      this.attributeBar.setOpaque(false);
   }
}
