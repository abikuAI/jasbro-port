/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.MaximumOccupantRequirement;
import jasbro.game.world.xml.ActivityRequirementParser;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

public class MaximumOccupantRequirementParser
implements ActivityRequirementParser {
    @Override
    public ActivityRequirement parse(Element requirementElement) {
        String count = requirementElement.getAttribute("count");
        Validate.matchesPattern((CharSequence)count, (String)"[0-9]", (String)"Value '%s' for 'count' in 'min-occupant' does not match numeric pattern.", (Object[])new Object[]{count});
        int numericCount = Integer.parseInt(count);
        return new MaximumOccupantRequirement(numericCount);
    }
}

