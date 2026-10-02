/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.menus.actions;

import jasbro.Jasbro;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;

public class SlotSaveAction
extends AbstractAction {
    private int slot;

    public SlotSaveAction(int slot) {
        super(TextUtil.t("ui.slot") + " " + slot);
        this.slot = slot;
    }

    public SlotSaveAction(String text, int slot) {
        super(text);
        this.slot = slot;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.getSlot() == -1) {
            Jasbro.getInstance().save(new File("quicksave.xml"));
        } else {
            Jasbro.getInstance().save(new File("save" + this.slot + ".xml"));
        }
        Jasbro.getInstance().getGui().getMainMenuBar().rebuildLoadMenu();
    }

    public int getSlot() {
        return this.slot;
    }
}

