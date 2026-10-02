package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.List;

public class RefusedToWork extends RunningActivity {
   private Charakter causedByCharacter;
   private ActivityType originalActivity;

   @Override
   public MessageData getBaseMessage() {
      MessageData messageData = new MessageData();
      messageData.setMessage(TextUtil.t("refusedtowork." + this.originalActivity.toString(), this.causedByCharacter));
      messageData.setBackground(this.causedByCharacter.getBackground());
      messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.causedByCharacter));
      messageData.setPriorityMessage(true);
      return messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = super.getStatModifications();
      if (this.originalActivity != ActivityType.WHORE) {
         modifications.add(
            new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.causedByCharacter, -0.25F, BaseAttributeTypes.OBEDIENCE)
         );
      } else {
         modifications.add(
            new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.causedByCharacter, -0.08F, BaseAttributeTypes.OBEDIENCE)
         );
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.causedByCharacter, -0.3F, EssentialAttributes.MOTIVATION));
      return modifications;
   }

   public Charakter getCausedByCharacter() {
      return this.causedByCharacter;
   }

   public void setCausedByCharacter(Charakter causedByCharacter) {
      this.causedByCharacter = causedByCharacter;
   }

   public ActivityType getOriginalActivity() {
      return this.originalActivity;
   }

   public void setOriginalActivity(ActivityType activity) {
      this.originalActivity = activity;
   }
}
