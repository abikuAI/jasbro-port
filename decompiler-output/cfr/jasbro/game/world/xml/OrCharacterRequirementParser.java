/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.OrCharacterRequirement;
import jasbro.game.world.RoomLoader;
import jasbro.game.world.xml.CharacterRequirementParser;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OrCharacterRequirementParser
implements CharacterRequirementParser {
    @Override
    public CharacterRequirement parse(Element requirementElement) {
        ArrayList<CharacterRequirement> requirements = new ArrayList<CharacterRequirement>();
        NodeList children = requirementElement.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node n = children.item(i);
            if (n.getNodeType() != 1) continue;
            requirements.add(RoomLoader.parseCharacterRequirement((Element)n));
        }
        return new OrCharacterRequirement(requirements.toArray(new CharacterRequirement[requirements.size()]));
    }
}

