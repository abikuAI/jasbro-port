/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.perks;

import jasbro.game.character.traits.SkillTree;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SkillTreeItemPanel
extends MyImage {
    private SkillTree skillTree;

    public SkillTreeItemPanel(SkillTree skillTree) {
        this.skillTree = skillTree;
        this.setOpaque(false);
        this.setImage(skillTree.getIcon());
        this.setPreferredSize(new Dimension(-1, 150));
        this.setBackground(GuiUtil.TRANSPARENTCOLOR);
        this.setCentered(true);
        this.setToolTipText(skillTree.getText());
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
                SkillTreeItemPanel.this.setInsetX(SkillTreeItemPanel.this.getWidth() / 12);
                SkillTreeItemPanel.this.setInsetY(SkillTreeItemPanel.this.getHeight() / 12);
                SkillTreeItemPanel.this.repaint();
            }
        });
    }

    public void setSelected(boolean selected) {
        if (selected) {
            this.setBackground(GuiUtil.SELECTEDTRANSPARENTCOLOR);
        } else {
            this.setBackground(GuiUtil.TRANSPARENTCOLOR);
        }
        this.repaint();
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(this.getBackground());
        Insets insets = this.getInsets();
        g.fillRect(insets.left, insets.top, this.getWidth() - insets.right, this.getHeight() - insets.bottom);
        super.paintComponent(g);
    }

    public SkillTree getSkillTree() {
        return this.skillTree;
    }
}

