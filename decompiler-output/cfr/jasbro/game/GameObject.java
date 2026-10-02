/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game;

import jasbro.Jasbro;
import jasbro.WeakList;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import java.awt.Component;
import java.io.Serializable;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GameObject
implements MyEventListener,
Serializable {
    private static final Logger log = LogManager.getLogger(GameObject.class);
    private static final long serialVersionUID = -7717504249836567448L;
    private transient WeakList<MyEventListener> guiListeners = new WeakList();
    private WeakList<MyEventListener> listeners = new WeakList();

    @Override
    public void handleEvent(MyEvent e) {
    }

    public void fireEvent(final MyEvent e) {
        try {
            if (this.getListeners().size() > 0) {
                for (final MyEventListener listener : this.getListeners().strongCopy()) {
                    try {
                        listener.handleEvent(e);
                    }
                    catch (NullPointerException ex) {
                    }
                    catch (Exception ex) {
                        log.error("Error", (Throwable)ex);
                    }
                }
            }
        }
        catch (Exception ex) {
            log.error("Error while notifying listeners", (Throwable)ex);
        }
        try {
            if (this.getGuiListeners().size() > 0) {
                for (final MyEventListener listener : this.getGuiListeners().strongCopy()) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                listener.handleEvent(e);
                            }
                            catch (NullPointerException ex) {
                            }
                            catch (Exception ex) {
                                log.error("Error", (Throwable)ex);
                            }
                        }
                    });
                }
            }
        }
        catch (Exception ex) {
            log.error("Error while notifying gui listeners", (Throwable)ex);
        }
        Jasbro.getInstance().getData().getEventManager().handleEvent(e);
    }

    private WeakList<MyEventListener> getListeners() {
        if (this.listeners == null) {
            this.listeners = new WeakList();
        }
        return this.listeners;
    }

    private WeakList<MyEventListener> getGuiListeners() {
        if (this.guiListeners == null) {
            this.guiListeners = new WeakList();
        }
        return this.guiListeners;
    }

    public void addListener(MyEventListener listener) {
        if (listener instanceof Component || !(listener instanceof Serializable)) {
            if (!this.getGuiListeners().contains(listener)) {
                this.getGuiListeners().add(listener);
            }
        } else if (!this.getListeners().contains(listener)) {
            this.getListeners().add(listener);
        }
    }

    public void clearGuiListeners() {
        try {
            this.guiListeners.clear();
        }
        catch (Exception e) {
            log.error("Error during clear", (Throwable)e);
        }
    }
}

