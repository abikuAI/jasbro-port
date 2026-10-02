package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import org.w3c.dom.Element;

public interface CharacterRequirementParser {
   CharacterRequirement parse(Element var1);
}
