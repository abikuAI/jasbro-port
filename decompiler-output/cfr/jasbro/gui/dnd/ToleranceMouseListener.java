/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.dnd;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ToleranceMouseListener
implements MouseMotionListener,
MouseListener {
    private static int CLICK_TOLERANCE = 15;
    private Point startPoint;
    private Object initialSource;

    private boolean checkDragTolerance(Point e) {
        if (this.startPoint != null) {
            float changesx = Math.abs(e.x - this.startPoint.x);
            float changesy = Math.abs(e.y - this.startPoint.y);
            return changesx < (float)CLICK_TOLERANCE && changesy < (float)CLICK_TOLERANCE;
        }
        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.startPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.checkDragTolerance(e.getPoint())) {
            e.consume();
        } else {
            e.setSource(this.initialSource);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.startPoint = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}

