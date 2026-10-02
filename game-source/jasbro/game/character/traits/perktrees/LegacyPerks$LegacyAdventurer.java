package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class LegacyPerks$LegacyAdventurer extends TraitEffect {
   @Override
   public void handleEvent(MyEvent e, Charakter character, Trait trait) {
      if (e.getType() == EventType.ACTIVITY) {
         RunningActivity activity = (RunningActivity)e.getSource();
         if (!(activity instanceof Whore)) {
            for (AttributeModification attributeModification : activity.getAttributeModifications()) {
               if (attributeModification.getAttributeType() == BaseAttributeTypes.COMMAND) {
                  float modification = attributeModification.getBaseAmount();
                  float change = Math.abs(modification) * -0.2F;
                  attributeModification.addModificator(change);
               }
            }
         }
      }
   }
}
