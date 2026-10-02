/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.IngredientItem;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectContainer;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;
import jasbro.util.itemEditor.IngredientItemEffectPanel;
import jasbro.util.itemEditor.SpawnDataPanel;
import jasbro.util.itemEditor.UsableItemEffectPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class IngredientItemEditorPanel
extends JPanel {
    private IngredientItem item;
    private IngredientItemEffectPanel selectedEffectPanel;
    private JComboBox<UsableItemEffectType> effectTypeComboBox;

    public IngredientItemEditorPanel(IngredientItem curItem) {
        this.item = curItem;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow(8)"), FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        final JPanel effectPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(effectPanel);
        this.add((Component)scrollPane, "1, 1, 1, 2, fill, fill");
        effectPanel.setLayout(new GridLayout(1, 1, 0, 0));
        JPanel panel = new JPanel();
        this.add((Component)panel, "3, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        this.effectTypeComboBox = new JComboBox();
        panel.add(this.effectTypeComboBox, "1, 1, fill, default");
        for (UsableItemEffectType itemEffectType : UsableItemEffectType.values()) {
            this.effectTypeComboBox.addItem(itemEffectType);
        }
        JButton btnAdd = new JButton("Add");
        panel.add((Component)btnAdd, "1, 2");
        btnAdd.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UsableItemEffectType usableItemEffectType = (UsableItemEffectType)((Object)IngredientItemEditorPanel.this.effectTypeComboBox.getSelectedItem());
                    UsableItemEffect usableItemEffect = usableItemEffectType.getItemEffectClass().newInstance();
                    if (effectPanel.getComponents().length != 0) {
                        if (!(IngredientItemEditorPanel.this.selectedEffectPanel.getItemEffect() instanceof UsableItemEffectContainer)) {
                            return;
                        }
                        IngredientItemEditorPanel.this.selectedEffectPanel.getItemEffect().getSubEffects().add(usableItemEffect);
                    } else {
                        IngredientItemEditorPanel.this.selectedEffectPanel = null;
                    }
                    UsableItemEffectPanel newPanel = new UsableItemEffectPanel();
                    newPanel.setItemEffect(usableItemEffect);
                    if (IngredientItemEditorPanel.this.selectedEffectPanel != null) {
                        IngredientItemEditorPanel.this.selectedEffectPanel.setSelected(false);
                    } else {
                        effectPanel.add(newPanel);
                    }
                    newPanel.setSelected(true);
                    IngredientItemEditorPanel.this.validate();
                    IngredientItemEditorPanel.this.repaint();
                    newPanel.addMouseListener(new MyMouseListener());
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        });
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!IngredientItemEditorPanel.this.selectedEffectPanel.hasChildEffects()) {
                    Container component;
                    for (component = IngredientItemEditorPanel.this.selectedEffectPanel.getParent(); component != IngredientItemEditorPanel.this && !(component instanceof UsableItemEffectPanel); component = component.getParent()) {
                    }
                    if (component != IngredientItemEditorPanel.this) {
                        UsableItemEffectPanel usableItemEffectPanel = (UsableItemEffectPanel)component;
                        usableItemEffectPanel.setSelected(true);
                        usableItemEffectPanel.getItemEffect().getSubEffects().remove(IngredientItemEditorPanel.this.selectedEffectPanel.getItemEffect());
                    } else {
                        IngredientItemEditorPanel.this.selectedEffectPanel = null;
                        effectPanel.removeAll();
                        IngredientItemEditorPanel.this.item.setItemEffect(null);
                    }
                    IngredientItemEditorPanel.this.validate();
                    IngredientItemEditorPanel.this.repaint();
                }
            }
        });
        panel.add((Component)btnDelete, "1, 3");
        if (this.item.getItemEffect() != null) {
            try {
                UsableItemEffectPanel usableItemEffectPanel = new UsableItemEffectPanel();
                this.initEffectPanel(usableItemEffectPanel);
                effectPanel.add(usableItemEffectPanel);
                usableItemEffectPanel.setSelected(true);
                usableItemEffectPanel.addMouseListener(new MyMouseListener());
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        this.add((Component)new SpawnDataPanel(curItem), "3, 2, fill, fill");
        this.validate();
        this.repaint();
    }

    private void initEffectPanel(UsableItemEffectPanel itemEffectPanel) throws InstantiationException, IllegalAccessException {
        UsableItemEffect effect = itemEffectPanel.getItemEffect();
        for (UsableItemEffect subEffect : effect.getSubEffects()) {
            UsableItemEffectPanel usableItemEffectPanel = new UsableItemEffectPanel();
            usableItemEffectPanel.addMouseListener(new MyMouseListener());
            usableItemEffectPanel.setItemEffect(subEffect);
            itemEffectPanel.addPanel(usableItemEffectPanel);
            this.initEffectPanel(usableItemEffectPanel);
        }
    }

    private class MyMouseListener
    extends MouseAdapter {
        private MyMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (IngredientItemEditorPanel.this.selectedEffectPanel != null) {
                IngredientItemEditorPanel.this.selectedEffectPanel.setSelected(false);
            }
            UsableItemEffectPanel newPanel = (UsableItemEffectPanel)e.getSource();
            newPanel.setSelected(true);
            IngredientItemEditorPanel.this.validate();
            IngredientItemEditorPanel.this.repaint();
        }
    }
}

