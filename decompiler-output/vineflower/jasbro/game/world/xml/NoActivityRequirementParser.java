package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.NoActivityRequirement;
import org.w3c.dom.Element;

public class NoActivityRequirementParser implements ActivityRequirementParser {
   @Override
   public ActivityRequirement parse(Element requirementElement) {
      return new NoActivityRequirement();
   }
}
