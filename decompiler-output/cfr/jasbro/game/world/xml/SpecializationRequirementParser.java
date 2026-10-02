/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.SpecializationRequirement;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.world.xml.CharacterRequirementParser;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

public class SpecializationRequirementParser
implements CharacterRequirementParser {
    @Override
    public CharacterRequirement parse(Element requirementElement) {
        String specialization = requirementElement.getAttribute("specialization");
        Validate.notBlank((CharSequence)specialization);
        return new SpecializationRequirement(SpecializationType.valueOf(specialization));
    }
}

