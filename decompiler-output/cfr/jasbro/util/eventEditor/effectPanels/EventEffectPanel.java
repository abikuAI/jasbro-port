/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.util.eventEditor.effectPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.gui.GuiUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventEffectPanel
extends JPanel {
    private static final Logger log = LogManager.getLogger(EventEffectPanel.class);
    private boolean selected = false;
    private WorldEventEffect worldEventEffect;
    private JPanel subeffectPanel;
    private JPanel dataPanel;
    private WorldEvent worldEvent;

    public EventEffectPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp, MouseListener mouseListener) {
        this.worldEventEffect = worldEventEffectTmp;
        this.worldEvent = worldEventTmp;
        this.setBackground(Color.GRAY);
        this.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_COLSPEC}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_ROWSPEC}));
        this.subeffectPanel = new JPanel();
        this.subeffectPanel.setOpaque(false);
        this.subeffectPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
        this.add((Component)this.subeffectPanel, "2, 3, fill, fill");
        BoxLayout boxLayout = new BoxLayout(this.subeffectPanel, 1);
        this.subeffectPanel.setLayout(boxLayout);
        this.add((Component)new JLabel(this.worldEventEffect.getType().getText() + " "), "2, 1");
        if (this.worldEventEffect != null) {
            try {
                Class<? extends JPanel> panelClass = this.worldEventEffect.getType().getEventPanelClass();
                this.dataPanel = panelClass != null ? panelClass.getConstructor(WorldEventEffect.class, WorldEvent.class).newInstance(this.getWorldEventEffect(), this.worldEvent) : new JPanel();
                this.dataPanel.setOpaque(false);
                this.dataPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
                this.add((Component)this.dataPanel, "2, 2, fill, fill");
            }
            catch (Exception ex) {
                log.error("Error when creating sub panel", (Throwable)ex);
            }
            this.addMouseListener(mouseListener);
            for (WorldEventEffect effect : this.worldEventEffect.getSubEffects()) {
                EventEffectPanel eventEffectPanel = new EventEffectPanel(effect, this.worldEvent, mouseListener);
                this.subeffectPanel.add(eventEffectPanel);
                eventEffectPanel.addMouseListener(mouseListener);
            }
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.setBackground(Color.BLUE);
        } else {
            this.setBackground(Color.GRAY);
        }
        this.repaint();
    }

    public WorldEventEffect getWorldEventEffect() {
        return this.worldEventEffect;
    }

    public void addPanel(EventEffectPanel eventEffectPanel) {
        this.subeffectPanel.add(eventEffectPanel);
    }

    public void removePanel(EventEffectPanel eventEffectPanel) {
        this.subeffectPanel.remove(eventEffectPanel);
    }

    public JPanel getSubeffectPanel() {
        return this.subeffectPanel;
    }

    public JPanel getDataPanel() {
        return this.dataPanel;
    }
}

