package jasbro.gui.pages.subView;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.RoomSlot;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.GuiUtil;
import jasbro.gui.dnd.CanReceiveCharacterDrop;
import jasbro.gui.dnd.MyCharacterTransferHandler;
import jasbro.gui.dnd.ToleranceMouseListener;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;

public class LocationPanel extends MyImage implements MyEventListener, CanReceiveCharacterDrop {
   private CharacterLocation characterLocation;
   private JPanel characterPanel;
   private JList<ActivityDetails> activitySelection;
   private JPanel activityPanel;
   private JLabel locationStatusLabel;
   private JPanel locationInfoPanel;
   private JComboBox<SelectionData<?>> selectionDataComboBox;
   private JScrollPane activityScrollPane;
   private ListSelectionListener myListener;

   public LocationPanel() {
      this.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
      FormLayout formLayout = new FormLayout(
         new ColumnSpec[]{
            ColumnSpec.decode("1dlu:grow(10)"), ColumnSpec.decode("1dlu:grow(10)"), ColumnSpec.decode("1dlu:grow(10)"), ColumnSpec.decode("25dlu:grow")
         },
         new RowSpec[]{RowSpec.decode("10dlu"), RowSpec.decode("1dlu:grow")}
      );
      this.setLayout(formLayout);
      this.locationStatusLabel = new LocationPanel.LocationStatusLabel();
      this.locationStatusLabel.setOpaque(true);
      this.locationStatusLabel.setHorizontalAlignment(11);
      this.locationStatusLabel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(139, 69, 19)));
      this.locationStatusLabel.setBackground(new Color(255, 255, 204));
      this.add(this.locationStatusLabel, "1,1,3,1,fill,fill");
      this.locationInfoPanel = new JPanel();
      this.locationInfoPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(139, 69, 19)));
      this.locationInfoPanel.setBackground(new Color(255, 255, 204));
      this.add(this.locationInfoPanel, "4, 1, fill, fill");
      this.locationInfoPanel.setLayout(new GridLayout(1, 0, 2, 2));
      this.characterPanel = new JPanel();
      this.characterPanel.setOpaque(false);
      this.add(this.characterPanel, "1, 2, 3, 1, fill, fill");
      this.characterPanel.setLayout(new MigLayout("", "[center]", "[center]"));
      this.characterPanel.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
         @Override
         public void ancestorResized(HierarchyEvent e) {
            LocationPanel.this.characterPanel.revalidate();
         }
      });
      this.activityPanel = new JPanel();
      this.activityPanel.setOpaque(false);
      this.activityPanel.setBackground(new Color(255, 255, 204));
      this.activityPanel.setBorder(new MatteBorder(0, 1, 0, 0, new Color(139, 69, 19)));
      this.add(this.activityPanel, "4, 2, fill, fill");
      this.activityPanel
         .setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("1px"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("1px")},
               new RowSpec[]{
                  RowSpec.decode("1px"), RowSpec.decode("top:default"), RowSpec.decode("2dlu:grow"), RowSpec.decode("top:10dlu"), RowSpec.decode("1px")
               }
            )
         );
      this.setBackground(new Color(1.0F, 1.0F, 0.9F, 0.5F));
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            LocationPanel.this.redoLayout();
         }
      });
   }

   public LocationPanel(CharacterLocation characterLocation) {
      this();
      this.setCharacterLocation(characterLocation);
   }

   public CharacterLocation getCharacterLocation() {
      return this.characterLocation;
   }

   public void setCharacterLocation(CharacterLocation characterLocation) {
      this.characterLocation = characterLocation;
      characterLocation.addListener(this);
      this.init();
   }

   private void init() {
      this.characterPanel.removeAll();
      this.activityPanel.removeAll();
      if (this.characterLocation != null) {
         List<Charakter> characters = this.characterLocation.getCurrentUsage().getCharacters();
         int width = this.getWidth();
         if (width == 0) {
            width = Jasbro.getInstance().getGui().getWidth() / 5;
         }

         for (int i = 0; i < characters.size(); i++) {
            Charakter character = characters.get(i);
            final CharacterShortView characterView = new CharacterShortView(character);
            if (characters.size() > 4 && i == 2) {
               this.characterPanel.add(characterView);
            } else {
               this.characterPanel.add(characterView);
            }

            characterView.setTransferHandler(new MyCharacterTransferHandler());
            ToleranceMouseListener tml = new ToleranceMouseListener() {
               @Override
               public void mouseDragged(MouseEvent e) {
                  super.mouseDragged(e);
                  if (!e.isConsumed()) {
                     TransferHandler handle = characterView.getTransferHandler();
                     handle.exportAsDrag(characterView, e, 1073741824);
                  }
               }
            };
            characterView.addMouseMotionListener(tml);
            characterView.addMouseListener(tml);
         }

         this.activityScrollPane = new JScrollPane(22, 31);
         this.activityScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, -1));
         this.activityPanel.add(this.activityScrollPane, "2, 2");
         this.activityScrollPane.setOpaque(false);
         this.activityScrollPane.setBorder(null);
         this.activityScrollPane.getViewport().setOpaque(false);
         this.activitySelection = new JList<>();
         this.activitySelection.setOpaque(false);
         this.activityScrollPane.setViewportView(this.activitySelection);
         this.activitySelection
            .setCellRenderer(
               new DefaultListCellRenderer() {
                  @Override
                  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
                     ActivityDetails details = (ActivityDetails)value;
                     label.setText(details.getText());
                     if (details.getDescription() != null) {
                        label.setToolTipText(TextUtil.htmlPreformatted(details.getDescription()));
                     } else {
                        label.setToolTipText("");
                     }

                     label.setOpaque(true);
                     label.setPreferredSize(label.getPreferredSize());
                     if (!isSelected) {
                        label.setBackground(new Color(255, 255, 204));
                     }

                     for (FontMetrics metrics = label.getFontMetrics(label.getFont());
                        LocationPanel.this.activitySelection.getWidth() != 0
                           && metrics.stringWidth(label.getText()) > LocationPanel.this.activitySelection.getWidth() - 4;
                        metrics = label.getFontMetrics(label.getFont())
                     ) {
                        Font font = label.getFont().deriveFont(label.getFont().getSize() - 1.0F);
                        label.setFont(font);
                     }

                     return label;
                  }
               }
            );
         this.activityScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
               LocationPanel.this.activitySelection.setPreferredSize(null);
               Dimension preferredSize = LocationPanel.this.activitySelection.getPreferredSize();
               preferredSize.width = LocationPanel.this.activityScrollPane.getWidth() + 5;
               LocationPanel.this.activitySelection.setPreferredSize(preferredSize);
               LocationPanel.this.repaint();
            }
         });
         this.myListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               LocationPanel.this.characterLocation.setSelectedActivityDetails(LocationPanel.this.activitySelection.getSelectedValue());
               LocationPanel.this.initSelectionComboBox();
            }
         };
         this.characterLocation.addListener(new MyEventListener() {
            private boolean updateInProgress = false;

            @Override
            public void handleEvent(MyEvent e) {
               if (!this.updateInProgress) {
                  this.updateInProgress = true;
                  SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                        LocationPanel.this.updateActivitySelectionList();
                        LocationPanel.this.repaint();
                        updateInProgress = false;
                     }
                  });
               }
            }
         });
         this.updateActivitySelectionList();
         if (this.characterLocation instanceof RoomSlot && !((RoomSlot)this.characterLocation).isAvailable()) {
            this.setBackgroundImage(null);
            this.setImage(this.characterLocation.getImage());
         } else if (this.characterLocation != null && !(this.characterLocation instanceof RoomSlot)) {
            this.setBackgroundImage(this.characterLocation.getImage());
         } else {
            this.setBackgroundImage(null);
         }
      }

      this.locationInfoPanel.removeAll();
      if (this.characterLocation.getDescription() != null) {
         MyImage infoImage = new MyImage(new ImageData("images/icons/info-pic.png"));
         infoImage.setToolTipText(TextUtil.htmlPreformatted(this.characterLocation.getDescription()));
         this.locationInfoPanel.add(infoImage);
      }

      this.setTransferHandler(new MyCharacterTransferHandler());
      this.initSelectionComboBox();
      this.redoLayout();
   }

   private void updateActivitySelectionList() {
      this.activitySelection.removeListSelectionListener(this.myListener);
      ActivityDetails selectedDetails = this.characterLocation.getSelectedActivityDetails();
      List<ActivityDetails> activityDetailList = this.characterLocation.getPossibleActivities();
      this.activitySelection.setListData(activityDetailList.toArray(new ActivityDetails[activityDetailList.size()]));
      this.activitySelection.setSelectedValue(selectedDetails, true);
      this.activitySelection.addListSelectionListener(this.myListener);
   }

   private void initSelectionComboBox() {
      List<SelectionData<?>> selectionDataList = this.characterLocation
         .getCurrentUsage()
         .getType()
         .getSelectionOptions(this.characterLocation.getCurrentUsage());
      if (selectionDataList != null) {
         if (this.selectionDataComboBox != null) {
            this.activityPanel.remove(this.selectionDataComboBox);
         }

         this.selectionDataComboBox = new JComboBox<>();
         this.activityPanel.add(this.selectionDataComboBox, "2, 4, fill, default");

         for (int i = 0; i < this.selectionDataComboBox.getComponentCount(); i++) {
            if (this.selectionDataComboBox.getComponent(i) instanceof JComponent) {
               ((JComponent)this.selectionDataComboBox.getComponent(i)).setBorder(GuiUtil.DEFAULTBORDER);
            }

            if (this.selectionDataComboBox.getComponent(i) instanceof AbstractButton) {
               ((AbstractButton)this.selectionDataComboBox.getComponent(i)).setBorder(GuiUtil.DEFAULTBORDER);
            }
         }

         this.selectionDataComboBox
            .setRenderer(
               new DefaultListCellRenderer() {
                  @Override
                  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
                     SelectionData<?> selectionData = (SelectionData<?>)value;
                     if (selectionData != null) {
                        label.setText(selectionData.getShortText());
                        if (selectionData.getTooltipText() != null) {
                           label.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText() + "\n" + selectionData.getTooltipText()));
                        } else {
                           label.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText()));
                        }
                     } else {
                        label.setText(" ");
                     }

                     for (FontMetrics metrics = label.getFontMetrics(label.getFont());
                        label.getWidth() != 0 && metrics.stringWidth(label.getText()) > label.getWidth();
                        metrics = label.getFontMetrics(label.getFont())
                     ) {
                        Font font = label.getFont().deriveFont(label.getFont().getSize() - 1.0F);
                        label.setFont(font);
                     }

                     return label;
                  }
               }
            );
         this.selectionDataComboBox.addItem(null);

         for (SelectionData<?> selectionData : selectionDataList) {
            this.selectionDataComboBox.addItem(selectionData);
         }

         this.selectionDataComboBox
            .addActionListener(
               new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                     SelectionData<?> selectionData = (SelectionData<?>)LocationPanel.this.selectionDataComboBox.getSelectedItem();
                     LocationPanel.this.characterLocation.getCurrentUsage().setSelectedOption(selectionData);
                     if (selectionData != null) {
                        if (selectionData.getTooltipText() != null) {
                           LocationPanel.this.selectionDataComboBox
                              .setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText() + "\n" + selectionData.getTooltipText()));
                        } else {
                           LocationPanel.this.selectionDataComboBox.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText()));
                        }
                     } else {
                        LocationPanel.this.selectionDataComboBox.setToolTipText("");
                     }
                  }
               }
            );
         this.selectionDataComboBox.setSelectedItem(this.characterLocation.getCurrentUsage().getSelectedOption());
      } else if (this.selectionDataComboBox != null) {
         this.activityPanel.remove(this.selectionDataComboBox);
      }

      this.activityPanel.validate();
      this.activityPanel.repaint();
   }

   private void redoLayout() {
      int components = this.characterPanel.getComponents().length;
      if (this.getWidth() < 300 && components > 2) {
         if (components < 5) {
            this.characterPanel.setLayout(new MigLayout("insets 1", "[center]1", "[center]1"));
         } else {
            this.characterPanel.setLayout(new MigLayout("insets 1, wrap 3", "[center]1", "[center]1"));
         }
      } else if (components >= 5 && this.characterPanel.getWidth() <= components * 80) {
         this.characterPanel.setLayout(new MigLayout("wrap 3", "[center]", "[center]"));
      } else {
         this.characterPanel.setLayout(new MigLayout("", "[center]", "[center]"));
      }

      this.characterPanel.validate();
      this.characterPanel.repaint();
   }

   @Override
   public synchronized void handleEvent(MyEvent e) {
      if (this.characterLocation.getCurrentUsage().getCharacters().size() != this.characterPanel.getComponents().length) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               LocationPanel.this.init();
               LocationPanel.this.validate();
               LocationPanel.this.repaint();
            }
         });
      }
   }

   @Override
   public void paintComponent(Graphics g) {
      if (this.getBackgroundImage() != null) {
         super.paintComponent(g);
      } else {
         g.setColor(this.getBackground());
         Insets insets = this.getInsets();
         g.fillRect(insets.left, insets.top, this.getWidth() - insets.right, this.getHeight() - insets.bottom);
         super.paintComponent(g);
      }
   }

   private class LocationStatusLabel extends JLabel {
      private LocationStatusLabel() {
      }

      @Override
      protected void paintComponent(Graphics g) {
         if (LocationPanel.this.characterLocation != null) {
            if (LocationPanel.this.characterLocation instanceof RoomSlot) {
               RoomSlot room = (RoomSlot)LocationPanel.this.characterLocation;
               if (room.getDownTime() > 0) {
                  this.setText(
                     LocationPanel.this.characterLocation.getName()
                        + "   "
                        + LocationPanel.this.characterLocation.getAmountPeople()
                        + " / "
                        + LocationPanel.this.characterLocation.getMaxPeople()
                        + " Availables in :"
                        + room.getDownTime()
                        + " days"
                  );
               } else {
                  this.setText(
                     LocationPanel.this.characterLocation.getName()
                        + "   "
                        + LocationPanel.this.characterLocation.getAmountPeople()
                        + " / "
                        + LocationPanel.this.characterLocation.getMaxPeople()
                        + " "
                  );
               }
            } else {
               this.setText(
                  LocationPanel.this.characterLocation.getName()
                     + "   "
                     + LocationPanel.this.characterLocation.getAmountPeople()
                     + " / "
                     + LocationPanel.this.characterLocation.getMaxPeople()
                     + " "
               );
            }
         }

         super.paintComponent(g);
      }
   }
}
