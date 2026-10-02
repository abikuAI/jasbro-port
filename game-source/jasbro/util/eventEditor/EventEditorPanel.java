package jasbro.util.eventEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.world.customContent.EventAndQuestFileLoader;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.effectPanels.EventEffectPanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventEditorPanel extends JPanel {
   private static final Logger log = LogManager.getLogger(EventEditorPanel.class);
   private WorldEvent worldEvent;
   private EventEffectPanel selectedEventEffectPanel;
   private JPanel eventEffectMasterPanel;
   private JPanel mainPanel;
   private JButton saveButton;
   private TriggerListPanel triggerListPanel;

   public EventEditorPanel(WorldEvent worldEventTmp) {
      this.worldEvent = worldEventTmp;
      this.setLayout(
         new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow(6)")})
      );
      JPanel buttonPanel = new JPanel();
      this.add(buttonPanel, "1, 1, fill, fill");
      buttonPanel.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               FormFactory.DEFAULT_COLSPEC,
               FormFactory.DEFAULT_COLSPEC,
               FormFactory.UNRELATED_GAP_COLSPEC,
               ColumnSpec.decode("12dlu"),
               ColumnSpec.decode("12dlu"),
               ColumnSpec.decode("20dlu"),
               FormFactory.DEFAULT_COLSPEC,
               ColumnSpec.decode("default:grow")
            },
            new RowSpec[]{RowSpec.decode("default:grow")}
         )
      );
      final JComboBox<WorldEventEffectType> worldEventComboBox = new JComboBox<>();
      buttonPanel.add(worldEventComboBox, "1, 1");
      JButton btnAddEventEffect = new JButton(TextUtil.t("eventEditor.addEventEffect"));
      buttonPanel.add(btnAddEventEffect, "2, 1");
      btnAddEventEffect.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                  EventEffectPanel effectPanel = new EventEffectPanel(
                     ((WorldEventEffectType)worldEventComboBox.getSelectedItem()).getEventClass().newInstance(),
                     EventEditorPanel.this.worldEvent,
                     EventEditorPanel.this.new MyMouseListener()
                  );
                  if (EventEditorPanel.this.eventEffectMasterPanel.getComponents().length == 0) {
                     EventEditorPanel.this.eventEffectMasterPanel.add(effectPanel);
                     EventEditorPanel.this.worldEvent.getEffects().add(effectPanel.getWorldEventEffect());
                     EventEditorPanel.this.setSelected(effectPanel);
                  } else if (EventEditorPanel.this.selectedEventEffectPanel != null
                     && EventEditorPanel.this.selectedEventEffectPanel.getWorldEventEffect().canAddSubEffect()) {
                     ((WorldEventEffectContainer)EventEditorPanel.this.selectedEventEffectPanel.getWorldEventEffect())
                        .addEffect(effectPanel.getWorldEventEffect());
                     EventEditorPanel.this.selectedEventEffectPanel.addPanel(effectPanel);
                     EventEditorPanel.this.setSelected(effectPanel);
                  }

                  EventEditorPanel.this.validate();
                  EventEditorPanel.this.repaint();
               } catch (Exception ex) {
                  EventEditorPanel.log.error("Error when creating event effect panel", ex);
               }
            }
         }
      );
      MyImage effectUp = new MyImage(new ImageData("images/icons/arrow_up.png"));
      buttonPanel.add(effectUp, "4, 1, fill, fill");
      effectUp.addMouseListener(
         new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (EventEditorPanel.this.selectedEventEffectPanel != null) {
                  EventEffectPanel parentEffectPanel = (EventEffectPanel)SwingUtilities.getAncestorOfClass(
                     EventEffectPanel.class, EventEditorPanel.this.selectedEventEffectPanel
                  );
                  if (parentEffectPanel != null) {
                     WorldEventEffect selectedEventEffect = EventEditorPanel.this.selectedEventEffectPanel.getWorldEventEffect();
                     WorldEventEffect parentEventEffect = parentEffectPanel.getWorldEventEffect();
                     int index = parentEventEffect.getSubEffects().indexOf(selectedEventEffect);
                     if (index > 0) {
                        index--;
                        parentEventEffect.getSubEffects().remove(selectedEventEffect);
                        parentEventEffect.getSubEffects().add(index, selectedEventEffect);
                        parentEffectPanel.getSubeffectPanel().remove(EventEditorPanel.this.selectedEventEffectPanel);
                        parentEffectPanel.getSubeffectPanel().add(EventEditorPanel.this.selectedEventEffectPanel, index);
                        EventEditorPanel.this.validate();
                        EventEditorPanel.this.repaint();
                     }
                  }
               }
            }
         }
      );
      MyImage effectDown = new MyImage(new ImageData("images/icons/arrow_down.png"));
      buttonPanel.add(effectDown, "5, 1, fill, fill");
      effectDown.addMouseListener(
         new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (EventEditorPanel.this.selectedEventEffectPanel != null) {
                  EventEffectPanel parentEffectPanel = (EventEffectPanel)SwingUtilities.getAncestorOfClass(
                     EventEffectPanel.class, EventEditorPanel.this.selectedEventEffectPanel
                  );
                  if (parentEffectPanel != null) {
                     WorldEventEffect selectedEventEffect = EventEditorPanel.this.selectedEventEffectPanel.getWorldEventEffect();
                     WorldEventEffect parentEventEffect = parentEffectPanel.getWorldEventEffect();
                     int index = parentEventEffect.getSubEffects().indexOf(selectedEventEffect);
                     if (index < parentEventEffect.getSubEffects().size() - 1) {
                        index++;
                        parentEventEffect.getSubEffects().remove(selectedEventEffect);
                        parentEventEffect.getSubEffects().add(index, selectedEventEffect);
                        parentEffectPanel.getSubeffectPanel().remove(EventEditorPanel.this.selectedEventEffectPanel);
                        parentEffectPanel.getSubeffectPanel().add(EventEditorPanel.this.selectedEventEffectPanel, index);
                        EventEditorPanel.this.validate();
                        EventEditorPanel.this.repaint();
                     }
                  }
               }
            }
         }
      );
      JButton btnDeleteEventEffect = new JButton(TextUtil.t("eventEditor.deleteEventEffect"));
      buttonPanel.add(btnDeleteEventEffect, "7, 1");
      btnDeleteEventEffect.setForeground(Color.RED);
      btnDeleteEventEffect.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (EventEditorPanel.this.selectedEventEffectPanel != null) {
               Container parent = EventEditorPanel.this.selectedEventEffectPanel.getParent();
               if (parent == EventEditorPanel.this.eventEffectMasterPanel) {
                  EventEditorPanel.this.worldEvent.getEffects().remove(EventEditorPanel.this.selectedEventEffectPanel.getWorldEventEffect());
                  EventEditorPanel.this.eventEffectMasterPanel.remove(EventEditorPanel.this.selectedEventEffectPanel);
               } else {
                  parent.remove(EventEditorPanel.this.selectedEventEffectPanel);

                  while (!(parent instanceof EventEffectPanel)) {
                     parent = parent.getParent();
                  }

                  ((EventEffectPanel)parent).getWorldEventEffect().getSubEffects().remove(EventEditorPanel.this.selectedEventEffectPanel.getWorldEventEffect());
               }

               EventEditorPanel.this.selectedEventEffectPanel = null;
               EventEditorPanel.this.validate();
               EventEditorPanel.this.repaint();
            }
         }
      });
      this.saveButton = new JButton(TextUtil.t("eventEditor.save"));
      buttonPanel.add(this.saveButton, "8, 1, right, default");
      this.saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EventAndQuestFileLoader.getInstance().save(EventEditorPanel.this.worldEvent);
         }
      });
      JScrollPane scrollPane = new JScrollPane();
      this.add(scrollPane, "1, 2, fill, fill");
      this.mainPanel = new JPanel();
      scrollPane.setViewportView(this.mainPanel);
      this.mainPanel
         .setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("default:grow")},
               new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("default:grow")}
            )
         );
      this.triggerListPanel = new TriggerListPanel(this.worldEvent);
      this.mainPanel.add(this.triggerListPanel, "1, 1, left, fill");
      this.eventEffectMasterPanel = new JPanel();
      this.eventEffectMasterPanel.setLayout(new BoxLayout(this.eventEffectMasterPanel, 1));
      this.mainPanel.add(this.eventEffectMasterPanel, "1, 3, fill, fill");

      for (WorldEventEffectType worldEventEffectType : WorldEventEffectType.values()) {
         worldEventComboBox.addItem(worldEventEffectType);
      }

      for (WorldEventEffect worldEventEffect : this.worldEvent.getEffects()) {
         this.addEventEffectPanel(worldEventEffect);
      }
   }

   public void addEventEffectPanel(WorldEventEffect worldEventEffect) {
      EventEffectPanel eventEffectPanel = new EventEffectPanel(worldEventEffect, this.worldEvent, new EventEditorPanel.MyMouseListener());
      this.eventEffectMasterPanel.add(eventEffectPanel);
   }

   public void setSelected(EventEffectPanel eventEffectPanel) {
      if (this.selectedEventEffectPanel != null) {
         this.selectedEventEffectPanel.setSelected(false);
      }

      this.selectedEventEffectPanel = eventEffectPanel;
      eventEffectPanel.setSelected(true);
      this.validate();
      this.repaint();
   }

   private class MyMouseListener extends MouseAdapter {
      private MyMouseListener() {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         if (e.getSource() instanceof EventEffectPanel) {
            EventEffectPanel newPanel = (EventEffectPanel)e.getSource();
            EventEditorPanel.this.setSelected(newPanel);
         }
      }
   }
}
