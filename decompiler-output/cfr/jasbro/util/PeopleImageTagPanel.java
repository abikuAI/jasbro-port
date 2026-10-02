/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.CharacterBase;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageTagGroup;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class PeopleImageTagPanel
extends JPanel {
    private PeopleImageTagPanel() {
        this(null, null);
    }

    public PeopleImageTagPanel(final ImageData image, final CharacterBase characterBase) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC}));
        JLabel lblNewLabel = new JLabel(TextUtil.t("imagetag.characterfuta"));
        this.add((Component)lblNewLabel, "1, 1, right, default");
        final JComboBox<Object> characterFutaCombobox = new JComboBox<Object>();
        this.add(characterFutaCombobox, "2, 1, fill, default");
        characterFutaCombobox.addItem(null);
        for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.CHARACTERGENDER)) {
            characterFutaCombobox.addItem((Object)imageTag);
            if (!image.getTags().contains((Object)imageTag)) continue;
            characterFutaCombobox.setSelectedItem((Object)imageTag);
        }
        characterFutaCombobox.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent((JList<?>)list, value, index, isSelected, hasFocus);
                ImageTag imageTag = (ImageTag)((Object)value);
                if (imageTag != null) {
                    label.setText(imageTag.getText());
                    label.setToolTipText(imageTag.getDescription());
                } else {
                    label.setText(" ");
                }
                return label;
            }
        });
        characterFutaCombobox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                ImageTag imageTag = (ImageTag)((Object)characterFutaCombobox.getSelectedItem());
                for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.CHARACTERGENDER)) {
                    image.getTags().remove((Object)curImageTag);
                }
                characterFutaCombobox.setToolTipText("");
                if (imageTag != null) {
                    image.addTag(imageTag);
                    characterFutaCombobox.setToolTipText(imageTag.getDescription());
                }
                characterBase.setChanged(true);
                PeopleImageTagPanel.this.repaint();
            }
        });
        lblNewLabel = new JLabel(TextUtil.t("imagetag.dominantPosition"));
        lblNewLabel.setToolTipText(TextUtil.t("imagetag.dominantPosition.description"));
        this.add((Component)lblNewLabel, "1, 2, right, default");
        final JComboBox<Object> dominantPositionCombobox = new JComboBox<Object>();
        this.add(dominantPositionCombobox, "2, 2, fill, default");
        dominantPositionCombobox.addItem(null);
        for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.DOMINANCE)) {
            dominantPositionCombobox.addItem((Object)imageTag);
            if (!image.getTags().contains((Object)imageTag)) continue;
            dominantPositionCombobox.setSelectedItem((Object)imageTag);
        }
        dominantPositionCombobox.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent((JList<?>)list, value, index, isSelected, hasFocus);
                ImageTag imageTag = (ImageTag)((Object)value);
                if (imageTag != null) {
                    label.setText(imageTag.getText());
                    label.setToolTipText(imageTag.getDescription());
                } else {
                    label.setText(" ");
                }
                return label;
            }
        });
        dominantPositionCombobox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                ImageTag imageTag = (ImageTag)((Object)dominantPositionCombobox.getSelectedItem());
                for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.DOMINANCE)) {
                    image.getTags().remove((Object)curImageTag);
                }
                dominantPositionCombobox.setToolTipText("");
                if (imageTag != null) {
                    image.addTag(imageTag);
                    dominantPositionCombobox.setToolTipText(imageTag.getDescription());
                }
                characterBase.setChanged(true);
                PeopleImageTagPanel.this.repaint();
            }
        });
        lblNewLabel = new JLabel(TextUtil.t("imagetag.genderOthers"));
        lblNewLabel.setToolTipText(TextUtil.t("imagetag.genderOthers.description"));
        this.add((Component)lblNewLabel, "1, 3, right, default");
        final JComboBox<Object> genderOthersCombobox = new JComboBox<Object>();
        this.add(genderOthersCombobox, "2, 3, fill, default");
        genderOthersCombobox.addItem(null);
        for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.OTHERGENDER)) {
            genderOthersCombobox.addItem((Object)imageTag);
            if (!image.getTags().contains((Object)imageTag)) continue;
            genderOthersCombobox.setSelectedItem((Object)imageTag);
        }
        genderOthersCombobox.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent((JList<?>)list, value, index, isSelected, hasFocus);
                ImageTag imageTag = (ImageTag)((Object)value);
                if (imageTag != null) {
                    label.setText(imageTag.getText());
                    label.setToolTipText(imageTag.getDescription());
                } else {
                    label.setText(" ");
                }
                return label;
            }
        });
        genderOthersCombobox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                ImageTag imageTag = (ImageTag)((Object)genderOthersCombobox.getSelectedItem());
                for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.OTHERGENDER)) {
                    image.getTags().remove((Object)curImageTag);
                }
                genderOthersCombobox.setToolTipText("");
                if (imageTag != null) {
                    image.addTag(imageTag);
                    genderOthersCombobox.setToolTipText(imageTag.getDescription());
                }
                characterBase.setChanged(true);
                PeopleImageTagPanel.this.repaint();
            }
        });
        lblNewLabel = new JLabel(TextUtil.t("imagetag.amountOthers"));
        lblNewLabel.setToolTipText(TextUtil.t("imagetag.amountOthers.description"));
        this.add((Component)lblNewLabel, "1, 4, right, default");
        final JComboBox<Object> amountOthersCombobox = new JComboBox<Object>();
        this.add(amountOthersCombobox, "2, 4, fill, default");
        amountOthersCombobox.addItem(null);
        for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.AMOUNTPEOPLE)) {
            amountOthersCombobox.addItem((Object)imageTag);
            if (!image.getTags().contains((Object)imageTag)) continue;
            amountOthersCombobox.setSelectedItem((Object)imageTag);
        }
        amountOthersCombobox.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent((JList<?>)list, value, index, isSelected, hasFocus);
                ImageTag imageTag = (ImageTag)((Object)value);
                if (imageTag != null) {
                    label.setText(imageTag.getText());
                    label.setToolTipText(imageTag.getDescription());
                } else {
                    label.setText(" ");
                }
                return label;
            }
        });
        amountOthersCombobox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                ImageTag imageTag = (ImageTag)((Object)amountOthersCombobox.getSelectedItem());
                for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.AMOUNTPEOPLE)) {
                    image.getTags().remove((Object)curImageTag);
                }
                amountOthersCombobox.setToolTipText("");
                if (imageTag != null) {
                    image.addTag(imageTag);
                    amountOthersCombobox.setToolTipText(imageTag.getDescription());
                }
                characterBase.setChanged(true);
                PeopleImageTagPanel.this.repaint();
            }
        });
    }
}

