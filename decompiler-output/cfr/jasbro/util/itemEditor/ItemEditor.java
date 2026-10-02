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
import jasbro.game.items.Item;
import jasbro.util.itemEditor.ItemEditorPanel;
import jasbro.util.itemEditor.ItemListPanel;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemEditor
extends JFrame {
    private static final Logger log = LogManager.getLogger(ItemEditor.class);
    private JPanel contentPane;
    private ItemEditorPanel itemEditorPanel;
    private ItemListPanel itemList;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                try {
                    ItemEditor frame = new ItemEditor();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    log.error("Error on creating frame", (Throwable)e);
                }
            }
        });
    }

    public ItemEditor() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("Uncaught Exception", e);
            }
        });
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        ToolTipManager.sharedInstance().setInitialDelay(100);
        this.setExtendedState(this.getExtendedState() | 6);
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 450, 300);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow(12)")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.itemList = new ItemListPanel(this);
        this.contentPane.add((Component)this.itemList, "1, 1, fill, fill");
    }

    public void setItem(Item item) {
        if (this.itemEditorPanel != null) {
            this.contentPane.remove(this.itemEditorPanel);
        }
        if (item != null) {
            this.itemEditorPanel = new ItemEditorPanel(item, this);
            this.contentPane.add((Component)this.itemEditorPanel, "3, 1, fill, fill");
        } else {
            this.itemList.updateList();
        }
        this.contentPane.validate();
        this.contentPane.repaint();
    }
}

