/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;

public class DelegateMouseListener
implements MouseListener,
MouseMotionListener,
MouseWheelListener {
    @Override
    public void mouseDragged(MouseEvent e) {
        this.dispatch(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.dispatch(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.dispatch(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.dispatch(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.dispatch(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.dispatch(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.dispatch(e);
    }

    public void dispatch(MouseEvent e) {
        Container parent = e.getComponent().getParent();
        MouseEvent e2 = SwingUtilities.convertMouseEvent(e.getComponent(), e, e.getComponent().getParent());
        parent.dispatchEvent(e2);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Container parent = e.getComponent().getParent();
        parent.dispatchEvent(e);
    }
}

