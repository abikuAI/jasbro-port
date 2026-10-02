package jasbro.game.world.customContent.requirements;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeRequirement extends TriggerRequirement {
   private static final Logger log = LogManager.getLogger(CodeRequirement.class);
   private String code;

   @Override
   public boolean isValid(TriggerParent triggerParent) throws EvalError {
      if (this.code != null && !this.code.equals("")) {
         try {
            return (Boolean)triggerParent.getInterpreter().eval(this.code);
         } catch (EvalError e) {
            log.error("Error in this code: " + this.code);
            throw e;
         }
      } else {
         return false;
      }
   }

   @Override
   public TriggerRequirementType getType() {
      return TriggerRequirementType.CODEREQUIREMENT;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }
}
