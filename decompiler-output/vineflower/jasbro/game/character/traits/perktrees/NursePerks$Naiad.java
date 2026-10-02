package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.texts.TextUtil;

public final class NursePerks$Naiad extends TraitEffect {
   @Override
   public void handleEvent(MyEvent e, Charakter character, Trait trait) {
      if (e.getType() == EventType.ACTIVITY) {
         RunningActivity activity = (RunningActivity)e.getSource();
         if (activity instanceof Swim) {
            MessageData message = activity.getMessages().get(0);
            activity.getStatModifications().add(activity.new ModificationData(RunningActivity.TargetType.SINGLE, character, 0.15F, BaseAttributeTypes.STAMINA));
            message.addToMessage(TextUtil.t("swim.naiad", character));
         }
      }
   }
}
