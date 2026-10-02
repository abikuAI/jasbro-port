/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.menus;

import jasbro.gui.objects.menus.GameMenu;
import jasbro.gui.objects.menus.LoadMenu;
import jasbro.gui.objects.menus.MyInfoPanel;
import jasbro.gui.objects.menus.OptionMenu;
import jasbro.gui.objects.menus.SaveMenu;
import jasbro.gui.objects.menus.actions.SlotLoadAction;
import jasbro.gui.objects.menus.actions.SlotSaveAction;
import jasbro.texts.TextUtil;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MainMenuBar
extends JMenuBar {
    private MyInfoPanel statusInfo;
    private JMenu gameMenu = new GameMenu();

    public MainMenuBar() {
        this.add(this.gameMenu);
        this.add(new OptionMenu());
        this.add(Box.createGlue());
        JMenuItem quickSaveButton = new JMenuItem(new SlotSaveAction(TextUtil.t("ui.quicksave"), -1));
        quickSaveButton.setAccelerator(KeyStroke.getKeyStroke(116, 0));
        this.add(quickSaveButton);
        JMenuItem quickLoadButton = new JMenuItem(new SlotLoadAction(TextUtil.t("ui.quickload"), -1));
        quickLoadButton.setAccelerator(KeyStroke.getKeyStroke(120, 0));
        this.add(quickLoadButton);
        this.add(Box.createGlue());
        this.add(Box.createGlue());
        this.add(Box.createGlue());
        this.add(Box.createGlue());
        this.add(Box.createGlue());
        this.add(Box.createGlue());
        this.add(Box.createGlue());
        this.statusInfo = new MyInfoPanel();
        this.add(this.statusInfo);
    }

    public void rebuildSaveMenu() {
        this.gameMenu.remove(3);
        this.gameMenu.add((Component)new SaveMenu(), 3);
    }

    public void rebuildLoadMenu() {
        this.gameMenu.remove(4);
        this.gameMenu.add((Component)new LoadMenu(), 4);
    }

    public void updateStatusInfo() {
        this.statusInfo.update();
    }
}

