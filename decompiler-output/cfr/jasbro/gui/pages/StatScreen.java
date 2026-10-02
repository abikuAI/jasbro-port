/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.pages.subView.StatPanel;
import jasbro.stats.StatCollector;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class StatScreen
extends JPanel
implements MessageInterface {
    private StatCollector statCollector;
    private JTabbedPane tabbedPane;
    private Time time;
    private boolean important = true;

    private StatScreen() {
        this.init();
    }

    public StatScreen(StatCollector statCollector, Time time, boolean important) {
        this.statCollector = statCollector;
        this.time = time;
        this.important = important;
    }

    @Override
    public void init() {
        this.removeAll();
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.setBackground(new Color(240, 230, 140));
        UIManager.put("TabbedPane.contentOpaque", false);
        this.tabbedPane = new JTabbedPane(1);
        this.add(this.tabbedPane);
        this.tabbedPane.setOpaque(false);
        StatPanel statPanel = new StatPanel(this.statCollector.getDailyData());
        statPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                Jasbro.getInstance().getGui().removeLayer(StatScreen.this);
            }
        });
        this.tabbedPane.addTab(TextUtil.t("stats.daily"), null, statPanel, null);
        statPanel = new StatPanel(this.statCollector.getShiftDataMap(Time.MORNING));
        statPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                Jasbro.getInstance().getGui().removeLayer(StatScreen.this);
            }
        });
        this.tabbedPane.addTab(Time.MORNING.getText(), null, statPanel, null);
        statPanel = new StatPanel(this.statCollector.getShiftDataMap(Time.AFTERNOON));
        statPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                Jasbro.getInstance().getGui().removeLayer(StatScreen.this);
            }
        });
        this.tabbedPane.addTab(Time.AFTERNOON.getText(), null, statPanel, null);
        statPanel = new StatPanel(this.statCollector.getShiftDataMap(Time.NIGHT));
        statPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                Jasbro.getInstance().getGui().removeLayer(StatScreen.this);
            }
        });
        this.tabbedPane.addTab(Time.NIGHT.getText(), null, statPanel, null);
        if (this.statCollector.getPreviousDayData() != null) {
            statPanel = new StatPanel(this.statCollector.getPreviousDayData());
            statPanel.addMouseListener(new MouseAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    Jasbro.getInstance().getGui().removeLayer(StatScreen.this);
                }
            });
            this.tabbedPane.addTab(TextUtil.t("stats.previousDay"), null, statPanel, null);
        }
        if (this.time == Time.NIGHT) {
            this.tabbedPane.setSelectedIndex(3);
        } else if (this.time == Time.AFTERNOON) {
            this.tabbedPane.setSelectedIndex(2);
        } else if (this.time == Time.MORNING) {
            this.tabbedPane.setSelectedIndex(1);
        }
        this.validate();
        this.repaint();
    }

    @Override
    public boolean isPriorityMessage() {
        return this.important;
    }

    public CharacterLocation getCharacterLocation() {
        return null;
    }

    public void setCharacterLocation(CharacterLocation characterLocation) {
    }

    @Override
    public void setMessageGroupObject(Object charcterGroupObject) {
    }

    @Override
    public Object getMessageGroupObject() {
        return null;
    }
}

