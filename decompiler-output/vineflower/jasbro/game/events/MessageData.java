package jasbro.game.events;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageData {
   private static final Logger log = LogManager.getLogger(MessageData.class);
   private List<ImageData> images = new ArrayList<>();
   private String message = "";
   private ImageData background;
   private boolean priorityMessage;
   private List<AttributeModification> attributeModifications = new ArrayList<>();
   private Object messageGroupObject;
   private List<Future<MessageData>> futures;
   private List<Integer> futureTextPosition;

   public MessageData() {
   }

   public MessageData(String message, ImageData image, ImageData background) {
      if (image != null) {
         this.images.add(image);
      }

      this.addToMessage(message);
      this.background = background;
   }

   public MessageData(String message, ImageTag tag, Charakter character) {
      this(message, ImageUtil.getInstance().getImageDataByTag(tag, character), character.getBackground());
   }

   public MessageData(String message, ImageTag tag, Charakter character, boolean priorityMessage) {
      this(message, tag, character);
      this.priorityMessage = priorityMessage;
   }

   public MessageData(String message, ImageData image, ImageData background, boolean priorityMessage) {
      this(message, image, background);
      this.priorityMessage = priorityMessage;
   }

   public MessageData(String message, ImageData image, ImageData image2, ImageData background) {
      this(message, image, background);
      this.images.add(image2);
   }

   public ImageData getImage() {
      return this.images.size() > 0 ? this.images.get(0) : null;
   }

   public void setImage(ImageData image) {
      if (this.images.size() == 1) {
         this.images.clear();
      }

      this.images.add(0, image);
   }

   public String getMessage() {
      this.getCallableData();
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
      this.addToMessage("");
   }

   public ImageData getBackground() {
      return this.background;
   }

   public void setBackground(ImageData background) {
      this.background = background;
   }

   public void addToMessage(String message) {
      this.message = this.message + message;
      if (this.message.length() > 0 && this.message.charAt(this.message.length() - 1) != '\n' && this.message.charAt(this.message.length() - 1) != ' ') {
         this.message = this.message + " ";
      }
   }

   public void addToMessage(Integer location, String message) {
      String tmpMessage = this.message.substring(location, this.message.length());
      this.message = this.message.substring(0, location);
      this.addToMessage(message);
      this.addToMessage(tmpMessage);
   }

   public ImageData getImage2() {
      return this.images.size() > 1 ? this.images.get(1) : null;
   }

   public void setImage2(ImageData image2) {
      if (this.images.size() > 0) {
         this.images.add(1, image2);
      } else {
         this.images.add(image2);
      }
   }

   public boolean isPriorityMessage() {
      return this.priorityMessage;
   }

   public void setPriorityMessage(boolean priorityMessage) {
      this.priorityMessage = priorityMessage;
   }

   public List<AttributeModification> getAttributeModifications() {
      return this.attributeModifications;
   }

   public void setAttributeModifications(List<AttributeModification> attributeModifications) {
      this.attributeModifications = attributeModifications;
   }

   public MessageInterface createMessageScreen() {
      return new MessageScreen(this);
   }

   public Object getMessageGroupObject() {
      return this.messageGroupObject;
   }

   public void setMessageGroupObject(Object messageGroupObject) {
      this.messageGroupObject = messageGroupObject;
   }

   public void addFuture(Future<MessageData> future) {
      if (this.futures == null) {
         this.futures = new ArrayList<>();
         this.futureTextPosition = new ArrayList<>();
      }

      this.futures.add(future);
      this.futureTextPosition.add(this.message.length());
   }

   public void getCallableData() {
      if (this.futures != null) {
         for (int i = 0; i < this.futures.size(); i++) {
            Future<MessageData> future = this.futures.get(i);

            try {
               MessageData changedData = future.get();
               if (changedData != null) {
                  if (changedData.getMessage() != null) {
                     this.addToMessage(this.futureTextPosition.get(i), changedData.getMessage());
                  }

                  this.images.addAll(changedData.images);
                  if (changedData.getBackground() != null) {
                     this.background = changedData.getBackground();
                  }
               }
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
               log.error("Error while collecting messagedata", e);
            }
         }

         this.futures = null;
      }
   }

   public List<ImageData> getImages() {
      while (this.images.contains(null)) {
         this.images.remove(null);
      }

      return this.images;
   }

   public void addImage(ImageData image) {
      this.getImages().add(image);
   }
}
