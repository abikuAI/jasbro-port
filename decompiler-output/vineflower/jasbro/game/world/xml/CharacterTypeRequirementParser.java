package jasbro.game.world.xml;

import jasbro.game.character.CharacterType;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.CharacterTypeRequirement;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

public class CharacterTypeRequirementParser implements CharacterRequirementParser {
   @Override
   public CharacterRequirement parse(Element requirementElement) {
      String type = requirementElement.getAttribute("char-type");
      Validate.notBlank(type);
      return new CharacterTypeRequirement(CharacterType.valueOf(type));
   }
}
