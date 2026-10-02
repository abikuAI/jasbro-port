package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;
import java.util.ArrayList;
import java.util.List;

public abstract class TriggerRequirement {
   public abstract boolean isValid(TriggerParent var1) throws EvalError;

   public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
      return false;
   }

   public List<TriggerRequirement> getSubRequirements() {
      return new ArrayList<>();
   }

   public abstract TriggerRequirementType getType();

   public enum Comparison {
      GREATERTHAN(">"),
      LESSTHAN("<"),
      EQUAL("=");

      private String displayName;

      Comparison(String displayName) {
         this.displayName = displayName;
      }

      public String getDisplayName() {
         return this.displayName;
      }
   }
}
