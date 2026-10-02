package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.business.Fight;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.customContent.RapeEvent;

public final class NursePerks$Medic extends TraitEffect {
   @Override
   public void handleEvent(MyEvent e, Charakter character, Trait trait) {
      if (e.getType() == EventType.ACTIVITYPERFORMED) {
         RunningActivity activity = (RunningActivity)e.getSource();
         if (activity instanceof RapeEvent || activity instanceof Fight) {
            for (AttributeModification modification : activity.getAttributeModifications()) {
               if (modification.getAttributeType() == EssentialAttributes.HEALTH && modification.getBaseAmount() < 0.0F) {
                  float reduceDamage = Math.min(character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 800.0F, 0.5F);
                  modification.setBaseAmount(modification.getBaseAmount() * (1.0F - reduceDamage));
               }
            }
         }
      }
   }
}
