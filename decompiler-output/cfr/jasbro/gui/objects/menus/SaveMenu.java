/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.menus;

import jasbro.gui.objects.menus.actions.SlotSaveAction;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class SaveMenu
extends JMenu {
    public SaveMenu() {
        super(TextUtil.t("ui.save"));
        JMenuItem saveQuickButton = new JMenuItem(new SlotSaveAction(TextUtil.t("ui.quicksave"), -1));
        saveQuickButton.setMnemonic(81);
        this.add(saveQuickButton);
        for (int i = 1; i <= ConfigHandler.getSetting(Settings.SAVESLOTS, 3); i = (int)((short)(i + 1))) {
            JMenuItem saveSlotButton = new JMenuItem(new SlotSaveAction(i));
            if (i < 10) {
                saveSlotButton.setMnemonic(48 + i);
                saveSlotButton.setAccelerator(KeyStroke.getKeyStroke(48 + i, 2));
            }
            this.add(saveSlotButton);
        }
    }
}

