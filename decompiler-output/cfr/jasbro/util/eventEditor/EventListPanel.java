/*
 * Decompiled with CFR 0.152.
 */
package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.EventPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EventListPanel
extends JPanel {
    private JList<WorldEvent> eventList;
    private JTextField textField;

    public EventListPanel(final EventPanel eventPanel) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")}));
        JPanel newItemPanel = new JPanel();
        this.add((Component)newItemPanel, "1, 1, fill, fill");
        newItemPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), FormFactory.DEFAULT_COLSPEC}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        this.textField = new JTextField();
        newItemPanel.add((Component)this.textField, "1, 1, fill, default");
        this.textField.setColumns(10);
        JButton btnCreateNewItem = new JButton(TextUtil.t("eventEditor.createEvent"));
        btnCreateNewItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String eventId = EventListPanel.this.textField.getText().trim();
                if (eventId != null && !eventId.equals("") && !Jasbro.getInstance().getWorldEvents().containsKey(eventId) && Util.isValidFileName(eventId)) {
                    WorldEvent event = new WorldEvent(eventId);
                    Jasbro.getInstance().getWorldEvents().put(eventId, event);
                    eventPanel.setEvent(event);
                    EventListPanel.this.update();
                }
            }
        });
        newItemPanel.add((Component)btnCreateNewItem, "2, 1");
        JScrollPane scrollPane = new JScrollPane();
        this.add((Component)scrollPane, "1, 2, fill, fill");
        this.eventList = new JList();
        scrollPane.setViewportView(this.eventList);
        this.eventList.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                eventPanel.setEvent((WorldEvent)EventListPanel.this.eventList.getSelectedValue());
            }
        });
        this.update();
    }

    public void update() {
        WorldEvent[] itemArray = new WorldEvent[Jasbro.getInstance().getWorldEvents().entrySet().size()];
        itemArray = Jasbro.getInstance().getWorldEvents().values().toArray(itemArray);
        Arrays.sort(itemArray, new Comparator<WorldEvent>(){

            @Override
            public int compare(WorldEvent o1, WorldEvent o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 != null) {
                    return o1.getId().compareTo(o2.getId());
                }
                return o2.getId().compareTo(null);
            }
        });
        this.eventList.setListData((WorldEvent[])itemArray);
        this.validate();
        this.repaint();
    }
}

