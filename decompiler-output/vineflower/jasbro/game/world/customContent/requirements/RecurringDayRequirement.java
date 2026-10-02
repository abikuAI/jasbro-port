package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.world.customContent.TriggerParent;

public class RecurringDayRequirement extends TriggerRequirement {
   private int everyXDays;
   private int offset;

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      if (this.everyXDays == 0) {
         return false;
      } else {
         int currentDay = Jasbro.getInstance().getData().getDay();
         if (currentDay == 0) {
            return false;
         } else {
            return currentDay < this.offset / this.everyXDays ? false : currentDay % this.everyXDays == this.offset % this.everyXDays;
         }
      }
   }

   public int getEveryXDays() {
      return this.everyXDays;
   }

   public void setEveryXDays(int everyXDays) {
      this.everyXDays = everyXDays;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int offset) {
      this.offset = offset;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.RECURRINGDAYREQUIREMENT;
   }
}
