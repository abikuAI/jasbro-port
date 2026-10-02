/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package jasbro.game.world.xml;

import jasbro.game.character.CharacterType;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.CharacterTypeRequirement;
import jasbro.game.world.xml.CharacterRequirementParser;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

public class CharacterTypeRequirementParser
implements CharacterRequirementParser {
    @Override
    public CharacterRequirement parse(Element requirementElement) {
        String type = requirementElement.getAttribute("char-type");
        Validate.notBlank((CharSequence)type);
        return new CharacterTypeRequirement(CharacterType.valueOf(type));
    }
}

