/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.housing.House;
import jasbro.game.housing.SecurityState;
import jasbro.game.interfaces.AreaInterface;
import jasbro.texts.TextUtil;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HouseInfoPanel
extends JPanel {
    private JLabel dirtLabel;
    private JLabel securityLabel;
    private JLabel fameLabel;

    public HouseInfoPanel() {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.setOpaque(false);
        this.fameLabel = new JLabel();
        this.fameLabel.setOpaque(false);
        this.add((Component)this.fameLabel, "2, 1");
        this.securityLabel = new JLabel();
        this.securityLabel.setOpaque(false);
        this.add((Component)this.securityLabel, "4, 1");
        this.dirtLabel = new JLabel();
        this.dirtLabel.setOpaque(false);
        this.add((Component)this.dirtLabel, "6, 1");
    }

    public void refresh(AreaInterface area) {
        if (area instanceof House) {
            House house = (House)area;
            this.fameLabel.setText(TextUtil.t("fame") + ": " + house.getFame().getFameBuilding().getText());
            Object[] arguments = new Object[]{house.getFame().getFame()};
            this.fameLabel.setToolTipText(TextUtil.t("formatted", arguments));
            this.dirtLabel.setText(TextUtil.t("ui.state") + ": " + house.getCleanState().getText());
            arguments[0] = house.getDirt();
            this.dirtLabel.setToolTipText(TextUtil.t("formatted", arguments));
            if (SecurityState.isImplemented()) {
                this.securityLabel.setText(TextUtil.t("ui.security") + ": " + house.getSecurityState().getText());
                arguments[0] = house.getSecurity();
                this.securityLabel.setToolTipText(TextUtil.t("formatted", arguments));
            }
        } else {
            this.fameLabel.setToolTipText("");
            this.fameLabel.setText("");
            this.securityLabel.setText("");
            this.securityLabel.setToolTipText("");
            this.dirtLabel.setText("");
            this.dirtLabel.setToolTipText("");
        }
    }
}

