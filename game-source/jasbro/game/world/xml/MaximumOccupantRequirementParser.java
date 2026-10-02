package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.MaximumOccupantRequirement;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

public class MaximumOccupantRequirementParser implements ActivityRequirementParser {
   @Override
   public ActivityRequirement parse(Element requirementElement) {
      String count = requirementElement.getAttribute("count");
      Validate.matchesPattern(count, "[0-9]", "Value '%s' for 'count' in 'min-occupant' does not match numeric pattern.", new Object[]{count});
      int numericCount = Integer.parseInt(count);
      return new MaximumOccupantRequirement(numericCount);
   }
}
