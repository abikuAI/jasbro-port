/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.enemyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.EnemySpawnData;
import jasbro.util.enemyEditor.EnemySpawnDataPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EnemySpawnDataListPanel
extends JPanel {
    private JPanel spawnListPanel;
    private ComplexEnemyTemplate complexEnemyTemplate;

    public EnemySpawnDataListPanel() {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        JButton btnAddSpawndata = new JButton("Add SpawnData");
        this.add((Component)btnAddSpawndata, "1, 1");
        btnAddSpawndata.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EnemySpawnData enemySpawnData = new EnemySpawnData();
                EnemySpawnDataListPanel.this.complexEnemyTemplate.getSpawnDataList().add(enemySpawnData);
                EnemySpawnDataListPanel.this.spawnListPanel.add(new EnemySpawnDataPanel(EnemySpawnDataListPanel.this.complexEnemyTemplate, enemySpawnData));
                EnemySpawnDataListPanel.this.validate();
                EnemySpawnDataListPanel.this.repaint();
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        this.add((Component)scrollPane, "1, 2, fill, fill");
        this.spawnListPanel = new JPanel();
        scrollPane.setViewportView(this.spawnListPanel);
        this.spawnListPanel.setLayout(new BoxLayout(this.spawnListPanel, 1));
    }

    public void setComplexEnemyTemplate(ComplexEnemyTemplate complexEnemyTemplate) {
        this.complexEnemyTemplate = complexEnemyTemplate;
        this.spawnListPanel.removeAll();
        for (EnemySpawnData enemySpawnData : complexEnemyTemplate.getSpawnDataList()) {
            this.spawnListPanel.add(new EnemySpawnDataPanel(complexEnemyTemplate, enemySpawnData));
        }
        this.validate();
    }
}

