/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.AllCharacterRequirement;
import jasbro.game.world.RoomLoader;
import jasbro.game.world.xml.ActivityRequirementParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AllCharacterRequirementParser
implements ActivityRequirementParser {
    @Override
    public ActivityRequirement parse(Element requirementElement) {
        Element charRequirementElement = null;
        NodeList children = requirementElement.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node n = children.item(i);
            if (n.getNodeType() != 1) continue;
            charRequirementElement = (Element)n;
            break;
        }
        return new AllCharacterRequirement(RoomLoader.parseCharacterRequirement(charRequirementElement));
    }
}

