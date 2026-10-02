/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.AndActivityRequirement;
import jasbro.game.world.RoomLoader;
import jasbro.game.world.xml.ActivityRequirementParser;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AndActivityRequirementParser
implements ActivityRequirementParser {
    @Override
    public ActivityRequirement parse(Element requirementElement) {
        ArrayList<ActivityRequirement> requirements = new ArrayList<ActivityRequirement>();
        NodeList children = requirementElement.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node n = children.item(i);
            if (n.getNodeType() != 1) continue;
            requirements.add(RoomLoader.parseActivityRequirement((Element)n));
        }
        return new AndActivityRequirement(requirements.toArray(new ActivityRequirement[requirements.size()]));
    }
}

