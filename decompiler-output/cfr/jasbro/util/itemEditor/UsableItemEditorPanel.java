/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.UsableItem;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectContainer;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsableItemEditorPanel
extends JPanel {
    private static final Logger log = LogManager.getLogger(UsableItemEditorPanel.class);
    private UsableItem item;
    private UsableItemEffectPanel selectedEffectPanel;
    private JComboBox<UsableItemEffectType> effectTypeComboBox;

    public UsableItemEditorPanel(UsableItem curItem) {
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
                    UsableItemEffectType usableItemEffectType = (UsableItemEffectType)((Object)UsableItemEditorPanel.this.effectTypeComboBox.getSelectedItem());
                    UsableItemEffect usableItemEffect = usableItemEffectType.getItemEffectClass().newInstance();
                    if (effectPanel.getComponents().length != 0) {
                        if (!(UsableItemEditorPanel.this.selectedEffectPanel.getItemEffect() instanceof UsableItemEffectContainer)) {
                            return;
                        }
                        UsableItemEditorPanel.this.selectedEffectPanel.getItemEffect().getSubEffects().add(usableItemEffect);
                    } else {
                        UsableItemEditorPanel.this.selectedEffectPanel = null;
                        UsableItemEditorPanel.this.item.setItemEffect(usableItemEffect);
                    }
                    UsableItemEffectPanel newPanel = new UsableItemEffectPanel();
                    newPanel.setItemEffect(usableItemEffect);
                    if (UsableItemEditorPanel.this.selectedEffectPanel != null) {
                        UsableItemEditorPanel.this.selectedEffectPanel.setSelected(false);
                        UsableItemEditorPanel.this.selectedEffectPanel.addPanel(newPanel);
                    } else {
                        effectPanel.add(newPanel);
                    }
                    UsableItemEditorPanel.this.selectedEffectPanel = newPanel;
                    newPanel.setSelected(true);
                    UsableItemEditorPanel.this.validate();
                    UsableItemEditorPanel.this.repaint();
                    newPanel.addMouseListener(new MyMouseListener());
                }
                catch (Exception ex) {
                    log.error("Error", (Throwable)ex);
                }
            }
        });
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!UsableItemEditorPanel.this.selectedEffectPanel.hasChildEffects()) {
                    Container component;
                    for (component = UsableItemEditorPanel.this.selectedEffectPanel.getParent(); component != UsableItemEditorPanel.this && !(component instanceof UsableItemEffectPanel); component = component.getParent()) {
                    }
                    if (component != UsableItemEditorPanel.this) {
                        UsableItemEffectPanel usableItemEffectPanel = (UsableItemEffectPanel)component;
                        usableItemEffectPanel.setSelected(true);
                        usableItemEffectPanel.getItemEffect().getSubEffects().remove(UsableItemEditorPanel.this.selectedEffectPanel.getItemEffect());
                        usableItemEffectPanel.removePanel(UsableItemEditorPanel.this.selectedEffectPanel);
                        UsableItemEditorPanel.this.selectedEffectPanel = usableItemEffectPanel;
                    } else {
                        UsableItemEditorPanel.this.selectedEffectPanel = null;
                        effectPanel.removeAll();
                        UsableItemEditorPanel.this.item.setItemEffect(null);
                    }
                    UsableItemEditorPanel.this.validate();
                    UsableItemEditorPanel.this.repaint();
                }
            }
        });
        panel.add((Component)btnDelete, "1, 3");
        if (this.item.getItemEffect() != null) {
            try {
                UsableItemEffectPanel usableItemEffectPanel = new UsableItemEffectPanel();
                usableItemEffectPanel.setItemEffect(this.item.getItemEffect());
                this.initEffectPanel(usableItemEffectPanel);
                effectPanel.add(usableItemEffectPanel);
                usableItemEffectPanel.setSelected(true);
                usableItemEffectPanel.addMouseListener(new MyMouseListener());
                this.selectedEffectPanel = usableItemEffectPanel;
            }
            catch (Exception e) {
                log.error("Error while initializing item effect panel", (Throwable)e);
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
            if (UsableItemEditorPanel.this.selectedEffectPanel != null) {
                UsableItemEditorPanel.this.selectedEffectPanel.setSelected(false);
            }
            UsableItemEffectPanel newPanel = (UsableItemEffectPanel)e.getSource();
            newPanel.setSelected(true);
            UsableItemEditorPanel.this.selectedEffectPanel = newPanel;
            UsableItemEditorPanel.this.validate();
            UsableItemEditorPanel.this.repaint();
        }
    }
}

