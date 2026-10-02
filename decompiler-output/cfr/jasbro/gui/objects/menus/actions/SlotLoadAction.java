/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.gui.objects.menus.actions;

import jasbro.Jasbro;
import jasbro.gui.objects.menus.actions.SlotSaveAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SlotLoadAction
extends SlotSaveAction {
    private static final Logger LOGGER = LogManager.getLogger(SlotLoadAction.class);

    public SlotLoadAction(int slot) {
        super(slot);
    }

    public SlotLoadAction(String name, int slot) {
        super(name, slot);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (this.getSlot() == -1) {
                Jasbro.getInstance().load(new File("quicksave.xml"));
            } else {
                Jasbro.getInstance().load(new File("save" + this.getSlot() + ".xml"));
            }
        }
        catch (IOException ex) {
            LOGGER.error("Failed to load save", (Throwable)ex);
        }
    }
}

