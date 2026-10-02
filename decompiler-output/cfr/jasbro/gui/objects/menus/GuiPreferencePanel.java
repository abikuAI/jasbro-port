/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.menus;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GuiPreferencePanel
extends JPanel {
    private JCheckBox hideArrowKeysCheckbox;
    private JCheckBox useSystemLookAndFellCheckbox;
    private JCheckBox showNumbersOnBarsCheckbox;

    public GuiPreferencePanel() {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC}));
        JLabel useSystemLFLabel = new JLabel(TextUtil.htmlPreformatted("Use System Look and Feel\n(Requires restarting the game)"));
        this.add((Component)useSystemLFLabel, "1, 2");
        this.useSystemLookAndFellCheckbox = new JCheckBox("", ConfigHandler.isUseSystemLookAndFeel());
        this.add((Component)this.useSystemLookAndFellCheckbox, "2, 2");
        JLabel showNumbersOnBarsLabel = new JLabel("Show Numbers on Attribute bars");
        this.add((Component)showNumbersOnBarsLabel, "1, 3");
        this.showNumbersOnBarsCheckbox = new JCheckBox("", ConfigHandler.isShowNumbersOnBars());
        this.add((Component)this.showNumbersOnBarsCheckbox, "2, 3");
        JLabel hideArrowKeysLabel = new JLabel("Character Screen: Hide navigation arrows");
        this.add((Component)hideArrowKeysLabel, "1, 4");
        this.hideArrowKeysCheckbox = new JCheckBox("", ConfigHandler.isHideArrowKeys());
        this.add((Component)this.hideArrowKeysCheckbox, "2, 4");
    }

    public JCheckBox getHideArrowKeysCheckbox() {
        return this.hideArrowKeysCheckbox;
    }

    public JCheckBox getUseSystemLookAndFellCheckbox() {
        return this.useSystemLookAndFellCheckbox;
    }

    public JCheckBox getShowNumbersOnBarsCheckbox() {
        return this.showNumbersOnBarsCheckbox;
    }
}

