/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.enemyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CombatEditorPanel
extends JPanel {
    private ComplexEnemyTemplate complexEnemyTemplate;
    private JSpinner healthSpinner;
    private JSpinner damageSpinner;
    private JSpinner armorSpinner;
    private JSpinner dodgeSpinner;
    private JSpinner hitSpinner;
    private JSpinner critChanceSpinner;
    private JSpinner critDamageSpinner;
    private JSpinner blockChanceSpinner;
    private JSpinner blockAmountSpinner;
    private JSpinner speedSpinner;

    public CombatEditorPanel() {
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        int row = 0;
        JLabel lblNewLabel2 = new JLabel("Hitpoints");
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.healthSpinner = new JSpinner();
        this.healthSpinner.setModel(new SpinnerNumberModel(1, 1, 9999999, 1));
        this.add((Component)this.healthSpinner, "2, " + row);
        this.healthSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setHitpoints((Integer)CombatEditorPanel.this.healthSpinner.getValue());
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.DAMAGE.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.damageSpinner = new JSpinner();
        this.damageSpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 9999999.0, 0.0));
        this.add((Component)this.damageSpinner, "2," + row);
        this.damageSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.DAMAGE, (double)((Double)CombatEditorPanel.this.damageSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.ARMORPERCENT.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.armorSpinner = new JSpinner();
        this.armorSpinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));
        this.add((Component)this.armorSpinner, "2," + row);
        this.armorSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.ARMORPERCENT, (double)((Double)CombatEditorPanel.this.damageSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.DODGE.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.dodgeSpinner = new JSpinner();
        this.dodgeSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        this.add((Component)this.dodgeSpinner, "2," + row);
        this.dodgeSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.DODGE, (double)((Integer)CombatEditorPanel.this.dodgeSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.HIT.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.hitSpinner = new JSpinner();
        this.hitSpinner.setModel(new SpinnerNumberModel(0, -100, 1000, 1));
        this.add((Component)this.hitSpinner, "2," + row);
        this.hitSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.HIT, (double)((Integer)CombatEditorPanel.this.hitSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.CRITCHANCE.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.critChanceSpinner = new JSpinner();
        this.critChanceSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        this.add((Component)this.critChanceSpinner, "2," + row);
        this.critChanceSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.CRITCHANCE, (double)((Integer)CombatEditorPanel.this.critChanceSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.CRITDAMAGEAMOUNT.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.critDamageSpinner = new JSpinner();
        this.critDamageSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        this.add((Component)this.critDamageSpinner, "2," + row);
        this.critDamageSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, (double)((Integer)CombatEditorPanel.this.critDamageSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.BLOCKCHANCE.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.blockChanceSpinner = new JSpinner();
        this.blockChanceSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        this.add((Component)this.blockChanceSpinner, "2," + row);
        this.blockChanceSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.BLOCKCHANCE, (double)((Integer)CombatEditorPanel.this.blockChanceSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.BLOCKAMOUNT.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.blockAmountSpinner = new JSpinner();
        this.blockAmountSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        this.add((Component)this.blockAmountSpinner, "2," + row);
        this.blockAmountSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.BLOCKAMOUNT, (double)((Integer)CombatEditorPanel.this.blockAmountSpinner.getValue()));
            }
        });
        lblNewLabel2 = new JLabel(CalculatedAttribute.SPEED.getText());
        this.add((Component)lblNewLabel2, "1, " + ++row);
        this.speedSpinner = new JSpinner();
        this.speedSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        this.add((Component)this.speedSpinner, "2," + row);
        this.speedSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                CombatEditorPanel.this.complexEnemyTemplate.setAttribute(CalculatedAttribute.SPEED, (double)((Integer)CombatEditorPanel.this.speedSpinner.getValue()));
            }
        });
    }

    public void setComplexEnemyTemplate(ComplexEnemyTemplate enemy) {
        this.complexEnemyTemplate = enemy;
        this.healthSpinner.setValue(enemy.getHitpoints());
        this.damageSpinner.setValue(enemy.getAttribute(CalculatedAttribute.DAMAGE));
        this.armorSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.ARMORPERCENT));
        this.dodgeSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.DODGE));
        this.hitSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.HIT));
        this.critChanceSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.CRITCHANCE));
        this.critDamageSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT));
        this.blockChanceSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.BLOCKCHANCE));
        this.blockAmountSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.BLOCKAMOUNT));
        this.speedSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.SPEED));
    }
}

