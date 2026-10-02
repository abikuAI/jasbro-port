/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.ImageDataFilterListModel;
import jasbro.gui.pictures.ImageData;
import jasbro.util.EditorInterface;
import jasbro.util.ImageDataFilterPanel;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class ImageListPanel
extends JPanel {
    private JList<ImageData> imageList;
    private ImageDataFilterListModel imageDataFilterListModel;
    private EditorInterface editor;

    public ImageListPanel(EditorInterface editorTmp) {
        this.editor = editorTmp;
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        this.imageDataFilterListModel = new ImageDataFilterListModel();
        ImageDataFilterPanel filterPanel = new ImageDataFilterPanel(this.imageDataFilterListModel);
        this.add((Component)filterPanel, "1, 1");
        JScrollPane scrollPane = new JScrollPane();
        this.add((Component)scrollPane, "1, 2, fill, fill");
        this.imageList = new JList<ImageData>(this.imageDataFilterListModel);
        scrollPane.setViewportView(this.imageList);
        this.imageList.setSelectionMode(1);
        this.imageList.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent evt) {
                ImageListPanel.this.imageListValueChanged(evt);
            }
        });
        this.imageDataFilterListModel.addListDataListener(new ListDataListener(){

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                if (ImageListPanel.this.imageDataFilterListModel.getSize() == 0) {
                    ImageListPanel.this.editor.setNoImageSelected(true);
                    ImageListPanel.this.imageList.setSelectedValue(null, true);
                } else {
                    ImageListPanel.this.editor.setNoImageSelected(false);
                    ImageListPanel.this.imageList.setSelectedIndex(0);
                    ImageListPanel.this.editor.changeCurrentImage((ImageData)ImageListPanel.this.imageList.getSelectedValue());
                }
            }
        });
        this.imageList.setCellRenderer(new ListCellRenderer<ImageData>(){
            private BasicComboBoxRenderer renderer = new BasicComboBoxRenderer();

            @Override
            public Component getListCellRendererComponent(JList<? extends ImageData> list, ImageData value, int index, boolean isSelected, boolean cellHasFocus) {
                BasicComboBoxRenderer component = (BasicComboBoxRenderer)this.renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                component.setText(value.getFilename());
                return component;
            }
        });
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        this.imageList.addListSelectionListener(listSelectionListener);
    }

    private void imageListValueChanged(ListSelectionEvent evt) {
        this.editor.changeCurrentImage(this.imageList.getSelectedValue());
    }

    public JList<ImageData> getImageList() {
        return this.imageList;
    }

    public ImageData getSelectedImage() {
        return this.imageList.getSelectedValue();
    }

    public void filter() {
        this.imageDataFilterListModel.filter();
    }

    public void setImageObject(HasImagesInterface imageObject) {
        this.imageDataFilterListModel.setBase(imageObject);
        this.imageList.setSelectedIndex(0);
    }
}

