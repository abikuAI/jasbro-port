/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.ChildCareRequirement;
import jasbro.game.world.xml.ActivityRequirementParser;
import org.w3c.dom.Element;

public class ChildCareRequirementParser
implements ActivityRequirementParser {
    @Override
    public ActivityRequirement parse(Element requirementElement) {
        return new ChildCareRequirement();
    }
}

