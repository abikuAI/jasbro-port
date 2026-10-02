package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.customContent.TriggerParent;

public class LocationTypeRequirement extends TriggerRequirement {
   private LocationTypeInterface locationType;

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      LocationTypeInterface locationTypeInterface = triggerParent.getLocation();
      return locationTypeInterface == null ? false : locationTypeInterface == this.locationType;
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.LOCATIONTYPEREQUIREMENT;
   }

   public LocationTypeInterface getLocationType() {
      return this.locationType;
   }

   public void setLocationType(LocationTypeInterface locationType) {
      this.locationType = locationType;
   }
}
