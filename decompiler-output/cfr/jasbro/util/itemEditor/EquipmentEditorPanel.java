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
import jasbro.game.items.AccessoryType;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentType;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;
import jasbro.util.itemEditor.EquipmentEffectPanel;
import jasbro.util.itemEditor.SpawnDataPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EquipmentEditorPanel
extends JPanel {
    private static final Logger log = LogManager.getLogger(EquipmentEditorPanel.class);
    private Equipment item;
    private JComboBox<EquipmentEffectType> effectTypeComboBox;
    private EquipmentEffectPanel selectedEffectPanel;
    private JComboBox<EquipmentType> equipmentTypeComboBox;
    private JComboBox<AccessoryType> accessoryTypeComboBox;

    public EquipmentEditorPanel(Equipment curItem) {
        this.item = curItem;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow(8)"), FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        final JPanel effectPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(effectPanel);
        this.add((Component)scrollPane, "1, 1, 1, 2, fill, fill");
        effectPanel.setLayout(new BoxLayout(effectPanel, 1));
        JPanel panel = new JPanel();
        this.add((Component)panel, "3, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        this.equipmentTypeComboBox = new JComboBox();
        panel.add(this.equipmentTypeComboBox, "1, 1, fill, default");
        for (EquipmentType equipmentType : EquipmentType.values()) {
            this.equipmentTypeComboBox.addItem(equipmentType);
        }
        this.equipmentTypeComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EquipmentType equipmentType = (EquipmentType)((Object)EquipmentEditorPanel.this.equipmentTypeComboBox.getSelectedItem());
                EquipmentEditorPanel.this.item.setEquipmentType(equipmentType);
                if (equipmentType == EquipmentType.ACCESSORY) {
                    EquipmentEditorPanel.this.accessoryTypeComboBox.setVisible(true);
                } else {
                    EquipmentEditorPanel.this.accessoryTypeComboBox.setVisible(false);
                    EquipmentEditorPanel.this.item.setAccessoryType(null);
                    EquipmentEditorPanel.this.accessoryTypeComboBox.setSelectedItem(null);
                }
            }
        });
        this.accessoryTypeComboBox = new JComboBox();
        panel.add(this.accessoryTypeComboBox, "1, 3, fill, default");
        this.accessoryTypeComboBox.addItem(null);
        for (Enum enum_ : AccessoryType.values()) {
            this.accessoryTypeComboBox.addItem((AccessoryType)enum_);
        }
        this.accessoryTypeComboBox.setSelectedItem((Object)this.item.getAccessoryType());
        this.accessoryTypeComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                EquipmentEditorPanel.this.item.setAccessoryType((AccessoryType)((Object)EquipmentEditorPanel.this.accessoryTypeComboBox.getSelectedItem()));
            }
        });
        this.equipmentTypeComboBox.setSelectedItem((Object)this.item.getEquipmentType());
        this.effectTypeComboBox = new JComboBox();
        panel.add(this.effectTypeComboBox, "1, 5, fill, default");
        for (Enum enum_ : EquipmentEffectType.values()) {
            this.effectTypeComboBox.addItem((EquipmentEffectType)enum_);
        }
        JButton btnAdd = new JButton("Add");
        panel.add((Component)btnAdd, "1, 6");
        btnAdd.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (EquipmentEditorPanel.this.selectedEffectPanel != null) {
                        EquipmentEditorPanel.this.selectedEffectPanel.setSelected(false);
                    }
                    EquipmentEffectType equipmentEffectType = (EquipmentEffectType)((Object)EquipmentEditorPanel.this.effectTypeComboBox.getSelectedItem());
                    EquipmentEffect equipmentEffect = equipmentEffectType.getItemEffectClass().newInstance();
                    EquipmentEffectPanel newPanel = new EquipmentEffectPanel();
                    newPanel.setItemEffect(equipmentEffect);
                    EquipmentEditorPanel.this.item.getEquipmentEffects().add(equipmentEffect);
                    effectPanel.add(newPanel);
                    newPanel.setSelected(true);
                    EquipmentEditorPanel.this.selectedEffectPanel = newPanel;
                    EquipmentEditorPanel.this.validate();
                    EquipmentEditorPanel.this.repaint();
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
                if (EquipmentEditorPanel.this.selectedEffectPanel != null && !EquipmentEditorPanel.this.selectedEffectPanel.hasChildEffects()) {
                    EquipmentEditorPanel.this.item.getEquipmentEffects().remove(EquipmentEditorPanel.this.selectedEffectPanel.getItemEffect());
                    effectPanel.remove(EquipmentEditorPanel.this.selectedEffectPanel);
                    if (effectPanel.getComponents().length > 0) {
                        EquipmentEffectPanel equipmentEffectPanel = (EquipmentEffectPanel)effectPanel.getComponent(0);
                        equipmentEffectPanel.setSelected(true);
                        EquipmentEditorPanel.this.selectedEffectPanel = equipmentEffectPanel;
                    } else {
                        EquipmentEditorPanel.this.selectedEffectPanel = null;
                    }
                    EquipmentEditorPanel.this.validate();
                    EquipmentEditorPanel.this.repaint();
                }
            }
        });
        panel.add((Component)btnDelete, "1, 7");
        JLabel lblCalculatedValue = new JLabel("Calculated Value:");
        panel.add((Component)lblCalculatedValue, "1, 8");
        final JLabel jLabel = new JLabel(TextUtil.t("formatted", this.item.calculateValue()));
        panel.add((Component)jLabel, "1, 9");
        JButton btnRecalculate = new JButton("Recalculate");
        panel.add((Component)btnRecalculate, "1, 10");
        btnRecalculate.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                jLabel.setText(TextUtil.t("formatted", EquipmentEditorPanel.this.item.calculateValue()));
            }
        });
        if (this.item.getEquipmentEffects() != null) {
            for (EquipmentEffect equipmentEffect : this.item.getEquipmentEffects()) {
                try {
                    EquipmentEffectPanel equipmentEffectPanel = new EquipmentEffectPanel();
                    equipmentEffectPanel.setItemEffect(equipmentEffect);
                    effectPanel.add(equipmentEffectPanel);
                    equipmentEffectPanel.addMouseListener(new MyMouseListener());
                }
                catch (Exception e) {
                    log.error("Error while initializing item effect panel", (Throwable)e);
                }
            }
        }
        this.add((Component)new SpawnDataPanel(curItem), "3, 2, fill, fill");
        this.validate();
        this.repaint();
    }

    private class MyMouseListener
    extends MouseAdapter {
        private MyMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (EquipmentEditorPanel.this.selectedEffectPanel != null) {
                EquipmentEditorPanel.this.selectedEffectPanel.setSelected(false);
            }
            EquipmentEffectPanel newPanel = (EquipmentEffectPanel)e.getSource();
            newPanel.setSelected(true);
            EquipmentEditorPanel.this.selectedEffectPanel = newPanel;
            EquipmentEditorPanel.this.validate();
            EquipmentEditorPanel.this.repaint();
        }
    }
}

