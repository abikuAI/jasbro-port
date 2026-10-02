/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.enemyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.util.enemyEditor.EnemyEditorPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EnemyListPanel
extends JPanel {
    private JList<ComplexEnemyTemplate> questList;
    private JTextField textField;

    public EnemyListPanel(final EnemyEditorPanel enemyPanel) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        JPanel newItemPanel = new JPanel();
        this.add((Component)newItemPanel, "1, 1, fill, fill");
        newItemPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        this.textField = new JTextField();
        newItemPanel.add((Component)this.textField, "1, 1, fill, default");
        this.textField.setColumns(10);
        JButton btnCreateNewItem = new JButton("Create Enemy");
        btnCreateNewItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String questId = EnemyListPanel.this.textField.getText().trim();
                if (questId != null && !questId.equals("") && !Jasbro.getInstance().getEnemyTemplates().containsKey(questId) && Util.isValidFileName(questId)) {
                    ComplexEnemyTemplate quest = new ComplexEnemyTemplate(questId);
                    Jasbro.getInstance().getEnemyTemplates().put(questId, quest);
                    enemyPanel.setEnemyTemplate(quest);
                    EnemyListPanel.this.update();
                }
            }
        });
        newItemPanel.add((Component)btnCreateNewItem, "2, 1");
        JScrollPane scrollPane = new JScrollPane();
        this.add((Component)scrollPane, "1, 2, fill, fill");
        this.questList = new JList();
        scrollPane.setViewportView(this.questList);
        this.questList.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                enemyPanel.setEnemyTemplate((ComplexEnemyTemplate)EnemyListPanel.this.questList.getSelectedValue());
            }
        });
        this.update();
    }

    public void update() {
        ComplexEnemyTemplate[] itemArray = new ComplexEnemyTemplate[Jasbro.getInstance().getEnemyTemplates().entrySet().size()];
        itemArray = Jasbro.getInstance().getEnemyTemplates().values().toArray(itemArray);
        Arrays.sort(itemArray, new Comparator<ComplexEnemyTemplate>(){

            @Override
            public int compare(ComplexEnemyTemplate o1, ComplexEnemyTemplate o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 != null) {
                    return o1.getId().compareTo(o2.getId());
                }
                return o2.getId().compareTo(null);
            }
        });
        this.questList.setListData((ComplexEnemyTemplate[])itemArray);
        this.validate();
        this.repaint();
    }
}

