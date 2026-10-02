package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldEventSimpleMessage extends WorldEventEffect {
   private static final Logger log = LogManager.getLogger(WorldEventSimpleMessage.class);
   private String message;
   private List<ImageSelection> images;
   private ImageSelection background;
   private boolean importantMessage = true;

   public WorldEventSimpleMessage() {
      this.images = new ArrayList<>();
      this.background = new ImageSelection();
      this.background.setBackground(true);
   }

   @Override
   public void perform(WorldEvent worldEvent) throws EvalError {
      MessageData messageData = new MessageData();
      if (this.message != null) {
         messageData.addToMessage(TextUtil.getInstance().applyTemplates(this.message, worldEvent.getPeople(), worldEvent.generateAttributeMap()));
      }

      for (ImageSelection imageSelection : this.images) {
         messageData.addImage(imageSelection.getImageData(worldEvent));
      }

      messageData.setBackground(this.background.getImageData(worldEvent));
      messageData.setPriorityMessage(this.importantMessage);
      List<AttributeModification> modifications = (List<AttributeModification>)worldEvent.getAttribute(WorldEvent.WorldEventVariables.attributemodifications);
      if (modifications != null) {
         messageData.setAttributeModifications(modifications);
         worldEvent.putAttribute(WorldEvent.WorldEventVariables.attributemodifications, null);
      }

      log.debug(messageData.getMessage());
      if (worldEvent.getActivity() != null
         && !worldEvent.getActivity().isAbort()
         && (worldEvent.getEvent() == null || worldEvent.getEvent().getType() != EventType.ACTIVITYFINISHED)) {
         worldEvent.getActivity().getMessages().add(messageData);
      } else {
         messageData.createMessageScreen();
      }
   }

   @Override
   public WorldEventEffectType getType() {
      return WorldEventEffectType.MESSAGE;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public List<ImageSelection> getImages() {
      return this.images;
   }

   public void setImages(List<ImageSelection> images) {
      this.images = images;
   }

   public ImageSelection getBackground() {
      return this.background;
   }

   public void setBackground(ImageSelection background) {
      this.background = background;
   }

   public boolean isImportantMessage() {
      return this.importantMessage;
   }

   public void setImportantMessage(boolean importantMessage) {
      this.importantMessage = importantMessage;
   }
}
