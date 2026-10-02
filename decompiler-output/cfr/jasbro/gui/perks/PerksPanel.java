/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.perks;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.PerkHandler;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.RPGView;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.perks.SkillTreeItemPanel;
import jasbro.gui.perks.SkillTreePanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PerksPanel
extends MyImage
implements MyEventListener {
    private List<SkillTreeItemPanel> skillTreeItems = new ArrayList<SkillTreeItemPanel>();
    private SkillTreeItemPanel selectedSkillTree;
    private SkillTreePanel skillTreePanel;
    private Charakter character;
    private JLabel perkPointLabel;

    public PerksPanel(Charakter characterTmp) {
        this.character = characterTmp;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("100dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        this.setBackgroundImage(new ImageData("images/icons/perks/Old_Scroll_Texture_II_by_Isthar_art.jpg"));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        this.add((Component)scrollPane, "1, 1, fill, fill");
        scrollPane.getViewport().setOpaque(false);
        JPanel skillTreeSelectionPanel = new JPanel();
        skillTreeSelectionPanel.setOpaque(false);
        scrollPane.setViewportView(skillTreeSelectionPanel);
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:none")});
        skillTreeSelectionPanel.setLayout(layout);
        MyMouseListener ml = new MyMouseListener();
        List<SkillTree> skillTrees = this.character.getSkillTrees();
        for (int i = 0; i < skillTrees.size(); ++i) {
            SkillTree skillTree = skillTrees.get(i);
            SkillTreeItemPanel skillTreeItemPanel = new SkillTreeItemPanel(skillTree);
            layout.appendRow(RowSpec.decode("default:none"));
            skillTreeSelectionPanel.add((Component)skillTreeItemPanel, "1," + (i + 1));
            skillTreeItemPanel.addMouseListener(ml);
            this.skillTreeItems.add(skillTreeItemPanel);
            if (i != 0) continue;
            skillTreeItemPanel.setSelected(true);
            this.selectedSkillTree = skillTreeItemPanel;
        }
        scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        this.add((Component)scrollPane, "3, 1, 1, 5, fill, fill");
        scrollPane.getViewport().setOpaque(false);
        this.skillTreePanel = new SkillTreePanel();
        scrollPane.setViewportView(this.skillTreePanel);
        Object[] arguments = new Object[]{this.character.getUnspentPerkPoints()};
        MyImage perkPointImage = new MyImage();
        perkPointImage.setBackgroundImage(new ImageData("images/icons/perks/button75892304.png"));
        perkPointImage.setPreferredSize(new Dimension(-1, 40));
        this.perkPointLabel = new JLabel(TextUtil.t("ui.perkPointsRemaining", arguments));
        this.perkPointLabel.setFont(GuiUtil.DEFAULTHEADERFONT);
        this.perkPointLabel.setHorizontalAlignment(0);
        perkPointImage.setLayout(new GridLayout(1, 1));
        perkPointImage.add(this.perkPointLabel);
        this.add((Component)perkPointImage, "1, 3, fill, fill");
        if (this.selectedSkillTree != null) {
            this.skillTreePanel.initSkillTree(this.selectedSkillTree.getSkillTree(), this.character);
        }
        this.character.addListener(this);
        Object[] arguments2 = new Object[]{100000};
        JButton btnResetPerks = new JButton(TextUtil.t("ui.resetPerks", arguments2));
        this.add((Component)btnResetPerks, "1, 5");
        btnResetPerks.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(Jasbro.getInstance().getGui(), TextUtil.t("ui.confirmResetPerks"), TextUtil.t("ui.confirmResetPerks.title"), 2) == 0) {
                    Jasbro.getInstance().getData().spendMoney(100000L, "Reset perks");
                    PerkHandler.resetPerks(PerksPanel.this.character);
                    PerksPanel.this.skillTreePanel.initSkillTree(PerksPanel.this.selectedSkillTree.getSkillTree(), PerksPanel.this.character);
                }
            }
        });
        this.validate();
    }

    @Override
    public Dimension getPreferredSize() {
        RPGView view = Jasbro.getInstance().getGui();
        if (view != null) {
            int heightGui = view.getHeight();
            int widthGui = view.getWidth();
            return new Dimension(widthGui * 3 / 4, heightGui * 3 / 4);
        }
        return super.getPreferredSize();
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.STATUSCHANGE) {
            Object[] arguments = new Object[]{this.character.getUnspentPerkPoints()};
            this.perkPointLabel.setText(TextUtil.t("ui.perkPointsRemaining", arguments));
            this.validate();
            this.repaint();
        }
    }

    private class MyMouseListener
    extends MouseAdapter {
        private MyMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            this.handleClick(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.handleClick(e);
        }

        public void handleClick(MouseEvent e) {
            for (SkillTreeItemPanel skillTreeItem : PerksPanel.this.skillTreeItems) {
                skillTreeItem.setSelected(false);
            }
            PerksPanel.this.selectedSkillTree = (SkillTreeItemPanel)e.getSource();
            PerksPanel.this.selectedSkillTree.setSelected(true);
            PerksPanel.this.skillTreePanel.initSkillTree(PerksPanel.this.selectedSkillTree.getSkillTree(), PerksPanel.this.character);
        }
    }
}

