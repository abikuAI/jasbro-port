/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.TriggerListPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class QuestStagePanel
extends JPanel {
    private CustomQuestStage questStage;
    private CustomQuestTemplate customQuest;
    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JCheckBox showInQuestLogCheckBox;
    private boolean selected = false;
    private JPanel contentPanel;

    public QuestStagePanel(CustomQuestStage questStageTmp, CustomQuestTemplate customQuestTmp) {
        this.questStage = questStageTmp;
        this.customQuest = customQuestTmp;
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC}));
        this.contentPanel = new JPanel();
        this.add((Component)this.contentPanel, "2, 2, fill, fill");
        this.contentPanel.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("60dlu"), RowSpec.decode("default:grow")}));
        this.showInQuestLogCheckBox = new JCheckBox(TextUtil.t("eventEditor.showInQuestLog"));
        this.contentPanel.add((Component)this.showInQuestLogCheckBox, "1, 1, left, top");
        this.showInQuestLogCheckBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                QuestStagePanel.this.questStage.setShowInQuestLog(QuestStagePanel.this.showInQuestLogCheckBox.isSelected());
            }
        });
        JPanel panel = new JPanel();
        this.contentPanel.add((Component)panel, "3, 1, fill, fill");
        panel.setLayout(new FlowLayout(1, 5, 5));
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.questStage") + ": " + this.customQuest.getQuestStages().indexOf(this.questStage));
        panel.add(lblNewLabel);
        JLabel queststageTitleLabel = new JLabel(TextUtil.t("eventEditor.questStageTitle"));
        this.contentPanel.add((Component)queststageTitleLabel, "1, 2, right, default");
        this.titleTextField = new JTextField();
        this.contentPanel.add((Component)this.titleTextField, "3, 2, fill, default");
        this.titleTextField.setColumns(10);
        this.titleTextField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                QuestStagePanel.this.questStage.setTitle(QuestStagePanel.this.titleTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                QuestStagePanel.this.questStage.setTitle(QuestStagePanel.this.titleTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                QuestStagePanel.this.questStage.setTitle(QuestStagePanel.this.titleTextField.getText());
            }
        });
        JLabel questStageDescription = new JLabel(TextUtil.t("eventEditor.questStageDescription"));
        this.contentPanel.add((Component)questStageDescription, "1, 3, right, default");
        JScrollPane scrollPane = new JScrollPane();
        this.contentPanel.add((Component)scrollPane, "3, 3, fill, fill");
        this.descriptionTextArea = new JTextArea();
        scrollPane.setViewportView(this.descriptionTextArea);
        this.descriptionTextArea.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                QuestStagePanel.this.questStage.setDescription(QuestStagePanel.this.descriptionTextArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                QuestStagePanel.this.questStage.setDescription(QuestStagePanel.this.descriptionTextArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                QuestStagePanel.this.questStage.setDescription(QuestStagePanel.this.descriptionTextArea.getText());
            }
        });
        TriggerListPanel triggerListPanel = new TriggerListPanel(this.questStage);
        this.contentPanel.add((Component)triggerListPanel, "1, 4, 3, 1, fill, fill");
        if (this.questStage != null) {
            this.showInQuestLogCheckBox.setSelected(this.questStage.isShowInQuestLog());
            this.titleTextField.setText(this.questStage.getTitle());
            this.descriptionTextArea.setText(this.questStage.getDescription());
        }
        this.setSelected(false);
        this.contentPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
        this.addMouseListener(new DelegateMouseListener(){

            @Override
            public void dispatch(MouseEvent e) {
                Container parent = e.getComponent().getParent();
                MouseEvent e2 = SwingUtilities.convertMouseEvent(e.getComponent(), e, e.getComponent().getParent());
                e2.setSource(QuestStagePanel.this);
                parent.dispatchEvent(e2);
            }
        });
    }

    public CustomQuestStage getQuestStage() {
        return this.questStage;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.setBackground(Color.BLUE);
        } else {
            this.setBackground(Color.DARK_GRAY);
        }
        this.repaint();
    }
}

