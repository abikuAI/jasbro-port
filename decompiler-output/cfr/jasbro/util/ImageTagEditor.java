/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.CharacterBase;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageTagGroup;
import jasbro.texts.TextUtil;
import jasbro.util.CharacterEditor;
import jasbro.util.PeopleImageTagPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ImageTagEditor
extends JPanel {
    private ImageData image;
    private CharacterEditor characterEditor;
    private JTabbedPane imageDataTabbedPane;
    private JList<ImageData> imageList;
    private CharacterBase characterBase;

    public ImageTagEditor() {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.imageDataTabbedPane = new JTabbedPane(1);
        this.add((Component)this.imageDataTabbedPane, "1, 1, fill, fill");
    }

    public ImageTagEditor(CharacterEditor characterEditor) {
        this();
        this.characterEditor = characterEditor;
    }

    private void addTagComponents() {
        KeyAdapter myTagKeyListener = new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent e) {
                ImageTagEditor.this.characterEditor.getImageList().dispatchEvent(e);
            }
        };
        String rowBehavior = "grow";
        for (ImageTagGroup imageTagGroup : ImageTagGroup.values()) {
            if (!imageTagGroup.isStandardGroup()) continue;
            JPanel curTabPanel = new JPanel();
            FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:grow"), RowSpec.decode("fill:default:grow")});
            curTabPanel.setLayout(layout);
            this.imageDataTabbedPane.addTab(imageTagGroup.getText(), null, curTabPanel, null);
            int amount = 0;
            for (final ImageTag imageTag : ImageTag.values()) {
                if (imageTag.getImageTagGroup() != imageTagGroup) continue;
                if (amount > 0 && amount % 18 == 0) {
                    if (ImageTag.values().length - amount < 15) {
                        rowBehavior = "none";
                    }
                    curTabPanel = new JPanel();
                    layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:" + rowBehavior), RowSpec.decode("fill:default:grow")});
                    curTabPanel.setLayout(layout);
                    this.imageDataTabbedPane.addTab(imageTagGroup.getText() + " " + (amount / 18 + 1), null, curTabPanel, null);
                } else if (amount > 0 && amount % 3 == 0) {
                    layout.insertRow(amount % 18 / 3 + 1, RowSpec.decode("pref:" + rowBehavior));
                }
                ImageTagCheckbox checkBox = new ImageTagCheckbox(imageTag, this.image.getTags().contains((Object)imageTag));
                curTabPanel.add((Component)checkBox, amount % 3 + 1 + ", " + (amount % 18 / 3 + 1) + ", fill, top");
                checkBox.addKeyListener(myTagKeyListener);
                checkBox.addItemListener(new ItemListener(){

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        JCheckBox box = (JCheckBox)e.getSource();
                        if (box.isSelected()) {
                            for (ImageData image : ImageTagEditor.this.imageList.getSelectedValuesList()) {
                                image.addTag(imageTag);
                            }
                        } else {
                            for (ImageData image : ImageTagEditor.this.imageList.getSelectedValuesList()) {
                                image.removeTag(imageTag);
                            }
                        }
                        if (ImageTagEditor.this.characterBase != null) {
                            ImageTagEditor.this.characterBase.setChanged(true);
                        }
                    }
                });
                ++amount;
            }
        }
        PeopleImageTagPanel peopleImageTagPabel = new PeopleImageTagPanel(this.image, this.characterBase);
        this.imageDataTabbedPane.addTab(TextUtil.t("imagetag.people"), null, peopleImageTagPabel, null);
        JPanel curTabPanel = new JPanel();
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("pref:none"), RowSpec.decode("fill:default:grow")});
        curTabPanel.setLayout(layout);
        this.imageDataTabbedPane.addTab("Other ", null, curTabPanel, null);
        final JTextField customTextField = new JTextField(this.image.getCustomText());
        curTabPanel.add((Component)new JLabel("Custom text:"), "1, 1, fill, top");
        curTabPanel.add((Component)customTextField, "2, 1, fill, top");
        customTextField.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                for (ImageData image : ImageTagEditor.this.imageList.getSelectedValuesList()) {
                    image.setCustomText(customTextField.getText());
                }
                if (ImageTagEditor.this.characterBase != null) {
                    ImageTagEditor.this.characterBase.setChanged(true);
                }
            }
        });
        this.repaint();
    }

    public void setImage(ImageData image, CharacterBase characterBase) {
        int selectedTab = this.imageDataTabbedPane.getSelectedIndex();
        this.characterBase = characterBase;
        if (this.image == null) {
            selectedTab = 0;
        }
        this.imageDataTabbedPane.removeAll();
        if (image != null) {
            this.image = image;
            this.addTagComponents();
            this.imageDataTabbedPane.setSelectedIndex(selectedTab);
        }
        this.validate();
    }

    public void setImageList(JList<ImageData> imageList) {
        this.imageList = imageList;
    }

    public class ImageTagCheckbox
    extends JCheckBox {
        public ImageTagCheckbox(ImageTag imageTag, boolean selected) {
            this.setText(imageTag.getText());
            this.setSelected(selected);
            String tooltip = imageTag.getDescription();
            if (imageTag == ImageTag.CLOTHED || imageTag == ImageTag.NAKED || imageTag == ImageTag.SLEEP || imageTag == ImageTag.MAID || imageTag == ImageTag.FUTA || imageTag == ImageTag.LESBIAN || imageTag == ImageTag.ORAL || imageTag == ImageTag.VAGINAL || imageTag == ImageTag.ANAL || imageTag == ImageTag.TITFUCK || imageTag == ImageTag.BONDAGE || imageTag == ImageTag.FOREPLAY || imageTag == ImageTag.GROUP || imageTag == ImageTag.SWIMSUIT || imageTag == ImageTag.ICON) {
                if (tooltip == null) {
                    tooltip = "";
                }
                tooltip = tooltip + "\n" + TextUtil.t("imagetag.importantTag");
                this.setForeground(Color.decode("#085508"));
            }
            if (imageTag.isExcludeTag() && imageTag.getImageTagGroup() != ImageTagGroup.FILTERTAGS) {
                if (tooltip == null) {
                    tooltip = "";
                }
                tooltip = tooltip + "\n\n" + TextUtil.t("imagetag.exclusiveTag");
                this.setForeground(Color.RED);
            }
            if (tooltip != null) {
                this.setText(this.getText() + " (i)");
                this.setToolTipText(TextUtil.htmlPreformatted(tooltip));
            }
        }
    }
}

