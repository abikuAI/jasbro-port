/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import jasbro.texts.TextUtil;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class AttributePanel
extends MyPanel {
    private Attribute attribute;
    private JLabel attributeNameLabel;
    private JLabel attributeValueLabel;

    public AttributePanel() {
        this.setOpaque(false);
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("3dlu:none")}, new RowSpec[]{RowSpec.decode("default:grow")});
        this.setLayout(layout);
        DelegateMouseListener listener = new DelegateMouseListener();
        this.addMouseMotionListener(listener);
        this.addMouseListener(listener);
        this.setBorder(new EmptyBorder(0, 2, 0, 0));
        this.getPreferredSize().width = 1;
        this.getMinimumSize().width = 1;
    }

    public AttributePanel(Attribute attribute) {
        this();
        this.attribute = attribute;
        this.initComponents();
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
        this.initComponents();
    }

    private void initComponents() {
        this.attributeNameLabel = new JLabel();
        this.attributeNameLabel.setHorizontalAlignment(2);
        this.attributeNameLabel.setOpaque(false);
        this.attributeNameLabel.setText(this.attribute.getNameResolved());
        this.attributeNameLabel.getPreferredSize().width = 1;
        this.attributeNameLabel.getMinimumSize().width = 1;
        this.attributeNameLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        this.add((Component)this.attributeNameLabel, "1, 1, fill, fill");
        this.attributeValueLabel = new JLabel();
        this.attributeValueLabel.setHorizontalAlignment(4);
        this.attributeValueLabel.setOpaque(false);
        this.attributeValueLabel.getPreferredSize().width = 1;
        this.attributeValueLabel.getMinimumSize().width = 1;
        this.attributeValueLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        this.add((Component)this.attributeValueLabel, "2, 1, fill, fill");
    }

    public JLabel getAttributeNameLabel() {
        return this.attributeNameLabel;
    }

    public JLabel getAttributeValueLabel() {
        return this.attributeValueLabel;
    }

    public void setNormalSize() {
        this.setMinimumSize(null);
        this.setPreferredSize(null);
        this.attributeNameLabel.setMinimumSize(null);
        this.attributeNameLabel.setPreferredSize(null);
        this.attributeValueLabel.setMinimumSize(null);
        this.attributeValueLabel.setPreferredSize(null);
    }

    @Override
    public void update() {
        if (this.attribute.getAttributeType() instanceof EssentialAttributes) {
            this.attributeNameLabel.setToolTipText(TextUtil.htmlPreformatted(((EssentialAttributes)this.attribute.getAttributeType()).getDescription()));
        } else if (this.attribute.getAttributeType() instanceof BaseAttributeTypes) {
            this.attributeNameLabel.setToolTipText(TextUtil.htmlPreformatted(((BaseAttributeTypes)this.attribute.getAttributeType()).getDescription()));
        }
        this.attributeValueLabel.setText(this.attribute.getValue() + "");
        int bonus = this.attribute.getBonus();
        Object[] attributes = new Object[]{(int)this.attribute.getInternValue(), this.attribute.getMaxValue(), bonus};
        String toolTip = bonus == 0 ? TextUtil.t("ui.attributetooltip", attributes) : TextUtil.t("ui.attributetooltip.bonus", attributes);
        this.attributeValueLabel.setToolTipText(toolTip);
    }
}

