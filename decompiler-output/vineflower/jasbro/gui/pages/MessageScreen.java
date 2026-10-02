package jasbro.gui.pages;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class MessageScreen extends JPanel implements MessageInterface {
   private MyImage imageAreaBackground;
   private String text;
   private List<ImageData> images;
   private ImageData background;
   private JLabel attributeModificationArea;
   private boolean priorityMessage;
   private Object messageGroupObject;
   private JPanel buttonPanel;
   private MessageData messageData;
   private boolean initialized = false;

   private MessageScreen() {
      this.init();
   }

   public MessageScreen(String text, ImageData image, ImageData background) {
      this.getImages().add(image);
      this.initVariables(text, background, false);
      Jasbro.getInstance().getGui().addMessage(this);
   }

   public MessageScreen(String text, ImageData image, ImageData background, boolean priorityMessage) {
      this(text, image, background);
      this.priorityMessage = priorityMessage;
   }

   public MessageScreen(MessageData messageData) {
      this.messageData = messageData;
      Jasbro.getInstance().getGui().addMessage(this);
      this.messageGroupObject = messageData.getMessageGroupObject();
      this.priorityMessage = messageData.isPriorityMessage();
   }

   public void initVariables(String text, ImageData background, boolean priorityMessage) {
      this.text = text;
      this.background = background;
      this.priorityMessage = priorityMessage;
   }

   @Override
   public synchronized void init() {
      if (!this.initialized) {
         this.initialized = true;
         if (this.messageData != null) {
            this.initVariables(this.messageData.getMessage(), this.messageData.getBackground(), this.messageData.isPriorityMessage());
            this.images = this.messageData.getImages();
            this.messageGroupObject = this.messageData.getMessageGroupObject();
         }

         this.setBackground(new Color(240, 230, 140));
         this.setOpaque(false);
         this.setLayout(
            new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow(6)"), ColumnSpec.decode("1dlu:grow")},
               new RowSpec[]{RowSpec.decode("1dlu:grow(3)"), RowSpec.decode("1dlu:grow")}
            )
         );
         this.imageAreaBackground = new MyImage();
         this.imageAreaBackground.setBackgroundImage(this.background);
         this.imageAreaBackground.setFocusable(true);
         this.add(this.imageAreaBackground, "1, 1, 3, 1, fill, fill");
         FormLayout layout;
         if (this.images.size() == 1) {
            layout = new FormLayout(
               new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow(6)"), ColumnSpec.decode("1dlu:grow")},
               new RowSpec[]{RowSpec.decode("1dlu:grow")}
            );
            this.imageAreaBackground.setLayout(layout);
         } else {
            layout = new FormLayout(
               new ColumnSpec[]{FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("1dlu:grow(4)"), FormFactory.RELATED_GAP_COLSPEC},
               new RowSpec[]{RowSpec.decode("1dlu:grow")}
            );
            this.imageAreaBackground.setLayout(layout);
         }

         int i = 1;

         for (ImageData image : this.getImages()) {
            if (i > 1) {
               layout.insertColumn(i * 2 - 1, ColumnSpec.decode("1dlu:grow"));
               layout.insertColumn(i * 2, ColumnSpec.decode("1dlu:grow(4)"));
            }

            MyImage myImage = new MyImage();
            myImage.setImage(image);
            myImage.setFocusable(true);
            this.imageAreaBackground.add(myImage, 2 * i + ", 1, fill, fill");
            i++;
         }

         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (e.getButton() == 1) {
                  Jasbro.getInstance().getGui().removeLayer(MessageScreen.this);
               } else if (e.getButton() == 3) {
                  Jasbro.getInstance().getGui().closeAllMessages();
               }
            }

            @Override
            public void mousePressed(MouseEvent e) {
               if (e.getButton() == 1) {
                  Jasbro.getInstance().getGui().removeLayer(MessageScreen.this);
               }

               if (e.getButton() == 3) {
                  Jasbro.getInstance().getGui().closeAllMessages();
               }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               MessageScreen.this.requestFocus();
            }
         });
         final JScrollPane scrollPane = new JScrollPane(20, 31);
         scrollPane.setBackground(new Color(240, 230, 140));
         scrollPane.getViewport().setOpaque(false);
         scrollPane.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
         scrollPane.getViewport().addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
         this.add(scrollPane, "2, 2, fill, fill");
         JTextPane messageField = new JTextPane();
         messageField.setEditable(false);
         scrollPane.setViewportView(messageField);
         messageField.setOpaque(false);
         messageField.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
         messageField.setFont(new Font("Tahoma", 0, 18));
         StyledDocument doc = messageField.getStyledDocument();

         try {
            doc.insertString(doc.getLength(), this.text, null);
         } catch (BadLocationException e2) {
         }

         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               scrollPane.getVerticalScrollBar().setValue(0);
            }
         });
         this.buttonPanel = new JPanel();
         this.add(this.buttonPanel, "3, 2, fill, fill");
         this.buttonPanel
            .setLayout(
               new FormLayout(
                  new ColumnSpec[]{ColumnSpec.decode("default:grow")},
                  new RowSpec[]{RowSpec.decode("min:none"), RowSpec.decode("min:none"), RowSpec.decode("min:none"), RowSpec.decode("default:grow")}
               )
            );
         JTextArea area = GuiUtil.getDefaultTextarea();
         area.setFocusable(false);
         area.setText(TextUtil.t("ui.clickAnywhere"));
         area.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
         area.setMinimumSize(null);
         this.buttonPanel.add(area, "1, 1, fill, top");
         JButton closeMessagesButton = new JButton(TextUtil.html(TextUtil.t("ui.closeMessages")));
         this.buttonPanel.add(closeMessagesButton, "1, 2, fill, top");
         closeMessagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                  Thread.sleep(400L);
               } catch (InterruptedException e1) {
               }

               Jasbro.getInstance().getGui().closeAllMessages();
            }
         });
         JScrollPane scrollPane2 = new JScrollPane();
         this.attributeModificationArea = new JLabel();
         this.attributeModificationArea.setOpaque(false);
         this.attributeModificationArea.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
         this.attributeModificationArea.setVerticalAlignment(1);
         scrollPane2.setViewportView(this.attributeModificationArea);
         scrollPane2.getViewport().setOpaque(false);
         this.buttonPanel.add(scrollPane2, "1, 4, fill, fill");
         this.setMessageGroupObject(this.messageGroupObject);
         this.setFocusable(true);
         this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               this.handleKeyEvent(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
               this.handleKeyEvent(e);
            }

            private void handleKeyEvent(KeyEvent e) {
               if (e.getKeyCode() == 10 || e.getKeyCode() == 32 || e.getKeyCode() == 39) {
                  Jasbro.getInstance().getGui().removeLayer(MessageScreen.this);
               } else if (e.getKeyCode() == 37) {
                  Jasbro.getInstance().getGui().restoreLastMessage();
               }
            }
         });
      }
   }

   private void setModifications(List<AttributeModification> modifications) {
      String text = "";

      for (AttributeModification modification : modifications) {
         if (modification.getRealModification() != 0.0F) {
            float value = Math.round(modification.getRealModification() * 100.0F) / 100.0F;
            text = text + modification.getTargetCharacter() + " " + modification.getAttributeType().getText() + " " + value + "\n";
         } else if (modification.getTargetCharacter().getAttribute(modification.getAttributeType()).isMaxed()
            && !(modification.getAttributeType() instanceof EssentialAttributes)) {
            text = text + "<font color=\"red\">" + modification.getTargetCharacter() + " " + modification.getAttributeType().getText() + " " + 0 + "</font>\n";
         }
      }

      text = TextUtil.htmlPreformatted(text);
      this.attributeModificationArea.setText(text);
      this.repaint();
   }

   @Override
   public boolean isPriorityMessage() {
      return this.priorityMessage;
   }

   public void setPriorityMessage(boolean priorityMessage) {
      this.priorityMessage = priorityMessage;
   }

   @Override
   public Object getMessageGroupObject() {
      return this.messageGroupObject;
   }

   @Override
   public void setMessageGroupObject(Object messageGroupObject) {
      this.messageGroupObject = messageGroupObject;
      if (messageGroupObject != null && this.buttonPanel != null) {
         Object[] arguments = new Object[]{messageGroupObject};
         JButton closeMessagesButton = new JButton(TextUtil.html(TextUtil.t("ui.closeMessagesLocation", arguments)));
         this.buttonPanel.add(closeMessagesButton, "1, 3, fill, top");
         closeMessagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                  Thread.sleep(200L);
               } catch (InterruptedException e1) {
               }

               Jasbro.getInstance().getGui().closeAllMessagesByLocation(MessageScreen.this.getMessageGroupObject());
            }
         });
         this.validate();
      }
   }

   private List<ImageData> getImages() {
      if (this.images == null) {
         this.images = new ArrayList<>();
      }

      return this.images;
   }

   @Override
   public void setVisible(boolean visibility) {
      super.setVisible(visibility);
      if (visibility) {
         this.requestFocus();
         if (this.messageData != null && this.attributeModificationArea.getText().equals("")) {
            this.setModifications(this.messageData.getAttributeModifications());
         }
      }
   }
}
