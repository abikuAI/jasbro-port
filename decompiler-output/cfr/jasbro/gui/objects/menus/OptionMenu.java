/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.gui.objects.menus.PreferencePanel;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class OptionMenu
extends JMenu {
    public OptionMenu() {
        super(TextUtil.t("ui.options"));
        JMenuItem optionsButton = new JMenuItem(new PreferenceAction());
        this.add(optionsButton);
    }

    private class PreferenceAction
    extends AbstractAction {
        private PreferencePanel preferencePanel;

        public PreferenceAction() {
            super(TextUtil.t("ui.preferences"));
            this.preferencePanel = new PreferencePanel();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(Jasbro.getInstance().getGui(), this.preferencePanel, "Preferences", 2) == 0) {
                try {
                    this.preferencePanel.applyChanges();
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(Jasbro.getInstance().getGui(), TextUtil.t("ui.error.optionsSaveFailed"), "Warning", 2);
                }
            }
        }
    }
}

