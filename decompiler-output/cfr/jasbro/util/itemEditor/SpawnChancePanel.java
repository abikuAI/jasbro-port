/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.itemEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpawnChancePanel
extends JPanel {
    private ItemSpawnData itemSpawnData = new ItemSpawnData();

    public SpawnChancePanel(ItemSpawnData curItemSpawnData, final Item item) {
        this.itemSpawnData = curItemSpawnData;
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC}));
        final JSpinner spinner_2 = new JSpinner();
        spinner_2.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        spinner_2.setValue(this.itemSpawnData.getMaxAmount());
        spinner_2.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                SpawnChancePanel.this.itemSpawnData.setMaxAmount((Integer)spinner_2.getValue());
            }
        });
        final JSpinner spinner_1 = new JSpinner();
        spinner_1.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spinner_1.setValue(this.itemSpawnData.getMinAmount());
        spinner_1.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                SpawnChancePanel.this.itemSpawnData.setMinAmount((Integer)spinner_1.getValue());
            }
        });
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        spinner.setValue(this.itemSpawnData.getChance());
        spinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                SpawnChancePanel.this.itemSpawnData.setChance((Integer)spinner.getValue());
            }
        });
        JLabel lblLocation = new JLabel("Spawn-Location");
        this.add((Component)lblLocation, "1, 2, right, default");
        final JComboBox<ItemLocation> comboBox = new JComboBox<ItemLocation>();
        comboBox.setSelectedItem((Object)this.itemSpawnData.getItemLocation());
        this.add(comboBox, "2, 2, fill, default");
        for (ItemLocation itemLocation : ItemLocation.values()) {
            comboBox.addItem(itemLocation);
        }
        comboBox.setSelectedItem((Object)this.itemSpawnData.getItemLocation());
        comboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SpawnChancePanel.this.itemSpawnData.setItemLocation((ItemLocation)((Object)comboBox.getSelectedItem()));
            }
        });
        JLabel lblChance = new JLabel("Chance");
        this.add((Component)lblChance, "1, 3");
        this.add((Component)spinner, "2, 3");
        JLabel lblMinimumAmount = new JLabel("Minimum Amount");
        this.add((Component)lblMinimumAmount, "1, 4");
        this.add((Component)spinner_1, "2, 4");
        JLabel lblMaximumAmount = new JLabel("Maximum Amount");
        this.add((Component)lblMaximumAmount, "1, 5");
        this.add((Component)spinner_2, "2, 5");
        JButton btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.RED);
        this.add((Component)btnDelete, "2, 6");
        btnDelete.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                item.getSpawnData().remove(SpawnChancePanel.this.itemSpawnData);
                Container parent = SpawnChancePanel.this.getParent();
                parent.remove(SpawnChancePanel.this);
                parent.validate();
                parent.repaint();
            }
        });
    }
}

