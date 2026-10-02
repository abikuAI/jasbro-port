/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.events.business.BuildingMercSecurity;
import jasbro.game.housing.House;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class SecurityPanel
extends TranslucentPanel {
    public SecurityPanel(House house) {
        this.setPreferredSize(null);
        this.setMinimumSize(null);
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        JLabel lblNewLabel = new JLabel(TextUtil.t("security"));
        lblNewLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
        lblNewLabel.setToolTipText(TextUtil.html(TextUtil.t("security.info")));
        this.add((Component)lblNewLabel, "1, 1");
        final BuildingMercSecurity security = house.getMercSecurity();
        Border defaultCheckboxBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        final JCheckBox mercCheckbox = new JCheckBox(TextUtil.t("security.mercs"));
        mercCheckbox.setBorder(defaultCheckboxBorder);
        mercCheckbox.setOpaque(false);
        mercCheckbox.setFont(GuiUtil.DEFAULTBOLDFONT);
        mercCheckbox.setSelected(security.hasMercs());
        mercCheckbox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                security.setMercs(mercCheckbox.isSelected());
            }
        });
        this.add((Component)mercCheckbox, "1, 2");
    }
}

