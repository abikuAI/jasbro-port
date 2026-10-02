/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.Unlocks;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UnlockObjectDisplay
extends JPanel {
    private MyImage myImage;
    private UnlockObject unlockObject;
    private Long requiredFame;
    private TranslucentPanel contentPanel;
    private JLabel titleLabel;
    private JLabel fameLabel;

    public UnlockObjectDisplay() {
        this.init();
    }

    public UnlockObjectDisplay(UnlockObject unlockObject) {
        this.unlockObject = unlockObject;
        this.init();
    }

    public UnlockObjectDisplay(Unlocks.FameUnlock fameUnlock) {
        this.unlockObject = fameUnlock.getUnlockObject();
        this.requiredFame = fameUnlock.getRequiredFame();
        this.init();
    }

    public void init() {
        this.setOpaque(false);
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("125dlu"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), RowSpec.decode("70dlu"), RowSpec.decode("default:grow")}));
        this.contentPanel = new TranslucentPanel();
        this.contentPanel.setLayout(new GridLayout(1, 1));
        this.add((Component)this.contentPanel, "2, 2, fill, fill");
        this.myImage = new MyImage();
        this.contentPanel.add(this.myImage);
        this.myImage.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC}));
        this.titleLabel = new JLabel("New label");
        this.myImage.add((Component)this.titleLabel, "2, 2");
        this.myImage.setBackgroundImage(this.unlockObject.getImage());
        this.titleLabel.setFont(GuiUtil.DEFAULTHEADERFONT);
        this.titleLabel.setText(this.unlockObject.getText());
        if (this.requiredFame != null) {
            this.fameLabel = new JLabel(TextUtil.t("ui.requiredFame", this.requiredFame));
            this.fameLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
            this.myImage.add((Component)this.fameLabel, "2, 4");
        }
        if (this.unlockObject.getDescription() != null && !this.unlockObject.getDescription().trim().equals("")) {
            this.myImage.setToolTipText(TextUtil.htmlPreformatted(this.unlockObject.getDescription()));
        }
        this.setGrayScale(!Jasbro.getInstance().getData().getUnlocks().getUnlockedObjects().contains(this.unlockObject));
    }

    public void setGrayScale(boolean grayScale) {
        this.myImage.setGrayscale(grayScale);
        if (grayScale) {
            this.titleLabel.setForeground(Color.RED);
            if (this.fameLabel != null) {
                this.fameLabel.setForeground(Color.RED);
            }
        } else {
            this.titleLabel.setForeground(Color.BLACK);
            if (this.fameLabel != null) {
                this.fameLabel.setForeground(Color.BLACK);
            }
        }
        this.repaint();
    }
}

