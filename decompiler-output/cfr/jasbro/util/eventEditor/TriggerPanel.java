/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.Trigger;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.triggerRequirementPanels.TriggerRequirementPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TriggerPanel
extends JPanel {
    private static final Logger log = LogManager.getLogger(TriggerPanel.class);
    private Trigger trigger;
    private CustomQuestStage customQuestStage;
    private JComboBox<String> eventComboBox;
    private TriggerRequirementPanel selectedRequirementPanel;
    private JPanel triggerRequirementPanel;
    private WorldEvent worldEvent;
    private JPanel customActivityPanel;
    private JTextField textField;

    public TriggerPanel(Trigger triggerTmp, WorldEvent worldEvent) {
        this.worldEvent = worldEvent;
        this.init(triggerTmp);
    }

    public TriggerPanel(Trigger triggerTmp, CustomQuestStage customQuestStage) {
        this.customQuestStage = customQuestStage;
        this.init(triggerTmp);
    }

    public void init(Trigger triggerTmp) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("left:default"), ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC}));
        this.trigger = triggerTmp;
        JLabel label = new JLabel(TextUtil.t("eventEditor.triggerType"));
        this.add((Component)label, "1, 1, left, center");
        final JComboBox<Trigger.TriggerType> triggerTypeComboBox = new JComboBox<Trigger.TriggerType>();
        this.add(triggerTypeComboBox, "2, 1, fill, top");
        for (Trigger.TriggerType triggerType : Trigger.TriggerType.values()) {
            triggerTypeComboBox.addItem(triggerType);
        }
        triggerTypeComboBox.setSelectedItem((Object)this.trigger.getTriggerType());
        triggerTypeComboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TriggerPanel.this.trigger.setTriggerType((Trigger.TriggerType)((Object)triggerTypeComboBox.getSelectedItem()));
                if (TriggerPanel.this.trigger.getTriggerType() == Trigger.TriggerType.CUSTOMACTIVITY) {
                    TriggerPanel.this.customActivityPanel.setVisible(true);
                } else {
                    TriggerPanel.this.customActivityPanel.setVisible(false);
                    TriggerPanel.this.trigger.setActivityDescription(null);
                    TriggerPanel.this.trigger.setActivityLabel(null);
                }
            }
        });
        JButton btnNewButton = new JButton(TextUtil.t("eventEditor.deleteTrigger"));
        btnNewButton.setForeground(Color.RED);
        btnNewButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (TriggerPanel.this.customQuestStage != null) {
                    TriggerPanel.this.customQuestStage.removeTrigger(TriggerPanel.this.trigger);
                } else {
                    TriggerPanel.this.worldEvent.getTriggers().remove(TriggerPanel.this.trigger);
                }
                Container parent = TriggerPanel.this.getParent();
                parent.remove(TriggerPanel.this);
                parent.validate();
                parent.repaint();
            }
        });
        this.add((Component)btnNewButton, "3, 1, 2, 1, right, top");
        this.customActivityPanel = new JPanel();
        this.add((Component)this.customActivityPanel, "1, 2, 4, 1, fill, fill");
        this.customActivityPanel.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        JLabel lblNewLabel_2 = new JLabel(TextUtil.t("eventEditor.activityName"));
        this.customActivityPanel.add((Component)lblNewLabel_2, "1, 1, right, default");
        this.textField = new JTextField();
        this.customActivityPanel.add((Component)this.textField, "2, 1, fill, default");
        this.textField.setText(this.trigger.getActivityLabel());
        this.textField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                TriggerPanel.this.trigger.setActivityLabel(TriggerPanel.this.textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TriggerPanel.this.trigger.setActivityLabel(TriggerPanel.this.textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                TriggerPanel.this.trigger.setActivityLabel(TriggerPanel.this.textField.getText());
            }
        });
        JLabel lblNewLabel_3 = new JLabel(TextUtil.t("eventEditor.activityDescription"));
        this.customActivityPanel.add((Component)lblNewLabel_3, "3, 1, right, default");
        final JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(31);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(this.trigger.getActivityDescription());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                TriggerPanel.this.trigger.setActivityDescription(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TriggerPanel.this.trigger.setActivityDescription(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                TriggerPanel.this.trigger.setActivityDescription(textArea.getText());
            }
        });
        this.customActivityPanel.add((Component)scrollPane, "4, 1, fill, fill");
        this.customActivityPanel.setVisible(this.trigger.getTriggerType() == Trigger.TriggerType.CUSTOMACTIVITY);
        JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.requirements"));
        this.add((Component)lblNewLabel_1, "1, 3, left, default");
        final JComboBox<TriggerRequirementType> requirementComboBox = new JComboBox<TriggerRequirementType>();
        this.add(requirementComboBox, "2, 3, fill, default");
        for (TriggerRequirementType triggerRequirementType : TriggerRequirementType.values()) {
            requirementComboBox.addItem(triggerRequirementType);
        }
        JButton addRequirementButton = new JButton(TextUtil.t("eventEditor.addRequirement"));
        this.add((Component)addRequirementButton, "3, 3");
        addRequirementButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TriggerRequirement triggerRequirement = ((TriggerRequirementType)((Object)requirementComboBox.getSelectedItem())).getRequirementClass().newInstance();
                    TriggerRequirementPanel triggerPanel = new TriggerRequirementPanel(triggerRequirement, new MyMouseListener());
                    if (TriggerPanel.this.triggerRequirementPanel.getComponents().length == 0) {
                        TriggerPanel.this.triggerRequirementPanel.add(triggerPanel);
                        TriggerPanel.this.trigger.setRequirement(triggerRequirement);
                        TriggerPanel.this.setSelected(triggerPanel);
                    } else if (TriggerPanel.this.selectedRequirementPanel != null && TriggerPanel.this.selectedRequirementPanel.getTriggerRequirement().canAddRequirement(triggerRequirement)) {
                        TriggerPanel.this.selectedRequirementPanel.getTriggerRequirement().getSubRequirements().add(triggerRequirement);
                        TriggerPanel.this.selectedRequirementPanel.addPanel(triggerPanel);
                        TriggerPanel.this.setSelected(triggerPanel);
                    }
                    Container parent = TriggerPanel.this.getParent();
                    parent.validate();
                    parent.repaint();
                }
                catch (Exception ex) {
                    log.error("Error when creating trigger requirement panel", (Throwable)ex);
                }
            }
        });
        JButton deleteRequirementButton = new JButton(TextUtil.t("eventEditor.deleteRequirement"));
        this.add((Component)deleteRequirementButton, "4, 3");
        deleteRequirementButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (TriggerPanel.this.selectedRequirementPanel != null) {
                    Container parent = TriggerPanel.this.selectedRequirementPanel.getParent();
                    if (parent == TriggerPanel.this.triggerRequirementPanel) {
                        TriggerPanel.this.trigger.setRequirement(null);
                        TriggerPanel.this.triggerRequirementPanel.remove(TriggerPanel.this.selectedRequirementPanel);
                    } else {
                        parent.remove(TriggerPanel.this.selectedRequirementPanel);
                        while (!(parent instanceof TriggerRequirementPanel)) {
                            parent = parent.getParent();
                        }
                        ((TriggerRequirementPanel)parent).getTriggerRequirement().getSubRequirements().remove(TriggerPanel.this.selectedRequirementPanel.getTriggerRequirement());
                    }
                    TriggerPanel.this.selectedRequirementPanel = null;
                    parent = TriggerPanel.this.getParent();
                    parent.validate();
                    parent.repaint();
                }
            }
        });
        this.triggerRequirementPanel = new JPanel();
        this.add((Component)this.triggerRequirementPanel, "1, 4, 4, 1, fill, fill");
        this.triggerRequirementPanel.setLayout(new GridLayout(0, 1, 0, 0));
        if (this.customQuestStage != null) {
            JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.triggerEvent"));
            this.add((Component)lblNewLabel, "1, 5, right, default");
            this.eventComboBox = new JComboBox();
            this.add(this.eventComboBox, "2, 5, fill, default");
            this.eventComboBox.setEditable(true);
            ArrayList<String> eventIds = new ArrayList<String>(Jasbro.getInstance().getWorldEvents().keySet());
            Collections.sort(eventIds);
            for (String worldEvent : eventIds) {
                this.eventComboBox.addItem(worldEvent);
            }
            this.eventComboBox.setSelectedItem(this.customQuestStage.getEventName(this.trigger));
            this.eventComboBox.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    TriggerPanel.this.customQuestStage.getTriggerToWorldEventMap().put(TriggerPanel.this.trigger, (String)TriggerPanel.this.eventComboBox.getSelectedItem());
                }
            });
        }
        if (this.trigger.getRequirement() != null) {
            this.addTriggerRequirementPanel(this.trigger.getRequirement());
        }
    }

    public void addTriggerRequirementPanel(TriggerRequirement triggerRequirement) {
        TriggerRequirementPanel triggerRequirementPanel = new TriggerRequirementPanel(triggerRequirement, new MyMouseListener());
        this.triggerRequirementPanel.add(triggerRequirementPanel);
    }

    public void setSelected(TriggerRequirementPanel triggerRequirementPanel) {
        if (this.selectedRequirementPanel != null) {
            this.selectedRequirementPanel.setSelected(false);
        }
        this.selectedRequirementPanel = triggerRequirementPanel;
        triggerRequirementPanel.setSelected(true);
        this.validate();
        this.repaint();
    }

    private class MyMouseListener
    extends MouseAdapter {
        private MyMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() instanceof TriggerRequirementPanel) {
                TriggerRequirementPanel newPanel = (TriggerRequirementPanel)e.getSource();
                TriggerPanel.this.setSelected(newPanel);
            }
        }
    }
}

