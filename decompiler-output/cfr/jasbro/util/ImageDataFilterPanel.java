/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.gui.ImageDataFilterListModel;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ImageDataFilterPanel
extends JPanel {
    private JTextField searchField;
    private ImageDataFilterListModel listModel;
    private JCheckBox chckbxShowOnlyUntagged;
    private JComboBox<ImageTag> comboBox;

    public ImageDataFilterPanel(ImageDataFilterListModel listModelTemp) {
        this.listModel = listModelTemp;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC}));
        JPanel topPanel = new JPanel();
        this.add((Component)topPanel, "1, 2, fill, fill");
        topPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("15dlu")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        final String searchTerm = TextUtil.t("ui.search");
        MyButton resetFilterButton = new MyButton("", new ImageData("images/icons/x.png"), new ImageData("images/icons/x.png"));
        topPanel.add((Component)resetFilterButton, "2, 1, fill, fill");
        resetFilterButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ImageDataFilterPanel.this.chckbxShowOnlyUntagged.setSelected(false);
                ImageDataFilterPanel.this.comboBox.setSelectedItem(null);
                ImageDataFilterPanel.this.searchField.setText("");
                ImageDataFilterPanel.this.listModel.setFilter(new ImageDataFilterListModel.Filter());
                ImageDataFilterPanel.this.repaint();
            }
        });
        this.searchField = new JTextField();
        topPanel.add((Component)this.searchField, "1, 1, fill, fill");
        this.searchField.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                if (ImageDataFilterPanel.this.searchField.getText().equals(searchTerm)) {
                    ImageDataFilterPanel.this.searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ImageDataFilterPanel.this.searchField.getText().isEmpty()) {
                    ImageDataFilterPanel.this.searchField.setText(searchTerm);
                }
            }
        });
        this.searchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!ImageDataFilterPanel.this.searchField.getText().equals(searchTerm)) {
                    ImageDataFilterPanel.this.listModel.getFilter().setSearchString(ImageDataFilterPanel.this.searchField.getText());
                    ImageDataFilterPanel.this.listModel.filter();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }
        });
        JPanel bottomPanel = new JPanel();
        this.add((Component)bottomPanel, "1, 3, fill, fill");
        bottomPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.chckbxShowOnlyUntagged = new JCheckBox("Show only untagged");
        bottomPanel.add((Component)this.chckbxShowOnlyUntagged, "1, 1");
        this.chckbxShowOnlyUntagged.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ImageDataFilterPanel.this.listModel.getFilter().setNoTagsOnly(ImageDataFilterPanel.this.chckbxShowOnlyUntagged.isSelected());
                ImageDataFilterPanel.this.listModel.filter();
            }
        });
        this.comboBox = new JComboBox();
        this.comboBox.addItem(null);
        for (ImageTag imageTag : ImageTag.values()) {
            if (!imageTag.getImageTagGroup().isStandardGroup()) continue;
            this.comboBox.addItem(imageTag);
        }
        bottomPanel.add(this.comboBox, "2, 1, fill, default");
        this.comboBox.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ImageDataFilterPanel.this.listModel.getFilter().setImageTag((ImageTag)((Object)ImageDataFilterPanel.this.comboBox.getSelectedItem()));
                ImageDataFilterPanel.this.listModel.filter();
            }
        });
    }

    public ImageDataFilterListModel getListModel() {
        return this.listModel;
    }

    public void setListModel(ImageDataFilterListModel listModel) {
        this.listModel = listModel;
    }
}

