/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

public class MyButton
extends MyImage {
    private JLabel textLabel;
    private boolean enabled = true;
    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    public MyButton(String text, final ImageData iconStandard, final ImageData iconHover) {
        this.setBackgroundImage(iconStandard);
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.textLabel = new JLabel(text);
        this.textLabel.setHorizontalAlignment(0);
        this.textLabel.setHorizontalTextPosition(0);
        this.add(this.textLabel);
        this.textLabel.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {
                if (MyButton.this.enabled && !e.isConsumed()) {
                    e.consume();
                    ActionEvent actionEvent = new ActionEvent(this, 0, "");
                    for (ActionListener actionListener : MyButton.this.actionListeners) {
                        if (actionListener == null) continue;
                        actionListener.actionPerformed(actionEvent);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                MyButton.this.setBackgroundImage(iconStandard);
                MyButton.this.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (MyButton.this.enabled) {
                    MyButton.this.setBackgroundImage(iconHover);
                    MyButton.this.repaint();
                }
            }
        });
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
                MyButton.this.updateFontSize();
            }
        });
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (this.textLabel != null) {
            this.textLabel.addMouseListener(l);
        }
    }

    public void updateFontSize() {
        Font font = this.getFont();
        int maxFontSize = this.getMaxFittingFontSize(font);
        font = font.deriveFont((float)(maxFontSize * 3 / 4));
        this.textLabel.setFont(font);
        this.repaint();
    }

    public int getMaxFittingFontSize(Font font) {
        int minSize = 0;
        int maxSize = 40;
        int curSize = font.getSize();
        int width = this.getWidth();
        int height = this.getHeight();
        if (width != 0 && height != 0 && this.textLabel.getText() != null) {
            while (maxSize - minSize > 1) {
                font = font.deriveFont((float)curSize);
                FontMetrics fm = this.getFontMetrics(font);
                int fontWidth = fm.stringWidth(this.textLabel.getText());
                int fontHeight = fm.getAscent() + fm.getLeading();
                if (fontWidth >= width || fontHeight >= height) {
                    maxSize = curSize;
                    curSize = (maxSize + minSize) / 2;
                    continue;
                }
                minSize = curSize;
                curSize = (minSize + maxSize) / 2;
            }
        }
        return minSize;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.setGrayscale(!enabled);
        this.repaint();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void addActionListener(ActionListener al) {
        this.actionListeners.add(al);
    }

    public void setText(String text) {
        this.textLabel.setText(text);
    }
}

