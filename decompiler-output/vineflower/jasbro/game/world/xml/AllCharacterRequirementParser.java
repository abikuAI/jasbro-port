package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.AllCharacterRequirement;
import jasbro.game.world.RoomLoader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AllCharacterRequirementParser implements ActivityRequirementParser {
   @Override
   public ActivityRequirement parse(Element requirementElement) {
      Element charRequirementElement = null;
      NodeList children = requirementElement.getChildNodes();

      for (int i = 0; i < children.getLength(); i++) {
         Node n = children.item(i);
         if (n.getNodeType() == 1) {
            charRequirementElement = (Element)n;
            break;
         }
      }

      return new AllCharacterRequirement(RoomLoader.parseCharacterRequirement(charRequirementElement));
   }
}
