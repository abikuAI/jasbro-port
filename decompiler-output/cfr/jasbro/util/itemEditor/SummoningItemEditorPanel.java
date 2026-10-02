/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.SummoningItem;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.util.itemEditor.SpawnDataPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SummoningItemEditorPanel
extends JPanel {
    private JComboBox<ComplexEnemyTemplate> unlockComboBox;
    private SummoningItem item;
    private String monsterID;
    private ComplexEnemyTemplate enemyTemplate;

    public SummoningItemEditorPanel(SummoningItem curItem) {
        this.item = curItem;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow(8)"), FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        JPanel panel = new JPanel();
        this.add((Component)panel, "1, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        JLabel lblUnlocks = new JLabel("Summons:");
        panel.add((Component)lblUnlocks, "1, 4, right, default");
        this.unlockComboBox = new JComboBox();
        panel.add(this.unlockComboBox, "2, 4, fill, default");
        for (ComplexEnemyTemplate enemyTemplate : Jasbro.getInstance().getEnemyTemplates().values()) {
            this.unlockComboBox.addItem(enemyTemplate);
            this.monsterID = enemyTemplate.getId();
        }
        this.unlockComboBox.setSelectedItem(this.item.getSummonedMonster());
        this.unlockComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SummoningItemEditorPanel.this.item.setSummonedMonster(SummoningItemEditorPanel.this.monsterID);
            }
        });
        SpawnDataPanel spawnDataPanel = new SpawnDataPanel(curItem);
        this.add((Component)spawnDataPanel, "3, 1, fill, fill");
        this.validate();
        this.repaint();
    }
}

