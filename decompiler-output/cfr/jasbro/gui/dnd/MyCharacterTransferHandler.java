/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.imgscalr.Scalr$Mode
 */
package jasbro.gui.dnd;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.dnd.CanReceiveCharacterDrop;
import jasbro.gui.dnd.TransferableCharacter;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pages.subView.LocationPanel;
import jasbro.gui.pictures.ImageUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

public class MyCharacterTransferHandler
extends TransferHandler {
    private static final Logger log = LogManager.getLogger(MyCharacterTransferHandler.class);

    @Override
    public int getSourceActions(JComponent c) {
        return 0x40000000;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof CharacterShortView) {
            Charakter character = ((CharacterShortView)c).getCharacter();
            character.setActivity(null);
            this.setDragImage(ImageUtil.getInstance().getImageResizedSpeed(character.getIcon(), 50, 50, Scalr.Mode.AUTOMATIC));
            return new TransferableCharacter(character);
        }
        return null;
    }

    private boolean importPossible(Component comp) {
        return comp instanceof CanReceiveCharacterDrop;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        log.error("Method not supported");
        return false;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        Component comp = support.getComponent();
        boolean canImport = false;
        do {
            canImport = this.importPossible(comp);
            comp = comp.getParent();
        } while (!canImport && comp != null);
        return canImport;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        return this.importData((JComponent)support.getComponent(), support.getTransferable());
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        try {
            Container cont = comp;
            while (!(cont instanceof CanReceiveCharacterDrop)) {
                cont = cont.getParent();
            }
            if (cont instanceof LocationPanel) {
                CharacterLocation characterLocation = ((LocationPanel)cont).getCharacterLocation();
                int id = (Integer)t.getTransferData(t.getTransferDataFlavors()[0]);
                if (id != -1) {
                    Charakter character = Jasbro.getInstance().getData().getCharacters().get(id);
                    if (!characterLocation.getCurrentUsage().getCharacters().contains(character)) {
                        return characterLocation.getCurrentUsage().add(character);
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        catch (Exception e) {
            log.error("Error on importing data", (Throwable)e);
            return false;
        }
    }
}

