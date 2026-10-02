/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.objects.div.MyImage;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IconAttributePanel
extends JPanel {
    private Attribute attribute;
    private MyImage attributeIcon;
    private JLabel attributeValue;

    public IconAttributePanel() {
        this.initComponents();
    }

    public IconAttributePanel(Attribute attribute) {
        this();
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
        this.attributeValue.setText(attribute.getValue() + "");
        this.attributeIcon.setImage(attribute.getIcon());
        int bonus = attribute.getBonus();
        Object[] attributes = new Object[]{(int)attribute.getInternValue(), attribute.getMaxValue(), bonus};
        String toolTip = bonus == 0 ? attribute.getNameResolved() + " " + TextUtil.t("ui.attributetooltip", attributes) : attribute.getNameResolved() + " " + TextUtil.t("ui.attributetooltip.bonus", attributes);
        if (attribute.getAttributeType() == BaseAttributeTypes.COMMAND || attribute.getAttributeType() == BaseAttributeTypes.OBEDIENCE) {
            int control = attribute.getCharacter().getControl();
            if (control > 0) {
                toolTip = toolTip + "\n" + TextUtil.t("ui.controlGenerated", control);
            } else if (control < 0) {
                toolTip = toolTip + "\n" + TextUtil.t("ui.controlUsed", -control);
            }
        }
        this.setToolTipText(TextUtil.htmlPreformatted(toolTip));
        if (attribute.getAttributeType() instanceof BaseAttributeTypes) {
            this.attributeIcon.setToolTipText(TextUtil.htmlPreformatted(toolTip + "\n" + ((BaseAttributeTypes)attribute.getAttributeType()).getDescription()));
        }
    }

    public int getMaxFittingFontSize(Font font) {
        int minSize = 0;
        int maxSize = 40;
        int curSize = font.getSize();
        int width = this.attributeValue.getWidth();
        int height = this.attributeValue.getHeight();
        if (width != 0 && height != 0) {
            while (maxSize - minSize > 1) {
                font = font.deriveFont((float)curSize);
                FontMetrics fm = this.getFontMetrics(font);
                int fontWidth = fm.stringWidth(this.attributeValue.getText());
                int fontHeight = fm.getAscent() + fm.getLeading();
                if (fontWidth >= width || fontHeight >= height) {
                    maxSize = curSize;
                    curSize = (maxSize + minSize) / 2;
                    continue;
                }
                minSize = curSize;
                curSize = (minSize + maxSize) / 2;
            }
        }
        return minSize;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (this.attributeValue != null) {
            this.attributeValue.setFont(font);
        }
    }

    private void initComponents() {
        this.attributeIcon = new MyImage();
        this.attributeIcon.setCentered(true);
        this.attributeValue = new JLabel();
        this.attributeValue.setOpaque(false);
        this.attributeValue.setFont(this.attributeValue.getFont().deriveFont((float)this.attributeValue.getFont().getSize() + 5.0f));
        this.attributeValue.setHorizontalAlignment(0);
        this.setFont(this.attributeValue.getFont());
        this.setOpaque(false);
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("1dlu:grow")});
        this.setLayout(layout);
        layout.setColumnGroups(new int[][]{{1, 2}});
        this.add((Component)this.attributeIcon, "1, 1, fill, fill");
        this.attributeValue.setText(" ");
        this.add((Component)this.attributeValue, "2, 1, fill, fill");
        DelegateMouseListener listener = new DelegateMouseListener();
        this.addMouseMotionListener(listener);
        this.addMouseListener(listener);
        this.attributeIcon.addMouseListener(listener);
        this.attributeIcon.addMouseMotionListener(listener);
    }
}

