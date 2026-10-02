/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.perks;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.SkillTreeItem;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class PerkItemPanel
extends MyImage {
    private SkillTreeItem perk;
    private Charakter character;
    private SkillTree skillTree;

    public PerkItemPanel(SkillTreeItem skillTreeItem, final Charakter character, SkillTree skillTree) {
        this.character = character;
        this.skillTree = skillTree;
        this.perk = skillTreeItem;
        this.setCentered(true);
        this.setToolTipText(TextUtil.htmlPreformatted(this.perk.getPerk().getText(character) + "\n" + this.perk.getDescription(skillTree, character)));
        this.setImage(skillTreeItem.getIcon());
        if (character.getTraits().contains(skillTreeItem.getPerk())) {
            this.setBackgroundImage(new ImageData("images/icons/perks/magic_circle_of_spark_by_llunet1-d50er3t.png"));
        }
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
                PerkItemPanel.this.setInsetX(PerkItemPanel.this.getWidth() / 5);
                PerkItemPanel.this.setInsetY(PerkItemPanel.this.getHeight() / 5);
                PerkItemPanel.this.repaint();
            }
        });
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (PerkItemPanel.this.perk.canLearn(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)) {
                    Object[] arguments = new Object[]{PerkItemPanel.this.perk.getPerk().getText(), PerkItemPanel.this.perk.getPerk().getDescriptionWithName(character)};
                    if (JOptionPane.showConfirmDialog(PerkItemPanel.this.getParent().getParent().getParent(), TextUtil.t("ui.confirmPerk", arguments), TextUtil.t("ui.learnPerk", arguments), 0) == 0) {
                        PerkItemPanel.this.character.addTrait(PerkItemPanel.this.perk.getPerk());
                        PerkItemPanel.this.setBackgroundImage(new ImageData("images/icons/perks/magic_circle_of_spark_by_llunet1-d50er3t.png"));
                        PerkItemPanel.this.setToolTipText(TextUtil.htmlPreformatted(PerkItemPanel.this.perk.getPerk().getText() + "\n" + PerkItemPanel.this.perk.getDescription(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)));
                    }
                }
                PerkItemPanel.this.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (PerkItemPanel.this.perk.canLearn(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)) {
                    Object[] arguments = new Object[]{PerkItemPanel.this.perk.getPerk().getText(), PerkItemPanel.this.perk.getPerk().getDescriptionWithName(character)};
                    if (JOptionPane.showConfirmDialog(PerkItemPanel.this.getParent().getParent().getParent(), TextUtil.t("ui.confirmPerk", arguments), TextUtil.t("ui.learnPerk", arguments), 0) == 0) {
                        PerkItemPanel.this.character.addTrait(PerkItemPanel.this.perk.getPerk());
                        PerkItemPanel.this.setBackgroundImage(new ImageData("images/icons/perks/magic_circle_of_spark_by_llunet1-d50er3t.png"));
                        PerkItemPanel.this.setToolTipText(TextUtil.htmlPreformatted(PerkItemPanel.this.perk.getPerk().getText() + "\n" + PerkItemPanel.this.perk.getDescription(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)));
                    }
                }
                PerkItemPanel.this.repaint();
            }
        });
    }

    public SkillTreeItem getPerk() {
        return this.perk;
    }

    public boolean canLearn() {
        return this.perk.canLearn(this.skillTree, this.character);
    }
}

