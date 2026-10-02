package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.customContent.TriggerParent;

public class UnlockRequirement extends TriggerRequirement {
   private UnlockObject unlockObject;

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      if (this.unlockObject == null) {
         return false;
      } else {
         return !this.unlockObject.isLocked() ? true : Jasbro.getInstance().getData().getUnlocks().getUnlockedObjects().contains(this.unlockObject);
      }
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.UNLOCKREQUIREMENT;
   }

   public UnlockObject getUnlockObject() {
      return this.unlockObject;
   }

   public void setUnlockObject(UnlockObject unlockObject) {
      this.unlockObject = unlockObject;
   }
}
