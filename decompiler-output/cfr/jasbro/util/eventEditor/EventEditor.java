/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.util.eventEditor;

import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.EventPanel;
import jasbro.util.eventEditor.QuestPanel;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventEditor
extends JFrame {
    private static final Logger log = LogManager.getLogger(EventEditor.class);
    private static EventEditor instance;

    public EventEditor() {
        instance = this;
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
        this.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
        JTabbedPane selectEditorTabbedPane = new JTabbedPane(1);
        this.getContentPane().add(selectEditorTabbedPane);
        QuestPanel questPanel = new QuestPanel();
        selectEditorTabbedPane.addTab(TextUtil.t("eventEditor.quests"), null, questPanel, null);
        EventPanel eventPanel = new EventPanel();
        selectEditorTabbedPane.addTab(TextUtil.t("eventEditor.events"), null, eventPanel, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                try {
                    EventEditor frame = new EventEditor();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    log.error("Error on creating frame", (Throwable)e);
                }
            }
        });
    }

    public static EventEditor getInstance() {
        return instance;
    }

    public static void setInstance(EventEditor instance) {
        EventEditor.instance = instance;
    }
}

